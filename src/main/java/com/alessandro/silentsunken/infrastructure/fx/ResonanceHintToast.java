package com.alessandro.silentsunken.infrastructure.fx;

import com.alessandro.silentsunken.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;

import java.util.List;

@NotNullParamsAndMethodsReturn
public class ResonanceHintToast implements Toast {
    private static final long DISPLAY_DURATION_MS = 4000L;
    private static final int WIDTH = 204;
    private static final int PADDING = 6;
    private static final int LINE_HEIGHT = 10;
    private static final int ICON_SIZE = 18;
    private static final int ICON_GAP = 2;
    private static final int TEXT_X = PADDING + ICON_SIZE + 4;

    private static final int BACKGROUND_COLOR = 0xE6152A1E;
    private static final int BORDER_COLOR = 0xFF3F6B4A;
    private static final int TITLE_COLOR = 0xFFB9E6B0;
    private static final int TEXT_COLOR = 0xFFDCEFDC;

    private static final Object TOKEN = new Object();

    private Component title;
    private List<FormattedCharSequence> wrappedLines;
    private List<ItemStack> items;
    private int height;

    private Visibility wantedVisibility = Visibility.HIDE;
    private long lastChanged;
    private boolean changed;

    private ResonanceHintToast(Component title, Component description, List<ItemStack> items) {
        reset(title, description, items);
    }

    public static void show(ToastManager toastManager, Component title, Component description, List<ItemStack> items) {
        var existing = toastManager.getToast(ResonanceHintToast.class, TOKEN);
        if (existing != null) {
            existing.reset(title, description, items);
        } else {
            toastManager.addToast(new ResonanceHintToast(title, description, items));
        }
    }

    private void reset(Component title, Component description, List<ItemStack> items) {
        this.title = title;
        this.items = List.copyOf(items);

        var font = Minecraft.getInstance().font;
        this.wrappedLines = font.split(description, WIDTH - TEXT_X - PADDING);

        var textBlockHeight = LINE_HEIGHT + (wrappedLines.size() * LINE_HEIGHT);
        var iconsBlockHeight = this.items.isEmpty() ? 0 : (this.items.size() * ICON_SIZE) + (Math.max(0, this.items.size() - 1) * ICON_GAP);
        this.height = PADDING + Math.max(textBlockHeight, iconsBlockHeight) + PADDING;

        this.changed = true;
    }

    @Override
    public int width() {
        return WIDTH;
    }

    @Override
    public int height() {
        return height;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, Font font, long fullyVisibleForMs) {
        graphics.fill(0, 0, width(), height(), BACKGROUND_COLOR);
        graphics.outline(0, 0, width(), height(), BORDER_COLOR);

        var textY = PADDING;
        graphics.text(font, title, TEXT_X, textY, TITLE_COLOR, false);
        textY += LINE_HEIGHT;

        for (var line : wrappedLines) {
            graphics.text(font, line, TEXT_X, textY, TEXT_COLOR, false);
            textY += LINE_HEIGHT;
        }

        var iconY = PADDING;
        for (var stack : items) {
            graphics.fakeItem(stack, PADDING, iconY);
            iconY += ICON_SIZE + ICON_GAP;
        }
    }

    @Override
    public Visibility getWantedVisibility() {
        return wantedVisibility;
    }

    @Override
    public void update(ToastManager toastManager, long fullyVisibleForMs) {
        if (changed) {
            lastChanged = fullyVisibleForMs;
            changed = false;
        }
        var timeSinceUpdate = fullyVisibleForMs - lastChanged;
        wantedVisibility = timeSinceUpdate < DISPLAY_DURATION_MS ? Visibility.SHOW : Visibility.HIDE;
    }

    @Override
    public Object getToken() {
        return TOKEN;
    }

    @Override
    public float yPos(int firstSlotIndex) {
        return firstSlotIndex * SLOT_HEIGHT;
    }
}

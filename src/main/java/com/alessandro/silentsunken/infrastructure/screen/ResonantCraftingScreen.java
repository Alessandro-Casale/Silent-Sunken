package com.alessandro.silentsunken.infrastructure.screen;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.nullability.NotNullParams;
import com.alessandro.silentsunken.infrastructure.inventory.ResonantCraftingMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@NotNullParams
@OnlyIn(Dist.CLIENT)
public class ResonantCraftingScreen extends AbstractContainerScreen<ResonantCraftingMenu> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(SilentSunken.MODID, "textures/gui/container/resonant_crafting_table.png");

    public ResonantCraftingScreen(ResonantCraftingMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 166);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 29;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        var xo = (this.width - this.imageWidth) / 2;
        var yo = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, xo, yo, 0.0f, 0.0f, this.imageWidth, this.imageHeight, 256, 256);
    }
}

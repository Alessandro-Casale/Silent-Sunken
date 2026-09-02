package com.alessandro.silentsunken.infrastructure.network.packet;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.fx.Position;
import com.alessandro.silentsunken.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.silentsunken.infrastructure.codec.SoundHintDefinition;
import com.alessandro.silentsunken.infrastructure.fx.ResonanceHintToast;
import com.alessandro.silentsunken.infrastructure.network.SilentPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@NotNullParamsAndMethodsReturn
public record ShowHintToastS2C(String title, String instructions, Map<Position, Item> hintItems) implements SilentPacket {
    public static final Type<ShowHintToastS2C> TYPE = new Type<>(Identifier.fromNamespaceAndPath(SilentSunken.MODID, "show_hint_toast_s2c"));

    public ShowHintToastS2C(int phase, int numberOfPhases, SoundHintDefinition definition) {
        this("Phase: " + phase + "/" + numberOfPhases, definition.instructions(), definition.hintItems());
    }

    public static final StreamCodec<ByteBuf, ShowHintToastS2C> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, ShowHintToastS2C::title,
        ByteBufCodecs.STRING_UTF8, ShowHintToastS2C::instructions,
        ByteBufCodecs.map(HashMap::new, ByteBufCodecs.fromCodec(StringRepresentable.fromEnum(Position::values)), ByteBufCodecs.fromCodec(BuiltInRegistries.ITEM.byNameCodec())), ShowHintToastS2C::hintItems,
        ShowHintToastS2C::new
    );

    private static final Component GENERIC_TITLE = Component.translatable("gui.silentsunken.toast_title");
    private static final Component GENERIC_INSTRUCTIONS = Component.translatable("gui.silentsunken.instructions.generic");

    @Override
    public void run(IPayloadContext context) {
        var minecraft = Minecraft.getInstance();
        var toastTitle = title.isEmpty() ? GENERIC_TITLE : Component.literal(title);
        var description = instructions.isEmpty() ? GENERIC_INSTRUCTIONS : Component.literal(instructions);

        var items = new ArrayList<ItemStack>();
        if (!hintItems.isEmpty()) {
            var top = hintItems.getOrDefault(Position.TOP, null);
            if (top != null) { items.add(top.getDefaultInstance()); }

            var bottom = hintItems.getOrDefault(Position.BOTTOM, null);
            if (bottom != null) { items.add(bottom.getDefaultInstance()); }
        }

        ResonanceHintToast.show(minecraft.getToastManager(), toastTitle, description, items);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

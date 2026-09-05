package com.alessandro.silentsunken.infrastructure.codec;

import com.alessandro.silentsunken.api.codec.Validators;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;

import java.util.List;

public record SoundMaterialDefinition(SoundMaterial material, Item counterpartMaterial, int color, List<Item> corners, Item rawTablet, Item gildedTablet, int requiredGoldIngotsForConversion) {
    public static final Codec<SoundMaterialDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        StringRepresentable.fromEnum(SoundMaterial::values).fieldOf("material").forGetter(SoundMaterialDefinition::material),
        BuiltInRegistries.ITEM.byNameCodec().fieldOf("counterpart_material").forGetter(SoundMaterialDefinition::counterpartMaterial),
        ExtraCodecs.STRING_RGB_COLOR.fieldOf("color").forGetter(SoundMaterialDefinition::color),
        BuiltInRegistries.ITEM.byNameCodec().listOf().fieldOf("corners").validate(items -> Validators.listWithSize(items, 4)).forGetter(SoundMaterialDefinition::corners),
        BuiltInRegistries.ITEM.byNameCodec().fieldOf("raw_tablet").forGetter(SoundMaterialDefinition::rawTablet),
        BuiltInRegistries.ITEM.byNameCodec().fieldOf("gilded_tablet").forGetter(SoundMaterialDefinition::gildedTablet),
        Codec.INT.fieldOf("required_gold_ingots_for_conversion").forGetter(SoundMaterialDefinition::requiredGoldIngotsForConversion)
    ).apply(instance, SoundMaterialDefinition::new));

    public Item firstCorner() {
        return corners.getFirst();
    }

    public Item secondCorner() {
        return corners.get(1);
    }

    public Item thirdCorner() {
        return corners.get(2);
    }

    public Item fourthCorner() {
        return corners.getLast();
    }
}

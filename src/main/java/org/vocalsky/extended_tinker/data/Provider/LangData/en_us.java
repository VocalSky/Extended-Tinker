package org.vocalsky.extended_tinker.data.Provider.LangData;


import lombok.Getter;

import java.util.function.Function;

public class en_us implements ILang {
    enum LangData implements ILangData {
        ET_COMMON("itemGroup.extended_tinker.common_items", "Extended Tinker | Common Items"),
        ET_MG("itemGroup.extended_tinker.golem_items", "Extended Tinker | Golem Items"),
        ET_IAF("itemGroup.extended_tinker.iaf_items", "Extended Tinker | Iaf Items"),
        PATTERN_BRIDLE("pattern.extended_tinker.bridle", "Bridle"),
        BRIDLE_ITEM("item.extended_tinker.bridle", "Bridle"),
        BRIDLE_SAND_CAST("item.extended_tinker.bridle_sand_cast", "Bridle Sand Cast"),
        BRIDLE_RED_SAND_CAST("item.extended_tinker.bridle_red_sand_cast", "Bridle Red Sand Cast"),
        BRIDLE_CAST("item.extended_tinker.bridle_cast", "Bridle Gold Cast"),
        HORSE_ARMOR("item.extended_tinker.horse_armor", "Horse Armor"),
        HORSE_ARMOR_CHESTPLATE("item.extended_tinker.horse_armor_chestplate", "Horse Armor"),
        HORSE_ARMOR_CHESTPLATE_DESCRIPTION("item.extended_tinker.horse_armor_chestplate.description", "Horse Armor is a piece of armor that protects your horse."),
        PAINLESS_HORSEARMOR("modifier.extended_tinker.painless_horsearmor", "Painless"),
        PAINLESS_HORSEARMOR_FLAVOR("modifier.extended_tinker.painless_horsearmor.flavor", "Ignore the pain"),
        PAINLESS_HORSEARMOR_FLAVOR_DESCRIPTION("modifier.extended_tinker.painless_horsearmor.description", "When the player is riding, the horse will not stand due to injury."),
        ASONE_HORSEARMOR("modifier.extended_tinker.asone_horsearmor", "As one"),
        ASONE_HORSEARMOR_FLAVOR("modifier.extended_tinker.asone_horsearmor.flavor", "Born apart, die together."),
        ASONE_HORSEARMOR_DESCRIPTION("modifier.extended_tinker.asone_horsearmor.description", "You will take damage along with your mount."),
        FIRECRACK("item.extended_tinker.firecrack", "Firecrack"),
        FIRECRACK_DESCRIPTION("item.extended_tinker.firecrack.description", "Firecrack is a modular firework."),
        FLIGHT_FIRECRACK("modifier.extended_tinker.flight_firecrack", "Flight"),
        FLIGHT_FIRECRACK_FLAVOR("modifier.extended_tinker.flight_firecrack.flavor", "How long can it fly?"),
        FLIGHT_FIRECRACK_DESCRIPTION("modifier.extended_tinker.flight_firecrack.description", "Affects flight time."),
        STAR_FIRECRACK("modifier.extended_tinker.star_firecrack", "Firework Star"),
        STAR_FIRECRACK_FLAVOR("modifier.extended_tinker.star_firecrack.flavor", "Carrying Firework Stars"),
        STAR_FIRECRACK_DESCRIPTION("modifier.extended_tinker.star_firecrack.description", "Possessed the ability of the Firework Star."),
        GOLEM_ARMOR("gui.extended_tinker.golem_armor", "Golem Armor"),
        GOLEM_ARMOR_DESCRIPTION("gui.extended_tinker.golem_armor.description", "Golem Armor is a kind of armor customized for the metal golem."),
        GOLEM_HELMET("item.extended_tinker.golem_helmet", "Golem Armor Helmet"),
        GOLEM_HELMET_DESCRIPTION("item.extended_tinker.golem_helmet.description", "Customized for the metal golem.。"),
        GOLEM_CHESTPLATE("item.extended_tinker.golem_chestplate", "Golem Armor Chestplate"),
        GOLEM_CHESTPLATE_DESCRIPTION("item.extended_tinker.golem_chestplate.description", "Customized for the metal golem.。"),
        GOLEM_LEGGINGS("item.extended_tinker.golem_leggings", "Golem Armor Leggings"),
        GOLEM_LEGGINGS_DESCRIPTION("item.extended_tinker.golem_leggings.description", "Customized for the metal golem.。"),
        PATTERN_HELMET_GOLEM_PLATING("pattern.extended_tinker.helmet_golem_plating", "Golem Chestplate Plating"),
        HELMET_GOLEM_PLATING_DUMMY("item.extended_tinker.helmet_golem_plating_dummy", "Stone Golem Helmet Plating"),
        HELMET_GOLEM_PLATING("item.extended_tinker.helmet_golem_plating", "Golem Helmet Plating"),
        HELMET_GOLEM_PLATING_SAND_CAST("item.extended_tinker.helmet_golem_plating_sand_cast", "Golem Helmet Plating Sand Cast"),
        HELMET_GOLEM_PLATING_RED_SAND_CAST("item.extended_tinker.helmet_golem_plating_red_sand_cast", "Golem Helmet Plating Red Sand Cast"),
        HELMET_GOLEM_PLATING_CAST("item.extended_tinker.helmet_golem_plating_cast", "Golem Helmet Plating Gold Cast"),
        PATTERN_CHESTPLATE_GOLEM_PLATING("pattern.extended_tinker.chestplate_golem_plating", "Golem Chestplate Plating"),
        CHESTPLATE_GOLEM_PLATING_DUMMY("item.extended_tinker.chestplate_golem_plating_dummy", "Stone Golem Chestplate Plating"),
        CHESTPLATE_GOLEM_PLATING("item.extended_tinker.chestplate_golem_plating", "Golem Chestplate Plating"),
        CHESTPLATE_GOLEM_PLATING_SAND_CAST("item.extended_tinker.chestplate_golem_plating_sand_cast", "Golem Chestplate Plating Sand Cast"),
        CHESTPLATE_GOLEM_PLATING_RED_SAND_CAST("item.extended_tinker.chestplate_golem_plating_red_sand_cast", "Golem Chestplate Plating Red Sand Cast"),
        CHESTPLATE_GOLEM_PLATING_CAST("item.extended_tinker.chestplate_golem_plating_cast", "Golem Chestplate Plating Gold Cast"),
        PATTERN_LEGGINGS_GOLEM_PLATING("pattern.extended_tinker.leggings_golem_plating", "Stone Golem Leggings Plating"),
        LEGGINGS_GOLEM_PLATING_DUMMY("item.extended_tinker.leggings_golem_plating_dummy", "Golem Leggings Plating"),
        LEGGINGS_GOLEM_PLATING("item.extended_tinker.leggings_golem_plating", "Golem Leggings Plating"),
        LEGGINGS_GOLEM_PLATING_SAND_CAST("item.extended_tinker.leggings_golem_plating_sand_cast", "Golem Leggings Plating Sand Cast"),
        LEGGINGS_GOLEM_PLATING_RED_SAND_CAST("item.extended_tinker.leggings_golem_plating_red_sand_cast", "Golem Leggings Plating Red Sand Cast"),
        LEGGINGS_GOLEM_PLATING_CAST("item.extended_tinker.leggings_golem_plating_cast", "Golem Leggings Plating Gold Cast"),
        DRAGON_ARMOR_HEAD("item.extended_tinker.dragonarmor_head", "Dragon Armor"),
        DRAGON_ARMOR_BODY("item.extended_tinker.dragonarmor_body", "Dragon Armor"),
        DRAGON_ARMOR_NECK("item.extended_tinker.dragonarmor_neck", "Dragon Armor"),
        DRAGON_ARMOR_TAIL("item.extended_tinker.dragonarmor_tail", "Dragon Armor"),
        DRAGON_ARMOR_MATERIAL_IRON("material.extended_tinker.dragon_armor_iron", "Iron"),
        DRAGON_ARMOR_MATERIAL_COPPER("material.extended_tinker.dragon_armor_copper", "Copper"),
        DRAGON_ARMOR_MATERIAL_SILVER("material.extended_tinker.dragon_armor_silver", "Silver"),
        DRAGON_ARMOR_MATERIAL_GOLD("material.extended_tinker.dragon_armor_gold", "Gold"),
        DRAGON_ARMOR_MATERIAL_DIAMOND("material.extended_tinker.dragon_armor_diamond", "Diamond"),
        DRAGON_ARMOR_MATERIAL_FIRE("material.extended_tinker.dragon_armor_fire", "Fire Dragonsteel"),
        DRAGON_ARMOR_MATERIAL_ICE("material.extended_tinker.dragon_armor_ice", "Ice Dragonsteel"),
        DRAGON_ARMOR_MATERIAL_LIGHTNING("material.extended_tinker.dragon_armor_lightning", "Lightning Dragonsteel"),
        PART_DRAGON_ARMOR_CORE_HEAD("item.extended_tinker.dragon_armor_core_head", "Dragon Armor Core"),
        PART_DRAGON_ARMOR_CORE_BODY("item.extended_tinker.dragon_armor_core_body", "Dragon Armor Core"),
        PART_DRAGON_ARMOR_CORE_NECK("item.extended_tinker.dragon_armor_core_neck", "Dragon Armor Core"),
        PART_DRAGON_ARMOR_CORE_TAIL("item.extended_tinker.dragon_armor_core_tail", "Dragon Armor Core"),
        STAT_PLATING_HEAD("stat.extended_tinker.plating_head", "Head Plating"),
        STAT_PLATING_BODY("stat.extended_tinker.plating_body", "Body Plating"),
        STAT_PLATING_NECK("stat.extended_tinker.plating_neck", "Neck Plating"),
        STAT_PLATING_TAIL("stat.extended_tinker.plating_tail", "Tail Plating"),
        MAGNETIC_STORM_SURGE("modifier.extended_tinker.magnetic_storm_surge", "Magnetic Storm Surge"),
        MAGNETIC_STORM_SURGE_FLAVOR("modifier.extended_tinker.magnetic_storm_surge.flavor", "System Keraunos!"),
        MAGNETIC_STORM_SURGE_DESCRIPTION("modifier.extended_tinker.magnetic_storm_surge.description", "Every monster in the domain will face the majesty of the lightning."),
        BURNS_THE_SKY("modifier.extended_tinker.burns_the_sky", "Burns the sky"),
        BURNS_THE_SKY_FLAVOR("modifier.extended_tinker.burns_the_sky.flavor", "Turning into KFC!"),
        BURNS_THE_SKY_DESCRIPTION("modifier.extended_tinker.burns_the_sky.description", "Every monster in the domain will be burned by the fire."),
        PERMAFROST("modifier.extended_tinker.permafrost", "Permafrost"),
        PERMAFROST_FLAVOR("modifier.extended_tinker.permafrost.flavor", "Building ice sculptures!"),
        PERMAFROST_DESCRIPTION("modifier.extended_tinker.permafrost.description", "Every monster in the domain will be put into suspended animation.");

        @Getter
        private final String key;
        @Getter
        private final String value;

        LangData(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public String getLocale() {
        return "en_us";
    }

    @Override
    public void allTranslation(Function<ILangData, Boolean> translation) {
        for (LangData translate : LangData.values())
            if (!translation.apply(translate))
                throw new IllegalStateException("Failed to add translation key " + translate.getKey());
    }
}
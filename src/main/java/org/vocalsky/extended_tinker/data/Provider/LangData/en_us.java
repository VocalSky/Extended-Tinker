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
        DRAGON_ARMOR_IRON("item.extended_tinker.dragonarmor_iron", "Dragon Armor"),
        DRAGON_ARMOR_COPPER("item.extended_tinker.dragonarmor_copper", "Dragon Armor"),
        DRAGON_ARMOR_SILVER("item.extended_tinker.dragonarmor_silver", "Dragon Armor"),
        DRAGON_ARMOR_GOLD("item.extended_tinker.dragonarmor_gold", "Dragon Armor"),
        DRAGON_ARMOR_DIAMOND("item.extended_tinker.dragonarmor_diamond", "Dragon Armor"),
        DRAGON_ARMOR_FIRE("item.extended_tinker.dragonarmor_dragonsteel_fire", "Dragon Armor"),
        DRAGON_ARMOR_ICE("item.extended_tinker.dragonarmor_dragonsteel_ice", "Dragon Armor"),
        DRAGON_ARMOR_LIGHTNING("item.extended_tinker.dragonarmor_dragonsteel_lightning", "Dragon Armor"),
        DRAGON_ARMOR_MATERIAL_IRON("material.extended_tinker.dragon_armor_iron", "Iron"),
        DRAGON_ARMOR_MATERIAL_COPPER("material.extended_tinker.dragon_armor_copper", "Copper"),
        DRAGON_ARMOR_MATERIAL_SILVER("material.extended_tinker.dragon_armor_silver", "Silver"),
        DRAGON_ARMOR_MATERIAL_GOLD("material.extended_tinker.dragon_armor_gold", "Gold"),
        DRAGON_ARMOR_MATERIAL_DIAMOND("material.extended_tinker.dragon_armor_diamond", "Diamond"),
        DRAGON_ARMOR_MATERIAL_FIRE("material.extended_tinker.dragon_armor_fire", "Fire Dragonsteel"),
        DRAGON_ARMOR_MATERIAL_ICE("material.extended_tinker.dragon_armor_ice", "Ice Dragonsteel"),
        DRAGON_ARMOR_MATERIAL_LIGHTNING("material.extended_tinker.dragon_armor_lightning", "Lightning Dragonsteel"),
        STAT_DRAGON_ARMOR_IRON("stat.extended_tinker.dragonarmor_iron", "Dragon Armor Core"),
        STAT_DRAGON_ARMOR_COPPER("stat.extended_tinker.dragonarmor_copper", "Dragon Armor Core"),
        STAT_DRAGON_ARMOR_SILVER("stat.extended_tinker.dragonarmor_silver", "Dragon Armor Core"),
        STAT_DRAGON_ARMOR_GOLD("stat.extended_tinker.dragonarmor_gold", "Dragon Armor Core"),
        STAT_DRAGON_ARMOR_DIAMOND("stat.extended_tinker.dragonarmor_diamond", "Dragon Armor Core"),
        STAT_DRAGON_ARMOR_FIRE("stat.extended_tinker.dragonarmor_fire", "Dragon Armor Core"),
        STAT_DRAGON_ARMOR_ICE("stat.extended_tinker.dragonarmor_ice", "Dragon Armor Core"),
        STAT_DRAGON_ARMOR_LIGHTNING("stat.extended_tinker.dragonarmor_lightning", "Dragon Armor Core"),
        STAT_PLATING_HEAD("stat.extended_tinker.plating_head", "Head Plating"),
        STAT_PLATING_BODY("stat.extended_tinker.plating_body", "Body Plating"),
        STAT_PLATING_NECK("stat.extended_tinker.plating_neck", "Neck Plating"),
        STAT_PLATING_TAIL("stat.extended_tinker.plating_tail", "Tail Plating"),
        PART_DRAGON_ARMOR_COPPER_CORE_HEAD("item.extended_tinker.dragon_armor_copper_core_head", "Head Core"),
        PART_DRAGON_ARMOR_COPPER_CORE_BODY("item.extended_tinker.dragon_armor_copper_core_body", "Body Core"),
        PART_DRAGON_ARMOR_COPPER_CORE_NECK("item.extended_tinker.dragon_armor_copper_core_neck", "Neck Core"),
        PART_DRAGON_ARMOR_COPPER_CORE_TAIL("item.extended_tinker.dragon_armor_copper_core_tail", "Tail Core"),
        PART_DRAGON_ARMOR_SILVER_CORE_HEAD("item.extended_tinker.dragon_armor_silver_core_head", "Head Core"),
        PART_DRAGON_ARMOR_SILVER_CORE_BODY("item.extended_tinker.dragon_armor_silver_core_body", "Body Core"),
        PART_DRAGON_ARMOR_SILVER_CORE_NECK("item.extended_tinker.dragon_armor_silver_core_neck", "Neck Core"),
        PART_DRAGON_ARMOR_SILVER_CORE_TAIL("item.extended_tinker.dragon_armor_silver_core_tail", "Tail Core"),
        PART_DRAGON_ARMOR_IRON_CORE_HEAD("item.extended_tinker.dragon_armor_iron_core_head", "Head Core"),
        PART_DRAGON_ARMOR_IRON_CORE_BODY("item.extended_tinker.dragon_armor_iron_core_body", "Body Core"),
        PART_DRAGON_ARMOR_IRON_CORE_NECK("item.extended_tinker.dragon_armor_iron_core_neck", "Neck Core"),
        PART_DRAGON_ARMOR_IRON_CORE_TAIL("item.extended_tinker.dragon_armor_iron_core_tail", "Tail Core"),
        PART_DRAGON_ARMOR_GOLD_CORE_HEAD("item.extended_tinker.dragon_armor_gold_core_head", "Head Core"),
        PART_DRAGON_ARMOR_GOLD_CORE_BODY("item.extended_tinker.dragon_armor_gold_core_body", "Body Core"),
        PART_DRAGON_ARMOR_GOLD_CORE_NECK("item.extended_tinker.dragon_armor_gold_core_neck", "Neck Core"),
        PART_DRAGON_ARMOR_GOLD_CORE_TAIL("item.extended_tinker.dragon_armor_gold_core_tail", "Tail Core"),
        PART_DRAGON_ARMOR_DIAMOND_CORE_HEAD("item.extended_tinker.dragon_armor_diamond_core_head", "Head Core"),
        PART_DRAGON_ARMOR_DIAMOND_CORE_BODY("item.extended_tinker.dragon_armor_diamond_core_body", "Body Core"),
        PART_DRAGON_ARMOR_DIAMOND_CORE_NECK("item.extended_tinker.dragon_armor_diamond_core_neck", "Neck Core"),
        PART_DRAGON_ARMOR_DIAMOND_CORE_TAIL("item.extended_tinker.dragon_armor_diamond_core_tail", "Tail Core"),
        PART_DRAGON_ARMOR_FIRE_CORE_HEAD("item.extended_tinker.dragon_armor_dragonsteel_fire_core_head", "Dragonsteel Head Core"),
        PART_DRAGON_ARMOR_FIRE_CORE_BODY("item.extended_tinker.dragon_armor_dragonsteel_fire_core_body", "Dragonsteel Body Core"),
        PART_DRAGON_ARMOR_FIRE_CORE_NECK("item.extended_tinker.dragon_armor_dragonsteel_fire_core_neck", "Dragonsteel Neck Core"),
        PART_DRAGON_ARMOR_FIRE_CORE_TAIL("item.extended_tinker.dragon_armor_dragonsteel_fire_core_tail", "Dragonsteel Tail Core"),
        PART_DRAGON_ARMOR_ICE_CORE_HEAD("item.extended_tinker.dragon_armor_dragonsteel_ice_core_head", "Dragonsteel Head Core"),
        PART_DRAGON_ARMOR_ICE_CORE_BODY("item.extended_tinker.dragon_armor_dragonsteel_ice_core_body", "Dragonsteel Body Core"),
        PART_DRAGON_ARMOR_ICE_CORE_NECK("item.extended_tinker.dragon_armor_dragonsteel_ice_core_neck", "Dragonsteel Neck Core"),
        PART_DRAGON_ARMOR_ICE_CORE_TAIL("item.extended_tinker.dragon_armor_dragonsteel_ice_core_tail", "Dragonsteel Tail Core"),
        PART_DRAGON_ARMOR_LIGHTNING_CORE_HEAD("item.extended_tinker.dragon_armor_dragonsteel_lightning_core_head", "Dragonsteel Head Core"),
        PART_DRAGON_ARMOR_LIGHTNING_CORE_BODY("item.extended_tinker.dragon_armor_dragonsteel_lightning_core_body", "Dragonsteel Body Core"),
        PART_DRAGON_ARMOR_LIGHTNING_CORE_NECK("item.extended_tinker.dragon_armor_dragonsteel_lightning_core_neck", "Dragonsteel Neck Core"),
        PART_DRAGON_ARMOR_LIGHTNING_CORE_TAIL("item.extended_tinker.dragon_armor_dragonsteel_lightning_core_tail", "Dragonsteel Tail Core");

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
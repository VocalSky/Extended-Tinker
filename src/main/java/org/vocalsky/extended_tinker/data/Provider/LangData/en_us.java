package org.vocalsky.extended_tinker.data.Provider.LangData;


import lombok.Getter;

import java.util.function.Function;

public class en_us implements ILang {
    enum LangData implements ILangData {
        ET_COMMON("itemGroup.extended_tinker.common_items", "Extended Tinker | Common Items"),
        ET_MG("itemGroup.extended_tinker.golem_items", "Extended Tinker | Golem Items"),
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
        LEGGINGS_GOLEM_PLATING_CAST("item.extended_tinker.leggings_golem_plating_cast", "Golem Leggings Plating Gold Cast");

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
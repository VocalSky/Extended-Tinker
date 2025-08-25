package org.vocalsky.extended_tinker.data.Provider.LangData;


import lombok.Getter;

import java.util.function.Function;

public class zh_cn implements ILang {
    enum LangData implements ILangData {
        ET_COMMON("itemGroup.extended_tinker.common_items", "匠魂扩张 | 常规物品"),
        ET_MG("itemGroup.extended_tinker.golem_items", "匠魂扩张 | 傀儡物品"),
        PATTERN_BRIDLE("pattern.extended_tinker.bridle", "马笼头"),
        BRIDLE_ITEM("item.extended_tinker.bridle", "马笼头"),
        BRIDLE_SAND_CAST("item.extended_tinker.bridle_sand_cast", "马笼头沙子铸模"),
        BRIDLE_RED_SAND_CAST("item.extended_tinker.bridle_red_sand_cast", "马笼头红沙铸模"),
        BRIDLE_CAST("item.extended_tinker.bridle_cast", "马笼头金质铸模"),
        HORSE_ARMOR("item.extended_tinker.horse_armor", "马铠"),
        HORSE_ARMOR_CHESTPLATE("item.extended_tinker.horse_armor_chestplate", "马铠"),
        HORSE_ARMOR_CHESTPLATE_DESCRIPTION("item.extended_tinker.horse_armor_chestplate.description", "马铠是一件防具，能保护你的马。"),
        PAINLESS_HORSEARMOR("modifier.extended_tinker.painless_horsearmor", "无痛化"),
        PAINLESS_HORSEARMOR_FLAVOR("modifier.extended_tinker.painless_horsearmor.flavor", "忽视痛觉"),
        PAINLESS_HORSEARMOR_FLAVOR_DESCRIPTION("modifier.extended_tinker.painless_horsearmor.description", "当玩家乘骑时，马不会因为受伤而战立。"),
        ASONE_HORSEARMOR("modifier.extended_tinker.asone_horsearmor", "人马一体"),
        ASONE_HORSEARMOR_FLAVOR("modifier.extended_tinker.asone_horsearmor.flavor", "不求同年同月生，但求同年同月死"),
        ASONE_HORSEARMOR_DESCRIPTION("modifier.extended_tinker.asone_horsearmor.description", "当玩家乘骑时，马和玩家分摊伤害。"),
        FIRECRACK("item.extended_tinker.firecrack", "烟火"),
        FIRECRACK_DESCRIPTION("item.extended_tinker.firecrack.description", "烟火是一种模块化的工具。"),
        FLIGHT_FIRECRACK("modifier.extended_tinker.flight_firecrack", "飞行"),
        FLIGHT_FIRECRACK_FLAVOR("modifier.extended_tinker.flight_firecrack.flavor", "它能飞多久？"),
        FLIGHT_FIRECRACK_DESCRIPTION("modifier.extended_tinker.flight_firecrack.description", "影响飞行时间。"),
        STAR_FIRECRACK("modifier.extended_tinker.star_firecrack", "烟火之星"),
        STAR_FIRECRACK_FLAVOR("modifier.extended_tinker.star_firecrack.flavor", "搭载烟火之星"),
        STAR_FIRECRACK_DESCRIPTION("modifier.extended_tinker.star_firecrack.description", "拥有了烟火之星的能力。"),
        GOLEM_ARMOR("gui.extended_tinker.golem_armor", "傀儡装甲"),
        GOLEM_ARMOR_DESCRIPTION("gui.extended_tinker.golem_armor.description", "傀儡装甲是一种防具，为大型金属傀儡定制。"),
        GOLEM_HELMET("item.extended_tinker.golem_helmet", "傀儡装甲头盔"),
        GOLEM_HELMET_DESCRIPTION("item.extended_tinker.golem_helmet.description", "为大型金属傀儡定制。"),
        GOLEM_CHESTPLATE("item.extended_tinker.golem_chestplate", "傀儡装甲胸甲"),
        GOLEM_CHESTPLATE_DESCRIPTION("item.extended_tinker.golem_chestplate.description", "为大型金属傀儡定制。"),
        GOLEM_LEGGINGS("item.extended_tinker.golem_leggings", "傀儡装甲护腿"),
        GOLEM_LEGGINGS_DESCRIPTION("item.extended_tinker.golem_leggings.description", "为大型金属傀儡定制。"),
        PATTERN_HELMET_GOLEM_PLATING("pattern.extended_tinker.helmet_golem_plating", "傀儡头盔镶板"),
        HELMET_GOLEM_PLATING_DUMMY("item.extended_tinker.helmet_golem_plating_dummy", "石头傀儡头盔镶板"),
        HELMET_GOLEM_PLATING("item.extended_tinker.helmet_golem_plating", "傀儡头盔镶板"),
        HELMET_GOLEM_PLATING_SAND_CAST("item.extended_tinker.helmet_golem_plating_sand_cast", "傀儡头盔镶板沙子铸模"),
        HELMET_GOLEM_PLATING_RED_SAND_CAST("item.extended_tinker.helmet_golem_plating_red_sand_cast", "傀儡头盔镶板红沙铸模"),
        HELMET_GOLEM_PLATING_CAST("item.extended_tinker.helmet_golem_plating_cast", "傀儡头盔镶板金制铸模"),
        PATTERN_CHESTPLATE_GOLEM_PLATING("pattern.extended_tinker.chestplate_golem_plating", "傀儡胸甲镶板"),
        CHESTPLATE_GOLEM_PLATING_DUMMY("item.extended_tinker.chestplate_golem_plating_dummy", "石头傀儡胸甲镶板"),
        CHESTPLATE_GOLEM_PLATING("item.extended_tinker.chestplate_golem_plating", "傀儡胸甲镶板"),
        CHESTPLATE_GOLEM_PLATING_SAND_CAST("item.extended_tinker.chestplate_golem_plating_sand_cast", "傀儡胸甲镶板沙子铸模"),
        CHESTPLATE_GOLEM_PLATING_RED_SAND_CAST("item.extended_tinker.chestplate_golem_plating_red_sand_cast", "傀儡胸甲镶板红沙铸模"),
        CHESTPLATE_GOLEM_PLATING_CAST("item.extended_tinker.chestplate_golem_plating_cast", "傀儡胸甲镶板金制铸模"),
        PATTERN_LEGGINGS_GOLEM_PLATING("pattern.extended_tinker.leggings_golem_plating", "傀儡护腿镶板"),
        LEGGINGS_GOLEM_PLATING_DUMMY("item.extended_tinker.leggings_golem_plating_dummy", "石头傀儡护腿镶板"),
        LEGGINGS_GOLEM_PLATING("item.extended_tinker.leggings_golem_plating", "傀儡护腿镶板"),
        LEGGINGS_GOLEM_PLATING_SAND_CAST("item.extended_tinker.leggings_golem_plating_sand_cast", "傀儡护腿镶板沙子铸模"),
        LEGGINGS_GOLEM_PLATING_RED_SAND_CAST("item.extended_tinker.leggings_golem_plating_red_sand_cast", "傀儡护腿镶板红沙铸模"),
        LEGGINGS_GOLEM_PLATING_CAST("item.extended_tinker.leggings_golem_plating_cast", "傀儡护腿镶板金制铸模");

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
        return "zh_cn";
    }

    @Override
    public void allTranslation(Function<ILangData, Boolean> translation) {
        for (LangData translate : LangData.values())
            if (!translation.apply(translate))
                throw new IllegalStateException("Failed to add translation key " + translate.getKey());
    }
}
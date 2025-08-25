package org.vocalsky.extended_tinker.data.Provider.LangData;


import lombok.Getter;

import java.util.function.Function;

public class zh_tw implements ILang {
    enum LangData implements ILangData {
        ET_COMMON("itemGroup.extended_tinker.common_items", "匠魂擴張 | 常規物品"),
        ET_MG("itemGroup.extended_tinker.golem_items", "匠魂擴張 | 傀儡物品"),
        PATTERN_BRIDLE("pattern.extended_tinker.bridle", "馬籠頭"),
        BRIDLE_ITEM("item.extended_tinker.bridle", "馬籠頭"),
        BRIDLE_SAND_CAST("item.extended_tinker.bridle_sand_cast", "馬籠頭沙子鑄模"),
        BRIDLE_RED_SAND_CAST("item.extended_tinker.bridle_red_sand_cast", "馬籠頭紅沙鑄模"),
        BRIDLE_CAST("item.extended_tinker.bridle_cast", "馬籠頭金質鑄模"),
        HORSE_ARMOR("item.extended_tinker.horse_armor", "馬鎧"),
        HORSE_ARMOR_CHESTPLATE("item.extended_tinker.horse_armor_chestplate", "馬鎧"),
        HORSE_ARMOR_CHESTPLATE_DESCRIPTION("item.extended_tinker.horse_armor_chestplate.description", "馬鎧是一件防具，能保護你的馬。"),
        PAINLESS_HORSEARMOR("modifier.extended_tinker.painless_horsearmor", "無痛化"),
        PAINLESS_HORSEARMOR_FLAVOR("modifier.extended_tinker.painless_horsearmor.flavor", "忽視痛覺"),
        PAINLESS_HORSEARMOR_FLAVOR_DESCRIPTION("modifier.extended_tinker.painless_horsearmor.description", "當玩家乘騎時，馬不會因為受傷而戰立。"),
        ASONE_HORSEARMOR("modifier.extended_tinker.asone_horsearmor", "人馬一體"),
        ASONE_HORSEARMOR_FLAVOR("modifier.extended_tinker.asone_horsearmor.flavor", "不求同年同月生，但求同年同月死"),
        ASONE_HORSEARMOR_DESCRIPTION("modifier.extended_tinker.asone_horsearmor.description", "當玩家乘騎時，馬和玩家分攤傷害。"),
        FIRECRACK("item.extended_tinker.firecrack", "煙火"),
        FIRECRACK_DESCRIPTION("item.extended_tinker.firecrack.description", "煙火是一種模塊化的工具。"),
        FLIGHT_FIRECRACK("modifier.extended_tinker.flight_firecrack", "飛行"),
        FLIGHT_FIRECRACK_FLAVOR("modifier.extended_tinker.flight_firecrack.flavor", "它能飛多久？"),
        FLIGHT_FIRECRACK_DESCRIPTION("modifier.extended_tinker.flight_firecrack.description", "影響飛行時間。"),
        STAR_FIRECRACK("modifier.extended_tinker.star_firecrack", "煙火之星"),
        STAR_FIRECRACK_FLAVOR("modifier.extended_tinker.star_firecrack.flavor", "搭載煙火之星"),
        STAR_FIRECRACK_DESCRIPTION("modifier.extended_tinker.star_firecrack.description", "擁有了煙火之星的能力。"),
        GOLEM_ARMOR("gui.extended_tinker.golem_armor", "傀儡裝甲"),
        GOLEM_ARMOR_DESCRIPTION("gui.extended_tinker.golem_armor.description", "傀儡裝甲是一種防具，為大型金屬傀儡訂製。"),
        GOLEM_HELMET("item.extended_tinker.golem_helmet", "傀儡裝甲頭盔"),
        GOLEM_HELMET_DESCRIPTION("item.extended_tinker.golem_helmet.description", "為大型金屬傀儡訂製。"),
        GOLEM_CHESTPLATE("item.extended_tinker.golem_chestplate", "傀儡裝甲胸甲"),
        GOLEM_CHESTPLATE_DESCRIPTION("item.extended_tinker.golem_chestplate.description", "為大型金屬傀儡訂製。"),
        GOLEM_LEGGINGS("item.extended_tinker.golem_leggings", "傀儡裝甲護腿"),
        GOLEM_LEGGINGS_DESCRIPTION("item.extended_tinker.golem_leggings.description", "為大型金屬傀儡訂製。"),
        PATTERN_HELMET_GOLEM_PLATING("pattern.extended_tinker.helmet_golem_plating", "傀儡頭盔鑲板"),
        HELMET_GOLEM_PLATING_DUMMY("item.extended_tinker.helmet_golem_plating_dummy", "石頭傀儡頭盔鑲板"),
        HELMET_GOLEM_PLATING("item.extended_tinker.helmet_golem_plating", "傀儡頭盔鑲板"),
        HELMET_GOLEM_PLATING_SAND_CAST("item.extended_tinker.helmet_golem_plating_sand_cast", "傀儡頭盔鑲板沙子鑄模"),
        HELMET_GOLEM_PLATING_RED_SAND_CAST("item.extended_tinker.helmet_golem_plating_red_sand_cast", "傀儡頭盔鑲板紅沙鑄模"),
        HELMET_GOLEM_PLATING_CAST("item.extended_tinker.helmet_golem_plating_cast", "傀儡頭盔鑲板金製鑄模"),
        PATTERN_CHESTPLATE_GOLEM_PLATING("pattern.extended_tinker.chestplate_golem_plating", "傀儡胸甲鑲板"),
        CHESTPLATE_GOLEM_PLATING_DUMMY("item.extended_tinker.chestplate_golem_plating_dummy", "石頭傀儡胸甲鑲板"),
        CHESTPLATE_GOLEM_PLATING("item.extended_tinker.chestplate_golem_plating", "傀儡胸甲鑲板"),
        CHESTPLATE_GOLEM_PLATING_SAND_CAST("item.extended_tinker.chestplate_golem_plating_sand_cast", "傀儡胸甲鑲板沙子鑄模"),
        CHESTPLATE_GOLEM_PLATING_RED_SAND_CAST("item.extended_tinker.chestplate_golem_plating_red_sand_cast", "傀儡胸甲鑲板紅沙鑄模"),
        CHESTPLATE_GOLEM_PLATING_CAST("item.extended_tinker.chestplate_golem_plating_cast", "傀儡胸甲鑲板金製鑄模"),
        PATTERN_LEGGINGS_GOLEM_PLATING("pattern.extended_tinker.leggings_golem_plating", "傀儡護腿鑲板"),
        LEGGINGS_GOLEM_PLATING_DUMMY("item.extended_tinker.leggings_golem_plating_dummy", "石頭傀儡護腿鑲板"),
        LEGGINGS_GOLEM_PLATING("item.extended_tinker.leggings_golem_plating", "傀儡護腿鑲板"),
        LEGGINGS_GOLEM_PLATING_SAND_CAST("item.extended_tinker.leggings_golem_plating_sand_cast", "傀儡護腿鑲板沙子鑄模"),
        LEGGINGS_GOLEM_PLATING_RED_SAND_CAST("item.extended_tinker.leggings_golem_plating_red_sand_cast", "傀儡護腿鑲板紅沙鑄模"),
        LEGGINGS_GOLEM_PLATING_CAST("item.extended_tinker.leggings_golem_plating_cast", "傀儡護腿鑲板金製鑄模");

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
        return "zh_tw";
    }

    @Override
    public void allTranslation(Function<ILangData, Boolean> translation) {
        for (LangData translate : LangData.values())
            if (!translation.apply(translate))
                throw new IllegalStateException("Failed to add translation key " + translate.getKey());
    }
}
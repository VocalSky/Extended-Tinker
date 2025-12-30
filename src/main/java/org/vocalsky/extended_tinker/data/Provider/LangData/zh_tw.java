package org.vocalsky.extended_tinker.data.Provider.LangData;


import lombok.Getter;

import java.util.function.Function;

public class zh_tw implements ILang {
    enum LangData implements ILangData {
        ET_COMMON("itemGroup.extended_tinker.common_items", "匠魂擴張 | 常規物品"),
        ET_MG("itemGroup.extended_tinker.golem_items", "匠魂擴張 | 傀儡物品"),
        ET_IAF("itemGroup.extended_tinker.iaf_items", "匠魂擴張 | 冰火傳說物品"),
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
        LEGGINGS_GOLEM_PLATING_CAST("item.extended_tinker.leggings_golem_plating_cast", "傀儡護腿鑲板金製鑄模"),
        DRAGON_ARMOR_HEAD("item.extended_tinker.dragonarmor_head", "龍鎧"),
        DRAGON_ARMOR_BODY("item.extended_tinker.dragonarmor_body", "龍鎧"),
        DRAGON_ARMOR_NECK("item.extended_tinker.dragonarmor_neck", "龍鎧"),
        DRAGON_ARMOR_TAIL("item.extended_tinker.dragonarmor_tail", "龍鎧"),
        DRAGON_ARMOR_HEAD_DESCRIPTION("item.extended_tinker.dragonarmor_head.description", "龍鎧是一件特別的防具，用傳統的工匠技藝無法鍛造，可以嘗試借助末影龍和龍鋼鍛爐的力量。"),
        DRAGON_ARMOR_BODY_DESCRIPTION("item.extended_tinker.dragonarmor_body.description", "龍鎧是一件特別的防具，用傳統的工匠技藝無法鍛造，可以嘗試借助末影龍和龍鋼鍛爐的力量。"),
        DRAGON_ARMOR_NECK_DESCRIPTION("item.extended_tinker.dragonarmor_neck.description", "龍鎧是一件特別的防具，用傳統的工匠技藝無法鍛造，可以嘗試借助末影龍和龍鋼鍛爐的力量。"),
        DRAGON_ARMOR_TAIL_DESCRIPTION("item.extended_tinker.dragonarmor_tail.description", "龍鎧是一件特別的防具，用傳統的工匠技藝無法鍛造，可以嘗試借助末影龍和龍鋼鍛爐的力量。"),
        DRAGON_ARMOR_MATERIAL_IRON("material.extended_tinker.dragon_armor_iron", "鍛鐵"),
        DRAGON_ARMOR_MATERIAL_COPPER("material.extended_tinker.dragon_armor_copper", "赤銅"),
        DRAGON_ARMOR_MATERIAL_SILVER("material.extended_tinker.dragon_armor_silver", "白銀"),
        DRAGON_ARMOR_MATERIAL_GOLD("material.extended_tinker.dragon_armor_gold", "黃金"),
        DRAGON_ARMOR_MATERIAL_DIAMOND("material.extended_tinker.dragon_armor_diamond", "鑽石"),
        DRAGON_ARMOR_MATERIAL_FIRE("material.extended_tinker.dragon_armor_fire", "炎鋼"),
        DRAGON_ARMOR_MATERIAL_ICE("material.extended_tinker.dragon_armor_ice", "霜鋼"),
        DRAGON_ARMOR_MATERIAL_LIGHTNING("material.extended_tinker.dragon_armor_lightning", "霆鋼"),
        STAT_PLATING_HEAD("stat.extended_tinker.plating_head", "頭部鑲板"),
        STAT_PLATING_BODY("stat.extended_tinker.plating_body", "軀體鑲板"),
        STAT_PLATING_NECK("stat.extended_tinker.plating_neck", "脖頸鑲板"),
        STAT_PLATING_TAIL("stat.extended_tinker.plating_tail", "尾部鑲板"),
        PART_DRAGON_ARMOR_CORE_HEAD("item.extended_tinker.dragon_armor_core_head", "龍鎧基底"),
        PART_DRAGON_ARMOR_CORE_BODY("item.extended_tinker.dragon_armor_core_body", "龍鎧基底"),
        PART_DRAGON_ARMOR_CORE_NECK("item.extended_tinker.dragon_armor_core_neck", "龍鎧基底"),
        PART_DRAGON_ARMOR_CORE_TAIL("item.extended_tinker.dragon_armor_core_tail", "龍鎧基底"),
        MAGNETIC_STORM_SURGE("modifier.extended_tinker.magnetic_storm_surge", "磁暴電湧"),
        MAGNETIC_STORM_SURGE_FLAVOR("modifier.extended_tinker.magnetic_storm_surge.flavor", "雷電降臨！"),
        MAGNETIC_STORM_SURGE_DESCRIPTION("modifier.extended_tinker.magnetic_storm_surge.description", "領域內的怪物將直面雷霆的威光。"),
        BURNS_THE_SKY("modifier.extended_tinker.burns_the_sky", "焚天滅世"),
        BURNS_THE_SKY_FLAVOR("modifier.extended_tinker.burns_the_sky.flavor", "KFC 自動化！"),
        BURNS_THE_SKY_DESCRIPTION("modifier.extended_tinker.burns_the_sky.description", "領域內的怪物將被烈火焚身。"),
        PERMAFROST("modifier.extended_tinker.permafrost", "永恆凍土"),
        PERMAFROST_FLAVOR("modifier.extended_tinker.permafrost.flavor", "來堆雪人！"),
        PERMAFROST_DESCRIPTION("modifier.extended_tinker.permafrost.description", "領域內的怪物將被送入永恆的冬眠。"),
        EXP_TRANSFER_ORB_ITEM("item.extended_tinker.exp_transfer_orb", "經驗轉移寶珠"),
        EXP_TRANSFER_ORB_EXPORT_TITLE("recipe.extended_tinker.exp_export.title", "經驗轉移"),
        EXP_TRANSFER_ORB_EXPORT_DESCRIPTION("recipe.extended_tinker.exp_export.description", "將工具的經驗轉移到寶珠上，可以在工匠砧上將寶珠的經驗注入回工具中（寶珠將會被消耗）。");

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
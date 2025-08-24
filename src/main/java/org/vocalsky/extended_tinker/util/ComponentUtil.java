package org.vocalsky.extended_tinker.util;

import net.minecraft.network.chat.Component;

public class ComponentUtil {
    public static Component replaceText(Component component, String oldText, String newText) {
        String json = Component.Serializer.toJson(component);
        return Component.Serializer.fromJson(json.replace(oldText, newText));
    }
}
package com.quoridor.Settings;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for color related operations
 */
public class ColorUtils {
    private static final Map<Color, String> colorNameMap = new HashMap<>();

    static {
        colorNameMap.put(Color.RED, "RED");
        colorNameMap.put(Color.BLUE, "BLUE");
        colorNameMap.put(Color.GREEN, "GREEN");
        colorNameMap.put(Color.YELLOW, "YELLOW");
        colorNameMap.put(Color.BLACK, "BLACK");
        colorNameMap.put(Color.WHITE, "WHITE");
    }

    public static String getColorName(Color color) {
        return colorNameMap.getOrDefault(color, "Unknown Color");
    }
}

package com.quoridor.Settings;

import java.awt.Color;

/**
 * Enum representing the color of the player. Color as in the color of the player's pawn and the symbol representing the player in board strings
 */
public enum PlayerColor {
    BLUE(Color.BLUE, '1'),
    RED(Color.RED, '2'),
    GREEN(Color.GREEN, '3'),
    YELLOW(Color.YELLOW, '4');

    private final Color color;
    private final char symbol;

    PlayerColor(Color color, char symbol) {
        this.color = color;
        this.symbol = symbol;
    }

    public Color getColor() {
        return color;
    }

    public char getSymbol() {
        return symbol;
    }

    public static PlayerColor fromSymbol(char symbol) {
        for (PlayerColor pc : values()) {
            if (pc.symbol == symbol) return pc;
        }
        throw new IllegalArgumentException("Invalid player symbol: " + symbol);
    }

    public String getColorName() {
        switch (this) {
            case BLUE: return "Blue";
            case RED: return "Red";
            case GREEN: return "Green";
            case YELLOW: return "Yellow";
            default: return "Unknown";
        }
    }
} 

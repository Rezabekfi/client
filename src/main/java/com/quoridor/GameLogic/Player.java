package com.quoridor.GameLogic;
import java.awt.*;

import com.quoridor.Settings.Constants;
import com.quoridor.Settings.PlayerColor;

/**
 * Player class representing a player in the game
 * Contains information about the player such as their position, direction, and number of walls
 */
public class Player {
    // Direction the player is trying to reach
    private GoalDirection direction;
    // Number of walls the player has left
    private int numberOfWalls;
    // Position of the player on the board
    private Position position;
    // Color of the player
    private PlayerColor playerColor;
    // Name of the player
    private String playerName;
    // Unique id of the player
    private String id;
    // Whether the player is connected to the game
    private boolean isConnected;

    // Constructor for the player
    public Player(String playerName, GoalDirection direction, Position position, PlayerColor playerColor) {
        this.playerName = playerName;
        this.direction = direction;
        this.numberOfWalls = Constants.STARTING_NUMBER_OF_WALLS;
        this.position = position;
        this.playerColor = playerColor;
        this.isConnected = true;
    }
    
    // Copy constructor for the player
    public void updatePlayer(Player player) {
        this.direction = player.getDirection();
        this.numberOfWalls = player.getNumberOfWalls();
        this.position = player.getPosition();
        this.playerColor = player.getPlayerColor();
        this.playerName = player.getName();
        this.id = player.getId();
    }

    // Check if the player has reached the goal
    public boolean isPlayerFinished() {
        switch (direction) {
            case NORTH:
                if (this.getRow() == 0) return true;
                break;
            case EAST:
                if (this.getCol() == Constants.BOARD_SIZE-1) return true;
                break;
            case SOUTH:
                if (this.getRow() == Constants.BOARD_SIZE-1) return true;
                break;
            case WEST:
                if (this.getCol() == 0) return true;
                break;
            default:
                return false;
        }
        return false;
    }

    // Player has placed a wall. Decrementing the number of walls the player has left to place.
    public void placedWall() {
        numberOfWalls--;
    }

    // Getters and setters for the player class
    public GoalDirection getDirection() {
        return direction;
    }

    public void setDirection(GoalDirection direction) {
        this.direction = direction;
    }

    public int getNumberOfWalls() {
        return numberOfWalls;
    }

    public void setNumberOfWalls(int numberOfWalls) {
        this.numberOfWalls = numberOfWalls;
    }

    public int getRow() {
        return position.getRow();
    }

    public void setRow(int row) {
        this.position.setRow(row);
    }

    public int getCol() {
        return position.getCol();
    }

    public void setCol(int col) {
        this.position.setCol(col);
    }

    public char getColorSymbol() {
        return playerColor.getSymbol();
    }

    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    public String getName() {
        return playerName;
    }

    public void setName(String playerName) {
        this.playerName = playerName;
    }

    public Color getColor2D() {
        return playerColor.getColor();
    }

    public Position getPosition() {
        return position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    @Override
    public String toString() {
        return "Player [direction=" + direction + ", numberOfWalls=" + numberOfWalls + ", row=" + getRow() + ", col=" + getCol()
                + ", color=" + playerColor.getSymbol() + "]";
    }
}

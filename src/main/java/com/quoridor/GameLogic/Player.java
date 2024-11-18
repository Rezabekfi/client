package com.quoridor.GameLogic;
import java.awt.*;

import com.quoridor.Settings.Constants;
import com.quoridor.Settings.PlayerColor;

public class Player {

    private GoalDirection direction;
    private int numberOfWalls;
    private Position position;
    private PlayerColor playerColor;
    private String playerName;
    private String id;
    private boolean isConnected;

    public Player(String playerName, GoalDirection direction, Position position, PlayerColor playerColor) {
        this.playerName = playerName;
        this.direction = direction;
        this.numberOfWalls = Constants.STARTING_NUMBER_OF_WALLS;
        this.position = position;
        this.playerColor = playerColor;
        this.isConnected = true;
    }

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

    @Override
    public String toString() {
        return "Player [direction=" + direction + ", numberOfWalls=" + numberOfWalls + ", row=" + getRow() + ", col=" + getCol()
                + ", color=" + playerColor.getSymbol() + "]";
    }

    public void placedWall() {
        numberOfWalls--;
    }

    public String getName() {
        return playerName;
    }

    public void setName(String playerName) {
        this.playerName = playerName;
    }

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

    public void updatePlayer(Player player) {
        this.direction = player.getDirection();
        this.numberOfWalls = player.getNumberOfWalls();
        this.position = player.getPosition();
        this.playerColor = player.getPlayerColor();
        this.playerName = player.getName();
        this.id = player.getId();
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }
}

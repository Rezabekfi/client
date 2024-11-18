package com.quoridor.GameLogic;

import java.io.Serializable;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.quoridor.Settings.PlayerColor;

import org.json.JSONArray;

public class GameMessage implements Serializable {

    private MessageType type;
    private JSONObject messageData;

    public enum MessageType {
        WELCOME,
        WAITING,
        GAME_STARTED,
        GAME_ENDED,
        MOVE,
        ERROR,
        WRONG_MESSAGE,
        ACK,
        NEXT_TURN,
        NAME_REQUEST,
        NAME_RESPONSE,
        HEARTBEAT
    }

    public GameMessage() {
        this.type = MessageType.WRONG_MESSAGE;
        this.messageData = new JSONObject();
    }

    public GameMessage(MessageType type, Map<String, Object> data) {
        this.type = type;
        this.messageData = new JSONObject(data);
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String toJSON() {
        JSONObject json = new JSONObject();
        json.put("type", type.toString().toLowerCase());
        json.put("data", messageData);
        return json.toString();
    }

    public static GameMessage fromJSON(String jsonString) {
        JSONObject json = new JSONObject(jsonString);
        MessageType type = MessageType.valueOf(json.getString("type").toUpperCase());
        JSONObject jsonData = json.getJSONObject("data");
        
        Map<String, Object> data = jsonData.toMap();
        return new GameMessage(type, data);
    }

    // Helper methods for specific message types
    public String getMessage() {
        return messageData.optString("message");
    }

    public String getLobbyId() {
        return messageData.optString("lobby_id");
    }

    public String getCurrentPlayerId() {
        return messageData.optString("current_player_id");
    }

    public String getWinnerId() {
        return messageData.optString("winner_id");
    }

    public String getBoardString() {
        return messageData.optString("board");
    }

    public List<Position> getHorizontalWalls() {
        return getWallsList("horizontal_walls");
    }

    public List<Position> getVerticalWalls() {
        return getWallsList("vertical_walls");
    }

    private List<Position> getWallsList(String key) {
        List<Position> walls = new ArrayList<>();
        JSONArray wallsArray = messageData.optJSONArray(key);
        if (wallsArray == null) return walls;
        
        for (int i = 0; i < wallsArray.length(); i++) {
            JSONArray wall = wallsArray.getJSONArray(i);
            walls.add(new Position(wall.getInt(0), wall.getInt(1)));
        }
        
        return walls;
    }

    public String getPlayerId(Player player) {
        JSONArray playersArray = messageData.optJSONArray("players");
        if (playersArray == null) return null;

        for (int i = 0; i < playersArray.length(); i++) {
            JSONObject playerData = playersArray.getJSONObject(i);
            if (playerData.getString("name").equals(player.getName())) {
                return playerData.getString("id");
            }
        }
        return null;
    }

    public Player getCurrentPlayer() {
        return getPlayerById(getCurrentPlayerId());
    }

    public Player getPlayerById(String id) {
        List<Player> players = getPlayers();
        for (Player player : players) {
            if (player.getId().equals(id)) return player;
        }
        return null;
    }

    // ment to be used at game start (declaring determineDirection wont work after first move)
    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();
        JSONArray playersArray = messageData.optJSONArray("players");
        if (playersArray == null) return players;

        for (int i = 0; i < playersArray.length(); i++) {
            JSONObject playerData = playersArray.getJSONObject(i);
            JSONArray posArray = playerData.getJSONArray("position");
            Position pos = new Position(posArray.getInt(0), posArray.getInt(1));
            char symbol = playerData.getString("board_char").charAt(0);
            String id = playerData.getString("id");
            // Determine direction based on position
            GoalDirection direction = determineDirection(pos);
            
            Player player = new Player(
                playerData.getString("name"),
                direction,
                pos,
                PlayerColor.fromSymbol(symbol)
            );
            player.setId(id);
            player.setNumberOfWalls(playerData.getInt("walls_left"));
            player.setName(playerData.getString("name"));
            players.add(player);
        }
        return players;
    }

    // used at game start (declaring determineDirection wont work after first move)
    // declared for 4 players but for now game is only for 2 players (possible future extension)
    private GoalDirection determineDirection(Position pos) {
        if (pos.getRow() == 0) return GoalDirection.SOUTH;
        if (pos.getRow() == 8) return GoalDirection.NORTH;
        if (pos.getCol() == 0) return GoalDirection.EAST;
        if (pos.getCol() == 8) return GoalDirection.WEST;
        return GoalDirection.NORTH; // default
    }

    private char getColorChar(String color) {
        switch (color.toLowerCase()) {
            case "blue": return '1';
            case "red": return '2';
            case "green": return '3';
            case "yellow": return '4';
            default: return '5';
        }
    }

    // Factory methods for creating specific message types
    public static GameMessage createMoveMessage(String playerId, boolean isHorizontal, Position... positions) {
        Map<String, Object> data = new HashMap<>();
        data.put("player_id", playerId);
        data.put("is_horizontal", isHorizontal);
        data.put("position", positions);
        return new GameMessage(MessageType.MOVE, data);
    }

    public static GameMessage createNameResponse(String name) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        return new GameMessage(MessageType.NAME_RESPONSE, data);
    }

    public static GameMessage createAckMessage() {
        return new GameMessage(MessageType.ACK, new HashMap<>());
    }
} 

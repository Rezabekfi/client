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
        HEARTBEAT,
        PLAYER_DISCONNECTED,
        PLAYER_RECONNECTED,
        ABANDON
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
        try {
            JSONObject json = new JSONObject(jsonString);
            MessageType type = MessageType.valueOf(json.getString("type").toUpperCase());
            JSONObject jsonData = json.getJSONObject("data");
            
            Map<String, Object> data = jsonData.toMap();
            if (!isValidMessage(type, data)) {
                GameMessage message = new GameMessage();
                message.messageData.put("message", "Invalid message format for type: " + type);
                return message;
            }
            return new GameMessage(type, data);
        } catch (Exception e) {
            GameMessage message = new GameMessage();
            message.messageData.put("message", "Invalid message format: " + e.getMessage());
            return message;
        }
    }

    private static boolean isValidMessage(MessageType type, Map<String, Object> data) {
        switch (type) {
            case WELCOME:
                return data.containsKey("message") && data.get("message") instanceof String;
            case WAITING:
                return data.isEmpty();
            case NAME_REQUEST:
                return data.isEmpty();
            case NAME_RESPONSE:
                return data.containsKey("name") && data.get("name") instanceof String;
            case GAME_STARTED:
            case NEXT_TURN:
                return isValidGameStartedOrNextTurn(data);
            case ERROR:
                return data.containsKey("message") && data.get("message") instanceof String;
            case GAME_ENDED:
                return data.containsKey("lobby_id") && data.get("lobby_id") instanceof Integer &&
                       data.containsKey("winner_id") && data.get("winner_id") instanceof String;
            case HEARTBEAT:
                return data.isEmpty();
            case ACK:
                return data.isEmpty();
            case PLAYER_DISCONNECTED:
                return data.containsKey("disconnected_player_id") && data.get("disconnected_player_id") instanceof String;
            case PLAYER_RECONNECTED:
                return data.containsKey("reconnected_player_id") && data.get("reconnected_player_id") instanceof String;
            default:
                return false;
        }
    }

    private static boolean isValidGameStartedOrNextTurn(Map<String, Object> data) {
        return data.containsKey("lobby_id") && data.get("lobby_id") instanceof Integer &&
               data.containsKey("board") && data.get("board") instanceof String &&
               data.containsKey("current_player_id") && data.get("current_player_id") instanceof String &&
               data.containsKey("horizontal_walls") && data.get("horizontal_walls") instanceof List &&
               data.containsKey("vertical_walls") && data.get("vertical_walls") instanceof List &&
               data.containsKey("players") && data.get("players") instanceof List &&
               isValidWalls(data.get("horizontal_walls")) &&
               isValidWalls(data.get("vertical_walls")) &&
               isValidPlayers(data.get("players"));
    }

    private static boolean isValidWalls(Object wallsObj) {
        if (!(wallsObj instanceof List)) return false;
        List<?> walls = (List<?>) wallsObj;
        for (Object wallObj : walls) {
            if (!(wallObj instanceof List)) return false;
            List<?> wall = (List<?>) wallObj;
            if (wall.size() != 2) return false;
            if (!(wall.get(0) instanceof Integer) || !(wall.get(1) instanceof Integer)) return false;
            if ((Integer) wall.get(0) < 0 || (Integer) wall.get(1) < 0) return false;
        }
        return true;
    }

    private static boolean isValidPlayers(Object playersObj) {
        if (!(playersObj instanceof List)) return false;
        List<?> players = (List<?>) playersObj;
        for (Object playerObj : players) {
            if (!(playerObj instanceof Map)) return false;
            Map<?, ?> player = (Map<?, ?>) playerObj;
            if (!player.containsKey("id") || !(player.get("id") instanceof String)) return false;
            if (!player.containsKey("position") || !(player.get("position") instanceof List)) return false;
            List<?> position = (List<?>) player.get("position");
            if (position.size() != 2) return false;
            if (!(position.get(0) instanceof Integer) || !(position.get(1) instanceof Integer)) return false;
            if ((Integer) position.get(0) < 0 || (Integer) position.get(1) < 0) return false;
            if (!player.containsKey("name") || !(player.get("name") instanceof String)) return false;
            if (!player.containsKey("walls_left") || !(player.get("walls_left") instanceof Integer)) return false;
            if (!player.containsKey("board_char") || !(player.get("board_char") instanceof String)) return false;
        }
        return true;
    }

    public String getReconnectedPlayerId() {
        return messageData.optString("reconnected_player_id");
    }

    // Helper methods for specific message types
    public String getMessage() {
        return messageData.optString("message");
    }

    public void setMessage(String message) {
        messageData.put("message", message);
    }

    public String getLobbyId() {
        return messageData.optString("lobby_id");
    }

    public String getDisconnectedPlayerId() {
        return messageData.optString("disconnected_player_id");
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

    /* 
    private char getColorChar(String color) {
        switch (color.toLowerCase()) {
            case "blue": return '1';
            case "red": return '2';
            case "green": return '3';
            case "yellow": return '4';
            default: return '5';
        }
    }
    */

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

    public static GameMessage createAbandonMessage() {
        return new GameMessage(MessageType.ABANDON, new HashMap<>());
    }

    public static GameMessage createErrorMessage(String message) {
        Map<String, Object> data = new HashMap<>();
        data.put("message", message);
        return new GameMessage(MessageType.ERROR, data);
    }

    public static GameMessage createHeartbeatMessage() {
        return new GameMessage(MessageType.HEARTBEAT, new HashMap<>());
    }
}

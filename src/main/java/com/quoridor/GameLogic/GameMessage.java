package com.quoridor.GameLogic;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import com.quoridor.Settings.PlayerColor;

public class GameMessage {

    private MessageType type;
    private Map<String, Object> messageData;

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
        ABANDON,
        LOST_CONNECTION
    }

    public GameMessage() {
        this.type = MessageType.WRONG_MESSAGE;
        this.messageData = new HashMap<>();
    }

    public GameMessage(String message) {
        this.type = MessageType.WRONG_MESSAGE;
        this.messageData = new HashMap<>();
        this.messageData.put("message", message);
    }

    public GameMessage(MessageType type, Map<String, Object> data) {
        this.type = type;
        this.messageData = data;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String toMessageString() {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("type:").append(type.toString().toLowerCase()).append("|data:");
        messageData.forEach((key, value) -> {
            messageBuilder.append(key).append("=").append(value).append(";");
        });
        if (messageData.isEmpty()) {
            messageBuilder.append(";");
        }
        return messageBuilder.toString();
    }

    public static GameMessage fromMessageString(String messageString) {
        try {
            String[] parts = messageString.split("\\|data:");
            MessageType type = MessageType.valueOf(parts[0].split(":")[1].toUpperCase());
            String[] dataParts = parts[1].split(";");
            Map<String, Object> data = new HashMap<>();
            for (String dataPart : dataParts) {
                if (!dataPart.isEmpty()) {
                    String[] keyValue = dataPart.split("=");
                    data.put(keyValue[0], keyValue[1]);
                }
            }
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
                return data.containsKey("lobby_id") && data.get("lobby_id") instanceof String &&
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
        return data.containsKey("lobby_id") && data.get("lobby_id") instanceof String &&
               data.containsKey("board") && data.get("board") instanceof String &&
               data.containsKey("current_player_id") && data.get("current_player_id") instanceof String &&
               data.containsKey("horizontal_walls") && data.get("horizontal_walls") instanceof String &&
               data.containsKey("vertical_walls") && data.get("vertical_walls") instanceof String &&
               data.containsKey("players") && data.get("players") instanceof String;
    }

    public String getReconnectedPlayerId() {
        return (String) messageData.get("reconnected_player_id");
    }

    // Helper methods for specific message types
    public String getMessage() {
        return (String) messageData.get("message");
    }

    public void setMessage(String message) {
        messageData.put("message", message);
    }

    public String getLobbyId() {
        return (String) messageData.get("lobby_id");
    }

    public String getDisconnectedPlayerId() {
        return (String) messageData.get("disconnected_player_id");
    }

    public String getCurrentPlayerId() {
        return (String) messageData.get("current_player_id");
    }

    public String getWinnerId() {
        return (String) messageData.get("winner_id");
    }

    public String getBoardString() {
        return (String) messageData.get("board");
    }

    public List<Position> getHorizontalWalls() {
        return getWallsList("horizontal_walls");
    }

    public List<Position> getVerticalWalls() {
        return getWallsList("vertical_walls");
    }

    private List<Position> getWallsList(String key) {
        List<Position> walls = new ArrayList<>();
        String wallsString = (String) messageData.get(key);
        if (wallsString == null || wallsString.equals("[]")) return walls;

        String[] wallsArray = wallsString.split("\\],\\[");
        for (String wall : wallsArray) {
            String[] coords = wall.replace("[", "").replace("]", "").split(",");
            walls.add(new Position(Integer.parseInt(coords[0]), Integer.parseInt(coords[1])));
        }
        return walls;
    }

    public String getPlayerId(Player player) {
        String playersString = (String) messageData.get("players");
        if (playersString == null) return null;

        List<Player> players = getPlayers();
        for (Player p : players) {
            if (p.getName().equals(player.getName())) return p.getId();
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

    public List<Player> getPlayers() {
        String id = "";
        int row = 0;
        int col = 0;
        String name = "";
        char board_char = ' ';
        int wallsLeft = 0;
        List<Player> players = new ArrayList<>();
        String playersString = (String) messageData.get("players");
        if (playersString == null) return players;
        String[] playersArray = playersString.split("\\],\\[");
        for (String playerData : playersArray) {
            
            String[] playerAttributes = playerData.replace("[", "").replace("]", "").split(",");

            for (String atrubute : playerAttributes) {
                String[] atrubuteArray = atrubute.split(":");
                if (atrubuteArray[0].equals("id")) {
                    id = atrubuteArray[1];
                } else if (atrubuteArray[0].equals("row")) {
                    row = Integer.parseInt(atrubuteArray[1]);
                } else if (atrubuteArray[0].equals("col")) {
                    col = Integer.parseInt(atrubuteArray[1]);
                } else if (atrubuteArray[0].equals("name")) {
                    name = atrubuteArray[1];
                } else if (atrubuteArray[0].equals("board_char")) {
                    board_char = atrubuteArray[1].charAt(0);
                } else if (atrubuteArray[0].equals("walls_left")) {
                    wallsLeft = Integer.parseInt(atrubuteArray[1]);
                }
            }
            Position pos = new Position(row, col);
            GoalDirection direction = determineDirection(pos);
            Player player = new Player(
                name,
                direction,
                pos,
                PlayerColor.fromSymbol(board_char)
            );
            player.setId(id);
            player.setNumberOfWalls(wallsLeft);
            players.add(player);
        }
        return players;
    }

    private GoalDirection determineDirection(Position pos) {
        if (pos.getRow() == 0) return GoalDirection.SOUTH;
        if (pos.getRow() == 8) return GoalDirection.NORTH;
        if (pos.getCol() == 0) return GoalDirection.EAST;
        if (pos.getCol() == 8) return GoalDirection.WEST;
        return GoalDirection.NORTH; // default
    }

    // Factory methods for creating specific message types
    public static GameMessage createMoveMessage(String playerId, boolean isHorizontal, Position[] positions) {
        Map<String, Object> data = new HashMap<>();
        data.put("player_id", playerId);
        data.put("is_horizontal", isHorizontal);
        data.put("position", "[" + positions[0].getRow() + "," + positions[0].getCol() + "]" + (positions.length > 1 ? ",[" + positions[1].getRow() + "," + positions[1].getCol() + "]" : ""));
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

    public static GameMessage createLostConnection() {
        return new GameMessage(MessageType.LOST_CONNECTION, new HashMap<>());
    }
}

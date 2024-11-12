package GameLogic;

import java.io.Serializable;

public class GameMessage implements Serializable {
    private MessageType type;
    private Object payload;
    private String playerName;

    public enum MessageType {
        CONNECT,
        DISCONNECT,
        MOVE,
        WALL_PLACEMENT,
        GAME_START,
        GAME_END
    }

    public GameMessage(MessageType type, Object payload, String playerName) {
        this.type = type;
        this.payload = payload;
        this.playerName = playerName;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
} 
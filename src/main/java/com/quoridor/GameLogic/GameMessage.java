package com.quoridor.GameLogic;

import java.io.Serializable;
import java.util.Map;
import java.util.List;
public class GameMessage implements Serializable {
    private MessageType type;
    private Map<String, Object> data;

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

    public GameMessage(MessageType type, Map<String, Object> data) {
        this.type = type;
        this.data = data;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    // Helper method to get specific data fields
    public String getStringData(String key) {
        Object value = data.get(key);
        return value != null ? value.toString() : null;
    }

    public Integer getIntData(String key) {
        Object value = data.get(key);
        return value != null ? Integer.parseInt(value.toString()) : null;
    }

    public Boolean getBooleanData(String key) {
        Object value = data.get(key);
        return value != null ? Boolean.parseBoolean(value.toString()) : null;
    }

    // might be dangerous
    public List<Position> getPositionListData(String key) {
        Object value = data.get(key);
        return value != null ? (List<Position>) value : null;
    }

    public Player getPlayerData(String key) {
        Object value = data.get(key);
        return value != null ? (Player) value : null;
    }
} 
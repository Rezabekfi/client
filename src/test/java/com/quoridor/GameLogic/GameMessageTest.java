
package com.quoridor.GameLogic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

public class GameMessageTest {

    @Test
    public void testSerialization() {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Welcome to the game!");
        GameMessage message = new GameMessage(GameMessage.MessageType.WELCOME, data);
        
        String json = message.toJSON();
        assertNotNull(json);
        assertTrue(json.contains("\"type\":\"welcome\""));
        assertTrue(json.contains("\"message\":\"Welcome to the game!\""));
    }

    @Test
    public void testDeserialization() {
        String jsonString = "{\"type\":\"welcome\",\"data\":{\"message\":\"Welcome to the game!\"}}";
        GameMessage message = GameMessage.fromJSON(jsonString);
        
        assertEquals(GameMessage.MessageType.WELCOME, message.getType());
        assertEquals("Welcome to the game!", message.getMessage());
    }

    @Test
    public void testMoveMessage() {
        String jsonString = "{\"type\":\"game_started\",\"data\":{\"lobby_id\":1,\"board\":\"XXXX2XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX\",\"current_player_id\":\"1\",\"horizontal_walls\":[],\"vertical_walls\":[],\"players\":[{\"id\":\"1\",\"position\":[8,4],\"name\":\"Player 22\",\"board_char\":\"1\",\"walls_left\":10},{\"id\":\"2\",\"position\":[0,4],\"name\":\"Player 11\",\"board_char\":\"2\",\"walls_left\":10}]}}";
        GameMessage message = GameMessage.fromJSON(jsonString);
        assertEquals(GameMessage.MessageType.GAME_STARTED, message.getType());
    }

    @Test
    public void testInvalidMessageType() {
        String jsonString = "{\"type\":\"invalid_type\",\"data\":{}}";
        GameMessage message = GameMessage.fromJSON(jsonString);
        
        assertEquals(GameMessage.MessageType.WRONG_MESSAGE, message.getType());
        // use startsWith because the message may contain additional information
        assertTrue(message.getMessage().startsWith("Invalid message format:"));
    }

    @Test
    public void testInvalidMessageFormat() {
        String jsonString = "{\"type\":\"welcome\",\"data\":\"invalid_data\"}";
        GameMessage message = GameMessage.fromJSON(jsonString);
        
        assertEquals(GameMessage.MessageType.WRONG_MESSAGE, message.getType());
        assertTrue(message.getMessage().startsWith("Invalid message format:"));
    }

    @Test
    public void testCreateAckMessage() {
        GameMessage message = GameMessage.createAckMessage();
        
        assertEquals(GameMessage.MessageType.ACK, message.getType());
        assertTrue(message.toJSON().contains("\"type\":\"ack\""));
    }

    @Test
    public void testCreateAbandonMessage() {
        GameMessage message = GameMessage.createAbandonMessage();
        
        assertEquals(GameMessage.MessageType.ABANDON, message.getType());
        assertTrue(message.toJSON().contains("\"type\":\"abandon\""));
    }
}
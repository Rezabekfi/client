
package com.quoridor.GameLogic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

public class GameMessageTest {

    /*
    @Test
    public void testSerialization() {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Welcome to the game!");
        GameMessage message = new GameMessage(GameMessage.MessageType.WELCOME, data);
        
        String json = message.toMessageString();
        assertNotNull(json);
        assertTrue(json.contains("type:welcome"));
        assertTrue(json.contains("message=Welcome to the game!"));
    }

    @Test
    public void testDeserialization() {
        String messageString = "type:welcome|data:message=Welcome to the game!;";
        GameMessage message = GameMessage.fromMessageString(messageString);
        
        assertEquals(GameMessage.MessageType.WELCOME, message.getType());
        assertEquals("Welcome to the game!", message.getMessage());
    }

    @Test
    public void testMoveMessage() {

        
    }

    @Test
    public void testInvalidMessageType() {
        String invalidMessageString = "type:inval|data:message=Welcome to the game!;";
        GameMessage message = GameMessage.fromMessageString(invalidMessageString);

        assertEquals(GameMessage.MessageType.WRONG_MESSAGE, message.getType());
    }

    @Test
    public void testInvalidMessageFormat() {
        String invalidMessageString = "type:welcome|data:message=Welcome to the game!;extra";
        GameMessage message = GameMessage.fromMessageString(invalidMessageString);

        assertEquals(GameMessage.MessageType.WRONG_MESSAGE, message.getType());
    }

    @Test
    public void testCreateAckMessage() {
       
    }

    @Test
    public void testCreateAbandonMessage() {
        
    }
     */
    @Test
    public void testDeserializationGameStarted() {
        String messageString = "type:game_started|data:lobby_id=3;board=XXXX2XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX;current_player_id=1;horizontal_walls=[];vertical_walls=[];players=[id:1,row:8,col:4,name:Player 1333,board_char:1,walls_left:10],[id:2,row:0,col:4,name:Player 12,board_char:2,walls_left:10];";
        GameMessage message = GameMessage.fromMessageString(messageString);

        assertEquals(GameMessage.MessageType.GAME_STARTED, message.getType());
    }

}

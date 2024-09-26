public class Main {
    
    public static void main(String[] args) {
        Board board = new Board(Constants.MAXIMUM_POSSIBLE_PLAYERS);
        board.setUpGame();
        board.printBoardDebugging();

        Player[] players = board.getPlayers();
        for (int i = 0; i < players.length; i++) {
            System.out.println(players[i].toString());
        }
    }
}

import javax.swing.*;

public class Snake_Game extends JFrame { //behave like a frame
    private GameBoard board;

    public Snake_Game(){
        board = new GameBoard();
        add(board);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {

        JFrame snake_Game = new Snake_Game(); //using constructor
    }
}

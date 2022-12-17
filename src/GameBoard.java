import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameBoard extends JPanel implements ActionListener {
    // Game Board is listen the Action Listener....so basically gameboard is the Action listener
    int height = 400;
    int width = 400;
    int x[] = new int[height*width];
    int y[] = new int[height*width];
    int dots ; //size of snake
    //position of food...
    int apple_x = 100;
    int apple_y = 100;
    int dot_size = 10;
    Image apple ;
    Image body ;
    Image head ;

    boolean leftDirection = true;
    boolean rightDirection = false;
    boolean upDirection = false;
    boolean downDirection = false;

    Timer timer; //javax swing class -> produced an action event in specified intervals
    int DELAY = 229; //it's map -> timer object to the actual time (here 400 is in mili seconds)
    //so here after 0.4 seconds in actual time the t=0 will go t=1 & after another
    // 0.4 seconds t=1 will go to t=2 and so on....basically after every 0.4second
    // the actual time which is t...increased by 1
    int RANDOM_POS = 39;
    boolean inGame = true; //here true means we're in the game
    public GameBoard(){
        addKeyListener(new TAdapter());
        setFocusable(true);
        setPreferredSize(new Dimension(width,height));
        setBackground(Color.BLACK);

        loadImages();
        initGame();
    }
    public void initGame(){
        dots = 3;
        for(int i=0 ; i<dots ; i++)
        {
            //moving the initial head position to 150,150
            x[i] = 150+i*dot_size;
            y[i] = 150;
        }
        timer = new Timer(DELAY,this); //delay -> used to map timer object with the actual time
        timer.start();
    }
    private void loadImages(){
        ImageIcon image_apple = new ImageIcon("src/resources/apple.png");
        apple = image_apple.getImage();

        ImageIcon image_head = new ImageIcon("src/resources/head.png");
        head = image_head.getImage();

        ImageIcon image_body = new ImageIcon("src/resources/dot.png");
        body = image_body.getImage();
    }

    @Override //method of panel class to draw images on particular position
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (inGame) {
            graphics.drawImage(apple, apple_x, apple_y, this);// this is used to specify -> what the observer is made
            for (int i = 0; i < dots; i++) {
                if (i == 0) {
                    graphics.drawImage(head, x[0], y[0], this);
                } else {
                    graphics.drawImage(body, x[i], y[i], this);
                }
            }
            //to sync with the timer...
            Toolkit.getDefaultToolkit().sync(); //sync function helps us to -> map this function to the timer
        } else {
            gameOver(graphics);
        }
    }

    private void move(){
        for(int i = dots-1; i>0 ; i--) //starts from dot-1 because its 0 based index
        {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        //for head direction....
        if(leftDirection){
            x[0] -= dot_size;
        }
        if(rightDirection){
            x[0] += dot_size;
        }
        if(upDirection){
            y[0] -= dot_size;
        }
        if(downDirection){
            y[0] += dot_size;
        }
    }

    //method for set the apple at random position
    private void locateApple(){
        int r = (int)(Math.random()*(RANDOM_POS)); // values 0 to 39.....if we multiply bt 10 so 0 , 10 , 20 ....390
        apple_x = r*dot_size;

        r = (int)(Math.random()*(RANDOM_POS)); // values 0 to 39.....if we multiply bt 10 so 0 , 10 , 20 ....390
        apple_y = r*dot_size;
    }

    //method to check snake-apple collision
    private void checkApple(){
        if(x[0]==apple_x && y[0]==apple_y){
            dots++;
            locateApple();
        }
    }

    //method to check collision of snake with itself or with the boundaries
    private void checkCollision(){
        //if collision with boundaries...
        if(x[0] < 0){      //it means snake collied with left border means we're now out of the game (game over)
            inGame = false;
        }
        if(x[0] >= width){ //it means snake collied with right border means we're now out of the game (game over)
            inGame = false;
        }
        if(y[0] < 0){     //it means snake collied with upper border means we're now out of the game (game over)
            inGame = false;
        }
        if(x[0] >= height){ //it means snake collied with bottom border means we're now out of the game (game over)
            inGame = false;
        }

        //if collision with it's own body...
        // we need minimum 4 dots to collied with snake with its own body
        for(int i=dots-1 ; i >=3 ; i--)
        {
            if(x[0]==x[i] && y[0]==y[i])
            {
                inGame = false ; //game over
                break;
            }
        }
    }

    // created a drawing that say "Game Over"
    private void gameOver(Graphics graphics){
        String msg = "GAME  OVER !!";
        Font small = new Font("Helvetica",Font.BOLD,18);
        FontMetrics metrics = getFontMetrics(small);
        graphics.setColor(Color.red);
        graphics.setFont(small);
        graphics.drawString(msg,(width-metrics.stringWidth(msg))/2,height/2); //drawString method is used to draw the String message
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent){
        if(inGame){ // here inGame true means we're in the game so then only we should to check other methods
            checkApple(); //this will be checked apple at every interval of t like t=0,1,2,3.... etc.
            checkCollision(); // because it's tied with the timer...every time the timer goes from 0 to 1 or 1 to 2 it should check everytime the its not collided with any of the 2 conditions (itself or any of the 4 borders)
            move();
        }
        repaint(); //repaint is a function -> AWT component help us to trigger again the drawing function
    }

    public class TAdapter extends KeyAdapter{ //TAdapter class inherits the properties of KeyAdapter class
        @Override //...the methods of KeyAdapter class in keyPressed class

        public void keyPressed(KeyEvent keyEvent){ // when key is pressed an key ActionEvent is called

            int key = keyEvent.getKeyCode(); // we have 4 types of code left,right,up and down
            // these are the constraints..these are code for the
            // perticular key  that to be pressed

            // if user press left key...
            if((key == KeyEvent.VK_LEFT) && (!rightDirection)){
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            // if user press right key...
            if((key == KeyEvent.VK_RIGHT) && (!leftDirection)){
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            // if user press up key...
            if((key == KeyEvent.VK_UP) && (!downDirection)){
                upDirection = true;
                leftDirection = false;
                rightDirection = false;
            }

            // if user press down key...
            if((key == KeyEvent.VK_DOWN) && (!upDirection)){
                downDirection = true;
                leftDirection = false;
                rightDirection = false;
            }

        }
    }

}


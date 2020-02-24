package bong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Bong implements KeyListener {

    JFrame j = new JFrame();
    
    JPanel p = new JPanel();
    
    Ball ball;
    
    int lives = 5;
    
    Paddle paddle = new Paddle();

    ArrayList<Square> squares = new ArrayList<Square>();
    
    int hits = 0;
    
    public class Square {
        int x, y;
        int width = 80;
        int height = 45;
        Color color = Color.WHITE;
        public boolean isHit() {
            if(ball.x >= x && ball.x <= x + width && ball.y >= y && ball.y <= y + height) {
                return true;
            }
            return false;
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    long time = 0;
    long timet = 0;

   @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            paddle.move_left();
            timet = new Date().getTime();
            if(Math.abs(time - timet) < 2000) {
                if(ball.x >= paddle.x && ball.x <= paddle.x + paddle.width && ball.y >= paddle.y-10 && ball.y <= paddle.y+10 + 15) {
                    paddle.accel_left();
                    ball.move();
                }
            }
            time = new Date().getTime();
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            paddle.move_right();
            timet = new Date().getTime();
            if(Math.abs(time - timet) < 2000) {
                if(ball.x >= paddle.x && ball.x <= paddle.x + paddle.width && ball.y >= paddle.y-10 && ball.y <= paddle.y+10 + 15) {
                    paddle.accel_right();
                    ball.move();
                }
            }
            time = new Date().getTime();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    Date date = new Date();
    
    public Bong() {
        j.requestFocus();
        j.setLayout(null);
        j.setBounds(0, 0, 1000, 700);
        p.setBounds(j.getBounds());
        j.add(p);
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        j.setVisible(true);
        j.addKeyListener(this);
        draw();
        setBall();
        play();
    }
    
    public class Paddle {
        int x, y;
        int width = 140;
        int accel = 0;
        public Paddle() {
            x = 450;
            y = 630;
        }
        public void draw() {
            Graphics g = p.getGraphics();
            g.setColor(Color.yellow);
            g.drawLine(x, y, x+width, y);
        }
        public void accel_left() {
            accel = -4;
            ball.plusx -= 6;
        }
        public void accel_right() {
            accel = 4;
            ball.plusx += 6;
        }
        public void move_left() {
            x-=20;
        }
        public void move_right() {
            x+=20;
        }
    }
    
    public class Ball {
        int x, y;
        int plusx, plusy;
        
        public Ball() {
            plusx = 1;
            plusy = 4;
        }
        
        public void move() {
            if(lives == 0) {
                JOptionPane.showMessageDialog(null, "Game Over");
                System.exit(0);
            }
            if(y > 1000) {
                y = 30;
                ball.plusx = 7;
                ball.plusy = 12;
                lives--;
            }
            if(x <= 10) {
                plusx = -plusx;
            }
            if(x >= 970) {
                plusx = -plusx;
            }
            if(y <= 20) {
                plusy = -plusy;
            }
            for(int i=0; i<squares.size(); i++) {
                if(x >= squares.get(i).x && x <= squares.get(i).x + squares.get(i).width &&
                    y >= squares.get(i).y && y <= squares.get(i).y + squares.get(i).height) {
                    hits++;
                    squares.remove(squares.get(i));
                    plusy = -plusy;
                    try {
                        makeSound("pew.wav");
                                
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if(x >= paddle.x && x <= paddle.x + paddle.width && y >= paddle.y-10 && y <= paddle.y+10 + 15) {
                plusy = -plusy;
                hits++;
            }
            x+=plusx;
            y+=plusy;
        }
    }
    
    public void setBall() {
        ball = new Ball();
        
        ball.x = 500;
        ball.y = 250;
        
        ball.plusx = 7;
        ball.plusy = 12;
    }
    
    public void drawSquares() {
        Graphics g = p.getGraphics();
        for(int i=0; i<squares.size(); i++) {
            g.setColor(squares.get(i).color);
            g.fillRect(squares.get(i).x, squares.get(i).y, squares.get(i).width, squares.get(i).height);
            g.setColor(Color.black);
            g.drawRect(squares.get(i).x, squares.get(i).y, squares.get(i).width, squares.get(i).height);
        }
    }
    
    int level = 1;
    
    private void makeSound(String file) throws Exception {

        File audioFile = new File(file);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

        AudioFormat format = audioStream.getFormat();

        DataLine.Info info = new DataLine.Info(Clip.class, format);
        Clip audioClip = (Clip) AudioSystem.getLine(info);

        audioClip.open(audioStream);
        audioClip.start();
        audioStream.close();
    }

    public void play() {
        for(int i=0; i<10; i++) {
            Square sq = new Square();
            sq.x = i*sq.width + 100;
            sq.y = 50;
            Random random = new Random();
            Color color = null;
            int v = random.nextInt(5);
            if(v == 0) {
                color = Color.BLUE;
            }
            if(v == 1) {
                color = Color.GREEN;
            }
            if(v == 2) {
                color = Color.YELLOW;
            }
            if(v == 3) {
                color = Color.ORANGE;
            }
            if(v == 4) {
                color = Color.RED;
            }
            sq.color = color;
            squares.add(sq);
        }
        Thread t = new Thread() {
            public void run() {
                while(true) {
                    if(level == 2 && squares.size() == 0) {
                        level = 3;
        for(int i=0; i<10; i++) {
            Square sq = new Square();
            sq.x = i*sq.width + 100;
            sq.y = 150;
            Random random = new Random();
            Color color = null;
            int v = random.nextInt(5);
            if(v == 0) {
                color = Color.BLUE;
            }
            if(v == 1) {
                color = Color.GREEN;
            }
            if(v == 2) {
                color = Color.YELLOW;
            }
            if(v == 3) {
                color = Color.ORANGE;
            }
            if(v == 4) {
                color = Color.RED;
            }
            sq.color = color;
            squares.add(sq);
        }
                        for(int i=0; i<10; i++) {
            Square sq = new Square();
            sq.x = i*sq.width + 100;
            sq.y = 50;
            Random random = new Random();
            Color color = null;
            int v = random.nextInt(5);
            if(v == 0) {
                color = Color.BLUE;
            }
            if(v == 1) {
                color = Color.GREEN;
            }
            if(v == 2) {
                color = Color.YELLOW;
            }
            if(v == 3) {
                color = Color.ORANGE;
            }
            if(v == 4) {
                color = Color.RED;
            }
            sq.color = color;
            squares.add(sq);
        }
        for(int i=0; i<10; i++) {
            Square sq = new Square();
            sq.x = i*sq.width + 100;
            sq.y = 100;
            Random random = new Random();
            Color color = null;
            int v = random.nextInt(5);
            if(v == 0) {
                color = Color.BLUE;
            }
            if(v == 1) {
                color = Color.GREEN;
            }
            if(v == 2) {
                color = Color.YELLOW;
            }
            if(v == 3) {
                color = Color.ORANGE;
            }
            if(v == 4) {
                color = Color.RED;
            }
            sq.color = color;
            squares.add(sq);
        }
                    }
                    if(level == 3 && squares.size() == 0) {
                        level = 4;
        for(int i=0; i<10; i++) {
            Square sq = new Square();
            sq.x = i*sq.width + 100;
            sq.y = 150;
            Random random = new Random();
            Color color = null;
            int v = random.nextInt(5);
            if(v == 0) {
                color = Color.BLUE;
            }
            if(v == 1) {
                color = Color.GREEN;
            }
            if(v == 2) {
                color = Color.YELLOW;
            }
            if(v == 3) {
                color = Color.ORANGE;
            }
            if(v == 4) {
                color = Color.RED;
            }
            sq.color = color;
            squares.add(sq);
        }
                        for(int i=0; i<10; i++) {
            Square sq = new Square();
            sq.x = i*sq.width + 100;
            sq.y = 50;
            Random random = new Random();
            Color color = null;
            int v = random.nextInt(5);
            if(v == 0) {
                color = Color.BLUE;
            }
            if(v == 1) {
                color = Color.GREEN;
            }
            if(v == 2) {
                color = Color.YELLOW;
            }
            if(v == 3) {
                color = Color.ORANGE;
            }
            if(v == 4) {
                color = Color.RED;
            }
            sq.color = color;
            squares.add(sq);
        }
        for(int i=0; i<10; i++) {
            Square sq = new Square();
            sq.x = i*sq.width + 100;
            sq.y = 100;
            Random random = new Random();
            Color color = null;
            int v = random.nextInt(5);
            if(v == 0) {
                color = Color.BLUE;
            }
            if(v == 1) {
                color = Color.GREEN;
            }
            if(v == 2) {
                color = Color.YELLOW;
            }
            if(v == 3) {
                color = Color.ORANGE;
            }
            if(v == 4) {
                color = Color.RED;
            }
            sq.color = color;
            squares.add(sq);
        }
        for(int i=0; i<8; i++) {
            Square sq = new Square();
            sq.x = i*sq.width + 100;
            sq.y = 200;
            Random random = new Random();
            Color color = null;
            int v = random.nextInt(5);
            if(v == 0) {
                color = Color.BLUE;
            }
            if(v == 1) {
                color = Color.GREEN;
            }
            if(v == 2) {
                color = Color.YELLOW;
            }
            if(v == 3) {
                color = Color.ORANGE;
            }
            if(v == 4) {
                color = Color.RED;
            }
            sq.color = color;
            squares.add(sq);
        }
                    }
                    if(level == 1 && squares.size() == 0) {
                        level = 2;
        for(int i=0; i<10; i++) {
            Square sq = new Square();
            sq.x = i*sq.width + 100;
            sq.y = 50;
            Random random = new Random();
            Color color = null;
            int v = random.nextInt(5);
            if(v == 0) {
                color = Color.BLUE;
            }
            if(v == 1) {
                color = Color.GREEN;
            }
            if(v == 2) {
                color = Color.YELLOW;
            }
            if(v == 3) {
                color = Color.ORANGE;
            }
            if(v == 4) {
                color = Color.RED;
            }
            sq.color = color;
            squares.add(sq);
        }
        for(int i=0; i<10; i++) {
            Square sq = new Square();
            sq.x = i*sq.width + 100;
            sq.y = 150;
            Random random = new Random();
            Color color = null;
            int v = random.nextInt(5);
            if(v == 0) {
                color = Color.BLUE;
            }
            if(v == 1) {
                color = Color.GREEN;
            }
            if(v == 2) {
                color = Color.YELLOW;
            }
            if(v == 3) {
                color = Color.ORANGE;
            }
            if(v == 4) {
                color = Color.RED;
            }
            sq.color = color;
            squares.add(sq);
        }
                    }
                    j.setTitle("Alleyway lives: " + lives + " hits: " + hits);
                    try {
                        Thread.sleep(50);
                        draw();
                        drawSquares();
                        ball.move();
                        paddle.draw();
                        drawBall();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }
    
    private void drawBall() {
        Graphics g = p.getGraphics();
        g.setColor(Color.white);
        g.fillOval(ball.x, ball.y, 15, 15);
    }
    
    public void draw() {
        Graphics g = p.getGraphics();
        try {
            Image img = ImageIO.read(getClass().getResourceAsStream("bg.jpg"));
            g.drawImage(img, 0, 0, 1000, 700, null);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new Bong();
    }
}
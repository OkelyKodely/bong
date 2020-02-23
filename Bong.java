package bong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Bong implements KeyListener {

    JFrame j = new JFrame();
    
    JPanel p = new JPanel();
    
    Ball ball;
    
    Paddle paddle = new Paddle();
    
    int hits = 0;
    
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
            if(Math.abs(time - timet) < 5) {
                if(ball.x >= paddle.x && ball.x <= paddle.x + paddle.width && ball.y >= paddle.y-10 && ball.y <= paddle.y-10 + 15) {
                    paddle.accel_left();
                    ball.move();
                }
            }
            time = new Date().getTime();
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            paddle.move_right();
            timet = new Date().getTime();
            if(Math.abs(time - timet) < 5) {
                if(ball.x >= paddle.x && ball.x <= paddle.x + paddle.width && ball.y >= paddle.y-10 && ball.y <= paddle.y-10 + 15) {
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
        int width = 200;
        int accel = 0;
        public Paddle() {
            x = 450;
            y = 630;
        }
        public void draw() {
            Graphics g = p.getGraphics();
            g.setColor(Color.black);
            g.drawLine(x, y, x+width, y);
        }
        public void accel_left() {
            accel = -4;
            ball.plusx -= 3;
        }
        public void accel_right() {
            accel = 4;
            ball.plusx += 3;
        }
        public void move_left() {
            x-=30;
        }
        public void move_right() {
            x+=30;
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
            if(y > 1000)
                y = 30;
            if(y > 1000)
                ball.plusx = 2;
            if(y > 1000)
                ball.plusy = 6;
            if(x <= 10) {
                plusx = -plusx;
            }
            if(x >= 970) {
                plusx = -plusx;
            }
            if(y <= 20) {
                plusy = -plusy;
            }
            if(x >= paddle.x && x <= paddle.x + paddle.width && y >= paddle.y-10 && y <= paddle.y-10 + 15) {
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
        ball.y = 50;
        
        ball.plusx = 2;
        ball.plusy = 6;
    }
    
    public void play() {
        Thread t = new Thread() {
            public void run() {
                while(true) {
                    j.setTitle("hits: " + hits);
                    try {
                        Thread.sleep(50);
                        draw();
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
        g.setColor(Color.red);
        g.fillOval(ball.x, ball.y, 20, 20);
    }
    
    public void draw() {
        Graphics g = p.getGraphics();
        g.setColor(Color.cyan);
        g.fillRect(0, 0, 1000, 700);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new Bong();
    }
    
}

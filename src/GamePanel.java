import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {
	int screenwidth = 600;
	int screenheight = 600;
	int unitsize = 25;
	int gameunit = (screenwidth * screenheight) / unitsize;
	int x[] = new int[gameunit];
	int y[] = new int[gameunit];
	int bodyparts = 6;
	int applecount = 0;
	char direction = 'R';
	boolean running = false;
	
	Random random;
	Timer timer;
	int delay = 100;
	
	int applex;
	int appley;
	
	
	GamePanel() {
		this.setPreferredSize(new Dimension(screenwidth, screenheight));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		running = true;
		StartGame();
	}
	
	public void StartGame() {
		resetGame();
		
		random = new Random();
		timer = new Timer(delay, this);
		firstApple();
		timer.start();
	}
	
	public void resetGame() {
		bodyparts = 6;
		applecount = 0;
		direction = 'R';
		running = true;
		
		for(int i = 0; i < gameunit; i++) {
			x[i] = 0;
		}
		for(int i = 0; i < gameunit; i++) {
			y[i] = 0;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkApple();
			checkCollision();
			repaint();
		}
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		if(running) {
			score(g);
			
			g.setColor(Color.red);
			g.fillRect(applex, appley, unitsize, unitsize);
			
			for(int i = 0; i < bodyparts; i++) {
				if(i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], unitsize, unitsize);
				} else {
					g.setColor(new Color(45, 180, 0));
					g.fillRect(x[i], y[i], unitsize, unitsize);
				}
			}
		} else {
			gameOver(g);
		}
	}
	
	public void firstApple() {
		applex = (random.nextInt(screenwidth/unitsize)) * unitsize;
		appley = (random.nextInt(screenwidth/unitsize)) * unitsize;
	}
	
	public void newApple() {
		boolean chave = false;
		do {
			chave = false;
			
			applex = (random.nextInt(screenwidth/unitsize)) * unitsize;
			appley = (random.nextInt(screenwidth/unitsize)) * unitsize;
			
			for(int i = 0; i < bodyparts; i++) {
				if(x[i] == applex && y[i] == appley) {
					chave = true;
				} 
			}
		} while(chave);
	}
	
	public void checkApple() {
		if(x[0] == applex && y[0] == appley) {
			newApple();
			bodyparts += 1;
			applecount += 1;
		}
	}
	
	public void move() {
		for(int i = bodyparts; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}
		switch(direction) {
		case 'U' :
			y[0] = y[0] - unitsize;
			break;
		case 'D' :
			y[0] = y[0] + unitsize;
			break;
		case 'L' :
			x[0] = x[0] - unitsize;
			break;
		case 'R' :
			x[0] = x[0] + unitsize;
			break;
		}
	}
	
	public void checkCollision() {
		if(x[0]  >= screenwidth) {
			running = false;
		}
		if(x[0]  < 0) {
			running = false;
		}
		if(y[0]  >= screenheight) {
			running = false;
		}
		if(y[0]  < 0) {
			running = false;
		}
		
		for(int i = bodyparts; i > 0; i--) {
			if(x[0] == x[i] && y[0] == y[i]) {
				running = false;
			}
		}
		if(!running) {
			timer.stop();
		}
	}
	
	public void gameOver(Graphics g) {
		score(g);
		
		g.setFont(new Font("Arial", Font.PLAIN, 34));
		FontMetrics fontmetrics = getFontMetrics(g.getFont());
		g.setColor(Color.white);
		g.drawString("GAME OVER", ((screenwidth - fontmetrics.stringWidth("GAME OVER"))/2), (screenheight / 2));
		
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		FontMetrics fontmetrics1 = getFontMetrics(g.getFont());
		g.setColor(Color.white);
		g.drawString("Press Enter to Restart", ((screenwidth - fontmetrics1.stringWidth("Press Enter to Restart")) / 2), ((screenheight / 2) + unitsize));
	}
	
	public void score(Graphics g) {
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		FontMetrics fontmetrics = getFontMetrics(g.getFont());
		g.setColor(Color.white);
		g.drawString("score: " + applecount, ((screenwidth - fontmetrics.stringWidth("score: " + applecount)) - unitsize), unitsize);
	}

	public class MyKeyAdapter extends KeyAdapter {
		boolean chave = true;
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_W:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_S:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			case KeyEvent.VK_A:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_D:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_ENTER:
				if(!running) {
					StartGame();
				}
				break;
			case KeyEvent.VK_ESCAPE:
				if(chave) {
					timer.stop();
					chave = false;
				} else {
					timer.start();
					chave = true;
				}
				break;
			}
		}
	}
	
}

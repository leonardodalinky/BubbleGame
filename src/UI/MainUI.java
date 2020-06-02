package UI;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import game.SceneManager;
import scene.Scene;
import javax.swing.*;

public class MainUI {

	private JFrame frame;
	private JPanel panel;
	//private BufferedImage buf_image = new BufferedImage(SceneManager.GAME_SOLUTION_X, SceneManager.GAME_SOLUTION_Y, BufferedImage.TYPE_4BYTE_ABGR);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainUI window = new MainUI();
					SceneManager sceneManager = SceneManager.getInstance();
					window.frame.setVisible(true);
					(new Thread() {
						@Override
						public void run() {
							sceneManager.init(window.panel, 60);
							sceneManager.start();	
						}
					}).start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("泡泡堂小游戏");
		frame.setResizable(false);

		frame.setBounds(100, 100, 1000, 792);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setPreferredSize(new Dimension(SceneManager.GAME_SOLUTION_X, SceneManager.GAME_SOLUTION_Y));
		frame.pack();
//		System.out.println(frame.getContentPane().getSize());
		
		panel = (new JPanel() {
			@Override
			public void paint(Graphics g) {
				// 双缓冲
				Image buf_image = createImage(SceneManager.GAME_SOLUTION_X, SceneManager.GAME_SOLUTION_Y);
				Graphics g2 = buf_image.getGraphics();
//				g2.clearRect(0, 0, SceneManager.GAME_SOLUTION_X, SceneManager.GAME_SOLUTION_Y);
				SceneManager.getInstance().paint(g2);
				g.drawImage(buf_image, 0, 0, SceneManager.GAME_SOLUTION_X, SceneManager.GAME_SOLUTION_Y, null);
			}
		});
		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_W:
				case KeyEvent.VK_UP:
					SceneManager.getInstance().getAttribute().setKeyPressed(Scene.KEY_UP);
					break;
				case KeyEvent.VK_S:
				case KeyEvent.VK_DOWN:
					SceneManager.getInstance().getAttribute().setKeyPressed(Scene.KEY_DOWN);
					break;
				case KeyEvent.VK_A:
				case KeyEvent.VK_LEFT:
					SceneManager.getInstance().getAttribute().setKeyPressed(Scene.KEY_LEFT);
					break;
				case KeyEvent.VK_D:
				case KeyEvent.VK_RIGHT:
					SceneManager.getInstance().getAttribute().setKeyPressed(Scene.KEY_RIGHT);
					break;
				case KeyEvent.VK_SPACE:
					SceneManager.getInstance().getAttribute().setKeyPressed(Scene.KEY_SPACE);
					break;
				default:
					break;
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_W:
				case KeyEvent.VK_UP:
					SceneManager.getInstance().getAttribute().setKeyReleased(Scene.KEY_UP);
					break;
				case KeyEvent.VK_S:
				case KeyEvent.VK_DOWN:
					SceneManager.getInstance().getAttribute().setKeyReleased(Scene.KEY_DOWN);
					break;
				case KeyEvent.VK_A:
				case KeyEvent.VK_LEFT:
					SceneManager.getInstance().getAttribute().setKeyReleased(Scene.KEY_LEFT);
					break;
				case KeyEvent.VK_D:
				case KeyEvent.VK_RIGHT:
					SceneManager.getInstance().getAttribute().setKeyReleased(Scene.KEY_RIGHT);
					break;
				case KeyEvent.VK_SPACE:
					SceneManager.getInstance().getAttribute().setKeyReleased(Scene.KEY_SPACE);
					break;
				default:
					break;
				}
			}
		});
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					SceneManager.getInstance().getAttribute().setMouseClickEvent(e.getX(), e.getY());
				}
			}
		});
		panel.setBounds(0, 0, SceneManager.GAME_SOLUTION_X, SceneManager.GAME_SOLUTION_Y);
		frame.getContentPane().add(panel);
	}
	public JPanel getPanel() {
		return panel;
	}
}

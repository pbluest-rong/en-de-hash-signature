package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import controller.Controller;

import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

public class MainView extends JFrame {
	private JPanel contentPane;
	private Controller controller;

	/**
	 * Create the frame.
	 */
	public MainView() {
		this.controller = new Controller();

		setTitle("ATBMHTTT - Pblues");
		try {
			Image logo = ImageIO.read(new File("logo.png"));
			setIconImage(logo);
		} catch (IOException e) {
			e.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		setSize(800, 700);
		setLocationRelativeTo(null);
		contentPane.setLayout(new BorderLayout());
		JPanel panelMain = new JPanel(new GridLayout(2, 1));
		contentPane.add(panelMain, BorderLayout.CENTER);

		panelMain.add(this.controller.panelSelectAlgorithms);
		panelMain.add(this.controller.panelGenLoadKey);
		panelMain.add(this.controller.panelEnDe);
		panelMain.add(this.controller.panelCheckFile);

		JPanel progressPanel = new JPanel(new BorderLayout());
		progressPanel.setVisible(true);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressBar.setString("Loading, please wait a few seconds 😊");
		progressBar.setFont(progressBar.getFont().deriveFont(16f));
		progressBar.setStringPainted(true);
		progressBar.setVisible(false);

		progressPanel.add(progressBar);
		progressPanel.setPreferredSize(new Dimension(contentPane.getWidth(), 20));
		contentPane.add(progressPanel, BorderLayout.NORTH);
		this.controller.setProgressBar(progressBar);

		this.setVisible(true);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					MainView frame = new MainView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}

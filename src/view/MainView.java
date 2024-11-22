package view;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
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

import config.GlobalConstants;
import controller.Controller;

import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JLabel;

public class MainView extends JFrame {
	private JPanel contentPane;
	private Controller controller;
	public JButton btn_signature;

	/**
	 * Create the frame.
	 */
	public MainView() {
		this.controller = new Controller();

		setTitle(GlobalConstants.APP_TITLE);
		try {
			Image logo = ImageIO.read(new File(GlobalConstants.LOGO_FILE_PATH));
			setIconImage(logo);
		} catch (IOException e) {
			e.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		setSize(GlobalConstants.DEFAULT_APP_WIDTH, GlobalConstants.DEFAULT_APP_HEIGHT);
		setLocationRelativeTo(null);
		contentPane.setLayout(new BorderLayout());
		JPanel panelMain = new JPanel(new GridLayout(2, 1));
		panelMain.setBorder(new EmptyBorder(0, 0, 5, 0));
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

		progressPanel.add(progressBar, BorderLayout.CENTER);
		progressPanel.setPreferredSize(new Dimension(contentPane.getWidth(), 20));
		contentPane.add(progressPanel, BorderLayout.NORTH);
		this.controller.setProgressBar(progressBar);

		JButton pdfButton = new JButton("Guide");
		pdfButton.setBorderPainted(false);
		pdfButton.setOpaque(true);
		pdfButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		pdfButton.setBackground(new Color(255, 223, 0));
		pdfButton.setForeground(Color.BLACK);
		pdfButton.setFont(new Font("Arial", Font.BOLD, 14)); // Bold font
		pdfButton.setFocusPainted(false);
		pdfButton.addActionListener(e -> {
			try {
				Desktop.getDesktop().open(new File(GlobalConstants.GUIDE_FILE_PATH));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});

		progressPanel.add(pdfButton, BorderLayout.EAST);

		btn_signature = new JButton("Signature");
		btn_signature.setFont(new Font("Tahoma", Font.BOLD, GlobalConstants.FONT_SIZE_3));
		btn_signature.addActionListener(e -> {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						JFrame frame = new JFrame("Signature Application");
						frame.setSize(GlobalConstants.DEFAULT_APP_WIDTH, GlobalConstants.DEFAULT_APP_HEIGHT);
						frame.setLocationRelativeTo(null);
						frame.getContentPane().add(new PanelSignature());
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		});
		contentPane.add(btn_signature, BorderLayout.SOUTH);
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

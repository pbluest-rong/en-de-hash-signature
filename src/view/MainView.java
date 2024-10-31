package view;

import java.awt.EventQueue;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.swing.border.LineBorder;
import java.awt.Color;

public class MainView extends JFrame {
	private JPanel contentPane;
	private JPanel panelGenKey;
	private JPanel panelSelectAlgorithms;
	private JPanel panelEnDe;
	private JPanel panelCheckFile;

	/**
	 * Create the frame.
	 */
	public MainView() {
		panelGenKey = new PanelGenKey();
		panelSelectAlgorithms = new PanelSelectAlgorithms();
		panelEnDe = new PanelEnDe();
		panelCheckFile = new PanelCheckFile();
		
		setTitle("ATBMHTTT - Pblues");
		try {
            Image logo = ImageIO.read(new File("logo.png"));
            setIconImage(logo);
        } catch (IOException e) {
            e.printStackTrace();
        }
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		setSize(800,600);
		setLocationRelativeTo(null);
		contentPane.setLayout(new GridLayout(2, 1));
		

		contentPane.add(panelSelectAlgorithms);
		contentPane.add(panelGenKey);
		contentPane.add(panelEnDe);
		contentPane.add(panelCheckFile);
		
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

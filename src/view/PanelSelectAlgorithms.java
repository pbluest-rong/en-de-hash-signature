package view;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import java.awt.CardLayout;
import javax.swing.border.TitledBorder;

import config.GlobalConstants;
import controller.Controller;

import javax.swing.border.EtchedBorder;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
import javax.swing.JLabel;

public class PanelSelectAlgorithms extends JPanel {
	public ActionListener actionListener;
	public JComboBox cbb_algorithm;
	public JRadioButton rdb_basic_symmetric;
	public JRadioButton rdb_modern_symmetric;
	public JRadioButton rdb_asymmetric;
	public JRadioButton rdb_modern_symmetric_bouncy_castle;

	public PanelSelectAlgorithms(Controller controller) {
		this.actionListener = controller;
		initUI();
		addEventHandle();
	}

	/**
	 * Create the panel.
	 */
	public void initUI() {
		setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"Select Algorithms", TitledBorder.CENTER, TitledBorder.TOP, null, Color.BLUE));
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(4, 1, 0, 0));

		rdb_basic_symmetric = new JRadioButton("Basic Symmetric");
		rdb_basic_symmetric.setCursor(new Cursor(Cursor.HAND_CURSOR));
		rdb_basic_symmetric.setFont(new Font("Tahoma", Font.BOLD, GlobalConstants.FONT_SIZE_2));
		panel.add(rdb_basic_symmetric);

		rdb_modern_symmetric = new JRadioButton("Modern Symmetric");
		rdb_modern_symmetric.setCursor(new Cursor(Cursor.HAND_CURSOR));
		rdb_modern_symmetric.setFont(new Font("Tahoma", Font.BOLD, GlobalConstants.FONT_SIZE_2));
		panel.add(rdb_modern_symmetric);

		rdb_modern_symmetric_bouncy_castle = new JRadioButton("Bouncy Castle");
		rdb_modern_symmetric_bouncy_castle.setCursor(new Cursor(Cursor.HAND_CURSOR));
		rdb_modern_symmetric_bouncy_castle.setFont(new Font("Tahoma", Font.BOLD, GlobalConstants.FONT_SIZE_2));
		panel.add(rdb_modern_symmetric_bouncy_castle);

		rdb_asymmetric = new JRadioButton("Asymmetric");
		rdb_asymmetric.setCursor(new Cursor(Cursor.HAND_CURSOR));
		rdb_asymmetric.setFont(new Font("Tahoma", Font.BOLD, GlobalConstants.FONT_SIZE_2));
		panel.add(rdb_asymmetric);

		ButtonGroup bg = new ButtonGroup();
		bg.add(rdb_basic_symmetric);
		bg.add(rdb_modern_symmetric);
		bg.add(rdb_asymmetric);
		bg.add(rdb_modern_symmetric_bouncy_castle);

		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));

		cbb_algorithm = new JComboBox();
		cbb_algorithm.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, GlobalConstants.FONT_SIZE_3));
		panel_1.add(cbb_algorithm, BorderLayout.NORTH);
	}

	private void addEventHandle() {
		// Gắn sự kiện ActionListener cho các thành phần
		rdb_basic_symmetric.addActionListener(actionListener);
		rdb_modern_symmetric.addActionListener(actionListener);
		rdb_asymmetric.addActionListener(actionListener);
		rdb_modern_symmetric_bouncy_castle.addActionListener(actionListener);
		cbb_algorithm.addActionListener(actionListener);
	}
}

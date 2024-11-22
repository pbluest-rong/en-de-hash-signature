package view;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import config.GlobalConstants;
import controller.Controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.border.EtchedBorder;
import javax.swing.JComboBox;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import javax.swing.JRadioButton;

public class PanelGenLoadKey extends JPanel {
	public JTextField tf_file_path;
	public JButton btn_open_file;
	public JComboBox cbb_key_size;
	public JButton btn_auto_key_genereration;
	public ActionListener actionListener;
	public JLabel lbl_fileKey;
	public JRadioButton rdo_save_key;
	public JRadioButton rdo_not_save_key;
	private JPanel panel_4;
	private JPanel panel_5;
	public JLabel lbl_modes;
	public JComboBox cbb_modes;
	private JPanel panel_6;
	public JLabel lbl_key_size;
	public JLabel lbl_padding;
	public JComboBox cbb_padding;

	public PanelGenLoadKey(Controller controller) {
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
				"Generate & load key", TitledBorder.CENTER, TitledBorder.TOP, null, Color.BLUE));
		this.setSize(460, 313);
		setLayout(new BorderLayout(0, 0));

		JPanel panel_north = new JPanel();
		panel_north.setLayout(new BorderLayout(0, 0));
		add(panel_north, BorderLayout.NORTH);

		JPanel panel_0 = new JPanel(new BorderLayout(0, 0));
		panel_0.setBorder(new EmptyBorder(20, 0, 0, 0));
		JPanel panel_1 = new JPanel(new BorderLayout(0, 0));
		JPanel panel_2 = new JPanel(new BorderLayout(0, 0));
		panel_2.setBorder(new EmptyBorder(10, 0, 0, 0));
		panel_0.add(panel_1, BorderLayout.NORTH);
		panel_0.add(panel_2, BorderLayout.SOUTH);
		panel_north.add(panel_0, BorderLayout.CENTER);

		lbl_fileKey = new JLabel("File key: ");
		lbl_fileKey.setFont(new Font("Tahoma", Font.BOLD, GlobalConstants.FONT_SIZE_0));
		panel_2.add(lbl_fileKey, BorderLayout.WEST);

		tf_file_path = new JTextField();
		tf_file_path.setEditable(false);
		tf_file_path.setFont(new Font("Tahoma", Font.PLAIN, GlobalConstants.FONT_SIZE_0));
		panel_2.add(tf_file_path, BorderLayout.CENTER);
		tf_file_path.setColumns(10);

		btn_open_file = new JButton("Open");
		btn_open_file.setFont(new Font("Tahoma", Font.BOLD, GlobalConstants.FONT_SIZE_0));
		panel_2.add(btn_open_file, BorderLayout.EAST);

		lbl_key_size = new JLabel("Key size: ");
		lbl_key_size.setFont(new Font("Tahoma", Font.BOLD, GlobalConstants.FONT_SIZE_0));
		panel_1.add(lbl_key_size, BorderLayout.WEST);

		cbb_key_size = new JComboBox();
		cbb_key_size.setFont(new Font("Tahoma", Font.BOLD, GlobalConstants.FONT_SIZE_3));
		panel_1.add(cbb_key_size, BorderLayout.CENTER);

		btn_auto_key_genereration = new JButton("Auto Key Generation");
		btn_auto_key_genereration.setFont(new Font("Tahoma", Font.BOLD, GlobalConstants.FONT_SIZE_0));

		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(0, 10, 0, 0));
		panel.setLayout(new BorderLayout(0, 0));
		panel.add(btn_auto_key_genereration);
		panel_1.add(panel, BorderLayout.EAST);

		JPanel panel_3 = new JPanel();
		panel.add(panel_3, BorderLayout.NORTH);

		rdo_save_key = new JRadioButton("Save Key");
		rdo_save_key.setSelected(true);
		rdo_save_key.setFont(new Font("Tahoma", Font.PLAIN, GlobalConstants.FONT_SIZE_0));
		panel_3.add(rdo_save_key);

		rdo_not_save_key = new JRadioButton("Don't Save Key");
		rdo_not_save_key.setFont(new Font("Tahoma", Font.PLAIN, GlobalConstants.FONT_SIZE_0));
		panel_3.add(rdo_not_save_key);

		ButtonGroup bg = new ButtonGroup();
		bg.add(rdo_not_save_key);
		bg.add(rdo_save_key);

		panel_4 = new JPanel();
		panel_north.add(panel_4, BorderLayout.NORTH);
		panel_4.setLayout(new BorderLayout(0, 0));

		panel_5 = new JPanel();
		panel_5.setBorder(new EmptyBorder(5, 0, 5, 0));
		panel_4.add(panel_5, BorderLayout.NORTH);
		panel_5.setLayout(new BorderLayout(0, 0));

		lbl_modes = new JLabel("Modes:    ");
		lbl_modes.setFont(new Font("Tahoma", Font.BOLD, GlobalConstants.FONT_SIZE_0));
		panel_5.add(lbl_modes, BorderLayout.WEST);

		cbb_modes = new JComboBox();
		cbb_modes.setFont(new Font("Tahoma", Font.BOLD, GlobalConstants.FONT_SIZE_3));
		panel_5.add(cbb_modes, BorderLayout.CENTER);

		panel_6 = new JPanel();
		panel_6.setBorder(new EmptyBorder(0, 0, 5, 0));
		panel_4.add(panel_6, BorderLayout.SOUTH);
		panel_6.setLayout(new BorderLayout(0, 0));

		lbl_padding = new JLabel("Padding:   ");
		lbl_padding.setFont(new Font("Tahoma", Font.BOLD, GlobalConstants.FONT_SIZE_0));
		panel_6.add(lbl_padding, BorderLayout.WEST);

		cbb_padding = new JComboBox();
		cbb_padding.setFont(new Font("Tahoma", Font.BOLD, GlobalConstants.FONT_SIZE_3));
		panel_6.add(cbb_padding, BorderLayout.CENTER);
	}

	private void addEventHandle() {
		this.cbb_key_size.addActionListener(actionListener);
		this.btn_auto_key_genereration.addActionListener(actionListener);
		this.btn_open_file.addActionListener(actionListener);
		this.cbb_modes.addActionListener(actionListener);
		this.cbb_padding.addActionListener(actionListener);
	}

}

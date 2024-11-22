package view;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import config.GlobalConstants;
import controller.Controller;

import javax.swing.border.EtchedBorder;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;

public class PanelCheckFile extends JPanel {

	private static final long serialVersionUID = 1L;
	public JTextField tf_input_file2;
	public ActionListener actionListener;
	public JTextField tf_input_text;
	public JTextField tf_output_text;
	public JButton btn_open_file2;
	public JButton btn_hash_file2;
	public JButton btn_hash_text;
	public JComboBox cbb_type;
	private JPanel panel_north_2;
	private JPanel panel;
	private JLabel lblNewLabel;
	public JTextField tf_input_file1;
	public JButton btn_open_file1;
	public JButton btn_hash_file1;
	private JPanel panel_1;
	public JTextField tf_output_file1;
	public JTextField tf_output_file2;

	public PanelCheckFile(Controller controller) {
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
				"Check file", TitledBorder.CENTER, TitledBorder.TOP, null, Color.BLUE));
		this.setSize(460, 313);
		setLayout(new BorderLayout(0, 0));

		JPanel panel_center = new JPanel();
		panel_center.setLayout(new BorderLayout(0, 0));

		JPanel panel_south_1 = new JPanel();
		panel_south_1.setBorder(new EmptyBorder(5, 5, 10, 5));
		panel_south_1.setLayout(new BorderLayout(0, 0));

		JPanel panel_center_2 = new JPanel();
		panel_center_2.setLayout(new BorderLayout(0, 0));
		panel_center.add(panel_center_2, BorderLayout.SOUTH);
		JLabel lblNewLabel2 = new JLabel("File 2: ");
		lblNewLabel2.setFont(new Font("Tahoma", Font.BOLD, GlobalConstants.FONT_SIZE_0));
		panel_center_2.add(lblNewLabel2, BorderLayout.WEST);

		tf_input_file2 = new JTextField();
		tf_input_file2.setFont(new Font("Tahoma", Font.PLAIN, GlobalConstants.FONT_SIZE_0));
		tf_input_file2.setColumns(10);
		panel_center_2.add(tf_input_file2, BorderLayout.CENTER);

		btn_open_file2 = new JButton("Open");
		btn_open_file2.setFont(new Font("Tahoma", Font.BOLD, GlobalConstants.FONT_SIZE_0));
		panel_center_2.add(btn_open_file2, BorderLayout.EAST);

		btn_hash_file2 = new JButton("Hash File 2");
		btn_hash_file2.setFont(new Font("Tahoma", Font.BOLD, GlobalConstants.FONT_SIZE_3));
		panel_center_2.add(btn_hash_file2, BorderLayout.SOUTH);

		panel_center.add(panel_south_1, BorderLayout.NORTH);
		
		panel_north_2 = new JPanel();
		panel_north_2.setBorder(new EmptyBorder(0, 0, 10, 0));
		panel_center_2.add(panel_north_2, BorderLayout.NORTH);
		panel_north_2.setLayout(new BorderLayout(0, 0));
		
		panel = new JPanel();
		panel_north_2.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		lblNewLabel = new JLabel("File 1: ");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, GlobalConstants.FONT_SIZE_0));
		panel.add(lblNewLabel, BorderLayout.WEST);
		
		tf_input_file1 = new JTextField();
		tf_input_file1.setFont(new Font("Tahoma", Font.PLAIN, GlobalConstants.FONT_SIZE_0));
		panel.add(tf_input_file1, BorderLayout.CENTER);
		tf_input_file1.setColumns(10);
		
		btn_open_file1 = new JButton("Open");
		btn_open_file1.setFont(new Font("Tahoma", Font.BOLD, GlobalConstants.FONT_SIZE_0));
		panel.add(btn_open_file1, BorderLayout.EAST);
		
		btn_hash_file1 = new JButton("Hash File 1");
		btn_hash_file1.setFont(new Font("Tahoma", Font.BOLD, GlobalConstants.FONT_SIZE_3));
		panel.add(btn_hash_file1, BorderLayout.SOUTH);
		add(panel_center, BorderLayout.CENTER);

		JLabel lbl_text = new JLabel("Text:   ");
		lbl_text.setFont(new Font("Tahoma", Font.BOLD, GlobalConstants.FONT_SIZE_0));
		panel_south_1.add(lbl_text, BorderLayout.WEST);

		tf_input_text = new JTextField();
		tf_input_text.setFont(new Font("Tahoma", Font.PLAIN, GlobalConstants.FONT_SIZE_0));
		panel_south_1.add(tf_input_text, BorderLayout.CENTER);
		tf_input_text.setColumns(10);

		btn_hash_text = new JButton("Hash Text");
		btn_hash_text.setFont(new Font("Tahoma", Font.BOLD, GlobalConstants.FONT_SIZE_3));
		panel_south_1.add(btn_hash_text, BorderLayout.EAST);

		tf_output_text = new JTextField();
		tf_output_text.setEditable(false);
		tf_output_text.setFont(new Font("Tahoma", Font.BOLD, GlobalConstants.FONT_SIZE_0));
		panel_south_1.add(tf_output_text, BorderLayout.SOUTH);
		tf_output_text.setColumns(10);
		
		panel_1 = new JPanel();
		panel_1.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel_center.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new GridLayout(2, 1, 0, 0));
		
		tf_output_file1 = new JTextField();
		tf_output_file1.setFont(new Font("Tahoma", Font.BOLD, GlobalConstants.FONT_SIZE_0));
		tf_output_file1.setEditable(false);
		panel_1.add(tf_output_file1);
		tf_output_file1.setColumns(10);
		
		tf_output_file2 = new JTextField();
		tf_output_file2.setFont(new Font("Tahoma", Font.BOLD, GlobalConstants.FONT_SIZE_0));
		tf_output_file2.setEditable(false);
		panel_1.add(tf_output_file2);
		tf_output_file2.setColumns(10);
		
		JPanel panel_north = new JPanel();
		panel_north.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(panel_north, BorderLayout.NORTH);
		panel_north.setLayout(new BorderLayout(0, 0));
		
		cbb_type = new JComboBox();
		cbb_type.setFont(new Font("Tahoma", Font.BOLD, GlobalConstants.FONT_SIZE_3));
		panel_north.add(cbb_type);
	}

	private void addEventHandle() {
		tf_input_file2.addActionListener(actionListener);
		tf_input_text.addActionListener(actionListener);
		tf_output_text.addActionListener(actionListener);
		btn_open_file1.addActionListener(actionListener);
		btn_open_file2.addActionListener(actionListener);
		btn_hash_file1.addActionListener(actionListener);
		btn_hash_file2.addActionListener(actionListener);
		btn_hash_text.addActionListener(actionListener);
		cbb_type.addActionListener(actionListener);
	}
}

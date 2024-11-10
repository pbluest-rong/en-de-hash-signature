package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import controller.Controller;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;
import java.awt.CardLayout;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

public class PanelEnDe extends JPanel {

	public static final long serialVersionUID = 1L;
	public JTextField tf_input;
	public JTextField tf_output;
	public ActionListener actionListener;
	public JRadioButton rdo_text;
	public JRadioButton rdo_file;
	public JButton btn_encrypt;
	public JButton btn_decrypt;
	public JButton btn_open_input_file;
	public JButton btn_open_input_folder;
	public JButton btn_open_output_file;

	/**
	 * Create the panel.
	 */
	public PanelEnDe(Controller controller) {
		this.actionListener = controller;
		initUI();
		addEventHandle();
	}

	public void initUI() {
		setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"Encryption & Decryption", TitledBorder.CENTER, TitledBorder.TOP, null, Color.BLUE));
		this.setSize(460, 313);
		setLayout(new BorderLayout(0, 0));
		JPanel panel_south = new JPanel();

		JPanel panel_center = new JPanel();
		add(panel_center, BorderLayout.NORTH);
		panel_center.setLayout(new BorderLayout(0, 0));

		JPanel panel_in = new JPanel(new BorderLayout(0, 0));
		panel_center.add(panel_in, BorderLayout.SOUTH);

		JPanel panel = new JPanel();
		panel_in.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.SOUTH);

		btn_open_input_file = new JButton("Select File");
		btn_open_input_file.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_2.add(btn_open_input_file);

		btn_open_input_folder = new JButton("Select Folder");
		btn_open_input_folder.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_2.add(btn_open_input_folder);

		JLabel lblNewLabel_1 = new JLabel("Input: ");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel.add(lblNewLabel_1, BorderLayout.WEST);

		tf_input = new JTextField();
		tf_input.setColumns(10);
		panel.add(tf_input, BorderLayout.CENTER);

		JPanel panel_1 = new JPanel();
		panel_in.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));

		JPanel panel_2_1 = new JPanel();
		panel_1.add(panel_2_1, BorderLayout.SOUTH);

		btn_open_output_file = new JButton("Export To");
		btn_open_output_file.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_2_1.add(btn_open_output_file);

		JLabel lblNewLabel_1_1 = new JLabel("Output: ");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_1.add(lblNewLabel_1_1, BorderLayout.WEST);

		tf_output = new JTextField();
		tf_output.setColumns(10);
		panel_1.add(tf_output, BorderLayout.CENTER);

		JPanel panel_3 = new JPanel();
		panel_center.add(panel_3, BorderLayout.NORTH);

		rdo_text = new JRadioButton("Text");
		rdo_text.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_3.add(rdo_text);

		rdo_file = new JRadioButton("File");
		rdo_file.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_3.add(rdo_file);

		ButtonGroup bg = new ButtonGroup();
		bg.add(rdo_text);
		bg.add(rdo_file);

		add(panel_south, BorderLayout.SOUTH);
		panel_south.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		btn_encrypt = new JButton("Encrypt");
		btn_encrypt.setFont(new Font("Tahoma", Font.BOLD, 20));
		panel_south.add(btn_encrypt);

		btn_decrypt = new JButton("Decrypt");
		btn_decrypt.setFont(new Font("Tahoma", Font.BOLD, 20));
		panel_south.add(btn_decrypt);
	}

	private void addEventHandle() {
		tf_input.addActionListener(actionListener);
		tf_output.addActionListener(actionListener);
		rdo_text.addActionListener(actionListener);
		rdo_file.addActionListener(actionListener);
		btn_encrypt.addActionListener(actionListener);
		btn_decrypt.addActionListener(actionListener);
		btn_open_input_file.addActionListener(actionListener);
		btn_open_input_folder.addActionListener(actionListener);
		btn_open_output_file.addActionListener(actionListener);
	}
}

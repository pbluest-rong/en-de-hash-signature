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
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;

public class PanelCheckFile extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JTextField textField_;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;

	/**
	 * Create the panel.
	 */
	public PanelCheckFile() {
		setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Check file", TitledBorder.CENTER, TitledBorder.TOP, null, Color.BLUE));
		this.setSize(460,313);
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel_north = new JPanel();
		panel_north.setLayout(new BorderLayout(0,0));
		
		JPanel panel_north_1 = new JPanel();
		panel_north_1.setBorder(new EmptyBorder(0, 0, 10, 0));
		panel_north_1.setLayout(new BorderLayout(0,0));
		
		JLabel lblNewLabel = new JLabel("File/Folder 1: ");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_north_1.add(lblNewLabel, BorderLayout.WEST);
		
		textField = new JTextField();
		textField.setColumns(10);
		panel_north_1.add(textField, BorderLayout.CENTER);
		
		JButton btn_open = new JButton("Open");
		btn_open.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_north_1.add(btn_open, BorderLayout.EAST);
		
		JPanel panel_north_2 = new JPanel();
		panel_north_2.setLayout(new BorderLayout(0,0));
		
		JLabel lblNewLabel2 = new JLabel("File/Folder 2: ");
		lblNewLabel2.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_north_2.add(lblNewLabel2, BorderLayout.WEST);
		
		textField_ = new JTextField();
		textField_.setColumns(10);
		panel_north_2.add(textField_, BorderLayout.CENTER);
		
		JButton btn_open2 = new JButton("Open");
		btn_open2.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_north_2.add(btn_open2, BorderLayout.EAST);
		
		JButton btn_check2 = new JButton("Check");
		btn_check2.setFont(new Font("Tahoma", Font.BOLD, 20));
		panel_north_2.add(btn_check2, BorderLayout.SOUTH);
		
		
		panel_north.add(panel_north_1, BorderLayout.NORTH);
		panel_north.add(panel_north_2, BorderLayout.SOUTH);
		add(panel_north, BorderLayout.NORTH);
		
		JPanel panel_center = new JPanel();
		panel_center.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblMd = new JLabel("MD5:");
		lblMd.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_center.add(lblMd);
		
		textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setColumns(10);
		panel_center.add(textField_1);
		
		textField_4 = new JTextField();
		textField_4.setEditable(false);
		panel_center.add(textField_4);
		textField_4.setColumns(10);
		
		JLabel lblSha = new JLabel("SHA1:");
		lblSha.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_center.add(lblSha);
		
		textField_2 = new JTextField();
		textField_2.setEditable(false);
		textField_2.setColumns(10);
		panel_center.add(textField_2);
		
		textField_5 = new JTextField();
		textField_5.setEditable(false);
		panel_center.add(textField_5);
		textField_5.setColumns(10);
		
		JLabel lblSha_2 = new JLabel("SHA256:");
		lblSha_2.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_center.add(lblSha_2);
		
		textField_3 = new JTextField();
		textField_3.setEditable(false);
		textField_3.setColumns(10);
		panel_center.add(textField_3);
		
		add(panel_center, BorderLayout.CENTER);
		
		textField_6 = new JTextField();
		textField_6.setEditable(false);
		panel_center.add(textField_6);
		textField_6.setColumns(10);
	}
}

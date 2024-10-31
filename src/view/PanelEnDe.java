package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
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

	private static final long serialVersionUID = 1L;
	private JTextField textField_1;
	private JTextField textField;

	/**
	 * Create the panel.
	 */
	public PanelEnDe() {
		setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Encryption & Decryption", TitledBorder.CENTER, TitledBorder.TOP, null, Color.BLUE));
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

		JButton btnNewButton_2 = new JButton("Select File");
		btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_2.add(btnNewButton_2);

		JButton btnNewButton_1_1 = new JButton("Select Folder");
		btnNewButton_1_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_2.add(btnNewButton_1_1);

		JLabel lblNewLabel_1 = new JLabel("Input: ");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel.add(lblNewLabel_1, BorderLayout.WEST);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		panel.add(textField_1, BorderLayout.CENTER);

		JPanel panel_1 = new JPanel();
		panel_in.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));

		JPanel panel_2_1 = new JPanel();
		panel_1.add(panel_2_1, BorderLayout.SOUTH);

		JButton btnNewButton_2_1 = new JButton("Select File");
		btnNewButton_2_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_2_1.add(btnNewButton_2_1);

		JButton btnNewButton_1_1_1 = new JButton("Select Folder");
		btnNewButton_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_2_1.add(btnNewButton_1_1_1);

		JLabel lblNewLabel_1_1 = new JLabel("Output: ");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_1.add(lblNewLabel_1_1, BorderLayout.WEST);

		textField = new JTextField();
		textField.setColumns(10);
		panel_1.add(textField, BorderLayout.CENTER);
		
		JPanel panel_3 = new JPanel();
		panel_center.add(panel_3, BorderLayout.NORTH);
		
		JRadioButton rdo_text = new JRadioButton("Text"); 
		rdo_text.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_3.add(rdo_text);
		
		JRadioButton rdo_file = new JRadioButton("File");
		rdo_file.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_3.add(rdo_file);

		ButtonGroup bg = new ButtonGroup();
        bg.add(rdo_text);
        bg.add(rdo_file);
        
		add(panel_south, BorderLayout.SOUTH);
		panel_south.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JButton btnNewButton_3 = new JButton("Encrypt");
		btnNewButton_3.setFont(new Font("Tahoma", Font.BOLD, 20));
		panel_south.add(btnNewButton_3);

		JButton btnNewButton_4 = new JButton("Decrypt");
		btnNewButton_4.setFont(new Font("Tahoma", Font.BOLD, 20));
		panel_south.add(btnNewButton_4);
	}

}

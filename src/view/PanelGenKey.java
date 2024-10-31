package view;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.border.EtchedBorder;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.SwingConstants;

public class PanelGenKey extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField textField;

	/**
	 * Create the panel.
	 */
	public PanelGenKey() {
		setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Generate & load key", TitledBorder.CENTER, TitledBorder.TOP, null, Color.BLUE));
		this.setSize(460,313);
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel_north = new JPanel();
		panel_north.setLayout(new BorderLayout(0,0));
		add(panel_north,BorderLayout.NORTH);
		

		JPanel panel_0 = new JPanel(new BorderLayout(0,0));
		JPanel panel_1 = new JPanel(new BorderLayout(0,0));
		JPanel panel_2 = new JPanel(new BorderLayout(0,0));
		panel_2.setBorder(new EmptyBorder(10, 0, 0, 0));
		panel_0.add(panel_1, BorderLayout.NORTH);
		panel_0.add(panel_2, BorderLayout.SOUTH);
		panel_north.add(panel_0, BorderLayout.CENTER);
		
		JLabel lblFileKey = new JLabel("File key: ");
		lblFileKey.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_2.add(lblFileKey, BorderLayout.WEST);
		
		textField = new JTextField();
		panel_2.add(textField, BorderLayout.CENTER);
		textField.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("Open");
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_2.add(btnNewButton_1, BorderLayout.EAST);
		
		JLabel lblNewLabel = new JLabel("Key size: ");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_1.add(lblNewLabel, BorderLayout.WEST);
		
		JComboBox comboBox = new JComboBox();
		panel_1.add(comboBox, BorderLayout.CENTER);
		
		JButton btnNewButton = new JButton("Auto key generation");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_1.add(btnNewButton, BorderLayout.EAST);
	}

}

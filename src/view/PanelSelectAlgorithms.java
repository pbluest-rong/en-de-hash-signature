package view;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import java.awt.CardLayout;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import java.awt.Font;

public class PanelSelectAlgorithms extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public PanelSelectAlgorithms() {
		setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Select Algorithms", TitledBorder.CENTER, TitledBorder.TOP, null, Color.BLUE));
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("Basic Symmetric");
		rdbtnNewRadioButton.setFont(new Font("Tahoma", Font.BOLD, 18));
		panel.add(rdbtnNewRadioButton, BorderLayout.NORTH);
		
		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("Modern Symmetric");
		rdbtnNewRadioButton_1.setFont(new Font("Tahoma", Font.BOLD, 18));
		panel.add(rdbtnNewRadioButton_1, BorderLayout.CENTER);
		
		JRadioButton rdbtnNewRadioButton_2 = new JRadioButton("Asymmetric");
		rdbtnNewRadioButton_2.setFont(new Font("Tahoma", Font.BOLD, 18));
		panel.add(rdbtnNewRadioButton_2, BorderLayout.SOUTH);
		
		ButtonGroup bg = new ButtonGroup();
        bg.add(rdbtnNewRadioButton);
        bg.add(rdbtnNewRadioButton_1);
        bg.add(rdbtnNewRadioButton_2);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JComboBox comboBox = new JComboBox();
		comboBox.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 20));
		panel_1.add(comboBox, BorderLayout.NORTH);

	}
}

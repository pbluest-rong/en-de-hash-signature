package view;

import javax.swing.*;
import model.signature.DS;
import model.signature.KeyPairContainer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class PanelSignature extends JPanel {
	private DS ds;

	private JTextField messageField;
	private JTextField filePathField;
	private JTextArea outputArea;
	private JButton signButton, verifyButton, loadButton, saveButton, selectFileButton;
	private JRadioButton signMessageRadioButton, signFileRadioButton;
	private ButtonGroup radioGroup;

	public PanelSignature() {
		try {
			ds = new DS("DSA", "SHA1PRNG", "SUN");
			ds.genKey();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setLayout(new BorderLayout(10, 10));

		// NORTH Panel: Input fields và Radio Buttons
		JPanel northPanel = new JPanel(new BorderLayout());

		JPanel subNorthPanel = new JPanel(new GridLayout(2, 2, 5, 5));
		northPanel.add(subNorthPanel, BorderLayout.CENTER);
		JLabel label = new JLabel("Message to Sign:");
		label.setFont(new Font("Tahoma", Font.BOLD, 12));
		subNorthPanel.add(label);
		messageField = new JTextField(20);
		messageField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		subNorthPanel.add(messageField);

		selectFileButton = new JButton("Select File");
		selectFileButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		subNorthPanel.add(selectFileButton);
		filePathField = new JTextField(20);
		filePathField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		subNorthPanel.add(filePathField);

		// Radio Buttons cho việc chọn giữa Sign Message và Sign File
		signMessageRadioButton = new JRadioButton("Sign Message", true);
		signMessageRadioButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		signFileRadioButton = new JRadioButton("Sign File");
		signFileRadioButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		radioGroup = new ButtonGroup();
		radioGroup.add(signMessageRadioButton);
		radioGroup.add(signFileRadioButton);

		// Thêm radio buttons vào panel
		JPanel radioPanel = new JPanel();
		radioPanel.add(signMessageRadioButton);
		radioPanel.add(signFileRadioButton);
		northPanel.add(radioPanel, BorderLayout.NORTH);

		add(northPanel, BorderLayout.NORTH);

		// CENTER Panel: Output area
		outputArea = new JTextArea(5, 30);
		outputArea.setEditable(false);
		outputArea.setFont(new Font("Monospaced", Font.BOLD, 20));
		outputArea.setTabSize(1);
		outputArea.setLineWrap(true);
		outputArea.setWrapStyleWord(true);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JScrollPane(outputArea), BorderLayout.CENTER);
		add(panel, BorderLayout.CENTER);

		// SOUTH Panel: Action buttons
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		signButton = new JButton("Sign");
		signButton.setFont(new Font("Tahoma", Font.BOLD, 20));
		verifyButton = new JButton("Verify");
		verifyButton.setFont(new Font("Tahoma", Font.BOLD, 20));
		loadButton = new JButton("Load");
		loadButton.setFont(new Font("Tahoma", Font.BOLD, 20));
		saveButton = new JButton("Save ");
		saveButton.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 20));

		southPanel.add(signButton);
		southPanel.add(verifyButton);
		southPanel.add(loadButton);
		southPanel.add(saveButton);

		add(southPanel, BorderLayout.SOUTH);
		// Action Listeners
		signButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (signMessageRadioButton.isSelected()) {
						String message = messageField.getText();
						if (!message.isEmpty()) {
							String sign = ds.sign(message);
							outputArea.setText(sign);
						} else {
							JOptionPane.showMessageDialog(PanelSignature.this, "Message is empty!",
									"Information Message", JOptionPane.INFORMATION_MESSAGE);
						}
					} else if (signFileRadioButton.isSelected()) {
						String filePath = filePathField.getText();
						if (!filePath.isEmpty()) {
							String sign = ds.signFile(filePath);
							outputArea.setText(sign);
						} else {
							JOptionPane.showMessageDialog(PanelSignature.this, "File path is empty!!",
									"Information Message", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(PanelSignature.this, "Error signing", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		verifyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (signMessageRadioButton.isSelected()) {
						String sign = outputArea.getText();
						if (sign != null && !sign.isEmpty()) {
							String messageToVerify = JOptionPane.showInputDialog(PanelSignature.this,
									"Enter signature to verify:", "Verify Signature", JOptionPane.PLAIN_MESSAGE);
							if (messageToVerify != null && !sign.isEmpty()) {
								System.out.println(messageToVerify);
								System.out.println(sign);
								boolean isVerified = ds.verify(messageToVerify, sign);
								JOptionPane.showMessageDialog(PanelSignature.this, isVerified ? "Valid" : "Invalid",
										"Verification result", JOptionPane.INFORMATION_MESSAGE);
							} else {
								JOptionPane.showMessageDialog(PanelSignature.this,
										"No signature entered for verification!!", "Information Message",
										JOptionPane.INFORMATION_MESSAGE);
							}
						} else {
							JOptionPane.showMessageDialog(PanelSignature.this, "No message entered for verification.!!",
									"Information Message", JOptionPane.INFORMATION_MESSAGE);
						}
					} else if (signFileRadioButton.isSelected()) {
						// Mở cửa sổ chọn file
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setCurrentDirectory(new File("./resources"));
						int result = fileChooser.showOpenDialog(PanelSignature.this);
						if (result == JFileChooser.APPROVE_OPTION) {
							String filePath = fileChooser.getSelectedFile().getAbsolutePath(); // Lấy đường dẫn tệp

							// Kiểm tra chữ ký
							String sign = outputArea.getText();
							if (sign != null && !sign.isEmpty()) {
								boolean isVerified = ds.verifyFile(filePath, sign);
								JOptionPane.showMessageDialog(PanelSignature.this, isVerified ? "Valid" : "Invalid",
										"File Verification result", JOptionPane.INFORMATION_MESSAGE);
							} else {
								JOptionPane.showMessageDialog(PanelSignature.this,
										"No signature entered for verification.", "Information Message",
										JOptionPane.INFORMATION_MESSAGE);
							}
						}
					}

				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(PanelSignature.this, "Error verifying", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setCurrentDirectory(new File("./resources"));
					int result = fileChooser.showOpenDialog(PanelSignature.this);
					if (result == JFileChooser.APPROVE_OPTION) {
						File selectedFile = fileChooser.getSelectedFile();
						KeyPairContainer keyPair = KeyPairContainer
								.loadKeyPairAndSignature(selectedFile.getAbsolutePath());
						ds = new DS("DSA", "SUN", keyPair.getPublicKey(), keyPair.getPrivateKey());
						outputArea.setText(keyPair.getSignature());
						JOptionPane.showMessageDialog(PanelSignature.this, "Signature result loaded successfully",
								"Information Message", JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(PanelSignature.this, "Error loading signature result", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String signature = outputArea.getText();
				if (signature != null && !signature.isEmpty()) {
					try {
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setCurrentDirectory(new File("./resources"));
						int result = fileChooser.showSaveDialog(PanelSignature.this);
						if (result == JFileChooser.APPROVE_OPTION) {
							File selectedFile = fileChooser.getSelectedFile();
							KeyPairContainer.saveKeyPairAndSignature(selectedFile.getAbsolutePath(), ds.publicKey,
									ds.privateKey, signature);
							JOptionPane.showMessageDialog(PanelSignature.this, "Signature result saved successfully",
									"Information Message", JOptionPane.INFORMATION_MESSAGE);
						}
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(PanelSignature.this, "Error saving signature result", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(PanelSignature.this, "Signature is empty!", "Information Message",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		selectFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File("./resources"));
				int result = fileChooser.showOpenDialog(PanelSignature.this);
				if (result == JFileChooser.APPROVE_OPTION) {
					filePathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});

		signMessageRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				messageField.setEnabled(true);
				filePathField.setEnabled(false);
				selectFileButton.setEnabled(false);
			}
		});

		signFileRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				messageField.setEnabled(false);
				filePathField.setEnabled(true);
				selectFileButton.setEnabled(true);
			}
		});
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Digital Signature Panel");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new PanelSignature());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}

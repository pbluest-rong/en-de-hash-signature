package controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.util.Base64;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import model.EAlgorithmType;
import model.EKeySize;
import model.EModes;
import model.EPadding;
import model.ICryptoAlgorithm;
import model.MainModel;
import model.asymmetric.RSA_AES;
import model.hash.EHashAlgorithm;
import view.PanelCheckFile;
import view.PanelEnDe;
import view.PanelGenLoadKey;
import view.PanelSelectAlgorithms;

public class Controller implements ActionListener {
	public PanelGenLoadKey panelGenLoadKey;
	public PanelCheckFile panelCheckFile;
	public PanelEnDe panelEnDe;
	public PanelSelectAlgorithms panelSelectAlgorithms;
	public MainModel model;
	public JProgressBar progressBar;

	private boolean isSelectBasic = false;
	private boolean isSelectModern = false;
	private boolean isSelectAsymmetric = false;

	private boolean isLoadHash = false;

	public Controller() {

		this.model = new MainModel();
		this.panelGenLoadKey = new PanelGenLoadKey(this);
		this.panelCheckFile = new PanelCheckFile(this);
		this.panelEnDe = new PanelEnDe(this);
		this.panelSelectAlgorithms = new PanelSelectAlgorithms(this);

		this.panelSelectAlgorithms.rdb_basic_symmetric.setSelected(true);
		loadBasicSymmetricAlgorithms();
		loadHashAlgorithms();
	}

	public void setProgressBar(JProgressBar bar) {
		this.progressBar = bar;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("./resources"));
		// select algorithms
		if (e.getSource() == panelSelectAlgorithms.rdb_basic_symmetric
				|| e.getSource() == panelSelectAlgorithms.rdb_modern_symmetric
				|| e.getSource() == panelSelectAlgorithms.rdb_asymmetric) {
			switchLoadUI();
		} else if (e.getSource() == panelSelectAlgorithms.cbb_algorithm) {
			if (this.panelSelectAlgorithms.cbb_algorithm.getSelectedIndex() >= 0) {
				System.out.println("cbb_algorithm :((");
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						SwingUtilities.invokeLater(() -> progressBar.setVisible(true));

						model.algorithmType = EAlgorithmType
								.get(panelSelectAlgorithms.cbb_algorithm.getSelectedItem().toString());

						if (ICryptoAlgorithm.isBasicSymmetric(model.algorithmType)) {
							panelGenLoadKey.lbl_key_size.setVisible(false);
							panelGenLoadKey.lbl_modes.setVisible(false);
							panelGenLoadKey.lbl_padding.setVisible(false);
							panelGenLoadKey.cbb_key_size.setVisible(false);
							panelGenLoadKey.cbb_modes.setVisible(false);
							panelGenLoadKey.cbb_padding.setVisible(false);
						} else {
							panelGenLoadKey.lbl_key_size.setVisible(true);
							panelGenLoadKey.lbl_modes.setVisible(true);
							panelGenLoadKey.lbl_padding.setVisible(true);
							panelGenLoadKey.cbb_key_size.setVisible(true);
							panelGenLoadKey.cbb_modes.setVisible(true);
							panelGenLoadKey.cbb_padding.setVisible(true);

							loadKeySize(model.algorithmType);
							loadModes(model.getModes());
							loadPadding(model.getPaddings());
						}
						if (model.algorithmType != null) {
							if (panelGenLoadKey.cbb_key_size.getItemCount() > 0)
								panelGenLoadKey.cbb_key_size.setSelectedIndex(0);
						}
						resetUIForAlgorithm();
						return null;
					}

					@Override
					protected void done() {
						progressBar.setVisible(false);
					}
				};
				worker.execute();
			}
		}
		// load key
		else if (e.getSource() == panelGenLoadKey.cbb_key_size) {
			if (this.panelGenLoadKey.cbb_key_size.getSelectedIndex() >= 0) {
				int ks = Integer.valueOf(this.panelGenLoadKey.cbb_key_size.getSelectedItem().toString());
				this.model.keySize = getKeySize(this.model.algorithmType, ks);
			}
		} else if (e.getSource() == panelGenLoadKey.btn_auto_key_genereration) {
			if (this.model.algorithmType != null) {
				if (ICryptoAlgorithm.isBasicSymmetric(this.model.algorithmType)) {
					if (this.model.chooseAlgorithm()) {
						System.out.println("chose basic symmetric");
					}
				} else {
					if (this.model.chooseAlgorithm()) {
						System.out.println("chose algorithm with key size");
					}
				}
				if (this.model.algorithm != null && ICryptoAlgorithm.isFileEncryption(this.model.algorithmType)) {
					if (panelGenLoadKey.rdo_save_key.isSelected()) {
						fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						JPanel accessoryPanel = new JPanel();
						accessoryPanel.setLayout(new BorderLayout(10, 10));
						JLabel label = new JLabel("New File Name:");
						label.setFont(new Font("Aria", Font.BOLD, 20));
						JTextField fileNameField = new JTextField(10);
						fileNameField.setFont(new Font("Aria", Font.BOLD, 20));
						accessoryPanel.add(label, BorderLayout.NORTH);
						JPanel panel_in = new JPanel(new BorderLayout());
						panel_in.add(fileNameField, BorderLayout.NORTH);
						accessoryPanel.add(panel_in, BorderLayout.CENTER);

						fileChooser.setAccessory(accessoryPanel);
						int returnVal = fileChooser.showDialog(this.panelGenLoadKey, "Select folder");
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							File selectedFolder = fileChooser.getSelectedFile();
							String folderPath = selectedFolder.getAbsolutePath();
							String fileName = fileNameField.getText().trim();
							if (!fileName.isEmpty()) {
								String fullPath = folderPath + File.separator + fileName + ".data";
								try {
									if (this.model.algorithm != null) {
										this.model.algorithm.genKey();
										this.model.algorithm.saveKeyToFile(fullPath);
										panelGenLoadKey.tf_file_path.setText(fullPath);
										System.out.println("generated key!");
										JOptionPane.showMessageDialog(panelGenLoadKey, "Generate key is successs!",
												"Success", JOptionPane.INFORMATION_MESSAGE);
									}
								} catch (Exception e1) {
									e1.printStackTrace();
								}
							} else {
								JOptionPane.showMessageDialog(panelGenLoadKey, "File name is empty!", "Warning",
										JOptionPane.WARNING_MESSAGE);
							}
						}
					} else {
						if (this.model.algorithm != null) {
							if (this.model.algorithm != null)
								try {
									this.model.algorithm.genKey();
									System.out.println("generated key!");
									JOptionPane.showMessageDialog(panelGenLoadKey, "Generate key is successs!",
											"Success", JOptionPane.INFORMATION_MESSAGE);
								} catch (Exception e1) {
									e1.printStackTrace();
								}
						}
					}
				}
			}
		} else if (e.getSource() == panelGenLoadKey.btn_open_file) {
			if (ICryptoAlgorithm.isBasicSymmetric(this.model.algorithmType)) {
				if (this.model.chooseAlgorithm()) {
					System.out.println("chose basic symmetric");
				}
			} else {
				if (this.model.chooseAlgorithm()) {
					System.out.println("chose algorithm with key size");
				}
			}

			int returnval = fileChooser.showOpenDialog(this.panelGenLoadKey);
			if (returnval == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				try {
					this.model.algorithm.loadKeyFromFile(file.getAbsolutePath());
					panelGenLoadKey.tf_file_path.setText(file.getAbsolutePath());
					int ks = this.model.algorithm.getKeySize();
					if (ks != 0) {
						for (int i = 0; i < this.panelGenLoadKey.cbb_key_size.getItemCount(); i++) {
							if (this.panelGenLoadKey.cbb_key_size.getItemAt(i).equals(ks))
								this.panelGenLoadKey.cbb_key_size.setSelectedIndex(i);
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(panelGenLoadKey, "Error!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		// encrypt and decrypt
		else if (e.getSource() == panelEnDe.rdo_text) {
			this.panelEnDe.btn_open_input_file.setVisible(false);
			this.panelEnDe.btn_open_input_folder.setVisible(false);
			this.panelEnDe.btn_open_output_file.setVisible(false);
			panelEnDe.tf_input.setText("");
			panelEnDe.tf_output.setText("");
		} else if (e.getSource() == panelEnDe.rdo_file) {
			this.panelEnDe.btn_open_input_file.setVisible(true);
			this.panelEnDe.btn_open_input_folder.setVisible(true);
			this.panelEnDe.btn_open_output_file.setVisible(true);
			panelEnDe.tf_input.setText("");
			panelEnDe.tf_output.setText("");
		} else if (e.getSource() == panelEnDe.btn_open_input_file || e.getSource() == panelEnDe.btn_open_input_folder) {
			if (e.getSource() == panelEnDe.btn_open_input_folder)
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			int returnval = fileChooser.showOpenDialog(this.panelEnDe);
			if (returnval == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				panelEnDe.tf_input.setText(file.getAbsolutePath());
				panelEnDe.tf_output.setText("");
			}
		} else if (e.getSource() == panelEnDe.btn_open_output_file) {
			if (panelEnDe.tf_input.getText().isEmpty()) {
				JOptionPane.showMessageDialog(panelEnDe, "Input File or folder is empty!", "Warning",
						JOptionPane.WARNING_MESSAGE);
			} else {
				File file = new File(panelEnDe.tf_input.getText());
				if (!file.exists()) {
					JOptionPane.showMessageDialog(panelEnDe, "Input File or folder is not found!", "Warning",
							JOptionPane.WARNING_MESSAGE);
				} else {
					boolean isDirToExport = false;
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

					if (file.isDirectory())
						isDirToExport = true;
					else
						isDirToExport = false;

					JPanel accessoryPanel = new JPanel();
					accessoryPanel.setLayout(new BorderLayout(10, 10));
					JLabel label = new JLabel(isDirToExport ? "New Folder Name:" : "New File Name:");
					label.setFont(new Font("Aria", Font.BOLD, 20));
					JTextField fileNameField = new JTextField(10);
					fileNameField.setFont(new Font("Aria", Font.BOLD, 20));
					accessoryPanel.add(label, BorderLayout.NORTH);
					JPanel panel_in = new JPanel(new BorderLayout());
					panel_in.add(fileNameField, BorderLayout.NORTH);
					accessoryPanel.add(panel_in, BorderLayout.CENTER);

					fileChooser.setAccessory(accessoryPanel);
					int returnVal = fileChooser.showDialog(this.panelEnDe, "Select folder");
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File selectedFolder = fileChooser.getSelectedFile();
						String folderPath = selectedFolder.getAbsolutePath();
						String fileName = fileNameField.getText().trim();
						if (!fileName.isEmpty()) {
							int dotIndex = panelEnDe.tf_input.getText().lastIndexOf('.');
							if (dotIndex > 0 && dotIndex < panelEnDe.tf_input.getText().length() - 1) {
								String fullPath = folderPath + File.separator + fileName
										+ (isDirToExport ? "" : panelEnDe.tf_input.getText().substring(dotIndex));
								panelEnDe.tf_output.setText(fullPath);
							}
						} else {
							JOptionPane.showMessageDialog(panelEnDe, "File or folder name is empty!", "Warning",
									JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			}
		} else if (e.getSource() == panelEnDe.btn_encrypt) {
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

				@Override
				protected Void doInBackground() throws Exception {
					SwingUtilities.invokeLater(() -> progressBar.setVisible(true));
					encrypt();
					return null;
				}

				@Override
				protected void done() {
					progressBar.setVisible(false);
				}
			};
			worker.execute();
		} else if (e.getSource() == panelEnDe.btn_decrypt) {

			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
				@Override
				protected Void doInBackground() throws Exception {
					SwingUtilities.invokeLater(() -> progressBar.setVisible(true));
					decrypt();
					return null;
				}

				@Override
				protected void done() {
					progressBar.setVisible(false);
				}
			};
			worker.execute();
		}
//		 check file
		else if (e.getSource() == panelCheckFile.btn_hash_text) {
			String text = panelCheckFile.tf_input_text.getText();
			if (text.isEmpty()) {
				JOptionPane.showMessageDialog(panelEnDe, "Text is empty!", "Warning", JOptionPane.WARNING_MESSAGE);
			} else {
				try {
					String ht = this.model.hash.hash(text);
					panelCheckFile.tf_output_text.setText(ht);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		} else if (e.getSource() == panelCheckFile.btn_open_file1) {
			int returnval = fileChooser.showOpenDialog(this.panelCheckFile);
			if (returnval == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				panelCheckFile.tf_input_file1.setText(file.getAbsolutePath());
			}
		} else if (e.getSource() == panelCheckFile.btn_hash_file1) {
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
				@Override
				protected Void doInBackground() throws Exception {
					SwingUtilities.invokeLater(() -> progressBar.setVisible(true));
					String filePath = panelCheckFile.tf_input_file1.getText();
					if (filePath.isEmpty()) {
						JOptionPane.showMessageDialog(panelCheckFile, "File not found!", "Warning",
								JOptionPane.WARNING_MESSAGE);
						panelCheckFile.tf_output_file1.setText("");

					} else {
						File file = new File(filePath);
						if (!file.exists()) {
							JOptionPane.showMessageDialog(panelCheckFile, "File not found!", "Warning",
									JOptionPane.WARNING_MESSAGE);
							panelCheckFile.tf_output_file1.setText("");
						} else {
							try {
								String f1 = model.hash.hashFile(filePath);
								panelCheckFile.tf_output_file1.setText(f1);
							} catch (Exception e1) {
								e1.printStackTrace();
								JOptionPane.showMessageDialog(panelCheckFile, "Error!", "Error",
										JOptionPane.ERROR_MESSAGE);
								panelCheckFile.tf_output_file1.setText("");
							}
						}
					}
					if (!panelCheckFile.tf_output_file1.getText().isEmpty()
							&& !panelCheckFile.tf_output_file2.getText().isEmpty())
						if (panelCheckFile.tf_output_file2.getText().equals(panelCheckFile.tf_output_file1.getText())) {
							panelCheckFile.tf_output_file1.setBackground(Color.GREEN);
							panelCheckFile.tf_output_file2.setBackground(Color.GREEN);
						} else {
							panelCheckFile.tf_output_file1.setBackground(Color.RED);
							panelCheckFile.tf_output_file2.setBackground(Color.RED);
						}
					else {
						panelCheckFile.tf_output_file1.setBackground(Color.WHITE);
						panelCheckFile.tf_output_file2.setBackground(Color.WHITE);
					}
					return null;
				}

				@Override
				protected void done() {
					progressBar.setVisible(false);
				}
			};
			worker.execute();
		} else if (e.getSource() == panelCheckFile.btn_open_file2) {
			int returnval = fileChooser.showOpenDialog(this.panelCheckFile);
			if (returnval == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				panelCheckFile.tf_input_file2.setText(file.getAbsolutePath());
			}
		} else if (e.getSource() == panelCheckFile.btn_hash_file2) {
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
				@Override
				protected Void doInBackground() throws Exception {
					SwingUtilities.invokeLater(() -> progressBar.setVisible(true));
					String filePath = panelCheckFile.tf_input_file2.getText();
					if (filePath.isEmpty()) {
						JOptionPane.showMessageDialog(panelCheckFile, "File not found!", "Warning",
								JOptionPane.WARNING_MESSAGE);
						panelCheckFile.tf_output_file2.setText("");
					} else {
						File file = new File(filePath);
						if (!file.exists()) {
							JOptionPane.showMessageDialog(panelCheckFile, "File not found!", "Warning",
									JOptionPane.WARNING_MESSAGE);
							panelCheckFile.tf_output_file2.setText("");
						} else {
							try {
								String f2 = model.hash.hashFile(filePath);
								panelCheckFile.tf_output_file2.setText(f2);
							} catch (Exception e1) {
								e1.printStackTrace();
								JOptionPane.showMessageDialog(panelCheckFile, "Error!", "Error",
										JOptionPane.ERROR_MESSAGE);
								panelCheckFile.tf_output_file2.setText("");
							}
						}
					}
					if (!panelCheckFile.tf_output_file1.getText().isEmpty()
							&& !panelCheckFile.tf_output_file2.getText().isEmpty())
						if (panelCheckFile.tf_output_file2.getText().equals(panelCheckFile.tf_output_file1.getText())) {
							panelCheckFile.tf_output_file1.setBackground(Color.GREEN);
							panelCheckFile.tf_output_file2.setBackground(Color.GREEN);
						} else {
							panelCheckFile.tf_output_file1.setBackground(Color.RED);
							panelCheckFile.tf_output_file2.setBackground(Color.RED);
						}
					else {
						panelCheckFile.tf_output_file1.setBackground(Color.WHITE);
						panelCheckFile.tf_output_file2.setBackground(Color.WHITE);
					}
					return null;
				}

				@Override
				protected void done() {
					progressBar.setVisible(false);
				}
			};
			worker.execute();
		} else if (e.getSource() == panelCheckFile.cbb_type) {
			if (this.panelCheckFile.cbb_type.getSelectedIndex() >= 0) {
				this.model
						.choooseHashAlgorithm(EHashAlgorithm.get(panelCheckFile.cbb_type.getSelectedItem().toString()));
				if (isLoadHash) {
					SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
						@Override
						protected Void doInBackground() throws Exception {
							SwingUtilities.invokeLater(() -> progressBar.setVisible(true));
							String filePath1 = panelCheckFile.tf_input_file1.getText();
							String filePath2 = panelCheckFile.tf_input_file2.getText();

							if (filePath1.isEmpty())
								panelCheckFile.tf_input_file1.setText("");

							if (filePath2.isEmpty())
								panelCheckFile.tf_input_file2.setText("");

							if (!filePath1.isEmpty() || !filePath2.isEmpty()) {
								File file1 = new File(filePath1);
								File file2 = new File(filePath2);
								try {
									String f1 = "";
									String f2 = "";
									if (file1.exists())
										f1 = model.hash.hashFile(filePath1);
									if (file2.exists())
										f2 = model.hash.hashFile(filePath2);

									panelCheckFile.tf_output_file1.setText(f1);
									panelCheckFile.tf_output_file2.setText(f2);
								} catch (Exception e1) {
									panelCheckFile.tf_input_file1.setText("");
									panelCheckFile.tf_input_file2.setText("");
									panelCheckFile.tf_output_file1.setText("");
									panelCheckFile.tf_output_file2.setText("");
								}
							} else {
								panelCheckFile.tf_output_file1.setText("");
								panelCheckFile.tf_output_file2.setText("");
							}
							if (!panelCheckFile.tf_output_file1.getText().isEmpty()
									&& !panelCheckFile.tf_output_file2.getText().isEmpty())
								if (panelCheckFile.tf_output_file2.getText()
										.equals(panelCheckFile.tf_output_file1.getText())) {
									panelCheckFile.tf_output_file1.setBackground(Color.GREEN);
									panelCheckFile.tf_output_file2.setBackground(Color.GREEN);
								} else {
									panelCheckFile.tf_output_file1.setBackground(Color.RED);
									panelCheckFile.tf_output_file2.setBackground(Color.RED);
								}
							else {
								panelCheckFile.tf_output_file1.setBackground(Color.WHITE);
								panelCheckFile.tf_output_file2.setBackground(Color.WHITE);
							}
							return null;
						}

						@Override
						protected void done() {
							progressBar.setVisible(false);
						}
					};
					worker.execute();
				}
			}
		}
	}

	private void loadBasicSymmetricAlgorithms() {
		this.panelSelectAlgorithms.cbb_algorithm.removeAllItems();
		for (EAlgorithmType type : this.model.basicSymmetricAlgorithmTypes) {
			this.panelSelectAlgorithms.cbb_algorithm.addItem(type.getAlgorithm());
		}
	}

	private void loadMordernSymmetricAlgorithms() {
		this.panelSelectAlgorithms.cbb_algorithm.removeAllItems();
		for (EAlgorithmType type : this.model.modernSymmetricAlgorithmTypes.keySet()) {
			this.panelSelectAlgorithms.cbb_algorithm.addItem(type.getAlgorithm());
		}
	}

	private void loadAsymmetricAlgorithms() {
		this.panelSelectAlgorithms.cbb_algorithm.removeAllItems();
		for (EAlgorithmType type : this.model.asymmetricAlgorithmTypes.keySet()) {
			this.panelSelectAlgorithms.cbb_algorithm.addItem(type.getAlgorithm());
		}
	}

	private void loadKeySize(EAlgorithmType type) {
		this.panelGenLoadKey.cbb_key_size.removeAllItems();
		if (ICryptoAlgorithm.isAsymmetricRSA(type) || ICryptoAlgorithm.isAsymmetricRSA_AES(type)) {
			for (EKeySize ks : this.model.asymmetricAlgorithmTypes.get(type)) {
				this.panelGenLoadKey.cbb_key_size.addItem(ks.getBits());
			}
		} else if (ICryptoAlgorithm.isModernSymmetric(type)) {
			for (EKeySize ks : this.model.modernSymmetricAlgorithmTypes.get(type)) {
				this.panelGenLoadKey.cbb_key_size.addItem(ks.getBits());
			}
		}
	}

	private void loadModes(List<EModes> modes) {
		this.panelGenLoadKey.cbb_modes.removeAllItems();
		for (EModes mode : modes) {
			this.panelGenLoadKey.cbb_modes.addItem(mode.getModeName());
		}
	}

	private void loadPadding(List<EPadding> paddings) {
		this.panelGenLoadKey.cbb_padding.removeAllItems();
		for (EPadding p : paddings) {
			this.panelGenLoadKey.cbb_padding.addItem(p.getPaddingName());
		}
	}

	public EKeySize getKeySize(EAlgorithmType type, int keySize) {
		if (ICryptoAlgorithm.isAsymmetricRSA(type) || ICryptoAlgorithm.isAsymmetricRSA_AES(type)) {
			for (EKeySize ks : this.model.asymmetricAlgorithmTypes.get(type)) {
				if (keySize == ks.getBits()) {
					return ks;
				}
			}
		} else if (ICryptoAlgorithm.isModernSymmetric(type)) {
			for (EKeySize ks : this.model.modernSymmetricAlgorithmTypes.get(type)) {
				if (keySize == ks.getBits()) {
					return ks;
				}
			}
		}
		return null;
	}

	private void switchLoadUI() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {

				System.out.println("switchLoadUI :((");
				// Hiển thị progress bar trước khi bắt đầu công việc nặng
				SwingUtilities.invokeLater(() -> progressBar.setVisible(true));

				panelGenLoadKey.tf_file_path.setText("");
				if (!isSelectBasic && panelSelectAlgorithms.rdb_basic_symmetric.isSelected()) {
					isSelectBasic = true;
					isSelectModern = false;
					isSelectAsymmetric = false;
					loadBasicSymmetricAlgorithms();
				} else if (!isSelectModern && panelSelectAlgorithms.rdb_modern_symmetric.isSelected()) {
					loadMordernSymmetricAlgorithms();
					isSelectBasic = false;
					isSelectModern = true;
					isSelectAsymmetric = false;
				} else if (!isSelectAsymmetric && panelSelectAlgorithms.rdb_asymmetric.isSelected()) {
					loadAsymmetricAlgorithms();
					isSelectBasic = false;
					isSelectModern = false;
					isSelectAsymmetric = true;
				}
				return null;
			}

			@Override
			protected void done() {
				// Ẩn progress bar sau khi công việc kết thúc
				progressBar.setVisible(false);
			}
		};
		worker.execute();
	}

	private void resetUIForAlgorithm() {
		if (ICryptoAlgorithm.isFileEncryption(this.model.algorithmType)) {
			this.panelGenLoadKey.lbl_fileKey.setVisible(true);
			this.panelGenLoadKey.btn_open_file.setVisible(true);
			this.panelGenLoadKey.tf_file_path.setVisible(true);

			this.panelEnDe.rdo_file.setVisible(true);
			this.panelEnDe.rdo_file.setSelected(true);
			this.panelEnDe.btn_open_input_file.setVisible(true);
			this.panelEnDe.btn_open_input_folder.setVisible(true);
			this.panelEnDe.btn_open_output_file.setVisible(true);
		} else {
			this.panelGenLoadKey.lbl_fileKey.setVisible(false);
			this.panelGenLoadKey.btn_open_file.setVisible(false);
			this.panelGenLoadKey.tf_file_path.setVisible(false);

			this.panelEnDe.rdo_file.setVisible(false);
			this.panelEnDe.rdo_text.setSelected(true);
			this.panelEnDe.btn_open_input_file.setVisible(false);
			this.panelEnDe.btn_open_input_folder.setVisible(false);
			this.panelEnDe.btn_open_output_file.setVisible(false);
		}
	}

	private void encrypt() {
		if (this.model.algorithm == null) {
			JOptionPane.showMessageDialog(panelEnDe, "Algorithm not initialized!", "Warning",
					JOptionPane.WARNING_MESSAGE);
		} else {
			boolean isEnFile = panelEnDe.rdo_file.isSelected();
			String input = panelEnDe.tf_input.getText();
			String output = panelEnDe.tf_output.getText();
			System.out.println("1");
			if (isEnFile) {
				System.out.println("2");
				if (input.isEmpty() || output.isEmpty())
					JOptionPane.showMessageDialog(panelEnDe, "File or folder is empty!", "Warning",
							JOptionPane.WARNING_MESSAGE);

				File inputFile = new File(input);
				File outputFile = new File(output);
				if (!inputFile.exists()) {
					JOptionPane.showMessageDialog(panelEnDe, "Input file or folder is not exist!", "Warning",
							JOptionPane.WARNING_MESSAGE);
				} else {
					if (inputFile.isDirectory()) {
						outputFile.mkdir();
						if (outputFile.isDirectory()) {
							for (File f : inputFile.listFiles()) {
								try {
									if (f.isFile())
										this.model.algorithm.encryptFile(f.getAbsolutePath(),
												output + "/" + f.getName());
									else if (f.isDirectory())
										this.model.algorithm.encryptFolder(f.getAbsolutePath(),
												output + "/" + f.getName());
								} catch (Exception e1) {
									e1.printStackTrace();
									JOptionPane.showMessageDialog(panelEnDe, "An error occurred! Please try again.",
											"Failure Notification", JOptionPane.ERROR_MESSAGE);
								}
							}
							JOptionPane.showMessageDialog(panelEnDe, "Encryption key is successs!", "Success",
									JOptionPane.INFORMATION_MESSAGE);
						}
					} else if (inputFile.isFile()) {
						System.out.println("3");
						try {
							this.model.algorithm.encryptFile(input, output);
							JOptionPane.showMessageDialog(panelEnDe, "Encryption key is successs!", "Success",
									JOptionPane.INFORMATION_MESSAGE);
						} catch (Exception e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(panelEnDe, "An error occurred! Please try again.",
									"Failure Notification", JOptionPane.ERROR_MESSAGE);
						}
					}
				}

			} else {
				if (input.isEmpty())
					JOptionPane.showMessageDialog(panelEnDe, "Text is empty!", "Warning", JOptionPane.WARNING_MESSAGE);
				else
					try {
						byte[] out = this.model.algorithm.encrypt(input);
						this.panelEnDe.tf_output.setText(Base64.getEncoder().encodeToString(out));
					} catch (Exception e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(panelEnDe, "An error occurred! Please try again.",
								"Failure Notification", JOptionPane.ERROR_MESSAGE);
					}
			}
		}
	}

	private void decrypt() {

		if (this.model.algorithm == null) {
			JOptionPane.showMessageDialog(panelEnDe, "Algorithm not initialized!", "Warning",
					JOptionPane.WARNING_MESSAGE);
		} else {
			boolean isDeFile = panelEnDe.rdo_file.isSelected();
			String input = panelEnDe.tf_input.getText();
			String output = panelEnDe.tf_output.getText();

			if (isDeFile) {
				if (input.isEmpty() || output.isEmpty())
					JOptionPane.showMessageDialog(panelEnDe, "File or folder is empty!", "Warning",
							JOptionPane.WARNING_MESSAGE);
				File inputFile = new File(input);
				File outputFile = new File(output);
				if (!inputFile.exists()) {
					JOptionPane.showMessageDialog(panelEnDe, "Input file or folder is not exist!", "Warning",
							JOptionPane.WARNING_MESSAGE);
				} else {
					if (inputFile.isDirectory()) {
						outputFile.mkdir();
						if (outputFile.isDirectory()) {
							for (File f : inputFile.listFiles()) {
								try {
									if (f.isFile())
										this.model.algorithm.decryptFile(f.getAbsolutePath(),
												output + "/" + f.getName());
									else if (f.isDirectory())
										this.model.algorithm.decryptFolder(f.getAbsolutePath(),
												output + "/" + f.getName());
								} catch (Exception e1) {
									e1.printStackTrace();
									JOptionPane.showMessageDialog(panelEnDe, "An error occurred! Please try again.",
											"Failure Notification", JOptionPane.ERROR_MESSAGE);
								}
							}
							JOptionPane.showMessageDialog(panelEnDe, "Encryption key is successs!", "Success",
									JOptionPane.INFORMATION_MESSAGE);
						}
					} else if (inputFile.isFile()) {
						try {
							this.model.algorithm.decryptFile(input, output);
							JOptionPane.showMessageDialog(panelEnDe, "Encryption key is successs!", "Success",
									JOptionPane.INFORMATION_MESSAGE);
						} catch (Exception e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(panelEnDe, "An error occurred! Please try again.",
									"Failure Notification", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			} else {
				if (input.isEmpty())
					JOptionPane.showMessageDialog(panelEnDe, "Text is empty!", "Warning", JOptionPane.WARNING_MESSAGE);
				else
					try {
						String out = this.model.algorithm.decrypt(Base64.getDecoder().decode(input));
						this.panelEnDe.tf_output.setText(out);
					} catch (Exception e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(panelEnDe, "An error occurred! Please try again.",
								"Failure Notification", JOptionPane.ERROR_MESSAGE);
					}
			}
		}
	}

	private void loadHashAlgorithms() {
		for (EHashAlgorithm type : this.model.hashAlgorithms) {
			panelCheckFile.cbb_type.addItem(type.getHashAlgorithm());
		}
		if (panelCheckFile.cbb_type.getItemCount() > 0) {
			panelCheckFile.cbb_type.setSelectedIndex(0);
			isLoadHash = true;
		}
	}
}

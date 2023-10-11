package pro;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.AbstractListModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class menu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel conPMenu;
	private JTextField txtFRuta;
	private static JTextField txtCoincidencies;
	private static String path = "";
	private static JList listFicheros = new JList();
	private static JComboBox cmbFiltro = new JComboBox();
	private static JComboBox cmbAD;
	private static JTextField txtFusionar;
	private JButton btnFusionar;
	private JScrollPane scrollPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					menu frame = new menu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public menu() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 560, 472);
		conPMenu = new JPanel();
		conPMenu.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(conPMenu);
		conPMenu.setLayout(null);

		JButton btnOrdenar = new JButton("Ordenar");
		btnOrdenar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getOrderedArchives();
			}
		});
		btnOrdenar.setBounds(359, 316, 125, 21);
		conPMenu.add(btnOrdenar);

		JButton btnCoincidencies = new JButton("Coincidencies");
		btnCoincidencies.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtCoincidencies.getText().length() >= 1) {
					getCoincidencies();
				}

			}
		});
		btnCoincidencies.setBounds(359, 346, 125, 21);
		conPMenu.add(btnCoincidencies);

		txtFRuta = new JTextField();
		txtFRuta.setBounds(55, 46, 285, 19);
		conPMenu.add(txtFRuta);
		txtFRuta.setColumns(10);
		// txtFRuta.setText("C:\\Dam2\\acces_Dades");
		JButton btnRuta = new JButton("Ruta");
		btnRuta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				seleccionarDirectorio();
				getArchives();
			}
		});
		btnRuta.setBounds(359, 45, 125, 21);
		conPMenu.add(btnRuta);

		cmbAD = new JComboBox();
		cmbAD.setModel(new DefaultComboBoxModel(new String[] { "Ascendente", "descendente" }));
		cmbAD.setBounds(205, 316, 135, 21);
		conPMenu.add(cmbAD);

		cmbFiltro.setModel(new DefaultComboBoxModel(new String[] { "Grandaria", "Fecha modificacio", "Nom" }));
		cmbFiltro.setBounds(55, 316, 135, 21);
		conPMenu.add(cmbFiltro);

		txtCoincidencies = new JTextField();
		txtCoincidencies.setColumns(10);
		txtCoincidencies.setBounds(55, 347, 285, 19);
		conPMenu.add(txtCoincidencies);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(55, 79, 427, 227);
		conPMenu.add(scrollPane);
		scrollPane.setViewportView(listFicheros);

		txtFusionar = new JTextField();
		txtFusionar.setColumns(10);
		txtFusionar.setBounds(55, 376, 285, 19);
		conPMenu.add(txtFusionar);

		btnFusionar = new JButton("Fusionar");
		btnFusionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fusio();
			}
		});
		btnFusionar.setBounds(359, 375, 125, 21);
		conPMenu.add(btnFusionar);
	}

	private void seleccionarDirectorio() {

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnValue = fileChooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedDirectory = fileChooser.getSelectedFile();
			path = selectedDirectory.getAbsolutePath();

		}
	}

	private static void getArchives() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss");
		DefaultListModel<String> listModel = new DefaultListModel<>();
		File element = new File(path);
		FileFilter txtFileFilter = new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isFile() && file.getName().toLowerCase().endsWith(".txt");
			}
		};

		File[] txtFiles = element.listFiles(txtFileFilter);

		for (File file : txtFiles) {
			listModel.addElement(file.getName().substring(0, file.getName().lastIndexOf('.')) + "   -   "
					+ file.getName().substring(file.getName().lastIndexOf('.') + 1) + "   -   " + file.length()
					+ " Bytes   -   " + dateFormat.format(element.lastModified()));
		}

		listFicheros.setModel(listModel);

	}

	private static void getOrderedArchives() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss");
		DefaultListModel<String> listModel = new DefaultListModel<>();
		File element = new File(path);
		FileFilter txtFileFilter = new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isFile() && file.getName().toLowerCase().endsWith(".txt");
			}
		};

		File[] txtFiles = element.listFiles(txtFileFilter);

		if (cmbFiltro.getSelectedIndex() == 0) {
			Arrays.sort(txtFiles, (f1, f2) -> {
				return Long.compare(f1.length(), f2.length());
			});
		} else if (cmbFiltro.getSelectedIndex() == 1) {
			Arrays.sort(txtFiles, (f1, f2) -> {
				return Long.compare(f1.lastModified(), f2.lastModified());
			});
		}
		if (cmbAD.getSelectedIndex() == 1) {
			File[] aux = new File[txtFiles.length];
			for (int i = 0; i < aux.length; i++) {
				aux[i] = txtFiles[txtFiles.length - (i + 1)];
			}
			txtFiles = Arrays.copyOf(aux, aux.length);
		}

		for (File file : txtFiles) {
			listModel.addElement(file.getName().substring(0, file.getName().lastIndexOf('.')) + "   -   "
					+ file.getName().substring(file.getName().lastIndexOf('.') + 1) + "   -   " + file.length()
					+ " Bytes   -   " + dateFormat.format(file.lastModified()));
		}

		listFicheros.setModel(listModel);

	}

	private static void getCoincidencies() {

		DefaultListModel<String> listModel = new DefaultListModel<>();
		File element = new File(path);
		FileFilter txtFileFilter = new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isFile() && file.getName().toLowerCase().endsWith(".txt");
			}
		};

		File[] txtFiles = element.listFiles(txtFileFilter);
		String[] result = new String[txtFiles.length];

		FileReader fr;
		BufferedReader br;

		for (File file : txtFiles) {

			try {
				fr = new FileReader(file);
				br = new BufferedReader(fr);
				String text = "";
				String line = br.readLine();
				while (line != null) {
					line = br.readLine();
					text += line;
				}

				int contador = 0;
				int indice = text.indexOf(txtCoincidencies.getText());

				while (indice != -1) {
					contador++;
					indice = text.indexOf(txtCoincidencies.getText(), indice + 1);
				}
				listModel.addElement(file.getName() + " -> " + contador + " coincidencies");
				br.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		listFicheros.setModel(listModel);

	}

	private static void fusio() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		if (listFicheros.getSelectedIndices().length >= 2) {
			File element = new File(path);
			FileFilter txtFileFilter = new FileFilter() {
				@Override
				public boolean accept(File file) {
					return file.isFile() && file.getName().toLowerCase().endsWith(".txt");
				}
			};

			File[] txtFiles = element.listFiles(txtFileFilter);
			if (cmbFiltro.getSelectedIndex() == 0) {
				Arrays.sort(txtFiles, (f1, f2) -> {
					return Long.compare(f1.length(), f2.length());
				});
			} else if (cmbFiltro.getSelectedIndex() == 1) {
				Arrays.sort(txtFiles, (f1, f2) -> {
					return Long.compare(f1.lastModified(), f2.lastModified());
				});
			}
			if (cmbAD.getSelectedIndex() == 1) {
				File[] aux = new File[txtFiles.length];
				for (int i = 0; i < aux.length; i++) {
					aux[i] = txtFiles[txtFiles.length - (i + 1)];
				}
				txtFiles = Arrays.copyOf(aux, aux.length);
			}
			File fusioFile = new File(path, txtFusionar.getText() + ".txt");//
			FileReader fr;
			FileWriter fw;
			BufferedReader br;
			BufferedWriter bw;
			try {
				fw = new FileWriter(fusioFile);
				bw = new BufferedWriter(fw);
				for (int index : listFicheros.getSelectedIndices()) {
					fr = new FileReader(txtFiles[index]);
					br = new BufferedReader(fr);

					String line = br.readLine();
					while (line != null) {
						bw.write(line + "\n");
						line = br.readLine();
					}
					fr.close();

					br.close();
				}
				bw.close();
				fw.close();
				JOptionPane.showMessageDialog(null, "Has creat un fitxer fusionant.",
						"Succes", JOptionPane.INFORMATION_MESSAGE);
				getOrderedArchives();
				} catch (IOException e) {

				e.printStackTrace();
			}
		}else {
			JOptionPane.showMessageDialog(null, "Has de seleccionar dos o mes arxius per a poder fusionar.",
					"Error", JOptionPane.INFORMATION_MESSAGE);
		}

	}
}

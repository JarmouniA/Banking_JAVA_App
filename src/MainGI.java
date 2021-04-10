import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class MainGI extends JFrame {
	private static final long serialVersionUID = 998078086148575949L;
	
	private static Agency agency;
	
	private static ImageIcon Client_photo;
	
	private static JMenuBar menuBar;
	private static JMenu menu;
	private static JMenuItem open, exit, search, add;
	
	private static JPanel p1, p2, p3, plist;
	private static JLabel name, CIN;
	private static JTextField nameTF, cinTF;
	private static JButton confirmB, cancelB;
	
	private static JLabel jLabelImage;
	private static JButton BrowseImagesB;

	private static JList<String> list_clients;
	private static DefaultListModel<String> listmodel;
	
	public MainGI() {
		menuBar = new JMenuBar();
		menu = new JMenu("Menu");
		open = new JMenuItem("Open");
		exit = new JMenuItem("Exit");
		search = new JMenuItem("Search");
		add = new JMenuItem("Add");
		
		agency = new Agency();
		
		TypeInterface();
		List_Clients();
		initComponents();
		this.setVisible(true);
	}
	
	private void initComponents() {
		this.setTitle("Banking Activity Management");
		this.setSize(350, 450);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		menu.add(open);
		menu.addSeparator();
		menu.add(add);
		menu.addSeparator();
		menu.add(search);
		menu.addSeparator();
		menu.add(exit);
		
		menuBar.add(menu);
		
		this.setJMenuBar(menuBar);
		
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
				
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				plist.setVisible(false);
				p1.setVisible(true);
				p2.setVisible(true);
				p3.setVisible(true);
				Clear_fields();
				AddClient();
			}
		});
		
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Clear_fields();
				p1.setVisible(false);
				p2.setVisible(false);
				p3.setVisible(false);
				RefreshList();
				plist.setVisible(true);
			}
		});
		BrowseImagesB.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
			BrowseImages(evt);
		    }
        	});
	}
	
	public static Agency getAgency() {
		return MainGI.agency;
	}
	
	private void TypeInterface() {
		p1 = new JPanel(new GridLayout(2, 2));
		p2 = new JPanel(new FlowLayout());
		p3 = new JPanel(new GridBagLayout());
		
		name = new JLabel("Name");
		CIN = new JLabel("CIN");
		nameTF = new JTextField("");
		cinTF = new JTextField("");
		confirmB = new JButton("Confirm");
		cancelB = new JButton("Cancel");
		
		BrowseImagesB = new JButton("Browse");
		BrowseImagesB.setBounds(100,100,95,30);
		jLabelImage = new JLabel();
		
		p1.add(name);
		p1.add(nameTF);
		p1.add(CIN);
		p1.add(cinTF);
		
		p3.add(BrowseImagesB);
		p3.add(jLabelImage);
		
		p2.add(confirmB);
		p2.add(cancelB);
		
		this.add(p1, BorderLayout.NORTH);
		this.add(p3, BorderLayout.CENTER);
		this.add(p2, BorderLayout.SOUTH);
		
		p1.setVisible(false);
		p2.setVisible(false);
		p3.setVisible(false);
	}
	
	private void AddClient() {
		confirmB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String clientName = nameTF.getText();
				int clientCIN = 0;
				try {
					clientCIN= Integer.parseInt(cinTF.getText());
				}
				catch(NumberFormatException e1){
					System.out.println("Invalid String!");
				}
				agency.Upload_Image(Client_photo, clientCIN);
				agency.addClient(clientCIN, clientName);
				JOptionPane.showMessageDialog(null, "Client added!", "Message", JOptionPane.INFORMATION_MESSAGE);
				Clear_fields();
				p1.setVisible(false);
				p2.setVisible(false);
				p3.setVisible(false);
				RefreshList();
				plist.setVisible(true);
			}
		});
		
		cancelB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Clear_fields();
				p1.setVisible(false);
				p2.setVisible(false);
				p3.setVisible(false);
				plist.setVisible(true);
			}
		});
	}
	
	private void BrowseImages(ActionEvent evt) { 
		JFileChooser browseImageFile = new JFileChooser();
		//Filter image extensions
		FileNameExtensionFilter fnef = new FileNameExtensionFilter("IMAGES", "png", "jpg", "jpeg");
		browseImageFile.addChoosableFileFilter(fnef);
		int showOpenDialogue = browseImageFile.showOpenDialog(null);
		if (showOpenDialogue == JFileChooser.APPROVE_OPTION) {
		    String selectedImagePath = browseImageFile.getSelectedFile().getAbsolutePath();
		    JOptionPane.showMessageDialog(null, selectedImagePath);
		    //Display image on JLabel
		    Client_photo = new ImageIcon(selectedImagePath);
		    jLabelImage.setIcon(ClientInfoSheet.scaleImage(Client_photo, 120, 140));
		}
    	}
	
	private void Clear_fields() {
		nameTF.setText("");
		cinTF.setText("");
		jLabelImage.setIcon(ClientInfoSheet.scaleImage(new ImageIcon("./noPhoto.jpg"), 120, 140));
		Client_photo = new ImageIcon("./noPhotoAvailable.jpg");
	}
	
	public static void RefreshList() {
		listmodel.clear();
		List<Client> list = agency.ClientsList();
		if ((list != null) &&  (list.size() > 0)) {
			for (int i = 0; i < list.size(); i++) {
				listmodel.addElement( Integer.toString( list.get(i).getCIN() ) );
			}
		}
		else if (list.size() == 0) {
			listmodel.addElement("0");
		}
	}
	private void List_Clients() {
		listmodel = new DefaultListModel<>();
		RefreshList();
		list_clients = new JList<>(listmodel);
		list_clients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		list_clients.addListSelectionListener(new ListSelectionListener() {
		    public void valueChanged(ListSelectionEvent e){
		        if(!e.getValueIsAdjusting()) {
		            final List<String> selectedValuesList = list_clients.getSelectedValuesList();
		            if (selectedValuesList.size() != 0) {
		            	Client Out;
		            	if ( (Out = agency.SearchClient( Integer.parseInt( selectedValuesList.get(0) ) ) ) != null) {
		            		@SuppressWarnings("unused")
					ClientInfoSheet fiche = new ClientInfoSheet(Out);
		            	}
		            }
		        }
		    }
		});
		
		JScrollPane listScroller = new JScrollPane(list_clients);
		listScroller.setViewportView(list_clients);
		list_clients.setLayoutOrientation(JList.VERTICAL);
		listScroller.setPreferredSize(new Dimension(200, 500));
		plist = new JPanel();
		plist.add(listScroller);
		this.add(plist, BorderLayout.WEST);
		plist.setVisible(true);
	}
}

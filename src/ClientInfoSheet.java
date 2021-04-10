import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

public class ClientInfoSheet extends JFrame{
	private static final long serialVersionUID = 5303938596514920064L;
	
	private Client client;
	
	private JPanel pfiche1, pfiche2, plist;
	private JLabel nameL, cinL, nameT, cinT;
	private JButton modifyB, cancelB, confirmB, deleteB;
	
	private JPanel IconP;
	private JLabel IconL;
	private ImageIcon Icon, Client_photo;
	
	private JPanel pfiche3;
	private JLabel jLabelImage;
	private JButton BrowseImagesB, addAccountB;
	private GridBagConstraints c;
	
	private JList<String> accountsList;
	private DefaultListModel<String> listmodel;
	
	public ClientInfoSheet(Client cl) {
		client = cl;
		
		pfiche1 = new JPanel(new GridLayout(2, 2));
		pfiche2 = new JPanel(new FlowLayout());
		pfiche3 = new JPanel(new GridBagLayout());
		
		nameL = new JLabel("Name");
		cinL = new JLabel("CIN");
		nameT = new JLabel(client.getName());
		cinT = new JLabel( Integer.toString(client.getCIN()) );
		
		Icon = scaleImage(new ImageIcon("./noPhotoAvailable.jpg"), 120, 140);
		IconL = new JLabel(Icon);
		IconP = new JPanel();
		
		addAccountB = new JButton("New Account");
		BrowseImagesB = new JButton("Browse");
		jLabelImage = new JLabel();
		c = new GridBagConstraints();
		
		modifyB = new JButton("Modify");
		cancelB = new JButton("Cancel");
		confirmB = new JButton("Confirm");
		deleteB = new JButton("Delete");
		
		Icon = scaleImage(new ImageIcon("./Agency/Images/" + client.getCIN() + ".jpg"), 120, 140);
		IconL.setIcon(Icon);
        if (Icon.getImageLoadStatus() == MediaTracker.ERRORED)
        	Icon = scaleImage(new ImageIcon("./noPhotoAvailable.jpg"), 120, 140);
        IconP.setVisible(true);
        
		ListAccounts();
		initComponents();
		this.setVisible(true);
	}
	  
	private void initComponents() {
		this.setSize(460, 400);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setTitle(client.getName());
		
		IconP.add(IconL);
		
		pfiche1.add(nameL);
		pfiche1.add(nameT);
		pfiche1.add(cinL);
		pfiche1.add(cinT);
		
		pfiche2.add(modifyB);
		pfiche2.add(cancelB);
		pfiche2.add(confirmB);
		pfiche2.add(deleteB);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 0;
		pfiche3.add(jLabelImage, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		pfiche3.add(BrowseImagesB, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		pfiche3.add(addAccountB, c);
		
		this.add(IconP, BorderLayout.EAST);
		this.add(pfiche1, BorderLayout.NORTH);
		this.add(pfiche3, BorderLayout.CENTER);
		this.add(pfiche2, BorderLayout.SOUTH);
		
		confirmB.setVisible(false);
		
		pfiche3.setVisible(true);
		BrowseImagesB.setVisible(false);
		jLabelImage.setVisible(false);
		
		cancelB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainGI.RefreshList();
				dispose();
			}
		});
		
		modifyB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IconP.setVisible(false);
				
				modifyB.setVisible(false);
				confirmB.setVisible(true);
				
				BrowseImagesB.setVisible(true);
				jLabelImage.setVisible(true);
				addAccountB.setVisible(false);
			}
		});
		
		confirmB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changePhoto();
				IconP.setVisible(true);
				
				addAccountB.setVisible(true);
				BrowseImagesB.setVisible(false);
				jLabelImage.setVisible(false);
				
				confirmB.setVisible(false);
				modifyB.setVisible(true);
			}
		});
		
		deleteB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteClient();
				dispose();
			}
		});
		
		BrowseImagesB.addActionListener(new ActionListener() {
            		public void actionPerformed(ActionEvent evt) {
                		BrowseImages(evt);
            		}
        	});
		addAccountB.addActionListener(new ActionListener() {
           		public void actionPerformed(ActionEvent evt) {
            			@SuppressWarnings("unused")
				AccountGI fiche = new AccountGI(client, null);
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
		    jLabelImage.setIcon(scaleImage(Client_photo, 120, 140));
        	}
   	}
	
	private void changePhoto() {
		new File("./Agency/Images/" + client.getCIN() + ".jpg").delete();
		MainGI.getAgency().Upload_Image(Client_photo, client.getCIN() );
		IconL.setIcon(
				scaleImage( new ImageIcon("./Agency/Images/" + client.getCIN() + ".jpg"),
							120, 140 ) );
	}
	
	private void deleteClient() {
		if ( MainGI.getAgency().removeClient( client.getCIN() ) ) {
			MainGI.RefreshList();
			JOptionPane.showMessageDialog(null, "Client deleted!", "Message", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void RefreshList() {
		listmodel.clear();
		List<Account> list = this.client.getAccounts();
		if ((list != null) &&  (list.size() > 0)) {
			for (int i = 0; i < list.size(); i++) {
				listmodel.addElement( Integer.toString( list.get(i).getCode() ) );
			}
		}
		else if (list.size() == 0) {
			listmodel.addElement("0");
		}
	}
	private void ListAccounts() {
		listmodel = new DefaultListModel<>();	
		RefreshList();
		accountsList = new JList<>(listmodel);
		accountsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		accountsList.addListSelectionListener(new ListSelectionListener() {
		    public void valueChanged(ListSelectionEvent e){
		        if(!e.getValueIsAdjusting()) {
		            final List<String> selectedValuesList = accountsList.getSelectedValuesList();
		            if (selectedValuesList.size() != 0) {
		            	Account account;
		            	if ( (account = client.SearchAccount( Integer.parseInt( selectedValuesList.get(0) ) ) ) != null) {
		            		@SuppressWarnings("unused")
					AccountGI fiche = new AccountGI(client, account);
		            	}
		            }
		        }
		    }
		});
		
		JScrollPane listScroller = new JScrollPane(accountsList);
		listScroller.setViewportView(accountsList);
		accountsList.setLayoutOrientation(JList.VERTICAL);
		listScroller.setPreferredSize(new Dimension(200, 500));
		plist = new JPanel();
		plist.add(listScroller);
		this.add(plist, BorderLayout.WEST);
		plist.setVisible(true);
	}
	
	public static ImageIcon scaleImage(ImageIcon icon, int w, int h) {
        int nw = icon.getIconWidth();
        int nh = icon.getIconHeight();

        if(icon.getIconWidth() > w){
          nw = w;
          nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
        }

        if(nh > h){
          nh = h;
          nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
        }
        return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_SMOOTH));
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;

public final class LoginGI extends JFrame{
	private static final long serialVersionUID = -8625102798689317248L;
	
	private static JPanel p1, p2;
	private static JLabel loginL, passwdL;
	private static JTextField loginTF;
	private static JPasswordField passwordTF;
	private static JButton confirmB, cancelB, subscribeB;
	private List<UserLogin> logins = new ArrayList<UserLogin>();
	
	private static JMenuBar menuBar;
	private static JMenu menu;
	private static JMenuItem New;
	
	public LoginGI() {
		p1 = new JPanel(new GridLayout());
		p2 = new JPanel(new FlowLayout());
		loginL = new JLabel("Login");
		passwdL = new JLabel("Password");
		loginTF = new JTextField("");
		passwordTF = new JPasswordField("");
		confirmB = new JButton("Confirm");
		subscribeB = new JButton("Add");
		cancelB = new JButton("Cancel");
		
		menuBar = new JMenuBar();
		menu = new JMenu("Menu");
		New = new JMenuItem("New");
		
		@SuppressWarnings("unused")
		boolean v = new File("./Logins").mkdir();
		try {
			@SuppressWarnings("unused")
			boolean v1 = new File("./Logins/Users.txt").createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		getLogins();
		initComponents();
		this.setVisible(true);
	}
	
	private void initComponents() {
		this.setTitle("Authentification");
		this.setSize(400, 200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		p1.add(loginL);
		p1.add(loginTF);
		p1.add(passwdL);
		p1.add(passwordTF);
		
		p2.add(confirmB);
		p2.add(subscribeB);
		p2.add(cancelB);
		
		this.add(p1, BorderLayout.NORTH);
		this.add(p2, BorderLayout.SOUTH);
		
		subscribeB.setVisible(false);
		cancelB.setVisible(false);
		
		menu.add(New);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
		
		New.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Clear_fields();
				confirmB.setVisible(false);
				cancelB.setVisible(true);
				subscribeB.setVisible(true);
			}
		});
		
		cancelB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Clear_fields();
				confirmB.setVisible(true);
				cancelB.setVisible(false);
				subscribeB.setVisible(false);
			}
		});
		
		subscribeB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				logins.add( new UserLogin( loginTF.getText(), 
								new String(passwordTF.getPassword()) ) );
				SaveLogins();
				Clear_fields();
				confirmB.setVisible(true);
				cancelB.setVisible(false);
				subscribeB.setVisible(false);
			}
		});
		confirmB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < logins.size(); i++) {
					if ((logins.get(i).getUserName().equals(loginTF.getText())) && 
							logins.get(i).getUserPass().equals(new String(passwordTF.getPassword()))) {
						@SuppressWarnings("unused")
						MainGI GestionInterface = new MainGI();
						dispose();
					}
				}
				if (isVisible()) {
					JOptionPane.showMessageDialog(null, "Failure", "Authentification", JOptionPane.ERROR_MESSAGE);
					Clear_fields();
				}
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	private void getLogins() {
		String filepath = "./Logins/Users.txt";
		if (Files.isRegularFile(new File(filepath).toPath())) {
	    	try {
	            // Reading the object from a file 
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filepath));
	            // Method for deserialization of object 
				logins = (List<UserLogin>) ois.readObject();
				ois.close();
	    	}
	    	catch(IOException ex) { 
	            System.out.println("IOException is caught :" + ex); 
	        } 
	          
	        catch(ClassNotFoundException ex) { 
	            System.out.println("ClassNotFoundException is caught :" + ex);
	        }
		}
	}
	
	private void SaveLogins() {
		String filepath = "./Logins/Users.txt";
		try {
			new File(filepath).delete();
			//Saving of object in a file 
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filepath));
			// Method for serialization of object
			oos.writeObject(logins);
			oos.flush();
			oos.close();
		}
		catch(IOException ex) { 
            System.out.println("IOException is caught :" + ex); 
        }
	}
	
	private void Clear_fields() {
		loginTF.setText("");
		passwordTF.setText("");
	}
}

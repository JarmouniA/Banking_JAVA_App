import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.util.List;
import javax.swing.*;

public final class AccountGI extends JFrame{
	private static final long serialVersionUID = 5161318020209981716L;
	private Client client;
	private Account account;
	
	private JPanel pfiche1, pfiche2, pfiche3, plist;
	private JLabel codeL, balanceL, typeL, attributeL, codeT, balanceT, typeT, attributeT;
	private JTextField balanceTF, typeTF, attributeTF, depositTF, withdrawTF;
	private JButton modifyB, cancelB, confirmB, deleteB, depositB, withdrawB;
	private GridBagConstraints c;
	
	private JList<String> transacsList;
	private DefaultListModel<String> listmodel;
	
	public AccountGI(Client cl, Account acc) {
		client = cl;
		account = acc;
		
		codeL = new JLabel("Code");
		balanceL = new JLabel("Balance");
		typeL = new JLabel("Type");
		attributeL = new JLabel("Attribute");
		
		cancelB = new JButton("Cancel");
		
		if (account != null) {
			this.setTitle( Integer.toString(account.getCode()) );
			codeT = new JLabel( Integer.toString(account.getCode()) );
			balanceT = new JLabel( Double.toString(account.getBalance()) );
			typeT = new JLabel(account.getType());
			attributeT = new JLabel( Double.toString(account.getAttribute()) );
			
			depositTF = new JTextField("");
			withdrawTF = new JTextField("");
			
			modifyB = new JButton("Modify");
			deleteB = new JButton("Delete");
			depositB = new JButton("Deposit");
			withdrawB = new JButton("Withdraw");
			
			c = new GridBagConstraints();
			
			pfiche3 = new JPanel(new GridBagLayout());
			
			ListTransacs();
		}
		else {
			this.setTitle("New Account");
			codeT = new JLabel( Integer.toString( client.getLastAccCode() + 1 ) );
			balanceTF = new JTextField("00.0");
			typeTF = new JTextField("0: Checking, 1: Savings");
			attributeTF = new JTextField("00.0");
			
			confirmB = new JButton("Confirm");
		}
		
		pfiche1 = new JPanel(new GridLayout(4, 2));
		pfiche2 = new JPanel(new FlowLayout());
		
		initComponents();
		this.setVisible(true);
	}
	
	private void initComponents() {
		this.setSize(450, 400);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		pfiche1.add(codeL);
		pfiche1.add(codeT);
		
		if (account != null) {
			pfiche1.add(balanceL);
			pfiche1.add(balanceT);
			pfiche1.add(typeL);
			pfiche1.add(typeT);
			pfiche1.add(attributeL);
			pfiche1.add(attributeT);
			
			pfiche2.add(modifyB);
			pfiche2.add(deleteB);
			
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.7;
			c.gridx = 0;
			c.gridy = 0;
			pfiche3.add(depositTF, c);
			
			c.weightx = 0.3;
			c.gridx = 1;
			c.gridy = 0;
			pfiche3.add(depositB, c);
			
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.7;
			c.gridx = 0;
			c.gridy = 1;
			pfiche3.add(withdrawTF, c);
			
			c.weightx = 0.3;
			c.gridx = 1;
			c.gridy = 1;
			pfiche3.add(withdrawB, c);
			
			this.add(pfiche3, BorderLayout.CENTER);
		}
		else {
			pfiche1.add(balanceL);
			pfiche1.add(balanceTF);
			pfiche1.add(typeL);
			pfiche1.add(typeTF);
			pfiche1.add(attributeL);
			pfiche1.add(attributeTF);
			
			pfiche2.add(confirmB);
		}
		
		pfiche2.add(cancelB);
		
		this.add(pfiche1, BorderLayout.NORTH);
		this.add(pfiche2, BorderLayout.SOUTH);
		
		if (account == null) {
			confirmB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					AddAccount();
					updateClient();
					JOptionPane.showMessageDialog(null, "Account added!", "Message", JOptionPane.INFORMATION_MESSAGE);
					dispose();
				}
			});
		}
		else {
			deleteB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (client.deleteAccount(account)) {
						updateClient();
						JOptionPane.showMessageDialog(null, "Account deleted!", "Message", JOptionPane.INFORMATION_MESSAGE);
					}
					dispose();
				}
			});
			depositB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (account.deposit( Double.parseDouble( depositTF.getText() ) ) ) {
						updateClient();
						balanceT.setText( Double.toString( account.getBalance()) );
						RefreshList();
						JOptionPane.showMessageDialog(null, "Amount added successfully!", "Message", JOptionPane.INFORMATION_MESSAGE);
					}
					else {
						JOptionPane.showMessageDialog(null, "Please try again!", "Message", JOptionPane.INFORMATION_MESSAGE);
					}
					depositTF.setText("");
				}
			});
			withdrawB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (account.withdraw( Double.parseDouble( withdrawTF.getText() ) ) ) {
						updateClient();
						balanceT.setText( Double.toString( account.getBalance()) );
						RefreshList();
						JOptionPane.showMessageDialog(null, "Amount withdrawn successfully!", "Message", JOptionPane.INFORMATION_MESSAGE);
					}
					else {
						JOptionPane.showMessageDialog(null, "Please try again!", "Message", JOptionPane.INFORMATION_MESSAGE);
					}
					withdrawTF.setText("");
				}
			});
		}
		
		cancelB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	private void AddAccount() {
		if ( typeTF.getText().equals("0") ) {
			client.addCheckingAccount( Double.parseDouble( balanceTF.getText() ), 
									    Double.parseDouble( attributeTF.getText() ) );
		}
		else {
			client.addSavingsAccount( Double.parseDouble( balanceTF.getText() ), 
				    						Double.parseDouble( attributeTF.getText() ) );
		}
	}
	private void updateClient() {
		if (new File("./Agency/Clients/" + client.getCIN() + ".txt").delete() ){
			try {
				//Saving of object in a file 
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./Agency/Clients/" + client.getCIN() + ".txt"));
				// Method for serialization of object
				oos.writeObject(client);
				oos.flush();
				oos.close();
			}
			catch(IOException ex) { 
	            System.out.println("IOException is caught :" + ex); 
	        }
		}
	}
	private void RefreshList() {
		listmodel.clear();
		List<Transaction> list = this.account.getTransactions();
		if ((list != null) && (list.size() > 0)) {
			for (int i = 0; i < list.size(); i++) {
				listmodel.addElement( DateFormat.getDateInstance().format(list.get(i).getDate()) );
			}
		}
		else if (list.size() == 0){
			listmodel.addElement("0");
		}
	}
	private void ListTransacs() {
		listmodel = new DefaultListModel<>();
		RefreshList();
		transacsList = new JList<>(listmodel);
		transacsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane listScroller = new JScrollPane(transacsList);
		listScroller.setViewportView(transacsList);
		transacsList.setLayoutOrientation(JList.VERTICAL);
		listScroller.setPreferredSize(new Dimension(200, 500));
		plist = new JPanel();
		plist.add(listScroller);
		this.add(plist, BorderLayout.WEST);
		plist.setVisible(true);
	}
}

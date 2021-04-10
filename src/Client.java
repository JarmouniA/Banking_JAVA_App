import java.io.*;
import java.util.*;

public final class Client implements Serializable, Comparable<Client> {	
	private static final long serialVersionUID = -8950357095205074738L;
	
	private int CIN;
	private String name;
	private int lastAccCode;
	
	private List<Account> Accounts = new ArrayList<Account>();
	
	public Client(int Cin, String Name) {
		CIN = Cin;
		name = Name;
		lastAccCode = 787134716;
	}
	
	public int getCIN() {
		return this.CIN;
	}
	public String getName() {
		return this.name;
	}
	public int getLastAccCode() {
		return this.lastAccCode;
	}
	public void setLastAccCode(int code) {
		this.lastAccCode = code;
	}
	public List<Account> getAccounts(){
		return this.Accounts;
	}
	
	public void addCheckingAccount(double InitialBalance, double Overdraft) {
		this.Accounts.add(new CheckingAccount(this, InitialBalance, Overdraft));
	}
	
	public void addSavingsAccount(double InitialBalance, double taux) {
		this.Accounts.add(new SavingsAccount(this, InitialBalance, taux));
	}
	
	public Account SearchAccount(int AccountCode){
		for (int i = 0; i < this.Accounts.size(); i++) {
			Account acc = this.Accounts.get(i);
			if (acc.getCode() == AccountCode) {
				return acc;
			}
		}
	    return null;
	}
	public boolean deleteAccount(Account acc) {
		for (int i = 0; i < this.Accounts.size(); i++) {
			if (this.Accounts.get(i).getCode() == acc.getCode()) {
				this.Accounts.remove(i);
				return true;
			}
		}
		return false;
	}
	
	@Override public int compareTo(Client arg0) {
		double totBalance = 0.0, totBalance1 = 0.0; 
		for (int i = 0; i < this.Accounts.size(); i++) {
	    	totBalance += this.Accounts.get(i).getBalance();
		}
		
		for (int i = 0; i < arg0.Accounts.size(); i++) {
	    	totBalance1 += arg0.Accounts.get(i).getBalance();
		}
		
		if (totBalance > totBalance1)
			return 1;
		else if (totBalance < totBalance1)
			return -1;
		return 0;
	}
}

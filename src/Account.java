import java.io.*;
import java.util.*;

abstract public class Account implements Serializable {
	private static final long serialVersionUID = -1742723906779054604L;
	
	private int code;
    private int clientCIN;
    protected double balance;
    
    private List<Transaction> transa = new ArrayList<Transaction>();
    
    public Account(Client client, double Balance){
        clientCIN = client.getCIN();
        balance = Balance;
        code = client.getLastAccCode() + 1;
        client.setLastAccCode(code);
        addTransaction(0, Balance);
    }
    
    abstract public boolean deposit(double Amount);
    abstract public boolean withdraw(double Amount);
    abstract public String getType();
    abstract public double getAttribute();
    
    public List<Transaction> getTransactions(){
    	return this.transa;
    }
    
    public int getCode(){
        return this.code;
    }
    public double getBalance(){
        return this.balance;
    }
    public int getClientCIN(){
        return this.clientCIN;
    }
    
    protected void addTransaction(int type, double Amount) { // 0: deposit , 1: withdraw
    	this.transa.add(new Transaction(type, Amount));
    }
    
    
}

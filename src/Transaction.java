import java.io.*;
import java.util.*;

public final class Transaction implements Serializable {
	private static final long serialVersionUID = -88485430006534552L;
	
	private Date date;
	private int type; // 0 : deposit , 1 : withdraw
	private double amount;
	
	public Transaction(int Type, double Amount) {
		this.date = new Date();
		this.type = Type;
		this.amount = Amount;
	}
	
	public Date getDate() {
		return this.date;
	}
	
	public int getType() {
		return this.type;
	}
	
	public double getAmount() {
		return this.amount;
	}
}

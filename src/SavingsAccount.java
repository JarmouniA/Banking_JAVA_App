class SavingsAccount extends Account implements Comparable<SavingsAccount>{
	private static final long serialVersionUID = -5387556641397641117L;
	
	private double interestRate;
	final String type = "SavingsAccount";
	
    public SavingsAccount(Client client, double Balance, double rate){
        super(client, Balance);
        if (rate > 0){
        	interestRate = rate;
        }
        else
        	interestRate = 0;
    }
    public void setInterestRate(double rate){
        this.interestRate = rate;
    }
    public double getAttribute() {
    	return this.interestRate;
    }
    public String getType() {
    	return this.type;
    }
    public boolean deposit(double Amount){
        if (Amount > 0){
            this.balance += Amount * (1 + this.interestRate);
            this.addTransaction(0, Amount);
            return true;
        }
        return false;
    }
    public boolean withdraw(double Amount){
        this.balance -= Amount;
        this.addTransaction(1, Amount);
        return true;
    }
    public int compareTo(SavingsAccount arg0) {
	if (this.interestRate > arg0.interestRate)
		return 1;
	else if (this.interestRate < arg0.interestRate)
		return -1;
	return 0;
    }
}


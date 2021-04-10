public class CheckingAccount extends Account implements Comparable<CheckingAccount>{    
    private static final long serialVersionUID = 4293439065594743029L;
    
    private double overdraft;
    final String type =  "CheckingAccount";
    
    public CheckingAccount(Client client, double Balance, double Overdraft){
        super(client, Balance);
        if (Overdraft > 0){
            overdraft = Overdraft;
        }
        else
            overdraft = 0;
    }
    public void setOverdraft(double Overdraft){
        this.overdraft = Overdraft;
    }
    public double getAttribute() {
        return this.overdraft;
    }
    public String getType() {
        return this.type;
    }
    public boolean deposit(double Amount){
        if (Amount > 0){
            this.balance += Amount;
            this.addTransaction(0, Amount);
            return true;
        }
        return false;
    }
    public boolean withdraw(double Amount){
        if (this.balance + this.overdraft > Amount){
            this.balance -= Amount;
            this.addTransaction(1, Amount);
            return true;
        }
        return false;
    }
    public int compareTo(CheckingAccount arg0) {
        if (this.overdraft > arg0.overdraft)
            return 1;
        else if (this.overdraft < arg0.overdraft)
            return -1;
        return 0;
    }
}

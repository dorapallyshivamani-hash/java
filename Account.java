public abstract class Account {
    protected String accountNumber;
    protected String holderName;
    protected double balance;

    public Account(String accountNumber, String holderName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = initialBalance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        } else {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
    }

    public abstract void withdraw(double amount) throws Exception;

    public double getBalance() {
        return balance;
    }

    public String displayBalance() {
        return "Account: " + accountNumber + " | Holder: " + holderName + " | Balance: $" + String.format("%.2f", balance);
    }
}

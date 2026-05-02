public class CurrentAccount extends Account {
    private double overdraftLimit;

    public CurrentAccount(String accountNumber, String holderName, double initialBalance, double overdraftLimit) {
        super(accountNumber, holderName, initialBalance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(double amount) throws Exception {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (balance - amount < -overdraftLimit) {
            throw new Exception("Overdraft limit exceeded. Available withdrawal limit: $" + String.format("%.2f", balance + overdraftLimit));
        }
        balance -= amount;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }
}

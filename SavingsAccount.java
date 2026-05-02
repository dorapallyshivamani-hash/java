public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(String accountNumber, String holderName, double initialBalance, double interestRate) {
        super(accountNumber, holderName, initialBalance);
        this.interestRate = interestRate;
    }

    @Override
    public void withdraw(double amount) throws Exception {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (amount > balance) {
            throw new Exception("Insufficient balance. Cannot withdraw more than available balance.");
        }
        balance -= amount;
    }

    public void addInterest() {
        double interest = balance * (interestRate / 100.0);
        deposit(interest);
    }
}

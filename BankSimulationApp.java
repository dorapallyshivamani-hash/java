import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BankSimulationApp extends JFrame {

    private JTextField accNumField;
    private JTextField holderNameField;
    private JTextField amountField;
    private JComboBox<String> accountTypeCombo;
    private JButton submitBtn;
    private JButton createAccBtn;
    private JButton depositBtn;
    private JButton withdrawBtn;
    private JButton checkBalanceBtn;
    private JButton addInterestBtn;
    private JTextArea outputArea;

    private Account account; // Polymorphic reference
    private boolean detailsSubmitted = false;

    // Constants
    private static final double SAVINGS_INTEREST_RATE = 5.0; // 5%
    private static final double CURRENT_OVERDRAFT_LIMIT = 1000.0;

    public BankSimulationApp() {
        setTitle("Bank Account Simulation System");
        setSize(550, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();
        layoutComponents();
        addListeners();
    }

    private void initComponents() {
        accNumField = new JTextField(15);
        holderNameField = new JTextField(15);
        amountField = new JTextField(15);
        
        String[] accountTypes = {"Savings Account", "Current Account"};
        accountTypeCombo = new JComboBox<>(accountTypes);

        submitBtn = new JButton("Submit");
        createAccBtn = new JButton("Create Account");
        createAccBtn.setEnabled(false); 
        
        depositBtn = new JButton("Deposit");
        withdrawBtn = new JButton("Withdraw");
        checkBalanceBtn = new JButton("Check Balance");
        addInterestBtn = new JButton("Add Interest");
        
        setOperationButtonsEnabled(false);

        outputArea = new JTextArea(12, 40);
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
    }

    private void layoutComponents() {
        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        inputPanel.add(new JLabel("Account Type:"));
        inputPanel.add(accountTypeCombo);
        inputPanel.add(new JLabel("Account Number:"));
        inputPanel.add(accNumField);
        inputPanel.add(new JLabel("Account Holder Name:"));
        inputPanel.add(holderNameField);
        inputPanel.add(new JLabel("Initial Balance ($):"));
        inputPanel.add(amountField);

        // Setup Buttons Panel
        JPanel setupPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        setupPanel.add(submitBtn);
        setupPanel.add(createAccBtn);

        // Operations Panel
        JPanel opsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        opsPanel.setBorder(BorderFactory.createTitledBorder("Operations"));
        opsPanel.add(depositBtn);
        opsPanel.add(withdrawBtn);
        opsPanel.add(checkBalanceBtn);
        opsPanel.add(addInterestBtn);

        // Top Wrapper
        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.add(inputPanel, BorderLayout.NORTH);
        topWrapper.add(setupPanel, BorderLayout.CENTER);
        
        JPanel midWrapper = new JPanel(new BorderLayout());
        midWrapper.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        midWrapper.add(opsPanel, BorderLayout.CENTER);

        JPanel upperHalf = new JPanel(new BorderLayout());
        upperHalf.add(topWrapper, BorderLayout.NORTH);
        upperHalf.add(midWrapper, BorderLayout.CENTER);

        // Output Panel
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(BorderFactory.createTitledBorder("Console Output"));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        outputPanel.add(scrollPane, BorderLayout.CENTER);

        add(upperHalf, BorderLayout.NORTH);
        add(outputPanel, BorderLayout.CENTER);
    }

    private void setOperationButtonsEnabled(boolean enabled) {
        depositBtn.setEnabled(enabled);
        withdrawBtn.setEnabled(enabled);
        checkBalanceBtn.setEnabled(enabled);
        
        if (enabled && account instanceof SavingsAccount) {
            addInterestBtn.setEnabled(true);
        } else {
            addInterestBtn.setEnabled(false);
        }
    }

    private void addListeners() {
        submitBtn.addActionListener(e -> {
            String accNum = accNumField.getText().trim();
            String name = holderNameField.getText().trim();
            
            if (accNum.isEmpty() || name.isEmpty()) {
                printToConsole(">> Error: Account Number and Holder Name cannot be empty.");
                detailsSubmitted = false;
                createAccBtn.setEnabled(false);
            } else {
                printToConsole(">> Details validated. Click 'Create Account' to proceed.");
                detailsSubmitted = true;
                createAccBtn.setEnabled(true);
            }
        });

        createAccBtn.addActionListener(e -> {
            if (!detailsSubmitted) return;

            String accNum = accNumField.getText().trim();
            String name = holderNameField.getText().trim();
            double initialBalance = 0.0;
            
            String amountText = amountField.getText().trim();
            if (!amountText.isEmpty()) {
                try {
                    initialBalance = Double.parseDouble(amountText);
                    if (initialBalance < 0) {
                        printToConsole(">> Error: Initial balance cannot be negative.");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    printToConsole(">> Error: Invalid initial balance amount.");
                    return;
                }
            }

            String type = (String) accountTypeCombo.getSelectedItem();
            if ("Savings Account".equals(type)) {
                account = new SavingsAccount(accNum, name, initialBalance, SAVINGS_INTEREST_RATE);
                printToConsole(">> Savings Account created with " + SAVINGS_INTEREST_RATE + "% interest rate.");
            } else {
                account = new CurrentAccount(accNum, name, initialBalance, CURRENT_OVERDRAFT_LIMIT);
                printToConsole(">> Current Account created with $" + CURRENT_OVERDRAFT_LIMIT + " overdraft limit.");
            }

            printToConsole(">> " + account.displayBalance());
            
            submitBtn.setEnabled(false);
            createAccBtn.setEnabled(false);
            accNumField.setEditable(false);
            holderNameField.setEditable(false);
            accountTypeCombo.setEnabled(false);
            
            setOperationButtonsEnabled(true);
            amountField.setText("");
        });

        depositBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Enter amount to deposit:", "Deposit", JOptionPane.QUESTION_MESSAGE);
            if (input != null && !input.trim().isEmpty()) {
                try {
                    double amount = Double.parseDouble(input.trim());
                    account.deposit(amount);
                    printToConsole(">> Deposited: $" + String.format("%.2f", amount));
                    printToConsole(">> " + account.displayBalance());
                } catch (NumberFormatException ex) {
                    printToConsole(">> Error: Please enter a valid numeric deposit amount.");
                } catch (IllegalArgumentException ex) {
                    printToConsole(">> Error: " + ex.getMessage());
                }
            }
        });

        withdrawBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Enter amount to withdraw:", "Withdraw", JOptionPane.QUESTION_MESSAGE);
            if (input != null && !input.trim().isEmpty()) {
                try {
                    double amount = Double.parseDouble(input.trim());
                    account.withdraw(amount); // Polymorphic call
                    printToConsole(">> Withdrawn: $" + String.format("%.2f", amount));
                    printToConsole(">> " + account.displayBalance());
                } catch (NumberFormatException ex) {
                    printToConsole(">> Error: Please enter a valid numeric withdrawal amount.");
                } catch (Exception ex) {
                    printToConsole(">> Error: " + ex.getMessage());
                }
            }
        });

        checkBalanceBtn.addActionListener(e -> {
            printToConsole(">> " + account.displayBalance());
        });

        addInterestBtn.addActionListener(e -> {
            if (account instanceof SavingsAccount) {
                ((SavingsAccount) account).addInterest();
                printToConsole(">> Interest added to Savings Account.");
                printToConsole(">> " + account.displayBalance());
            }
        });
    }

    private void printToConsole(String message) {
        outputArea.append(message + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new BankSimulationApp().setVisible(true);
        });
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

// DashboardFrame.java - main ATM operations screen after login
public class DashboardFrame extends JFrame implements ActionListener {
    private AccountManager manager;
    private Account account;

    private JLabel balanceLabel;
    private JButton depositBtn, withdrawBtn, balanceBtn, statementBtn, logoutBtn;

    public DashboardFrame(AccountManager manager, Account account) {
        this.manager = manager;
        this.account = account;

        setTitle("ATM Simulator - Dashboard");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel welcome = new JLabel("Account: " + account.getAccountNumber(), SwingConstants.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 16));
        welcome.setBounds(50, 20, 300, 25);
        add(welcome);

        balanceLabel = new JLabel("Balance: " + account.getBalance(), SwingConstants.CENTER);
        balanceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        balanceLabel.setBounds(50, 55, 300, 25);
        add(balanceLabel);

        depositBtn = new JButton("Deposit");
        depositBtn.setBounds(100, 100, 200, 30);
        depositBtn.addActionListener(this);
        add(depositBtn);

        withdrawBtn = new JButton("Withdraw");
        withdrawBtn.setBounds(100, 140, 200, 30);
        withdrawBtn.addActionListener(this);
        add(withdrawBtn);

        balanceBtn = new JButton("Balance Enquiry");
        balanceBtn.setBounds(100, 180, 200, 30);
        balanceBtn.addActionListener(this);
        add(balanceBtn);

        statementBtn = new JButton("Mini Statement");
        statementBtn.setBounds(100, 220, 200, 30);
        statementBtn.addActionListener(this);
        add(statementBtn);

        logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(100, 260, 200, 30);
        logoutBtn.addActionListener(this);
        add(logoutBtn);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == depositBtn) {
            String input = JOptionPane.showInputDialog(this, "Enter amount to deposit:");
            if (input != null) {
                try {
                    double amount = Double.parseDouble(input);
                    if (amount <= 0) {
                        JOptionPane.showMessageDialog(this, "Enter a valid positive amount.");
                        return;
                    }
                    manager.deposit(account, amount);
                    refreshBalance();
                    JOptionPane.showMessageDialog(this, "Deposit successful!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid amount entered.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == withdrawBtn) {
            String input = JOptionPane.showInputDialog(this, "Enter amount to withdraw:");
            if (input != null) {
                try {
                    double amount = Double.parseDouble(input);
                    if (amount <= 0) {
                        JOptionPane.showMessageDialog(this, "Enter a valid positive amount.");
                        return;
                    }
                    manager.withdraw(account, amount);
                    refreshBalance();
                    JOptionPane.showMessageDialog(this, "Withdrawal successful!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid amount entered.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (InsufficientBalanceException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == balanceBtn) {
            JOptionPane.showMessageDialog(this, "Current Balance: " + account.getBalance());
        } else if (e.getSource() == statementBtn) {
            List<String> statement = manager.getMiniStatement(account.getAccountNumber(), 5);
            if (statement.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No transactions yet.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (String line : statement) {
                    sb.append(line).append("\n");
                }
                JOptionPane.showMessageDialog(this, sb.toString(), "Mini Statement", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (e.getSource() == logoutBtn) {
            dispose();
            new LoginFrame(manager);
        }
    }

    private void refreshBalance() {
        balanceLabel.setText("Balance: " + account.getBalance());
    }
}

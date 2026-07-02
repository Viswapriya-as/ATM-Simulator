import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// LoginFrame.java - first screen: Login or Signup
public class LoginFrame extends JFrame implements ActionListener {
    private JTextField accField;
    private JPasswordField pinField;
    private JButton loginBtn, signupBtn;
    private AccountManager manager;

    public LoginFrame(AccountManager manager) {
        this.manager = manager;

        setTitle("ATM Simulator - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel title = new JLabel("WELCOME TO ATM", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(50, 20, 300, 30);
        add(title);

        JLabel accLabel = new JLabel("Account No:");
        accLabel.setBounds(50, 80, 100, 25);
        add(accLabel);

        accField = new JTextField();
        accField.setBounds(160, 80, 180, 25);
        add(accField);

        JLabel pinLabel = new JLabel("PIN:");
        pinLabel.setBounds(50, 120, 100, 25);
        add(pinLabel);

        pinField = new JPasswordField();
        pinField.setBounds(160, 120, 180, 25);
        add(pinField);

        loginBtn = new JButton("LOGIN");
        loginBtn.setBounds(60, 180, 120, 30);
        loginBtn.addActionListener(this);
        add(loginBtn);

        signupBtn = new JButton("SIGN UP");
        signupBtn.setBounds(210, 180, 120, 30);
        signupBtn.addActionListener(this);
        add(signupBtn);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginBtn) {
            String accNo = accField.getText().trim();
            String pin = new String(pinField.getPassword()).trim();

            Account acc = manager.login(accNo, pin);
            if (acc != null) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                new DashboardFrame(manager, acc);
                dispose(); // close login window
            } else {
                JOptionPane.showMessageDialog(this, "Invalid account number or PIN.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == signupBtn) {
            openSignupDialog();
        }
    }

    private void openSignupDialog() {
        JTextField newAcc = new JTextField();
        JPasswordField newPin = new JPasswordField();
        JTextField initialDeposit = new JTextField();

        Object[] fields = {
            "New Account No:", newAcc,
            "Set PIN:", newPin,
            "Initial Deposit:", initialDeposit
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Create New Account", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String accNo = newAcc.getText().trim();
                String pin = new String(newPin.getPassword()).trim();
                double deposit = Double.parseDouble(initialDeposit.getText().trim());

                if (accNo.isEmpty() || pin.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Account number and PIN cannot be empty.");
                    return;
                }

                boolean created = manager.createAccount(accNo, pin, deposit);
                if (created) {
                    JOptionPane.showMessageDialog(this, "Account created successfully! You can now log in.");
                } else {
                    JOptionPane.showMessageDialog(this, "Account number already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Initial deposit must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

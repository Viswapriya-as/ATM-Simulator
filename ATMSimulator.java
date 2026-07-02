import javax.swing.*;

// ATMSimulator.java - entry point
public class ATMSimulator {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AccountManager manager = new AccountManager();
            new LoginFrame(manager);
        });
    }
}

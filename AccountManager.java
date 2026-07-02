import java.io.*;
import java.util.*;

// AccountManager.java - business logic layer
// Handles loading/saving accounts to file, login, signup, deposit, withdraw
public class AccountManager {
    private static final String ACCOUNTS_FILE = "accounts.txt";
    private static final String TRANSACTIONS_FILE = "transactions.txt";

    private HashMap<String, Account> accounts = new HashMap<>();

    public AccountManager() {
        loadAccounts();
    }

    // Load all accounts from file into memory
    private void loadAccounts() {
        File file = new File(ACCOUNTS_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    Account acc = new Account(parts[0], parts[1], Double.parseDouble(parts[2]));
                    accounts.put(acc.getAccountNumber(), acc);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading accounts: " + e.getMessage());
        }
    }

    // Save all accounts back to file (overwrite)
    private void saveAllAccounts() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ACCOUNTS_FILE))) {
            for (Account acc : accounts.values()) {
                writer.println(acc.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }

    // Create a new account (signup)
    public boolean createAccount(String accNo, String pin, double initialDeposit) {
        if (accounts.containsKey(accNo)) {
            return false; // already exists
        }
        Account acc = new Account(accNo, pin, initialDeposit);
        accounts.put(accNo, acc);
        saveAllAccounts();
        logTransaction(accNo, "Account created with initial deposit: " + initialDeposit);
        return true;
    }

    // Login - returns Account if valid, null if not
    public Account login(String accNo, String pin) {
        Account acc = accounts.get(accNo);
        if (acc != null && acc.getPin().equals(pin)) {
            return acc;
        }
        return null;
    }

    public void deposit(Account acc, double amount) {
        acc.setBalance(acc.getBalance() + amount);
        saveAllAccounts();
        logTransaction(acc.getAccountNumber(), "Deposited: " + amount + " | New Balance: " + acc.getBalance());
    }

    public void withdraw(Account acc, double amount) throws InsufficientBalanceException {
        if (amount > acc.getBalance()) {
            throw new InsufficientBalanceException("Insufficient balance. Available: " + acc.getBalance());
        }
        acc.setBalance(acc.getBalance() - amount);
        saveAllAccounts();
        logTransaction(acc.getAccountNumber(), "Withdrew: " + amount + " | New Balance: " + acc.getBalance());
    }

    // Append a transaction record to the log file
    private void logTransaction(String accNo, String detail) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TRANSACTIONS_FILE, true))) {
            writer.println(accNo + " | " + detail);
        } catch (IOException e) {
            System.out.println("Error logging transaction: " + e.getMessage());
        }
    }

    // Get last N transactions for an account (mini statement)
    public List<String> getMiniStatement(String accNo, int limit) {
        List<String> allLines = new ArrayList<>();
        File file = new File(TRANSACTIONS_FILE);
        if (!file.exists()) return allLines;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(accNo + " |")) {
                    allLines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading transactions: " + e.getMessage());
        }

        int fromIndex = Math.max(0, allLines.size() - limit);
        return allLines.subList(fromIndex, allLines.size());
    }
}

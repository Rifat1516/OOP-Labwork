import Bank.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Atm {
    private static final String DATABASE_FILE = "Bank Database.txt" ;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Welcome to IIT Banking System");
        System.out.println("Enter credentials to proceed");
        ArrayList<Account> accounts = loadAccounts();

        Account currentAccount = null;

        System.out.println("Welcome to the ATM Booth");
        while (true) {
            System.out.println("\n1. Create New Account");
            System.out.println("2. Login to Existing Account");
            System.out.println("3. Exit ATM");
            System.out.print("Enter Your Option: ");
            int initialOption = input.nextInt();
            input.nextLine();

            if (initialOption == 3) {
                System.out.println("Thank You for using the ATM.");
                saveAccounts(accounts);
                break;
            }

            switch (initialOption) {
                case 1:
                    System.out.print("Enter New Account Number: ");
                    int newAccNum = input.nextInt();
                    input.nextLine();

                    boolean accExists = false;
                    for (Account acc : accounts) {
                        if (acc.getAccountNumber() == newAccNum) {
                            accExists = true;
                            System.out.println("Account Number already exists. Please choose a different one.");
                            break;
                        }
                    }

                    if (!accExists) {
                        System.out.print("Enter Account Holder Name: ");
                        String newAccHolder = input.nextLine();

                        System.out.println("Select Account Type:");
                        System.out.println("1. Saving Account");
                        System.out.println("2. Current Account");
                        System.out.println("3. Fixed Deposit Account");
                        System.out.print("Enter your choice: ");
                        int accountTypeChoice = input.nextInt();
                        input.nextLine();

                        System.out.print("Enter Initial Balance: ");
                        float initialBalance = input.nextFloat();
                        input.nextLine();
                        System.out.print("Set a new Pin for the account: ");
                        int newPassWord = input.nextInt();
                        input.nextLine();

                        Account newAccount = null;
                        switch (accountTypeChoice) {
                            case 1:
                                newAccount = new SavingAccount(newAccNum, newAccHolder, initialBalance, newPassWord);
                                break;
                            case 2:
                                newAccount = new CurrentAccount(newAccNum, newAccHolder, initialBalance, newPassWord);
                                break;
                            case 3:
                                newAccount = new FixedDepositAccount(newAccNum, newAccHolder, initialBalance, newPassWord);
                                break;
                            default:
                                System.out.println("Invalid account type choice. Account creation failed.");
                                break;
                        }

                        if (newAccount != null) {
                            accounts.add(newAccount);
                            System.out.println("Account created successfully!");
                        }

                        saveAccounts(accounts);
                    }
                    break;

                case 2:
                    System.out.print("Enter Account Number: ");
                    int accNum = input.nextInt();
                    System.out.print("Enter Password: ");
                    int password = input.nextInt();

                    for (Account acc : accounts) {
                        if (acc.getAccountNumber() == accNum && acc.getPassWord() == password) {
                            currentAccount = acc;
                            break;
                        }
                    }

                    if (currentAccount != null) {
                        System.out.println("Welcome Mr." + currentAccount.getAccountHolder() + " to the Atm Booth");
                        while (true) {
                            System.out.println("\n1. Deposit");
                            System.out.println("2. Withdraw");
                            System.out.println("3. Transfer to other bank accounts");
                            System.out.println("4. Balance Inquiry");
                            System.out.println("5. Pin Change");
                            System.out.println("6. Mini Statement");
                            System.out.println("7. Logout");

                            System.out.print("Enter Your Option: ");
                            int option = input.nextInt();

                            if (option == 7) {
                                System.out.println("Logging out from " + currentAccount.getAccountHolder());
                                saveAccounts(accounts);
                                currentAccount = null;
                                break;
                            }
                            switch (option) {
                                case 1:
                                    currentAccount.deposit();
                                    saveAccounts(accounts);
                                    break;
                                case 2:
                                    currentAccount.withdraw();
                                    saveAccounts(accounts);
                                    break;
                                case 3:
                                    System.out.print("Enter Destination Account Number: ");
                                    int destAccNum = input.nextInt();
                                    input.nextLine();

                                    Account destinationAccount = null;
                                    for (Account acc : accounts) {
                                        if (acc.getAccountNumber() == destAccNum) {
                                            destinationAccount = acc;
                                            break;
                                        }
                                    }

                                    if (destinationAccount != null) {
                                        if (destinationAccount.getAccountNumber() == currentAccount.getAccountNumber()) {
                                            System.out.println("Cannot transfer to the same account.");
                                        } else {
                                            System.out.print("Enter Amount to Transfer: ");
                                            int transferAmount = input.nextInt();
                                            currentAccount.transferTo(destinationAccount, transferAmount);
                                            System.out.println("Your current balance: " + currentAccount.getCurrentBalance());
                                        }
                                    } else {
                                        System.out.println("Destination account not found.");
                                    }
                                    System.out.println();
                                    saveAccounts(accounts);
                                    break;
                                case 4:
                                    System.out.println("Current Balance: " + currentAccount.getCurrentBalance());
                                    System.out.println();
                                    break;
                                case 5:
                                    currentAccount.setPassword();
                                    saveAccounts(accounts);
                                    break;
                                case 6:
                                    System.out.println("Mini Statement");
                                    System.out.println("Account Number: " + currentAccount.getAccountNumber());
                                    System.out.println("Account Holder: " + currentAccount.getAccountHolder());
                                    System.out.println("Recent Deposit History: " + currentAccount.getLastDeposit());
                                    System.out.println("Recent Withdrawal History: " + currentAccount.getLastWithdrawal());
                                    System.out.println("Current Balance: " + currentAccount.getCurrentBalance());
                                    System.out.println();
                                    break;
                                default:
                                    System.out.println("Invalid option. Please try again.");
                            }
                        }
                    } else {
                        System.out.println("Invalid Credentials. Account not found or wrong password.");
                    }
                    break;
                default:
                    System.out.println("Invalid initial option. Please try again.");
            }
        }
        input.close();
    }
    private static void saveAccounts(ArrayList<Account> accounts) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATABASE_FILE))) {
            oos.writeObject(accounts);
        } catch (IOException e) {
            System.err.println("Error saving accounts: " + e.getMessage());
        }
    }
    @SuppressWarnings("unchecked")
    private static ArrayList<Account> loadAccounts() {
        ArrayList<Account> accounts = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATABASE_FILE))) {
            accounts = (ArrayList<Account>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Welcome To new Bank Management System");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading accounts: " + e.getMessage());
        }
        return accounts;
    }
}
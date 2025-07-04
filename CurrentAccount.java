package Bank;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.Scanner;

public class CurrentAccount extends Account implements Serializable {
    private transient Scanner input = new Scanner(System.in);

    private final int accountNumber;
    private final String accountHolder;
    private int passWord;
    private float lastDeposit;
    private float lastWithdraw;

    public CurrentAccount(int accountNumber, String accountHolder, float initialBalance, int passWord) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.currentBalance = initialBalance;
        this.passWord = passWord;
    }


    public float getCurrentBalance() {
        return currentBalance;
    }
    public int getAccountNumber() {
        return accountNumber;
    }
    public String getAccountHolder() {
        return accountHolder;
    }

    public int getPassWord() {
        return passWord;
    }

    public void setPassword() {
        System.out.print("Enter Previous Pin: ");
        int prevPass = input.nextInt();

        if(prevPass == passWord){
            System.out.print("Enter New Pin: ");
            passWord = input.nextInt();
            System.out.println("Pin Changed Successfully");
        }
        else System.out.println("Invalid Credentials.");

        System.out.println();
    }

    public void deposit(){
        System.out.print("Enter Pin Number: ");
        int pinNum = input.nextInt();

        if(pinNum == passWord){
            System.out.print("Enter Amount to Deposit: ");
            int amount = input.nextInt();
            currentBalance+=amount;
            lastDeposit = amount;
            System.out.println("Successfully Deposited...");
        }
        else System.out.println("Invalid Credentials.");
        System.out.println();
    }

    public void withdraw(){
        System.out.print("Enter Pin Number: ");
        int password = input.nextInt();

        if(password == passWord) {
            System.out.print("Enter Amount to Withdraw: ");
            int amount = input.nextInt();
            if (amount > currentBalance) System.out.println("Insufficient Balance.");
            else {
                currentBalance -= amount;
                lastWithdraw = amount;
                System.out.println("Successfully Withdraw.");
            }
        }
        else System.out.println("Invalid Credentials");
        System.out.println();
    }

    public float getLastDeposit(){
        return lastDeposit;
    }
    public float getLastWithdrawal(){
        return lastWithdraw;
    }
    public void transferTo(Account another, int amount) {
        if (amount > this.currentBalance) {
            System.out.println("Amount exceeded balance.");
        } else {
            this.currentBalance -= amount;
            another.currentBalance += amount;
        }
    }

    @Serial
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        input = new Scanner(System.in);
    }
}

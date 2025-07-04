package Bank;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.Scanner;

public class FixedDepositAccount extends Account implements Serializable {
        private transient Scanner input = new Scanner(System.in);

        private final int accountNumber;
        private final String accountHolder;
        private int passWord;
        private int installment = 0;
        private final int initialBalance;
        private float lastDeposit;
        private int depositCount = 0;

        public FixedDepositAccount(int accountNumber, String accountHolder, float initialBalance, int passWord) {
            this.accountNumber = accountNumber;
            this.accountHolder = accountHolder;
            this.currentBalance = initialBalance;
            this.initialBalance = (int) initialBalance;
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
                installment+=amount;
                depositCount++;
                if(depositCount >= 12){
                    float interestAmount = (float) (currentBalance*0.07);
                    currentBalance+=interestAmount;
                }
                System.out.println("Successfully Deposited...");
            }
            else System.out.println("Invalid Credentials.");
            System.out.println();
        }

        public void withdraw() {

            if (installment >= (initialBalance*5)) {
                System.out.print("Enter Pin Number: ");
                int password = input.nextInt();

                if(password == passWord) {
                    System.out.print("Enter Amount to Withdraw: ");
                    int amount = input.nextInt();
                    if (amount > currentBalance) System.out.println("Insufficient Balance.");
                    else {
                        currentBalance -= amount;
                        System.out.println("Withdrawal Successfully.");
                    }
                }
                else System.out.println("Invalid Credentials");
                System.out.println();
            }
            else{
                System.out.println("Cannot Withdraw Now, Complete your installment");
            }
        }

    @Override
    public void transferTo(Account another, int amount) {
        if(installment>=5){
            if (amount > this.currentBalance) {
                System.out.println("Amount exceeded balance.");
            } else {
                this.currentBalance -= amount;
                another.currentBalance += amount;
            }
        }
        else{
            System.out.println("Transfer not possible now");
        }
    }
    public float getLastWithdrawal(){
        return 0.0f;
    }

    @Override
    public float getLastDeposit() {
        return lastDeposit;
    }

    @Serial
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        input = new Scanner(System.in);
    }
}

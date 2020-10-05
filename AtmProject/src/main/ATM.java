package main;

import java.util.Scanner;

public class ATM {

	public static void main(String[] args) {

		// initiate scanner

		Scanner sc = new Scanner(System.in);

		// create bank object
		String bankName = "The National Bank";

		Bank bank = new Bank(bankName);

		// add a user which also creates a savings account
		String pin = "1234";
		String firstName = "Tom";
		String lastName = "Hanks";

		User aUser = bank.addUser(firstName, lastName, pin);
		// add a checking account for our user
		Account newAccount = new Account("Checking", aUser, bank);
		aUser.addAccount(newAccount);
		bank.addAccount(newAccount);

		User curUser;
		while (true) {

			// stay in login prompt
			curUser = ATM.mainMenuPrompt(bank, sc);

			// stay in main menu until user quits
			ATM.printUserMenu(curUser, sc);
		}

	}

	/**
	 * @param theBank
	 * @param sc
	 * @return
	 */
	@SuppressWarnings("null")
	public static User mainMenuPrompt(Bank theBank, Scanner sc) {
		// initiate
		String userID;
		String pin;
		User authUser;

		// prompt user for ID/pin combo until a correct one is reached
		do {
			System.out.printf("\n\nWelcome to %s\n\n", theBank.getName());
			System.out.print("Enter user ID: ");
			userID = sc.nextLine();
			System.out.print("Enter pin: ");
			pin = sc.nextLine();

			// try to get the user object corresponding to the ID/pin combo
			authUser = theBank.userLogin(userID, pin);
			if (authUser == null) {
				System.out.println("Incorrect user ID/pin combination. " + "Please try again.");
			}
		} while (authUser == null); // continue looping until successful login

		return authUser;
	}

	private static void printUserMenu(User theUser, Scanner sc) {

		// print a summary of user's accounts
		theUser.printAccountsSummary();

		// initiate
		int choice;

		// user menu
		do {
			System.out.printf("Welcome %s, what would you like to do?\n", theUser.getFirstName());
			System.out.println("  1) Show account transaction history");
			System.out.println("  2) Withdrawal");
			System.out.println("  3) Deposit");
			System.out.println("  4) Transfer");
			System.out.println("  5) Quit");
			System.out.println();
			System.out.println("Enter choice");
			choice = sc.nextInt();

			if (choice < 1 || choice > 5) {
				System.out.println("Invalid choice. Please choose 1-5");
			}
		} while (choice < 1 || choice > 5);
		// process the choice
		switch (choice) {

		case 1:
			ATM.showTransHistory(theUser, sc);
			break;
		case 2:
			ATM.withdrawalFunds(theUser, sc);
			break;
		case 3:
			ATM.depositFunds(theUser, sc);
			break;
		case 4:
			ATM.transferFunds(theUser, sc);
			break;
		case 5:
			// gobble up rest of previous input
			sc.nextLine();
			break;
		}
		// redisplay this menu unless the user wants to quit
		if (choice != 5) {
			ATM.printUserMenu(theUser, sc);
		}
	}

	/**
	 * Show the transaction history for an account
	 * 
	 * @param theUser the logged-in User object
	 * @param sc      the scanner object used for user input
	 */
	private static void showTransHistory(User theUser, Scanner sc) {

		int theAcct;

		// get account whose transaction history to look at

		do {
			System.out.printf("Enter the number (1-%d) of the account\n" + "whose transactions you want to see: ",
					theUser.numAccounts());
			theAcct = sc.nextInt() - 1;
			if (theAcct < 0 || theAcct >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
		} while (theAcct < 0 || theAcct >= theUser.numAccounts());

		// print the transaction history
		theUser.printAcctTransHistory(theAcct);
	}

	/**
	 * process transferring funds from one account to another
	 * 
	 * @param theUser the logged-in User object
	 * @param sc      the scanner object used for user input
	 */
	private static void transferFunds(User theUser, Scanner sc) {
		// initiate
		int fromAcct;
		int toAcct;
		double amount;
		double acctBal;
		String memo;

		// get the account to transfer from
		do {
			System.out.printf("Enter the number (1-%d) of the account\n" + "to transfer from: ", theUser.numAccounts());
			fromAcct = sc.nextInt() - 1;
			if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again");
			}
		} while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
		acctBal = theUser.getAcctBalance(fromAcct);

		// get the account to transfer to
		do {
			System.out.printf("Enter the number (1-%d) of the account\n" + "to transfer to: ", theUser.numAccounts());
			toAcct = sc.nextInt() - 1;
			if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again");
			}
		} while (toAcct < 0 || toAcct >= theUser.numAccounts());

		// get the amount to transfer
		do {
			System.out.printf("Enter the amount to transfer (max €%.02f): €", acctBal);
			amount = sc.nextDouble();
			if (amount < 0) {
				System.out.println("Amaount must be greater than zero.");
			} else if (amount > acctBal) {
				System.out.printf("Amount must not be greater than\n" + "balance of €%.02f.\n", acctBal);
			}
		} while (amount < 0 || amount > acctBal);

		// gobble up rest of previous input
		sc.nextLine();

		// get a memo

		System.out.println("Enter a memo: ");
		memo = sc.nextLine();

		// finally do the transfer
		theUser.addAccountTransaction(fromAcct, -1 * amount,
				String.format("Transfer to account %s (%s)", theUser.getAcctUUID(toAcct), memo));
		theUser.addAccountTransaction(toAcct, amount,
				String.format("Transfer from account %s (%s)", theUser.getAcctUUID(fromAcct), memo));

	}

	/**
	 * process a fund withdrawal from an account
	 * 
	 * @param theUser the logged in User object
	 * @param sc      the scanner object used for user input
	 */
	private static void withdrawalFunds(User theUser, Scanner sc) {

		// initiate
		int fromAcct;
		double amount;
		double acctBal;
		String memo;

		// get the account to withdraw from
		do {
			System.out.printf("Enter the number (1-%d) of the account\n" + "to withdraw from: ", theUser.numAccounts());
			fromAcct = sc.nextInt() - 1;
			if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again");
			}
		} while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
		acctBal = theUser.getAcctBalance(fromAcct);

		// get the amount to withdraw
		do {
			System.out.printf("Enter the amount to withdraw (max €%.02f): €", acctBal);
			amount = sc.nextDouble();
			if (amount < 0) {
				System.out.println("Amaount must be greater than zero.");
			} else if (amount > acctBal) {
				System.out.printf("Amount must not be greater than\n" + "balance of €%.02f.\n", acctBal);
			}
		} while (amount < 0 || amount > acctBal);

		// gobble up rest of previous input
		sc.nextLine();

		// get a memo

		System.out.println("Enter a memo: ");
		memo = sc.nextLine();

		// do the withdrawal
		theUser.addAccountTransaction(fromAcct, -1 * amount, memo);
	}

	private static void depositFunds(User theUser, Scanner sc) {

		// initiate
		int toAcct;
		double amount;
		double acctBal;
		String memo;

		// get the account to deposit to
		do {
			System.out.printf("Enter the number (1-%d) of the account\n" + "to deposit in: ", theUser.numAccounts());
			toAcct = sc.nextInt() - 1;
			if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again");
			}
		} while (toAcct < 0 || toAcct >= theUser.numAccounts());
		acctBal = theUser.getAcctBalance(toAcct);

		// get the amount to deposit
		do {
			System.out.printf("Enter the amount to deposit: €", acctBal);
			amount = sc.nextDouble();
			if (amount < 0) {
				System.out.println("Amaount must be greater than zero.");
			}
		} while (amount < 0);

		// gobble up rest of previous input
		sc.nextLine();

		// get a memo

		System.out.println("Enter a memo: ");
		memo = sc.nextLine();

		// do the deposit
		theUser.addAccountTransaction(toAcct, amount, memo);
	}
}

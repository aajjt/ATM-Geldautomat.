package main;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * @author alael
 *
 */
public class User {

	// First name of user

	private String firstName;

	// Last name of user

	private String lastName;

	// ID number of user

	private String uuid;

	// MD5 hash of user's pin number

	private byte pinHash[];

	// List of Accounts for this User

	public ArrayList<Account> accounts;

	/**
	 * Create a new user
	 * 
	 * @param firstName the user's first name
	 * @param lastName  the user's last name
	 * @param pin       the user's account pin number
	 * @param bank      the bank object that the user is a customer of
	 */
	public User(String firstName, String lastName, String pin, Bank bank) {
		// set user's name
		this.firstName = firstName;
		this.lastName = lastName;

		// store thepin's MD5 hash rather than the original value for security reasons

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");

			this.pinHash = md.digest(pin.getBytes());
		} catch (NoSuchAlgorithmException e) {
			System.err.println("error, caught NoSuchAlgorithmException");
			e.printStackTrace();
			System.exit(1);
		}

		// get a new, unique universal ID for the user

		this.uuid = bank.getNewUserUUID();

		// create empty list of accounts

		this.accounts = new ArrayList<Account>();

		// print log message

		System.out.printf("New user: %s %s with ID %s created. \n", firstName, lastName, this.uuid);

	}

	/**
	 * Add an account for the user
	 * 
	 * @param anAccount the account to add
	 */
	public void addAccount(Account anAccount) {
		this.accounts.add(anAccount);
	}

	/**
	 * @return UUID
	 */
	public String getUUID() {
		return this.uuid;
	}

	/**
	 * check whether a given pin matches the user pin or not
	 * 
	 * @param aPin the pin to be checked
	 * @return whether the pin is valid or not
	 */
	public boolean validatePin(String aPin) {

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");

			return MessageDigest.isEqual(md.digest(aPin.getBytes()), this.pinHash);
		} catch (NoSuchAlgorithmException e) {
			System.err.println("error, caught NoSuchAlgorithmException");
			e.printStackTrace();
			System.exit(1);
		}

		return false;
	}

	/**
	 * Return the user's first name.
	 * 
	 * @return the first name
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * Print summaries for the accounts of this user.
	 */
	public void printAccountsSummary() {
		System.out.printf("\n\n%s's accounts summary\n", this.firstName);
		for (int a = 0; a < this.accounts.size(); a++) {
			System.out.printf("  %d) %s", a + 1, this.accounts.get(a).getSummaryLine());
			System.out.println();
		}

	}

	/**
	 * Get the number of accounts of the user.
	 * 
	 * @return number of accounts
	 */
	public Integer numAccounts() {

		return this.accounts.size();
	}

	/**
	 * Print transaction history for a particular account.
	 * 
	 * @param acctIdx the index of the account to use.
	 */
	public void printAcctTransHistory(int acctIdx) {

		this.accounts.get(acctIdx).printTransHistory();
	}

	/**
	 * Get the balance of a particular account
	 * 
	 * @param acctIdx the index of the account to use
	 * @return the balance of the account
	 */
	public double getAcctBalance(int acctIdx) {
		return this.accounts.get(acctIdx).getBalance();
	}

	/**
	 * Get the UUID of a particular account
	 * 
	 * @param acctIdx the index of the account to use
	 * @return the UUIDof the account
	 */
	public String getAcctUUID(int acctIdx) {

		return this.accounts.get(acctIdx).getUUID();
	}

	/**
	 * Add a transaction to a particular account
	 * 
	 * @param acctIdx the index of the account
	 * @param amount  the amount of the transaction
	 * @param memo    the memo of the transaction
	 */
	public void addAccountTransaction(int acctIdx, double amount, String memo) {
		this.accounts.get(acctIdx).addTransaction(amount, memo);

	}
}

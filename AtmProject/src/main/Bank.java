package main;

import java.util.ArrayList;
import java.util.Random;

public class Bank {

	private String name;

	private ArrayList<User> users;

	private ArrayList<Account> accounts;

	/**
	 * create a new bank object with empty lists of users and accounts
	 * 
	 * @param name the name of the bank
	 */
	public Bank(String name) {
		this.name = name;
		this.users = new ArrayList<User>();
		this.accounts = new ArrayList<Account>();
	}

	/**
	 * Add an account for the bank
	 * 
	 * @param anAccount the account to add
	 */
	public void addAccount(Account anAccount) {
		this.accounts.add(anAccount);
	}

	/**
	 * @return
	 */
	public String getNewUserUUID() {

		Random rng = new Random();
		String uuid;
		int len = 6;
		boolean notUnique;

		// continue looping until we get a unique ID
		do {

			uuid = "";

			// generate ID
			for (int c = 0; c < len; c++) {
				uuid += ((Integer) rng.nextInt(10)).toString();
			}

			// compare ID to existing user IDs and check if it is unique

			notUnique = false;
			for (User u : this.users) {
				if (uuid.compareTo(u.getUUID()) == 0) {
					notUnique = true;
				}
				break;
			}
		} while (notUnique);

		// return new unique ID
		return uuid;
	}

	public String getNewAccountUUID() {

		Random rng = new Random();
		String uuid;
		int len = 10;
		boolean notUnique;

		// continue looping until we get a unique ID
		do {

			uuid = "";

			// generate ID
			for (int c = 0; c < len; c++) {
				uuid += ((Integer) rng.nextInt(10)).toString();
			}

			// compare ID to existing user IDs and check if it is unique

			notUnique = false;
			for (Account a : this.accounts) {
				if (uuid.compareTo(a.getUUID()) == 0) {
					notUnique = true;
				}
				break;
			}
		} while (notUnique);

		// return new unique ID
		return uuid;
	}

	/**
	 * @param firstName of new user
	 * @param lastName  of new user
	 * @param pin       of new user
	 * @return
	 */
	public User addUser(String firstName, String lastName, String pin) {

		// create new user object and add it to list of users in the bank
		User newUser = new User(firstName, lastName, pin, this);
		this.users.add(newUser);

		// create new saving account for the user
		Account newAccount = new Account("Savings", newUser, this);
		this.accounts.add(newAccount);
		newUser.addAccount(newAccount);

		return newUser;
	}

	public User userLogin(String userID, String pin) {
		// search through list of users
		for (User u : this.users) {

			// check user login input
			if (userID.compareTo(u.getUUID()) == 0 && u.validatePin(pin)) {
				return u;

			}
		}

		// if the login inputs are incorrect
		return null;
	}

	/**
	 * Get the name of the bank
	 * 
	 * @return the name of the bank
	 */
	public String getName() {
		return this.name;
	}

}

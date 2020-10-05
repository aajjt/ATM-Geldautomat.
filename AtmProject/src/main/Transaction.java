package main;

import java.util.Date;

public class Transaction {

	// Amount of transaction

	private double amount;

	// time and date of transaction

	private Date timestamp;

	// A memo for this transaction

	private String memo;

	// The account in which the transaction was performed

	private Account inAccount;

	/**
	 * create a new transaction
	 * 
	 * @param amount    transacted
	 * @param inAccount the account the transaction belongs to
	 */
	public Transaction(double amount, Account inAccount) {

		this.timestamp = new Date();
		this.amount = amount;
		this.memo = "";
		this.inAccount = inAccount;
	}

	/**
	 * create a new transaction
	 * 
	 * @param amount    transacted
	 * @param memo      for the transaction
	 * @param inAccount the account the transaction belongs to
	 */
	public Transaction(double amount, String memo, Account inAccount) {

		this(amount, inAccount);
		this.memo = memo;

	}

	/**
	 * Get the amount of transaction
	 * 
	 * @return amount of transaction
	 */
	public double getAmount() {
		return this.amount;
	}

	/**
	 * Get a string summarizing transaction
	 * 
	 * @return the summary string
	 */
	public String getSummaryLine() {
		if (this.amount >= 0) {
			return String.format("%s : €%.02f : %s", this.timestamp.toString(), this.amount, this.memo);
		} else {
			return String.format("%s : €(%.02f) : %s", this.timestamp.toString(), this.amount, this.memo);
		}

	}
}
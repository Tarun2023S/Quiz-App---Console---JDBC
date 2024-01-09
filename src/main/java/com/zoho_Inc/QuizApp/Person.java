package com.zoho_Inc.QuizApp;

public class Person {
	static int id = 1;
	private int personId;
	private String name;
	private String emailId;
	private String password;
	private int totalQuizTaken = 0;
	private int totalWins = 0;
	private int totalLosses = 0;
	
//	public Person(String name, String emailId, String password) {
//		personId = id++;
//		this.name = name;
//		this.emailId = emailId;
//		this.password = password;
//	}
	
	public Person(String name) {
		personId = id++;
		this.name = name;
	}

	public int getPersonId() {
		return this.personId;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getTotalQuizTaken() {
		return totalQuizTaken;
	}

	public void setTotalQuizTaken(int totalQuizTaken) {
		this.totalQuizTaken = totalQuizTaken;
	}

	public int getTotalWins() {
		return totalWins;
	}

	public void setTotalWins(int totalWins) {
		this.totalWins = totalWins;
	}

	public int getTotalLosses() {
		return totalLosses;
	}

	public void setTotalLosses(int totalLosses) {
		this.totalLosses = totalLosses;
	}
	
	public String toString() {
		return "Person name: "+name+"\nTotal Games Played: "+totalQuizTaken+"\nTotal Wins: "+totalWins+"\nTotal Losses: "+totalLosses;
	}
}

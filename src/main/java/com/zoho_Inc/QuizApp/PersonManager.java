package com.zoho_Inc.QuizApp;

import java.util.*;

public class PersonManager {
	Map<Integer, Person> personQuizManager;
	List<Person> personList;
	public PersonManager() {
		personQuizManager = new HashMap<>();
		personList = new LinkedList();
	}
	
	public boolean checkIfThePersonExists(String name) {
		for(Person p: personList) {
			if(p.getName().equals(name)) {
				System.out.println("A PERSON WITH THE SAME NAME EXISTS..");
				return true;
			}
		}
		return false;
	}
}

package com.zoho_Inc.QuizApp;
import java.sql.*;
import java.util.*;

public class App {
    private static String url = "jdbc:mysql://localhost:3306/";
    private static String userName = "root";
    private static String password = "root";

    private static int validChoices() {
        int choice = 0;
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("\t\n*** QUIZ APPLICATION MENU ***\n");
                System.out.println("\t1. Play Game");
                System.out.println("\t2. Play Game Category Wise");
                System.out.println("\t3. View All Questions");
                System.out.println("\t4. View All Options For a Question");
                System.out.println("\t6. EXIT\n");
                System.out.print("Enter your choice: ");
                choice = sc.nextInt();
                break; 
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid integer choice.\n");
                sc.next(); 
            }
        }
        return choice;
    }
    
    public static void main(String[] args) {
//        System.out.println("Hello World!");
        try (Connection connection = DriverManager.getConnection(url, userName, password);) {
            Class.forName("com.mysql.cj.jdbc.Driver");
//            System.out.println("Connection Established successfully..");

            if (!DBManager.databaseExists(connection)) {
            	DBManager.createDatabase(connection);
            } else {
//                System.out.println("Database already exists");
            }

            // CREATE NECESSARY TABLES
            DBManager.createNecessaryTables(connection);

            // POPULATE SOME DATA
//            populateData(categoryTableName, "category", connection);
//            populateData(optionTableName, "options", connection);
//            fetchDataFromTheTables(optionTableName, connection);
//            populateData(questionTableName, "question, category_id, answer_id", connection);
//            populateData(questionOptionTableName, "question_id, options_id", connection);
//            DBManager.populateData("dummy", "dummy", connection);
//            fetchDataFromTheTables(questionOptionTableName, connection);

            // QUIZ APPLICATION MENU
            Scanner sc = new Scanner(System.in);
            GameManager gm = new GameManager();
            PersonManager pm = new PersonManager();
            String personName = "Shami";
            Person p = new Person(personName);
            while (true) {
                int choice = validChoices();
//            	int choice = sc.nextInt();
//                System.out.println("Choice entered: "+choice);
                switch (choice) {
                    case 1:
                    	gm.playGame(connection, p, -1);
                    	break;
                    case 2:
                    	gm.fetchQuestionsCategoryWise(connection, p);
                        break;
                    case 3:
                    	DBManager.displayAllQuestions(connection);
                        break;
                    case 4:
                    	System.out.print("Enter question_id to display options: ");
                        int questionId = sc.nextInt();
                        DBManager.displayOptionsForQuestion(connection, questionId);
                        break;
                    case 6:
                        System.out.println("Exiting Quiz Application. Goodbye!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            }
            
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

}
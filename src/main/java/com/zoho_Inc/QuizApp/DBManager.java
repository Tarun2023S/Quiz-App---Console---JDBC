package com.zoho_Inc.QuizApp;

import java.sql.*;
import java.util.*;

public class DBManager {

    private static String databaseName = "quiz_app";

    private static String categoryTableName = "category";
    private static String categoryTableValues = "id INT PRIMARY KEY AUTO_INCREMENT, category VARCHAR(60)";
    
    private static String optionTableName = "options";
    private static String optionTableValues = "id INT PRIMARY KEY AUTO_INCREMENT, options VARCHAR(100)";
    
    private static String questionTableName = "question";
    private static String questionTableValues = "id INT PRIMARY KEY AUTO_INCREMENT, question VARCHAR(2000), category_id INT, answer_id INT, FOREIGN KEY(category_id) REFERENCES "+categoryTableName+"(id), FOREIGN KEY(answer_id) REFERENCES "+optionTableName+"(id)";
    
    private static String questionOptionTableName = "question_options";
    private static String questionOptionTableValues = "id INT PRIMARY KEY AUTO_INCREMENT, question_id INT, options_id INT, FOREIGN KEY(question_id) REFERENCES "+questionTableName+"(id), FOREIGN KEY(options_id) REFERENCES "+optionTableName+"(id)";

	 public static String getDatabaseName() {
		return databaseName;
	}

	public static void setDatabaseName(String databaseName) {
		DBManager.databaseName = databaseName;
	}

	public static String getCategoryTableName() {
		return categoryTableName;
	}

	public static void setCategoryTableName(String categoryTableName) {
		DBManager.categoryTableName = categoryTableName;
	}

	public static String getOptionTableName() {
		return optionTableName;
	}

	public static void setOptionTableName(String optionTableName) {
		DBManager.optionTableName = optionTableName;
	}

	public static String getQuestionTableName() {
		return questionTableName;
	}

	public static void setQuestionTableName(String questionTableName) {
		DBManager.questionTableName = questionTableName;
	}

	public static String getQuestionOptionTableName() {
		return questionOptionTableName;
	}

	public static void setQuestionOptionTableName(String questionOptionTableName) {
		DBManager.questionOptionTableName = questionOptionTableName;
	}

	public static void displayAllQuestions(Connection connection) {
	     String fetchDataQuery = "SELECT id, question, category_id FROM " + databaseName + "." + questionTableName + ";";
	     try (PreparedStatement ps1 = connection.prepareStatement(fetchDataQuery)) {
	            ResultSet resultSet = ps1.executeQuery();
	            ResultSetMetaData metaData = resultSet.getMetaData();

	            int columnCount = metaData.getColumnCount();
	            System.out.println("\n");
	            while (resultSet.next()) {
	                for (int i = 1; i <= columnCount; i++) {
	                    String columnName = metaData.getColumnName(i);
	                    String columnType = metaData.getColumnTypeName(i);

	                    // Display column name and data based on the column type
	                    if ("INT".equalsIgnoreCase(columnType) || "INTEGER".equalsIgnoreCase(columnType)) {
	                        System.out.println(columnName + ": " + resultSet.getInt(i));
	                    } else if ("VARCHAR".equalsIgnoreCase(columnType) || "CHAR".equalsIgnoreCase(columnType)) {
	                        System.out.println(columnName + ": " + resultSet.getString(i));
	                    } else if ("DATE".equalsIgnoreCase(columnType)) {
	                        System.out.println(columnName + ": " + resultSet.getDate(i));
	                    } else {
	                        System.out.println(columnName + ": " + resultSet.getObject(i));
	                    }
	                }
	                System.out.println(); // Separate each question's details
	            }
	        } catch (SQLException e) {
	            System.out.println("Exception occurred: " + e.getMessage());
	        }
	    }

	    public static void displayOptionsForQuestion(Connection connection, int questionId) {
	        String fetchDataQuery = "SELECT options.options FROM " + databaseName + "." + optionTableName +
	                                " INNER JOIN " + databaseName + "." + questionOptionTableName +
	                                " ON options.id = question_options.options_id WHERE question_options.question_id = ?;";

	        try (PreparedStatement ps1 = connection.prepareStatement(fetchDataQuery)) {
	            ps1.setInt(1, questionId);

	            ResultSet resultSet = ps1.executeQuery();

	            System.out.println("\nOptions for Question " + questionId + ":");
	            while (resultSet.next()) {
	                System.out.println(resultSet.getString(1));
	            }
	        } catch (SQLException e) {
	            System.out.println("Exception occurred: " + e.getMessage());
	        }
	    }
	    
	    static boolean databaseExists(Connection connection) {
	        try {
	            Statement statement = connection.createStatement();
	            String checkDatabaseExistsQuery = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '" + databaseName + "'";
	            return statement.executeQuery(checkDatabaseExistsQuery).next();
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	    static void createDatabase(Connection connection) {
	        try {
	            Statement statement = connection.createStatement();
	            String queryToCreateDB = "CREATE DATABASE " + databaseName;
	            statement.executeUpdate(queryToCreateDB);
	            System.out.println("Database created successfully");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	      
	    private static void createTableQuery(String tableName, String tableValues, Connection connection) {
	        String queryToCreateTable = "CREATE TABLE IF NOT EXISTS " + databaseName + ".`" + tableName + "`(" + tableValues + ");";
//	        System.out.println("Create table query: " + queryToCreateTable);
	        try (PreparedStatement preparedStatement = connection.prepareStatement(queryToCreateTable)) {
	            int modifiedRows = preparedStatement.executeUpdate();
//	            System.out.println("Modified no. of rows: " + modifiedRows);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    public static void createNecessaryTables(Connection connection) {
	    	//CREATING ALL THE NECESSARY TABLES
	        createTableQuery(categoryTableName, categoryTableValues, connection);
	        createTableQuery(optionTableName, optionTableValues, connection);
	        createTableQuery(questionTableName, questionTableValues, connection);
	        createTableQuery(questionOptionTableName, questionOptionTableValues, connection);
	    }
	    
	    
	    public static void populateData(String tableName, String columnNames, Connection connection) {
	        String insertQueryForTables = "INSERT INTO " + databaseName + ".`" + tableName + "`(" + columnNames + ") VALUES(";
	        String[] columnNamesArray = columnNames.split(",");

	        for (int i = 0; i < columnNamesArray.length; i++) {
	            insertQueryForTables += "?,";
	        }

	        // Remove the trailing comma and close the VALUES parentheses
	        insertQueryForTables = insertQueryForTables.substring(0, insertQueryForTables.length() - 1) + ")";

	        System.out.println("Insert Query: " + insertQueryForTables);

	        try (Scanner sc = new Scanner(System.in);
	             PreparedStatement ps1 = connection.prepareStatement(insertQueryForTables)) {

	            System.out.println("colArr: " + Arrays.toString(columnNamesArray));
	            System.out.println("Enter the number of times you want to insert values: ");
	            int n = sc.nextInt();
	            sc.nextLine();

	            while (n > 0) {
	                for (int i = 0; i < columnNamesArray.length; i++) {
	                    System.out.println("Enter value for " + columnNamesArray[i] + ": ");
	                    String s = sc.nextLine();
	                    ps1.setObject(i + 1, s); // Use i + 1 as parameter index
	                }
	                ps1.addBatch();
	                n--;
	            }

	            // Execute the batch and clear it
	            int[] res = ps1.executeBatch();
	            ps1.clearBatch();

	            System.out.println("Rows modified: " + Arrays.toString(res));
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } catch (Exception e) {
	            System.out.println("Error occurred: " + e.getMessage());
	        }
	    }

	    public static void displayRandomQuestions(Connection connection, int numQuestions) {
	        String fetchDataQuery = "SELECT * FROM " + databaseName + "." + questionTableName + " ORDER BY RAND() LIMIT ?;";
	        try (PreparedStatement ps1 = connection.prepareStatement(fetchDataQuery)) {
	            ps1.setInt(1, numQuestions);

	            ResultSet resultSet = ps1.executeQuery();
	            ResultSetMetaData metaData = resultSet.getMetaData();

	            int columnCount = metaData.getColumnCount();

	            while (resultSet.next()) {
	                for (int i = 1; i <= columnCount; i++) {
	                    String columnName = metaData.getColumnName(i);
	                    String columnType = metaData.getColumnTypeName(i);

	                    // Display column name and data based on the column type
	                    if ("INT".equals(columnType) || "INTEGER".equals(columnType)) {
	                        System.out.println(columnName + ": " + resultSet.getInt(i));
	                    } else if ("VARCHAR".equals(columnType) || "CHAR".equals(columnType)) {
	                        System.out.println(columnName + ": " + resultSet.getString(i));
	                    } else if ("DATE".equals(columnType)) {
	                        System.out.println(columnName + ": " + resultSet.getDate(i));
	                    } else {
	                        System.out.println(columnName + ": " + resultSet.getObject(i));
	                    }
	                }
	                System.out.println(); // Separate each question's details
	            }
	        } catch (SQLException e) {
	            System.out.println("Exception occurred: " + e.getMessage());
	        }
	    }
	    
	    

	    public static void fetchDataFromTheTables(String tableName, Connection connection) {
	         String fetchDataQuery = "SELECT * FROM " + databaseName + "." + tableName + ";";
	            try (PreparedStatement ps1 = connection.prepareStatement(fetchDataQuery)) {
	                ResultSet resultSet = ps1.executeQuery();
	                ResultSetMetaData metaData = resultSet.getMetaData();

	                int columnCount = metaData.getColumnCount();

	                while (resultSet.next()) {
	                    for (int i = 1; i <= columnCount; i++) {
	                        String columnName = metaData.getColumnName(i);
	                        String columnType = metaData.getColumnTypeName(i);

	                        // Display column name and data based on the column type
	                        if ("INT".equals(columnType) || "INTEGER".equals(columnType)) {
	                            System.out.print(columnName + ": " + resultSet.getInt(i)+", ");
	                        } else if ("VARCHAR".equals(columnType) || "CHAR".equals(columnType)) {
	                            System.out.print(columnName + ": " + resultSet.getString(i)+", ");
	                        } else if ("DATE".equals(columnType)) {
	                            System.out.print(columnName + ": " + resultSet.getDate(i)+", ");
	                        } else {
	                            System.out.print(columnName + ": " + resultSet.getObject(i)+", ");
	                        }
	                    }
	                    System.out.println();
	                }
	            } catch (SQLException e) {
	                System.out.println("Exception occurred: " + e.getMessage());
	            }
	        }
	    
	 // Method to choose a category and return its ID
	    public static int chooseCategory(Connection connection) {
	        try {
	            String fetchDataQuery = "SELECT * FROM " + databaseName + "." + categoryTableName + ";";
	            try (PreparedStatement ps1 = connection.prepareStatement(fetchDataQuery)) {
	                ResultSet resultSet = ps1.executeQuery();
	                int categoryCount = 0;

	                System.out.println("Choose a category:\n");
	                while (resultSet.next()) {
	                    categoryCount++;
	                    String categoryName = resultSet.getString("category");
	                    System.out.println(categoryCount + ". " + categoryName);
	                }
	                
	                Scanner sc = new Scanner(System.in);
	                int selectedCategory = 0;
	                boolean validInput = false;

	                while (!validInput) {
	                    System.out.print("\nEnter the number corresponding to the category: ");
	                    try {
	                        selectedCategory = sc.nextInt();
	                        if (selectedCategory > 0 && selectedCategory <= categoryCount) {
	                            validInput = true;
	                        } else {
	                            System.out.println("Please enter a valid category number.");
	                        }
	                    } catch (InputMismatchException e) {
	                        System.out.println("Invalid input. Please enter a valid number.");
	                        sc.next(); // consume the invalid input to avoid an infinite loop
	                    }
	                }

	                // Fetch the category ID based on the selected category
	                fetchDataQuery = "SELECT id FROM " + databaseName + "." + categoryTableName + " LIMIT ?, 1;";
	                try (PreparedStatement ps2 = connection.prepareStatement(fetchDataQuery)) {
	                    ps2.setInt(1, selectedCategory - 1); // Adjust for 0-based index
	                    ResultSet resultSet2 = ps2.executeQuery();
	                    resultSet2.next();
	                    return resultSet2.getInt("id");
	                }
	            }
	        } catch (SQLException e) {
	            System.out.println("Exception occurred: " + e.getMessage());
	            return -1; // Return -1 to indicate an error
	        }
	    }

}

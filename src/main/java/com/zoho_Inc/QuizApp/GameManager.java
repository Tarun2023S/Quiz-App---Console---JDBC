package com.zoho_Inc.QuizApp;
import java.sql.*;
import java.util.*;

public class GameManager {
    private static DBManager dbm;
    private static String databaseName;
    private static String optionTableName;
    private static String questionTableName;
    private static String questionOptionsTableName;

    static {
        dbm = new DBManager();
        databaseName = dbm.getDatabaseName();
        optionTableName = dbm.getOptionTableName();
        questionTableName = dbm.getQuestionTableName();
        questionOptionsTableName = dbm.getQuestionOptionTableName();
    }

    List<Person> personList;
    Map<Integer, Person> personQuizManager;

    private static class Question {
        private final int id;
        private final String questionText;
        private final int categoryId;
        private final int answerId;

        public Question(int id, String questionText, int categoryId, int answerId) {
            this.id = id;
            this.questionText = questionText;
            this.categoryId = categoryId;
            this.answerId = answerId;
        }

        public int getId() {
            return id;
        }

        public String getQuestionText() {
            return questionText;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public int getAnswerId() {
            return answerId;
        }

        public String toString() {
            return "id: " + id + "\nquestionText: " + questionText + "\ncategory: " + categoryId + "\nanswerId: " + answerId;
        }
    }

    public GameManager() {
        personQuizManager = new HashMap<>();
        personList = new LinkedList<>();
    }

//    void playGame(Connection connection, Person p, int categoryId) {
//        try {
//        	Scanner sc = new Scanner(System.in);
//        	int randomFetchNumber = checkIfValidInput();
//        	// ..
//        	/*Thread t1 = null;
//        	Runnable r = new Runnable() {
//    			public void run() {
//    				try {
//						startTimer();
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
////						System.out.println(e.getMessage());
//						isTimeLimitExceeded = true;
////						return;
//					}
//    			}
//    		};
//        	t1 = new Thread(r);
////        	t1.start(); 
//        	// .. */
//            List<Question> questionList = fetchRandomQuestions(connection, databaseName, questionTableName, randomFetchNumber, categoryId);
//            if(questionList.size()==0) {
//            	System.out.println("There are no questions available..");
//            	return;
//            }
//            if(categoryId!=-1 && randomFetchNumber > questionList.size()) {
//            	System.out.println("There are only "+questionList.size()+" questions available for this category..");
//            }
//            for (int i = 0; i < questionList.size(); i++) {
////            	t1.start();
//                displayQuestionOptions(questionList.get(i), connection, p, i + 1);
//                
//            }
//
//            int totalMatchesCount = p.getTotalQuizTaken();
//            p.setTotalQuizTaken(totalMatchesCount + 1);
//
//            System.out.println("\t**GAME OVER**\n");
//            
//            float fractionScore = 100.0f / randomFetchNumber;  // Ensure one of the operands is a float to get a floating-point result
//            float percentageScore = fractionScore * p.getTotalWins();
//
////            System.out.println("YOUR SCORE IS: " + p.getTotalWins() + " / " + randomFetchNumber + " (" + percentageScore + " %)");
//            System.out.printf("YOUR SCORE IS: %d / %d (%.0f%%)\n", p.getTotalWins(), randomFetchNumber, percentageScore);
//
//            //Once the game over, I'm setting the player experience to 0
//            p.setTotalWins(0);
//            
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
////            System.out.println("Error occurred: " + e.getMessage());
//        }
//    }
    
    //...
    private static int thresholdDrawTimeLimit = 10;
    private static int currentSecond = 0;
    private static boolean isTimeLimitExceeded = false;
    protected void startTimer() throws InterruptedException {
//    	int currentSecond = 0;
    	for (int seconds = 0; seconds <= thresholdDrawTimeLimit; seconds++) {
            System.out.println("Timer: " + seconds + " seconds");
            currentSecond = seconds;
            if(isTimeLimitExceeded==true) {
            	Thread.currentThread().interrupt();
            	isTimeLimitExceeded = false;
            	System.out.println("TLE Get out: "+isTimeLimitExceeded);
            	return;
            }
            if(currentSecond >= thresholdDrawTimeLimit) {
            	Thread.currentThread().interrupt();
            	isTimeLimitExceeded = false;
            	throw new InterruptedException();
//            	throw new TimeLimitExceededException("Time Limit Exceeded from Start Timer");
            }
            try {
                // Sleep for 1 second (1000 milliseconds)
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            	System.out.println("Sllepp interrupetd");
            	return;
//                e.printStackTrace();
            }
        }
    	
//    	return currentSecond;
    }
    
    //...
    void playGame(Connection connection, Person p, int categoryId) {
        try {
            Scanner sc = new Scanner(System.in);
            int randomFetchNumber = checkIfValidInput();

            List<Question> questionList = fetchRandomQuestions(connection, databaseName, questionTableName, randomFetchNumber, categoryId);
            if (questionList.size() == 0) {
                System.out.println("There are no questions available..");
                return;
            }
            if (categoryId != -1 && randomFetchNumber > questionList.size()) {
                System.out.println("There are only " + questionList.size() + " questions available for this category..");
            }

            int currentIndex = 0;
            while (currentIndex < questionList.size()) {
                displayQuestionOptions(questionList.get(currentIndex), connection, p, currentIndex + 1);

                System.out.println("\nSelect an option:");
                System.out.println("1. Move to the next question");
                System.out.println("2. Move to the previous question");
                System.out.println("3. Exit game");

                int option = sc.nextInt();
                switch (option) {
                    case 1:
                        currentIndex++;
                        break;
                    case 2:
                        if (currentIndex > 0) {
                            currentIndex--;
                        } else {
                            System.out.println("You are at the first question.");
                        }
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }

            System.out.println("\t**GAME OVER**\n");

            float fractionScore = 100.0f / randomFetchNumber;
            float percentageScore = fractionScore * p.getTotalWins();

            System.out.printf("YOUR SCORE IS: %d / %d (%.0f%%)\n", p.getTotalWins(), randomFetchNumber, percentageScore);

            p.setTotalWins(0);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    // Method to fetch four random questions
    private static List<Question> fetchRandomQuestions(Connection connection, String databaseName, String questionTableName, int count, int category_id) throws SQLException {
        List<Question> questions = new ArrayList<>();
        String fetchDataQuery = "SELECT * FROM " + databaseName + "." + questionTableName;
        if(category_id!=-1) {
        	fetchDataQuery += " WHERE category_id = ?";
        }
        fetchDataQuery += " ORDER BY RAND() LIMIT ?";
        try (PreparedStatement ps1 = connection.prepareStatement(fetchDataQuery)) {
        	if(category_id!=-1) {
        		ps1.setInt(1, category_id);
        		ps1.setInt(2, count);
        	}
        	else {
        		ps1.setInt(1, count);
        	}
            
            ResultSet resultSet = ps1.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String questionText = resultSet.getString("question");
                int categoryId = resultSet.getInt("category_id");
                int answerId = resultSet.getInt("answer_id");
                Question question = new Question(id, questionText, categoryId, answerId);
                questions.add(question);
            }
        }
        return questions;
    }

    // Method to display options for a question
    private static void displayQuestionOptions(Question question, Connection connection, Person p, int questionNumber) throws SQLException {
        String fetchDataQuery = "SELECT op.id, op.options FROM " + databaseName + "." + questionOptionsTableName + " qo JOIN " + databaseName + "." + optionTableName + " op ON qo.options_id = op.id WHERE qo.question_id = ? ";
        fetchDataQuery += "ORDER BY RAND();";

        try (PreparedStatement ps1 = connection.prepareStatement(fetchDataQuery)) {
            ps1.setInt(1, question.getId());
            ResultSet resultSet = ps1.executeQuery();
            Map<Character, Integer> optionChoiceMap = new HashMap();
            char optionChoice = 'a';
            System.out.println("\nQUESTION " + questionNumber + ":    " + question.getQuestionText() + "\n");

            while (resultSet.next()) {
                int optionId = resultSet.getInt("op.id");
                String optionText = resultSet.getString("options");
                System.out.print(optionChoice + ". " + optionText + "\t");
                optionChoiceMap.put(optionChoice, optionId);
                optionChoice += 1;
            }

            Scanner sc = new Scanner(System.in);
            System.out.println();

            char choice;
            boolean validChoice = false;
//            try {
//				if(currentSecond==thresholdDrawTimeLimit ||isTimeLimitExceeded) {
//					System.out.println("The time is over for this question..");
//					isTimeLimitExceeded = false;
//					return;
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				System.out.println("Error: msg: "+e.getMessage());
//				e.printStackTrace();
//			}
            // Continue prompting until a valid choice (a/b/c/d) is entered
            System.out.println("\nEnter your choice (a/b/c/d)...");
            while (!validChoice) {
                choice = sc.next().toLowerCase().charAt(0);

                if (choice >= 'a' && choice <= 'd') {
                    validChoice = true;
                    int totalWins = p.getTotalWins();
                    int totalLosses = p.getTotalLosses();

                    if (validateAnswer(question, choice, connection, optionChoiceMap)) {
                        System.out.println("It's the correct answer!");
                        if (p.getTotalQuizTaken() == 0) {
                            p.setTotalWins(1);
                            p.setTotalQuizTaken(1);
                        } else {
                            p.setTotalWins(totalWins + 1);
                        }
                    } else {
                        System.out.println("WRONG ANSWER!");
                        String correctAnswer = getOptionNameById(question.answerId, connection);
                        System.out.println("The correct answer is: " + correctAnswer);
                        if (p.getTotalQuizTaken() == 0) {
                            p.setTotalLosses(1);
                            p.setTotalQuizTaken(1);
                        } else {
                            p.setTotalLosses(totalLosses + 1);
                        }
                    }
                } else {
                    System.out.println("Please enter a valid choice (a/b/c/d).");
                }
            }
        }
    }

    // Method to validate the user's answer
    private static boolean validateAnswer(Question question, char userChoice, Connection connection, Map<Character, Integer> optionChoiceMap) throws SQLException {
        int actualChoice = optionChoiceMap.get(userChoice);
        String fetchDataQuery = "SELECT * FROM " + databaseName + "." + questionTableName + " WHERE id = ? AND answer_id = ?;";
        try (PreparedStatement ps1 = connection.prepareStatement(fetchDataQuery)) {
            ps1.setInt(1, question.getId());
            ps1.setInt(2, actualChoice);
            ResultSet resultSet = ps1.executeQuery();
            return resultSet.next();
        }
    }

    private static String getOptionNameById(int options_id, Connection connection) {
        String query = "SELECT options FROM " + databaseName + "." + optionTableName + " WHERE id = ?;";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, options_id);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return resultSet.getString("options");
        } catch (Exception e) {
            System.out.println("An Error occurred.." + e.getMessage());
        }
        return "";
    }

    void fetchQuestionsCategoryWise(Connection connection, Person p) {
//      DBManager.fetchDataFromTheTables(DBManager.getCategoryTableName(), connection);
      int id = DBManager.chooseCategory(connection);
      System.out.println("Enetr the id: "+id);
      playGame(connection, p, id);
    }
//    private static char getChoiceForOption(String correctAnswer, Map<Character, Integer> optionChoiceMap) {
//        for (Map.Entry<Character, Integer> entry : optionChoiceMap.entrySet()) {
//            if (getOptionNameById(entry.getValue()) != null && getOptionNameById(entry.getValue()).equals(correctAnswer)) {
//                return entry.getKey();
//            }
//        }
//        return ' ';
//    }

    private static int checkIfValidInput() {
    	int randomFetchNumber = 0;
    	Scanner sc = new Scanner(System.in);
    	boolean validInput = false;

    	while (!validInput) {
    	    try {
    	        System.out.println("\nEnter the number of questions to fetch: ");
    	        randomFetchNumber = sc.nextInt();

    	        // Add validation for a positive number
    	        if (randomFetchNumber > 0) {
    	            validInput = true;
    	        } else {
    	            System.out.println("Please enter a positive number..\n");
    	        }
    	    } catch (InputMismatchException e) {
    	        System.out.println("Invalid input. Please enter a valid number..\n");
    	        sc.next(); // consume the invalid input to avoid an infinite loop
    	    }
    	}
    	
    	return randomFetchNumber;
    }
    private static int validChoices() {
        char choice = 'a';
        Scanner sc = new Scanner(System.in);

        while (true) {
            try {
                System.out.print("Enter your choice: ");
                choice = sc.next().charAt(0);
                break;
            } catch (InputMismatchException e) {
                System.out.println("PLEASE ENTER A VALID OPTION CHOICE..\n");
                sc.next();
            }
        }

        return choice;
    }
}
 
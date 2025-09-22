package quiz;
 
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
 
public class MathQuizGame {
    private static final String[] OPERATORS = {"+", "-", "*", "/"};
    private Scanner scanner;
    private int score;
    private boolean isAnswerReceived;
    private int timeLimitSeconds = 10;
    private boolean timeUp;
    private List<String> summaryList;
 
    public MathQuizGame() {
        scanner = new Scanner(System.in);
        score = 0;
        timeUp = false;
        summaryList = new ArrayList<>();
    }
    
 
    public void startGame() {
        System.out.println("Welcome to the Math Quiz Game!");
        System.out.print("How many questions would you like to answer? ");
        int numQuestions = scanner.nextInt();
        scanner.nextLine();  // Consume the newline character
 
        for (int i = 0; i < numQuestions && !timeUp; i++) {
            MathQuestion<Integer> question = generateRandomQuestion();
            System.out.println(question);
 
            // Start the timer in a separate thread
            Thread timerThread = new Thread(this::runTimer);
            timerThread.start();
 
            double userAnswer = askQuestion();  // User's answer
 
            timerThread.interrupt();  // Stop the timer when the answer is received or time's up
 
            if (timeUp) {
                break;  // If time is up, break the loop and end the game
            }
 
            if (isAnswerReceived) {
                if (question.checkAnswer(userAnswer)) {
                    System.out.println("Correct!");
                    score++;
                } else {
                    System.out.println("Wrong!");
                }
            }
 
            // Add to summary list (correct answer for this question)
            summaryList.add(question + " Correct answer: " + question.getCorrectAnswer());
        }
 
        // End the game and display the summary
        System.out.println("Game Over!");
        System.out.println("Your final score is: " + score);
        showSummary();
    }
 
    private MathQuestion<Integer> generateRandomQuestion() {
        Random random = new Random();
 
        int number1 = random.nextInt(100) + 1;  // Generate random number between 1 and 100
        int number2 = random.nextInt(100) + 1;
        String operator = OPERATORS[random.nextInt(OPERATORS.length)];  // Pick a random operator
 
        return new MathQuestion<>(number1, number2, operator);
    }
 
    private double askQuestion() {
        isAnswerReceived = false;
 
        System.out.print("Your answer: ");
        String input = scanner.nextLine();  // Get user's input
 
        if (!input.isEmpty()) {
            isAnswerReceived = true;
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                return 0;  // Invalid input
            }
        } else {
            return 0;  // If no input provided
        }
    }
 
    private void runTimer() {
        try {
            Thread.sleep(timeLimitSeconds * 1000);  // Wait for the time limit
            if (!isAnswerReceived) {
                System.out.println("\nTime's up!");
                timeUp = true;  // Mark the time-up flag
            }
        } catch (InterruptedException e) {
            // Do nothing, as the timer was interrupted by a user answer
        }
    }
 
    private void showSummary() {
        System.out.println("\nSummary of answers:");
        for (String summary : summaryList) {
            System.out.println(summary);  // Print the summary for each question
        }
    }
    
 
    public static void main(String[] args) {
        MathQuizGame game = new MathQuizGame();
        game.startGame();
    }
}
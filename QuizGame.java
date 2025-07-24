// This file should be renamed to QuizGame.java for proper Java class naming.
// To do this, rename the file in your file explorer or IDE from 'java' to 'QuizGame.java'.
// The code is already using 'public class QuizGame', so this will match the filename and avoid compilation errors.
// No code changes are needed, just the filename update.
import java.util.*;
import java.io.*;

class Question {
    String question;
    String[] options;
    int correctOption;

    public Question(String question, String[] options, int correctOption) {
        this.question = question;
        this.options = options;
        this.correctOption = correctOption;
    }
}

public class QuizGame {
    static final String ANSI_RESET = "\u001B[0m";
    static final String ANSI_GREEN = "\u001B[32m";
    static final String ANSI_RED = "\u001B[31m";
    static final String HIGH_SCORE_FILE = "highscores.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String playerName;
        System.out.print("Enter your name: ");
        playerName = scanner.nextLine();

        boolean playAgain = true;
        while (playAgain) {
            List<Question> questions = new ArrayList<>();
            questions.add(new Question(
                "What is the capital of France?",
                new String[]{"1) Berlin", "2) Paris", "3) Rome", "4) Madrid"},
                2
            ));
            questions.add(new Question(
                "Which planet is known as the Red Planet?",
                new String[]{"1) Earth", "2) Venus", "3) Mars", "4) Jupiter"},
                3
            ));
            questions.add(new Question(
                "Who wrote 'Romeo and Juliet'?",
                new String[]{"1) Charles Dickens", "2) William Shakespeare", "3) Mark Twain", "4) Jane Austen"},
                2
            ));

            Collections.shuffle(questions); // Randomize question order

            int score = 0;
            int correctAnswers = 0;
            long fastestTime = Long.MAX_VALUE;
            long totalTime = 0;

            for (int i = 0; i < questions.size(); i++) {
                Question q = questions.get(i);
                System.out.println("Question " + (i + 1) + ": " + q.question);
                for (String option : q.options) {
                    System.out.println(option);
                }
                System.out.print("You have 10 seconds. Your answer (enter option number): ");

                long startTime = System.currentTimeMillis();
                int answer = -1;
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    public void run() {
                        System.out.println("\nTime's up!");
                        System.exit(0);
                    }
                };
                timer.schedule(task, 10000);
                try {
                    answer = scanner.nextInt();
                } catch (InputMismatchException e) {
                    scanner.next();
                }
                timer.cancel();
                long elapsed = System.currentTimeMillis() - startTime;
                totalTime += elapsed;
                if (elapsed < fastestTime) fastestTime = elapsed;

                if (answer == q.correctOption) {
                    System.out.println(ANSI_GREEN + "Correct!" + ANSI_RESET + "\n");
                    score++;
                    correctAnswers++;
                } else {
                    System.out.println(ANSI_RED + "Wrong! The correct answer was option " + q.correctOption + ANSI_RESET + "\n");
                }
            }
            double accuracy = (double) correctAnswers / questions.size() * 100;
            System.out.println("Quiz Over! Your score: " + score + "/" + questions.size());
            System.out.printf("Accuracy: %.2f%%\n", accuracy);
            System.out.println("Fastest answer: " + (fastestTime / 1000.0) + " seconds");

            saveHighScore(playerName, score);
            showHighScores();

            System.out.print("Play again? (y/n): ");
            String again = scanner.next();
            playAgain = again.equalsIgnoreCase("y");
            scanner.nextLine(); // clear buffer
        }
        scanner.close();
    }

    static void saveHighScore(String name, int score) {
        try (FileWriter fw = new FileWriter(HIGH_SCORE_FILE, true)) {
            fw.write(name + ": " + score + "\n");
        } catch (IOException e) {
            System.out.println("Could not save high score.");
        }
    }

    static void showHighScores() {
        System.out.println("High Scores:");
        try (BufferedReader br = new BufferedReader(new FileReader(HIGH_SCORE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("No high scores yet.");
        }
    }
}

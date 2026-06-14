package practice_13;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static final String FILE_NAME = "document.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("\n=== Меню редактора ===");
            System.out.println("1. Додати рядки до файлу");
            System.out.println("2. Прочитати увесь вміст файлу");
            System.out.println("3. Прочитати діапазон рядків");
            System.out.println("4. Вставити рядок у задану позицію");
            System.out.println("5. Вийти");
            System.out.print("Оберіть дію: ");

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                writeMultipleLines(scanner);
            } else if (choice.equals("2")) {
                readAllLines();
            } else if (choice.equals("3")) {
                readLineRange(scanner);
            } else if (choice.equals("4")) {
                insertLineAtPosition(scanner);
            } else if (choice.equals("5")) {
                isRunning = false;
            } else {
                System.out.println("Невідома команда!");
            }
        }
        scanner.close();
    }

    private static int getLineCount() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return 0;
        }
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
        return count;
    }

    private static void writeMultipleLines(Scanner scanner) {
        int currentLine = getLineCount() + 1;
        System.out.println("Вводьте текст (для завершення введіть порожній рядок):");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            while (true) {
                System.out.print(currentLine + ": ");
                String line = scanner.nextLine();

                if (line.isEmpty()) {
                    break;
                }

                writer.write(line);
                writer.newLine();
                currentLine++;
            }
        } catch (IOException e) {
            System.out.println("Помилка запису: " + e.getMessage());
        }
    }

    private static void readAllLines() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("Файл порожній або не існує.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                System.out.println(lineNumber + ": " + line);
                lineNumber++;
            }
        } catch (IOException e) {
            System.out.println("Помилка читання: " + e.getMessage());
        }
    }

    private static void readLineRange(Scanner scanner) {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("Файл порожній або не існує.");
            return;
        }

        System.out.print("Введіть початковий рядок: ");
        int startLine = 0;
        int endLine = 0;

        try {
            startLine = Integer.parseInt(scanner.nextLine());
            System.out.print("Введіть кінцевий рядок: ");
            endLine = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Помилка: потрібно ввести число.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                if (lineNumber >= startLine && lineNumber <= endLine) {
                    System.out.println(lineNumber + ": " + line);
                }
                lineNumber++;
            }
        } catch (IOException e) {
            System.out.println("Помилка читання: " + e.getMessage());
        }
    }

    private static void insertLineAtPosition(Scanner scanner) {
        File file = new File(FILE_NAME);
        int totalLines = getLineCount();

        System.out.print("Введіть номер рядка для вставки: ");
        int targetLine = 0;
        try {
            targetLine = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Помилка: потрібно ввести число.");
            return;
        }

        System.out.print("Введіть текст для цього рядка: ");
        String newText = scanner.nextLine();

        String[] allLines = new String[totalLines];
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                for (int i = 0; i < totalLines; i++) {
                    allLines[i] = reader.readLine();
                }
            } catch (IOException e) {
                System.out.println("Помилка читання: " + e.getMessage());
                return;
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, false))) {
            int writtenLines = 1;
            for (int i = 0; i < totalLines; i++) {
                if (writtenLines == targetLine) {
                    writer.write(newText);
                    writer.newLine();
                    writtenLines++;
                }
                writer.write(allLines[i]);
                writer.newLine();
                writtenLines++;
            }

            if (targetLine >= writtenLines) {
                writer.write(newText);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Помилка запису: " + e.getMessage());
        }
    }
}
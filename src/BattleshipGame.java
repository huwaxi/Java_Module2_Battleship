import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class BattleshipGame {
    private static final int BOARD_SIZE = 8;
    private static final int NUM_SHIPS = 3;
    private static final int SHIP1_ROW = 2;
    private static final int SHIP1_COL = 2;
    private static final int SHIP2_ROW = 5;
    private static final int SHIP2_COL = 4;
    private static final int SHIP3_ROW = 0;
    private static final int SHIP3_COL = 0;

    private char[][] board;
    private int numShips;
    private long startTime;
    private List<Long> gameTimes;

    public static void main(String[] args) {
        BattleshipGame game = new BattleshipGame();
        game.play();
    }

    private void displayTopGameTimes() {
        System.out.println("Топ времен игры:");

        if (gameTimes.isEmpty()) {
            System.out.println("Нет записей о времени игры.");
            return;
        }

        // Сортировка времен игры по возрастанию
        Collections.sort(gameTimes);

        int rank = 1;
        for (long time : gameTimes) {
            System.out.println("Место " + rank + ": " + time + " секунд");
            rank++;

            if (rank > 10) {
                break; // Показываем только топ-10 результатов
            }
        }
    }


    public BattleshipGame() {
        board = new char[BOARD_SIZE][BOARD_SIZE];
        numShips = NUM_SHIPS;
        gameTimes = new ArrayList<>();
    }

    public void initializeBoard() {
        for (char[] row : board) {
            for (int i = 0; i < row.length; i++) {
                row[i] = '-';
            }
        }
    }

    public void placeShips() {
        placeShip(SHIP1_ROW, SHIP1_COL, 1);
        placeShip(SHIP2_ROW, SHIP2_COL, 2);
        placeShip(SHIP2_ROW, SHIP2_COL + 1, 2);
        placeShip(SHIP3_ROW, SHIP3_COL, 1);
    }

    private void placeShip(int row, int col, int length) {
        for (int i = 0; i < length; i++) {
            board[row][col + i] = 'S';
        }
    }

    public void printBoard() {
        System.out.print("  ");
        for (int col = 0; col < BOARD_SIZE; col++) {
            System.out.print((char) ('A' + col) + " ");
        }
        System.out.println();

        for (int row = 0; row < BOARD_SIZE; row++) {
            System.out.print((row + 1) + " ");
            for (int col = 0; col < BOARD_SIZE; col++) {
                System.out.print(board[row][col] + " ");
            }
            System.out.println();
        }
    }

    public boolean isGameOver() {
        return numShips == 0;
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Добро пожаловать в игру Морской бой!");

        boolean exit = false;
        while (!exit) {
            System.out.println();
            System.out.println("Выберите действие:");
            System.out.println("1. Новая игра");
            System.out.println("2. Результаты");
            System.out.println("3. Выход");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Чтение символа новой строки после считывания целочисленного значения

            switch (choice) {
                case 1:
                    System.out.println("Новая игра");
                    initializeBoard();
                    placeShips();
                    startTime = System.currentTimeMillis();
                    playGame(scanner);
                    long endTime = System.currentTimeMillis();
                    long gameTime = (endTime - startTime) / 1000; // Продолжительность игры в секундах
                    gameTimes.add(gameTime);
                    break;
                case 2:
                    System.out.println("Результаты");
                    displayTopGameTimes();
                    break;
                case 3:
                    System.out.println("Выход");
                    exit = true;
                    break;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
                    break;
            }
        }
    }

    private void checkDestroyedShips(int row, int col) {
        // Проверка, был ли уничтожен корабль, и пометка палуб "X" вокруг него
        if (row > 0 && board[row - 1][col] == 'U') {
            board[row - 1][col] = 'X';
            markAdjacentCells(row - 1, col);
        }
        if (row < BOARD_SIZE - 1 && board[row + 1][col] == 'U') {
            board[row + 1][col] = 'X';
            markAdjacentCells(row + 1, col);
        }
        if (col > 0 && board[row][col - 1] == 'U') {
            board[row][col - 1] = 'X';
            markAdjacentCells(row, col - 1);
        }
        if (col < BOARD_SIZE - 1 && board[row][col + 1] == 'U') {
            board[row][col + 1] = 'X';
            markAdjacentCells(row, col + 1);
        }
    }

    private void markAdjacentCells(int row, int col) {
        // Проверка и пометка смежных ячеек "X"
        if (row > 0 && board[row - 1][col] == '-') {
            board[row - 1][col] = 'X';
        }
        if (row < BOARD_SIZE - 1 && board[row + 1][col] == '-') {
            board[row + 1][col] = 'X';
        }
        if (col > 0 && board[row][col - 1] == '-') {
            board[row][col - 1] = 'X';
        }
        if (col < BOARD_SIZE - 1 && board[row][col + 1] == '-') {
            board[row][col + 1] = 'X';
        }
    }

    private void playGame(Scanner scanner) {
        boolean gameWon = false;
        while (!gameWon) {
            System.out.println();
            System.out.println("Куда стреляем? (например, A2)");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                continue;
            }

            if (input.length() != 2) {
                System.out.println("Некорректный ввод. Попробуйте снова.");
                continue;
            }

            char colChar = input.charAt(0);
            char rowChar = input.charAt(1);

            if (!Character.isLetter(colChar) || !Character.isDigit(rowChar)) {
                System.out.println("Некорректный ввод. Попробуйте снова.");
                continue;
            }

            int col = colChar - 'A';
            int row = rowChar - '1';

            if (col < 0 || col >= BOARD_SIZE || row < 0 || row >= BOARD_SIZE) {
                System.out.println("Некорректные координаты. Попробуйте снова.");
                continue;
            }

            if (board[row][col] == '-') {
                System.out.println("Промах!");
                board[row][col] = 'o';
            } else if (board[row][col] == 'S') {
                System.out.println("Попадание!");
                board[row][col] = 'U';
                numShips--;
                checkDestroyedShips(row, col);
                if (isGameOver()) {
                    gameWon = true;
                }
            } else {
                System.out.println("Вы уже стреляли в эту ячейку. Попробуйте снова.");
            }

            printBoard();
        }

        long endTime = System.currentTimeMillis();
        long gameTime = (endTime - startTime) / 1000;
        System.out.println("Поздравляем! Вы победили!");
        System.out.println("Время игры: " + gameTime + " секунд");
    }

}


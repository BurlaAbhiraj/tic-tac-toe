import java.util.Scanner;

public class TicTacToe {

    private static final char COMPUTERMOVE = 'O';
    private static final char HUMANMOVE = 'X';
    private static final int SIDE = 3;
    private static final int COMPUTER = 1;
    private static final int HUMAN = 2;

    private static void showBoard(char[][] board) {
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                System.out.print("\t\t\t " + board[i][j]);
            }
            System.out.println("\n\t\t\t--------------------------------------------------");
        }
    }

    private static void showInstructions() {
        System.out.println("\nChoose a cell numbered from 1 to 9 as below and play\n\n");
        System.out.println("\t\t\t 1 | 2 | 3 \n");
        System.out.println("\t\t\t-----------\n");
        System.out.println("\t\t\t 4 | 5 | 6 \n");
        System.out.println("\t\t\t-----------\n");
        System.out.println("\t\t\t 7 | 8 | 9 \n\n");
    }

    private static void initialise(char[][] board) {
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                board[i][j] = '*';
            }
        }
    }

    private static void declareWinner(int whoseTurn) {
        if (whoseTurn == COMPUTER)
            System.out.println("COMPUTER has won");
        else
            System.out.println("HUMAN has won");
    }

    private static boolean rowCrossed(char[][] board) {
        for (int i = 0; i < SIDE; i++) {
            if (board[i][0] == board[i][1] &&
                    board[i][1] == board[i][2] &&
                    board[i][0] != '*') {
                return true;
            }
        }
        return false;
    }

    private static boolean columnCrossed(char[][] board) {
        for (int i = 0; i < SIDE; i++) {
            if (board[0][i] == board[1][i] &&
                    board[1][i] == board[2][i] &&
                    board[0][i] != '*') {
                return true;
            }
        }
        return false;
    }

    private static boolean diagonalCrossed(char[][] board) {
        // Check for diagonal win from top-left to bottom-right
        if (board[0][0] != '*' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return true;
        }
        // Check for diagonal win from top-right to bottom-left
        if (board[0][2] != '*' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return true;
        }
        return false;
    }

    private static boolean gameOver(char[][] board) {
        return (rowCrossed(board) || columnCrossed(board) || diagonalCrossed(board));
    }

    private static int minimax(char[][] board, int depth, boolean isAI) {
        if (gameOver(board)) {
            if (isAI) {
                return -10;
            } else {
                return 10;
            }
        }

        int bestScore;
        if (isAI) {
            bestScore = -999;
            for (int i = 0; i < SIDE; i++) {
                for (int j = 0; j < SIDE; j++) {
                    if (board[i][j] == '*') {
                        board[i][j] = COMPUTERMOVE;
                        int score = minimax(board, depth + 1, false);
                        board[i][j] = '*';
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
        } else {
            bestScore = 999;
            for (int i = 0; i < SIDE; i++) {
                for (int j = 0; j < SIDE; j++) {
                    if (board[i][j] == '*') {
                        board[i][j] = HUMANMOVE;
                        int score = minimax(board, depth + 1, true);
                        board[i][j] = '*';
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
        }

        return bestScore;
    }

    private static int bestMove(char[][] board, int moveIndex) {
        int bestScore = -999;
        int bestMove = -1;
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (board[i][j] == '*') {
                    board[i][j] = COMPUTERMOVE;
                    int score = minimax(board, moveIndex + 1, false);
                    board[i][j] = '*';
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = i * SIDE + j;
                    }
                }
            }
        }
        return bestMove;
    }

    private static void playTicTacToe(int whoseTurn) {
        char[][] board = new char[SIDE][SIDE];
        int moveIndex = 0, x = 0, y = 0;

        initialise(board);
        showInstructions();

        while (!gameOver(board) && moveIndex != SIDE * SIDE) {
            if (whoseTurn == COMPUTER) {
                int n = bestMove(board, moveIndex);
                x = n / SIDE;
                y = n % SIDE;
                board[x][y] = COMPUTERMOVE;
                System.out.println("COMPUTER has put a " + COMPUTERMOVE + " in cell " + (n + 1) + "\n");
                showBoard(board);
                moveIndex++;
                whoseTurn = HUMAN;
            } else if (whoseTurn == HUMAN) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("You can insert in the following positions : ");
                for (int i = 0; i < SIDE; i++)
                    for (int j = 0; j < SIDE; j++)
                        if (board[i][j] == '*')
                            System.out.print((i * 3 + j) + 1 + " ");
                System.out.print("\n\nEnter the position = ");
                int n = scanner.nextInt();
                n--;
                x = n / SIDE;
                y = n % SIDE;

                if (board[x][y] == '*' && n < 9 && n >= 0) {
                    board[x][y] = HUMANMOVE;
                    System.out.println("\nHUMAN has put a " + HUMANMOVE + " in cell " + (n + 1) + "\n");
                    showBoard(board);
                    moveIndex++;
                    whoseTurn = COMPUTER;
                } else if (board[x][y] != '*' && n < 9 && n >= 0) {
                    System.out.println("\nPosition is occupied, select any one place from the available places\n\n");
                } else if (n < 0 || n > 8) {
                    System.out.println("Invalid position\n");
                }
            }
        }

        if (!gameOver(board) && moveIndex == SIDE * SIDE)
            System.out.println("It's a draw\n");
        else {
            if (whoseTurn == COMPUTER)
                whoseTurn = HUMAN;
            else if (whoseTurn == HUMAN)
                whoseTurn = COMPUTER;
            declareWinner(whoseTurn);
        }
    }

    public static void main(String[] args) {
        System.out.println("\n-------------------------------------------------------------------\n");
        System.out.println("\t\t\t Tic-Tac-Toe\n");
        System.out.println("\n-------------------------------------------------------------------\n");

        Scanner scanner = new Scanner(System.in);
        char cont = 'y';
        do {
            char choice;
            System.out.print("Do you want to start first?(y/n) : ");
            choice = scanner.next().charAt(0);
            if (choice == 'n')
                playTicTacToe(COMPUTER);
            else if (choice == 'y')
                playTicTacToe(HUMAN);
            else
                System.out.println("Invalid choice");
            System.out.print("\nDo you want to quit(y/n) : ");
            cont = scanner.next().charAt(0);
        } while (cont == 'n');
        scanner.close();
    }

}
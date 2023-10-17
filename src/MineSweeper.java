import java.util.Random;
import java.util.Scanner;

public class MineSweeper {
    int rowNumber;
    int columnNumber;
    int mineNumber;
    char[][] mineMap;
    boolean[][] revealedCells;
    boolean gameWon;
    boolean gameLost;

    public MineSweeper() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Satır sayısını girin: ");
        rowNumber = scanner.nextInt();
        System.out.print("Sütun sayısını girin: ");
        columnNumber = scanner.nextInt();
        mineNumber = (rowNumber * columnNumber) / 4;

        this.mineMap = new char[rowNumber][columnNumber];
        this.revealedCells = new boolean[rowNumber][columnNumber];

        for (int i = 0; i < rowNumber; i++) {
            for (int j = 0; j < columnNumber; j++) {
                mineMap[i][j] = 'X'; // Kapalı hücre
                revealedCells[i][j] = false;
            }
        }
        mine();
    }

    public void mine() {
        Random random = new Random();
        int sumCells = rowNumber * columnNumber;
        int sumMine = sumCells / 4;
        if (mineNumber > sumMine) {
            mineNumber = sumMine;
        }

        int mined = 0;
        while (mined < mineNumber) {
            int row = random.nextInt(rowNumber);
            int column = random.nextInt(columnNumber);
            if (mineMap[row][column] != 'M') {
                mineMap[row][column] = 'M';
                mined++;
            }
        }
    }

    public void gameBoard() {
        System.out.println("Mine Sweeper:");
        for (int i = 0; i < rowNumber; i++) {
            for (int j = 0; j < columnNumber; j++) {
                if (revealedCells[i][j]) {
                    System.out.print(mineMap[i][j] + " ");
                } else {
                    System.out.print("X ");
                }
            }
            System.out.println();
        }
    }

    public boolean isGame() {
        return !gameWon && !gameLost;
    }

    public void start(int row, int column) {
        if (row < 0 || row > rowNumber || column < 0 || column > columnNumber || revealedCells[row][column]) {
            System.out.println("Haritada olmayan bir hücre seçimi yaptınız. Lütfen haritada bulunan bir hücre seçin.");
            return;
        }

        revealedCells[row][column] = true;
        if (mineMap[row][column] == 'M') {
            gameLost = true;
            System.out.println("Oyunu kaybettiniz!");
            return;
        }

        int mineNumber = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int neighborRow = row + i;
                int neighborColumn = column + j;
                if (neighborRow >= 0 && neighborRow < rowNumber && neighborColumn >= 0 && neighborColumn < columnNumber) {
                    if (mineMap[neighborRow][neighborColumn] == 'M') {
                        mineNumber++;
                    }
                }
            }
        }

        if (mineNumber > 0) {
            mineMap[row][column] = (char) (mineNumber + '0');
        } else {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int neighborRow = row + i;
                    int neighborColumn = column + j;
                    if (neighborRow >= 0 && neighborRow < rowNumber && neighborColumn >= 0 && neighborColumn < columnNumber) {
                        start(neighborRow, neighborColumn);
                    }
                }
            }
        }

        if (isIndex()) {
            gameWon = true;
            System.out.println("Tebrikler kazandınız!");
        }
    }

    public boolean isIndex() {
        for (int i = 0; i < rowNumber; i++) {
            for (int j = 0; j < columnNumber; j++) {
                if (!revealedCells[i][j] && mineMap[i][j] != 'M') {
                    return false;
                }
            }
        }
        return true;
    }

    public void run() {
        while (isGame()) {
            gameBoard();
            System.out.print("Açmak istediğiniz hücrenin satırını girin: ");
            int row = new Scanner(System.in).nextInt();
            System.out.print("Açmak istediğiniz hücrenin sütununu girin: ");
            int column = new Scanner(System.in).nextInt();
            start(row, column);
        }
    }

}

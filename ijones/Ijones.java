package ads_001.ijones;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Victor Artemyev on 28.07.2015.
 */
public class Ijones {

    private static final String FILE_NAME_IN = "ijones.in";
    private static final String FILE_NAME_OUT = "ijones.out";

    private static int corridorWidth;
    private static int corridorHeight;
    private static char[][] slabs;
    private static long[][] solutions;

    public static void main(String[] args) {
        readFromFile();
        System.out.println(Arrays.deepToString(slabs));
        long result = getSuccessfulWays();
        System.out.println(result);
        writeToFile(result);
    }

    private static void readFromFile() {
        try (FileReader fileReader = new FileReader(FILE_NAME_IN);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String[] corridorSize = bufferedReader.readLine().split(" ");
            corridorWidth = Integer.parseInt(corridorSize[0]);
            corridorHeight = Integer.parseInt(corridorSize[1]);

            slabs = new char[corridorHeight][corridorWidth];
            solutions = new long[corridorHeight][corridorWidth];

            for (int row = 0; row < corridorHeight; row++) {
                char[] symbols = bufferedReader.readLine().toCharArray();
                System.arraycopy(symbols, 0, slabs[row], 0, corridorWidth);
            }

//            for (int i = 0; i < slabs.length; i++) {
//                for (int j = 0; j < slabs[i].length; j++) {
//                    System.out.print(slabs[i][j]);
//                }
//                System.out.println();
//            }

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    private static void writeToFile(long value) {
        try (FileWriter writer = new FileWriter(FILE_NAME_OUT)) {
            writer.write(String.valueOf(value));
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    private static long getSuccessfulWays() {

//        long[] waysBySymbol = new long[corridorHeight];
//        long[] currentSolutions = new long[corridorHeight];


        for (int row = 0; row < corridorHeight; row++) {
            for (int column = 0; column < corridorWidth; column++) {
                solutions[row][column] = 1;
            }
        }

        for (int row = 0; row < corridorHeight; row++) {
            for (int column = 0; column < corridorWidth; column++) {
                countJumps(solutions, row, column);
            }
        }

        if(corridorHeight == 1) {
            return solutions[0][solutions[0].length - 1];
        } else {
            return solutions[0][solutions[0].length - 1] +
                    solutions[solutions.length - 1][solutions[0].length - 1];
        }
    }

    private static void countJumps(long[][] solutions, int row, int column) {

        char symbol = slabs[row][column];
        long previousJumps = solutions[row][column];

        for (int j = column + 1; j <  corridorWidth; j++) {
            for (int i = 0; i < corridorHeight; i++) {

                if (i == row && j == column + 1) {
                    continue;
//                    solutions[i][j] += 1;
                }

                else if (symbol == slabs[i][j]) {
                    solutions[i][j] += previousJumps;
                    for (int col = j + 1; col < corridorWidth; col++) {
                        solutions[i][col]++;
                    }
                }
            }
        }
    }

}

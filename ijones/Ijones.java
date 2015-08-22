package ads_001.ijones;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Victor Artemyev on 28.07.2015.
 */
public class Ijones {

    private static final String FILE_NAME_IN = "ijones.in";
    private static final String FILE_NAME_OUT = "ijones.out";

    private static int corridorWidth;
    private static int corridorHeight;
    private static char[][] slabs;

    public static void main(String[] args) {
        readFromFile();
        BigInteger result = getSuccessfulPath();
        writeToFile(result);
    }

    private static void readFromFile() {
        try (FileReader fileReader = new FileReader(FILE_NAME_IN);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String[] corridorSize = bufferedReader.readLine().split(" ");
            corridorWidth = Integer.parseInt(corridorSize[0]);
            corridorHeight = Integer.parseInt(corridorSize[1]);

            slabs = new char[corridorHeight][corridorWidth];

            for (int row = 0; row < corridorHeight; row++) {
                char[] symbols = bufferedReader.readLine().toCharArray();
                System.arraycopy(symbols, 0, slabs[row], 0, corridorWidth);
            }

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    private static void writeToFile(BigInteger value) {
        try (FileWriter writer = new FileWriter(FILE_NAME_OUT)) {
            writer.write(String.valueOf(value));
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    private static BigInteger getSuccessfulPath() {

        BigInteger[][] solutions = new BigInteger[corridorHeight][corridorWidth];
        Map<Character, BigInteger> distinctPathToLetter = new HashMap<>();

        for (char letter = 'a'; letter <= 'z'; letter++) {
            distinctPathToLetter.put(letter, BigInteger.ZERO);
        }

        for (int y = 0; y < corridorHeight; y++) {
            solutions[y][0] = BigInteger.ONE;
            for (int x = 1; x < corridorWidth; x++) {
                solutions[y][x] = BigInteger.ZERO;
            }
            BigInteger countPath = distinctPathToLetter.get(slabs[y][0]).add(BigInteger.ONE);
            distinctPathToLetter.put(slabs[y][0], countPath);
        }

        for (int x = 1; x < corridorWidth; x++) {
            Map<Character, BigInteger> lettersEncounteredInThisColumn = new HashMap<>();

            for (char letter = 'a'; letter <= 'z'; letter++) {
                lettersEncounteredInThisColumn.put(letter, BigInteger.ZERO);
            }

            for (int y = 0; y < corridorHeight; y++) {
                BigInteger case1 = distinctPathToLetter.get(slabs[y][x]);
                BigInteger case2 = slabs[y][x - 1] != slabs[y][x] ? solutions[y][x - 1] : BigInteger.ZERO;
                solutions[y][x] = case1.add(case2);

                BigInteger updatedPath = lettersEncounteredInThisColumn.get(slabs[y][x]).add(solutions[y][x]);
                lettersEncounteredInThisColumn.put(slabs[y][x], updatedPath);
            }

            for (char letter = 'a'; letter <= 'z'; letter++) {
                BigInteger updatedDistinctPath =
                        distinctPathToLetter.get(letter).add(
                                lettersEncounteredInThisColumn.get(letter));
                distinctPathToLetter.put(letter, updatedDistinctPath);
            }
        }

        if(corridorHeight == 1) {
            return solutions[0][corridorWidth - 1];
        } else {
            return solutions[0][corridorWidth - 1]
                    .add(solutions[corridorHeight - 1][corridorWidth - 1]);
        }
    }
}

package ads_001.bugtrk;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

/**
 * Created by Victor Artemyev on 27.06.2015.
 * Updated on 18.07.2015
 */
public class Bugtrk {

    private static final String FILE_NAME_IN = "bugtrk.in";
    private static final String FILE_NAME_OUT = "bugtrk.out";

    private static BigInteger count;
    private static BigInteger width;
    private static BigInteger height;

    private static void readFromFile() {
        try (FileReader fileReader = new FileReader(FILE_NAME_IN);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String[] data = bufferedReader.readLine().split(" ");
            count = new BigInteger(data[0]);
            width = new BigInteger(data[1]);
            height = new BigInteger(data[2]);
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

    private static long getMinSquareSideLength() {

        BigInteger result = BigInteger.ZERO;

        if (count.compareTo(width) == 0 &&
                count.compareTo(height) == 0) {
            result = count;
            return result.longValue();
        }

        BigInteger left = BigInteger.ONE;
        BigInteger right = count.multiply(width.max(height));

        boolean isCurrentEnough;
        boolean isOneBiggerEnough;

        while (right.compareTo(left) > 0) {
            BigInteger middle = left.add(right).divide(BigInteger.valueOf(2));

            isCurrentEnough = isSizeEnough(middle);
            isOneBiggerEnough = isSizeEnough(middle.add(BigInteger.ONE));
            if (!isCurrentEnough && isOneBiggerEnough) {
                result = middle.add(BigInteger.ONE);
                break;
            } else if (!isCurrentEnough) {
                left = middle.add(BigInteger.ONE);
            } else if (isOneBiggerEnough) {
                right = middle;
            }
        }
        return result.longValue();
    }

    private static boolean isSizeEnough(BigInteger size) {
        BigInteger cardsPerRow = size.divide(width);
        BigInteger cardsPerColumn = size.divide(height);
        return cardsPerRow.multiply(cardsPerColumn).compareTo(count) >= 0;
    }

    public static void main(String[] args) {
        readFromFile();
        long minLength = getMinSquareSideLength();
        writeToFile(minLength);
    }
}

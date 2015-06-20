package ads_001.lngpok;

import java.io.*;

/**
 * Created by Victor Artemjev on 30.05.2015.
 */
public class Lngpok {

    private static final String FILE_NAME_IN = "lngpok.in";
    private static final String FILE_NAME_OUT = "lngpok.out";

    private static int[] array;

    public static void main(String[] args) {
        array = readFromFile(FILE_NAME_IN);
        QuickSort.sort(array);
        int numberOfZeros = getNumberOfZeros(array);
        int longestSequenceLength = getLongestSequenceLength(array, numberOfZeros);
        System.out.println(longestSequenceLength);
        writeToFile(FILE_NAME_OUT, longestSequenceLength);
    }

    private static int getLongestSequenceLength(int[] array, int numberOfZeros) {
        if (array.length <= 1) {
            return array.length;
        }

        int maxLength = 1;
        int zeroCount = numberOfZeros;
        int startPosition = zeroCount + 1;

        for (int i = startPosition; i < array.length; i++) {
            int countLength = 1;
            int currentItem = array[i - 1];
            int nextItem = currentItem + 1;

            while (i < array.length) {

                while (i < array.length && currentItem == array[i]) {
                    i++;
                }

                if (i < array.length && nextItem == array[i]) {
                    countLength++;
                    i++;
                }

                while (i < array.length && nextItem == array[i]) {
                    i++;
                }

                nextItem++;

                if ((i < array.length &&
                        nextItem + zeroCount < array[i])
                        || i >= array.length) {

                    while (zeroCount != 0) {
                        zeroCount--;
                        countLength++;
                    }
                    break;

                } else if (nextItem < array[i]) {
                    while (nextItem != array[i]) {
                        zeroCount--;
                        countLength++;
                        nextItem++;
                    }
                }
            }

            zeroCount = numberOfZeros;

            if (maxLength < countLength) {
                maxLength = countLength;
            }
        }
        return maxLength;
    }

    private static int getNumberOfZeros(int[] data) {
        int zeroCount = 0;
        // Assuming the array is already sorted
        while (data[zeroCount] == 0) {
            zeroCount++;
        }
        return zeroCount;
    }

    private static int[] readFromFile(String fileName) {
        int[] result = null;

        try (FileReader fileReader = new FileReader(fileName);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(" ");
                result = new int[data.length];
                for (int i = 0; i < result.length; i++) {
                    result[i] = Integer.parseInt(data[i]);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private static void writeToFile(String fileName, int value) {
        try (FileWriter fileWriter = new FileWriter(fileName);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(String.valueOf(value));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

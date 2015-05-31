package ads_001.lngpok;

import java.io.*;
import java.util.Arrays;

/**
 * Created by Victor Artemjev on 30.05.2015.
 */
public class LngPok {

    private static final String FILE_NAME_IN = "lngpok.in";
    private static final String File_NAME_OUT = "lngpok.out";

    private static int[] array;

//    static int[] array = new int[]
//            {0, 10, 15, 50, 0, 14, 9, 12, 40, 51, 52, 53, 54, 55, 58, 59, 60, 80, 81};
//            {5, 6, 5, 6, 5, 6, 5, 6, 5, 6, 5, 0, 0, 0, 0};
//            {0, 0, 7, 8, 10, 11, 12, 13, 14, 15, 16};
//            {0, 10, 11, 13, 15, 0, 16, 18, 20, 22};
//            {999, 1000, 1, 2};
//            {0, 12, 10, 11, 15, 0, 0, 16, 17, 18, 20};
//            {0, 0, 1, 3, 4, 5, 5, 5, 6, 7, 0, 0};
//            {0, 10, 15, 50, 0, 14, 9, 12, 40};


    public static void main(String[] args) {

        array = readFromFile("C:\\users\\vitman\\desktop\\homework\\movrat.in");
//        array = Lesson_1.Main.getRandomArray(10000000);
        QuickSort.sort(array);
        System.out.println(Arrays.toString(array));
        int zeroCount = getZeroCount(array);
        int longestSequenceLength = getLongestSequenceLength(array, zeroCount);
        System.out.println(longestSequenceLength);
        writeToFile("C:\\users\\vitman\\desktop\\homework\\movrat.out", longestSequenceLength);
    }

    private static int getLongestSequenceLength(int[] array, int zero) {
        if (array.length <= 1) {
            return array.length;
        }

        int maxLength = 1;
        int zeroCount = zero;
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
                if ((i < array.length && nextItem + zeroCount < array[i])
                        || i >= array.length) {
                    while (zeroCount != 0) {
                        zeroCount--;
                        countLength++;
                    }
                    break;
                } else if (nextItem < array[i]){
                    while (nextItem != array[i]) {
                        zeroCount--;
                        countLength++;
                        nextItem++;
                    }
                }
            }
            zeroCount = zero;
            if (maxLength < countLength) {
                maxLength = countLength;
            }
        }
        return maxLength;
    }

    private static int getZeroCount(int[] data) {
        int count = 0;
        // Assuming the array is already sorted
        while (data[count] == 0) {
            count++;
        }
        return count;
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
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private static void writeToFile(String fileName, int value) {
        try(FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(String.valueOf(value));
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

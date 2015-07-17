package ads_001.movrat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Victor Artemyev on 15.05.2015.
 * Updated on 17.07.2015
 */
public class MovRat {

    private static final String FILE_NAME_IN = "movrat.in";
    private static final String FILE_NAME_OUT = "movrat.out";

    public static int[] ratings;
    public static int lowIgnoreCount;
    public static int highIgnoreCount;

    public static void main(String[] args) throws IOException {
        readFromFile();
        int rating = calculateRating();
        writeToFile(rating);
    }

    private static void readFromFile() {
        try (FileReader fileReader = new FileReader(FILE_NAME_IN);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            bufferedReader.readLine();
            String[] data = bufferedReader.readLine().split(" ");
            ratings = new int[data.length];
            for (int i = 0; i < data.length; i++) {
                ratings[i] = Integer.parseInt(data[i]);
            }
            lowIgnoreCount = Integer.parseInt(bufferedReader.readLine());
            highIgnoreCount = Integer.parseInt(bufferedReader.readLine());
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    private static int calculateRating() {
        MergeSort.sort(ratings);
        int leftPos = lowIgnoreCount;
        int rightPos = ratings.length - highIgnoreCount;
        int sum = 0;
        int number = 0;
        for (int i = leftPos; i < rightPos; i++) {
            sum += ratings[i];
            number++;
        }
        int result = sum / number;
        return result;
    }

    private static void writeToFile(int value) {
        try (FileWriter writer = new FileWriter(FILE_NAME_OUT)) {
            writer.write(String.valueOf(value));
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }
}

package ads_001.movrat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Victor Artemjev on 15.05.2015.
 */
public class Main {

    private static final Path FILE_IN = FileSystems.getDefault()
            .getPath("C:\\users\\vitman\\desktop\\homework\\movrat.in");
    private static final Path FILE_OUT = FileSystems.getDefault()
            .getPath("C:\\users\\vitman\\desktop\\homework\\movrat.out");

    public static int N;
    public static int[] ratings;
    public static int lowIgnoreCount;
    public static int highIgnoreCount;
    public static boolean isCorrectData = false;

    public static void main(String[] args) throws IOException {
        calculateMeanRatingFromFile();
//        calculateMeanRatingFromConsole();
    }

    private static boolean isRatingDataValid() {
        return (lowIgnoreCount + highIgnoreCount) < N;
    }

    private static void initRatingData(String[] data) {
        try {
            N = Integer.parseInt(data[0]);
            ratings = new int[N];
            String[] numbers = data[1].split(" ");
            if (numbers.length != N) {
                throw new Exception("Data is not valid!");
            }
            for (int i = 0; i < numbers.length; i++) {
                ratings[i] = Integer.parseInt(numbers[i]);
            }
            lowIgnoreCount = Integer.parseInt(data[2]);
            highIgnoreCount = Integer.parseInt(data[3]);
        } catch (Exception ex) {
            System.err.format("Exception: %s%n", ex);
        }
    }

    private static String[] readRatingsDataFromFile(Path file) {
        int lineNumber = 0;
        String[] data = new String[4];
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                data[lineNumber++] = line;
            }
        } catch (IOException ex) {
            System.err.format("IOException: %s%n", ex);
        }
        return data;
    }

    private static void writeMeanRatingToFile(Path file, int rating) {
        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            writer.write(String.valueOf(rating));
        } catch (IOException ex) {
            System.err.format("IOException: %s%n", ex);
        }
    }

    private static void calculateMeanRatingFromFile() {
        String[] data = readRatingsDataFromFile(FILE_IN);
        initRatingData(data);
        if (isRatingDataValid()) {
            int meanRating = MovRat.calculateArithmeticMeanRating
                    (ratings, lowIgnoreCount, highIgnoreCount);
            writeMeanRatingToFile(FILE_OUT, meanRating);
        } else {
            System.err.println("Data is not valid!");
        }
    }

    private static void calculateMeanRatingFromConsole() {
        while (!isCorrectData) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            try {
                // Input number of ratings
                System.out.println("Кількість оцінок користувачів: ");
                N = Integer.parseInt(reader.readLine());
                ratings = new int[N];

                // Input array of ratings
                System.out.println("Оцінки окремих користувачів: ");
                String[] numbers = reader.readLine().split(" ");
                if (numbers.length != N) {
                    throw new Exception();
                }
                for (int i = 0; i < numbers.length; i++) {
                    ratings[i] = Integer.parseInt(numbers[i]);
                }

                // Input number of low ratings
                System.out.println("Кількість найнижчих оцінок: ");
                lowIgnoreCount = Integer.parseInt(reader.readLine());

                // Input number of high ratings
                System.out.println("Кількість найвижчих оцінок: ");
                highIgnoreCount = Integer.parseInt(reader.readLine());

                // Check sum of high and low ratings
                if ((lowIgnoreCount + highIgnoreCount) >= N) {
                    throw new Exception();
                } else {
                    isCorrectData = true;
                }

            } catch (Exception ex) {
                System.out.println("Некоректні дані!");
                continue;
            }
        }

        // Output arithmetic mean of rating
        if (isCorrectData) {
            System.out.println("РЕЙТИНГ:");
            System.out.println(MovRat
                    .calculateArithmeticMeanRating(ratings, lowIgnoreCount, highIgnoreCount));
        }
    }
}

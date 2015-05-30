package ads_001.movrat;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Victor Artemjev on 15.05.2015.
 */
public class MovRat {

    // Random numbers 0...100
    public static final int RANDOM_NUMBER = 100;

    // Number of ratings
    public static final int N = 4;

    // Number of low ratings
    public static final int LOW_IGNORE_COUNT = 0;

    // Number of high ratings
    public static final int HIGH_IGNORE_COUNT = 0;

    // User ratings
    public static int[] ratings = getRandomRating(N);

    public static void main(String[] args) {
        System.out.println(Arrays.toString(ratings));
        System.out.println(calculateArithmeticMeanRating(ratings, LOW_IGNORE_COUNT, HIGH_IGNORE_COUNT));
    }

    public static int calculateArithmeticMeanRating(int[] array, int lowIgnoreCount, int highIgnoreCount) {
        MergeSort.sort(array);
        int leftPos = lowIgnoreCount;
        int rightPos = array.length - highIgnoreCount;
        int sum = 0;
        int number = 0;
        for (int i = leftPos; i < rightPos; i++) {
            sum += array[i];
            number++;
        }
        return getArithmeticMean(sum, number);
    }

    public static int getArithmeticMean(int sum, int number) {
        return sum / number;
    }

    public static int[] getRandomRating(int n) {
        int[] result = new int[n];

        Random random = new Random();
        for (int i = 0; i < result.length; i++) {
            result[i] = random.nextInt(RANDOM_NUMBER);
        }
        return result;
    }
}

package ads_001.discnt;

import java.util.Random;

/**
 * Created by Victor Artemyev on 19.05.2015.
 */
public class QuickSort {

    public static void sort(int[] data) {

            sortRecursive(data, 0, data.length - 1);

    }

    private static void sortRecursive(int[] data, int leftIndex, int rightIndex) {
        int pivot = getPivot(data, leftIndex);
        int leftWritePos = leftIndex;
        int rightWritePos = rightIndex;

        while (leftWritePos <= rightWritePos) {
            while (compare(data[leftWritePos], pivot)) {
                leftWritePos++;
            }

            while (compare(pivot, data[rightWritePos])) {
                rightWritePos--;
            }

            if (leftWritePos <= rightWritePos) {
                swap(data, leftWritePos, rightWritePos);
                leftWritePos++;
                rightWritePos--;
            }
        }

        if (leftIndex < leftWritePos - 1) {
            sortRecursive(data, leftIndex, leftWritePos - 1);
        }
        if (leftWritePos < rightIndex) {
            sortRecursive(data, leftWritePos, rightIndex);
        }
    }

    private static int getPivot(int[] data, int index) {
        // Assuming the array is already shuffled
        return data[index];
    }

    private static void shuffle(int[] data) {
        Random random = new Random();
        int N = data.length;
        for (int i = 0; i < N; i++) {
            int randomItemIndex = random.nextInt(N - i);
            swap(data, i, randomItemIndex);
        }
    }

    private static void swap(int[] data, int index1, int index2) {
        int temp = data[index1];
        data[index1] = data[index2];
        data[index2] = temp;
    }

    private static boolean compare(int a, int b) {
        return a > b;
    }
}
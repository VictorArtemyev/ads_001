package ads_001;

/**
 * Created by Victor Artemjev on 15.05.2015.
 */
public class MergeSort {

    private static int[] holder;

    public static void sort(int[] array) {
        holder = new int[array.length];
        sortRecursive(array, 0, array.length - 1);
    }

    private static void sortRecursive(int[] array, int left, int right) {
        if (right <= left) {
            return;
        }
        int middle = left + (right - left) / 2;
        sortRecursive(array, left, middle);
        sortRecursive(array, middle + 1, right);
        merge(array, left, middle, right);
    }

    private static void merge(int[] array, int left, int middle, int right) {
        int leftPos = left;
        int middlePos = middle + 1;

        // Copy part of array to holder
        for (int i = left; i <= right; i++) {
            holder[i] = array[i];
        }

        // Merge back to array
        int currentPos = left;
        while (currentPos <= right) {
            if (leftPos > middle) {
                array[currentPos] = holder[middlePos++];
            } else if (middlePos > right) {
                array[currentPos] = holder[leftPos++];
            } else if (holder[middlePos] < holder[leftPos]) {
                array[currentPos] = holder[middlePos++];
            } else {
                array[currentPos] = holder[leftPos++];
            }
            currentPos++;
        }
    }
}

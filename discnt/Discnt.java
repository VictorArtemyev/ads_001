package ads_001.discnt;

import java.io.*;
import java.util.Locale;

/**
 * Created by Victor Artemjev on 01.06.2015.
 */
public class Discnt {

    private static final String FILE_NAME_IN = "E:\\Java\\Algorithms\\src\\ads_001\\discnt\\discnt.in";
    private static final String FILE_NAME_OUT = "E:\\Java\\Algorithms\\src\\ads_001\\discnt\\discnt.out";

    private static final int PURCHASE_DISCOUNT_NUMBER = 3;

    private static int[] prices;
    private static double discount;

    public static void main(String[] args) {
        readFromFile(FILE_NAME_IN);
        double minSum = getMinSum();
        writeToFile(FILE_NAME_OUT, minSum);
    }

    private static void readFromFile(String fileName) {
        try (FileReader fileReader = new FileReader(fileName);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String[] data = bufferedReader.readLine().split(" ");
            prices = new int[data.length];
            for (int i = 0; i < data.length; i++) {
                prices[i] = Integer.parseInt(data[i]);
            }
            discount = getDiscount(Integer.parseInt(bufferedReader.readLine()));
        } catch (Exception e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    private static double getMinSum() {
        QuickSort.sort(prices);
        int discountCount = prices.length / PURCHASE_DISCOUNT_NUMBER;
        double result = 0;
        for (int i = 0; i < discountCount; i++) {
            result += getPriceWithDiscount(prices[i]);
        }
        for (int i = discountCount; i < prices.length; i++) {
            result += prices[i];
        }
        return result;
    }

    private static double getPriceWithDiscount(int price) {
        return price * discount;
    }

    private static double getDiscount(int value) {
        return (double) (100 - value) / 100;
    }

    private static void writeToFile(String fileName, double value) {
        String result = getRoundedValue(value);
        try (FileWriter fileWriter = new FileWriter(fileName);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(result);
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    private static String getRoundedValue(double value) {
        Locale locale = new Locale("en", "UK");
        return String.format(locale, "%.2f", value);
    }
}

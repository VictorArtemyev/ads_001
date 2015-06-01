package ads_001.discnt;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Victor Artemjev on 01.06.2015.
 */
public class Discnt {
    private static final String FILE_NAME_IN = "discnt.in";
    private static final String FILE_NAME_OUT = "discnt.out";

    private static final int PURCHASE_DISCOUNT_NUMBER = 3;

    private static int[] prices;
    private static double discount;

    public static void main(String[] args) {
        readFromFile(FILE_NAME_IN);
        double minSum = getMinSum(prices);
        writeToFile(FILE_NAME_OUT, minSum);
    }

    private static double getMinSum(int[] prices) {
        double minSum = 0.0;
        QuickSort.sort(prices);
        int lastMaxPriceIndex = prices.length - 1;
        int discountCount = prices.length / PURCHASE_DISCOUNT_NUMBER;
        int barrier = prices.length - discountCount;
        if (prices.length % PURCHASE_DISCOUNT_NUMBER == 0) {
            barrier++;
        }
        System.out.println("Barier: " + barrier);
        int priceCount = 1;
        System.out.println("Discount: " + discountCount);

        for (int i = 0; i < barrier; i++) {
            if (discountCount != 0 && priceCount % PURCHASE_DISCOUNT_NUMBER == 0) {
                minSum += getPriceWithDiscount(prices[lastMaxPriceIndex--]);
                discountCount--;
                if (i == barrier - 1 && barrier % PURCHASE_DISCOUNT_NUMBER == 0) {
                    break;
                }
            }
            minSum += prices[i];
            priceCount++;
        }

        return minSum;
    }

    private static double getPriceWithDiscount(int price) {
        return price * discount;
    }

    private static double getDiscount(int value) {
        return (double) (100 - value) / 100;
    }

    private static void readFromFile(String fileName) {
        try (FileReader fileReader = new FileReader(fileName);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(" ");
                if (data.length > 1) {
                    prices = new int[data.length];
                    for (int i = 0; i < prices.length; i++) {
                        prices[i] = Integer.parseInt(data[i]);
                    }
                } else {
                    discount = getDiscount(Integer.parseInt(data[0]));
                }
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void writeToFile(String fileName, double value) {
        String result = getRoundedValue(value);
        try(FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(result);
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static String getRoundedValue(double value) {
        Locale locale  = new Locale("en", "UK");
        String pattern = "##.00";
        DecimalFormat decimalFormat = (DecimalFormat)
                NumberFormat.getNumberInstance(locale);
        decimalFormat.applyPattern(pattern);
        return decimalFormat.format(value);

    }
}

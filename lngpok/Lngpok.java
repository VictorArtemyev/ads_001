package ads_001.lngpok;

import java.io.*;
import java.util.*;

/**
 * Created by Victor Artemjev on 30.05.2015.
 * Updated on 18.07.2015
 */
public class Lngpok {

    private static final String FILE_NAME_IN = "lngpok.in";
    private static final String FILE_NAME_OUT = "lngpok.out";

    private static final int JOKER = 0;

    private static int[] cards;
    private static int jokerCount;
    private static List<Integer> uniqueCards;

    public static void main(String[] args) {
        cards = readFromFile(FILE_NAME_IN);
        QuickSort.sort(cards);
        jokerCount = getNumberOfJokers(cards);
        uniqueCards = getUniqueCards(cards);
        int longestSequenceLength = getLongestSequenceLength();
        writeToFile(FILE_NAME_OUT, longestSequenceLength);
    }

    private static List<Integer> getUniqueCards(int[] cards) {
        List<Integer> result = new ArrayList<>();
        int currentCard = JOKER;
        for (int i = 0; i < cards.length; i++) {
            if (cards[i] != currentCard) {
                result.add(cards[i]);
                currentCard = cards[i];
            }
        }
        return result;
    }

    private static int getLongestSequenceLength() {
        int result = 0;
        for (int i = 0; i < uniqueCards.size(); i++) {
            for (int j = uniqueCards.size() - 1; j >= 0; j--) {
                int length = j - i + 1;
                if (getJokerCost(j, i) <= jokerCount && result < length) {
                    result = length;
                }
            }
        }
        return result + jokerCount;
    }

    private static int getJokerCost(int right, int left) {
        return uniqueCards.get(right) - uniqueCards.get(left) + 1 - (right - left + 1);
    }

//    private static int getLongestSequenceLength() {
//        int result = 0;
//        int lastCard = uniqueCards.size() - 1;
//
//        for (int i = 0; i < uniqueCards.size(); i++) {
//            int leftCard = i;
//            int rightCard = lastCard;
//
//            while (leftCard < rightCard) {
//                int middleCard = (leftCard + rightCard + 1) / 2;
//                int jokerCost = getJokerCost(middleCard, i);
//                if (jokerCount < jokerCost) {
//                    rightCard = middleCard - 1;
//                } else {
//                    leftCard = middleCard;
//                }
//            }
//            int length = rightCard - i + 1;
//            if (length > result) {
//                result = length;
//            }
//        }
//        return result + jokerCount;
//    }

    private static int getNumberOfJokers(int[] cards) {
        int jokerCount = 0;
        for (int card : cards) {
            if (card == JOKER) {
                jokerCount++;
            }
        }
        if(cards.length != jokerCount) {
            return jokerCount;
        } else {
            return cards.length;
        }
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
            System.err.format("IOException: %s%n", ex);
        }
        return result;
    }

    private static void writeToFile(String fileName, int value) {
        try (FileWriter fileWriter = new FileWriter(fileName);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(String.valueOf(value));
        } catch (IOException ex) {
            System.err.format("IOException: %s%n", ex);
        }
    }
}

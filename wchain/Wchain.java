package ads_001.wchain;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by Victor Artemyev on 23.06.2015.
 */
public class Wchain {
    private static final String FILE_NAME_IN = "wchain.in";
    private static final String FILE_NAME_OUT = "wchain.out";

    private static int wordCount;
    private static Word[] words;

    public static void readFromFile() {
        try (FileReader fileReader = new FileReader(FILE_NAME_IN);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String label = bufferedReader.readLine();
            wordCount = Integer.parseInt(label);

            words = new Word[wordCount];
            Map<String, Word> wordMap = new HashMap<>();
            for (int i = 0; i < wordCount; i++) {
                label = bufferedReader.readLine();
                Word word = new Word(i, label);
                words[i] = word;
                wordMap.put(label, word);
            }

            for (Word w : words) {
                label = w.label;
                for (int i = 0; i < label.length(); i++) {
                    String target = removeChar(label, i);
                    if (wordMap.containsKey(target)) {
                        Word childWord = wordMap.get(target);
//                        w.derivedWords.add(childWord);
                        w.derivedWord = childWord;
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            System.err.format("IOException: %s%n", ex);
        }
    }

    public static int DFS(Word word) {
        boolean[] visited = new boolean[wordCount];
        Stack<Word> stack = new Stack<>();
        stack.push(word);

        int count = 0;
//        int wordLength = word.label.length();

        while (!stack.isEmpty()) {
            Word w = stack.pop();
            if (w == null) {
                return count;
            }
//            if (wordLength - 1 == w.label.length()) {
//                wordLength = w.label.length();
                count++;
//            }
            if (!visited[w.id]) {
                visited[w.id] = true;
//                System.out.println(w.derivedWords);
//                stack.addAll(w.derivedWords);
                stack.push(w.derivedWord);
            }
        }
        return count;
    }

    private static int getMaxChainLength(int[] chainLength) {
        Arrays.sort(chainLength);
        return chainLength[chainLength.length - 1];
    }

    private static int[] getChainLength() {
        int[] chainLength = new int[wordCount];
        for (Word w : words) {
            chainLength[w.id] = DFS(w);
        }
        return chainLength;
    }

    private static String removeChar(String word, int position) {
        StringBuilder stringBuilder = new StringBuilder(word);
        stringBuilder.delete(position, position + 1);
        return stringBuilder.toString();
    }

    private static void writeToFile(int value) {
        try (FileWriter fileWriter = new FileWriter(FILE_NAME_OUT);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(String.valueOf(value));
        } catch (IOException ex) {
            System.err.format("IOException: %s%n", ex);
        }
    }

    public static void main(String[] args) {
//        long time = System.currentTimeMillis();

        readFromFile();
        int[] chainLength = getChainLength();
        int maxChainLength = getMaxChainLength(chainLength);

//        time = System.currentTimeMillis() - time;
//        Date date = new Date(time);
//        DateFormat formatter = new SimpleDateFormat("mm:ss:SSS");
//        String dateFormatted = formatter.format(date);
//        System.out.println("getMaxChainLength: " + dateFormatted);

        writeToFile(maxChainLength);
    }

    private static class Word {
        public int id;
        public String label;
//        public List<Word> derivedWords = new ArrayList<>();
        public Word derivedWord;

        public Word(int id, String label) {
            this.id = id;
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }
}

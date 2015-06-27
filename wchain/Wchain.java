package ads_001.wchain;

import java.io.*;
import java.util.*;

/**
 * Created by Victor Artemjev on 23.06.2015.
 */
public class Wchain {
    private static final String FILE_NAME_IN = "wchain.in";
    private static final String FILE_NAME_OUT = "wchain.out";

    private static int wordCount;
    private static Word[] words;

    public static void readFromFile() {
        String line = null;
        try (FileReader fileReader = new FileReader(FILE_NAME_IN);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            line = bufferedReader.readLine();
            wordCount = Integer.parseInt(line);

            words = new Word[wordCount];
            Map<String, Word> wordMap = new HashMap<>();
            for (int i = 0; i < words.length; i++) {
                line = bufferedReader.readLine();
                Word word = new Word(i, line);
                words[i] = word;
                wordMap.put(line, word);
            }

            for (Word w : words) {
                String label = w.label;
                for (int i = 0; i < label.length(); i++) {
                    String target = removeChar(label, i);
                    if (wordMap.containsKey(target)) {
                        Word childWord = wordMap.get(target);
                        w.derivedWords.add(childWord);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static int DFS(Word word) {
        boolean[] visited = new boolean[wordCount];
        Stack<Word> stack = new Stack<>();
        stack.push(word);

        int count = 1;
        int wordLength = word.label.length();

        while (!stack.isEmpty()) {
            Word w = stack.pop();
            if (wordLength - 1 == w.label.length()) {
                wordLength = w.label.length();
                count++;
            }
            if (!visited[w.id]) {
                visited[w.id] = true;
                for (int i = w.derivedWords.size() - 1; i >= 0; i--) {
                    stack.push(w.derivedWords.get(i));
                }
            }
        }
        return count;
    }

    private static int getMaxChainLength(int[] chainLength) {
        Arrays.sort(chainLength);
        return chainLength[chainLength.length - 1];
    }

    private static int[] getChainLength () {
        int[] chainLength = new int[wordCount];
        for (Word w : words) {
            chainLength[w.id] = DFS(w);
        }
        return chainLength;
    }

    private static String removeChar(String word, int position) {
        return word.substring(0, position) + word.substring(position + 1, word.length());
    }

    private static void writeToFile(int value) {
        try (FileWriter fileWriter = new FileWriter(FILE_NAME_OUT);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(String.valueOf(value));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        readFromFile();
        int[] chainLength = getChainLength();
        int maxChainLength = getMaxChainLength(chainLength);
        writeToFile(maxChainLength);
    }

    private static class Word {
        public int id;
        public String label;
        public List<Word> derivedWords = new ArrayList<>();

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

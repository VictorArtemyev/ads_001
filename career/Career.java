package ads_001.career;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Victor Artemyev on 26.07.2015.
 */
public class Career {

    private static final String FILE_NAME_IN = "career.in";
    private static final String FILE_NAME_OUT = "career.out";

    public static void main(String[] args) {

        int[][] works = readFromFile();

//        for (int i = 0; i < works.length; i++) {
//            for (int j = 0; j < works[i].length; j++) {
//                System.out.print(works[i][j]);
//            }
//            System.out.println();
//        }

        int maxTotalExperience = findMaxTotalExperience(works);
        writeToFile(maxTotalExperience);
    }

    private static int findMaxTotalExperience(int[][] works) {
        for (int i = works.length - 2; i >= 0 ; i--) {
            for (int j = 0; j <= i; j++) {
                works[i][j] += Math.max(works[i + 1][j], works[i + 1][j + 1]);
            }
        }
        return works[0][0];
    }

    private static int[][] readFromFile() {

        int[][] works = null;

        try (FileReader fileReader = new FileReader(FILE_NAME_IN);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            int levels = Integer.parseInt(bufferedReader.readLine());

            works = new int[levels][];
            for (int i = 0; i < works.length; i++) {
                String[] data = bufferedReader.readLine().split(" ");
                works[i] = new int[data.length];
                for (int j = 0; j < works[i].length; j++) {
                    works[i][j] = Integer.parseInt(data[j]);
                }
            }

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
        return works;
    }

    private static void writeToFile(int value) {
        try (FileWriter writer = new FileWriter(FILE_NAME_OUT)) {
            writer.write(String.valueOf(value));
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }
}

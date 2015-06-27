package ads_001.bugtrk;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Victor Artemjev on 27.06.2015.
 */
public class Bugtrk {

    private static final String FILE_NAME_IN = "bugtrk.in";
    private static final String FILE_NAME_OUT = "bugtrk.out";

    private static long paperCount;
    private static long paperWidth;
    private static long paperHeight;

    private static void readFromFile() {
        try (FileReader fileReader = new FileReader(FILE_NAME_IN);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String[] data = bufferedReader.readLine().split(" ");
            paperCount = Integer.parseInt(data[0]);
            paperWidth = Integer.parseInt(data[1]);
            paperHeight = Integer.parseInt(data[2]);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeToFile(long value) {
        try (FileWriter writer = new FileWriter(FILE_NAME_OUT)) {
            writer.write(String.valueOf(value));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static long getMinSquareSideLength() {
        long minLength = paperHeight;

        long area = paperWidth * paperHeight;
        long totalArea = area * paperCount;

        long squareSide = (long) Math.sqrt(totalArea);

        while (squareSide > minLength) {
            minLength += paperHeight;
        }
        return minLength;
    }

    public static void main(String[] args) {
        readFromFile();
        long minLength = getMinSquareSideLength();
        writeToFile(minLength);
    }
}

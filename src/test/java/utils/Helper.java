package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class Helper {

    public static String readTextFromFile(String filePath) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/files/" + filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();
    }


}

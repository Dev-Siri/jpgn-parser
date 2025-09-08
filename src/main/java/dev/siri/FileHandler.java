package dev.siri;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileHandler {
    private final String filePath;

    FileHandler(String filePath) {
        this.filePath = filePath;
    }

    public String readFileToString() throws IOException {
        final StringBuilder fileContents = new StringBuilder();
        final FileReader fileReader = new FileReader(filePath);
        final BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line;

        while ((line = bufferedReader.readLine()) != null) {
            fileContents.append(line);
            fileContents.append('\n');
        }

        return fileContents.toString();
    }
}

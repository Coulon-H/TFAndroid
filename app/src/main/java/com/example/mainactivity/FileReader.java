package com.example.mainactivity;

import java.io.*;

public class FileReader {
    public byte[] readFile(String fileName) throws IOException {
        File file = new File(fileName);
        byte[] fileData = new byte[(int)file.length()];
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(file));

        int bytesRead = 0;
        int b;
        while ((b = br.read()) != -1) {
            fileData[bytesRead++] = (byte)b;
        }

        br.close();
        return fileData;
    }
}


package com.example.mainactivity;

import java.io.*;

public class FileWriter {
    public int writeFile(File fs, InputStream inputStream) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fs));
        int bytesWritten = 0;
        int b = 0;
        while ((b = inputStream.read()) != -1) {
            bos.write(b);
            bytesWritten++;
        }
        bos.close();

        return bytesWritten;
    }
}

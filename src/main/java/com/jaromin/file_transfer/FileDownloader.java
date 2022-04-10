package com.jaromin.file_transfer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileDownloader {

    public static TransferredFile getFile(String path){
        byte[] bytes = toByreArray(path);
        return TransferredFile.builder()
                .fileName(path)
                .fileBytesArray(bytes)
                .build();
    }

    private static byte[] toByreArray(String path) {
        byte[] bytesArray = new byte[16*1024];
        try {
            bytesArray = Files.readAllBytes(Path.of(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytesArray;
    }
}

package com.jaromin.file_transfer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUploader {

    public static TransferredFile getFile(String path){
        byte[] bytes = toByreArray(path);
        String[] pathElements = path.split("/");
        String fileName = pathElements[pathElements.length-1];
        return TransferredFile.builder()
                .fileName(fileName)
                .fileBytesArray(bytes)
                .build();
    }

    private static byte[] toByreArray(String path) {
        byte[] bytesArray = new byte[1024];
        try {
            bytesArray = Files.readAllBytes(Path.of(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytesArray;
    }
}



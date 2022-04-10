package com.jaromin.file_transfer;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class TransferredFile implements Serializable {
    private String fileName;
    private int fileSize;
    private byte[] fileBytesArray;
}

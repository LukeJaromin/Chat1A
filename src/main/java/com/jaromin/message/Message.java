package com.jaromin.message;

import java.io.Serializable;

import com.jaromin.file_transfer.TransferredFile;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message implements Serializable {

    private String text;
    private String sender;
    private String receiver;
    private Action action;
    private String channel;
    private TransferredFile transferredFile;

}

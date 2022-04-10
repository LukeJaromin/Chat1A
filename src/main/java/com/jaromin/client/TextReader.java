package com.jaromin.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TextReader {

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final BufferedReader reader;
    private final Consumer<String> onText;

    public TextReader(InputStream inputStream, Consumer<String> onText) {
        this.onText = onText;
        reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public void read () {
        String text;
        try {
            while ((text = reader.readLine()) != null) {
                onText.accept(text);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Read message failed: " + e.getMessage());
        }
    }
}

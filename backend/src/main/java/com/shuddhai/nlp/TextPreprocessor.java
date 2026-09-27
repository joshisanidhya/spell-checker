package com.shuddhai.nlp;

import java.text.Normalizer;

public final class TextPreprocessor {
    private TextPreprocessor() { }

    public static String clean(String text) {
        if (text == null) return "";
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFC);
        return normalized.replaceAll("[^\\u0900-\\u097F\\s]", " ")
                .replaceAll("\\s+", " ").trim();
    }
}
package com.shuddhai.nlp;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Tokenizer {
    private Tokenizer() { }

    public static List<String> tokenize(String text) {
        String cleaned = TextPreprocessor.clean(text);
        return cleaned.isEmpty() ? Collections.emptyList() : Arrays.asList(cleaned.split("\\s+"));
    }
}
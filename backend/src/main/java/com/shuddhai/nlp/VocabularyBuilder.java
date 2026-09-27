package com.shuddhai.nlp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public final class VocabularyBuilder {
    private VocabularyBuilder() { }

    public static Vocabulary fromFile(Path path) throws IOException {
        Map<String, Integer> frequencies = new LinkedHashMap<>();
        try (var lines = Files.lines(path, StandardCharsets.UTF_8)) {
            lines.flatMap(line -> Tokenizer.tokenize(line).stream())
                    .forEach(word -> frequencies.merge(word, 1, Integer::sum));
        }
        return new Vocabulary(frequencies);
    }
}
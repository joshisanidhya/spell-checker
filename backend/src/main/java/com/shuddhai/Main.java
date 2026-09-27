package com.shuddhai;

import com.shuddhai.api.ApiServer;
import com.shuddhai.nlp.Vocabulary;
import com.shuddhai.nlp.VocabularyBuilder;
import com.shuddhai.spell.SpellChecker;
import java.nio.file.Path;

public final class Main {
    private Main() { }

    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        Path dataPath = Path.of(System.getenv().getOrDefault("DATASET_PATH", "backend/data/hindi_10k.txt"));
        Vocabulary vocabulary = VocabularyBuilder.fromFile(dataPath);
        System.out.printf("ShuddhAI vocabulary loaded: %d words%n", vocabulary.size());
        ApiServer server = new ApiServer(port, new SpellChecker(vocabulary));
        server.start();
        System.out.printf("ShuddhAI API listening on http://localhost:%d%n", port);
    }
}
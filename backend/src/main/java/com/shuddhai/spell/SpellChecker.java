package com.shuddhai.spell;

import com.shuddhai.model.Candidate;
import com.shuddhai.model.Correction;
import com.shuddhai.model.SpellCheckResponse;
import com.shuddhai.nlp.Tokenizer;
import com.shuddhai.nlp.Vocabulary;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class SpellChecker {
    private final Vocabulary vocabulary;
    private final Map<String, List<Candidate>> cache = new HashMap<>();

    public SpellChecker(Vocabulary vocabulary) { this.vocabulary = vocabulary; }

    public SpellCheckResponse check(String text) {
        long started = System.nanoTime();
        List<String> tokens = Tokenizer.tokenize(text);
        List<Correction> errors = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (int position = 0; position < tokens.size(); position++) {
            String word = tokens.get(position);
            if (vocabulary.contains(word) || !seen.add(word)) continue;
            List<Candidate> suggestions = cache.computeIfAbsent(word,
                    value -> Scorer.score(value, CandidateGenerator.generate(value, vocabulary), vocabulary));
            errors.add(new Correction(word, position, suggestions));
        }
        return new SpellCheckResponse(text, errors, (System.nanoTime() - started) / 1_000_000);
    }
}
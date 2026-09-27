package com.shuddhai.spell;

import com.shuddhai.nlp.EditDistance;
import com.shuddhai.nlp.Vocabulary;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class CandidateGenerator {
    private CandidateGenerator() { }

    public static List<String> generate(String word, Vocabulary vocabulary) {
        int wordLength = word.codePointCount(0, word.length());
        List<String> candidates = new ArrayList<>();
        for (String candidate : vocabulary.words()) {
            if (candidate.equals(word) || Math.abs(wordLength - candidate.codePointCount(0, candidate.length())) > 2) continue;
            if (EditDistance.calculate(word, candidate) <= 2) candidates.add(candidate);
        }
        candidates.sort(Comparator.naturalOrder());
        return candidates;
    }
}
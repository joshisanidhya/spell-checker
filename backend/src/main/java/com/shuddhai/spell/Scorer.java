package com.shuddhai.spell;

import com.shuddhai.model.Candidate;
import com.shuddhai.nlp.EditDistance;
import com.shuddhai.nlp.Vocabulary;
import java.util.Comparator;
import java.util.List;

public final class Scorer {
    private Scorer() { }

    public static List<Candidate> score(String word, List<String> words, Vocabulary vocabulary) {
        double maxLog = Math.log1p(vocabulary.getMaxFrequency());
        return words.stream().map(candidate -> {
            int distance = EditDistance.calculate(word, candidate);
            double editSimilarity = EditDistance.similarity(word, candidate);
            int frequency = vocabulary.getFrequency(candidate);
            double frequencyScore = maxLog == 0 ? 0 : Math.log1p(frequency) / maxLog;
            return new Candidate(candidate, distance, editSimilarity, frequency, frequencyScore,
                    0.70 * editSimilarity + 0.30 * frequencyScore);
        }).sorted(Comparator.comparingDouble(Candidate::finalScore).reversed()
                .thenComparingInt(Candidate::editDistance).thenComparing(Candidate::word)).limit(5).toList();
    }
}
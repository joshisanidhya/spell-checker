import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class SpellChecker {

    private final Vocabulary vocabulary;

    public SpellChecker(Vocabulary vocabulary) {
        this.vocabulary = vocabulary;
    }

    public boolean isKnownWord(String word) {
        return vocabulary.contains(word);
    }

    public List<String> getSuggestions(String input, int limit) {

        List<String> suggestions = new ArrayList<>();

        input = Tokenizer.cleanText(input);

        if (input.isEmpty() || vocabulary.contains(input)) {
            return suggestions;
        }

        List<Candidate> candidates = new ArrayList<>();

        for (Map.Entry<String, Integer> entry :
                vocabulary.getWords().entrySet()) {

            String word = entry.getKey();

            int wordLength =
                    word.codePointCount(0, word.length());

            int distance =
                    EditDistance.calculate(input, word);
            candidates.add(
                    new Candidate(
                            word,
                            distance,
                            entry.getValue()
                    )
            );
        }

        candidates.sort(
                Comparator
                        .comparingInt(Candidate::getDistance)
                        .thenComparing(
                                Comparator.comparingInt(
                                        Candidate::getFrequency
                                ).reversed()
                        )
                        .thenComparing(Candidate::getWord)
        );

        int count =
                Math.min(limit, candidates.size());

        for (int i = 0; i < count; i++) {
            suggestions.add(
                    candidates.get(i).getWord()
            );
        }

        return suggestions;
    }

    private static class Candidate {

        private final String word;
        private final int distance;
        private final int frequency;

        public Candidate(
                String word,
                int distance,
                int frequency
        ) {
            this.word = word;
            this.distance = distance;
            this.frequency = frequency;
        }

        public String getWord() {
            return word;
        }

        public int getDistance() {
            return distance;
        }

        public int getFrequency() {
            return frequency;
        }
    }
}

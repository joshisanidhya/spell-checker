import java.util.Comparator;
import java.util.List;
import java.util.Map;

/** Finds the five closest vocabulary words, using corpus frequency for ties. */
public final class SpellChecker {
    private final Vocabulary vocabulary;

    public SpellChecker(Vocabulary vocabulary) { this.vocabulary = vocabulary; }

    public boolean isCorrect(String word) { return vocabulary.contains(word); }

    public List<String> suggestions(String word) {
        return vocabulary.frequencies().keySet().stream()
                .sorted(Comparator.comparingInt((String candidate) -> EditDistance.calculate(word, candidate))
                        .thenComparing(Comparator.comparingInt(vocabulary::frequency).reversed())
                        .thenComparing(Comparator.naturalOrder()))
                .limit(5)
                .toList();
    }
}

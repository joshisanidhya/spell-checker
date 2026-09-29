import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/** Word frequencies counted from the Hindi corpus. */
public final class Vocabulary {
    private final Map<String, Integer> frequencies;

    public Vocabulary(Map<String, Integer> frequencies) {
        this.frequencies = Map.copyOf(frequencies);
    }

    public static Vocabulary fromFile(Path path) throws IOException {
        Map<String, Integer> counts = new LinkedHashMap<>();
        try (var lines = Files.lines(path, StandardCharsets.UTF_8)) {
            lines.flatMap(line -> Tokenizer.tokenize(line).stream())
                    .forEach(word -> counts.merge(word, 1, Integer::sum));
        }
        return new Vocabulary(counts);
    }

    public boolean contains(String word) { return frequencies.containsKey(word); }
    public int frequency(String word) { return frequencies.getOrDefault(word, 0); }
    public Map<String, Integer> frequencies() { return frequencies; }
}

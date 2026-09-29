import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Vocabulary {

    private final Map<String, Integer> frequencies =
            new HashMap<>();

    public void load(Path filePath) throws IOException {

        List<String> lines = Files.readAllLines(
                filePath,
                StandardCharsets.UTF_8
        );

        for (String line : lines) {

            List<String> words =
                    Tokenizer.tokenize(line);

            for (String word : words) {

                if (word.codePointCount(0, word.length()) < 3) {
                    continue;
                }

                frequencies.put(
                        word,
                        frequencies.getOrDefault(word, 0) + 1
                );
            }
        }
    }

    public boolean contains(String word) {
        return frequencies.containsKey(word);
    }

    public int getFrequency(String word) {
        return frequencies.getOrDefault(word, 0);
    }

    public Map<String, Integer> getWords() {
        return frequencies;
    }

    public int size() {
        return frequencies.size();
    }
}

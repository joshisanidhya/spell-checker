import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;

/** Turns corpus text or input into NFC-normalized Devanagari words. */
public final class Tokenizer {
    private Tokenizer() { }

    public static List<String> tokenize(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFC);
        String cleaned = normalized.replaceAll("[^\\u0900-\\u097F\\s]", " ")
                .replaceAll("\\s+", " ").trim();
        return cleaned.isEmpty() ? List.of() : Arrays.asList(cleaned.split(" "));
    }
}

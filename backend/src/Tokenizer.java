import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    public static String cleanText(String text) {

        text = Normalizer.normalize(
                text,
                Normalizer.Form.NFC
        );

        text = text.replaceAll(
                "[^\\u0900-\\u097F\\s]",
                " "
        );

        text = text.replaceAll(
                "\\s+",
                " "
        ).trim();

        return text;
    }

    public static List<String> tokenize(String text) {

        List<String> words = new ArrayList<>();

        String cleaned = cleanText(text);

        if (cleaned.isEmpty()) {
            return words;
        }

        String[] tokens = cleaned.split("\\s+");

        for (String token : tokens) {

            if (!token.isEmpty()) {
                words.add(token);
            }
        }

        return words;
    }
}
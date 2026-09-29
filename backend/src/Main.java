import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

public final class Main {
    private static final Path CORPUS = Path.of("backend/data/hindi_10k.txt");

    private Main() { }

    public static void main(String[] args) throws Exception {
        Vocabulary vocabulary = Vocabulary.fromFile(CORPUS);
        SpellChecker checker = new SpellChecker(vocabulary);
        System.out.println("================================");
        System.out.println("       ShuddhAI Spell Checker");
        System.out.println("================================");

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        while (true) {
            System.out.print("\nEnter a Hindi word (or 'exit'): ");
            String line = input.readLine();
            if (line == null || line.trim().equalsIgnoreCase("exit")) break;
            List<String> words = Tokenizer.tokenize(line);
            if (words.isEmpty()) {
                System.out.println("Please enter a Hindi word.");
                continue;
            }
            String word = words.get(0);
            if (checker.isCorrect(word)) {
                System.out.println("The word is correct.");
                continue;
            }
            System.out.println("\nWord not found.\n");
            System.out.println("Top 5 suggestions:");
            List<String> suggestions = checker.suggestions(word);
            for (int i = 0; i < suggestions.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, suggestions.get(i));
            }
        }
    }
}

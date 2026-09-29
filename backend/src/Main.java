import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("Loading Hindi vocabulary...");

            Vocabulary vocabulary = new Vocabulary();
            vocabulary.load(Path.of("backend/data/hindi_10k.txt"));

            System.out.println("Vocabulary loaded: " + vocabulary.size() + " words");
            new Server(new SpellChecker(vocabulary)).start();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

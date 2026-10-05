# ShuddhAI

ShuddhAI is a Hindi spelling suggestion system. You type a Hindi word, and it tells you whether the spelling is correct. If not, it shows up to 5 close suggestions based on the words in its Hindi corpus.

## Features

- Hindi spelling checking
- Top 5 spelling suggestions
- Levenshtein edit distance for finding similar words
- Hindi corpus (`hindi_10k.txt`) as the word source
- Word frequency used to rank suggestions
- Hindi virtual keyboard (Devanagari) in the browser
- Simple web interface served directly from the Java backend

## How It Works

1. Hindi words are loaded from the corpus file on startup.
2. A vocabulary is built from these words, along with each word's frequency.
3. When you enter a word, it checks if the word exists in the vocabulary.
4. If the word is not found, Levenshtein distance is calculated between your word and every word in the vocabulary.
5. Candidates are sorted by distance first, then by frequency. The top 5 are shown as suggestions.

## Tech Stack

| Layer      | Technology                      |
|------------|---------------------------------|
| Frontend   | HTML, CSS, JavaScript           |
| Backend    | Java (jdk.httpserver)           |
| Algorithm  | Levenshtein Edit Distance       |
| Data       | Hindi corpus (`hindi_10k.txt`)  |
| Deployment | Docker / Render                 |

## Project Structure

```
spell-checker/
├── backend/
│   ├── data/
│   │   └── hindi_10k.txt       # Hindi corpus
│   └── src/
│       ├── Main.java           # Entry point
│       ├── Server.java         # HTTP server and routing
│       ├── SpellChecker.java   # Suggestion logic
│       ├── EditDistance.java   # Levenshtein implementation
│       ├── Vocabulary.java     # Vocabulary and frequency map
│       └── Tokenizer.java      # Text cleaning
├── frontend/
│   ├── index.html
│   ├── style.css
│   └── script.js
├── Dockerfile
└── README.md
```

## Run Locally

Make sure you have **Java 17+** installed.

```bash
# Compile
javac --add-modules jdk.httpserver -encoding UTF-8 backend/src/*.java -d backend/src

# Run
java --add-modules jdk.httpserver -cp backend/src Main
```

Then open `http://localhost:8080` in your browser.

> The `PORT` environment variable can be set to change the default port (8080).

## Docker

```bash
# Build the image
docker build -t shuddhai .

# Run the container
docker run -p 8080:10000 shuddhai
```

Then open `http://localhost:8080`.

## API

**POST** `/check`

Request body:
```json
{ "word": "हिंदी" }
```

Response:
```json
{
  "word": "हिंदी",
  "correct": true,
  "suggestions": []
}
```

If the word is not in the vocabulary, `correct` is `false` and `suggestions` contains up to 5 similar words.

**GET** `/health` — returns `{"status":"ok"}` if the server is running.

## Limitations

- Suggestions depend entirely on the corpus — if a valid word isn't in it, the system won't recognize it.
- It only checks individual word spelling, not grammar or sentence meaning.
- Edit distance works at the character level, so phonetically similar but visually different words may not rank well.

## Future Improvements

- Larger and more diverse Hindi corpus
- Context-aware suggestions (sentence-level)
- Support for more Indian languages

## Author

**Sanidhya Joshi**

GitHub: https://github.com/joshisanidhya

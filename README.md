# ShuddhAI

ShuddhAI is a Hindi spelling suggestion system. We type a Hindi word, and it tells us whether the spelling is correct. If not, it shows up to 5 close suggestions based on the words in its Hindi corpus.

## Features

- Hindi spelling checking
- Up to 5 spelling suggestions
- Levenshtein edit distance for finding similar words
- Hindi corpus as the vocabulary source
- Word frequency used to rank suggestions
- Hindi virtual keyboard (Devanagari)
- Simple web interface served from the Java backend

## How It Works

1. Hindi sentences are loaded from the corpus when the application starts.
2. A vocabulary is created from the words in the corpus along with their frequencies.
3. The entered word is checked against the vocabulary.
4. If the word is not found, Levenshtein edit distance is calculated between the input word and vocabulary words.
5. The candidates are sorted by edit distance first and then by word frequency.
6. The top 5 suggestions are shown to the user.

## Dataset / Data Source

The Hindi text data used in this project is taken from the **AI4Bharat IndicNLP Corpus**, developed by **AI4Bharat at IIT Madras**.

A subset of **10,000 Hindi sentences** was extracted from the Hindi corpus and stored in:


backend/data/hindi_10k.txt

This subset is used to build the vocabulary and calculate word frequencies for the spelling suggestion system.

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

## Docker

```bash
# Build the image
docker build -t shuddhai .

# Run the container
docker run -p 8080:10000 shuddhai
```

Then open `http://localhost:8080`.


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


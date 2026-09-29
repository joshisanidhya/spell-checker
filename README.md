# ShuddhAI

ShuddhAI is a simple Hindi spelling suggestion system. Enter a Hindi word to check it against a corpus vocabulary and, if it is unknown, receive up to five close suggestions.

The project uses a small Java backend and a plain HTML, CSS, and JavaScript frontend. It does not use external libraries.

## Features

- Hindi Unicode text support with NFC normalization
- Vocabulary and word frequencies built from a Hindi corpus
- A clickable Devanagari keyboard, plus support for physical keyboards
- Unknown-word detection
- Up to five suggestions ranked by Levenshtein distance, then corpus frequency
- A minimum input length of three Unicode code points

## How It Works

```text
Browser
   ↓
HTML/CSS/JavaScript
   ↓
POST /check
   ↓
Java HttpServer
   ↓
Vocabulary
   ↓
Levenshtein Distance
   ↓
Top 5 Suggestions
   ↓
JSON response
   ↓
Browser
```

The backend reads and tokenizes the corpus, then counts each word. Tokens with fewer than three Unicode code points are excluded. When a word is submitted, the backend checks vocabulary membership directly. If the word is unknown, it calculates its Levenshtein distance to vocabulary words and ranks all candidates by smallest distance. More frequent words come first when distances tie. The ranking has no distance cutoff, so it returns five suggestions whenever at least five vocabulary words are available.

Levenshtein distance is the minimum number of insertions, deletions, and substitutions needed to change one word into another. A lower distance means a closer spelling.

## Hindi Virtual Keyboard

The keyboard provides common Devanagari vowels, consonants, vowel signs, and marks, along with Space, Backspace, and Clear. Clicking a character inserts it at the current cursor or selection in the input. The physical keyboard works as usual.

## Dataset

The project uses the existing `backend/data/hindi_10k.txt` corpus as UTF-8 input. Corpus words and their frequencies determine which words can be suggested and how equal-distance suggestions are ordered.

## Project Structure

```text
spell-checker/
├── .gitignore
├── README.md
├── ResearchPaper/
│   ├── 2025.coling-main.406.pdf
│   ├── proposal.pdf
│   └── report_context.pdf
├── backend/
│   ├── data/
│   │   └── hindi_10k.txt
│   └── src/
│       ├── Main.java
│       ├── Server.java
│       ├── SpellChecker.java
│       ├── Vocabulary.java
│       ├── Tokenizer.java
│       └── EditDistance.java
└── frontend/
    ├── index.html
    ├── style.css
    └── script.js
```

## Requirements

Java 11 or later with the `jdk.httpserver` module. The frontend uses standard browser HTML, CSS, and JavaScript and needs no package installation.

## Run Locally

From the repository root, compile the Java backend:

```bash
javac --add-modules jdk.httpserver -encoding UTF-8 backend/src/*.java
```

Start the backend from the repository root so it can find the corpus:

```bash
java --add-modules jdk.httpserver -cp backend/src Main
```

Keep the backend terminal running. Open `frontend/index.html` in a browser to use the app. Stop the backend with Ctrl+C.

## Use the API

The backend accepts `POST /check` at `http://localhost:8080/check` with a JSON body:

```json
{"word":"हिनदि"}
```

An unknown word returns `correct: false` and up to five vocabulary suggestions. A known word returns `correct: true`. Inputs shorter than three code points receive an error message.

## Limitations

- Suggestions can only come from words in the corpus.
- The checker compares spellings character by character; it does not use meaning or sentence context.
- Suggestion quality depends on the corpus.
- This is a basic spelling suggestion system, not a grammar checker.

## Future Improvements

Possible future work includes adding a larger corpus, context-aware or phonetic suggestions, Hindi morphological analysis, or support for other Indian languages.

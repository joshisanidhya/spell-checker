# ShuddhAI

ShuddhAI is a simple Hindi spelling suggestion system that suggests the top 5 closest words for a potentially misspelled Hindi word using a Hindi text corpus and Levenshtein edit distance.

## Features

- Hindi Unicode text support with NFC normalization
- Vocabulary generation and word-frequency counting from a Hindi corpus
- Detection of words that are not in the vocabulary
- Levenshtein edit-distance suggestions measured over Unicode code points
- Up to five suggestions, ranked by edit distance and then corpus frequency
- Interactive command-line interface with an `exit` command

## How It Works

```text
Hindi Corpus
     ↓
Text Preprocessing
     ↓
Tokenization
     ↓
Vocabulary + Word Frequency
     ↓
User enters Hindi word
     ↓
Check whether word exists
     ↓
Calculate Levenshtein Distance
     ↓
Rank candidate words
     ↓
Top 5 Suggestions
```

Levenshtein distance counts the character edits needed to change one word into another: insertions, deletions, and substitutions. This implementation compares Unicode code points, so Devanagari characters are handled correctly. A smaller distance means the candidate is closer to the entered word. When distances tie, the word that occurs more often in the corpus is ranked first.

## Example

Example from running the program with `भारत`, `हिनदि`, and `exit`:

```text
================================
       ShuddhAI Spell Checker
================================

Enter a Hindi word (or 'exit'): The word is correct.

Enter a Hindi word (or 'exit'): 
Word not found.

Top 5 suggestions:
1. हिंदी
2. हिंदू
3. हिन्दी
4. हानि
5. हिन्दू

Enter a Hindi word (or 'exit'):
```

Suggestions depend on the corpus and may differ if its contents change.

## Dataset

The program reads `backend/data/hindi_10k.txt` as UTF-8, tokenizes its text, and counts how often each word occurs. These words and frequencies form the vocabulary used for checking and ranking suggestions.

## Technology

- Java
- Java Collections Framework
- UTF-8 and Unicode NFC normalization
- Dynamic programming for Levenshtein distance

## Project Structure

```text
spell-checker/
├── .gitignore
├── README.md
├── ResearchPaper/
│   ├── 2025.coling-main.406.pdf
│   ├── proposal.pdf
│   └── report_context.pdf
└── backend/
    ├── data/
    │   └── hindi_10k.txt
    └── src/
        ├── Main.java
        ├── Tokenizer.java
        ├── Vocabulary.java
        ├── EditDistance.java
        └── SpellChecker.java
```

## Requirements

Java 16 or later is required. The project uses only standard Java APIs and has no external library dependencies.

## Setup and Usage

From the repository root, compile and run:

```bash
javac -encoding UTF-8 -d backend/out backend/src/*.java
java -cp backend/out Main
```

The corpus path is relative to the repository root, so run the commands there. Enter one Hindi word at a time. The program checks the first token entered. If it is in the vocabulary, it prints `The word is correct.` Otherwise, it displays up to five suggestions. Type `exit` to quit.

## Algorithm

1. Read the corpus as UTF-8 text.
2. Normalize text to NFC, retain Devanagari characters, and split it into words.
3. Count each word to build the vocabulary and its frequencies.
4. Normalize and check the user's first input token against the vocabulary.
5. For an unknown word, calculate its Levenshtein distance to each vocabulary word.
6. Sort by smallest distance, then highest corpus frequency, and display up to five words.

## Limitations

- Suggestions are limited to words present in the corpus.
- The comparison is character-based; it does not use word meaning or sentence context.
- Suggestion quality depends on the corpus.
- This is a basic spelling suggestion tool, not a grammar checker.

## Future Improvements

Possible future work includes using a larger corpus, adding context-aware or phonetic suggestions, exploring Hindi morphology, and supporting other Indian languages or a web interface.

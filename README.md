# ShuddhAI

A small Java console spell checker that builds word frequencies from the existing Hindi corpus and suggests nearby words using Levenshtein edit distance.

```text
Hindi Corpus
     ↓
Tokenization
     ↓
Vocabulary
     ↓
User Input
     ↓
Levenshtein Distance
     ↓
Rank Candidates
     ↓
Top 5 Suggestions
```

Words are normalized to Unicode NFC, and edit distance counts Unicode code points. Suggestions are ordered by lowest edit distance, then highest corpus frequency.

## Compile and run

Run from the repository root with Java 17 or newer:

```bash
javac -encoding UTF-8 -d backend/out backend/src/*.java
java -cp backend/out Main
```

The program reads `backend/data/hindi_10k.txt`. Enter one Hindi word at a time; enter `exit` to quit.

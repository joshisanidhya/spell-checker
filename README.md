# ShuddhAI

ShuddhAI is a simple Hindi spelling suggestion system that uses a Hindi corpus, vocabulary, word frequency, and Levenshtein edit distance to suggest the top 5 closest words for a potentially misspelled Hindi word.

## Overview

The Java server loads the included Hindi corpus and builds a word-frequency vocabulary. The browser sends a word to the server. If that word is not in the vocabulary, the server ranks vocabulary words by edit distance and then frequency, and returns up to five suggestions. The interface is written in HTML, CSS, and vanilla JavaScript.

## Features

- Hindi spelling checks with Unicode NFC normalization
- Vocabulary and word frequencies built from the corpus
- Up to five suggestions ranked by Levenshtein distance, then frequency
- Clickable Hindi virtual keyboard, including vowels, consonants, matras, signs, Space, Backspace, and Clear
- Character counter and correct-word or suggestion results
- Java built-in `HttpServer` serves the frontend and spelling endpoint
- `GET /health` health response
- Dockerfile for running the frontend and backend in one container
- Render-compatible `PORT` handling and `0.0.0.0` binding

## Live Demo

Live demo will be added after deployment.

## How It Works

```text
                User
                  |
                  v
        HTML/CSS/JavaScript
                  |
                  | POST /check
                  v
          Java HTTP Server
                  |
                  v
          Hindi Vocabulary  <--- backend/data/hindi_10k.txt
                  |
          +-------+-------+
          |               |
          v               v
      Word Check     Edit Distance
                          |
                          v
                  Candidate Ranking
                          |
                          v
                  Top 5 Suggestions
                          |
                          v
                    Frontend
```

The server checks vocabulary membership directly. For an unknown word, it calculates Levenshtein distance against vocabulary words using Unicode code points. Levenshtein distance is the minimum number of character insertions, deletions, and substitutions needed to transform one word into another. Candidates are sorted by lowest distance; higher-frequency words come first when distances tie. There is no edit-distance cutoff, so the checker returns five suggestions whenever at least five vocabulary words are available.

Vocabulary tokens shorter than three Unicode code points are ignored, and input shorter than three code points is rejected.

## Dataset

The project uses the existing UTF-8 corpus at `backend/data/hindi_10k.txt`. The corpus is not replaced or downloaded at runtime. It provides both the vocabulary candidates and the word frequencies used to break ranking ties.

## Project Structure

```text
spell-checker/
├── .dockerignore
├── .gitignore
├── Dockerfile
├── README.md
├── ResearchPaper/
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

- Java 17 or later
- Docker, if building or running the container

The project uses standard Java APIs and has no external Java or frontend package dependencies.

## Local Run

Run these commands from the repository root. The server reads the corpus and frontend files using repository-relative paths.

Compile:

```bash
javac --add-modules jdk.httpserver -encoding UTF-8 backend/src/*.java
```

Start the server:

```bash
java --add-modules jdk.httpserver -cp backend/src Main
```

Open <http://localhost:8080>. The server uses port `8080` by default and uses the `PORT` environment variable when set. Stop it with Ctrl+C.

## Usage and Routes

The page at `/` includes the Hindi input, virtual keyboard, spelling check button, and results. Enter a word and choose **Check Spelling** (or press Enter). Words in the vocabulary are reported as correct; unknown words receive up to five suggestions. Enter at least three Unicode code points. The virtual keyboard inserts characters at the cursor and also provides Space, Backspace, and Clear.

| Method | Path | Description |
| --- | --- | --- |
| `GET` | `/` | Serves the frontend page |
| `GET` | `/style.css` | Serves frontend styles |
| `GET` | `/script.js` | Serves frontend behavior |
| `GET` | `/health` | Returns `{"status":"ok"}` |
| `POST` | `/check` | Checks a word and returns JSON suggestions |

Example request:

```json
{"word":"हिनदि"}
```

An unknown word returns `correct: false` and a `suggestions` array. A known word returns `correct: true` with an empty suggestions array. The frontend calls `/check` as a relative path, so local and deployed use the same origin.

## Docker

Build the image from the repository root:

```bash
docker build -t shuddhai .
```

Run it locally on port 8080:

```bash
docker run --rm -p 8080:8080 -e PORT=8080 shuddhai
```

Then open <http://localhost:8080>. The image includes the Java sources and compiled classes, frontend files, and Hindi corpus.

## Render Deployment

1. Push the project to GitHub.
2. In Render, choose **New → Web Service** and connect the repository.
3. Select **Docker** as the runtime, use the repository root, and set the Dockerfile path to `./Dockerfile`.
4. Deploy. The Java process serves the frontend and API through the same Render web service.
5. Open the generated `onrender.com` URL shown in Render.

Render supplies the `PORT` environment variable at runtime. The server binds to `0.0.0.0` on that port; the local fallback is `8080`. Render documents these requirements in its [Web Services guide](https://render.com/docs/web-services) and [Docker guide](https://render.com/docs/docker).

## Limitations

- Suggestions can only come from words in the included corpus.
- The algorithm compares spellings character by character; it does not use word meaning or sentence context.
- Suggestion quality depends on the corpus.
- This is a spelling suggestion system, not a grammar checker.

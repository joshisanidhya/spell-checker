# ShuddhAI

ShuddhAI is an explainable Hindi spell checker. It detects words absent from a corpus vocabulary, proposes up to five vocabulary words, and shows the evidence behind each ranking.

## Features

- UTF-8 and NFC-normalized Devanagari preprocessing
- Frequency-based vocabulary built from `backend/data/hindi_10k.txt`
- Unicode code-point Levenshtein distance
- Deterministic candidates within two edits
- Explainable 70% edit similarity and 30% corpus frequency score
- Java REST API and responsive React/Vite interface
- Small TSV evaluation set and Docker Compose deployment

## Architecture

The backend loads the corpus at startup, tokenizes every line, and counts each token. A request is normalized and tokenized; unknown words are compared with the vocabulary, scored, and returned in deterministic top-five order. The frontend calls `POST /api/check` and renders every score component.

## Scoring

For input word w and candidate c:

`EditSimilarity(c) = 1 - distance(w,c) / max(|w|, |c|)`

`FrequencyScore(c) = log(1 + frequency(c)) / log(1 + maximumFrequency)`

`FinalScore(c) = 0.7 x EditSimilarity(c) + 0.3 x FrequencyScore(c)`

Here, lengths are Unicode code-point lengths. Candidate generation uses a maximum edit distance of two and excludes the input word itself.

## API

`POST /api/check`

```json
{"text":"मुझे हिनदि बहुत पसन्द है"}
```

The response contains the original text, each unique unknown word with its zero-based token position, up to five candidates, and processing time in milliseconds. `OPTIONS` is supported for CORS. Invalid or empty requests return HTTP 400; unsupported methods return 405.

## Dataset and evaluation

The project uses the existing UTF-8 corpus at `backend/data/hindi_10k.txt` without modifying it. `backend/data/test_cases.tsv` contains ten small, intentionally misspelled examples in `incorrect<TAB>correct` format. This is a project evaluation sample, not authoritative linguistic ground truth, and no accuracy claim is made from it.

## Run locally

Requirements: Java 17+, Maven, Node.js 20+, and npm.

```bash
cd backend && mvn clean package && cd ..
java -jar backend/target/shuddhai-backend-1.0.0.jar
```

In another terminal:

```bash
cd frontend
npm install
npm run dev
```

Vite serves the UI at `http://localhost:5173`. For a production-like local run:

```bash
docker compose up --build
```

The Compose services expose the API on port 8080 and the Nginx frontend on port 3000.

## Limitations and future work

This first version uses a full vocabulary scan, has no morphological or keyboard-layout model, and treats corpus absence as a spelling signal. Larger corpora could use an indexed candidate structure. Future evaluation should use a reviewed, larger gold set and report precision, recall, top-five accuracy, and latency.

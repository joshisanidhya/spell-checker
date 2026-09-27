import { useState } from 'react';
import Header from './components/Header';
import InputBox from './components/InputBox';
import ErrorResult from './components/ErrorResult';
import { checkSpelling } from './api';

export default function App() {
  const [text, setText] = useState('');
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  async function submit(event) {
    event.preventDefault(); setLoading(true); setError('');
    try { setResult(await checkSpelling(text)); } catch (caught) { setError(caught.message); } finally { setLoading(false); }
  }
  return <><Header /><main><section className="intro"><span className="eyebrow">A transparent language tool</span><h1>Write Hindi with<br /><em>confidence.</em></h1><p>Find likely spelling errors and understand exactly why each correction was recommended.</p></section><InputBox text={text} setText={setText} onSubmit={submit} loading={loading} />{error && <div className="request-error" role="alert">{error}</div>}{result && <ErrorResult result={result} />}<section className="how"><span className="eyebrow">Under the hood</span><h2>Simple methods. Clear reasoning.</h2><div className="steps"><div><b>01</b><h3>Detect</h3><p>Words are checked against the Hindi corpus vocabulary.</p></div><div><b>02</b><h3>Compare</h3><p>Candidate words are compared using edit distance.</p></div><div><b>03</b><h3>Rank</h3><p>Similarity and corpus frequency determine the ranking.</p></div></div></section></main><footer>ShuddhAI <span>·</span> Built for explainable Hindi NLP research</footer></>;
}
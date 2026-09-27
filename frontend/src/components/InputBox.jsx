export default function InputBox({ text, setText, onSubmit, loading }) {
  return <form className="input-panel" onSubmit={onSubmit}>
    <div className="panel-label"><span>Paste Hindi text</span><span>{text.length} characters</span></div>
    <textarea value={text} onChange={(event) => setText(event.target.value)} placeholder="मुझे हिनदि बहुत पसन्द है।" maxLength={5000} aria-label="Hindi text" />
    <div className="input-footer"><span className="hint">Up to 5,000 characters</span><button disabled={!text.trim() || loading}>{loading ? 'Checking...' : 'Check Spelling'}<span aria-hidden="true">↗</span></button></div>
  </form>;
}
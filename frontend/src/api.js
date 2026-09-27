const API_URL = import.meta.env.VITE_API_URL || '/api';

export async function checkSpelling(text) {
  const response = await fetch(`${API_URL}/check`, {
    method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ text })
  });
  const payload = await response.json();
  if (!response.ok) throw new Error(payload.error || 'Unable to check text');
  return payload;
}
const cache = new Map();

function get(key) {
  const entry = cache.get(key);
  if (!entry) {
    return null;
  }

  if (entry.expiresAt < Date.now()) {
    cache.delete(key);
    return null;
  }

  return entry.value;
}

function set(key, value, ttlMs = 15000) {
  cache.set(key, {
    value,
    expiresAt: Date.now() + ttlMs
  });
}

function clearByPrefix(prefix) {
  for (const key of cache.keys()) {
    if (key.startsWith(prefix)) {
      cache.delete(key);
    }
  }
}

module.exports = {
  get,
  set,
  clearByPrefix
};
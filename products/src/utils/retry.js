function sleep(ms) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

async function withRetry(operation, options = {}) {
  const retries = Number.isInteger(options.retries) ? options.retries : 2;
  const delayMs = Number.isFinite(options.delayMs) ? options.delayMs : 300;
  let lastError;

  for (let attempt = 0; attempt <= retries; attempt += 1) {
    try {
      return await operation();
    } catch (error) {
      lastError = error;
      if (attempt < retries) {
        await sleep(delayMs);
      }
    }
  }

  throw lastError;
}

module.exports = {
  withRetry
};
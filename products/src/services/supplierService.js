const config = require("../config");
const { withRetry } = require("../utils/retry");

async function syncFromSupplier() {
  if (!config.supplierApiUrl) {
    return {
      synced: false,
      message: "SUPPLIER_API_URL no esta configurada"
    };
  }

  const execute = async () => {
    const controller = new AbortController();
    const timeout = setTimeout(() => controller.abort(), config.requestTimeoutMs);

    try {
      const response = await fetch(config.supplierApiUrl, {
        method: "GET",
        signal: controller.signal,
        headers: {
          "content-type": "application/json"
        }
      });

      if (!response.ok) {
        throw new Error(`Error de servicio externo: ${response.status}`);
      }

      return await response.json();
    } finally {
      clearTimeout(timeout);
    }
  };

  const data = await withRetry(execute, {
    retries: config.maxRetries,
    delayMs: config.retryDelayMs
  });

  return {
    synced: true,
    supplierPayload: data
  };
}

module.exports = {
  syncFromSupplier
};
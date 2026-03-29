const path = require("path");
const dotenv = require("dotenv");

dotenv.config({ path: path.resolve(process.cwd(), ".env") });

function toNumber(value, fallback) {
  const parsed = Number(value);
  return Number.isFinite(parsed) ? parsed : fallback;
}

module.exports = {
  appName: "catalog-service",
  env: process.env.NODE_ENV || "development",
  port: toNumber(process.env.PORT, 3000),
  logLevel: process.env.LOG_LEVEL || "info",
  supplierApiUrl: process.env.SUPPLIER_API_URL || "",
  requestTimeoutMs: toNumber(process.env.REQUEST_TIMEOUT_MS, 5000),
  maxRetries: toNumber(process.env.MAX_RETRIES, 2),
  retryDelayMs: toNumber(process.env.RETRY_DELAY_MS, 300)
};
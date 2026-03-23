const app = require("./app");
const config = require("./config");
const logger = require("./logger");

const server = app.listen(config.port, () => {
  logger.info({ port: config.port }, "Catalog service running");
});

function shutdown(signal) {
  // Cierre limpio para despliegues y reinicios
  logger.warn({ signal }, "Shutdown signal received");
  server.close(() => {
    logger.info("Server closed");
    process.exit(0);
  });

  setTimeout(() => {
    logger.error("Forced shutdown");
    process.exit(1);
  }, 8000).unref();
}

process.on("SIGTERM", () => shutdown("SIGTERM"));
process.on("SIGINT", () => shutdown("SIGINT"));
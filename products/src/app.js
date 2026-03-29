const express = require("express");
const pinoHttp = require("pino-http");
const catalogRoutes = require("./routes/catalogRoutes");
const logger = require("./logger");

const app = express();

app.use(express.json({ limit: "1mb" }));
app.use(pinoHttp({ logger }));

app.get("/health", (_req, res) => {
  res.json({
    ok: true,
    service: "catalog-service"
  });
});

app.get("/", (_req, res) => {
  res.json({
    service: "catalog-service",
    docs: {
      health: "GET /health",
      products: "GET /products",
      categories: "GET /categories"
    }
  });
});

app.use("/", catalogRoutes);

// Manejador central para no exponer detalles internos
app.use((err, _req, res, _next) => {
  logger.error({ err }, "Unhandled error");
  res.status(500).json({
    error: "INTERNAL_ERROR",
    message: "Error interno en el microservicio"
  });
});

module.exports = app;
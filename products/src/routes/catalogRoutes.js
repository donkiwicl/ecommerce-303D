const express = require("express");
const repository = require("../repositories/productsRepository");
const supplierService = require("../services/supplierService");
const cacheService = require("../services/cacheService");

const router = express.Router();

// GET /categories - GET /products/:id (PRD-001) - POST /products - POST /products/sync/external

router.get("/products", (req, res) => {
  const query = {
    q: req.query.q || "",
    category: req.query.category || ""
  };

  const key = `products:list:${query.q}:${query.category}`;
  const cached = cacheService.get(key);
  if (cached) {
    return res.json({
      cached: true,
      count: cached.length,
      items: cached
    });
  }

  const items = repository.list(query);
  cacheService.set(key, items, 15000);

  return res.json({
    cached: false,
    count: items.length,
    items
  });
});

router.get("/products/:id", (req, res) => {
  const key = `products:item:${req.params.id}`;
  const cached = cacheService.get(key);
  if (cached) {
    return res.json({
      cached: true,
      item: cached
    });
  }

  const item = repository.getById(req.params.id);
  if (!item) {
    return res.status(404).json({ error: "PRODUCT_NOT_FOUND" });
  }

  cacheService.set(key, item, 15000);

  return res.json({
    cached: false,
    item
  });
});

router.get("/categories", (_req, res) => {
  const key = "products:categories";
  const cached = cacheService.get(key);
  if (cached) {
    return res.json({
      cached: true,
      items: cached
    });
  }

  const items = repository.categories();
  cacheService.set(key, items, 15000);

  return res.json({
    cached: false,
    items
  });
});

router.post("/products", (req, res) => {
  const { id, name, category, imageUrl, stock, price } = req.body;

  // Validacion basica del payload
  if (
    !id ||
    !name ||
    !category ||
    !imageUrl ||
    !Number.isFinite(Number(stock)) ||
    !Number.isFinite(Number(price))
  ) {
    return res.status(400).json({
      error: "INVALID_PAYLOAD",
      message: "id, name, category, imageUrl, stock y price son obligatorios"
    });
  }

  const item = repository.upsert({ id, name, category, imageUrl, stock, price });

  // Limpieza de cache para mantener consistencia
  cacheService.clearByPrefix("products:");

  return res.status(201).json(item);
});

router.post("/products/sync/external", async (_req, res, next) => {
  try {
    // Reintentos basicos para servicio externo
    const result = await supplierService.syncFromSupplier();
    return res.json(result);
  } catch (error) {
    return next(error);
  }
});

module.exports = router;
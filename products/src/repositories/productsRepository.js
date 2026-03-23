const products = new Map([
  ["PRD-001", {
    id: "PRD-001",
    name: "Polera blanca",
    category: "Ropa",
    imageUrl: "../../images/9775.png",
    stock: 40,
    price: 19.9
  }],
  ["PRD-002", {
    id: "PRD-002",
    name: "Polera roja",
    category: "Ropa",
    imageUrl: "../../images/9776.png",
    stock: 25,
    price: 39.9
  }],
  ["PRD-003", {
    id: "PRD-003",
    name: "Polera verde",
    category: "Ropa",
    imageUrl: "../../images/9777.png",
    stock: 12,
    price: 69.9
  }]
]);

function list(filters = {}) {
  const q = (filters.q || "").toLowerCase();
  const category = (filters.category || "").toLowerCase();
  const source = [...products.values()];

  return source.filter((item) => {
    const byText = !q || item.name.toLowerCase().includes(q);
    const byCategory = !category || item.category.toLowerCase() === category;
    return byText && byCategory;
  });
}

function getById(id) {
  return products.get(id) || null;
}

function upsert(item) {
  const normalized = {
    id: item.id,
    name: item.name,
    category: item.category,
    imageUrl: item.imageUrl,
    stock: Number(item.stock),
    price: Number(item.price)
  };

  products.set(normalized.id, normalized);
  return normalized;
}

function categories() {
  return [...new Set([...products.values()].map((item) => item.category))];
}

module.exports = {
  list,
  getById,
  upsert,
  categories
};
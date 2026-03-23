# Documento Técnico - Implementación de Estándares Éticos

## 1. Seguridad - Implementación JWT

### Instalación
```bash
npm install jsonwebtoken express-jwt bcryptjs
```

### Middleware de Autenticación

```javascript
// middleware/auth.js
const jwt = require('jsonwebtoken');

const verifyToken = (req, res, next) => {
  const token = req.headers['authorization']?.split(' ')[1];
  
  if (!token) {
    return res.status(401).json({ error: 'Token requerido' });
  }

  try {
    const decoded = jwt.verify(token, process.env.JWT_SECRET);
    req.user = decoded;
    next();
  } catch (err) {
    res.status(403).json({ error: 'Token inválido' });
  }
};

module.exports = { verifyToken };
```

### RBAC - Control de Acceso

```javascript
// middleware/rbac.js
const authMiddleware = {
  ADMIN: (req, res, next) => {
    if (req.user.role !== 'admin') {
      return res.status(403).json({ error: 'Acceso denegado' });
    }
    next();
  },

  USER: (req, res, next) => {
    if (!['admin', 'user'].includes(req.user.role)) {
      return res.status(403).json({ error: 'Acceso denegado' });
    }
    next();
  }
};

module.exports = authMiddleware;
```

### Aplicar en Rutas

```javascript
// routes/catalogRoutes.js
const express = require('express');
const { verifyToken } = require('../middleware/auth');
const rbac = require('../middleware/rbac');

const router = express.Router();

// Público
router.get('/products', getProducts);

// Protegido
router.get('/products/:id', verifyToken, rbac.USER, getProductById);

// Solo admin
router.post('/products', verifyToken, rbac.ADMIN, createProduct);

module.exports = router;
```

---

## 2. Privacidad - GDPR Compliance

### Redacción de Logs

```javascript
// logger.js
const pino = require('pino');

const logger = pino({
  redact: {
    paths: ['email', 'phone', 'password', 'creditCard'],
    censor: '***REDACTED***'
  }
});

module.exports = logger;
```

### Endpoint GDPR Art. 17 - Derecho al Olvido

```javascript
// routes/privacyRoutes.js
router.delete('/my-data', verifyToken, async (req, res) => {
  try {
    const userId = req.user.id;

    const deleteRequest = {
      userId,
      requestDate: new Date(),
      status: 'pending',
      dueDate: new Date(Date.now() + 30 * 24 * 60 * 60 * 1000)
    };

    await deleteRequestRepository.create(deleteRequest);

    logger.info({
      event: 'deletion_requested',
      userId,
      timestamp: new Date()
    });

    res.json({
      message: 'Solicitud procesada. Se eliminará en máximo 30 días.',
      dueDate: deleteRequest.dueDate
    });
  } catch (err) {
    res.status(500).json({ error: 'Error procesando solicitud' });
  }
});
```

---

## 3. Validación de Entrada

### Instalar Joi

```bash
npm install joi
```

### Schema de Producto

```javascript
// validation/productSchema.js
const Joi = require('joi');

const productSchema = Joi.object({
  name: Joi.string().alphanum().min(3).max(50).required(),
  price: Joi.number().positive().precision(2).required(),
  stock: Joi.number().integer().min(0).required(),
  category: Joi.string().valid('electronics', 'clothing').required()
});

module.exports = productSchema;
```

### Middleware de Validación

```javascript
// middleware/validate.js
const validate = (schema) => {
  return (req, res, next) => {
    const { error, value } = schema.validate(req.body);

    if (error) {
      return res.status(400).json({
        error: 'Validación fallida',
        details: error.details
      });
    }

    req.validatedData = value;
    next();
  };
};

module.exports = validate;
```

---

## 4. Rate Limiting

### Instalación

```bash
npm install express-rate-limit
```

### Implementación

```javascript
// middleware/rateLimiter.js
const rateLimit = require('express-rate-limit');

const limiter = rateLimit({
  windowMs: 15 * 60 * 1000,
  max: 100,
  message: 'Demasiadas solicitudes'
});

module.exports = { limiter };
```

### Usar en App

```javascript
// app.js
const { limiter } = require('./middleware/rateLimiter');

app.use('/api/', limiter);
```

---

## 5. Health Check

```javascript
// routes/healthRoutes.js
const express = require('express');
const router = express.Router();

router.get('/health', (req, res) => {
  const health = {
    status: 'UP',
    timestamp: new Date(),
    uptime: process.uptime(),
    memory: {
      used: Math.round(process.memoryUsage().heapUsed / 1024 / 1024),
      total: Math.round(process.memoryUsage().heapTotal / 1024 / 1024)
    }
  };

  res.json(health);
});

module.exports = router;
```

---

## 6. Variables de Entorno

```env
NODE_ENV=production
JWT_SECRET=tu_secret_super_seguro_32_chars_minimo
DATABASE_URL=postgresql://user:pass@localhost:5432/ecommerce
LOG_LEVEL=info
LOG_RETENTION_DAYS=30
RATE_LIMIT_WINDOW_MS=900000
RATE_LIMIT_MAX_REQUESTS=100
```

---

## 7. Docker

```dockerfile
FROM node:20-alpine

WORKDIR /app

COPY package*.json ./
RUN npm ci --only=production

COPY src ./src

USER node

EXPOSE 3000

CMD ["node", "src/index.js"]
```

---

## 8. Testing de Seguridad

```bash
npm install --save-dev jest supertest
```

```javascript
// __tests__/auth.test.js
const request = require('supertest');
const app = require('../src/app');

describe('Authentication', () => {
  test('Debe rechazar request sin token', async () => {
    const res = await request(app)
      .get('/api/products/1')
      .expect(401);
  });
});
```

---

## Checklist de Implementación

- [ ] JWT implementado
- [ ] RBAC con roles
- [ ] Validación con Joi
- [ ] Rate limiting activo
- [ ] Logs redactados
- [ ] GDPR endpoints
- [ ] Health check
- [ ] Tests de seguridad
- [ ] Docker configurado
- [ ] Variables de entorno

---

*Documento generado: 23/03/2026*

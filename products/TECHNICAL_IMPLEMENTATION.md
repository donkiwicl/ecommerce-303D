# Implementacion Tecnica - Etica en Arquitectura

## 1. JWT - Autenticacion

```bash
npm install jsonwebtoken express-jwt bcryptjs
```

```javascript
// middleware/auth.js
const jwt = require('jsonwebtoken');

const verifyToken = (req, res, next) => {
  const token = req.headers['authorization']?.split(' ')[1];
  if (!token) return res.status(401).json({ error: 'Token requerido' });

  try {
    const decoded = jwt.verify(token, process.env.JWT_SECRET);
    req.user = decoded;
    next();
  } catch (err) {
    res.status(403).json({ error: 'Token invalido' });
  }
};

module.exports = { verifyToken };
```

---

## 2. RBAC - Control de Acceso

```javascript
// middleware/rbac.js
const rbac = {
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
  },

  PUBLIC: (req, res, next) => next()
};

module.exports = rbac;
```

Usar en rutas:
```javascript
router.get('/products', rbac.PUBLIC, getProducts);
router.post('/products', verifyToken, rbac.ADMIN, createProduct);
```

---

## 3. Validacion - Joi

```bash
npm install joi
```

```javascript
// validation/productSchema.js
const Joi = require('joi');

const productSchema = Joi.object({
  name: Joi.string().alphanum().min(3).max(50).required(),
  price: Joi.number().positive().required(),
  stock: Joi.number().integer().min(0).required(),
  category: Joi.string().valid('electronics', 'clothing', 'books').required()
});

module.exports = productSchema;
```

Middleware:
```javascript
const validate = (schema) => (req, res, next) => {
  const { error, value } = schema.validate(req.body);
  if (error) return res.status(400).json({ error: error.details });
  req.validatedData = value;
  next();
};
```

---

## 4. Logs Redactados

```javascript
// logger.js
const pino = require('pino');

const logger = pino({
  redact: {
    paths: ['email', 'phone', 'password', 'creditCard'],
    censor: '***REDACTADO***'
  }
});

module.exports = logger;
```

---

## 5. GDPR - Derecho al Olvido

```javascript
// routes/privacyRoutes.js
router.delete('/my-data', verifyToken, async (req, res) => {
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
    message: 'Solicitud procesada. Eliminacion en max 30 dias.',
    dueDate: deleteRequest.dueDate
  });
});
```

Acceso a datos:
```javascript
router.get('/my-data', verifyToken, async (req, res) => {
  const userId = req.user.id;
  const userData = await userRepository.getAllData(userId);
  res.json(userData);
});
```

---

## 6. Rate Limiting

```bash
npm install express-rate-limit
```

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

En app.js:
```javascript
app.use('/api/', limiter);
```

---

## 7. Health Check

```javascript
// routes/health.js
router.get('/health', (req, res) => {
  res.json({
    status: 'UP',
    timestamp: new Date(),
    uptime: process.uptime(),
    memory: {
      used: Math.round(process.memoryUsage().heapUsed / 1024 / 1024),
      total: Math.round(process.memoryUsage().heapTotal / 1024 / 1024)
    }
  });
});
```

---

## 8. Variables de Entorno

```env
NODE_ENV=production
JWT_SECRET=tu_secret_minimo_32_caracteres
DATABASE_URL=postgresql://user:pass@localhost:5432/db
LOG_LEVEL=info
RATE_LIMIT_WINDOW_MS=900000
RATE_LIMIT_MAX_REQUESTS=100
```

---

## 9. Testing

```bash
npm install --save-dev jest supertest
```

```javascript
// __tests__/auth.test.js
const request = require('supertest');
const app = require('../src/app');

describe('Authentication', () => {
  test('rechaza request sin token', async () => {
    const res = await request(app)
      .get('/api/products/1')
      .expect(401);
    expect(res.body.error).toBe('Token requerido');
  });
});
```

```bash
npm test
npm run test:security
npm run test:coverage
```

---

## 10. Checklist de Implementacion

- [ ] JWT middleware implementado
- [ ] RBAC en todas las rutas
- [ ] Validacion Joi en entrada
- [ ] Rate limiting activo
- [ ] Logs redactados automaticamente
- [ ] Endpoints GDPR funcionando
- [ ] Health check disponible
- [ ] Tests de seguridad >80%
- [ ] Variables de entorno configuradas
- [ ] Documentacion actualizada


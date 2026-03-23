# Plantilla de Presentación PPT - Ética en Arquitectura

## Slide 1: Portada
# Ética del Diseño de Arquitecturas
### Microservicio de Catálogo de Productos - E-commerce 303D
Autor: Diego Duoc | Fecha: Marzo 2026

---

## Slide 2: Tabla de Contenidos
- 📋 Introducción
- 🔐 Seguridad
- 🛡️ Privacidad
- 🌱 Sostenibilidad
- ⚠️ Riesgos Identificados
- ✅ Recomendaciones
- 🎯 Conclusiones

---

## Slide 3: Introducción
### ¿Por qué importa la ética en arquitectura?

**Responsabilidades:**
- Los datos son responsabilidad ética
- Las arquitecturas moldean el comportamiento
- El impacto ambiental es medible
- La confianza es fundamental

---

## Slide 4: Stack Tecnológico

**Backend:** Node.js + Express
**Auth:** JWT (Por implementar)
**Logs:** Pino + @pinojs/redact
**Orquestación:** Kubernetes + HPA
**Base de Datos:** PostgreSQL (TBD)

---

## Slide 5: Pilares de Ética

```
       ÉTICA ARQUITECTÓNICA
            |
    ________|________
   |        |        |
SEGURIDAD | PRIVACIDAD | SOSTENIBILIDAD
```

---

## Slide 6: Dimensión de Seguridad

**Autenticación & Acceso:**
- ❌ Sin autenticación actual
- ✅ Logger auditado
- 🔄 Implementar JWT + RBAC

**Riesgos:** Inyección SQL, XSS, Acceso no autorizado

---

## Slide 7: Dimensión de Privacidad

**Protecciones:**
- ✅ Variables de entorno
- ✅ Logs redactados

**Falta Implementar:**
- GDPR: Derecho al olvido
- Política de retención
- Consentimiento explícito

---

## Slide 8: Dimensión de Sostenibilidad

**Implementado:**
- ✅ Caching
- ✅ HPA (Auto-scaling)

**Mejoras:**
- Monitoreo de recursos
- Métricas de CO2
- Optimización de queries

---

## Slide 9: Matriz de Riesgos

| Riesgo | Impacto | Probabilidad |
|--------|---------|--------------|
| Acceso no autorizado | CRÍTICO | MEDIA |
| Exposición de datos | CRÍTICO | BAJA |
| Consumo excesivo | MEDIO | MEDIA |

---

## Slide 10: Recomendaciones

### Priority 1 (Inmediato)
- JWT + RBAC
- Validación de entrada
- Rate limiting

### Priority 2 (Corto Plazo)
- Auditoría de logs
- Documentar privacidad
- HTTPS/TLS

---

## Slide 11: Plan de Implementación

**Sprint 1-2:** Autenticación y Validación
**Sprint 3-4:** Privacidad y Auditoría
**Sprint 5-6:** Monitoreo y Certificación

---

## Slide 12: Conclusiones

**Status:** Requiere implementación completa

**Próximos pasos:**
1. Aprobación del plan
2. Asignación de recursos
3. Ejecución por sprints

---

*Convertir a PowerPoint:*
```bash
pandoc PRESENTATION_TEMPLATE.md -t pptx -o presentation.pptx
```

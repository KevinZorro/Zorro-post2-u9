# Zorro-post2-u9 — Pruebas de Integración + CI/CD con GitHub Actions

![CI](https://github.com/Kevinzorro/Zorro-post2-u9/actions/workflows/ci.yml/badge.svg)

Microservicio de gestión de productos con suite completa de **pruebas de
integración** usando `@DataJpaTest` y `@WebMvcTest`, más un pipeline de
**Integración Continua** con GitHub Actions que ejecuta las pruebas y genera
reporte de cobertura JaCoCo en cada push.

### Evidencias:

<img width="950" height="289" alt="image" src="https://github.com/user-attachments/assets/dc5ded2f-7dee-4a73-bf7a-aecf004896d1" />

<img width="654" height="463" alt="image" src="https://github.com/user-attachments/assets/d0741d04-9c35-4d21-a84e-79951ec22af8" />

<img width="1129" height="129" alt="image" src="https://github.com/user-attachments/assets/2ee5abc5-bfcc-4e68-8b17-ffdb28321c01" />

<img width="976" height="332" alt="image" src="https://github.com/user-attachments/assets/7d78836d-60ff-44e5-8b2e-8068f34d7db2" />

---

## Estructura del Proyecto
Zorro-post2-u9/
├── .github/
│ └── workflows/
│ └── ci.yml ← Pipeline CI/CD
├── src/
│ ├── main/java/com/universidad/productosservice/
│ │ ├── ProductosServiceApplication.java
│ │ ├── domain/
│ │ │ └── Producto.java ← @Entity JPA
│ │ ├── repository/
│ │ │ └── ProductoRepository.java ← JpaRepository<Producto,Long>
│ │ ├── service/
│ │ │ ├── ProductoService.java ← Interfaz
│ │ │ └── ProductoServiceImpl.java ← @Service con validaciones
│ │ └── controller/
│ │ ├── ProductoController.java ← @RestController
│ │ └── GlobalExceptionHandler.java ← @RestControllerAdvice
│ ├── main/resources/
│ │ └── application.properties
│ └── test/
│ ├── java/com/universidad/productosservice/
│ │ ├── repository/
│ │ │ └── ProductoRepositoryTest.java ← @DataJpaTest (4 pruebas)
│ │ ├── controller/
│ │ │ └── ProductoControllerTest.java ← @WebMvcTest (3 pruebas)
│ │ └── service/
│ │ └── ProductoServiceImplTest.java ← @ExtendWith(Mockito) (7 pruebas)
│ └── resources/
│ └── application-test.properties
└── README.md

text

---

## Suite de Pruebas

### `@DataJpaTest` — `ProductoRepositoryTest`

| Prueba | Qué verifica |
|---|---|
| `save_asignaIdAutomaticamente` | `id > 0` tras persistir |
| `findById_existente_retornaProducto` | Nombre correcto al recuperar por ID |
| `findAll_retornaListaCompleta` | Lista con exactamente 2 productos |
| `deleteById_eliminaProducto` | `findById` retorna vacío tras borrar |

`@BeforeEach` llama `deleteAll()` para garantizar aislamiento entre pruebas.

### `@WebMvcTest` — `ProductoControllerTest`

| Prueba | Qué verifica |
|---|---|
| `listarProductos_retorna200ConLista` | `200 OK` + `$.length() == 2` + primer nombre |
| `crearProducto_datosValidos_retorna201` | `201 Created` + `$.id` + `$.nombre` |
| `buscarProducto_noExistente_retorna404` | `404 Not Found` via `@RestControllerAdvice` |

### `@ExtendWith(MockitoExtension)` — `ProductoServiceImplTest`

| Prueba | Tipo |
|---|---|
| `crear_datosValidos_retornaProductoGuardado` | Happy path |
| `buscarPorId_existente_retornaProducto` | Happy path |
| `buscarPorId_noExistente_lanzaRuntimeException` | Error |
| `crear_nombreInvalido_*` | `@ParameterizedTest` — 5 iteraciones |
| `crear_precioInvalido_*` | `@ParameterizedTest` — 4 iteraciones |
| `crear_nombreConEspacios_guardaNombreNormalizado` | `ArgumentCaptor` |
| `eliminar_productoExistente_llamaDeleteById` | `verify()` |

**Total: ≥ 14 ejecuciones, 0 fallos.**

---

## Pipeline CI/CD — GitHub Actions

El archivo `.github/workflows/ci.yml` se ejecuta automáticamente en cada
`push` y `pull_request` a `main`

---

## Reporte de Cobertura JaCoCo

> `ProductoServiceImpl` alcanza ≥ **70% de cobertura de líneas**.

![Captura JaCoCo](docs/jacoco-report.png)

Para generar el reporte localmente:

```bash
mvn verify
# Abrir en el navegador:
start target/site/jacoco/index.html        # Windows
open  target/site/jacoco/index.html        # macOS
xdg-open target/site/jacoco/index.html    # Linux
```

---

## Requisitos

- Java 21+
- Maven 3.9+

---

## Ejecutar las pruebas

```bash
mvn test      # Solo pruebas (sin reporte JaCoCo)
mvn verify    # Pruebas + reporte JaCoCo completo
```

Resultado esperado:
[INFO] Tests run: 14, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS

text

---

## Checkpoints Verificados

- [x] `ProductoRepositoryTest` — 4 pruebas `@DataJpaTest` en verde
- [x] `@BeforeEach deleteAll()` garantiza aislamiento entre pruebas de repositorio
- [x] `ProductoControllerTest` — 3 pruebas `@WebMvcTest` en verde
- [x] `buscarProducto_noExistente_retorna404` → `@RestControllerAdvice` retorna `404`
- [x] `mvn verify` genera `target/site/jacoco/index.html` con cobertura ≥70%
- [x] `.github/workflows/ci.yml` configurado correctamente
- [x] Pipeline GitHub Actions termina con estado **✅ Success**
- [x] Badge del workflow muestra verde en el README
- [x] Artefacto `jacoco-report` disponible en la pestaña Actions de GitHub

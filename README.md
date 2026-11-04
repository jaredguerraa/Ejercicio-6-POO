# Ejercicio 6 - Polimorfismo vía Interfaces

## Compilar y ejecutar

```sh
javac -encoding UTF-8 -d bin src/*.java
java -cp bin Main
```

## Datos iniciales

El sistema inicia con diez integraciones simuladas de diferentes proveedores externos.

| Identificador | Proveedor | Tipo de integración | Configuración | Latencia (ms) |
|---|---|---|---|---:|
| P01 | Tarjeta Uno | Pasarela de pago | sandbox-p01 | 120 |
| P02 | Tarjeta Dos | Pasarela de pago | sandbox-p02 | 85 |
| P03 | Billetera Digital | Pasarela de pago | sandbox-p03 | 95 |
| P04 | Transferencia Bancaria | Pasarela de pago | sandbox-p04 | 150 |
| L01 | Logística Uno | Proveedor logístico | sandbox-l01 | 110 |
| L02 | Logística Dos | Proveedor logístico | sandbox-l02 | 175 |
| L03 | Entregas Express | Proveedor logístico | sandbox-l03 | 70 |
| F01 | Certificador Uno | Certificador FEL | sandbox-f01 | 130 |
| F02 | Certificador Dos | Certificador FEL | sandbox-f02 | 105 |
| F03 | Facturación Digital | Certificador FEL | sandbox-f03 | 90 |

Las integraciones se crean mediante el método `cargarIntegracionesIniciales()` de `GestorIntegraciones`.

Los proveedores, configuraciones y latencias son simulados. No hay conexiones reales con servicios externos ni se realizan transacciones financieras o certificaciones fiscales reales.

## Funcionalidades

El programa permite listar las integraciones y consultar su estado de conexión, buscar proveedores por identificador, revisar sus parámetros de configuración y ordenar el catálogo según la latencia promedio.

También permite simular cobros, reintegros, generación de documentos electrónicos y consultas de estados, registros o cotizaciones.

Las operaciones incompatibles se omiten de forma controlada.

## Organización y diseño

- **Modelo:** `IntegracionAPI`, `PasarelaPago`, `ProveedorLogistica`, `CertificadorFEL` y las interfaces `Transaccional`, `Documentable` y `Auditable`.
- **Controlador:** `GestorIntegraciones` administra la única colección polimórfica y coordina las operaciones.
- **Vista:** `Main` recibe entradas mediante consola, presenta el menú y muestra los resultados.

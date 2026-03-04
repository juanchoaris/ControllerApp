# Controller App

Aplicación Android desarrollada en Kotlin con arquitectura limpia siguiendo principios SOLID.

## 📋 Características

### Capa de Seguridad
- **Control de Versiones**: Verifica la versión de la aplicación contra el servidor
- **Autenticación**: Sistema de login con almacenamiento local de credenciales

### Capa de Datos
- **Base de Datos Local**: SQLite mediante Room Database
- **Sincronización**: Obtiene y almacena esquema de tablas desde API

### Capa de Presentación
- **Pantalla Login**: Autenticación y control de versiones
- **Pantalla Home**: Información del usuario con navegación
- **Pantalla Tablas**: Visualización de tablas del esquema
- **Pantalla Localidades**: Listado de localidades de recogida

## 🏗️ Arquitectura

La aplicación sigue una arquitectura limpia con separación de capas:

```
app/
├── data/
│   ├── local/          # Base de datos Room
│   │   ├── dao/        # Data Access Objects
│   │   ├── entity/     # Entidades de BD
│   │   └── database/   # Configuración de la BD
│   ├── remote/         # API y modelos de red
│   │   ├── api/        # Servicios Retrofit
│   │   └── model/      # Modelos de respuesta
│   └── repository/     # Repositorios (patrón Repository)
├── presentation/       # UI y ViewModels
│   ├── login/
│   ├── home/
│   ├── tablas/
│   └── localidades/
└── utils/              # Utilidades y constantes
```

## 🔧 Tecnologías y Librerías

- **Lenguaje**: Kotlin 1.9.20
- **Build System**: Gradle (KTS)
- **Arquitectura**: MVVM + Repository Pattern
- **Base de Datos**: Room 2.6.1
- **Networking**: Retrofit 2.9.0 + OkHttp 4.12.0
- **JSON**: Gson 2.10.1
- **Async**: Kotlin Coroutines 1.7.3
- **UI**: Material Components 1.11.0, ViewBinding
- **Lifecycle**: ViewModel, LiveData, Lifecycle Runtime 2.7.0

## 🎭 Modo Mock (Híbrido)

La aplicación incluye un **sistema de simulación selectivo** que mockea solo los endpoints con problemas mientras usa los servicios reales para los que funcionan correctamente.

### Configuración Actual

**Endpoints MOCKEADOS** (simulados):
- ❌ **Login** (`AuthenticaUsuarioApp`) - El servidor tiene problemas
- ❌ **Esquema** (`ObtenerEsquema`) - El servidor tiene problemas

**Endpoints REALES** (conectan al servidor):
- ✅ **Versión** (`ConsultarParametrosFramework/VPStoreAppControl`) - Funciona correctamente
- ✅ **Localidades** (`ObtenerLocalidadesRecogidas`) - Funciona correctamente

### Cómo Activar/Desactivar el Modo Mock

1. Abrir el archivo [Constants.kt](app/src/main/java/com/example/controllerapp/utils/Constants.kt)
2. Cambiar la constante `MOCK_MODE_ENABLED`:
   ```kotlin
   const val MOCK_MODE_ENABLED = true  // Mock solo endpoints con problemas
   // o
   const val MOCK_MODE_ENABLED = false // Intentar usar todos los servicios reales
   ```

### Respuestas Simuladas

Cuando el modo mock está activo, estos endpoints retornan datos simulados:

**Login (MOCK)**:
- Usuario: pam.meredy21
- Identificación: 987204545  
- Nombre: MEREDY PAOLA ACERO MULCUE
- Token simulado
- Mensaje incluye "(MOCK)"

**Esquema (MOCK)**:
- 3 tablas de ejemplo: Clientes, Pedidos, Productos
- Con campos apropiados para cada tabla
- Mensaje incluye "(MOCK)"

### Ventajas del Modo Híbrido

✅ Usa servicios reales cuando funcionan  
✅ Simula solo los endpoints con problemas  
✅ Datos reales de versión y localidades desde el servidor  
✅ Permite desarrollo continuo sin bloqueos  
✅ Fácil migración: cuando los endpoints se reparen, solo desactiva el mock

**Nota**: Los mensajes en modo mock incluyen el sufijo "(MOCK)" para identificar fácilmente qué datos son simulados y cuáles vienen del servidor real.

## 📱 Principios SOLID Implementados

### Single Responsibility Principle (SRP)
- Cada clase tiene una única responsabilidad
- ViewModels solo manejan lógica de presentación
- Repositorios solo manejan acceso a datos
- DAOs solo manejan operaciones de base de datos

### Open/Closed Principle (OCP)
- Extensible mediante interfaces
- Clases selladas para manejo de estados (Result)
- Adaptadores reutilizables con DiffUtil

### Liskov Substitution Principle (LSP)
- Interfaces bien definidas para repositorios
- ViewModels intercambiables mediante factories

### Interface Segregation Principle (ISP)
- Interfaces específicas para cada servicio
- DAOs segregados por entidad

### Dependency Inversion Principle (DIP)
- Dependencias inyectadas mediante constructores
- ViewModels dependen de abstracciones (repositorios)
- Uso de factories para crear dependencias

## 🛡️ Manejo de Errores

### Try/Catch
Todas las operaciones críticas están protegidas:
```kotlin
try {
    val response = apiService.getAppVersion()
    // Procesar respuesta
} catch (e: Exception) {
    Result.Error(e, NetworkExceptionHandler.handleException(e))
}
```

### Manejo de Errores de API
- Verificación de códigos HTTP
- Manejo específico de errores de red (timeout, conectividad)
- Mensajes descriptivos para el usuario
- Clase centralizada `NetworkExceptionHandler`

### Result Pattern
```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception, val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
```

## 📡 Endpoints Consumidos

### 1. Control de Versiones
```
GET https://apitesting.interrapidisimo.co/apicontrollerpruebas/api/ParametrosFramework/ConsultarParametrosFramework/VPStoreAppControl
```

### 2. Autenticación
```
POST https://apitesting.interrapidisimo.co/FtEntregaElectronica/MultiCanales/ApiSeguridadPruebas/api/Seguridad/AuthenticaUsuarioApp

Headers:
- Usuario: pam.meredy21
- Identificacion: 987204545
- Accept: text/json
- IdUsuario: pam.meredy21
- IdCentroServicio: 1295
- NombreCentroServicio: PTO/BOGOTA/CUND/COL/OF PRINCIPAL - CRA 30 # 7-45
- IdAplicativoOrigen: 9
- Content-Type: application/json

Body:
{
  "Mac": "",
  "NomAplicacion": "Controller APP",
  "Password": "SW50ZXIyMDIx\n",
  "Path": "",
  "Usuario": "cGFtLm1lcmVkeTIx\n"
}
```

### 3. Esquema de Datos
```
GET https://apitesting.interrapidisimo.co/apicontrollerpruebas/api/SincronizadorDatos/ObtenerEsquema/true
```

### 4. Localidades
```
GET https://apitesting.interrapidisimo.co/apicontrollerpruebas/api/ParametrosFramework/ObtenerLocalidadesRecogidas
```

## 🗄️ Base de Datos Local

### Tablas

#### user_info
- `usuario` (PRIMARY KEY)
- `identificacion`
- `nombre`

#### schema_tables
- `id` (PRIMARY KEY, AUTO INCREMENT)
- `tableName`
- `tableDescription`
- `tableData` (JSON)

## 🚀 Instalación y Ejecución

### Requisitos Previos
- Android Studio Hedgehog (2023.1.1) o superior
- JDK 17
- Android SDK 34
- Kotlin Plugin 1.9.20

### Pasos
1. Abrir el proyecto en Android Studio
2. Sincronizar Gradle
3. Ejecutar en emulador o dispositivo físico (API 24+)

### Comandos Gradle
```bash
# Compilar proyecto
./gradlew build

# Correr tests
./gradlew test

# Generar APK
./gradlew assembleDebug

# Instalar en dispositivo
./gradlew installDebug
```

## 📖 Flujo de la Aplicación

1. **Inicio (Login)**
   - Verifica versión de la aplicación
   - Muestra mensaje si la versión difiere
   - Permite autenticación al presionar botón
   - Al autenticar exitosamente, obtiene esquema de datos
   - Guarda información del usuario localmente
   - Navega a pantalla Home

2. **Home**
   - Muestra datos del usuario autenticado
   - Botón "TABLAS" → navega a lista de tablas
   - Botón "LOCALIDADES" → navega a lista de localidades

3. **Tablas**
   - Muestra tablas almacenadas localmente del esquema
   - Cada item muestra nombre y descripción

4. **Localidades**
   - Consulta API en tiempo real
   - Muestra abreviación de ciudad y nombre completo
   - Maneja errores de conexión

## 📝 Documentación del Código

Todo el código está documentado con:
- KDoc para clases y funciones públicas
- Comentarios explicativos de lógica compleja
- Anotaciones de principios SOLID aplicados

## ⚠️ Manejo de Estados HTTP

- **200 OK**: Operación exitosa
- **401 Unauthorized**: Credenciales inválidas
- **403 Forbidden**: Acceso prohibido
- **404 Not Found**: Recurso no encontrado
- **500+ Server Error**: Error del servidor

## 🔒 Seguridad

- Credenciales codificadas en Base64
- Comunicación HTTPS
- Timeout configurado para evitar bloqueos
- Validación de respuestas antes de procesar

## 🧪 Testing

El proyecto está preparado para:
- Unit Tests (JUnit)
- Integration Tests
- UI Tests (Espresso)

## 📄 Licencia

Proyecto de demostración para prueba técnica.

## 👨‍💻 Autor

Desarrollado siguiendo las mejores prácticas de Android y principios SOLID.

---

**Versión**: 1.0.0  
**Última actualización**: 2026-02-26

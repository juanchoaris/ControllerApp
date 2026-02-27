# 🚀 GUÍA RÁPIDA DE EJECUCIÓN - Controller App

## Requisitos Previos

Para ejecutar esta aplicación Android necesitas:

1. **Android Studio** (Hedgehog 2023.1.1 o superior)
   - Descarga: https://developer.android.com/studio
   
2. **JDK 17**
   - Incluido con Android Studio
   
3. **Android SDK** (API 24 - 34)
   - Se instala automáticamente con Android Studio

4. **Emulador Android** o **Dispositivo Físico**
   - Emulador: Se configura desde Android Studio (AVD Manager)
   - Dispositivo: Activar "Opciones de Desarrollador" y "Depuración USB"

---

## 📝 PASOS PARA EJECUTAR

### Opción 1: Abrir en Android Studio (RECOMENDADO)

1. **Abrir Android Studio**

2. **Importar el Proyecto**
   - Click en "Open" o "Open an Existing Project"
   - Navegar a: `C:\ControllerApp`
   - Seleccionar la carpeta `ControllerApp`
   - Click en "OK"

3. **Esperar Sincronización de Gradle**
   - Android Studio sincronizará automáticamente las dependencias
   - Esto puede tomar 2-5 minutos la primera vez
   - Verás un indicador de progreso en la parte inferior

4. **Configurar Emulador (si no tienes uno)**
   - Click en "Tools" → "Device Manager"
   - Click en "Create Device"
   - Seleccionar un dispositivo (ej: Pixel 5)
   - Seleccionar System Image (ej: API 34, Android 14)
   - Click en "Finish"

5. **Ejecutar la Aplicación**
   - Click en el botón ▶️ "Run 'app'" (o presiona Shift+F10)
   - Seleccionar el emulador o dispositivo
   - La aplicación se instalará y ejecutará automáticamente

---

### Opción 2: Línea de Comandos (Requiere configuración adicional)

Si tienes Gradle y Android SDK configurados:

```powershell
# Navegar al proyecto
cd C:\ControllerApp

# Compilar el proyecto
.\gradlew.bat assembleDebug

# Instalar en dispositivo/emulador conectado
.\gradlew.bat installDebug

# O todo en un comando
.\gradlew.bat installDebug
```

**NOTA**: Esta opción requiere:
- Gradle Wrapper JAR (se genera automáticamente al abrir en Android Studio)
- Android SDK configurado en variables de entorno
- Un dispositivo/emulador ya en ejecución

---

## 🎯 FLUJO DE LA APLICACIÓN

### 1. Pantalla de Login (Inicio)
Al iniciar la aplicación:
- ✅ Se verifica automáticamente la versión contra el servidor
- ✅ Aparece un mensaje indicando si la versión está actualizada
- ✅ Click en "INICIAR SESIÓN" para autenticar
- ✅ Si la autenticación es exitosa (HTTP 200), navega a Home
- ❌ Si falla, muestra mensaje de error

### 2. Pantalla Home
Después del login exitoso:
- Muestra: **Usuario**, **Identificación** y **Nombre**
- Dos botones disponibles:
  - **TABLAS**: Ver tablas del esquema sincronizado
  - **LOCALIDADES**: Ver localidades de recogida

### 3. Pantalla Tablas
- Lista todas las tablas obtenidas del endpoint de esquema
- Cada item muestra: Nombre de tabla y Descripción
- Si no hay tablas, muestra mensaje vacío

### 4. Pantalla Localidades
- Consulta en tiempo real al endpoint de localidades
- Muestra: **Abreviación Ciudad** y **Nombre Completo**
- Maneja errores de conexión con mensajes claros

---

## 🔗 ENDPOINTS CONSUMIDOS

La aplicación consume los siguientes servicios:

1. **Control de Versiones**
   ```
   GET https://apitesting.interrapidisimo.co/apicontrollerpruebas/api/ParametrosFramework/ConsultarParametrosFramework/VPStoreAppControl
   ```

2. **Autenticación**
   ```
   POST https://apitesting.interrapidisimo.co/FtEntregaElectronica/MultiCanales/ApiSeguridadPruebas/api/Seguridad/AuthenticaUsuarioApp
   ```

3. **Esquema de Datos**
   ```
   GET https://apitesting.interrapidisimo.co/apicontrollerpruebas/api/SincronizadorDatos/ObtenerEsquema/true
   ```

4. **Localidades**
   ```
   GET https://apitesting.interrapidisimo.co/apicontrollerpruebas/api/ParametrosFramework/ObtenerLocalidadesRecogidas
   ```

---

## ⚠️ SOLUCIÓN DE PROBLEMAS COMUNES

### Error: "SDK location not found"
**Solución**: Crear archivo `local.properties` en la raíz del proyecto:
```properties
sdk.dir=C\:\\Users\\TU_USUARIO\\AppData\\Local\\Android\\Sdk
```

### Error: "Gradle sync failed"
**Solución**: 
1. File → Invalidate Caches / Restart
2. Esperar a que sincronice nuevamente

### Error: "Unable to resolve dependency"
**Solución**:
1. Verificar conexión a internet
2. File → Settings → Build, Execution, Deployment → Gradle
3. Asegurar que "Gradle JDK" es JDK 17

### Error: "No connected devices"
**Solución**:
1. Iniciar un emulador desde Device Manager
2. O conectar dispositivo físico con USB Debugging activado

### La aplicación muestra "Error de red"
**Solución**:
- Verificar conexión a internet
- Los endpoints de testing deben estar disponibles
- En emulador, verificar que tiene acceso a internet

---

## 📱 REQUISITOS DE DISPOSITIVO/EMULADOR

- **API Mínimo**: 24 (Android 7.0)
- **API Target**: 34 (Android 14)
- **Permisos**: Internet (ya configurado en AndroidManifest.xml)

---

## 🧪 PARA DESARROLLO Y TESTING

### Ver Logs en Android Studio
1. Click en "Logcat" (parte inferior)
2. Filtrar por "com.example.controllerapp"
3. Ver errores, warnings y logs de la app

### Debugging
1. Agregar breakpoints en el código (click en margen izquierdo)
2. Click en el botón 🐛 "Debug 'app'"
3. Interactuar con la aplicación
4. El debugger se detendrá en los breakpoints

### Probar en diferentes dispositivos
1. Device Manager → Create Device
2. Probar con diferentes APIs (24, 28, 30, 34)
3. Probar en diferentes tamaños de pantalla

---

## 📊 CARACTERÍSTICAS TÉCNICAS

- ✅ Arquitectura: MVVM + Repository Pattern
- ✅ Base de Datos: Room (SQLite)
- ✅ Networking: Retrofit + OkHttp
- ✅ Async: Kotlin Coroutines + Flow
- ✅ UI: Material Design Components
- ✅ Principios SOLID implementados
- ✅ Manejo de excepciones Try/Catch
- ✅ Código completamente documentado

---

## 📞 AYUDA ADICIONAL

- **Documentación Android**: https://developer.android.com/docs
- **Kotlin Docs**: https://kotlinlang.org/docs/home.html
- **Material Design**: https://material.io/develop/android

---

**Versión**: 1.0.0  
**Fecha**: Febrero 2026  
**Estado**: ✅ Listo para ejecutar y sustentar

<p align="center">
  <img src="docs/icono-app-512x512.png" width="120" alt="Logo de MiMercado">
</p>

<h1 align="center">MiMercado</h1>

<p align="center">
  Organiza tus listas de mercado por categorías y marca lo que ya compraste.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Android-8.0%2B-3DDC84?logo=android&logoColor=white" alt="Android 8.0+">
  <img src="https://img.shields.io/badge/Kotlin-2.0-7F52FF?logo=kotlin&logoColor=white" alt="Kotlin">
  <img src="https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?logo=jetpackcompose&logoColor=white" alt="Jetpack Compose">
  <img src="https://img.shields.io/badge/target%20SDK-35-2F7D4B" alt="Target SDK 35">
  <a href="https://www.figma.com/design/OuyesjUdBJazh0nN60tXD6/MiMercado?node-id=0-1"><img src="https://img.shields.io/badge/Dise%C3%B1o-Figma-F24E1E?logo=figma&logoColor=white" alt="Diseño en Figma"></a>
</p>

---

## 📱 Capturas

<p align="center">
  <img src="docs/1-bienvenida.png" width="250" alt="Pantalla de bienvenida">
  <img src="docs/2-inicio.png" width="250" alt="Pantalla de inicio">
  <img src="docs/3-lista.png" width="250" alt="Detalle de una lista">
</p>

## ✨ Funcionalidades

- **Cuentas de usuario**: registro, inicio de sesión y recuperación de contraseña.
- **Varias listas**: crea listas (mercado semanal, cena, aseo…) con ícono y color propios.
- **Productos detallados**: cantidad, unidad, categoría, nota y prioridad.
- **Progreso visual**: marca lo comprado y mira el avance de cada lista.
- **Búsqueda y filtros**: busca productos y filtra entre pendientes y comprados.
- **Orden por categoría**: agrupa los productos para recorrer la tienda más rápido.
- **Tema claro, oscuro o del sistema.**
- **Privacidad**: todos los datos se guardan solo en el dispositivo. Sin anuncios, sin rastreo y sin conexión a internet.

## 🛠️ Tecnologías

| Área | Herramienta |
|---|---|
| Lenguaje | Kotlin 2.0 |
| Interfaz | Jetpack Compose + Material 3 |
| Navegación | Navigation Compose |
| Base de datos | Room (SQLite) |
| Preferencias y sesión | DataStore Preferences |
| Concurrencia | Kotlin Coroutines + Flow |
| Arquitectura | MVVM (ViewModel + Repository) |

## 📂 Estructura del repositorio

```
MiMercado/                                  ← raíz del repositorio
├── MiMercado/                              ← proyecto Android 
│   ├── app/
│   │   ├── build.gradle.kts
│   │   ├── proguard-rules.pro
│   │   └── src/main/
│   │       ├── AndroidManifest.xml
│   │       ├── java/co/edu/upb/mimercado/
│   │       │   ├── data/                   # Room (entidades, DAOs, base de datos), repositorio y datos de ejemplo
│   │       │   │   ├── AppDatabase.kt
│   │       │   │   ├── Catalog.kt
│   │       │   │   ├── Daos.kt
│   │       │   │   ├── MercadoRepository.kt
│   │       │   │   ├── Models.kt
│   │       │   │   └── Seed.kt
│   │       │   ├── ui/
│   │       │   │   ├── components/         # Componentes reutilizables (campos, botones, logo…)
│   │       │   │   ├── navigation/         # Rutas y navegación entre pantallas
│   │       │   │   ├── screens/            # Autenticación, listas, productos y configuración
│   │       │   │   └── theme/              # Colores, tipografía y tema
│   │       │   ├── MainActivity.kt
│   │       │   ├── MercadoViewModel.kt
│   │       │   └── MiMercadoApplication.kt
│   │       └── res/
│   │           ├── drawable/               # Ícono de la app 
│   │           ├── font/                   # Poppins e Inter
│   │           ├── mipmap-anydpi-v26/      # Ícono adaptativo
│   │           └── values/                 # Colores, textos y temas
│   ├── gradle/wrapper/
│   ├── build.gradle.kts
│   ├── settings.gradle.kts
│   ├── gradle.properties
│   ├── gradlew
│   └── gradlew.bat
├── WireframesManuales/                     # Wireframes de cada pantalla (PNG, DOCX y PDF)
├── docs/                                   # Logo y capturas usadas en README
├── MiMercado_Documento_Diseno_Inicial.docx # Documento de diseño inicial
└── README.md
```

## 🚀 Cómo ejecutarlo

### Requisitos
- Android Studio (versión reciente)
- JDK 17 o 21
- Android SDK 35

### Pasos
1. Clona el repositorio:
   ```bash
   git clone https://github.com/Niscko/MiMercado.git
   ```
2. En Android Studio abre la carpeta interna **`MiMercado/MiMercado`** (la que contiene `settings.gradle.kts`) y espera a que Gradle sincronice.
3. Ejecuta la app en un emulador o celular con Android 8.0 (API 26) o superior.

### Cuenta de prueba
La app incluye una cuenta de demostración con listas de ejemplo:

| Correo | Contraseña |
|---|---|
| `andrea@email.com` | `mercado123` |

## 📐 Diseño

- 🎨 [Diseño, wireframes y prototipo en Figma](https://www.figma.com/design/OuyesjUdBJazh0nN60tXD6/MiMercado?node-id=0-1)
- 📄 [Documento de diseño inicial](MiMercado_Documento_Diseno_Inicial.docx)
- ✏️ [Wireframes manuales (PDF)](WireframesManuales/MiMercado_Wireframes_Manuales.pdf)

<p align="center">
  <img src="WireframesManuales/00_mapa.png" width="600" alt="Mapa de navegación">
</p>

## 🔒 Privacidad

MiMercado no recopila ni comparte datos. Consulta la [política de privacidad](https://sites.google.com/view/mimercado-privacidad/p%C3%A1gina-principal).

## 👥 Autores

| Integrante |
|---|
| Josue Pino |
| Nicolás Agudelo |

**Universidad Pontificia Bolivariana** · Ingeniería de Sistemas · Aplicaciones Móviles

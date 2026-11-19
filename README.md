# 📄 DocConverter

**DocConverter** is an Android application that makes document conversion simple and convenient. It allows users to convert images and Office documents into PDF format through an easy-to-use interface.

The application is built using **Kotlin** and **Jetpack Compose**, providing a modern Android experience.

## ✨ Features

- 🖼️ **Images to PDF:** Select multiple images and combine them into a single PDF document.
- 📝 **Word to PDF:** Convert Microsoft Word documents into PDF format.
- 📊 **Excel to PDF:** Convert Microsoft Excel spreadsheets into PDF format.
- 📽️ **PowerPoint to PDF:** Convert PowerPoint presentations into PDF format.
- 📂 **Multiple Image Selection:** Select multiple images for a single PDF conversion.
- 💾 **Save PDF Files:** Choose the destination and filename for the generated PDF.
- ⚡ **Background Processing:** Perform conversion operations using background threads to keep the UI responsive.
- 📱 **Simple Interface:** Easy-to-use interface built with Jetpack Compose.

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Kotlin | Primary programming language |
| Jetpack Compose | Modern Android UI |
| Material 3 | UI components and styling |
| Android SDK | Android application development |
| Kotlin Coroutines | Background processing |
| Android PdfDocument API | Image-to-PDF generation |
| OkHttp | HTTP networking |
| Gradle Kotlin DSL | Build configuration |

## 📁 Project Structure

```text
DocConverter/
│
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/example/docconverter/
│   │       │       ├── MainActivity.kt
│   │       │       └── ...
│   │       │
│   │       ├── res/
│   │       └── AndroidManifest.xml
│   │
│   └── build.gradle.kts
│
├── gradle/
│   └── libs.versions.toml
│
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
└── README.md
```

## 🚀 Getting Started

### Prerequisites

Before running the application, make sure you have:

- Android Studio
- Android SDK 26 or higher
- JDK 11 or compatible Gradle JDK
- Internet connection for required dependencies

### Installation

**1. Clone the repository**

```bash
git clone https://github.com/naveensingh-byte/DocConverter.git
```

**2. Navigate to the project directory**

```bash
cd DocConverter
```

**3. Open the project**

Open the project in Android Studio.

**4. Sync Gradle**

Allow Android Studio to download the required dependencies and synchronize the project.

**5. Run the application**

Connect an Android device or start an emulator and click the Run button.

## 📖 How to Use

### Images to PDF

1. Open the DocConverter application.
2. Select the **Images → PDF** option.
3. Choose one or more images from your device.
4. Enter the desired PDF filename and select the save location.
5. Wait for the conversion process to complete.
6. Your PDF file will be saved successfully.

### Word / Excel / PowerPoint to PDF

1. Open the DocConverter application.
2. Select **Word/Excel/PPT → PDF**.
3. Choose the Office document you want to convert.
4. Select the destination and filename for the PDF.
5. Wait for the conversion process to finish.
6. Your converted PDF will be saved at the selected location.

## 📋 Supported File Formats

| Category | Supported Formats |
|---|---|
| Images | Image formats supported by Android BitmapFactory |
| Microsoft Word | `.doc`, `.docx` |
| Microsoft Excel | `.xls`, `.xlsx` |
| Microsoft PowerPoint | `.ppt`, `.pptx` |
| Output | `.pdf` |

## 🔒 Permissions

The application requires Internet access for its network-related functionality.

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

Files are accessed using Android's document picker, allowing users to select input documents and choose PDF output locations.

## 🔮 Future Improvements

- Support additional image formats.
- Add PDF merging functionality.
- Add PDF compression.
- Add PDF preview functionality.
- Improve conversion progress indicators.
- Add conversion history.
- Improve error handling and user feedback.
- Support additional document formats.

## 🤝 Contributing

Contributions are welcome!

1. Fork this repository.
2. Create a new branch.

```bash
git checkout -b feature/your-feature-name
```

3. Commit your changes.

```bash
git commit -m "Add: your feature description"
```

4. Push your branch.

```bash
git push origin feature/your-feature-name
```

5. Open a Pull Request.

## 📜 License

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for more information.

## 👨‍💻 Author

**Naveen Singh**

GitHub: [@naveensingh-byte](https://github.com/naveensingh-byte)

---

⭐ If you find this project useful, consider giving it a star on GitHub!

**Repository:** https://github.com/naveensingh-byte/DocConverter

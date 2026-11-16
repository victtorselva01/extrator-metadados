# extrator-metadados

A command-line tool written in Java that extracts metadata from images, PDFs and Word documents, with a focus on what is useful for OSINT (open-source intelligence) investigations.

Files often reveal more than their owners expect: the device that took a photo, the exact time and place, the software that generated a PDF, the author of a document. This tool collects that information in one readable report.

## Features

- **Images** (JPG, PNG, HEIC/HEIF, TIFF, WebP): device make and model, lens, capture date and time zone, GPS coordinates (with a map link), altitude and camera direction
- **PDF**: title, author, subject, keywords, creating application, PDF producer, creation and modification dates, page count, encryption status
- **Word (.docx)**: title, author, last modified by, creation and modification dates, revision number, application name and version, company
- **Two output formats**: human-readable text or JSON (`--json`)
**Full dump** of every metadata tag for images (--tudo)

## Requirements

- JDK 25 or newer
- Maven 3.9 or newer

## Build

```
mvn compile dependency:copy-dependencies
```

## Usage

```
java -cp "target/classes:target/dependency/*" extrator.Main <file> [--tudo] [--json]
```

On Windows, use `;` instead of `:` in the classpath:

```
java -cp "target\classes;target\dependency\*" extrator.Main <file> [--tudo] [--json]
```

| Option   | Description                                              |
|----------|----------------------------------------------------------|
| `--tudo` | Also print every metadata tag found (images only)         |
| `--json` | Print the report as JSON instead of plain text            |

### Example

```
java -cp "target\classes;target\dependency\*" extrator.Main report.pdf --json
```

```json
{
  "Arquivo": {
    "Nome": "report.pdf",
    "Tamanho": "77799 bytes"
  },
  "Documento PDF": {
    "Título": "Quarterly report",
    "Criado com": "Chromium",
    "Gerado por": "Skia/PDF m157",
    "Criação": "2026-11-15 09:53:11 Z",
    "Páginas": "2",
    "Criptografado": "não"
  }
}
```

> The report labels are currently in Portuguese. Localization is on the roadmap.

## Why it matters (OSINT context)

- **Photos** can contain the exact GPS position where they were taken. Many platforms strip this data, but files shared directly (e-mail, cloud links, forums) often keep it.
- **Documents** reveal the author, the software and its version, and the dates of creation and editing. This helps verify authenticity and spot inconsistencies.
- **Dates in PDFs are usually stored in UTC**, so always check the time zone before comparing them with local times.

## Security notes

- Word files are ZIP archives with XML inside. The XML parser used here **rejects DOCTYPE declarations** to prevent XXE (XML External Entity) attacks from malicious documents. A unit test covers this.
- The tool only reads the files you give it. It does not upload or send anything over the network.

## Responsible use

Metadata can contain personal information such as a person's location. Use this tool only on files you own or have permission to analyze, and follow the laws and ethical guidelines that apply to your investigation. Do not publish coordinates or personal data extracted from other people's files.

## Tests

```
mvn test
```

## Tech stack

- Java 25, Maven
- [metadata-extractor](https://github.com/drewnoakes/metadata-extractor) for images
- [Apache PDFBox](https://pdfbox.apache.org/) for PDFs
- [Gson](https://github.com/google/gson) for JSON output
- JUnit 5 for tests

## Roadmap

- [ ] Single runnable `.jar`
- [ ] English labels and a language option
- [ ] Support for `.xlsx` and `.pptx`
- [ ] Batch mode for whole folders

## Author

Victor Silva: [LinkedIn](https://www.linkedin.com/in/victtorselva1) · [GitHub](https://github.com/victtorselva01)
package extrator;

import com.drew.imaging.ImageMetadataReader;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Uso: java extrator.Main <caminho-do-arquivo> [--tudo]");
            return;
        }

        Path caminho = Path.of(args[0]);

        if (!Files.exists(caminho)) {
            System.out.println("Erro: arquivo não encontrado: " + caminho);
            return;
        }

        String nome = caminho.getFileName().toString();
        long tamanho = Files.size(caminho);

        System.out.println("=== Arquivo ===");
        System.out.println("Nome: " + nome);
        System.out.println("Tamanho: " + tamanho + " bytes");

        Metadata metadata = ImageMetadataReader.readMetadata(caminho.toFile());

        mostrarResumo(metadata);

        if (args.length > 1 && args[1].equals("--tudo")) {
            mostrarTudo(metadata);
        }
    }

    private static void mostrarResumo(Metadata metadata) {
        ExifIFD0Directory ifd0 = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        ExifSubIFDDirectory sub = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        GpsDirectory gps = metadata.getFirstDirectoryOfType(GpsDirectory.class);

        System.out.println();
        System.out.println("=== Dispositivo ===");
        if (ifd0 != null) {
            imprimir("Fabricante", ifd0.getString(ExifIFD0Directory.TAG_MAKE));
            imprimir("Modelo", ifd0.getString(ExifIFD0Directory.TAG_MODEL));
            imprimir("Software", ifd0.getString(ExifIFD0Directory.TAG_SOFTWARE));
        }
        if (sub != null) {
            imprimir("Lente", sub.getString(ExifSubIFDDirectory.TAG_LENS_MODEL));
        }

        System.out.println();
        System.out.println("=== Data e hora ===");
        if (sub != null) {
            imprimir("Captura", sub.getString(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL));
            // 0x9011 = Time Zone Original
            imprimir("Fuso horário", sub.getString(0x9011));
        }

        System.out.println();
        System.out.println("=== Localização ===");
        if (gps == null) {
            System.out.println("Sem dados de GPS.");
            return;
        }

        GeoLocation local = gps.getGeoLocation();
        if (local == null || local.isZero()) {
            System.out.println("Sem coordenadas válidas.");
        } else {
            String coordenadas = String.format(Locale.ROOT, "%.6f, %.6f",
                    local.getLatitude(), local.getLongitude());
            imprimir("Coordenadas", coordenadas);
            imprimir("Mapa", "https://www.google.com/maps?q=" + coordenadas.replace(" ", ""));
        }
        imprimir("Altitude", gps.getDescription(GpsDirectory.TAG_ALTITUDE));
        imprimir("Direção da câmera", gps.getDescription(GpsDirectory.TAG_IMG_DIRECTION));
    }

    private static void mostrarTudo(Metadata metadata) {
        System.out.println();
        System.out.println("=== Todos os metadados ===");
        for (Directory diretorio : metadata.getDirectories()) {
            for (Tag tag : diretorio.getTags()) {
                System.out.println("[" + diretorio.getName() + "] "
                        + tag.getTagName() + " = " + tag.getDescription());
            }
        }
    }

    private static void imprimir(String rotulo, String valor) {
        if (valor != null && !valor.isBlank()) {
            System.out.println(rotulo + ": " + valor);
        }
    }
}
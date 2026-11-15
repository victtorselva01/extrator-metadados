package extrator;

import com.drew.imaging.ImageMetadataReader;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;

import java.nio.file.Path;
import java.util.Locale;

public class LeitorImagem {

    public static void ler(Path caminho, boolean tudo) throws Exception {
        Metadata metadata = ImageMetadataReader.readMetadata(caminho.toFile());

        ExifIFD0Directory ifd0 = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        ExifSubIFDDirectory sub = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        GpsDirectory gps = metadata.getFirstDirectoryOfType(GpsDirectory.class);

        System.out.println();
        System.out.println("=== Dispositivo ===");
        boolean achou = false;
        if (ifd0 != null) {
            achou |= Saida.imprimir("Fabricante", ifd0.getString(ExifIFD0Directory.TAG_MAKE));
            achou |= Saida.imprimir("Modelo", ifd0.getString(ExifIFD0Directory.TAG_MODEL));
            achou |= Saida.imprimir("Software", ifd0.getString(ExifIFD0Directory.TAG_SOFTWARE));
        }
        if (sub != null) {
            achou |= Saida.imprimir("Lente", sub.getString(ExifSubIFDDirectory.TAG_LENS_MODEL));
        }
        if (!achou) {
            Saida.semDados();
        }

        System.out.println();
        System.out.println("=== Data e hora ===");
        achou = false;
        if (sub != null) {
            achou |= Saida.imprimir("Captura", sub.getString(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL));
            // 0x9011 = Time Zone Original
            achou |= Saida.imprimir("Fuso horário", sub.getString(0x9011));
        }
        if (!achou) {
            Saida.semDados();
        }

        System.out.println();
        System.out.println("=== Localização ===");
        if (gps == null) {
            System.out.println("Sem dados de GPS.");
        } else {
            GeoLocation local = gps.getGeoLocation();
            if (local == null || local.isZero()) {
                System.out.println("Sem coordenadas válidas.");
            } else {
                String coordenadas = String.format(Locale.ROOT, "%.6f,%.6f",
                        local.getLatitude(), local.getLongitude());
                Saida.imprimir("Coordenadas", coordenadas);
                Saida.imprimir("Mapa", "https://www.google.com/maps?q=" + coordenadas);
            }
            Saida.imprimir("Altitude", gps.getDescription(GpsDirectory.TAG_ALTITUDE));
            Saida.imprimir("Direção da câmera", gps.getDescription(GpsDirectory.TAG_IMG_DIRECTION));
        }

        if (tudo) {
            mostrarTudo(metadata);
        }
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
}
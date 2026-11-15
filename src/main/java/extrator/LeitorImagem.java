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

    public static void ler(Path caminho, boolean tudo, Relatorio relatorio) throws Exception {
        Metadata metadata = ImageMetadataReader.readMetadata(caminho.toFile());

        ExifIFD0Directory ifd0 = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        ExifSubIFDDirectory sub = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        GpsDirectory gps = metadata.getFirstDirectoryOfType(GpsDirectory.class);

        String dispositivo = "Dispositivo";
        relatorio.criarSecao(dispositivo);
        if (ifd0 != null) {
            relatorio.adicionar(dispositivo, "Fabricante", ifd0.getString(ExifIFD0Directory.TAG_MAKE));
            relatorio.adicionar(dispositivo, "Modelo", ifd0.getString(ExifIFD0Directory.TAG_MODEL));
            relatorio.adicionar(dispositivo, "Software", ifd0.getString(ExifIFD0Directory.TAG_SOFTWARE));
        }
        if (sub != null) {
            relatorio.adicionar(dispositivo, "Lente", sub.getString(ExifSubIFDDirectory.TAG_LENS_MODEL));
        }

        String dataHora = "Data e hora";
        relatorio.criarSecao(dataHora);
        if (sub != null) {
            relatorio.adicionar(dataHora, "Captura", sub.getString(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL));
            // 0x9011 = Time Zone Original
            relatorio.adicionar(dataHora, "Fuso horário", sub.getString(0x9011));
        }

        String localizacao = "Localização";
        relatorio.criarSecao(localizacao);
        if (gps != null) {
            GeoLocation local = gps.getGeoLocation();
            if (local != null && !local.isZero()) {
                String lat = String.format(Locale.ROOT, "%.6f", local.getLatitude());
                String lon = String.format(Locale.ROOT, "%.6f", local.getLongitude());
                relatorio.adicionar(localizacao, "Latitude", lat);
                relatorio.adicionar(localizacao, "Longitude", lon);
                relatorio.adicionar(localizacao, "Mapa", "https://www.google.com/maps?q=" + lat + "," + lon);
            }
            relatorio.adicionar(localizacao, "Altitude", gps.getDescription(GpsDirectory.TAG_ALTITUDE));
            relatorio.adicionar(localizacao, "Direção da câmera", gps.getDescription(GpsDirectory.TAG_IMG_DIRECTION));
        }

        if (tudo) {
            String todos = "Todos os metadados";
            relatorio.criarSecao(todos);
            for (Directory diretorio : metadata.getDirectories()) {
                for (Tag tag : diretorio.getTags()) {
                    relatorio.adicionar(todos,
                            "[" + diretorio.getName() + "] " + tag.getTagName(),
                            tag.getDescription());
                }
            }
        }
    }
}
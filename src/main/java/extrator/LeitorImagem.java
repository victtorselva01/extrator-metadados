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

    private static String t(String chave) {
        return Mensagens.get(chave);
    }

    public static void ler(Path caminho, boolean tudo, Relatorio relatorio) throws Exception {
        Metadata metadata = ImageMetadataReader.readMetadata(caminho.toFile());

        ExifIFD0Directory ifd0 = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        ExifSubIFDDirectory sub = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        GpsDirectory gps = metadata.getFirstDirectoryOfType(GpsDirectory.class);

        String dispositivo = t("device.section");
        relatorio.criarSecao(dispositivo);
        if (ifd0 != null) {
            relatorio.adicionar(dispositivo, t("device.make"), ifd0.getString(ExifIFD0Directory.TAG_MAKE));
            relatorio.adicionar(dispositivo, t("device.model"), ifd0.getString(ExifIFD0Directory.TAG_MODEL));
            relatorio.adicionar(dispositivo, t("device.software"), ifd0.getString(ExifIFD0Directory.TAG_SOFTWARE));
        }
        if (sub != null) {
            relatorio.adicionar(dispositivo, t("device.lens"), sub.getString(ExifSubIFDDirectory.TAG_LENS_MODEL));
        }

        String dataHora = t("datetime.section");
        relatorio.criarSecao(dataHora);
        if (sub != null) {
            relatorio.adicionar(dataHora, t("datetime.captured"), sub.getString(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL));
            // 0x9011 = Time Zone Original
            relatorio.adicionar(dataHora, t("datetime.timezone"), sub.getString(0x9011));
        }

        String localizacao = t("location.section");
        relatorio.criarSecao(localizacao);
        if (gps != null) {
            GeoLocation local = gps.getGeoLocation();
            if (local != null && !local.isZero()) {
                String lat = String.format(Locale.ROOT, "%.6f", local.getLatitude());
                String lon = String.format(Locale.ROOT, "%.6f", local.getLongitude());
                relatorio.adicionar(localizacao, t("location.latitude"), lat);
                relatorio.adicionar(localizacao, t("location.longitude"), lon);
                relatorio.adicionar(localizacao, t("location.map"), "https://www.google.com/maps?q=" + lat + "," + lon);
            }
            relatorio.adicionar(localizacao, t("location.altitude"), gps.getDescription(GpsDirectory.TAG_ALTITUDE));
            relatorio.adicionar(localizacao, t("location.direction"), gps.getDescription(GpsDirectory.TAG_IMG_DIRECTION));
        }

        if (tudo) {
            String todos = t("all.section");
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
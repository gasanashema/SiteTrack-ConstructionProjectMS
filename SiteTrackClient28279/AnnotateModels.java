import java.io.*;
import java.nio.file.*;
import java.util.*;

public class AnnotateModels {
    public static void main(String[] args) throws Exception {
        File dir = new File("c:/Users/GeekNest/Documents/NetBeansProjects/SiteTrack/SiteTrackServer28279/src/model");
        for (File f : dir.listFiles()) {
            if (!f.getName().endsWith(".java")) continue;
            
            List<String> lines = Files.readAllLines(f.toPath());
            List<String> out = new ArrayList<>();
            boolean modified = false;
            
            for (String line : lines) {
                if (line.contains("private LocalDate ") && !line.contains("@javax.persistence.Convert")) {
                    out.add("    @javax.persistence.Convert(converter = util.LocalDateAttributeConverter.class)");
                    out.add(line);
                    modified = true;
                } else if (line.contains("private LocalDateTime ") && !line.contains("@javax.persistence.Convert")) {
                    out.add("    @javax.persistence.Convert(converter = util.LocalDateTimeAttributeConverter.class)");
                    out.add(line);
                    modified = true;
                } else {
                    out.add(line);
                }
            }
            
            if (modified) {
                Files.write(f.toPath(), out);
                System.out.println("Annotated: " + f.getName());
            }
        }
    }
}

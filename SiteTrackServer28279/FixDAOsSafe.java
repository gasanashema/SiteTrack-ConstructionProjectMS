import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FixDAOsSafe {
    public static void main(String[] args) throws Exception {
        File dir = new File("c:/Users/GeekNest/Documents/NetBeansProjects/SiteTrack/SiteTrackServer28279/src/dao");
        for (File f : dir.listFiles()) {
            if (!f.getName().endsWith("Dao.java")) continue;
            
            String content = new String(Files.readAllBytes(f.toPath()));
            
            // Split by "public " to process each method individually
            // (Assuming methods start with "public ")
            String[] parts = content.split("public ");
            
            StringBuilder sb = new StringBuilder();
            sb.append(parts[0]);
            
            boolean changed = false;
            for (int i = 1; i < parts.length; i++) {
                String part = parts[i];
                
                // If it's a method that uses Transaction tr
                if (part.contains("Transaction tr = ss.beginTransaction();")) {
                    // We need to inject "Transaction tr = null;" right before "try {"
                    part = part.replace("Transaction tr = ss.beginTransaction();", "tr = ss.beginTransaction();");
                    
                    int tryIndex = part.indexOf("try {");
                    if (tryIndex != -1) {
                        part = part.substring(0, tryIndex) + "Transaction tr = null;\n        " + part.substring(tryIndex);
                    }
                    
                    // Add tr.rollback() to catch
                    part = part.replace("} catch (Exception e) {", "} catch (Exception e) { if (tr != null && tr.isActive()) { tr.rollback(); }");
                    changed = true;
                }
                
                sb.append("public ").append(part);
            }
            
            if (changed) {
                Files.write(f.toPath(), sb.toString().getBytes());
                System.out.println("Refactored: " + f.getName());
            }
        }
        System.out.println("Finished.");
    }
}

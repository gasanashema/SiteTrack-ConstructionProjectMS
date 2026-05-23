import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class RefactorDAOs {
    public static void main(String[] args) throws Exception {
        File dir = new File("c:/Users/GeekNest/Documents/NetBeansProjects/SiteTrack/SiteTrackServer28279/src/dao");
        for (File f : dir.listFiles()) {
            if (!f.getName().endsWith("Dao.java") || f.getName().equals("AuditLogDao.java")) continue;
            
            String content = new String(Files.readAllBytes(f.toPath()));
            
            // 1. Replace 'try { Session ss = ...'
            content = content.replaceAll("try\\s*\\{\\s*Session ss = HibernateUtil\\.getSessionFactory\\(\\)\\.openSession\\(\\);",
                "Session ss = null;\n        try {\n            ss = HibernateUtil.getSessionFactory().openSession();");
                
            // 2. Remove 'ss.close();'
            content = content.replaceAll("\\s*ss\\.close\\(\\);", "");
            
            // 3. Find catch blocks and append finally
            // A catch block starts with '} catch (Exception e) {'
            // We need to find the closing '}' of this catch block.
            StringBuilder sb = new StringBuilder(content);
            int idx = 0;
            while ((idx = sb.indexOf("} catch (Exception e) {", idx)) != -1) {
                // Find the closing brace of the catch block
                int openBraces = 0;
                int closeIdx = -1;
                for (int i = idx + 23; i < sb.length(); i++) {
                    if (sb.charAt(i) == '{') openBraces++;
                    else if (sb.charAt(i) == '}') {
                        if (openBraces == 0) {
                            closeIdx = i;
                            break;
                        } else {
                            openBraces--;
                        }
                    }
                }
                
                if (closeIdx != -1) {
                    String finallyBlock = " finally {\n            if (ss != null && ss.isOpen()) {\n                ss.close();\n            }\n        }";
                    sb.insert(closeIdx + 1, finallyBlock);
                    idx = closeIdx + finallyBlock.length() + 1;
                } else {
                    idx += 23; // Should not happen if valid Java
                }
            }
            
            Files.write(f.toPath(), sb.toString().getBytes());
            System.out.println("Refactored: " + f.getName());
        }
    }
}

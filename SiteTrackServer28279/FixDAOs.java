import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class FixDAOs {
    public static void main(String[] args) throws Exception {
        File dir = new File("c:/Users/GeekNest/Documents/NetBeansProjects/SiteTrack/SiteTrackServer28279/src/dao");
        Pattern pattern = Pattern.compile("(Session ss = null;\\s*)(try \\{.*?)(Transaction tr = ss\\.beginTransaction\\(\\);)(.*?)(\\} catch \\(Exception e\\) \\{)", Pattern.DOTALL);
        
        for (File f : dir.listFiles()) {
            if (!f.getName().endsWith("Dao.java")) continue;
            
            String content = new String(Files.readAllBytes(f.toPath()));
            Matcher m = pattern.matcher(content);
            StringBuffer sb = new StringBuffer();
            
            boolean changed = false;
            while (m.find()) {
                String preTry = m.group(1);
                String insideTry = m.group(2);
                String txBegin = m.group(3);
                String restOfTry = m.group(4);
                String catchBlock = m.group(5);
                
                // If there's already tr.rollback() in the next few characters, skip
                int nextIndex = m.end();
                String nextChars = content.substring(nextIndex, Math.min(nextIndex + 100, content.length()));
                if (nextChars.contains("tr.rollback()")) {
                    m.appendReplacement(sb, Matcher.quoteReplacement(m.group(0)));
                    continue;
                }
                
                String newPreTry = preTry + "        Transaction tr = null;\n";
                String newInsideTry = insideTry + "tr = ss.beginTransaction();" + restOfTry;
                String newCatch = "} catch (Exception e) {\n            if (tr != null && tr.isActive()) {\n                tr.rollback();\n            }";
                
                m.appendReplacement(sb, Matcher.quoteReplacement(newPreTry + newInsideTry + newCatch));
                changed = true;
            }
            m.appendTail(sb);
            
            if (changed) {
                Files.write(f.toPath(), sb.toString().getBytes());
                System.out.println("Refactored: " + f.getName());
            }
        }
        System.out.println("DAO rollback fix completed.");
    }
}

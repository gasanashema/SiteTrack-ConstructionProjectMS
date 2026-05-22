import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class RefactorColors {
    public static void main(String[] args) throws Exception {
        Files.walk(Paths.get("src/view")).filter(Files::isRegularFile).filter(p -> p.toString().endsWith(".java")).forEach(p -> {
            try {
                String content = new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
                boolean changed = false;
                
                // Replace hardcoded foreground colors
                if (content.contains("Color.decode(\"#1f242e\")") && content.contains("setForeground")) {
                    content = content.replace("Color.decode(\"#1f242e\")", "UIManager.getColor(\"Label.foreground\")");
                    changed = true;
                }
                if (content.contains("Color.decode(\"#2c3e50\")")) {
                    content = content.replace("Color.decode(\"#2c3e50\")", "UIManager.getColor(\"Label.foreground\")");
                    changed = true;
                }
                if (content.contains("Color.decode(\"#5F6368\")")) {
                    content = content.replace("Color.decode(\"#5F6368\")", "UIManager.getColor(\"Label.disabledForeground\")");
                    changed = true;
                }
                // Replace borders
                if (content.contains("Color.decode(\"#E0E0E0\")")) {
                    content = content.replace("Color.decode(\"#E0E0E0\")", "UIManager.getColor(\"Component.borderColor\")");
                    changed = true;
                }
                // Sidebar background
                if (p.toString().contains("SidebarPanel.java")) {
                    if (content.contains("Color.decode(\"#1f242e\")")) {
                        content = content.replace("Color.decode(\"#1f242e\")", "UIManager.getColor(\"Panel.background\")");
                        changed = true;
                    }
                }
                
                if (changed) {
                    Files.write(p, content.getBytes(StandardCharsets.UTF_8));
                    System.out.println("Updated " + p);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

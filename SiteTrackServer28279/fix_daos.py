import os
import re

dao_dir = r"c:\Users\GeekNest\Documents\NetBeansProjects\SiteTrack\SiteTrackServer28279\src\dao"

for filename in os.listdir(dao_dir):
    if not filename.endswith("Dao.java"):
        continue
    
    filepath = os.path.join(dao_dir, filename)
    with open(filepath, "r", encoding="utf-8") as f:
        content = f.read()

    # 1. Change "Transaction tr = ss.beginTransaction();" to "tr = ss.beginTransaction();"
    # and add "Transaction tr = null;" before "try {" if not already present
    
    # We will do this by finding methods that have a transaction
    
    # Regex to find:
    # Session ss = null;
    # try {
    #     ss = HibernateUtil.getSessionFactory().openSession();
    #     Transaction tr = ss.beginTransaction();
    
    pattern1 = re.compile(r'(Session ss = null;\s*try \{\s*ss = HibernateUtil\.getSessionFactory\(\)\.openSession\(\);\s*)Transaction tr = ss\.beginTransaction\(\);')
    content = pattern1.sub(r'Transaction tr = null;\n        \1tr = ss.beginTransaction();', content)
    
    # 2. Add rollback in catch block if `tr = ss.beginTransaction()` was used
    # Note: some catch blocks already have it if we fixed them, so we'll check
    pattern2 = re.compile(r'(\} catch \(Exception e\) \{\s*)(?!if \(tr != null)')
    
    # But wait, we only want to add rollback if `Transaction tr` is in scope.
    # It's safer to just replace catch block in methods where we know we added `Transaction tr = null;`
    # Let's do a more robust approach.

    new_content = []
    lines = content.split('\n')
    in_tx_method = False
    
    for i, line in enumerate(lines):
        if "Session ss = null;" in line:
            # Look ahead to see if it uses transaction
            ahead = "\n".join(lines[i:i+10])
            if "Transaction tr = ss.beginTransaction();" in ahead or "tr = ss.beginTransaction();" in ahead:
                in_tx_method = True
        
        if "Transaction tr = ss.beginTransaction();" in line:
            line = line.replace("Transaction tr = ss.beginTransaction();", "tr = ss.beginTransaction();")
            # We must inject Transaction tr = null; before try {
            # Let's inject it right above try { ... Actually, we can just replace the whole method start
        
        if "} catch (Exception e) {" in line and in_tx_method:
            if i + 1 < len(lines) and "tr.rollback()" not in lines[i+1]:
                line += "\n            if (tr != null && tr.isActive()) {\n                tr.rollback();\n            }"
            in_tx_method = False # reset after catch
            
        new_content.append(line)

    # Let's do it simply using regex on the whole file
    
    # reset content
    with open(filepath, "r", encoding="utf-8") as f:
        content = f.read()
        
    def replace_method(match):
        pre_try = match.group(1)
        inside_try = match.group(2)
        catch_block = match.group(3)
        
        if "tr.rollback()" in catch_block:
            return match.group(0) # already fixed
            
        new_pre_try = pre_try + "        Transaction tr = null;\n"
        new_inside_try = inside_try.replace("Transaction tr = ss.beginTransaction();", "tr = ss.beginTransaction();")
        
        new_catch = "} catch (Exception e) {\n            if (tr != null && tr.isActive()) {\n                tr.rollback();\n            }" + catch_block[23:]
        
        return new_pre_try + new_inside_try + new_catch

    # Matches from "Session ss = null;" to "} catch (Exception e) {"
    # Assumes there's a Transaction tr = ss.beginTransaction(); inside
    pattern = re.compile(r'(Session ss = null;\s*)(try \{.*?Transaction tr = ss\.beginTransaction\(\);.*?)(\} catch \(Exception e\) \{)', re.DOTALL)
    
    content = pattern.sub(replace_method, content)
    
    with open(filepath, "w", encoding="utf-8") as f:
        f.write(content)

print("DAOs patched successfully.")

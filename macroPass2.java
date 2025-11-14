import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class macroPass2 {

    static class MNTEntry {
        String macroName;
        int mdtIndex;
        int kpdtIndex;
        int kpCount;
        MNTEntry(String name, int mdtIndex, int kpdtIndex, int kpCount) {
            this.macroName = name;
            this.mdtIndex = mdtIndex;
            this.kpdtIndex = kpdtIndex;
            this.kpCount = kpCount;
        }
    }

    static class KPDTEntry {
        String paramName;
        String defaultValue;
        KPDTEntry(String paramName, String defaultValue) {
            this.paramName = paramName;
            this.defaultValue = defaultValue;
        }
    }

    List<MNTEntry> MNT = new ArrayList<>();
    List<String> MDT = new ArrayList<>();
    List<KPDTEntry> KPDTAB = new ArrayList<>();
    Map<String, Map<String, Integer>> PNTAB = new HashMap<>();

    public macroPass2() {
       
        MNT.add(new MNTEntry("INCR", 0, 0, 1)); 

       
        MDT.add("A 1,#1");
        MDT.add("A 2,#2");
        MDT.add("MEND");

       
        KPDTAB.add(new KPDTEntry("Y", "5")); 

        
        Map<String, Integer> incrPntab = new HashMap<>();
        incrPntab.put("X", 1); 
        incrPntab.put("Y", 2); 
        PNTAB.put("INCR", incrPntab);
    }


    public void pass2(List<String> lines) {
        for (String line : lines) {
            line = line.trim();

            if (line.equals("MACRO") || line.equals("MEND")) continue;
            if (line.startsWith("END")) {
                System.out.println("END");
                break;
            }

            String[] parts = line.split("\\s+", 2);
            String name = parts[0];

            Optional<MNTEntry> macroOpt =
                MNT.stream().filter(m -> m.macroName.equals(name)).findFirst();

            if (macroOpt.isPresent()) {
                expandMacro(macroOpt.get(), parts.length > 1 ? parts[1] : "");
            } else {
                System.out.println(line); 
            }
        }
    }

    private void expandMacro(MNTEntry mntEntry, String argStr) {
        String[] args = argStr.isEmpty() ? new String[0] : argStr.split(",");
        Map<Integer, String> actualParams = new HashMap<>();
    
        for (int i = 0; i < mntEntry.kpCount; i++) {
            int kpIndex = mntEntry.kpdtIndex + i;
            KPDTEntry kp = KPDTAB.get(kpIndex);
            int index = PNTAB.get(mntEntry.macroName).get(kp.paramName);
            actualParams.put(index, kp.defaultValue);
        }

        int pos = 1;
        for (String a : args) {
            a = a.trim();
            if (a.isEmpty()) continue;

            if (a.contains("=")) {
                String[] kv = a.split("=", 2);
                String key = kv[0].replace("&", "").trim();
                String val = (kv.length > 1) ? kv[1].trim() : "";
                int idx = PNTAB.get(mntEntry.macroName).get(key);
                actualParams.put(idx, val);
            } else {
                actualParams.put(pos, a);
                pos++;
            }
        }

        int i = mntEntry.mdtIndex;
        while (i < MDT.size()) {
            String mdtLine = MDT.get(i);
            if (mdtLine.equals("MEND")) break;

            String expanded = mdtLine;
            for (Map.Entry<String, Integer> p : PNTAB.get(mntEntry.macroName).entrySet()) {
                int idx = p.getValue();
                String val = actualParams.getOrDefault(idx, "");
                expanded = expanded.replace("#" + idx, val);
            }

            System.out.println(expanded.trim());
            i++;
        }
    }

    public static void main(String[] args) {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get("input4.txt"));
        } catch (IOException e) {
            System.err.println("Error reading input.txt: " + e.getMessage());
            System.exit(1);
        }

        macroPass2 pass2 = new macroPass2();

        System.out.println("\nExpanded Code:");
        pass2.pass2(lines);
    }
}

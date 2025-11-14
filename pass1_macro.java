import java.io.*;
import java.util.*;
public class pass1_macro {
    public static class MNT{
        String macro_name;
        int pp_count;
        int kp_count;
        int MDT_idx;
        int kp_idx;
        MNT(String macro_name,int pp_count,int kp_count,int MDT_idx,int kp_idx){
            this.macro_name = macro_name;
            this.pp_count = pp_count;
            this.kp_count = kp_count;
            this.MDT_idx = MDT_idx;
            this.kp_idx = kp_idx;
        }
    }
    static HashMap<String,String> kptab = new  LinkedHashMap<>();
    static List<String> MDT = new  ArrayList<>();
    static List<MNT> mnt = new  ArrayList<>();
    static int mdt_idx = 0;
    static int kp_idx = 0;
    static boolean isMacro = false;
    static List<String> pntab = new   ArrayList<>();
    public static void main(String[] args) {
        try{
            BufferedReader br = new BufferedReader(new FileReader("macro.txt"));
            String line;
            while((line = br.readLine()) != null){
                processLine(line.trim());
            }
            System.out.println("-----------MNT-----------");
            System.out.println("name " + "pp_count " + "kp_count " + "MDT_idx " + "kp_idx");
            int i = 1;
            for(MNT m : mnt){
                System.out.println(i + " " + m.macro_name + " " + m.pp_count + " " + m.kp_count + " " + m.MDT_idx + " " + m.kp_idx);
                i+=1;
            }
            System.out.println("-----------kptab----------");
            i = 1;
            for(Map.Entry<String, String> e : kptab.entrySet()){
                System.out.println(i + " " + e.getKey() + " : " + e.getValue());
                i += 1;
            }
            System.out.println("=---------MDT------------");
            for(String s : MDT){
                System.out.println(s);
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static void processLine(String line){
        String []tokens = line.split("\\s+");
        int i = 0;
        if(tokens[i].equals("MACRO")){
            if(mdt_idx == 0) mdt_idx = 1;
            if(kp_idx == 0) kp_idx = 1;
            isMacro = true;
            return;
        }
        if(isMacro){
            MNT macro_header = new MNT("",0,0,0,0);
            macro_header.macro_name = tokens[0];
            i+=1;
            int kp_count = 0;
            int pp_count = 0;
            macro_header.kp_idx = kp_idx;
            for(;i<tokens.length;i++){
                if(tokens[i].contains("=")){
                    if(tokens[i].length() > 3){
                        kptab.put(tokens[i].substring(0,3),tokens[i].substring(3,7));
                    }else{
                        kptab.put(tokens[i].substring(0,3),"-1");

                    }
                    pntab.add(tokens[i].substring(0,2));
                    kp_idx += 1;
                    kp_count++;
                }else{
                    pntab.add(tokens[i].substring(0,2));
                    pp_count++;
                }
            }
            macro_header.kp_count = kp_count;
            macro_header.pp_count = pp_count;
            macro_header.MDT_idx = mdt_idx;
            mnt.add(macro_header);
            isMacro = false;
        }else{
            String instruction = "";
            if(tokens[i].equals("MEND")){
                instruction = "MEND";
                System.out.println("------pntab------");
                int idx = 1;
                for(String s : pntab){
                    System.out.println(idx + " " + s);
                    idx += 1;
                }
                pntab.clear();
                MDT.add(instruction);
                mdt_idx += 1;
                return;
            }
            for(int j = 0;j<tokens.length;++j){
                if(!pntab.contains(tokens[j])){
                    instruction += (tokens[j] + " ");
                }else{
                    int idx = 0;
                    while(idx<pntab.size()){
                        if(pntab.get(idx).equals(tokens[j])){
                            break;
                        }
                        idx += 1;
                    }
                    idx += 1;
                    instruction += "(P,"+idx+")";
                }
            }
            MDT.add(instruction);
            mdt_idx += 1;
        }
    }
}
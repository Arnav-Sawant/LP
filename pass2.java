
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class pass2 {
    static Map<String,Integer> symtab = new LinkedHashMap<>();
    static Map<String,Integer> littab = new  LinkedHashMap<>();
    public static void main(String[] args){
        initialize();
        try{
            BufferedReader br =  new BufferedReader(new FileReader("input1.txt"));
            String line;
            while((line = br.readLine())!= null){
                processLine(line.trim());
            }
        }catch(Exception e){

        }
    }
    public static void processLine(String line){
        String []tokens = line.split("\\s+");
        int i = 0;
        if(tokens[i].contains("(AD,") || tokens[i].contains("(DL,01)")){
            return;
        }
        if(tokens[i].contains("(IS,")){
            System.out.print(tokens[i].substring(4,6) + " ");
            if(tokens.length >= 2){
                i+=1;
                if(tokens[i].contains("(CC,")) System.out.print(" " +tokens[i].substring(4,5));
                else System.out.print(tokens[i].substring(1,2));
            }
            if(tokens.length >=3){
                i+=1;
                if(tokens[i].contains("(L,")){
                    System.out.println(" " + getAddress(Integer.parseInt(tokens[i].substring(4,5))));
                }else{
                    System.out.println(" " +getAddress1(Integer.parseInt(tokens[i].substring(4,5))));
                }
            }
            return;
        }
        if(tokens[i].contains("(DL,02)")){
            System.out.println("0 " + "0 "+ tokens[i+1].substring(3,5));
        }
    }
    public static int getAddress(int idx){
        int i = 1;
        for(Map.Entry<String,Integer> entry : littab.entrySet()){
            if(i == idx){
                return entry.getValue();
            }
            i+=1;
        }
        return -1;
    }
    public static int getAddress1(int idx){
        int i = 1;
        for(Map.Entry<String,Integer> entry : symtab.entrySet()){
            if(i == idx){
                return entry.getValue();
            }
            i+=1;
        }
        return -1;
    }
    public static void initialize(){
        //symtab
        symtab.put("X",206);
        symtab.put("L1",203);
        symtab.put("NEXT",208);
        symtab.put("BACK",213);

        //littab
        littab.put("=5",206);
        littab.put("=2",203);
        littab.put("=1",214);

    }
}

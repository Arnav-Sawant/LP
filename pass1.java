import java.util.*;
import java.io.*;


public class pass1{
    static Map<String,Integer> symtab = new LinkedHashMap<>();
    static Map<String,Integer> littab = new LinkedHashMap<>();
    static List<Integer> pooltab = new  ArrayList<>();
    static int LC = 0;
    public static void main(String[] args) {
        try{
            BufferedReader br = new BufferedReader(new FileReader("input.txt"));
            String line;
            pooltab.add(0);
            while((line = br.readLine())!=null){
                processLine(line.trim());
            }
            //symtab
            int i = 1;
            System.out.println("----symtab----");
            for(Map.Entry<String,Integer> entry:symtab.entrySet()){
                System.out.println(i +" " + entry.getKey() + " : " + entry.getValue());
            }
            i = 1;
            //littab
            System.out.println("----littab----");
            for(Map.Entry<String,Integer> entry:littab.entrySet()){
                System.out.println(i+" " + entry.getKey() + " : " + entry.getValue());
            }
            //pooltab
            System.out.println("----pooltab----");
            for( i = 0;i<pooltab.size();++i){
                System.out.println(i + " " +  pooltab.get(i));
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static void processLine(String line){
        String[] tokens =  line.split("\\s+");
        int i = 0;
        if(!isAD(tokens[i]) && !isIS(tokens[i])){
            symtab.put(tokens[i],LC);
            i+=1;
        }
        if(i >= tokens.length) return;
        switch(tokens[i]){
            case "START":
                System.out.println("(AD,01) (C,"+tokens[i+1]+")");
                LC = Integer.parseInt(tokens[i+1]);
                break;
            case "END":
                System.out.println("(AD,02)");
                assignLiterals();
                pooltab.add(littab.size());
                break;
            case "LTORG":
                System.out.println("(AD,05)");
                assignLiterals1();
                pooltab.add(littab.size());
                break;
        }
        if(isIS(tokens[i])){
            System.out.print(LC + " " + "(IS,"+opCode(tokens[i])+") ");
            LC += 1;
            for(int j = i+1;j<tokens.length;j++){
                if(isReg(tokens[j])){
                    System.out.print("("+regCode(tokens[j])+")");
                }else{
                    if(tokens[j].equals("LT")) continue;
                    if(tokens[j].contains("=")){;
                        littab.putIfAbsent(tokens[j],-1);
                        System.out.print("(L,"+getlitIdx(tokens[j])+")");
                    }else{
                        symtab.putIfAbsent(tokens[j],-1);
                        System.out.print("(S,"+getsymIdx(tokens[j])+")");
                    }
                }
            }
            System.out.println();
        }
        if(tokens[i].equals("DS")){
            System.out.println(LC + " " +"(DL,01) , (C,"+tokens[i+1]+")");
            LC += Integer.parseInt(tokens[i+1]);
        }
    }
    public static void assignLiterals(){
        for(Map.Entry<String,Integer> entry:littab.entrySet()){
            if(entry.getValue() == -1) {
                entry.setValue(LC++);
            }
        }
    }
    public static void assignLiterals1(){
        for(Map.Entry<String,Integer> entry:littab.entrySet()){
            if(entry.getValue() == -1) {
                int tmp = LC;
                entry.setValue(LC++);
                System.out.println(tmp + " (DL,02) (C," + entry.getKey() + ")");
            }
        }
    }
    public static int getlitIdx(String token){
        int i = 1;
        for(String key : littab.keySet()){
            if(key.equals(token)){
                return i;
            }
            i+=1;
        }
        return -1;
    }
    public static int getsymIdx(String token){
        int i = 1;
        for(String key : symtab.keySet()){
            if(key.equals(token)){
                return i;
            }
            i+=1;
        }
        return -1;
    }
    public static boolean isAD(String str){
        return List.of("START","END","ORIGIN","EQU","LTORG").contains(str);
    }
    public static boolean isIS(String str){
        return List.of("STOP","ADD","SUB","MULT","MOVER","MOVEM","COMP","BC","DIV","READ","PRINT").contains(str);
    }
    public static boolean isReg(String str){
        return List.of("AREG","BREG","CREG","DREG").contains(str);
    }
    public static int opCode(String str){
        switch (str){
            case "STOP" : return 0;
            case "ADD" : return 1;
            case "SUB" : return 2;
            case "MULT" : return 3;
            case "MOVER" : return 4;
            case "MOVEM" : return 5;
            case "COMP" : return 6;
            case "BC" : return 7;
            case "DIV" : return 8;
            case "READ" : return 9;
            case "PRINT" : return 10;
        }
        return -1;
    }
    public static int regCode(String str){
        switch (str){
            case "AREG" : return 1;
            case "BREG" : return 2;
            case "CREG" : return 3;
            case "DREG" : return 4;
        }
        return -1;
    }
}
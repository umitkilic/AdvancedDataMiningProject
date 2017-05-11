/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package advanceddataminingproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author umit
 */
public class FileOperations {
    String line;
    String[] ozellikler=null;
    public int valuemapdegeri=1;
    List<String> attribues=new ArrayList<>();
    String[] ozellikListesi;
    List<String> yedekVeriler=new ArrayList<>();
    List<String> veriler=new ArrayList<>();
    
    public String[] ExtractAttributes(File file,HashMap<String,Integer> valuesMap,List<String> hashmapkeys){
        try {
                InputStream fis=new FileInputStream(file);
                InputStreamReader isr=new InputStreamReader(fis,Charset.forName("UTF-8"));
                BufferedReader br=new BufferedReader(isr);
                
                while ((line=br.readLine())!=null) {                
                    if(line.startsWith("%"))
                        continue;
                    
                    while(line.toLowerCase().contains("@attribute") || line.toLowerCase().contains("@attrıbute")){
                        
                        
                        ozellikler=line.split(" ");
                        
                        if(line.contains("{") && line.contains("}")){
                            
                            int a=line.indexOf("{");
                            int b=line.indexOf("}");
                            String subline=line.substring(a+1, b).toLowerCase();
                            ozellikler[1]=ozellikler[1]+" > "+subline;
                            String[] sublines=new String[subline.split(",").length];
                            sublines=subline.split(",");
                            
                            for (int i = 0; i < sublines.length; i++) {
                                sublines[i]=sublines[i].trim();
                                valuesMap.put(sublines[i], valuemapdegeri);
                                hashmapkeys.add(sublines[i]);
                                valuemapdegeri+=1;
                                
                            }
                        }
                        
                        
                        else if(line.toLowerCase().contains("numeric")){
                            ozellikler[1]=ozellikler[1]+" ---> "+"NUMERIC";
                        }
                        else if(line.toLowerCase().contains("real")){
                            ozellikler[1]=ozellikler[1]+" ---> "+"REAL";
                        }
                            
                        
                        attribues.add(ozellikler[1]);
                        line=br.readLine();
                        
                    }
                    
                    ozellikListesi=new String[attribues.size()];
                    
                    for (int i = 0; i < attribues.size(); i++) {
                        ozellikListesi[i]=attribues.get(i);
                    }
            }     
        }
        catch (Exception e) {    
        }
         
        
        return ozellikListesi;
    }

    public List<String> getDatas(File file,HashMap<String,Integer> valuesMap){
        try{
                InputStream fis=new FileInputStream(file);
                InputStreamReader isr=new InputStreamReader(fis,Charset.forName("UTF-8"));
                BufferedReader br=new BufferedReader(isr);
                
                while ((line=br.readLine())!=null) {
                    if(line.startsWith("%")||line.toLowerCase().contains("@attribute") || line.toLowerCase().contains("@attrıbute"))
                        continue;
                    
                    else if(line.toLowerCase().equals("@data")){
                        while((line=br.readLine())!=null){
                            yedekVeriler.add(line);
                            
                            line=line.toLowerCase();
                            String[] lines=line.split(",");
                            
                            for (int i = 0; i < lines.length; i++) {
                                
                                if(valuesMap.containsKey(lines[i])){
                                    line=line.replace(lines[i], Integer.toString(valuesMap.get(lines[i])));
                                }
                                
                            }
                            
                            
                            veriler.add(line);
                        }
                    }
                }
        }catch(Exception e){
            
        }
        return veriler;
    }
  
}

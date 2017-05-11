/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package advanceddataminingproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author umit
 */
public class KNN {
        int uzunluk=0;
        List<String> result_list=new ArrayList<>();
        List<Double> okliduzakliklari=new ArrayList<>();
 
    public List<Integer> getHatali_indexler(List<String> unclassified_set,List<String> classified_test){
        List<Integer> hatali_indexler=new ArrayList<>();
        
        String[] unclissifed;
        for (int i = 0; i < classified_test.size(); i++) {
            unclissifed=unclassified_set.get(i).split(",");
            int len=unclissifed.length-1;
            
            if(!unclissifed[len].equals(classified_test.get(i))){
                hatali_indexler.add(i);
            }
        }

        return hatali_indexler;
    }
    
    public List<String> getResultsList(List<String> test_set,List<String> classified_test){
        for(int i=0;i<classified_test.size();i++){
            result_list.add(test_set.get(i)+"=>"+classified_test.get(i));
        }
        return result_list;
    }
    
    public List<String> classify_with_KNN(int K,List<String> trainingSet,List<String> testSet,HashMap<String,Integer> valuesMap,List<String> hashmapkeys){
               
        List<String> classified_test=new ArrayList<>(); // sınıflandırılmış testin sınıf bilgileri burada
        
        
        
        List<String> egitimSeti=new ArrayList<>(); 
        List<String> testSeti=new ArrayList<>();
        egitimSeti=trainingSet;
        testSeti=testSet;
                
        int testSeti_uzunluk=testSeti.size();
        int egitimSeti_uzunluk=egitimSeti.size();
        
        String[] gelentestSeti=new String[0];
        String[] gelenegitimSeti=new String[0];
        
        for(int i=0;i<testSeti_uzunluk;i++){
            gelentestSeti=testSeti.get(i).split(",");
            double sum;
            for (int e=0;e<egitimSeti_uzunluk;e++){
                gelenegitimSeti=egitimSeti.get(e).split(",");
                sum=0;
                for(int b=0;b<gelentestSeti.length-1 ;b++)
                    sum+=Math.pow(Double.parseDouble(gelenegitimSeti[b])-Double.parseDouble(gelentestSeti[b]), 2);
                okliduzakliklari.add(Math.sqrt(sum));
            }
            
            
            List<Integer> indexler=new ArrayList<>();
            double min=999999999;
            int index=0;
            List<String> komsuSiniflari=new ArrayList<>();
            List<Double> komsuSiniflariOklidler=new ArrayList<>();
            double[] siniflarinOklidUzakliklari=new double[hashmapkeys.size()];
            
            for(int y=0;y<siniflarinOklidUzakliklari.length;y++){
                siniflarinOklidUzakliklari[y]=0.0;
            }
            
            System.out.println("----------");
            for(int c=0;c<K;c++){
                min=999999999;
                for(int j=0;j<okliduzakliklari.size();j++){
                    if(okliduzakliklari.get(j)<min){
                        if(!indexler.contains(j)){
                            min=okliduzakliklari.get(j);
                            index=j;
                        }
                    }
                }
                indexler.add(index);
                
                komsuSiniflari.add(egitimSeti.get(indexler.get(c)).split(",")[gelenegitimSeti.length-1]);
                System.out.println("komsu"+c+":"+ komsuSiniflari.get(c));
                
                komsuSiniflariOklidler.add(min);
                siniflarinOklidUzakliklari[Integer.parseInt(komsuSiniflari.get(c))-1]+=min; 
            }
            for(int t=0;t<siniflarinOklidUzakliklari.length;t++)
            System.out.println("uzaklik"+t+":"+ siniflarinOklidUzakliklari[t]);
            System.out.println("----------");
          
            
            // komsuluklardan en fazla bulunani bul
            HashMap<String,Integer> repetitions=new HashMap<>();
            
            for(int x=0;x<komsuSiniflari.size();x++){
                String clss=komsuSiniflari.get(x);
                
                if(repetitions.containsKey(clss))
                    repetitions.put(clss, repetitions.get(clss) + 1);
                else
                    repetitions.put(clss, 1);
                    
            }
            
            
            int max_sinif=0;
            String sinif_key="";
            for(Map.Entry<String, Integer> e : repetitions.entrySet()){
                if(e.getValue()>max_sinif){
                    max_sinif=e.getValue();
                    sinif_key=e.getKey();
                    System.out.println("deger:"+e.getValue()+ " key:"+ e.getKey());
                }else if(e.getValue()==max_sinif){
                    System.out.println("esitlik var.");
                    System.out.println("egetkey:"+e.getKey()+" egetvalue:"+e.getValue());
                    System.out.println("egetkey okliduzak.:"+siniflarinOklidUzakliklari[Integer.parseInt(e.getKey())-1]);
                    System.out.println("egetkey okliduzak.:"+siniflarinOklidUzakliklari[max_sinif]);
                    
                    if(siniflarinOklidUzakliklari[Integer.parseInt(e.getKey())-1]<siniflarinOklidUzakliklari[max_sinif]){
                        max_sinif=e.getValue();
                        sinif_key=e.getKey();
                    }
                }
            }
            classified_test.add(sinif_key);
            
            komsuSiniflari.clear();
            okliduzakliklari.clear();
        }
            
        return classified_test;
        }
    
    public List<String> cross_validation(int foldvalue,int K,List<String> veriler){
        List<String> test_datas=new ArrayList<>();
        List<String> train_datas=new ArrayList<>();
        List<String> hashmapkeys=new ArrayList<>();
        HashMap<String,Integer> valuesMap=new HashMap<>();
        List<String> classifiedtest=new ArrayList<>();
        List<String> resultListtest=new ArrayList<>();
        
        int foldnumber=veriler.size()/foldvalue;
        
        
        
        for (int i = 0; i < foldvalue; i++) {
            
            
            if(i==0){
                classifiedtest.clear();
                    for(int x=0;x<foldnumber;x++)
                       test_datas.add(veriler.get(x));
                    for(int y=foldnumber;y<veriler.size();y++)
                       train_datas.add(veriler.get(y));
                    
                    classifiedtest=this.classify_with_KNN(K, train_datas, test_datas, valuesMap, hashmapkeys);
                    resultListtest=this.getResultsList(test_datas, classifiedtest);
                    
                    
            }else if(i==foldvalue-1){
                   test_datas.clear();
                   train_datas.clear();
                   classifiedtest.clear();
                   
                   for(int x=veriler.size()-foldnumber;x<veriler.size();x++){
                       test_datas.add(veriler.get(x));
                   }
                   for(int y=0;y<veriler.size()-foldnumber;y++){
                       train_datas.add(veriler.get(y));
                   }
                   
                    classifiedtest=this.classify_with_KNN(K, train_datas, test_datas, valuesMap, hashmapkeys);
                    resultListtest=this.getResultsList(test_datas, classifiedtest);
                   
            }else{
                   test_datas.clear();
                   train_datas.clear();
                   classifiedtest.clear();
                   
                   for(int x=i*foldnumber;x<i*foldnumber+foldnumber;x++){
                       test_datas.add(veriler.get(x));
                   }
                   for(int y=0;y<i*foldnumber;y++){
                       train_datas.add(veriler.get(y));
                   }
                   for(int y=i*foldnumber+foldnumber;y<veriler.size();y++){
                       train_datas.add(veriler.get(y));
                   }
                   
                    classifiedtest=this.classify_with_KNN(K, train_datas, test_datas, valuesMap, hashmapkeys);
                    resultListtest=this.getResultsList(test_datas, classifiedtest);
                   
            }
            
        }
        return resultListtest;
    }

    public List<Integer> cross_validation_hataliindexler(List<String> classified_set){
        List<Integer> hatali_indexler=new ArrayList<>();
        String ayir;
        for(int i=0;i<classified_set.size();i++){
            ayir=classified_set.get(i).substring(classified_set.get(i).length() -5);
            String class1=ayir.substring(ayir.length()-4,ayir.length()-3);
            String class2=ayir.substring(ayir.length()-1);            
            if(!class1.equals(class2))
                hatali_indexler.add(i);
        }
        
        return hatali_indexler;
    }
    
    public List<String> getwithRealClassNames(List<String> veriler,List<String> hashmapkeys){
        List<String> withRealClassName=new ArrayList<>();
        
        for(int a=0;a<veriler.size();a++){
               
            String[] ayrik=veriler.get(a).split("=>");
            String soldaki=ayrik[0];
            String soldaki_sinifsiz=soldaki.substring(0,soldaki.length()-1);
            String sagdaki=ayrik[1];
            String soldaki_sinif=soldaki.substring(soldaki.length()-1,soldaki.length());
            soldaki_sinif=hashmapkeys.get(Integer.parseInt(soldaki_sinif)-1);
            sagdaki=hashmapkeys.get(Integer.parseInt(sagdaki)-1);
               
           
            String sonhali=soldaki_sinifsiz+soldaki_sinif+"=>"+sagdaki;
            withRealClassName.add(sonhali);            
        }
        return withRealClassName;
    }
    
}
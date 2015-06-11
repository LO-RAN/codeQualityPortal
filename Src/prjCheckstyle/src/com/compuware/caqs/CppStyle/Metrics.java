package com.compuware.caqs.CppStyle;

import com.compuware.caqs.FileManager.*;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.BufferedWriter;
/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 3 mai 2006
 * Time: 17:28:59
 * To change this template use File | Settings | File Templates.
 */
public class Metrics {

    String[] contents;
    String[] snavContents;
    //tableau des fonctions
    String [][]tableFu;
    //tableau des fichiers
    String [][]tableFi;
    
    PrintWriter output;
    BufferedWriter outbuf;

    String directory;
    //nombre de mi et de fu
    int nbF;

    public Metrics(String dir){
    	
        directory=new String(dir);
        DirectoryManage directory=new  DirectoryManage(dir);
        String[] fileNames=directory.getFileNames(dir,"cpp");
        
        FileManage snav=new FileManage(Main.outDir+"\\fil_file.csv");
        this.snavContents=snav.getContents();
        nbF=0;
        this.tableFu=new String[snavContents.length][8];
        this.tableFi=new String[fileNames.length][4];
        //CParser.log.write("writing in -- method_metrics.csv");
        for(int i =0; i<snavContents.length;i++){
        	
            findFunc(i);
        }
        try{
        	
        output = new PrintWriter(new BufferedWriter(new FileWriter(Main.outDir+"\\function_metrics.csv")));
        
        output.println("Fichiers;Classe;méthodes/fonctions;debut;fin;LOC;VG;CLOC");
        	//System.out.println("Fichiers;Classe;méthodes/fonctions;debut;fin;LOC;VG;CLOC");
        for(int i =0; i<nbF;i++){
            collectCppMetrics(null,i);
        }
        output.close();
        output = new PrintWriter(new BufferedWriter(new FileWriter(Main.outDir+"\\file_metrics.csv")));
        output.println(fileNames.length+"Fichiers;LOC;VG;CLOC");
        for(int i=0;i<fileNames.length;i++){
            collectCppMetrics(fileNames[i],i);
        }
        output.close();
        }catch(Exception e){
        	e.printStackTrace(); 
        }

    }
    
    private String getParameters(int line){
        int deb=this.snavContents [line].lastIndexOf("{")+1;
        int fin=this.snavContents [line].indexOf("}",deb);
        return this.snavContents [line].substring(deb,fin);
    }
    
    private void findFunc(int line){

        //nombre de mi et de fu
       
        if(Constants.fu.matcher(this.snavContents [line]).matches() ||
                Constants.mi.matcher(this.snavContents [line]).matches() ||
                Constants.catchFu.matcher(this.snavContents [line]).matches()){
        	//System.out.println(this.snavContents [line]);
            
            int df1= this.snavContents [line].indexOf("{",10)+1;
            int df2= this.snavContents [line].indexOf(".",df1);
            
            int ef1= this.snavContents [line].indexOf(" ",df2)+1;
            int ef2= this.snavContents [line].indexOf(".",ef1);
            
            
            String j=this.snavContents [line].substring(df1,df2);
            String i=this.snavContents [line].substring(ef1,ef2);
            //System.out.println(j+" "+i);
            String fichier=this.snavContents [line].substring(
                    this.snavContents [line].indexOf("{",1)+1 ,
                    this.snavContents [line].indexOf("}")
            );
            //System.out.println("fichier : "+fichier);
            int dc=this.snavContents [line].indexOf("{",10)+1;
            //dc=this.snavContents [line].indexOf(" ",dc)+1;
            int fc=this.snavContents [line].indexOf(" ",dc)+1;
            String classe=this.snavContents [line].substring(dc,fc);
            String fonction="";
            if(Constants.fu.matcher(this.snavContents [line]).matches())
            	fonction=this.snavContents [line].substring(
            		this.snavContents [line].lastIndexOf(" ",this.snavContents [line].lastIndexOf(" fu",df1)-1),
                    this.snavContents [line].lastIndexOf(" fu",df1));
            
            if(Constants.mi.matcher(this.snavContents [line]).matches())
                fonction=this.snavContents [line].substring(
                		this.snavContents [line].lastIndexOf(" ",this.snavContents [line].lastIndexOf(" mi",df1)-1),
                        this.snavContents [line].lastIndexOf(" mi",df1));
            
            if(Constants.catchFu.matcher(this.snavContents [line]).matches()){
                //ici, on rattache le catch externe en donnant sa ligne de fin à la fonction précédente
                tableFu[nbF-1][4]=i;
            }else{
                tableFu[nbF][0]=fichier;
                tableFu[nbF][1]=classe;
                tableFu[nbF][2]=fonction+"("+this.getParameters(line)+")" ;
                tableFu[nbF][3]=i;
                tableFu[nbF][4]=j;
                //System.out.println(this.snavContents [line]+"\n"+tableFu[nbF][0]+"-"+tableFu[nbF][1]+"-"+tableFu[nbF][2]+"-"+tableFu[nbF][3]+"-"+tableFu[nbF][4]);
                nbF++;
            }
        }

    }

    private String getElement(int line, int elNum){
        return tableFu[line][elNum];
    }

    private void collectCppMetrics(String file,int line){
    	
        //booleen specifiant que la zone est implicitement commentée (en bloc)
        boolean isCommented=false;
        vg=1;
        //marqueur de début et de fin
        int df;
        int ff;
        try{
        //si file est null, alors on étudie par fonction, sinon, c'est le fichier en entier
        if(file==null){
            FileManage sourceFile=new FileManage(getElement(line,0));
            this.contents = sourceFile.getContents();
            df=new Integer(tableFu[line][3]).intValue();
            ff=new Integer(tableFu[line][4]).intValue();
           
        }else{
            FileManage sourceFile=new FileManage(file);
            this.contents = sourceFile.getContents();
            df=0;
            ff=this.contents.length;
        }

        //init métriques
        String v4="";
        int nbV4=0;
        int loc=0;
        int cloc=0;
        //int findReturn=0;
        //parcours du tableau contenant les lignes de code en fonction des marqueurs spécifiés
        for (int i =df;i<ff;i++){
        	if(file==null){
        	//if(Constants.returnKw.matcher(this.contents[i]).matches()){
        		//findReturn=i;
        		
        	//}
        	}
            if(Constants.blocCommKw.matcher(this.contents[i]).matches() || isCommented==true){
                isCommented=true;
                cloc++;

            }else{
            	//vg=0;
                findKw(this.contents[i]);
                if(this.contents[i].indexOf("\"")>0){
                	v4+=String.valueOf(i+1)+", ";
                	nbV4++;
                }
                if(!(Constants.noSourceComment.matcher(this.contents[i]).matches()
                        || Constants.blankLine.matcher(this.contents[i]).matches())
                ){

                    loc++;
                }
            }
            if(Constants.commKw.matcher(this.contents[i]).matches() && !isCommented){

                cloc++;

            }else{
                if(this.contents[i].indexOf("/*")>-1 && !isCommented){

                    cloc++;
                    isCommented=true;
                }
            }
            if(this.contents[i].indexOf("*/")>-1 ){

                isCommented=false;

            }
        }
        if(file==null){
            tableFu[line][5]=new Integer(loc).toString();
            tableFu[line][6]=new Integer(vg).toString();
            tableFu[line][7]=new Integer(cloc).toString();
            //if(findReturn == 0){
            	//System.out.println(" $ "+this.contents[df-1]);
            //}
            output.println(tableFu[line][0]+";"+tableFu[line][1]+";"+tableFu[line][2]+";"+df+";"+ff+";"+tableFu[line][5]+";"+tableFu[line][6]+";"+tableFu[line][7]);
            
        }else{

            tableFi[line][0]=file;
            tableFi[line][1]=new Integer(loc).toString();
            tableFi[line][2]=new Integer(vg).toString();
            tableFi[line][3]=new Integer(cloc).toString();
            System.out.println(tableFi[line][0]+" line "+v4+";V4;"+nbV4);
            output.println(tableFi[line][0]+";"+tableFi[line][1]+";"+tableFi[line][2]+";"+tableFi[line][3]);
        }
        }catch(Exception e){
        e.printStackTrace();	
        }
        
    }
    private static int vg=0;
    private void findKw(String chaine){
    	if(Constants.ifKw.matcher(chaine).matches()
                || Constants.if2Kw.matcher(chaine).matches()
                || Constants.questionKw.matcher(chaine).matches()
                || Constants.whileKw.matcher(chaine).matches()
                || Constants.while2Kw.matcher(chaine).matches()
                || Constants.forKw.matcher(chaine).matches()
                || Constants.for2Kw.matcher(chaine).matches()
                || Constants.doKw.matcher(chaine).matches()
                || Constants.do2Kw.matcher(chaine).matches()
                || Constants.caseKw.matcher(chaine).matches()
               // || Constants.orKw.matcher(chaine).matches()
               // || Constants.andKw.matcher(chaine).matches()
                || Constants.switchKw.matcher(chaine).matches()
                || Constants.tryKw.matcher(chaine).matches()
                || Constants.catchKw.matcher(chaine).matches()
        ){
    		vg++;
        	//System.out.println("find Kw : "+chaine+" : "+vg);
        	
        	if(Constants.ifKw.matcher(chaine).matches() || Constants.if2Kw.matcher(chaine).matches()){
        		findKw(chaine.substring(chaine.indexOf("if")+2));
        	}
        	if(Constants.whileKw.matcher(chaine).matches() || Constants.while2Kw.matcher(chaine).matches()){
        		findKw(chaine.substring(chaine.indexOf("while")+5));
        	}
        	if(Constants.forKw.matcher(chaine).matches() || Constants.for2Kw.matcher(chaine).matches()){
        		findKw(chaine.substring(chaine.indexOf("for")+3));
        	}
            
        }
    	
    }
    
}
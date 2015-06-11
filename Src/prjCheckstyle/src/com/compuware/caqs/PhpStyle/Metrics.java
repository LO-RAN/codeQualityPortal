package com.compuware.caqs.PhpStyle;

import java.util.regex.Pattern;

import com.compuware.caqs.FileManager.*;
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

    String directory;
    //nombre de mi et de fu
    int nbF;

    public Metrics(String dir){
        directory=new String(dir);
        DirectoryManage directory=new  DirectoryManage(dir);
        String[] fileNames=directory.getFileNames(dir,"php");
        
        FileManage snav=new FileManage(this.directory+"\\fil_file.csv");
        this.snavContents=snav.getContents();
        nbF=0;
        this.tableFu=new String[snavContents.length][8];
        this.tableFi=new String[fileNames.length][4];
        
        for(int i =0; i<snavContents.length;i++){
            findFunc(i);
        }
        System.out.println("Fichiers;Classe;méthodes/fonctions;debut;fin;LOC;VG;CLOC");
        for(int i =0; i<nbF;i++){
            collectCppMetrics(null,i);
        }
        System.out.println("");
        System.out.println("Fichiers;LOC;VG;CLOC");
        for(int i=0;i<fileNames.length;i++){
        	//System.out.println("collect du fichier : "+fileNames[i]);
            collectCppMetrics(fileNames[i],i);
        }

    }
    public String getParameters(int line){
        int deb=this.snavContents [line].lastIndexOf("{")+1;
        int fin=this.snavContents [line].indexOf("}",deb);
        return this.snavContents [line].substring(deb,fin);
    }
    private void findFunc(int line){

        //nombre de mi et de fu
        //System.out.println(this.snavContents [line]);
    	//System.out.println(this.snavContents [line]);
        if(PHPConstants.fu.matcher(this.snavContents [line]).matches() ||
                PHPConstants.mi.matcher(this.snavContents [line]).matches() ||
                PHPConstants.catchFu.matcher(this.snavContents [line]).matches()){
            int df= this.snavContents [line].indexOf("}");
            int df2= this.snavContents [line].indexOf(".",df);
            int df1= this.snavContents [line].indexOf("}")+2 ;
            
            int ef1= this.snavContents [line].indexOf("{",df2);
            int ef2= this.snavContents [line].indexOf(".",ef1);
            
            String i=this.snavContents [line].substring(df1,df2);
            String j=this.snavContents [line].substring(ef1+1,ef2);
            
            String fichier=this.snavContents [line].substring(
                    this.snavContents [line].indexOf(this.directory)+this.directory.length()+4 ,
                    this.snavContents [line].indexOf("}")
            );
            int dc=this.snavContents [line].indexOf(" ",df1)+1;
            int fc=this.snavContents [line].indexOf(" ",dc)+1;
            String classe=this.snavContents [line].substring(dc,fc);
            String fonction=this.snavContents [line].substring(
                    fc,
                    this.snavContents [line].indexOf(" ",fc+2));

            if(PHPConstants.catchFu.matcher(this.snavContents [line]).matches()){
                //ici, on rattache le catch externe en donnant sa ligne de fin à la fonction précédente
                tableFu[nbF-1][4]=j;
            }else{
                tableFu[nbF][0]=fichier;
                tableFu[nbF][1]=classe;
                tableFu[nbF][2]=fonction+"("+this.getParameters(line)+")" ;
                tableFu[nbF][3]=i;
                tableFu[nbF][4]=j;
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

        //marqueur de début et de fin
        int df;
        int ff;

        //si file est null, alors on étudie par fonction, sinon, c'est le fichier en entier
        if(file==null){
        //	System.out.println(this.directory+"\\"+getElement(line,0));
            FileManage sourceFile=new FileManage(this.directory+"\\"+getElement(line,0));
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
        int vg=1;
        int loc=0;
        int cloc=0;

        //parcours du tableau contenant les lignes de code en fonction des marqueurs spécifiés
        for (int i =df;i<ff;i++){
            if(PHPConstants.blocCommKw.matcher(this.contents[i]).matches() || isCommented==true){
                isCommented=true;
                cloc++;

            }else{
                if(PHPConstants.ifKw.matcher(this.contents[i]).matches()
                        || PHPConstants.elseIfKw.matcher(this.contents[i]).matches()
                        || PHPConstants.questionKw.matcher(this.contents[i]).matches()
                        || PHPConstants.whileKw.matcher(this.contents[i]).matches()
                        || PHPConstants.doKw.matcher(this.contents[i]).matches()
                        || PHPConstants.forKw.matcher(this.contents[i]).matches()
                        || PHPConstants.foreachKw.matcher(this.contents[i]).matches()
                        || PHPConstants.doKw.matcher(this.contents[i]).matches()
                        || PHPConstants.caseKw.matcher(this.contents[i]).matches()
                        || PHPConstants.orKw.matcher(this.contents[i]).matches()
                        || PHPConstants.andKw.matcher(this.contents[i]).matches()
                        || PHPConstants.ORKw.matcher(this.contents[i]).matches()
                        || PHPConstants.XORKw.matcher(this.contents[i]).matches()
                        || PHPConstants.ANDKw.matcher(this.contents[i]).matches()
                        
                ){
                	
                    vg++;
                }
                

                if(!(PHPConstants.noSourceComment.matcher(this.contents[i]).matches()
                        || PHPConstants.blankLine.matcher(this.contents[i]).matches())
                ){
                	
                    loc++;
                }
            }
            if(PHPConstants.commKw.matcher(this.contents[i]).matches() && !isCommented){
            	
            	if(this.contents[i].indexOf("//")>=0){
            		//System.err.println("comm line : "+this.contents[i]);
                cloc++;
            	}
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

            System.out.println(tableFu[line][0]+";"+tableFu[line][1]+";"+tableFu[line][2]+";"+df+";"+ff+";"+tableFu[line][5]+";"+tableFu[line][6]+";"+tableFu[line][7]);
        }else{

            tableFi[line][0]=file;
            tableFi[line][1]=new Integer(loc).toString();
            tableFi[line][2]=new Integer(vg).toString();
            tableFi[line][3]=new Integer(cloc).toString();
            System.out.println(tableFi[line][0]+";"+tableFi[line][1]+";"+tableFi[line][2]+";"+tableFi[line][3]);
        }
    }
}

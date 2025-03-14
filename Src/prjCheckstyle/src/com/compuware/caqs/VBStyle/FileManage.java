package com.compuware.caqs.VBStyle;

import com.compuware.caqs.PL1Style.Constants;

import java.io.*;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 24 mars 2006
 * Time: 15:20:33
 * Gestion des fichiers et de leur contenu.
 */
public class FileManage {
    private String fileName;
    private BufferedReader buffer;
    private String[] conts;
    private String[] contentsWithoutBlanks;
    private String[] contentsWithoutBlanksAndComments;

    FileManage(String name){
        this.fileName = name;
        
        this.openFile();
        this.conts=this.setContents();
        this.contentsWithoutBlanks =this.setContentsWithoutBlanks();
        this.contentsWithoutBlanksAndComments =this.setContentsWithoutBlanksAndComments();
    }

    private void openFile(){

        try{

            if(new File(this.fileName).isFile() && new File(this.fileName).canRead() ){
                this.buffer = new BufferedReader(new FileReader(new File(this.fileName)));

            }
        }catch(Exception e){
        	System.out.println("le fichier ne correspond pas! "+this.fileName );
            e.printStackTrace();
        }
    }

    private String[] setContents(){
        ArrayList l = new ArrayList();
        String s=null;
       // int passAttribute=0;
        try{
        	//System.out.println("lecture du fichier "+this.fileName );
            while((s=this.buffer.readLine()) != null){
               // System.out.println(s);
               // if(s.indexOf("Attribute ")>-1 && passAttribute==0){
                 //   System.out.println("Attribute trouv� en ligne : "+s);
                  //  passAttribute=1;
                //}
                //else{
                  //  if(passAttribute==1){
                       // System.out.println("dernier Attribute trouv� en ligne : "+s);
                    //    passAttribute=2;
                    //}
                //}
                //if(passAttribute==2 && s.indexOf("Attribute ")<0){
                   
                l.add(s);
                //}

            }
        }catch(IOException e){
            e.printStackTrace();
        }
        String[] t = new String[0];
        return (String[]) l.toArray(t);
    }

    public String[] getContents(){
        return this.conts;
    }

    private String[] setContentsWithoutBlanks(){
        ArrayList l = new ArrayList();
        int i=0;


        try{
            while(i<this.conts.length ){
                if(Constants.blankLine.matcher(this.conts[i]).matches()){
                    l.add(this.conts[i]);

                }
                i=i+1;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        String[] t = new String[0];
        return (String[]) l.toArray(t);
    }

    public String[] getContentsWithoutBlanks(){
        return this.contentsWithoutBlanks;
    }

    private String[] setContentsWithoutBlanksAndComments(){
        ArrayList l = new ArrayList();
        int i=0;

        try{
            while(i<this.conts.length ){

                if(!Constants.blankLine.matcher(this.conts[i]).matches()
                        &&!Constants.CommentLine.matcher(this.conts[i]).matches()){
                    l.add(this.conts[i]);

                }
                i=i+1;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        String[] t = new String[0];
        return (String[]) l.toArray(t);
    }

    public String[] getContentsWithoutBlanksAndComments(){
        return this.contentsWithoutBlanksAndComments;
    }

    public int getFileLength(){
        return this.conts.length;
    }

    public int getFileLengthWithoutBlanks(){
        return this.contentsWithoutBlanks.length;
    }

    public int getFileLengthWithoutBlanksAndComments(){
        return this.contentsWithoutBlanksAndComments.length;
    }
}

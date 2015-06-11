/*
 * Baseline.java
 *
 * @author  cwfr-dzysman
 */

package com.compuware.caqs.business.load;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConvertDevPartner {


    public ConvertDevPartner() {

    }

    public boolean convertion(String input, String output) {
        boolean retour = true;
        if ((input == null) || (output == null)) {
            retour = false;
        } else {
            try {
                FileReader fd = new FileReader(input);
                BufferedReader br = new BufferedReader(fd);
                String line = null;
                FileWriter fw = new FileWriter(output);
                BufferedWriter bw = new BufferedWriter(fw);
                br.readLine();//on passe l'entête.
                bw.write("methodname;pctcovered;called;nblinesnotexecuted");
                bw.newLine();
                while ((line = br.readLine()) != null) {
                    //on remplace les , de séparation par des ; en prenant soin de conserver les , séparant les
                    //paramètres de méthodes.
                    int parenthese = 0;
                    boolean dansGuillemet=false;
                    for (int i = 0; i < line.length(); i++) {
                    	if(line.charAt(i) == '\"') {
                    		dansGuillemet = !dansGuillemet;
                    	}
                        if (line.charAt(i) == ',') {
                            if (parenthese == 0 && !dansGuillemet) {
                                line = line.substring(0, i) + ";" + line.substring(i + 1);
                            }
                        }
                        if (line.charAt(i) == '('){
                            parenthese++;
                        }
                        if (line.charAt(i) == ')') {
                            parenthese--;
                        }
                    }
                    String out = line.replaceAll("\"", "");
                    out = out.replaceAll("%", "");
                    out = out.replaceAll(" ", "");
                    out = out.replaceAll("\\([a-z\\.]*", "(");
                    out = out.replaceAll(",[a-z\\.]*", ",");
                    bw.write(out, 0, out.length());
                    bw.newLine();
                }
                bw.close();
                fw.close();
                br.close();
                fd.close();

            } catch (IOException exc) {
                retour = false;
                System.out.println(exc);
            }
        }
        return retour;
    }




}

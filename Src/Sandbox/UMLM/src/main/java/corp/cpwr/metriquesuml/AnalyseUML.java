/*
 * MainClass.java
 *
 * Created on 7 juin 2004, 10:00
 */

package corp.cpwr.metriquesuml;
import java.util.*;
/**
 *
 * @author  cwfr-dzysman
 */
public class AnalyseUML {
    private static int NB_ARGS_INIT = 1;
    private String errorMessage = "";
    
    public AnalyseUML() {
    }
    
    public boolean allAnalyse(int typeModelisateur, Collection<String> files, String output) {
        boolean retour = true;
        if(files.isEmpty()) {
            errorMessage = "Aucun fichier donné à analyser.";
            retour = false;
        }
        if(retour) {
            FonctionsParser.setParseur(typeModelisateur);
            try {
                boolean first = true;
                for(Iterator it = files.iterator(); it.hasNext() && retour; ) {
                    String fichier = (String) it.next();
                    Modele m = new Modele();
                    if(!m.startAnalyse(fichier, output, first)) {
                        retour = false;
                        switch(m.getLastError()) {
                            case Modele.FILENOTFOUND:
                                errorMessage += " Le fichier "+fichier+" n'a pas été trouvé.";
                                break;
                            case Modele.IMPOSSIBLEWRITE:
                                errorMessage += " Impossible d'écrire le fichier CSV.";
                                break;
                            case Modele.LOADMODELIMPOSSIBLE:
                                errorMessage += " Chargement du modèle impossible.";
                                break;
                            case Modele.JDOMERROR:
                                errorMessage += " Erreur JDOM.";
                                break;
                            case Modele.NOXMI:
                                errorMessage += " Erreur : pas de fichier XMI.";
                                break;
                            default:
                                errorMessage += " Erreur non définie.";
                                break;
                        }
                    }
                    first = false;
                }
            }catch(java.lang.StackOverflowError e) {
                e.printStackTrace();
            }
        }
        return retour;
    }
    
    public String getErrorMessage() {return errorMessage;}
    
    /**
     * Cette méthode est le main du projet.
     * @param args les arguments à envoyer au projet.
     */
    public static void main(String[] args) {
        int nb_args_init = 0;
        int typeModelisateur = -1;
        List<String> files = new ArrayList<String>();
        
        if(args.length==0) {
            FonctionsParser.setParseur(FonctionsParser.ENTREPRISE_ARCHITECT);
        } else {
            try {
                for(int i=0; i<args.length; i++) {
                    if(args[i].startsWith("-modelisateur=")) {
                        String parser = args[i].substring(14);
                        if(parser.equals("EA_3.60")) {
                            typeModelisateur = FonctionsParser.ENTREPRISE_ARCHITECT;
                            nb_args_init++;
                        }
                    }
                    if(args[i].startsWith("-file=")) {
                        if(nb_args_init!=NB_ARGS_INIT) {
                            break;
                        }
                        String file = args[i].substring(6);
                        files.add(file);
                    }
                }
            }catch(java.lang.StackOverflowError e) {
                e.printStackTrace();
            }
            AnalyseUML mc = new AnalyseUML();
            if(mc.allAnalyse(typeModelisateur, files, "d:\\outputdirectory\\")) {
                System.out.println("les analyses se sont bien passées");
            }
        }
    }
    
    
}

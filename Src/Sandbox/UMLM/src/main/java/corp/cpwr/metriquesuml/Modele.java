/*
 * Modele.java
 *
 * Created on 10 mai 2004, 17:10
 */

package corp.cpwr.metriquesuml;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import java.io.*;
import java.util.*;
import corp.cpwr.metriquesuml.Diagrammes.*;
import corp.cpwr.metriquesuml.Liens.*;
import corp.cpwr.metriquesuml.Objets.*;

/**
 * Cette classe d�finie un mod�le UML complet. De plus, elle contient la fonction
 * <code>main</code> n�cessaire � l'ex�cution du projet.
 * @author  cwfr-dzysman
 */
public class Modele extends XMIObject{
    /** Le num�ro de version, commun � tous les �l�ments du mod�le*/
    public static float    numeroVersion = 0.0f;
    
    /********************** LES METRIQUES ****************************************/
    //toutes les moyennes par diagrammes se font en fonction des diagrammes concern�s.
    //le nombre d'acteurs dans le mod�le. Il n'y a pas de doublons dans le comptage.
    //-> moyenne par diagrammes contenant des acteurs.
    private float    nombreActeursModele       = 0.0f;
    //le nombre d'associations de type agr�gation ou composition dans le mod�le.
    //-> moyenne par diagrammes contenant des agr�gations ou compositions.
    private float    nombreAgregationsModele   = 0.0f;
    //le nombre de liens total dans le mod�le (sauf les messages).
    //-> moyenne par diagrammes.
    private float    nombreAssociationsModele  = 0.0f;
    //le nombre de classes dans le mod�le (objets de type CLASSEDC).
    //-> moyenne par nombre de diagrammes de classes.
    private float    nombreClassesModele       = 0.0f;
    //le nombre de relations d'h�ritage dans le mod�le.
    //-> moyenne par diagrammes.
    private float    nombreHeritagesModele     = 0.0f;
    //le nombre de messages dans le mod�le.
    //-> moyenne par diagrammes.
    private float    nombreMessagesModele      = 0.0f;
    //le nombre de packages dans le mod�le.
    //-> moyenne par diagrammes.
    private float    nombrePackagesModele      = 0.0f;
    //le nombre d'objets dans le mod�le.
    //-> moyenne par diagrammes.
    private float    nombreObjetsModele        = 0.0f;
    //le nombre de cas d'utilisations dans le mod�le.
    //->moyenne par diagrammes.
    private float    nombreUseCasesModele      = 0.0f;
    //nombre de m�thodes document�es.
    //->ratio entre le nombre de m�thodes document�es et le nombre de m�thodes.
    private float    nombreMethodesDocumentees = 0.0f;
    //nombre d'�tats (pseudos et simples et �v�nements) dans le mod�le.
    //->moyenne par diagrammes.
    private float    nombreEtatsModele         = 0.0f;
    //nombre de types d�finis dans le mod�le.
    //->total dans le mod�le.
    private int      nombreDataType            = 0;
    //nombre moyen d'op�rations par classe.
    //->moyenne par classe.
    private float    nombreMoyenOperationsClasses = 0.0f;
    //nombre moyen pond�r� d'op�rations par classe.
    //->moyenne pond�r�e par classe. poids : 0 private, 0.5 proteected, 1 public.
    private float    nombreMoyenPondereOperationsClasses = 0.0f;
    //nombre moyen de m�thodes surcharg�es par classe.
    //->moyenne par classe.
    private float    nombreMoyenMethodesSurchargees = 0.0f;
    //nombre moyen d'acteurs par use case.
    //->moyenne par use case.
    private float    nombreMoyenActeursUseCase = 0.0f;
    //nombre moyen de messages par use case.
    //->moyenne par use case.
    private float    nombreMoyenMessagesUseCase = 0.0f;
    //nombre moyen d'associations par classe.
    //->moyenne par classe.
    private float    nombreMoyenAssociationsClasse = 0.0f;
    //nombre moyen pond�r� d'attributs par classe.
    //->moyenne pond�r�e par classe. poids : 0 private, 0.5 protected, 1 public.
    private float    nombreMoyenPondereAttributsClasse = 0.0f;
    //nombre moyen d'attributs par classe.
    //->moyenne par classe.
    private float    nombreMoyenAttributsClasse = 0.0f;
    //nombre moyen de classes instanci�es.
    //->ratio entre le nombre de classes instanci�es et le nombre de classes.
    private float    nombreMoyenInstances = 0.0f;
    //nombre moyen de commentaires dans les diagrammes.
    //->moyenne par diagrammes.
    private float    nombreCommentairesMoyen = 0.0f;
    //profondeur moyenne d'une classe.
    //->moyenne par classe.
    private float    profondeurMoyenneClasses = 0.0f;
    //nombre moyen de classes filles.
    //->moyenne par classes.
    private float    nombreFillesMoyen = 0.0f;
    //nombre moyen de descendants pour une classe.
    //->moyenne par classe.
    private float    nombreDescendantsMoyen = 0.0f;
    //couplage moyen d'une classe.
    //->moyenne par classe.
    private float    couplageMoyen = 0.0f;
    //complexit� moyenne d'un package.
    //->moyenne par package.
    private float    complexitePackages = 0.0f;
    //complexite moyenne d'un diagramme.
    //->moyenne par diagramme.
    private float    complexiteDiagrammes = 0.0f;
    //complexite cyclomatique moyenne d'une m�thode, si document�e.
    //->moyenne par m�thode document�e.
    private float    complexiteCyclomatiqueMoyenne = 0.0f;
    //nombre moyen de m�thodes h�rit�es par une classe.
    //->moyenne par classe.
    private float    nombreMoyenMethodesHeritees = 0.0f;
    //valeur moyenne de la m�trique rfc.
    //->moyenne par classe.
    private float    rfcMoyenne = 0.0f;
    //nombre moyen d'interfaces impl�ment�es par une classe.
    //->moyenne par classe.
    private float    nombreMoyenInterfacesImplementees = 0.0f;
    //nombre de diagrammes.
    //->total sur l'ensemble du mod�le.
    private int      nombreDiagrammes = 0;
	//le nombre de packages de classes
	//->total sur l'ensemble du mod�le.
    private float      nombrePackagesClasses = 0;
    private int nbDiagrammesClasses = 0;
        //diagramme d'�tats-transitions.
    private int nbDiagrammesET = 0;
        //diagrammes de collaboration.
    private int nbDiagrammesColl = 0;
    //diagrammes de cas d'utilisations.
    private int nbDiagrammesUC = 0;
        //diagrammes de s�quences.
    private int nbDiagrammesSeq = 0;
        //diagrammes d'activit�s.
    private int nbDiagrammesAct = 0;
   
    private int      lastError = -1;
    public static final int FILENOTFOUND = 0;
    public static final int IMPOSSIBLEWRITE = 1;
    public static final int LOADMODELIMPOSSIBLE = 2;
    public static final int JDOMERROR = 3;
    public static final int NOXMI = 4;
    
    /**
     * Ce constructeur permet l'instanciation d'un mod�le mais ne lance pas le
     * parsing d'un fichier XMI.
     */
    public Modele() {
        super(null, XMIObject.MODELE, null, XMIObject.NOT_DEFINED, true);
    }
    
    /**
     * Cette m�thode retourne le num�ro de version.
     * @return la version du mod�le
     */
    public static float getNumeroVersion() {return numeroVersion;}
    public static void  setNumeroVersion(float num) {numeroVersion = num;}
    
    
    public void blacklisterDiagrammes(Element root, ArrayList allElements) {
        //on blacklist tous les packages racines de diagrammes non utilis�s.
        for(Iterator it=allElements.iterator(); it.hasNext(); ) {
            Element e = (Element) it.next();
            boolean utilise = false;
            Element elt = FonctionsParser.findElement(root, "Package", "xmi.id", e.getAttributeValue("owner"));
            for(Iterator it2=objets.iterator(); it2.hasNext() && !utilise; ) {
                Diagramme diag = (Diagramme)it2.next();
                if(diag.getRootElement()==elt) utilise=true;
            }
            if(!utilise) {
                for(Iterator it2=objets.iterator(); it2.hasNext(); ) {
                    Diagramme diag = (Diagramme)it2.next();
                    diag.addInBlackList(elt);
                }
            }
        }
    }
    /**
     * Cette m�thode recherche tous les diagrammes dans un arbre g�n�r� par JDOM et
     * repr�sentant un flux XMI.
     * @param root La racine de l'arbre g�n�r� par JDOM et repr�sentant un flux XMI.
     */
    public void determinerDiagrammes(Element root) {
        int             type_diag = -1;
        
        ArrayList allElements = FonctionsParser.findAllBalises(root, "Diagram");
        for(Iterator it = allElements.iterator(); it.hasNext(); ) {
            Element e = (Element) it.next();
            
            String  name = e.getAttributeValue("name");
            
            String temp = e.getAttributeValue("diagramType");
            if(temp.equals("ClassDiagram")) type_diag = XMIObject.DIAGRAMME_CLASSE;//diagramme de classes
            else if(temp.equals("UseCaseDiagram")) type_diag = XMIObject.DIAGRAMME_UC;//diagramme de cas d'utilisation
            else if(temp.equals("StateDiagram")) type_diag = XMIObject.DIAGRAMME_ET;//diagramme d'�tats-transitions
            else if(temp.equals("ActivityDiagram")) type_diag = XMIObject.DIAGRAMME_ACT;//diagramme d'activit�s
            else if(temp.equals("SequenceDiagram")) type_diag = XMIObject.DIAGRAMME_SEQ;//diagramme de s�quences
            else if(temp.equals("CollaborationDiagram")) type_diag = XMIObject.DIAGRAMME_COLL;//diagramme de collaboration
            else continue;
            
            boolean dejaInsere = false;
            //tout d'abord, on analyse les diagrammes d�j� ins�r�s pour savoir si le diagramme a d�j� �t� ins�r�,
            //m�me sous un autre nom.
            for(Iterator enum2 = objets.iterator(); enum2.hasNext() && !dejaInsere; ) {
                XMIObject o = (XMIObject)enum2.next();
                if(o.getTypeObjet()==type_diag)
                    dejaInsere = ((Diagramme)o).getRootElement().getAttributeValue("xmi.id").equals(e.getAttributeValue("owner"));
            }
            
            //s'il n'a pas d�j� �t� ins�r�.
            if(!dejaInsere) {
                Element elt = FonctionsParser.findElement(root, "Package", "xmi.id", e.getAttributeValue("owner"));
                Diagramme diag = null;
                switch(type_diag) {
                    case XMIObject.DIAGRAMME_COLL:
                    case XMIObject.DIAGRAMME_UC:
                    case XMIObject.DIAGRAMME_CLASSE:
                        diag = new DiagrammeCUC(elt, name, this, type_diag);
                        break;
                    case XMIObject.DIAGRAMME_ET:
                        diag = new DiagrammeET(elt, name, this);
                        break;
                    case XMIObject.DIAGRAMME_ACT:
                        diag = new DiagrammeActivites(elt, name, this);
                        break;
                    case XMIObject.DIAGRAMME_SEQ:
                        diag = new DiagrammeSequence(elt, name, this);
                        break;
                }
                if(diag!=null)  objets.add(diag);
            }
        }
        
        blacklisterDiagrammes(root, allElements);
        verificationParentee(root, allElements);
    }
    
    public void verificationParentee(Element root, ArrayList allElements) {
        //maintenant, il faut v�rifier si un diagramme est en fait un package "fils" d'un autre diagramme.
        int taille = objets.size();
        for(int i=0; i<taille; i++) {
            Diagramme diag1 =(Diagramme)objets.get(i);
            Element root01 = diag1.getRootElement();
            for(int j=0; j<taille; j++) {
                Diagramme diag2 = (Diagramme)objets.get(j);
                Element root02 = diag2.getRootElement();
                if( (root01.isAncestor(root02)) && (root01!=root02) ) {
                    if(diag1.getTypeObjet()==diag2.getTypeObjet()) {
                        objets.remove(j);
                        j--;
                        taille--;
                        //on le rajoute dans la whitelist, ce qui le retirera de la blacklist s'il y est d'une mani�re ou d'une autre.
                        diag1.addInWhiteList(root02);
                    }
                    else {
                        diag1.addInBlackList(root02);
                    }
                }
            }
        }
    }
    
    /**
     * Cette m�thode lance l'analyse de tous les diagrammes trouv�s dans un mod�le
     * et reconnus. L'analyse se fait de la fa�on suivante :
     * <ul>
     * <li>Les diagrammes sont analys�s depuis le fichier XMI.
     * <li>Les liens "dynamiques" sont cr��s (liens d'associations entre objets).
     * <li>Les m�triques sont calcul�es.
     * <li>La base de donn�es est remplie.
     * </ul>
     */
    public void processAllDiagrams() {
        for(Iterator e=objets.iterator(); e.hasNext(); ) {
            ((Diagramme)e.next()).analyseDiagramme();
        }
        
        for(Iterator e=objets.iterator(); e.hasNext(); ) {
            ((Diagramme)e.next()).fillAll();
        }
        
        for(Iterator e=objets.iterator(); e.hasNext(); ) {
            ((Diagramme)e.next()).calculerMetriques();
        }
        
        calculerMetriques();
    }
    
    /**
     * Cette m�thode lance le parsing d'un fichier XMI donn� en param�tre et
     * lance imm�diatement apr�s l'analyse compl�te du mod�le pars�.
     *
     * @param file Le fichier contenant le flux XMI � parser.
     * @return Un bool�en indiquant la r�ussite (<code>true</code>) ou l'�chec
     *         (<code>false</code>) du parsing. Un �chec peut signifier que le
     *         fichier n'existe pas ou qu'il n'est pas parsable.
     */
    public boolean loadModeleFomFile(String file) {
        boolean    retour = true;
        Document   doc        = null;
        SAXBuilder saxbuilder = new SAXBuilder();
        //le builder va parser le fichier donn� en param�tre
        try{
            doc = saxbuilder.build(file);
            if(doc==null) {
                lastError = Modele.FILENOTFOUND;
                retour = false;
            }
            else {
                Element e = FonctionsParser.findFirstBalise(doc.getRootElement(), "Model");
                if(e!=null) {
                    setRootElement(e);
                    determinerDiagrammes(doc.getRootElement());
                    processAllDiagrams();
                }
                else {
                    retour = false;
                    lastError = Modele.NOXMI;
                }
            }
        }
        catch(org.jdom.JDOMException e) {
            lastError = Modele.JDOMERROR;
            retour = false;
        }
        catch(java.io.IOException e) {
            lastError = Modele.FILENOTFOUND;
            retour = false;
        }
        return retour;
    }
    
    /**
     *
     */
    public void demarrerAnalyse(String fichier) {
        Document   doc        = null;
        SAXBuilder saxbuilder = new SAXBuilder();
        //le builder va parser le fichier donn� en param�tre
        try{
            doc = saxbuilder.build(fichier);
            if(doc==null) {
                Modele.setNumeroVersion(1.0f);
                return ;
            }
            Element e = FonctionsParser.findFirstBalise(doc.getRootElement(), "Analyse");
            if(e!=null) {
                String temp = e.getAttributeValue("finish");
                float f  = Float.parseFloat(e.getAttributeValue("version"));
                if(temp.equals("true")) {
                    f = f+1.0f;
                } else {
                    //la derni�re analyse n'a pas �t� finie.
                }
                Modele.setNumeroVersion(f);
            }
            else {
                Modele.setNumeroVersion(1.0f);
            }
        }
        catch(org.jdom.JDOMException e) {
        }
        catch(java.io.IOException e) {
            Modele.setNumeroVersion(1.0f);
        }
    }
    
    public void setAnalyse(String fichier, boolean finish, String path) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path+fichier));
            String line = "<?xml version=\"1.0\" encoding=\"windows-1252\" ?>";
            bw.write(line, 0, line.length());
            bw.newLine();
            line = "<Analyse version=\""+Modele.getNumeroVersion()+"\" finish=\""+finish+"\" xmlns:html=\"http://www.w3c.org/TR/REC-html40/\" />";
            bw.write(line, 0, line.length());
            
            bw.close();
        } catch(java.io.IOException exc) {
        }
    }
    
    public boolean startAnalyse(String fic, String output, boolean first) {
        boolean retour = false;
        int lastindex = fic.lastIndexOf('/');
        int l2 = fic.lastIndexOf('\\');
        if(l2>lastindex) lastindex = l2;
        String nom_fic = output+ fic.substring(lastindex+1)+".analyse";
        demarrerAnalyse(nom_fic);
        System.out.println("fichier de sortie : "+ nom_fic);
        setAnalyse(nom_fic, false, output);
        if(loadModeleFomFile(fic)) {
            if(writeCSV(output,first)) {
                setAnalyse(nom_fic, true, output);
                retour = true;
            }
            else {
                setAnalyse(nom_fic, false, output);
            }
        } else {
            setAnalyse(nom_fic, false, output);
        }
        return retour;
    }
    
    public DiagrammeActivites searchActivityDiagram(String n) {
        for(Iterator e=objets.iterator(); e.hasNext(); ){
            Diagramme d = (Diagramme)e.next();
            if(d.getTypeObjet()!=XMIObject.DIAGRAMME_ACT) continue;
            if(d.getName().equals(n)) return (DiagrammeActivites)d;
        }
        return null;
    }
    
    public void calculerMetriques() {
        //on r�cup�re l'ensemble des diagrammes que l'on place dans l'array 'diagrammes'.
        //on r�cup�re type par type et on stocke le nombre de chaque type.
        //diagramme de classe.
        ArrayList diagrammes = getAll(XMIObject.DIAGRAMME_CLASSE, false);
        nbDiagrammesClasses = diagrammes.size();
        //diagramme d'�tats-transitions.
        ArrayList temp = getAll(XMIObject.DIAGRAMME_ET, false);
        diagrammes.addAll(temp);
        nbDiagrammesET = temp.size();
        //diagrammes de collaboration.
        temp = getAll(XMIObject.DIAGRAMME_COLL, false);
        diagrammes.addAll(temp);
        nbDiagrammesColl = temp.size();
        //diagrammes de cas d'utilisations.
        temp = getAll(XMIObject.DIAGRAMME_UC, false);
        diagrammes.addAll(temp);
        nbDiagrammesUC = temp.size();
        //diagrammes de s�quences.
        temp = getAll(XMIObject.DIAGRAMME_SEQ, false);
        diagrammes.addAll(temp);
        nbDiagrammesSeq = temp.size();
        //diagrammes d'activit�s.
        temp = getAll(XMIObject.DIAGRAMME_ACT, false);
        diagrammes.addAll(temp);
        nbDiagrammesAct = temp.size();
        
        //on calcule la compl�xit� moyenne par diagramme.
        for(Iterator it=diagrammes.iterator(); it.hasNext(); ) {
            Diagramme diag = (Diagramme)it.next();
            complexiteDiagrammes += diag.getComplexite();
        }
        nombreDiagrammes = diagrammes.size();
        if(nombreDiagrammes!=0)
            complexiteDiagrammes = complexiteDiagrammes / nombreDiagrammes;
        
        //on r�cup�re l'ensemble des acteurs.
        ArrayList acteursModele = getAll(XMIObject.ACTOR, true);
        ArrayList al = new ArrayList();
        //on ne garde que ceux qui n'ont pas de doublons de nom.
        for(Iterator it=acteursModele.iterator(); it.hasNext(); ) {
            Actor act = (Actor) it.next();
            String actorName = act.getName();
            boolean trouve = false;
            for(Iterator it2=al.iterator(); it2.hasNext() && !trouve; ) {
                String s = (String) it2.next();
                if(actorName.equals(s)) trouve=true;
            }
            if(!trouve) al.add(actorName);
        }
        //on consid�re que deux acteurs de deux diagrammes ou packages diff�rents ayant le m�me nom sont
        //identiques.
        //on fait la moyenne.
        int somme = nbDiagrammesClasses+nbDiagrammesSeq;
        if(somme!=0) nombreActeursModele = al.size() / ((float)somme);
        
        //on calcule le nombre moyen de relations d'h�ritage dans un diagramme.
        if((nbDiagrammesClasses+nbDiagrammesUC)!=0)
            nombreHeritagesModele = ((float)(getAll(XMIObject.GENERALIZATION, true).size()/(nbDiagrammesClasses+nbDiagrammesUC)));
        
        //on r�cup�re tous les liens sauf les messages.
        nombreAssociationsModele = getAll(XMIObject.ASSOCIATION, true).size();
        nombreAssociationsModele += getAll(XMIObject.ASSOCIATIONUC, true).size();
        nombreAssociationsModele += getAll(XMIObject.LINKSUML, true).size();
        nombreAssociationsModele += getAll(XMIObject.TRANSITION, true).size();
        somme = nbDiagrammesClasses+nbDiagrammesET+nbDiagrammesColl+nbDiagrammesUC+nbDiagrammesSeq+nbDiagrammesAct;
        if(somme!=0) nombreAssociationsModele = ((float)(nombreAssociationsModele / somme));
        
        //on r�cup�re l'ensemble des classes du mod�le.
        ArrayList dcList = getAll(XMIObject.CLASSEDC, true);
        //on en fait la moyenne par diagramme de classes.
        if(nbDiagrammesClasses!=0) nombreClassesModele = ((float)(dcList.size()/nbDiagrammesClasses));
        //on ajoute � l'array l'ensemble des interfaces du mod�le.
        dcList.addAll(getAll(XMIObject.INTERFACEDC, true));
        int nbObjetsDC = dcList.size();
        
        //on calcule les moyennes suivantes:
        //le nombre moyen de m�thodes par classe.
        //le nombre moyen d'associations par classe.
        //la profondeur maximale moyenne d'une classe.
        //le nombre de classes filles moyen d'une classe.
        //le nombre de classes descendantes moyennes d'une classe.
        //le couplage entrant moyen d'une classe.
        //le couplage sortant moyen d'une classe.
        //le nombre moyen de m�thodes surcharg�es.
        //le nombre moyen de m�thodes h�rit�es.
        //le ratio de classes instanci�es.
        //la valeur moyenne de la m�trique RFC.
        //le nombre moyen d'interfaces qu'une classe impl�mente.
        for(Iterator it=dcList.iterator(); it.hasNext(); ) {
            DC dc = (DC) it.next();
            nombreMoyenOperationsClasses += (float)dc.getNbOperationsClasses();
            nombreMoyenAssociationsClasse += (float)dc.getNbAssociationsLiees();
            profondeurMoyenneClasses += (float)dc.getProfondeurMaximale();
            nombreFillesMoyen += (float)dc.getNombreFilles();
            nombreDescendantsMoyen += (float)dc.getNombreDescendants();
            couplageMoyen += (float)dc.getCouplageEntrant();
            nombreMoyenMethodesSurchargees += (float) dc.getNombreMethodesSurchargees();
            nombreMoyenMethodesHeritees += (float) dc.getNbOperationsHeritees();
            nombreMoyenInstances += (float) dc.getNbInstances();
            rfcMoyenne += (float)dc.getRFC();
            nombreMoyenInterfacesImplementees += (float) dc.getNbInterfacesImplementees();
        }
        
        if(nbObjetsDC!=0) {
            nombreMoyenOperationsClasses = nombreMoyenOperationsClasses / ((float)nbObjetsDC);
            nombreMoyenAssociationsClasse = nombreMoyenAssociationsClasse / ((float)nbObjetsDC);
            profondeurMoyenneClasses = profondeurMoyenneClasses / ((float)nbObjetsDC);
            nombreFillesMoyen = nombreFillesMoyen / ((float)nbObjetsDC);
            nombreDescendantsMoyen = nombreDescendantsMoyen / ((float)nbObjetsDC);
            couplageMoyen = couplageMoyen / ((float)nbObjetsDC);
            
            nombreMoyenMethodesSurchargees = nombreMoyenMethodesSurchargees / ((float)nbObjetsDC);
            nombreMoyenMethodesHeritees = nombreMoyenMethodesHeritees / ((float) nbObjetsDC);
            nombreMoyenInstances = nombreMoyenInstances / ((float) nbObjetsDC);
            nombreMoyenInterfacesImplementees = nombreMoyenInterfacesImplementees / ((float) nbObjetsDC);
            rfcMoyenne = rfcMoyenne / ((float) nbObjetsDC);
            //on calcule le nombre moyen (pond�r� et non) d'attributs d'une classe (seulement si le diagramme a des classes).
            ArrayList attributs = getAll(XMIObject.ATTRIBUT, true);
            nombreMoyenAttributsClasse = ((float)attributs.size())/((float)nbObjetsDC);
            for(Iterator it = attributs.iterator(); it.hasNext(); ) {
                Attribut attribut = (Attribut) it.next();
                switch(attribut.getVisibility()) {
                /*case XMIObject.PRIVATE:
                    nombreMoyenPondereAttributsClasse+=0.0f;
                    break;*/
                    case XMIObject.PROTECTED:
                        nombreMoyenPondereAttributsClasse+=0.5f;
                        break;
                    case XMIObject.PUBLIC:
                        nombreMoyenPondereAttributsClasse+=1.0f;
                        break;
                }
            }
            nombreMoyenPondereAttributsClasse = nombreMoyenPondereAttributsClasse / ((float)nbObjetsDC);
            
            //on calcule :
            //le nombre moyen pond�r� et non de m�thodes d'une classe.
            //le nombre de m�thodes document�es
            //la complexit� cyclomatique moyenne d'une m�thode, si elle est document�e.
            ArrayList operations = getAll(XMIObject.METHODE, true);
            for(Iterator it=operations.iterator(); it.hasNext(); ) {
                Methode methode = (Methode)it.next();
                if(methode.isDocumentee()) {
                    nombreMethodesDocumentees++;
                    complexiteCyclomatiqueMoyenne += (float)methode.getComplexiteCyclomatique();
                }
                switch(methode.getVisibility()) {
                /*case XMIObject.PRIVATE:
                    nombreMoyenPondereOperationsClasse+=0.0f;
                    break;*/
                    case XMIObject.PROTECTED:
                        nombreMoyenPondereOperationsClasses+=0.5f;
                        break;
                    case XMIObject.PUBLIC:
                        nombreMoyenPondereOperationsClasses+=1.0f;
                        break;
                }
            }
            nombreMoyenPondereOperationsClasses = nombreMoyenPondereOperationsClasses / ((float)nbObjetsDC);
            if(nombreMethodesDocumentees!=0) {
                complexiteCyclomatiqueMoyenne = complexiteCyclomatiqueMoyenne / ((float)nombreMethodesDocumentees);
            }
            
            //on calcule le nombre d'agr�gations/compositions par classe et le
            //nombre d'associations par classe.
            ArrayList vect = getAll(XMIObject.ASSOCIATIONDC, true);
            for(Iterator e2=vect.iterator(); e2.hasNext(); ) {
                Association a = (Association) e2.next();
                if(a.getTypeAssociation() == Association.AGGREGATION ||
                a.getTypeAssociation() == Association.COMPOSITION ) {
                    nombreAgregationsModele++;
                }
                else
                    nombreMoyenAssociationsClasse++;
            }
            nombreAgregationsModele = nombreAgregationsModele / ((float)nbObjetsDC);
            nombreMoyenAssociationsClasse = nombreMoyenAssociationsClasse / ((float)nbObjetsDC);
        }
        
        //on calcule maintenant le nombre moyen de messages par diagramme en contenant.
        somme = nbDiagrammesSeq+nbDiagrammesColl;
        if(somme!=0)
            nombreMessagesModele = ((float)(getAll(XMIObject.MESSAGE, true).size()/somme));
        //on calcule le nombre moyen d'objets dans les diagrammes.
        somme= nbDiagrammesClasses+nbDiagrammesColl+nbDiagrammesSeq;
        if(somme!=0)
            nombreObjetsModele = ((float)(getAll(XMIObject.OBJECT_UML, true).size()/somme));
        
        //on r�cup�re l'ensemble des packages.
        ArrayList packages = getAll(XMIObject.PACKAGEDC, true);
	nombrePackagesClasses = packages.size() / nbDiagrammesClasses;
        packages.addAll(getAll(XMIObject.PACKAGEUC, true));
        packages.addAll(getAll(XMIObject.PACKAGE_COLL, true));
        nombrePackagesModele = packages.size();
        //et on en calcule la complexit� moyenne.
        for(Iterator it=packages.iterator(); it.hasNext(); ) {
            complexitePackages+=((corp.cpwr.metriquesuml.Packages.PackageUML)it.next()).getComplexite();
        }
        if(nombrePackagesModele!=0)
            complexitePackages = ((float)(complexitePackages / nombrePackagesModele));
        nombrePackagesModele = nombrePackagesModele / (nbDiagrammesClasses+nbDiagrammesColl+nbDiagrammesUC);
        //on calcule le nombre moyen de cas d'utilisations par diagramme.
        ArrayList ucList = getAll(XMIObject.USECASE, true);
        if(nbDiagrammesUC!=0)
            nombreUseCasesModele = ucList.size()/((float)nbDiagrammesUC);
        
        //on calcule ensuite le nombre moyen d'acteurs associ�s � un cas d'utilisation
        //et de messages � un cas d'utilisation.
        for(Iterator it=ucList.iterator(); it.hasNext(); ) {
            UseCase uc = (UseCase) it.next();
            nombreMoyenActeursUseCase += (float)uc.getNbActeursAssocies();
            nombreMoyenMessagesUseCase += (float)uc.getNbMessagesAssocies();
        }
        if(nombreUseCasesModele!=0) {
            nombreMoyenActeursUseCase = nombreMoyenActeursUseCase / ((float)ucList.size());
            nombreMoyenMessagesUseCase = nombreMoyenMessagesUseCase / ((float)ucList.size());
        }
        
        //on calcule le nombre moyen d'�tats par diagramme.
        nombreEtatsModele = getAll(XMIObject.EVENT_ACT, true).size();
        nombreEtatsModele += getAll(XMIObject.PSEUDO_STATE, true).size();
        nombreEtatsModele += getAll(XMIObject.SIMPLE_STATE, true).size();
        somme = nbDiagrammesAct+nbDiagrammesET;
        if(somme!=0)
            nombreEtatsModele = ((float)(nombreEtatsModele / somme));
        
        //on r�cup�re le nombre de type de donn�es.
        nombreDataType = (FonctionsParser.findAllBalises(getRootElement(), "DataType")).size();
        //on calcule le nombre de commentaires moyen.
        somme = nbDiagrammesClasses+nbDiagrammesET+nbDiagrammesColl+nbDiagrammesUC+nbDiagrammesSeq+nbDiagrammesAct;
        if(somme!=0)  nombreCommentairesMoyen = ((float)((FonctionsParser.findAllBalises(getRootElement(), "Comment")).size() / somme));
    }
    
    public boolean writeCSV(String fic, boolean first) {
        boolean retour = true;
        try{
            BufferedWriter bw = null;
            String line = null;
            if(first) {
                //tout d'abord, on fait un "reset" sur les fichiers qui le n�cessitent.
                bw = new BufferedWriter(new FileWriter(fic+"CLASSES.CSV"));
                line = "Classe;profondeurMaximale;nombreFilles;nombreDescendants;nombreAncetres;couplageEntrant;couplageSortant;complexiteCyclomatique;nbAssociationsClasse;nbAttributsClasse;nbOperationsClasse;nbOperationsSurchargees;nbOperationsHeritees;nbInstances;nbInterfacesImplementees;rfcClasse;nbAttributsPublics;nbOperationsPubliques;nbClassesMeres";
                bw.write(line, 0, line.length());
                bw.close();
                
                bw = new BufferedWriter(new FileWriter(fic+"PACKAGES.CSV"));
                line = "Package;complexitePackage";
                bw.write(line, 0, line.length());
                bw.close();
                
                bw = new BufferedWriter(new FileWriter(fic+"DIAGRAMMES.CSV"));
                line = "Diagrammes;complexiteDiagramme;ratioMalForme;nbActeurs;nbUseCases";
                bw.write(line, 0, line.length());
                bw.close();
                
                bw = new BufferedWriter(new FileWriter(fic+"MODELE.CSV"));
                line = "Modele;Version;nombreActeursModele;nombreAgregationsModele;nombreAssociationsModele;"+
                "nombreClassesModele;nombreHeritagesModele;nombreMessagesModele;nombrePackagesModele;"+
                "nombreObjetsModele;nombreUseCasesModele;nombreMethodesDocumentees;nombreEtatsModele;"+
                "nombreMoyenOperationsClasses;nombreMoyenActeursUseCases;nombreMoyenAssociationsClasses;"+
                "nombreMoyenPondereAttrClasses;nombreMoyenAttributsClasses;nombreMoyenPondereOperClasses;"+
                "nombreMoyenMessagesUseCases;nombreCommentairesMoyen;profondeurMoyenneClasses;nombreFillesMoyen;"+
                "nombreDescendantsMoyen;couplageMoyen;complexitePackages;"+
                "complexiteDiagrammes;complexiteCyclomatiqueMoyenne;nbMoyenMethodesSurchargees;nbMoyenMethodesHeritees"+
                ";nombreMoyenInstances;rfcMoyenne;nombreMoyenInterfImpl;nbDiagrammes;nombreDataType;"+
                "nombrePackagesClasses;nbDiagClasses;nbDiagUC;nbDiagSeq;nbDiagET;nbDiagAct;nbDiagColl";
                bw.write(line, 0, line.length());
            }
            else
                bw = new BufferedWriter(new FileWriter(fic+"MODELE.CSV", true));
                
            bw.newLine();
            line = getName()+";"+getNumeroVersion()+";"+nombreActeursModele+";"+nombreAgregationsModele+";"+
            nombreAssociationsModele+";"+nombreClassesModele+";"+nombreHeritagesModele+";"+
            nombreMessagesModele+";"+nombrePackagesModele+";"+nombreObjetsModele+";"+nombreUseCasesModele+";"+
            nombreMethodesDocumentees+";"+nombreEtatsModele+";"+nombreMoyenOperationsClasses+
            ";"+nombreMoyenActeursUseCase+";"+nombreMoyenAssociationsClasse+";"+
            nombreMoyenPondereAttributsClasse+";"+nombreMoyenAttributsClasse+";"+
            nombreMoyenPondereOperationsClasses+";"+nombreMoyenMessagesUseCase+";"+nombreCommentairesMoyen+";"+
            profondeurMoyenneClasses+";"+nombreFillesMoyen+";"+nombreDescendantsMoyen+";"+couplageMoyen+
            ";"+complexitePackages+";"+complexiteDiagrammes+";"+
            complexiteCyclomatiqueMoyenne+";"+nombreMoyenMethodesSurchargees+";"+nombreMoyenMethodesHeritees+";"+
            nombreMoyenInstances+";"+rfcMoyenne+";"+nombreMoyenInterfacesImplementees+";"+nombreDiagrammes+";"+
            nombreDataType+";"+nombrePackagesClasses+";"+nbDiagrammesClasses+";"+nbDiagrammesUC+";"+nbDiagrammesSeq+";"+
            nbDiagrammesET+";"+nbDiagrammesAct+";"+nbDiagrammesColl;
        
            bw.write(line, 0, line.length());
            bw.close();
            
            for(Iterator it=objets.iterator(); it.hasNext(); ) {
                ((Diagramme)it.next()).writeCSV(fic, getName());
            }
            
        } catch(java.io.IOException exc) {
            lastError = Modele.IMPOSSIBLEWRITE;
            retour = false;
        }
        return retour;
    }
    
    public int getLastError() {return lastError;}
}

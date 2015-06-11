package code2html;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

public class Code2HTML {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: Code2HTML <modesDir> <inputFile> <outputFile>");
            System.exit(-1);
        }
        processFile(args[0], args[1], args[2]);
    }

    public static void processFile(String modesDir, String inputFileName, String outputFileName) {
        if ((inputFileName != null) && (inputFileName.length() != 0) && outputFileName!=null) {
            if (inputFileName.contains("\\ ")) {
                inputFileName = inputFileName.replaceAll("\\\\\\s", " ");
            }
            if (outputFileName.contains("\\ ")) {
                outputFileName = outputFileName.replaceAll("\\\\\\s", " ");
            }

            String code = readFileContent(inputFileName, "utf-8");

            ModeLoader.loadModeCatalog(modesDir + File.separatorChar + "catalog", false);

            Mode mode = ModeLoader.getMode("text");
            File f = new File(inputFileName);
            Vector modes = ModeLoader.getModes();
            for (int j = 0; j < modes.size(); j++) {

                if (((Mode) modes.get(j)).accept(f.getName(), "")) {
                    mode = (Mode) modes.get(j);
                    break;
                }
            }
//			System.out.println("Highlighting "+inputFileName+" as "+mode.getName());

            String output = HtmlSyntaxHighlighter.syntaxTextToHtml(code, mode.getName());

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(
                        outputFileName));

                writer.write("<html>\n<head>\n");
                writer.write("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />");
                writer.write("<link type='text/css' rel='stylesheet' href='css/syntax.css'>\n");
                writer.write("</head>\n<body>\n<pre>");

                writer.write(output);

                writer.write("\n</pre>\n</body>\n</html>");
                writer.flush();
                writer.close();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    protected static String readFileContent(String filePath, String encoding) {
        String s = "";
        BufferedReader bufferedReader = null;
        FileInputStream fis = null;
        File f = new File(filePath);
        //if(f.exists())
        try {
            fis = new FileInputStream(f);
            try {
                bufferedReader = encoding != null ? new BufferedReader(
                        new InputStreamReader(fis, encoding)) : new BufferedReader(
                        new InputStreamReader(fis));
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            StringBuffer buff = new StringBuffer();
            try {
                for (String line = null; (line = bufferedReader.readLine())
                        != null;) {
                    if (line.equals("")) {
                        line = " ";
                    }
                    buff.append(line).append("\n");
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            s = buff.toString();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return s;
    }
}

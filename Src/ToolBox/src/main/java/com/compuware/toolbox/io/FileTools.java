/*
 * FileTools.java
 *
 * Created on 12 mars 2004, 13:53
 */
package com.compuware.toolbox.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

import com.compuware.toolbox.io.insert.FileInsertEntry;
import com.compuware.toolbox.constants.Constants;
import java.io.InputStream;

/**
 * File tools library.
 * @author  cwfr-fdubois
 */
public class FileTools {

    static protected Logger logger = com.compuware.toolbox.util.logging.LoggerManager.getLogger(Constants.LOGGER_NAME);
    public static final char FILE_SEPARATOR_BACKSLASH = '\\';
    public static final char FILE_SEPARATOR_SLASH = '/';
    public static final String FILE_SEPARATOR_BACKSLASH_STR = "\\";
    public static final String FILE_SEPARATOR_SLASH_STR = "/";
    private static final int BUFFER_SIZE = 8192;

    /** Creates a new instance of FileTools */
    public FileTools() {
    }

    /** Verifie si le chemin d'acces se termine par un separateur de fichier ou non.
     * @param path le chemin d'acces a verifier.
     * @return true si le chemin d'acces se termine par un separateur, false sinon.
     */
    public static boolean endsWithFileSeparator(String path) {
        boolean result = false;
        if (path != null) {
            result = path.endsWith(FILE_SEPARATOR_BACKSLASH_STR)
                    || path.endsWith(FILE_SEPARATOR_SLASH_STR);
        }
        return result;
    }

    /** Copy the source file into the specified destination.
     * Possible parameters:
     *    - source as file and destination as directory: copy the file into the directory.
     *    - source as file and destination as file: copy the file to the destination
     * file (possibly changing name).
     * @param src the source file or directory.
     * @param dest the destination file or directory.
     * @param append if true, then byte will be written at the end of the existing file.
     * @throws IOException An I/O exception occurs during copy.
     */
    public static void copy(File src, File dest, boolean append) throws IOException {
        if (!src.isDirectory()) {
            try {
                // Create channel on the source
                FileChannel srcChannel = new FileInputStream(src).getChannel();
                // Create channel on the destination
                FileOutputStream destStream = null;
                if (dest.isDirectory()) {
                    // The destination is a directory: the file is copied into the directory.
                    destStream = new FileOutputStream(new File(dest, src.getName()), append);
                } else {
                    // The destination is a file: the file is copied using the destination file.
                    destStream = new FileOutputStream(dest, append);
                }
                FileChannel dstChannel = destStream.getChannel();
                // Copy file contents from source to destination
                dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
                // Close the channels
                srcChannel.close();
                dstChannel.close();
            } catch (FileNotFoundException e) {
                // The source or destination file does not exist.
                logger.debug("Error ignored: file not found", e);
            }
        }
    }

    /** Copy the source file into the specified destination.
     * Possible parameters:
     *    - source as file and destination as directory: copy the file into the directory.
     *    - source as file and destination as file: copy the file to the destination
     * file (possibly changing name).
     * @param src the source file or directory.
     * @param dest the destination file or directory.
     * @throws IOException An I/O exception occurs during copy.
     */
    public static void copy(File src, File dest) throws IOException {
        FileTools.copy(src, dest, false);
    }

    /** Copy recursively the source file or directory into the specified destination.
     * Possible parameters:
     *    - source as file and destination as directory: copy the file into the directory.
     *    - source as file and destination as file: copy the file to the destination
     * file (possibly changing name).
     * @param src the source file or directory.
     * @param dest the destination file or directory.
     * @throws IOException An I/O exception occurs during copy.
     */
    public static void rcopy(File src, File dest) throws IOException {
        if (!src.isDirectory()) {
            // The source is a file: just copy the file to the destination.
            copy(src, dest);
        } else {
            // The source is a directory: copy the directory to the destination directory.
            if (dest.isDirectory()) {
                // The destination is a directory: create the source directory to the destination directory.
                File newdir = new File(dest, src.getName());
                newdir.mkdir();
                // Copy the source directory files to the new created directory.
                File[] subfiles = src.listFiles();
                rcopy(subfiles, newdir);
            }
        }
    }

    /** Copy a list of file into the specified directory.
     * @param list A list of file.
     * @param dest the destination directory.
     * @throws IOException An I/O exception occurs during copy.
     */
    public static void copy(File[] list, File dest) throws IOException {
        // Loop on the file list.
        for (int i = 0; i < list.length; i++) {
            // For each file, copy it to the destination directory.
            FileTools.copy(list[i], dest);
        }
    }

    /** Copy recursivelya list of file into the specified directory.
     * @param list A list of file.
     * @param dest the destination directory.
     * @throws IOException An I/O exception occurs during copy.
     */
    public static void rcopy(File[] list, File dest) throws IOException {
        // Loop on the file or directory list.
        for (int i = 0; i < list.length; i++) {
            // For each file or directory, copy it and its content to the destination directory.
            FileTools.rcopy(list[i], dest);
        }
    }

    /**
     * Concatene deux chemin d'acces en supprimant ou ajoutant les separateurs si necessaire.
     * @param pathBase le chemin d'acces racine.
     * @param pathRelative le chemin d'acces a concatener.
     * @return la concatenation des deux chemins d'acces.
     */
    public static String concatPath(String pathBase, String pathRelative) {
        StringBuffer result = new StringBuffer();
        if (pathBase != null && pathRelative != null) {
            if (pathBase.endsWith("/") || pathBase.endsWith("\\")) {
                if (pathRelative.startsWith("/")
                        || pathRelative.startsWith("\\")) {
                    result.append(pathBase).append(pathRelative.substring(1));
                } else {
                    result.append(pathBase).append(pathRelative);
                }
            } else if (pathRelative.startsWith("/")
                    || pathRelative.startsWith("\\")) {
                result.append(pathBase).append(pathRelative);
            } else {
                result.append(pathBase).append('/').append(pathRelative);
            }
        }
        return result.toString();
    }

    /** Compresse le contenu d'un repertoire au format ZIP.
     * Methode recursive.
     * @param zipDir le repertoire a zipper.
     * @param zos le stream d'ecriture au format ZIP.
     * @param rootPath la racine a partir de laquelle on a commence a zipper.
     */
    public static void zipDir(File zipDir, ZipOutputStream zos, String rootPath) {
        try {
            // create a new File object based on the directory we
            // have to zip File
            // get a listing of the directory content
            String[] dirList = zipDir.list();
            byte[] readBuffer = new byte[BUFFER_SIZE];
            int bytesIn = 0;
            int rootLength = 0;
            if (rootPath != null) {
                rootLength = rootPath.length();
            }
            // loop through dirList, and zip the files
            for (int i = 0; i < dirList.length; i++) {
                File f = new File(zipDir, dirList[i]);
                if (f.isDirectory()) {
                    // if the File object is a directory, call this
                    // function again to add its content recursively
                    zipDir(f, zos, rootPath);
                    // loop again
                    continue;
                }
                // if we reached here, the File object if was not
                // a directory
                // create a FileInputStream on top of f
                FileInputStream fis = new FileInputStream(f);
                String filePath = f.getPath();
                // create a new zip entry
                ZipEntry anEntry = new ZipEntry(filePath.substring(rootLength));
                // place the zip entry in the ZipOutputStream object
                zos.putNextEntry(anEntry);
                // now write the content of the file to the ZipOutputStream
                while ((bytesIn = fis.read(readBuffer)) != -1) {
                    zos.write(readBuffer, 0, bytesIn);
                }
                // close the Stream
                fis.close();
            }
        } catch (Exception e) {
            // handle exception
        }
    }

    /** Compresse le contenu d'un repertoire au format ZIP.
     * @param zipDir le repertoire a zipper.
     * @param zipFile le fichier ZIP a creer.
     * @param rootPath  la racine a retirer aux repertoire stockes dans le zip.
     * @throws IOException
     */
    public static void zipDir(File zipDir, File zipFile, String rootPath) throws IOException {
        //create a ZipOutputStream to zip the data to
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
        zipDir(zipDir, zos, rootPath);
        zos.flush();
        //close the stream
        zos.close();
    }

    /** Compresse le contenu d'un fichier au format ZIP.
     * @param srcFile le fichier a zipper.
     * @param zipFile le fichier ZIP a creer.
     * @param rootPath  la racine a retirer aux repertoire stockes dans le zip.
     * @throws IOException
     */
    public static void zipFile(File srcFile, File zipFile, String rootPath) throws IOException {
        byte[] readBuffer = new byte[BUFFER_SIZE];
        int bytesIn = 0;
        int rootLength = 0;
        if (rootPath != null) {
            rootLength = rootPath.length();
        }
        //create a ZipOutputStream to zip the data to
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
        // create a FileInputStream on top of f
        FileInputStream fis = new FileInputStream(srcFile);
        String filePath = srcFile.getPath();
        // create a new zip entry
        ZipEntry anEntry = new ZipEntry(filePath.substring(rootLength));
        // place the zip entry in the ZipOutputStream object
        zos.putNextEntry(anEntry);
        // now write the content of the file to the ZipOutputStream
        while ((bytesIn = fis.read(readBuffer)) != -1) {
            zos.write(readBuffer, 0, bytesIn);
        }
        // close the Stream
        fis.close();
        zos.flush();
        //close the stream
        zos.close();
    }

    /** Compresse le contenu d'une liste de fichiers au format ZIP.
     * @param srcFileList la liste de fichiers a zipper.
     * @param zipFile le fichier ZIP a creer.
     * @param rootPath  la racine a retirer aux repertoire stockes dans le zip.
     * @throws IOException
     */
    public static void zipFile(List srcFileList, File zipFile, String rootPath) throws IOException {
        byte[] readBuffer = new byte[BUFFER_SIZE];
        int bytesIn = 0;
        int rootLength = 0;
        if (rootPath != null) {
            rootLength = rootPath.length();
        }
        //create a ZipOutputStream to zip the data to
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));

        Iterator i = srcFileList.iterator();
        while (i.hasNext()) {
            File srcFile = (File) i.next();
            // create a FileInputStream on top of f
            FileInputStream fis = new FileInputStream(srcFile);
            String filePath = srcFile.getPath();
            // create a new zip entry
            ZipEntry anEntry = new ZipEntry(filePath.substring(rootLength));
            // place the zip entry in the ZipOutputStream object
            zos.putNextEntry(anEntry);
            // now write the content of the file to the ZipOutputStream
            while ((bytesIn = fis.read(readBuffer)) != -1) {
                zos.write(readBuffer, 0, bytesIn);
            }
            // close the Stream
            fis.close();
        }
        zos.flush();
        //close the stream
        zos.close();
    }

    /** Compresse le contenu d'une liste de fichiers au format ZIP.
     * @param srcFileList la liste de fichiers a zipper.
     * @param zipFile le fichier ZIP a creer.
     * @param rootPath  la racine a retirer aux repertoire stockes dans le zip.
     * @throws IOException
     */
    public static void zipFile(List srcFileList, File zipFile, boolean keepPath) throws IOException {
        byte[] readBuffer = new byte[BUFFER_SIZE];
        int bytesIn = 0;

        //create a ZipOutputStream to zip the data to
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));

        Iterator i = srcFileList.iterator();
        while (i.hasNext()) {
            File srcFile = (File) i.next();
            // create a FileInputStream on top of f
            FileInputStream fis = new FileInputStream(srcFile);
            String filePath = null;
            if (keepPath) {
                filePath = srcFile.getPath();
            } else {
                filePath = srcFile.getName();
            }
            // create a new zip entry
            ZipEntry anEntry = new ZipEntry(filePath);
            // place the zip entry in the ZipOutputStream object
            zos.putNextEntry(anEntry);
            // now write the content of the file to the ZipOutputStream
            while ((bytesIn = fis.read(readBuffer)) != -1) {
                zos.write(readBuffer, 0, bytesIn);
            }
            // close the Stream
            fis.close();
        }
        zos.flush();
        //close the stream
        zos.close();
    }

    /** Compresse le contenu d'un repertoire au format ZIP.
     * @param zipDir le repertoire a zipper.
     * @param zipFile le fichier ZIP a creer.
     * @param rootPath  la racine a retirer aux repertoire stockes dans le zip.
     * @throws IOException
     */
    public static void zipDir(String zipDir, String zipFile, String rootPath) throws IOException {
        zipDir(new File(zipDir), new File(zipFile), rootPath);
    }

    /** Cree les repertoires manquant pour la creation d'un fichier.
     * @param destinationDir le repertoire racine.
     * @param entryName le path relatif au fichier.
     */
    private static void prepareFileDirectories(String destinationDir, String entryName) {
        String dirEntryName = null;
        String stdEntryName = entryName.replaceAll("\\\\", "/");
        if (stdEntryName.indexOf('/') > 0) {
            dirEntryName = stdEntryName.substring(0, stdEntryName.lastIndexOf("/"));
            File d = new File(destinationDir + "/" + dirEntryName);
            if (!d.exists()) {
                d.mkdirs();
            }
        }
    }

    /** decompresse un fichier zip dans un repertoire.
     * @param destinationDir le repertoire destination.
     * @param zipFile le fichier ZIP.
     * @throws FileNotFoundException
     */
    public static void unzipToDir(String destinationDir, File zipFile) throws FileNotFoundException {
        try {
            // Create a ZipInputStream to read the zip file
            BufferedOutputStream dest = null;
            FileInputStream fis = new FileInputStream(zipFile);
            ZipInputStream zis = new ZipInputStream(
                    new BufferedInputStream(fis));

            // Loop over all of the entries in the zip file
            int count;
            byte data[] = new byte[BUFFER_SIZE];
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    String entryName = entry.getName();
                    prepareFileDirectories(destinationDir, entryName);
                    String destFN = destinationDir + File.separator
                            + entry.getName();

                    // Write the file to the file system
                    FileOutputStream fos = new FileOutputStream(destFN);
                    dest = new BufferedOutputStream(fos, BUFFER_SIZE);
                    while ((count = zis.read(data, 0, BUFFER_SIZE)) != -1) {
                        dest.write(data, 0, count);
                    }
                    dest.flush();
                    dest.close();
                }
            }
            zis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Delete recursively the source file or directory into the specified destination.
     * Possible parameters:
     *    - source as file and destination as directory: copy the file into the directory.
     *    - source as file and destination as file: copy the file to the destination
     * file (possibly changing name).
     * @param src the source file or directory.
     * @throws IOException An I/O exception occurs during copy.
     */
    public static void rdelete(File src) throws IOException {
        if (src != null && src.exists()) {
            if (src.isDirectory()) {
                // The source is a directory: delete its content.
                File[] subfiles = src.listFiles();
                if (subfiles != null) {
                    for (int i = 0; i < subfiles.length; i++) {
                        rdelete(subfiles[i]);
                    }
                }
            }
            src.delete();
        }
    }

    /** Insere une liste de fichiers dans un fichier principal.
     * @param mainFile le fichier principal.
     * @param fileInsertList une liste de FileInsertEntry a inserer.
     * @throws IOException
     */
    public static void insertFiles(File mainFile, List fileInsertList) throws IOException {
        FileReader mainReader = new FileReader(mainFile);
        BufferedReader bufreader = new BufferedReader(mainReader);
        File tmp = new File(mainFile.getParentFile(), mainFile.getName()
                + ".tmp");
        FileWriter writer = new FileWriter(tmp);
        Iterator i = fileInsertList.iterator();
        while (i.hasNext()) {
            FileInsertEntry entry = (FileInsertEntry) i.next();
            insertFile(bufreader, entry, writer);
        }
        String line = bufreader.readLine();
        while (line != null) {
            writer.write(line);
            writer.write('\n');
            line = bufreader.readLine();
        }
        writer.close();
        bufreader.close();
        String fileName = mainFile.getName();
        mainFile.delete();
        tmp.renameTo(new File(tmp.getParentFile(), fileName));
    }

    /** Insere un fichier dans un fichier principal.
     * @param mainReader le lecteur en cours pour le fichier principal.
     * @param entry le fichier a inserer a l'emplacement voulu.
     * @param out le fichier temporaire de sortie.
     * @throws IOException
     */
    private static void insertFile(BufferedReader mainReader, FileInsertEntry entry, FileWriter out) throws IOException {
        String line = mainReader.readLine();
        while (line != null) {
            if (entry.regexpMatch(line)) {
                entry.write(out);
                out.write('\n');
                break;
            } else {
                out.write(line);
                out.write('\n');
            }
            line = mainReader.readLine();
        }
    }

    /**
     * reads a file and put it in a byte array
     * @param file
     * @return
     * @throws IOException
     */
    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        if (length > Integer.MAX_VALUE) {
            // File is too large
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }
}

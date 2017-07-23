package com.firozmemon.fmfileexplorer.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by firoz on 25/6/17.
 */

public class FileUtil {

    private static FileUtil fileUtil;

    public static FileUtil getInstance() {
        if (fileUtil == null)
            fileUtil = new FileUtil();
        return fileUtil;
    }

    /**
     * Get file extension from string passed as param
     *
     * @param url
     * @return file extension
     */
    public String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
    }

    /**
     * Performing copy file operation
     *
     * @param srcFile
     * @param destDir
     * @return true -> success, false -> fail
     */
    public boolean copyFile(File srcFile, File destDir) throws IOException {
        InputStream in = null;
        OutputStream out = null;

        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        in = new FileInputStream(srcFile);
        out = new FileOutputStream(destDir.getPath() + File.separator + srcFile.getName());

        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        in.close();
        in = null;

        // write the output file (You have now copied the file)
        out.flush();
        out.close();
        out = null;

        return true;
    }

    // To Delete the directory inside list of files and inner Directory
    public boolean deleteFileOrDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteFileOrDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }
}

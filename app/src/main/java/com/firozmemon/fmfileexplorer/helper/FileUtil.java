package com.firozmemon.fmfileexplorer.helper;

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
}

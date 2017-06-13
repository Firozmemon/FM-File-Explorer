package com.firozmemon.fmfileexplorer.models;

/**
 * Created by firoz on 11/6/17.
 */

public class FileModel {
    String name, parentDirectoryPath, fileSize;
    boolean isFolder;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean folder) {
        isFolder = folder;
    }

    public String getParentDirectoryPath() {
        return parentDirectoryPath;
    }

    public void setParentDirectoryPath(String parentDirectoryPath) {
        this.parentDirectoryPath = parentDirectoryPath;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
}

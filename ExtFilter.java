package com.spbtablo;

import java.io.File;
import java.io.FileFilter;

public class ExtFilter implements FileFilter {
    String ext;
    ExtFilter(String ext)
    {
        this.ext = ext;
    }
    private String getExtension(File pathname)
    {
        String filename = pathname.getPath();
        int i = filename.lastIndexOf('.');
        if ((i > 0) && (i < filename.length()-1)) {
            return filename.substring(i+1).toLowerCase();
        }
        return "";
    }

    @Override
    public boolean accept(File pathname) {
        if (!pathname.isFile())
            return false;
        String extension = getExtension(pathname);
            if (ext.equalsIgnoreCase(extension)){
                return true;
        }
        return false;
    }
}

package com.github.nihaorz.util;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Nihaorz
 */
public class CompressUtils {

    /**
     * 归档zip
     *
     * @param entry       归档路径
     * @param outFilePath 输出文件绝对路径
     * @param needRootDir zip文件中是否需要顶层Root文件夹
     * @return
     * @throws IOException
     */
    public static File archiveZip(String entry, String outFilePath, boolean needRootDir) throws IOException {
        File outFile = new File(outFilePath);
        File file = new File(entry);
        ZipArchiveOutputStream tos = new ZipArchiveOutputStream(new FileOutputStream(outFile.getAbsolutePath()));
        String base = file.getName();
        if (file.isDirectory()) {
            archiveDir(file, tos, needRootDir ? base : null);
        } else {
            archiveHandle(tos, file, needRootDir ? base : null);
        }
        tos.close();
        return outFile;
    }

    /**
     * 递归文件夹
     *
     * @param file
     * @param tos
     * @param basePath
     * @throws IOException
     */
    private static void archiveDir(File file, ZipArchiveOutputStream tos, String basePath) throws IOException {
        File[] listFiles = file.listFiles();
        for (File fi : listFiles) {
            if (fi.isDirectory()) {
                archiveDir(fi, tos, basePath == null ? fi.getName() : basePath + File.separator + fi.getName());
            } else {
                archiveHandle(tos, fi, basePath);
            }
        }
    }

    /**
     * 具体归档处理（文件）
     *
     * @param tos
     * @param fi
     * @param basePath
     * @throws IOException
     */
    private static void archiveHandle(ZipArchiveOutputStream tos, File fi, String basePath) throws IOException {
        ZipArchiveEntry tEntry = new ZipArchiveEntry(basePath == null ? fi.getName() : basePath + File.separator + fi.getName());
        tEntry.setSize(fi.length());
        tos.putArchiveEntry(tEntry);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fi));
        byte[] buffer = new byte[1024];
        int read;
        while ((read = bis.read(buffer)) != -1) {
            tos.write(buffer, 0, read);
        }
        bis.close();
        tos.closeArchiveEntry();
    }

}

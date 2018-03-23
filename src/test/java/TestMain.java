import com.github.nihaorz.svn.util.MySVNDiffStatusHandler;
import com.github.nihaorz.svn.util.PatchFile;
import com.github.nihaorz.svn.util.PropertiesConfig;
import com.github.nihaorz.svn.util.SVNUtils;
import com.github.nihaorz.util.CompressUtils;
import org.apache.commons.io.FileUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.junit.Test;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNRevision;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.UUID;

/**
 * @author Nihaorz
 */
public class TestMain {

    private static final String username = PropertiesConfig.getSVNUsername();
    private static final String password = PropertiesConfig.getSVNPassword();
    private static final SVNRevision fromVersion = SVNRevision.create(PropertiesConfig.getReVersionStart());
    private static final SVNRevision toVersion = SVNRevision.create(PropertiesConfig.getReVersionEnd());
    private SVNUtils svnUtils = new SVNUtils(username, password);
    /**
     * 打包根目录名
     */
    private static final String PACKAGE_ROOT_DIR = "content";


    @Test
    public void testDiff() throws SVNException, IOException {
        /**
         * 对比两个版本之间的修改列表
         */
        SVNURL svnUrl = SVNURL.parseURIEncoded(PropertiesConfig.getDeploymentPackageSVNUrl());
        MySVNDiffStatusHandler handler = new MySVNDiffStatusHandler();
        handler.setFromVersion(fromVersion);
        handler.setToVersion(toVersion);
        svnUtils.doDiffStatus(svnUrl, fromVersion, toVersion, handler);

        String uuid = UUID.randomUUID().toString().replace("-", "");
        /**
         * 补丁包临时文件
         */
        String patchPackageTempDirectory = PropertiesConfig.getPatchPackageTempDirectory() + File.separator + uuid;
        /**
         * 打包根目录
         */
        String patchPackageRootDirectory = patchPackageTempDirectory + File.separator + PACKAGE_ROOT_DIR;
        /**
         * 根据差异下载文件到临时目录
         */
        downloadFileToTempDirectory(svnUrl, handler.getPatchFiles(), patchPackageRootDirectory + File.separator + "file" + File.separator + PropertiesConfig.getPatchPackageRootDirectory());

        /**
         * 构建XML，标记删除文件
         */
        createXML(handler.getPatchFiles(), patchPackageRootDirectory);
        /**
         * 打包临时文件为zip
         */
        String outFilePath = patchPackageTempDirectory + File.separator + PropertiesConfig.getPatchPackageName();
        try {
            File outFile = CompressUtils.archiveZip(patchPackageRootDirectory, outFilePath, false);
            System.out.println("输出文件:" + outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createXML(List<PatchFile> patchFiles, String patchPackageRootDirectory) throws IOException {
        Element rootEle = DocumentHelper.createElement("root");
        Element deletesEle = rootEle.addElement("deletes");
        for (PatchFile patchFile : patchFiles) {
            if (PatchFile.TYPE.DELETE.equals(patchFile.getType())) {
                Element deleteEle = deletesEle.addElement("delete");
                deleteEle.setText(patchFile.getLocalPath());
            }
        }
        rootEle.addElement("merges").setText("file/OperationCenter");
        StringWriter writer = new StringWriter();
        // 格式化输出流
        XMLWriter xmlWriter = new XMLWriter(writer, OutputFormat.createPrettyPrint());
        // 将document写入到输出流
        xmlWriter.write(rootEle);
        xmlWriter.close();
        File xmlFile = new File(patchPackageRootDirectory + File.separator + "init.xml");
        FileUtils.write(xmlFile, writer.toString(), "UTF-8");
    }

    /**
     * 下载文件到临时目录
     *
     * @param svnUrl
     * @param patchFiles
     * @param patchPackageTempDirectory
     * @throws SVNException
     */
    private void downloadFileToTempDirectory(SVNURL svnUrl, List<PatchFile> patchFiles, String patchPackageTempDirectory) throws SVNException {
        for (PatchFile patchFile : patchFiles) {
            String urlPath = patchFile.getUrlPath();
            String relativePath = urlPath.substring(svnUrl.toString().length(), urlPath.length());
            String relativeDirectory = relativePath;
            if (relativePath.indexOf("/") > 0) {
                relativeDirectory = relativeDirectory.substring(0, relativeDirectory.lastIndexOf("/"));
            }
            if (PatchFile.TYPE.ADD.equals(patchFile.getType()) || PatchFile.TYPE.MODIFY.equals(patchFile.getType())) {
                File exportDirectory = new File(patchPackageTempDirectory + relativeDirectory);
                downloadFileFromSVN(SVNURL.parseURIEncoded(urlPath), exportDirectory, toVersion);
            }
        }
    }

    /**
     * 从SVN下载文件
     *
     * @param svnUrl
     * @param exportDirectory
     * @param version
     */
    private void downloadFileFromSVN(SVNURL svnUrl, File exportDirectory, SVNRevision version) {
        try {
            svnUtils.doExport(svnUrl, exportDirectory, version);
        } catch (SVNException e) {
            e.printStackTrace();
        }
    }

}

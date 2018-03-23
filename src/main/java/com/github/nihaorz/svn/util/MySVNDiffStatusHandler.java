package com.github.nihaorz.svn.util;

import org.tmatesoft.svn.core.wc.ISVNDiffStatusHandler;
import org.tmatesoft.svn.core.wc.SVNDiffStatus;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatusType;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SVN差异状态处理类
 *
 * @author Nihaorz
 */
public class MySVNDiffStatusHandler implements ISVNDiffStatusHandler {

    private static final Logger logger = Logger.getLogger(MySVNDiffStatusHandler.class.getName());

    /**
     * 对比起始版本
     */
    private SVNRevision fromVersion;

    /**
     * 对比结束版本
     */
    private SVNRevision toVersion;

    /**
     * 对比补丁文件
     */
    private List<PatchFile> patchFiles = new ArrayList<>();

    @Override
    public void handleDiffStatus(SVNDiffStatus diffStatus) {
        String modificationType = diffStatus.getModificationType().toString();
        logger.log(Level.INFO, "modificationType:" + modificationType);
        char code = diffStatus.getModificationType().getCode();
        PatchFile patchFile = new PatchFile();
        patchFile.setFromVersion(fromVersion);
        patchFile.setToVersion(toVersion);
        patchFile.setUrlPath(diffStatus.getURL().toString());
        patchFile.setLocalPath(diffStatus.getPath());
        if (SVNStatusType.STATUS_DELETED.getCode() == code) {
            patchFile.setType(PatchFile.TYPE.DELETE);
        } else if (SVNStatusType.STATUS_ADDED.getCode() == code) {
            patchFile.setType(PatchFile.TYPE.ADD);
        } else if (SVNStatusType.STATUS_MODIFIED.getCode() == code) {
            patchFile.setType(PatchFile.TYPE.MODIFY);
        } else {

        }
        patchFiles.add(patchFile);
    }

    public void setFromVersion(SVNRevision fromVersion) {
        this.fromVersion = fromVersion;
    }

    public void setToVersion(SVNRevision toVersion) {
        this.toVersion = toVersion;
    }

    public List<PatchFile> getPatchFiles() {
        return patchFiles;
    }
}

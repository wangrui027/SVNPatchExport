package com.github.nihaorz.svn.util;

import org.tmatesoft.svn.core.wc.SVNRevision;

/**
 * SVN补丁文件对象
 *
 * @author Nihaorz
 */
public class PatchFile {

    /**
     * 补丁文件类型枚举
     */
    public enum TYPE {
        ADD, // 新增
        MODIFY, // 修改
        DELETE // 删除
    }

    /**
     * 补丁文件类型
     */
    private TYPE type;

    /**
     * 本地路径，相对于
     */
    private String localPath;

    /**
     * url绝对地址
     */
    private String urlPath;

    /**
     * 旧版本
     */
    private SVNRevision fromVersion;

    /**
     * 当前版本
     */
    private SVNRevision toVersion;

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public SVNRevision getFromVersion() {
        return fromVersion;
    }

    public void setFromVersion(SVNRevision fromVersion) {
        this.fromVersion = fromVersion;
    }

    public SVNRevision getToVersion() {
        return toVersion;
    }

    public void setToVersion(SVNRevision toVersion) {
        this.toVersion = toVersion;
    }
}

package com.github.nihaorz.svn.util;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.ISVNDiffStatusHandler;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import java.io.File;

/**
 * SVN工具类
 *
 * @author Nihaorz
 */
public class SVNUtils {

    private final DefaultSVNOptions defaultSVNOptions = SVNWCUtil.createDefaultOptions(true);

    private final SVNClientManager svnClientManager;

    private final SVNUpdateClient svnUpdateClient;

    private final SVNDiffClient svnDiffClient;

    public SVNUtils(String username, String password) {
        DAVRepositoryFactory.setup();
        svnClientManager = SVNClientManager.newInstance(defaultSVNOptions, username, password);
        svnUpdateClient = svnClientManager.getUpdateClient();
        svnUpdateClient.setIgnoreExternals(false);
        svnDiffClient = svnClientManager.getDiffClient();
    }

    /**
     * 从SVN下载某个目录的最新版本
     *
     * @param svnUrl          svn地址
     * @param exportDirectory 导出的本地存放目录
     * @throws SVNException
     */
    public void doExport(SVNURL svnUrl, File exportDirectory) throws SVNException {
        doExport(svnUrl, exportDirectory, SVNRevision.HEAD);
    }

    /**
     * 从SVN下载某个目录的指定版本
     *
     * @param svnUrl          svn地址
     * @param exportDirectory 导出的本地存放目录
     * @param svnRevision     svn版本号
     * @throws SVNException
     */
    public void doExport(SVNURL svnUrl, File exportDirectory, SVNRevision svnRevision) throws SVNException {
        svnUpdateClient.doExport(svnUrl, exportDirectory, svnRevision, svnRevision, null, true, SVNDepth.INFINITY);
    }

    /**
     * 处理svn上某个路径两本版本之间的差异
     *
     * @param url           svn地址
     * @param revisionStart 最初的版本
     * @param revisionEnd   当前对比的版本
     * @param handler       对比结果处理类
     * @throws SVNException
     */
    public void doDiffStatus(SVNURL url, SVNRevision revisionStart, SVNRevision revisionEnd, ISVNDiffStatusHandler handler) throws SVNException {
        svnDiffClient.doDiffStatus(url, revisionStart, url, revisionEnd, SVNDepth.INFINITY, true, handler);
    }

}

package com.github.nihaorz.svn.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @author Nihaorz
 */
public class PropertiesConfig {

    private static final Properties props = new Properties();

    static {
        try {
            props.load(new InputStreamReader(PropertiesConfig.class.getResourceAsStream("/config.properties"), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取SVN用户名
     *
     * @return
     */
    public static String getSVNUsername() {
        return props.getProperty("svn.username");
    }

    /**
     * 获取SVN密码
     *
     * @return
     */
    public static String getSVNPassword() {
        return props.getProperty("svn.password");
    }

    /**
     * 获取部署包的SVN路径
     *
     * @return
     */
    public static String getDeploymentPackageSVNUrl() {
        return props.getProperty("svn.deployment.package.url");
    }

    /**
     * 获取部署包的SVN对比起始版本
     *
     * @return
     */
    public static Long getReVersionStart() {
        return Long.parseLong(props.getProperty("svn.reversion.start"));
    }

    /**
     * 获取部署包的SVN对比结束版本
     *
     * @return
     */
    public static Long getReVersionEnd() {
        return Long.parseLong(props.getProperty("svn.reversion.end"));
    }

    /**
     * 获取补丁包临时目录
     *
     * @return
     */
    public static String getPatchPackageTempDirectory() {
        return props.getProperty("svn.patch.temp.directory");
    }

    /**
     * 获取补丁包压缩文件的根目录
     *
     * @return
     */
    public static String getPatchPackageRootDirectory() {
        return props.getProperty("svn.patch.zip.root.directory");
    }

    /**
     * 获取补丁包的名字
     *
     * @return
     */
    public static String getPatchPackageName() {
        SimpleDateFormat sdf = new SimpleDateFormat(props.getProperty("svn.patch.file.date.format"));
        return props.getProperty("svn.patch.file.format").replace("${date}", sdf.format(new Date()));
    }

    /**
     * 获取补丁包的SVN上传路径
     *
     * @return
     */
    public static String getPatchPackageSVNUrl() {
        return props.getProperty("svn.patch.package.url");
    }

}

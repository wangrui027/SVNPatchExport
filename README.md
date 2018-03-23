## 概述

本程序基于`SVN`仓库对比两个版本之间的差异，并将新增和修改的文件下载到本地打包为`zip`文件，同时新建`init.xml`，标记被删除的文件，从而实现补丁包的自动构建。

## 配置

```properties
# svn用户名
svn.username=******
# svn密码
svn.password=******
# 部署包的svn地址，程序基于本地址对比差异
svn.deployment.package.url=https://******/svn/201705-0200-0007-GeoStack2.0地理信息智能云/40_源码/20_综合运维/基线包/实时解压版本/OperationCenter-1.8/OperationCenter
# svn起始版本号
svn.reversion.start=8937
# svn结束版本号
svn.reversion.end=10182
# 存放补丁包文件的临时目录
svn.patch.temp.directory=C:\\Users\\Nihaorz\\Desktop\\patch
# 补丁包压缩文件的根目录，即zip文件的根目录
svn.patch.zip.root.directory=OperationCenter
# 补丁包日期格式
svn.patch.file.date.format=yyyymmdd
# 补丁包文件名格式
svn.patch.file.format=geostack_operationcenter-2.0.1.0-${date}.patch.el6.zip
# 补丁包的上传地址
svn.patch.package.url=https://******/svn/201705-0200-0007-GeoStack2.0地理信息智能云/40_源码/20_综合运维/基线包/对外打包版本/OperationCenter-1.8
```

克隆代码后请自行修改以下配置：

`svn.username`：svn用户名
`svn.password`：svn密码
`svn.deployment.package.url`：部署包的svn地址，程序基于本地址对比差异
`svn.reversion.start`：svn起始版本号
`svn.reversion.end`：svn结束版本号
`svn.patch.temp.directory`：存放补丁包文件的临时目录
`svn.patch.zip.root.directory`：补丁包压缩文件的根目录，即zip文件的根目录
`svn.patch.file.date.format`：补丁包日期格式
`svn.patch.file.format`：补丁包文件名格式
`svn.patch.package.url`：补丁包的上传地址（目前未实现补丁包自动上传，后面有时间再实现吧）

然后运行`TestMain`测试类的`testDiff()`方法即可，如果需要修改补丁包结构或者`xml`文件结构，请自行修改测试类实现。




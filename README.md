# 星点盒

星点盒是一个基于 Compose Desktop 开发的原神千星奇域辅助工具。目前包括以下功能：

- UI 富文本编辑器

开发中功能：

- 资源管理器

## 下载

目前暂无发布版本。但是你可以在 GitHub Actions 上下载到最新的构建版。

## 编译

你需要安装 Bazel。推荐使用 [Bazelisk](https://github.com/bazelbuild/bazelisk) 来安装。

安装好 Bazel 后，你可以用以下命令编译项目：

```bash
bazel build //star-dot-box:star-dot-box_deploy.jar
```

编译好后，可以在 `bazel-bin/star-dot-box/star-dot-box_deploy.jar` 找到编译好的 JAR 文件。


## 许可

该项目采用 GNU Affero General Public License 版本 3 或更高版本授权。

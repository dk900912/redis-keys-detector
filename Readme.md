<p align="center">
<a href="https://openjdk.java.net/"><img src="https://img.shields.io/badge/Java-21+-green?logo=java&logoColor=white" alt="Java support"></a>
<a href="https://www.apache.org/licenses/LICENSE-2.0.html"><img src="https://img.shields.io/github/license/dk900912/redis-keys-detector?color=4D7A97&logo=apache" alt="License"></a>
<a href="https://search.maven.org/search?q=a:redis-keys-detector"><img src="https://img.shields.io/maven-central/v/io.github.dk900912/redis-keys-detector?logo=apache-maven" alt="Maven Central"></a>
<a href="https://github.com/dk900912/redis-keys-detector/stargazers"><img src="https://img.shields.io/github/stars/dk900912/redis-keys-detector" alt="GitHub Stars"></a>
<a href="https://github.com/dk900912/redis-keys-detector/fork"><img src="https://img.shields.io/github/forks/dk900912/redis-keys-detector" alt="GitHub Forks"></a>
<a href="https://github.com/dk900912/redis-keys-detector/issues"><img src="https://img.shields.io/github/issues/dk900912/redis-keys-detector" alt="GitHub issues"></a>
<a href="https://github.com/dk900912/redis-keys-detector/graphs/contributors"><img src="https://img.shields.io/github/contributors/dk900912/redis-keys-detector" alt="GitHub Contributors"></a>
<a href="https://github.com/dk900912/redis-keys-detector"><img src="https://img.shields.io/github/repo-size/dk900912/redis-keys-detector" alt="GitHub repo size"></a>
</p>

### 背景
笔者目前工作于一公有云厂商，前期一款产品在线上使用了`Redis`中`KEYS *`高危指令，最终影响该款产品登录、连接功能异常，引发批量客诉。笔者所在班组由于职责所在需要全量排查所有内循环订购`Redis`的上层产品是否涉及此高危指令，但无奈30+款产品202个代码仓库的排查工作量巨大，于是就萌生了写一个工具的想法。
### 如何使用
- *预留了众多拓展点，请自行阅读源码！*
```java
KeysRiskDetector.builder()
        .input(xxx)
        .reporter(yyy)
        .scanners(zzz)
        .build()
        .detect();
```

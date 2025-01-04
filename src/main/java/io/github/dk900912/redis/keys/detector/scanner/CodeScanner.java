package io.github.dk900912.redis.keys.detector.scanner;

import io.github.dk900912.redis.keys.detector.model.SourceCodeRisk;
import java.io.File;
import java.util.List;

/**
 * 代码扫描器接口，定义了用于扫描源代码文件并检测潜在风险的方法。
 *
 * @author dukui
 */
public interface CodeScanner {

    /**
     * 获取当前扫描器支持的编程语言。
     *
     * @return 返回一个字符串，表示该扫描器支持的语言。
     */
    String support();

    /**
     * 判断给定的内容是否应被忽略。
     *
     * @param content 要检查的代码内容
     * @return 如果内容应被忽略，则返回 true；否则返回 false。
     */
    boolean ignore(String content);

    /**
     * 扫描指定的文件，并返回其中发现的所有源代码风险。
     *
     * @param file 要扫描的文件对象
     * @return 包含所有检测到的源代码风险的列表
     */
    List<SourceCodeRisk> scanFile(File file);
}
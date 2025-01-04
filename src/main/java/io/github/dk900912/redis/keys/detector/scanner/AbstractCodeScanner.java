package io.github.dk900912.redis.keys.detector.scanner;

import io.github.dk900912.redis.keys.detector.model.SourceCodeRisk;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 抽象代码扫描器类，实现了 {@link CodeScanner} 接口。
 * 提供了基本的代码扫描功能，并允许子类通过实现 {@link #getPattern()} 方法来自定义搜索模式。
 *
 * @author dukui
 */
public abstract class AbstractCodeScanner implements CodeScanner {

    /**
     * 获取用于匹配潜在风险代码的正则表达式模式。
     * 子类必须实现此方法以提供具体的匹配模式。
     *
     * @return 正则表达式模式对象
     */
    protected abstract Pattern getPattern();

    /**
     * 判断给定的内容是否应被忽略（例如注释行）。
     *
     * @param content 要检查的代码内容
     * @return 如果内容应被忽略，则返回 true；否则返回 false。
     */
    public boolean ignore(String content) {
        if (StringUtils.isBlank(content)) {
            return true;
        }
        String trimmed = content.trim();
        return trimmed.startsWith("//")
                || trimmed.startsWith("/*")
                || trimmed.startsWith("/**")
                || trimmed.startsWith("*")
                || trimmed.endsWith("*/");
    }

    /**
     * 扫描指定的文件，并返回其中发现的所有源代码风险。
     *
     * @param file 要扫描的文件对象
     * @return 包含所有检测到的源代码风险的列表
     */
    @Override
    public List<SourceCodeRisk> scanFile(File file) {
        List<SourceCodeRisk> sourceCodeRisks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (!ignore(line) && getPattern().matcher(line).find()) {
                    sourceCodeRisks.add(new SourceCodeRisk(file.getAbsolutePath(), lineNumber, line.trim()));
                }
            }
        } catch (IOException e) {
            // Ignored
        }
        return sourceCodeRisks;
    }
}
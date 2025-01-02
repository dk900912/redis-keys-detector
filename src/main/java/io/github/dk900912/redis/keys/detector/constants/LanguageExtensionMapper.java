package io.github.dk900912.redis.keys.detector.constants;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LanguageExtensionMapper {

    // 定义常见编程语言及其文件扩展名
    // key: extension, value: language name
    private static final Map<String, String> LANGUAGE_EXTENSIONS = new HashMap<>();

    static {
        LANGUAGE_EXTENSIONS.put("java", "Java");
        LANGUAGE_EXTENSIONS.put("py", "Python");
        LANGUAGE_EXTENSIONS.put("js", "JavaScript");
        LANGUAGE_EXTENSIONS.put("ts", "TypeScript");
        LANGUAGE_EXTENSIONS.put("c", "C");
        LANGUAGE_EXTENSIONS.put("cpp", "C++");
        LANGUAGE_EXTENSIONS.put("h", "C/C++ Header");
        LANGUAGE_EXTENSIONS.put("cs", "C#");
        LANGUAGE_EXTENSIONS.put("rb", "Ruby");
        LANGUAGE_EXTENSIONS.put("php", "PHP");
        LANGUAGE_EXTENSIONS.put("go", "Go");
        LANGUAGE_EXTENSIONS.put("rs", "Rust");
        LANGUAGE_EXTENSIONS.put("swift", "Swift");
        LANGUAGE_EXTENSIONS.put("kt", "Kotlin");
        LANGUAGE_EXTENSIONS.put("scala", "Scala");
        LANGUAGE_EXTENSIONS.put("m", "Objective-C");
        LANGUAGE_EXTENSIONS.put("html", "HTML");
        LANGUAGE_EXTENSIONS.put("css", "CSS");
        LANGUAGE_EXTENSIONS.put("sh", "Shell");
        LANGUAGE_EXTENSIONS.put("sql", "SQL");
        LANGUAGE_EXTENSIONS.put("vue", "Vue");
    }

    // 获取语言的扩展名
    public static String getExtension(String language) {
        for (Map.Entry<String, String> entry : LANGUAGE_EXTENSIONS.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(language)) {
                return entry.getKey();
            }
        }
        int lastIndexOfDot = language.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            return language;
        }
        return language.substring(lastIndexOfDot + 1).toLowerCase();
    }

    // 检查是否是已知的语言扩展名
    public static boolean isKnownExtension(String extension) {
        return StringUtils.isNotBlank(extension)
                && LANGUAGE_EXTENSIONS.containsKey(extension.toLowerCase());
    }

    // 获取语言的名称
    public static String getLanguageName(String extension) {
        return isKnownExtension(extension) ?
                LANGUAGE_EXTENSIONS.get(extension.toLowerCase()) : null;
    }

    // 获取语言的名称
    public static String getLanguageName(File file) {
        final String fileName = file.getName();
        int lastIndexOfDot = fileName.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            return null;
        }
        String extension = fileName.substring(lastIndexOfDot + 1).toLowerCase();
        return getLanguageName(extension);
    }

    // 私有构造函数，防止实例化
    private LanguageExtensionMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }
}
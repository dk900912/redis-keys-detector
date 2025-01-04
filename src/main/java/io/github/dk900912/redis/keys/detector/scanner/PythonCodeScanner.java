package io.github.dk900912.redis.keys.detector.scanner;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class PythonCodeScanner extends AbstractCodeScanner {

    // keys('*')
    private final Pattern REDIS_KEYS_PATTERN_PYTHON = Pattern.compile("\\.keys\\(\\s*[a-zA-Z0-9_\'*]+\\s*\\)");

    @Override
    public String support() {
        return "Python";
    }

    @Override
    public boolean ignore(String content) {
        if (StringUtils.isBlank(content)) {
            return true;
        }
        String trimmed = content.trim();
        return trimmed.startsWith("#") || trimmed.startsWith("'''");
    }

    @Override
    protected Pattern getPattern() {
        return REDIS_KEYS_PATTERN_PYTHON;
    }
}

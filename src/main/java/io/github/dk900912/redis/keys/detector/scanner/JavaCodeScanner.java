package io.github.dk900912.redis.keys.detector.scanner;

import java.util.regex.Pattern;

public class JavaCodeScanner extends AbstractCodeScanner {

    // keys("*")
    // getKeysByPattern("*")
    private static final Pattern REDIS_KEYS_PATTERN_JAVA = Pattern.compile("(getKeysByPattern|keys)\\(\\s*[ a-zA-Z0-9_\"*+]+\\s*\\)");

    @Override
    public String support() {
        return "Java";
    }

    @Override
    protected Pattern getPattern() {
        return REDIS_KEYS_PATTERN_JAVA;
    }
}

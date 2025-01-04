package io.github.dk900912.redis.keys.detector.scanner;

import java.util.regex.Pattern;

public class CCodeScanner extends AbstractCodeScanner {

    // keys("*"
    // "KEYS *"
    // "keys *"
    private static final Pattern REDIS_KEYS_PATTERN_C = Pattern.compile("\\(\"keys\",[ ]?[ a-zA-Z_\"*+]+\\)|\\(\"KEYS\",[ ]?[ a-zA-Z_\"*+]+\\)|\"keys [a-zA-Z0-9_*]+\"|\"KEYS [a-zA-Z0-9_*]+\"|\\.keys\\([a-zA-Z0-9_\"*]+");

    @Override
    public String support() {
        return "C";
    }

    @Override
    protected Pattern getPattern() {
        return REDIS_KEYS_PATTERN_C;
    }
}

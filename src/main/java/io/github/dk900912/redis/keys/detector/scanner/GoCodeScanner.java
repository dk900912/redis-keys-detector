package io.github.dk900912.redis.keys.detector.scanner;

import java.util.regex.Pattern;

public class GoCodeScanner extends AbstractCodeScanner {

    // ("KEYS", "*")
    // ("keys", "*")
    // Keys(ctx, "*")
    private static final Pattern REDIS_KEYS_PATTERN_GO = Pattern.compile("\\(\"keys\",[ ]?[ a-zA-Z_\"*+]+\\)|\\(\"KEYS\",[ ]?[ a-zA-Z_\"*+]+\\)|Keys\\(\\w{1,15},[ ]?[ a-zA-Z_\"*+]+\\)");

    @Override
    public String support() {
        return "Go";
    }

    @Override
    protected Pattern getPattern() {
        return REDIS_KEYS_PATTERN_GO;
    }
}

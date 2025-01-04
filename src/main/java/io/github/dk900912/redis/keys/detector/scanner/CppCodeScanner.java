package io.github.dk900912.redis.keys.detector.scanner;

import java.util.regex.Pattern;

public class CppCodeScanner extends AbstractCodeScanner {

    // boost::redis: execute("keys", "*")
    // redis-plus-plus: keys(*)
    // hiredis: "keys *"
    // cpp_redis: client.keys("*", [](const std::vector<std::string>& keys) {...}
    private static final Pattern REDIS_KEYS_PATTERN_CPP = Pattern.compile("\\(\"keys\",[ ]?[ a-zA-Z_\"*+]+\\)|\\(\"KEYS\",[ ]?[ a-zA-Z_\"*+]+\\)|\"keys [a-zA-Z0-9_*]+\"|\"KEYS [a-zA-Z0-9_*]+\"|\\.keys\\([a-zA-Z0-9_\"*]+");

    @Override
    public String support() {
        return "C++";
    }

    @Override
    protected Pattern getPattern() {
        return REDIS_KEYS_PATTERN_CPP;
    }
}

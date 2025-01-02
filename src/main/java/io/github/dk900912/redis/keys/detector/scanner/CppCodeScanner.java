package io.github.dk900912.redis.keys.detector.scanner;

import io.github.dk900912.redis.keys.detector.model.SourceCodeRisk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CppCodeScanner extends AbstractCodeScanner {

    // boost::redis: execute("keys", "*")
    // redis-plus-plus: keys(*)
    // hiredis: "keys *"
    // cpp_redis: client.keys("*", [](const std::vector<std::string>& keys) {...}
    private static final Pattern REDIS_COMMAND_PATTERN_CPP = Pattern.compile("\\(\"keys\",[ ]?[ a-zA-Z_\"*+]+\\)|\\(\"KEYS\",[ ]?[ a-zA-Z_\"*+]+\\)|\"keys [a-zA-Z0-9_*]+\"|\"KEYS [a-zA-Z0-9_*]+\"|\\.keys\\([a-zA-Z0-9_\"*]+");

    @Override
    public String support() {
        return "C++";
    }

    @Override
    public List<SourceCodeRisk> scanFile(File file) {
        List<SourceCodeRisk> sourceCodeRisks = new ArrayList<>();
        int riskLineNumber = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                riskLineNumber++;
                if (!shouldIgnored(line) && REDIS_COMMAND_PATTERN_CPP.matcher(line).find()) {
                    sourceCodeRisks.add(new SourceCodeRisk(file.getAbsolutePath(), riskLineNumber, line.trim()));
                }
            }
        } catch (IOException e) {
            // Ignored
        }
        return sourceCodeRisks;
    }
}

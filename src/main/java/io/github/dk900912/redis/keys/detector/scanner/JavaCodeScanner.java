package io.github.dk900912.redis.keys.detector.scanner;

import io.github.dk900912.redis.keys.detector.model.SourceCodeRisk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class JavaCodeScanner extends AbstractCodeScanner {

    // keys("*")
    // getKeysByPattern("*")
    private static final Pattern REDIS_KEYS_PATTERN_JAVA = Pattern.compile("(getKeysByPattern|keys)\\(\\s*[ a-zA-Z0-9_\"*+]+\\s*\\)");

    @Override
    public String support() {
        return "Java";
    }

    public List<SourceCodeRisk> scanFile(File file) {
        List<SourceCodeRisk> sourceCodeRisks = new ArrayList<>();
        int riskLineNumber = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                riskLineNumber++;
                if (!shouldIgnored(line) && REDIS_KEYS_PATTERN_JAVA.matcher(line).find()) {
                    sourceCodeRisks.add(new SourceCodeRisk(file.getAbsolutePath(), riskLineNumber, line.trim()));
                }
            }
        } catch (IOException e) {
            // Ignored
        }
        return sourceCodeRisks;
    }
}

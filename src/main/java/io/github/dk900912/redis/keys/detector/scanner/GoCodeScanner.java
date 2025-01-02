package io.github.dk900912.redis.keys.detector.scanner;

import io.github.dk900912.redis.keys.detector.model.SourceCodeRisk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    public List<SourceCodeRisk> scanFile(File file) {
        List<SourceCodeRisk> sourceCodeRisks = new ArrayList<>();
        int riskLineNumber = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                riskLineNumber++;
                if (!shouldIgnored(line) && REDIS_KEYS_PATTERN_GO.matcher(line).find()) {
                    sourceCodeRisks.add(new SourceCodeRisk(file.getAbsolutePath(), riskLineNumber, line.trim()));
                }
            }
        } catch (IOException e) {
            // Ignored
        }
        return sourceCodeRisks;
    }
}

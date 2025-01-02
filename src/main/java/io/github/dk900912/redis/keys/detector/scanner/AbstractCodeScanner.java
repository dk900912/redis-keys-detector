package io.github.dk900912.redis.keys.detector.scanner;

import io.github.dk900912.redis.keys.detector.model.SourceCodeRisk;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class AbstractCodeScanner implements CodeScanner {

    @Override
    public String support() {
        return "";
    }

    @Override
    public List<SourceCodeRisk> scanFile(File file) {
        return Collections.emptyList();
    }

    protected boolean shouldIgnored(String content) {
        return content.trim().startsWith("//") || content.trim().startsWith("/*");
    }
}

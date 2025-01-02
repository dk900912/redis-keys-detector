package io.github.dk900912.redis.keys.detector.scanner;

import io.github.dk900912.redis.keys.detector.model.SourceCodeRisk;

import java.io.File;
import java.util.List;

public interface CodeScanner {

    public String support();

    public List<SourceCodeRisk>  scanFile(File file);
}


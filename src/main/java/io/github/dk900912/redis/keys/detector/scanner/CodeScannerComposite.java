package io.github.dk900912.redis.keys.detector.scanner;

import io.github.dk900912.redis.keys.detector.constants.LanguageExtensionMapper;
import io.github.dk900912.redis.keys.detector.model.SourceCodeRisk;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CodeScannerComposite {

    public static final List<CodeScanner> DEFAULT_CODE_SCANNERS = new ArrayList<>();

    static {
        DEFAULT_CODE_SCANNERS.add(new JavaCodeScanner());
        DEFAULT_CODE_SCANNERS.add(new GoCodeScanner());
        DEFAULT_CODE_SCANNERS.add(new CppCodeScanner());
        DEFAULT_CODE_SCANNERS.add(new CCodeScanner());
        DEFAULT_CODE_SCANNERS.add(new PythonCodeScanner());
    }

    private final List<CodeScanner> codeScanners = new ArrayList<>();

    public CodeScannerComposite(List<CodeScanner> codeScanners) {
        this.codeScanners.addAll(codeScanners);
    }

    public List<SourceCodeRisk> scanFile(List<File> sourceFiles) {
        List<SourceCodeRisk> sourceCodeRisks = new ArrayList<>();
        for (File file : sourceFiles) {
            for (CodeScanner codeScanner : getCodeScanners()) {
                if (!codeScanner.support().equals(LanguageExtensionMapper.getLanguageName(file))) {
                    continue;
                }
                sourceCodeRisks.addAll(codeScanner.scanFile(file));
            }
        }
        return sourceCodeRisks;
    }

    public List<CodeScanner> getCodeScanners() {
        return this.codeScanners;
    }
}

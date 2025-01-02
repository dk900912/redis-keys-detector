package io.github.dk900912.redis.keys.detector.command;

import io.github.dk900912.redis.keys.detector.model.BranchSimpleInfo;
import io.github.dk900912.redis.keys.detector.scanner.CodeScannerComposite;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScanSourceFileCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(ScanSourceFileCommand.class);

    private final CodeScannerComposite codeScannerComposite;

    public ScanSourceFileCommand(CodeScannerComposite codeScannerComposite) {
        this.codeScannerComposite = codeScannerComposite;
    }

    @Override
    public boolean execute(Context context) throws Exception {
        final RiskDetectionContext ctx = (RiskDetectionContext) context;
        logger.info("STEP 5: Scan Source File --- {}", ctx);
        final BranchSimpleInfo busiestBranch = ctx.getBusiestBranch();
        scanSourceFile(busiestBranch);
        return false;
    }

    private void scanSourceFile(BranchSimpleInfo busiestBranch) {
        final List<File> sourceFiles = busiestBranch.getSourceFiles();
        busiestBranch.setSourceCodeRisk(new ArrayList<>(codeScannerComposite.scanFile(sourceFiles)));
    }
}

package io.github.dk900912.redis.keys.detector.support;

import io.github.dk900912.redis.keys.detector.model.BranchSimpleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleRiskDetectionReporter implements RiskDetectionReporter {

    private static final Logger logger = LoggerFactory.getLogger(SimpleRiskDetectionReporter.class);

    @Override
    public void report(BranchSimpleInfo busiestBranch) throws Exception {
        logger.info("O======}==================>");
        logger.info(
                "'{}/{}' repo has '{}' source files, the main content distribution is as follows '{}', there are '{}' risks.",
                busiestBranch.getRepositoryName(),
                BranchNameUtil.getSimplifiedBranchNameBasedOrigin(busiestBranch.getBranchName()),
                busiestBranch.getSourceFiles().size(),
                busiestBranch.getLanguageCounts(),
                busiestBranch.getSourceCodeRisk().size()
        );
        busiestBranch.getSourceCodeRisk()
                .forEach(risk -> {
                    logger.info("file_name='{}' | line_number='{}' | content='{}'", risk.getSourceCodeName(), risk.getRiskLineNum(), risk.getRisk());
                });
        logger.info("<==================}======O");
    }
}

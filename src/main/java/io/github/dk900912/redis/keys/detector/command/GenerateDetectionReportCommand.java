package io.github.dk900912.redis.keys.detector.command;

import io.github.dk900912.redis.keys.detector.model.BranchSimpleInfo;
import io.github.dk900912.redis.keys.detector.support.RiskDetectionReporter;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenerateDetectionReportCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(GenerateDetectionReportCommand.class);

    private RiskDetectionReporter riskDetectionReporter;

    public GenerateDetectionReportCommand(RiskDetectionReporter riskDetectionReporter) {
        this.riskDetectionReporter = riskDetectionReporter;
    }

    @Override
    public boolean execute(Context context) throws Exception {
        final RiskDetectionContext ctx = (RiskDetectionContext) context;
        logger.info("STEP 6: Generate Detection Report --- {}", ctx);
        final BranchSimpleInfo busiestBranch = ctx.getBusiestBranch();
        try {
            riskDetectionReporter.report(busiestBranch);
            return false;
        } catch (Exception e) {
            // Ignored
        }
        return true;
    }
}

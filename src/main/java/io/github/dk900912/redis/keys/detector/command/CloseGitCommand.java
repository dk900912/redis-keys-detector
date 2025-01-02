package io.github.dk900912.redis.keys.detector.command;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloseGitCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(CloseGitCommand.class);


    @Override
    public boolean execute(Context context) throws Exception {
        final RiskDetectionContext ctx = (RiskDetectionContext) context;
        logger.info("STEP 7: Close Git --- {}", ctx);
        ctx.closeGit();
        return true;
    }
}

package io.github.dk900912.redis.keys.detector.command;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenGitCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(OpenGitCommand.class);

    @Override
    public boolean execute(Context context) throws Exception {
        final RiskDetectionContext ctx = (RiskDetectionContext) context;
        logger.info("STEP 1: Open Git --- {}", ctx);
        return !ctx.openGit(ctx.getRepositoryDirectory());
    }
}

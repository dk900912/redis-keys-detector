package io.github.dk900912.redis.keys.detector.command;

import io.github.dk900912.redis.keys.detector.support.BranchNameUtil;
import io.github.dk900912.redis.keys.detector.model.BranchSimpleInfo;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.lib.Ref;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SwitchBusiestBranchCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(SwitchBusiestBranchCommand.class);

    @Override
    public boolean execute(Context context) throws Exception {
        final RiskDetectionContext ctx = (RiskDetectionContext) context;
        logger.info("STEP 3: Switch Busiest Branch --- {}", ctx);
        final Git git = ctx.getGit();
        final BranchSimpleInfo busiestBranch = ctx.getBusiestBranch();
        return switchBranch(git, busiestBranch);
    }

    private boolean switchBranch(Git git, BranchSimpleInfo busiestBranch) {
        try {
            List<String> simplifiedBranchNames = git.branchList()
                    .call()
                    .stream()
                    .map(Ref::getName)
                    .map(BranchNameUtil::getSimplifiedBranchNameBasedOrigin)
                    .toList();
            final String simplifiedBusiestBranchName = BranchNameUtil.getSimplifiedBranchNameBasedOrigin(busiestBranch.getBranchName());
            // 检查本地是否存在该分支，如果不存在则创建
            if (!simplifiedBranchNames.contains(simplifiedBusiestBranchName)) {
                git.checkout()
                        .setName(simplifiedBusiestBranchName)
                        .setCreateBranch(true)
                        .setStartPoint(busiestBranch.getBranchName())
                        .call();
                return false;
            } else {
                git.checkout()
                        .setName(simplifiedBusiestBranchName)
                        .call();
                return false;
            }
        } catch (GitAPIException | JGitInternalException e) {
            logger.error("Failed to switch branch: {}", BranchNameUtil.getSimplifiedBranchNameBasedOrigin(busiestBranch.getBranchName()), e);
        }
        return true;
    }
}

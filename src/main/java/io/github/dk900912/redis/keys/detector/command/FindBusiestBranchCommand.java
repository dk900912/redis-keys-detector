package io.github.dk900912.redis.keys.detector.command;

import io.github.dk900912.redis.keys.detector.model.BranchSimpleInfo;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.SymbolicRef;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FindBusiestBranchCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(FindBusiestBranchCommand.class);

    @Override
    public boolean execute(Context context) throws Exception {
        final RiskDetectionContext ctx = (RiskDetectionContext) context;
        // 获取所有分支
        List<Ref> branches = ctx.getGit()
                .branchList()
                .setListMode(ListBranchCommand.ListMode.REMOTE)
                .call()
                .stream()
                .filter(ref -> !(ref.getClass().getName().contains("RefDirectory$") || ref instanceof SymbolicRef))
                .toList();

        // 统计每个分支的提交次数
        List<BranchSimpleInfo> branchSimpleInfoList = new ArrayList<>();
        try (RevWalk revWalk = new RevWalk(ctx.getGit().getRepository())) {
            for (Ref branch : branches) {
                ObjectId commitId = branch.getObjectId();
                if (commitId != null) {
                    RevCommit commit = revWalk.parseCommit(commitId);
                    int commitTime = commit.getCommitTime();
                    revWalk.reset(); // 重置 RevWalk 否则统计不准确
                    revWalk.markStart(commit);
                    int count = 0;
                    for (RevCommit ignored : revWalk) {
                        count++;
                    }
                    branchSimpleInfoList.add(new BranchSimpleInfo(branch.getName(), count, commitTime));
                }
            }
        }

        // 找到提交次数最多的分支
        final BranchSimpleInfo busiestBranch = findBusiestBranch(branchSimpleInfoList);
        ctx.setBusiestBranch(busiestBranch);
        logger.info("STEP 2: Find Busiest Branch --- {}", ctx);
        return false;
    }

    protected BranchSimpleInfo findBusiestBranch(List<BranchSimpleInfo> branchSimpleInfoList) {
        return branchSimpleInfoList.stream().max(Comparator.comparingInt(BranchSimpleInfo::getCommitCount)
                        .thenComparing(BranchSimpleInfo::getLatestCommit))
                .orElse(new BranchSimpleInfo("#NA", 0, 0));

    }
}

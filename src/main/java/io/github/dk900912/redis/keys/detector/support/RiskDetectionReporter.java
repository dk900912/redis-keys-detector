package io.github.dk900912.redis.keys.detector.support;

import io.github.dk900912.redis.keys.detector.model.BranchSimpleInfo;

public interface RiskDetectionReporter {
    public void report(BranchSimpleInfo busiestBranch) throws Exception;
}

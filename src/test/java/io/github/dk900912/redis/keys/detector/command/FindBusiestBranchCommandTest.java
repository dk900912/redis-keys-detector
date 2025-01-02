package io.github.dk900912.redis.keys.detector.command;

import io.github.dk900912.redis.keys.detector.model.BranchSimpleInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FindBusiestBranchCommandTest {

    private FindBusiestBranchCommand findBusiestBranchCommand;

    @BeforeEach
    public void setUp() {
        findBusiestBranchCommand = new FindBusiestBranchCommand();
    }

    @Test
    public void findBusiestBranch_EmptyList_ReturnsDefaultBranch() {
        List<BranchSimpleInfo> branchSimpleInfoList = new ArrayList<>();
        BranchSimpleInfo busiestBranch = findBusiestBranchCommand.findBusiestBranch(branchSimpleInfoList);
        assertEquals("#NA", busiestBranch.getBranchName());
    }

    @Test
    public void findBusiestBranch_SingleBranch_ReturnsSameBranch() {
        List<BranchSimpleInfo> branchSimpleInfoList = new ArrayList<>();
        BranchSimpleInfo branch = new BranchSimpleInfo("branch1", 10, (int) (System.currentTimeMillis() / 1000L));
        branchSimpleInfoList.add(branch);
        BranchSimpleInfo busiestBranch = findBusiestBranchCommand.findBusiestBranch(branchSimpleInfoList);
        assertEquals(branch, busiestBranch);
    }

    @Test
    public void findBusiestBranch_MultipleBranchesDifferentCommitCounts_ReturnsBusiestBranch() {
        List<BranchSimpleInfo> branchSimpleInfoList = new ArrayList<>();
        branchSimpleInfoList.add(new BranchSimpleInfo("branch1", 5, (int) (System.currentTimeMillis() / 1000L)));
        branchSimpleInfoList.add(new BranchSimpleInfo("branch2", 10, (int) (System.currentTimeMillis() / 1000L)));
        branchSimpleInfoList.add(new BranchSimpleInfo("branch3", 3, (int) (System.currentTimeMillis() / 1000L)));
        BranchSimpleInfo busiestBranch = findBusiestBranchCommand.findBusiestBranch(branchSimpleInfoList);
        assertEquals("branch2", busiestBranch.getBranchName());
    }

    @Test
    public void findBusiestBranch_MultipleBranchesSameCommitCountsAndLatestCommits_ReturnsFirstBranch() {
        List<BranchSimpleInfo> branchSimpleInfoList = new ArrayList<>();
        int now = (int) (System.currentTimeMillis() / 1000L);
        branchSimpleInfoList.add(new BranchSimpleInfo("branch1", 5, now));
        branchSimpleInfoList.add(new BranchSimpleInfo("branch2", 5, now));
        branchSimpleInfoList.add(new BranchSimpleInfo("branch3", 5, now + 100));
        BranchSimpleInfo busiestBranch = findBusiestBranchCommand.findBusiestBranch(branchSimpleInfoList);
        assertEquals("branch3", busiestBranch.getBranchName());
    }
}

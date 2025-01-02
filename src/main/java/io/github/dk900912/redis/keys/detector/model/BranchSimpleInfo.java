package io.github.dk900912.redis.keys.detector.model;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

public class BranchSimpleInfo {

    private final String branchName;

    private final int commitCount;

    private final LocalDateTime latestCommit;

    private String repositoryName;

    private Map<String, Integer> languageCounts;

    private List<File> sourceFiles;

    private List<SourceCodeRisk> sourceCodeRisks;

    public BranchSimpleInfo(String branchName, int commitCount, int latestCommit) {
        this.branchName = branchName;
        this.commitCount = commitCount;
        Instant instant = Instant.ofEpochMilli((long) latestCommit * 1000);
        this.latestCommit = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public String getBranchName() {
        return branchName;
    }

    public int getCommitCount() {
        return commitCount;
    }

    public LocalDateTime getLatestCommit() {
        return latestCommit;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public Map<String, Integer> getLanguageCounts() {
        return languageCounts;
    }

    public void setLanguageCounts(Map<String, Integer> languageCounts) {
        this.languageCounts = languageCounts;
    }

    public List<File> getSourceFiles() {
        return sourceFiles;
    }

    public void setSourceFiles(List<File> sourceFiles) {
        this.sourceFiles = sourceFiles;
    }

    public List<SourceCodeRisk> getSourceCodeRisk() {
        return sourceCodeRisks;
    }

    public void setSourceCodeRisk(List<SourceCodeRisk> sourceCodeRisks) {
        this.sourceCodeRisks = sourceCodeRisks;
    }
}

package io.github.dk900912.redis.keys.detector.command;

import io.github.dk900912.redis.keys.detector.model.BranchSimpleInfo;
import io.github.dk900912.redis.keys.detector.support.BranchNameUtil;
import org.apache.commons.chain.impl.ContextBase;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * 风险检测上下文类，扩展了 {@link ContextBase}。
 * 该类用于管理 Git 仓库的打开和关闭操作，并提供与风险检测相关的上下文信息，避免通过过多的方法签名来透传参数。
 *
 * @author dukui
 */
public class RiskDetectionContext extends ContextBase {

    private static final Logger logger = LoggerFactory.getLogger(RiskDetectionContext.class);

    private transient File repositoryDirectory;

    private transient Git git;

    private transient BranchSimpleInfo busiestBranch;

    public File getRepositoryDirectory() {
        return this.repositoryDirectory;
    }

    public void setRepositoryDirectory(File repositoryDirectory) {
        this.repositoryDirectory = repositoryDirectory;
    }

    public Git getGit() {
        return this.git;
    }

    public void setGit(Git git) {
        this.git = git;
    }

    public BranchSimpleInfo getBusiestBranch() {
        return this.busiestBranch;
    }

    public void setBusiestBranch(BranchSimpleInfo busiestBranch) {
        this.busiestBranch = busiestBranch;
    }

    public boolean openGit(File repoDirectory) {
        try {
            final Repository repository = new FileRepositoryBuilder()
                    .setGitDir(new File(repoDirectory, ".git"))
                    .readEnvironment()
                    .build();
            setGit(new Git(repository));
            return true;
        } catch (IOException e) {
            logger.error("Failed to open repository: {}", repoDirectory.getAbsolutePath(), e);
        }
        return false;
    }

    public void closeGit() {
        getGit().close();
    }

    @Override
    public String toString() {
        return new StringJoiner("|", "[", "]")
                .add(repositoryDirectory.getName())
                .add(Optional.ofNullable(busiestBranch)
                        .map(BranchSimpleInfo::getBranchName)
                        .map(BranchNameUtil::getSimplifiedBranchNameBasedOrigin)
                        .orElse("")
                ).toString();
    }
}

package io.github.dk900912.redis.keys.detector.command;

import io.github.dk900912.redis.keys.detector.support.BranchNameUtil;
import io.github.dk900912.redis.keys.detector.constants.LanguageExtensionMapper;
import io.github.dk900912.redis.keys.detector.model.BranchSimpleInfo;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopulateBusiestBranchInfoCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(PopulateBusiestBranchInfoCommand.class);

    @Override
    public boolean execute(Context context) throws Exception {
        final RiskDetectionContext ctx = (RiskDetectionContext) context;
        logger.info("STEP 4: Populate Busiest Branch Info --- {}", ctx);
        final Git git = ctx.getGit();
        final BranchSimpleInfo busiestBranch = ctx.getBusiestBranch();
        final File repositoryDirectory = ctx.getRepositoryDirectory();
        return !populateBusiestBranchInfo(git, busiestBranch, repositoryDirectory);
    }

    public boolean populateBusiestBranchInfo(Git git, BranchSimpleInfo busiestBranch, File repositoryDirectory) {
        TreeWalk treeWalk = null;
        try {
            // 获取当前分支名称
            String currentBranch = git.getRepository().getFullBranch();

            // 获取当前分支的最新提交的树
            treeWalk = new TreeWalk(git.getRepository());
            treeWalk.addTree(git.getRepository().resolve(currentBranch + "^{tree}"));
            treeWalk.setRecursive(true);

            // 统计文件数量
            Map<String, Integer> languageCounts = new HashMap<>();
            // 搜集源码文件
            List<File> sourceFiles = new ArrayList<>();
            while (treeWalk.next()) {
                String path = treeWalk.getPathString();
                Path filePath = Paths.get(repositoryDirectory.getAbsolutePath(), path);
                if (Files.isRegularFile(filePath)) {
                    String extension = LanguageExtensionMapper.getExtension(path);
                    String language = LanguageExtensionMapper.getLanguageName(extension);
                    if (language != null) {
                        languageCounts.put(language, languageCounts.getOrDefault(language, 0) + 1);
                        sourceFiles.add(filePath.toFile());
                    }
                }
            }
            busiestBranch.setRepositoryName(git.getRepository().getWorkTree().getName());
            busiestBranch.setSourceFiles(sourceFiles);
            busiestBranch.setLanguageCounts(languageCounts);
            return true;
        } catch (IOException e) {
            logger.error("Failed to populate busiest branch info: {}", BranchNameUtil.getSimplifiedBranchNameBasedOrigin(busiestBranch.getBranchName()), e);
        } finally {
            // 关闭资源
            if (treeWalk != null) {
                treeWalk.close();
            }
        }
        return false;
    }
}

package io.github.dk900912.redis.keys.detector.command;

import io.github.dk900912.redis.keys.detector.constants.LanguageExtensionMapper;
import io.github.dk900912.redis.keys.detector.model.BranchSimpleInfo;
import io.github.dk900912.redis.keys.detector.support.BranchNameUtil;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        try (TreeWalk treeWalk = new TreeWalk(git.getRepository())) {
            // 获取当前分支名称
            String currentBranch = git.getRepository().getFullBranch();

            // 获取当前分支的最新提交的树
            treeWalk.addTree(git.getRepository().resolve(currentBranch + "^{tree}"));
            treeWalk.setRecursive(true);

            // 收集所有路径，然后并行处理
            List<String> paths = new ArrayList<>();
            while (treeWalk.next()) {
                paths.add(treeWalk.getPathString());
            }

            // 统计文件数量
            Map<String, Integer> languageCounts = new ConcurrentHashMap<>();
            // 搜集源码文件
            List<File> sourceFiles = Collections.synchronizedList(new ArrayList<>());
            paths.parallelStream().forEach(path -> {
                Path filePath = Paths.get(repositoryDirectory.getAbsolutePath(), path);
                if (Files.isRegularFile(filePath, LinkOption.NOFOLLOW_LINKS)) {
                    String extension = LanguageExtensionMapper.getExtension(path);
                    String language = LanguageExtensionMapper.getLanguageName(extension);
                    if (language != null) {
                        languageCounts.merge(language, 1, Integer::sum);
                        sourceFiles.add(filePath.toFile());
                    }
                }
            });
            busiestBranch.setRepositoryName(git.getRepository().getWorkTree().getName());
            busiestBranch.setSourceFiles(sourceFiles);
            busiestBranch.setLanguageCounts(languageCounts);
            return true;
        } catch (IOException e) {
            logger.error("Failed to populate busiest branch info: {}", BranchNameUtil.getSimplifiedBranchNameBasedOrigin(busiestBranch.getBranchName()), e);
        }
        return false;
    }
}

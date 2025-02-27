package io.github.dk900912.redis.keys.detector.support;

import io.github.dk900912.redis.keys.detector.command.CloseGitCommand;
import io.github.dk900912.redis.keys.detector.command.FindBusiestBranchCommand;
import io.github.dk900912.redis.keys.detector.command.GenerateDetectionReportCommand;
import io.github.dk900912.redis.keys.detector.command.OpenGitCommand;
import io.github.dk900912.redis.keys.detector.command.PopulateBusiestBranchInfoCommand;
import io.github.dk900912.redis.keys.detector.command.RiskDetectionContext;
import io.github.dk900912.redis.keys.detector.command.ScanSourceFileCommand;
import io.github.dk900912.redis.keys.detector.command.SwitchBusiestBranchCommand;
import io.github.dk900912.redis.keys.detector.scanner.CodeScanner;
import io.github.dk900912.redis.keys.detector.scanner.CodeScannerComposite;
import org.apache.commons.chain.impl.ChainBase;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * KeysRiskDetector为启动引导类，用于管理Git仓库的扫描和风险检测流程。
 * 它通过一系列命令链来执行以下任务：
 * - 打开Git仓库
 * - 查找最繁忙的分支
 * - 切换到最繁忙的分支
 * - 填充最繁忙分支的信息
 * - 扫描源文件
 * - 生成检测报告
 * - 关闭Git仓库
 *
 * @author dukui
 */
public class KeysRiskDetector {

    private static final Logger logger = LoggerFactory.getLogger(KeysRiskDetector.class);

    private String input;

    private Integer gitDirectoryMaxWalkingDepth;

    private RiskDetectionReporter reporter;

    private List<CodeScanner> scanners;

    private KeysRiskDetector() {}

    public void detect() {
        final ChainBase chainBase = createCommandChain();
        final FileSystemRepositoryDirectoryScanner fileSystemRepositoryDirectoryScanner
                = new FileSystemRepositoryDirectoryScanner(new GitDirectoryScannerStrategy(), getGitDirectoryMaxWalkingDepth());
        final List<File> repoDirectories
                = fileSystemRepositoryDirectoryScanner.getRepoDirectories(getInput());
        for (File repoDirectory : repoDirectories) {
            final RiskDetectionContext riskDetectionContext = new RiskDetectionContext();
            riskDetectionContext.setRepositoryDirectory(repoDirectory);
            try {
                chainBase.execute(riskDetectionContext);
            } catch (Exception e) {
                logger.error("Error occurred while detecting keys risk in {}", repoDirectory.getAbsolutePath(), e);
            }
        }
    }

    public static KeyRiskDetectorBuilder builder() {
        return new KeyRiskDetectorBuilder();
    }

    public String getInput() {
        return input;
    }

    public Integer getGitDirectoryMaxWalkingDepth() {
        return gitDirectoryMaxWalkingDepth;
    }

    public RiskDetectionReporter getReporter() {
        return reporter;
    }

    public List<CodeScanner> getScanners() {
        return scanners;
    }

    private ChainBase createCommandChain() {
        ChainBase chainBase = new ChainBase();
        chainBase.addCommand(new OpenGitCommand());
        chainBase.addCommand(new FindBusiestBranchCommand());
        chainBase.addCommand(new SwitchBusiestBranchCommand());
        chainBase.addCommand(new PopulateBusiestBranchInfoCommand());
        chainBase.addCommand(new ScanSourceFileCommand(new CodeScannerComposite(getScanners())));
        chainBase.addCommand(new GenerateDetectionReportCommand(getReporter()));
        chainBase.addCommand(new CloseGitCommand());
        return chainBase;
    }

    public static class KeyRiskDetectorBuilder {

        private String input;

        private Integer gitDirectoryMaxWalkingDepth;

        private RiskDetectionReporter reporter;

        private List<CodeScanner> scanners;

        public KeyRiskDetectorBuilder input(String input) {
            this.input = input;
            return this;
        }

        public KeyRiskDetectorBuilder gitDirectoryMaxWalkingDepth(Integer gitDirectoryMaxWalkingDepth) {
            this.gitDirectoryMaxWalkingDepth = gitDirectoryMaxWalkingDepth;
            return this;
        }

        public KeyRiskDetectorBuilder reporter(RiskDetectionReporter reporter) {
            this.reporter = reporter;
            return this;
        }

        public KeyRiskDetectorBuilder scanners(List<CodeScanner> scanners) {
            this.scanners = scanners;
            return this;
        }

        public KeysRiskDetector build() {
            KeysRiskDetector detector = new KeysRiskDetector();
            if (StringUtils.isBlank(this.input)) {
                throw new IllegalArgumentException("The 'input' argument can't be null");
            }
            if (this.gitDirectoryMaxWalkingDepth == null) {
                this.gitDirectoryMaxWalkingDepth = 3;
                logger.warn("The 'gitDirectoryMaxWalkingDepth' argument is null, using the default 3");
            }
            if (this.reporter == null) {
                this.reporter = new SimpleRiskDetectionReporter();
                logger.warn("The 'reporter' argument is null, using the default logging reporter");
            }
            if (CollectionUtils.isEmpty(this.scanners)) {
                this.scanners = CodeScannerComposite.DEFAULT_CODE_SCANNERS;
                logger.warn("The 'scanners' argument is null, using the default scanners: {}", this.scanners.stream().map(CodeScanner::support).toList());
            }
            detector.input = this.input;
            detector.gitDirectoryMaxWalkingDepth = this.gitDirectoryMaxWalkingDepth;
            detector.reporter = this.reporter;
            detector.scanners = scanners;
            return detector;
        }
    }
}

package io.github.dk900912.redis.keys.detector.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class FileSystemRepositoryDirectoryScanner implements RepositoryDirectoryScanner {

    public static final Logger logger = LoggerFactory.getLogger(FileSystemRepositoryDirectoryScanner.class);

    private final DirectoryScannerStrategy scannerStrategy;

    public FileSystemRepositoryDirectoryScanner() {
        this.scannerStrategy = new GitDirectoryScannerStrategy();
    }

    public FileSystemRepositoryDirectoryScanner(DirectoryScannerStrategy scannerStrategy) {
        this.scannerStrategy = scannerStrategy;
    }

    @Override
    public boolean shouldScan(String directoryPath) {
        if (directoryPath == null || directoryPath.isEmpty()) {
            return false;
        }
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            logger.error("Invalid repository directory: {}", directoryPath);
            return false;
        }
        return true;
    }

    @Override
    public List<File> getRepoDirectories(String directoryPath) {
        if (!shouldScan(directoryPath)) {
            return Collections.emptyList();
        }
        // 限制搜索深度：3
        try (Stream<Path> walk = Files.walk(Paths.get(directoryPath), 3)) {
            // 使用并行流
            return walk.parallel()
                    .filter(Files::isDirectory)
                    .filter(scannerStrategy::isValidRepository)
                    .map(Path::toFile)
                    .toList();
        } catch (IOException e) {
            logger.error("Failed to scan repository directory: {}", directoryPath);
        }

        return Collections.emptyList();
    }
}

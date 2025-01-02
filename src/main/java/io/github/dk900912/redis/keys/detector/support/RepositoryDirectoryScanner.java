package io.github.dk900912.redis.keys.detector.support;

import java.io.File;
import java.util.List;

public interface RepositoryDirectoryScanner {
    boolean shouldScan(String directoryPath);
    List<File> getRepoDirectories(String directoryPath);
}
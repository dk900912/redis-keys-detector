package io.github.dk900912.redis.keys.detector.support;

import java.nio.file.Files;
import java.nio.file.Path;

public class GitDirectoryScannerStrategy implements DirectoryScannerStrategy {
    @Override
    public boolean isValidRepository(Path path) {
        return Files.exists(path.resolve(".git"));
    }
}

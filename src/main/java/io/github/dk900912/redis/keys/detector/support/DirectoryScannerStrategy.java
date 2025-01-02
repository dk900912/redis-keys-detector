package io.github.dk900912.redis.keys.detector.support;

import java.nio.file.Path;

public interface DirectoryScannerStrategy {
    boolean isValidRepository(Path path);
}

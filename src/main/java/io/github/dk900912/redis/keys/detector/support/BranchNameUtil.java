package io.github.dk900912.redis.keys.detector.support;

import org.apache.commons.lang3.StringUtils;

public class BranchNameUtil {

    private BranchNameUtil() {}

    public static String getSimplifiedBranchNameBasedOrigin(String originBranchName) {
        if (StringUtils.isNotBlank(originBranchName)) {
            int lastSlashIndex = originBranchName.lastIndexOf('/');
            if (lastSlashIndex != -1) {
                originBranchName = originBranchName.substring(lastSlashIndex + 1);
            }
        }

        return originBranchName;
    }

}

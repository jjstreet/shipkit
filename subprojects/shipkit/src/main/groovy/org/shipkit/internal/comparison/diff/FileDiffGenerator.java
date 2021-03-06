package org.shipkit.internal.comparison.diff;

import difflib.DiffUtils;
import difflib.Patch;
import org.shipkit.internal.gradle.util.StringUtil;

import java.util.Arrays;
import java.util.List;

public class FileDiffGenerator {

    private static final String INDENTATION = "    ";

    /**
     * See {@link #generateDiff(String, String, int)}
     */
    public String generateDiff(String previousContent, String currentContent) {
        return generateDiff(previousContent, currentContent, 0);
    }

    /**
     * generates diff between contents of two files in the same format as "git diff"
     */
    public String generateDiff(String previousContent, String currentContent, int context) {
        List<String> previousLines = breakIntoLines(previousContent);
        List<String> currentLines = breakIntoLines(currentContent);

        Patch<String> patch = DiffUtils.diff(previousLines, currentLines);

        List<String> unifiedDiff = DiffUtils.generateUnifiedDiff("", "", previousLines, patch, context);

        if (unifiedDiff.size() <= 2) {
            return ""; // no differences found
        }

        List<String> diffWithoutFileNames = unifiedDiff.subList(2, unifiedDiff.size()); // remove file names

        return INDENTATION + StringUtil.join(diffWithoutFileNames, getLineSeparator() + INDENTATION);
    }

    private List<String> breakIntoLines(String previousContent) {
        return Arrays.asList(previousContent.split(getLineSeparator()));
    }

    private String getLineSeparator() {
        return System.getProperty("line.separator");
    }
}

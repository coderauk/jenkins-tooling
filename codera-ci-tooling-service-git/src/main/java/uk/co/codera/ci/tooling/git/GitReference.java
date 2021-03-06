package uk.co.codera.ci.tooling.git;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitReference {

    private static final Pattern PATTERN_VALID_GIT_REFERENCE = Pattern.compile("refs/(\\w*)/(.*)");
    private static final String BRANCH_DELIMITER = "/";
    private final String fullReference;
    private final String referenceType;
    private final String branchName;

    private GitReference(String reference) {
        Matcher matcher = validate(reference);
        this.fullReference = reference;
        this.referenceType = matcher.group(1);
        this.branchName = matcher.group(2);
    }

    public static GitReference from(String reference) {
        return new GitReference(reference);
    }

    public String branchName() {
        return this.branchName;
    }

    public String shortBranchName() {
        String longBranchName = branchName();
        if (longBranchName.contains(BRANCH_DELIMITER)) {
            return longBranchName.substring(longBranchName.indexOf(BRANCH_DELIMITER) + 1);
        }
        return longBranchName;
    }

    @Override
    public String toString() {
        return this.fullReference;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof GitReference)) {
            return false;
        }
        GitReference other = (GitReference) obj;
        return this.fullReference.equals(other.fullReference);
    }

    @Override
    public int hashCode() {
        return this.fullReference.hashCode();
    }

    private Matcher validate(String reference) {
        Matcher matcher = matcher(reference);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(String.format("Invalid git reference [%s]", reference));
        }
        return matcher;
    }

    private Matcher matcher(String reference) {
        return PATTERN_VALID_GIT_REFERENCE.matcher(reference);
    }

    public boolean isTag() {
        return referenceType.equals("tags");
    }
}
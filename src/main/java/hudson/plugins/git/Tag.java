package hudson.plugins.git;

import java.util.Objects;
import org.eclipse.jgit.lib.ObjectId;

/**
 * Git tag including SHA1 and message of the associated commit.
 */
public class Tag extends GitObject {
    private static final long serialVersionUID = 1L;
    /** SHA1 hash of the tagged commit */
    public String commitSHA1;
    /** Commit message of the tagged commit */
    public String commitMessage;

    /**
     * Returns the commit message.
     *
     * @return the commit message.
     */
    public String getCommitMessage() {
        return commitMessage;
    }

    /**
     * Sets the commit message.
     *
     * @param commitMessage the commit message
     */
    public void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
    }

    /**
     * Constructs a tag.
     *
     * @param name a {@link String} object.
     * @param sha1 a {@link ObjectId} object.
     */
    public Tag(String name, ObjectId sha1) {
        super(name, sha1);
    }

    /**
     * Returns the SHA1 hash of the tagged commit.
     *
     * @return the SHA1 hash of the tagged commit
     */
    public String getCommitSHA1() {
        return commitSHA1;
    }

    /**
     * Sets the SHA1 hash of the tagged commit.
     *
     * @param commitSHA1 the SHA1 hash of the tagged commit
     */
    public void setCommitSHA1(String commitSHA1) {
        this.commitSHA1 = commitSHA1;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * Includes sha1 and name in the comparison. Objects of subclasses of this object
     * are not equal to objects of this class, even if they add no fields.
     *
     * @param obj the reference object with which to compare.
     * @return true if this object is the same as the obj argument; false
     * otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tag other = (Tag) obj;
        return Objects.equals(this.name, other.name)
                && Objects.equals(this.sha1, other.sha1);
    }
}

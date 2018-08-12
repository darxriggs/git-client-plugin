package hudson.plugins.git;

import org.eclipse.jgit.lib.ObjectId;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

import java.io.Serializable;
import java.util.Objects;

/**
 * An object in a git repository.
 *
 * Includes the SHA1 and name of the object stored in git (tag, branch, etc.).
 */
@ExportedBean(defaultVisibility = 999)
public class GitObject implements Serializable {

    private static final long serialVersionUID = 1L;

    final ObjectId sha1;
    final String name;

    /**
     * Constructs a git object, a named SHA1 (tag, branch, etc.).
     *
     * @param name {@link String} name of this object
     * @param sha1 {@link ObjectId} which uniquely identifies this object
     */
    public GitObject(String name, ObjectId sha1) {
        this.name = name;
        this.sha1 = sha1;
    }

    /**
     * Returns the SHA1 hash of this git object as an {@link ObjectId}.
     *
     * @return {@link ObjectId} SHA1 of the object.
     */
    public ObjectId getSHA1() {
        return sha1;
    }

    /**
     * Returns the name of this git object (branch name, tag name, etc.).
     *
     * @return {@link String} name of the object.
     */
    @Exported
    public String getName() {
        return name;
    }

    /**
     * Returns the SHA1 hash of this git object as a {@link String}.
     *
     * @return {@link String} SHA1 of the object.
     */
    @Exported(name="SHA1")
    public String getSHA1String() {
        return sha1 != null ? sha1.name() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.sha1);
        hash = 97 * hash + Objects.hashCode(this.name);
        return hash;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * Includes sha1 and name in the comparison. Objects of subclasses of this object
     * are not equal to objects of this class, even if they add no fields.
     *
     * @param obj the reference object with which to compare.
     * @return true if this object is the same as the obj argument; false otherwise
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
        final GitObject other = (GitObject) obj;
        return Objects.equals(this.name, other.name)
                && Objects.equals(this.sha1, other.sha1);
    }
}

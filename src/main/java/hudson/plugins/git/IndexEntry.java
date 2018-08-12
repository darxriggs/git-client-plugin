package hudson.plugins.git;

import org.eclipse.jgit.submodule.SubmoduleWalk;

import java.io.Serializable;

/**
 * Git index / tree entry.
 *
 * @author nigelmagnay
 */
public class IndexEntry implements Serializable {
    String mode, type, object, file;

    /**
     * Returns the mode.
     *
     * @return the mode.
     */
    public String getMode() {
        return mode;
    }

    /**
     * Sets the mode.
     *
     * @param mode value to be assigned
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * Returns the type.
     *
     * @return the type.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type a {@link String} object.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns the associated object.
     *
     * @return the associated object.
     */
    public String getObject() {
        return object;
    }

    /**
     * Sets the associated object.
     *
     * @param object the associated object.
     */
    public void setObject(String object) {
        this.object = object;
    }

    /**
     * Returns the associated file.
     *
     * @return the associated file.
     */
    public String getFile() {
        return file;
    }

    /**
     * Sets the associated file.
     *
     * @param file the associated file
     */
    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return String.format("IndexEntry[mode=%s,type=%s,file=%s,object=%s]",mode,type,file,object);
    }

    /**
     * Constructs an index entry.
     *
     * @param mode the mode
     * @param type the type
     * @param object the associated object
     * @param file the associated file
     */
    public IndexEntry(String mode, String type, String object, String file) {
        this.mode = mode;
        this.type = type;
        this.file = file;
        this.object = object;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.mode != null ? this.mode.hashCode() : 0);
        hash = 89 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 89 * hash + (this.object != null ? this.object.hashCode() : 0);
        hash = 89 * hash + (this.file != null ? this.file.hashCode() : 0);
        return hash;
    }

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
        final IndexEntry other = (IndexEntry) obj;
        if ((this.mode == null) ? (other.mode != null) : !this.mode.equals(other.mode)) {
            return false;
        }
        if ((this.type == null) ? (other.type != null) : !this.type.equals(other.type)) {
            return false;
        }
        if ((this.object == null) ? (other.object != null) : !this.object.equals(other.object)) {
            return false;
        }
        if ((this.file == null) ? (other.file != null) : !this.file.equals(other.file)) {
            return false;
        }
        return true;
    }

    /**
     * Populates an {@link IndexEntry} from the current node that {@link SubmoduleWalk} is pointing to.
     *
     * @param walk a {@link SubmoduleWalk} object.
     */
    public IndexEntry(SubmoduleWalk walk) {
        this("160000","commit",walk.getObjectId().name(),walk.getPath());
    }

    private static final long serialVersionUID = 1L;
}

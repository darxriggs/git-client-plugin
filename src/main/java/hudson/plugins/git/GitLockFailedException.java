package hudson.plugins.git;

/**
 * Records failure to lock a git repository.
 *
 * Lock failures are a special case and may indicate that a retry attempt might succeed.
 */
public class GitLockFailedException extends GitException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a git lock failed exception.
     */
    public GitLockFailedException() {
        super();
    }

    /**
     * Constructs a git lock failed exception.
     *
     * @param message {@link String} description to associate with this exception
     */
    public GitLockFailedException(String message) {
        super(message);
    }

    /**
     * Constructs a git lock failed exception.
     *
     * @param cause {@link Throwable} which caused this exception
     */
    public GitLockFailedException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a git lock failed exception.
     *
     * @param message {@link String} description to associate with this exception
     * @param cause {@link Throwable} which caused this exception
     */
    public GitLockFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}

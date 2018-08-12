package hudson.plugins.git;

/**
 * Records exception information related to git operations.
 *
 * This exception is used to encapsulate command line git errors,
 * JGit errors, and other errors related to git operations.
 */
public class GitException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a git exception.
     */
    public GitException() {
        super();
    }

    /**
     * Constructs a git exception.
     *
     * @param message {@link String} description to associate with this exception
     */
    public GitException(String message) {
        super(message);
    }

    /**
     * Constructs a git exception.
     *
     * @param cause {@link Throwable} which caused this exception
     */
    public GitException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a git exception.
     *
     * @param message {@link String} description to associate with this exception
     * @param cause {@link Throwable} which caused this exception
     */
    public GitException(String message, Throwable cause) {
        super(message, cause);
    }
}

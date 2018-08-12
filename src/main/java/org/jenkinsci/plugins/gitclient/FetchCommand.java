package org.jenkinsci.plugins.gitclient;

import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.URIish;

import java.util.List;

/**
 * Command to fetch from a repository.
 *
 * @author <a href="mailto:nicolas.deloof@gmail.com">Nicolas De Loof</a>
 */
public interface FetchCommand extends GitCommand {

    /**
     * Specifies what to fetch.
     *
     * @param remote a {@link org.eclipse.jgit.transport.URIish} object.
     * @param refspecs a {@link java.util.List} object.
     * @return a {@link FetchCommand} object.
     */
    FetchCommand from(URIish remote, List<RefSpec> refspecs);

    /**
     * Prune stale remote references.
     *
     * @return a {@link FetchCommand} object.
     * @deprecated favour {@link #prune(boolean)}
     */
    @Deprecated
    FetchCommand prune();

    /**
     * Prune stale remote references.
     *
     * @param prune whether fetch should prune
     * @return a {@link FetchCommand} object.
     * @since 2.5.0
     */
    FetchCommand prune(boolean prune);

    /**
     * Only fetch the most recent history, not preceding history.
     * <p>
     * Depth of the shallow fetch is controlled by the {@link #depth} method.
     *
     * @param shallow whether the fetch is shallow
     * @return a {@link FetchCommand} object.
     */
    FetchCommand shallow(boolean shallow);

    /**
     * Timeout after which the command should be terminated.
     *
     * @param timeout timeout in minutes
     * @return a {@link FetchCommand} object.
     */
    FetchCommand timeout(Integer timeout);

    /**
     * Request that tags and their references are not fetched.
     * <p>
     * Default is to fetch tags.
     *
     * @param tags whether tags are fetched
     * @return a {@link FetchCommand} object.
     */
    FetchCommand tags(boolean tags);

    /**
     * When shallow cloning, allow for a depth to be set in cases where you need more than the immediate last commit.
     * <p>
     * Has no effect if {@link #shallow} is set to {@code false} (default).
     *
     * @param depth number of revisions to be included in shallow clone
     * @return a {@link FetchCommand} object.
     */
    FetchCommand depth(Integer depth);
}

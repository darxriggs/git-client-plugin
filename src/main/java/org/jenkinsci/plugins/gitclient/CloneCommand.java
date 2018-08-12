package org.jenkinsci.plugins.gitclient;

import java.util.List;

import org.eclipse.jgit.transport.RefSpec;

/**
 * Command to clone a repository.
 * <p>
 * This command behaves differently from CLI clone command, it never actually checks out
 * into the workspace.
 *
 * @author Kohsuke Kawaguchi
 */
public interface CloneCommand extends GitCommand {

    /**
     * URL of the repository to be cloned.
     *
     * @param url a {@link String} object.
     * @return a {@link CloneCommand} object.
     */
    CloneCommand url(String url);

    /**
     * Name of the remote, such as 'origin' (which is the default).
     *
     * @param name a {@link String} object.
     * @return a {@link CloneCommand} object.
     */
    CloneCommand repositoryName(String name);

    /**
     * Only clone the most recent history, not preceding history.
     * <p>
     * Depth of the shallow clone is controlled by the {@link #depth} method.
     *
     * @return a {@link CloneCommand} object.
     * @deprecated favour {@link #shallow(boolean)}
     */
    @Deprecated
    CloneCommand shallow();

    /**
     * Only clone the most recent history, not preceding history.
     * <p>
     * Depth of the shallow clone is controlled by the {@link #depth} method.
     *
     * @param shallow whether the clone is shallow or not
     * @return a {@link CloneCommand} object.
     * @since 2.5.0
     */
    CloneCommand shallow(boolean shallow);

    /**
     * When the repository to clone is on the local machine, instead of using hard links, automatically setup
     * {@code .git/objects/info/alternates} to share the objects with the source repository.
     *
     * @return a {@link CloneCommand} object.
     * @deprecated favour {@link #shared(boolean)}
     */
    @Deprecated
    CloneCommand shared();

    /**
     * When the repository to clone is on the local machine, instead of using hard links, automatically setup
     * {@code .git/objects/info/alternates} to share the objects with the source repository.
     *
     * @param shared whether the clone is shared or not
     * @return a {@link CloneCommand} object.
     * @since 2.5.0
     */
    CloneCommand shared(boolean shared);

    /**
     * Use a reference repository.
     *
     * @param reference path of the reference repository
     * @return a {@link CloneCommand} object.
     */
    CloneCommand reference(String reference);

    /**
     * Timeout after which the command should be terminated.
     *
     * @param timeout timeout in minutes
     * @return a {@link CloneCommand} object.
     */
    CloneCommand timeout(Integer timeout);

    /**
     * When just cloning repository without populating the workspace is required (for instance when sparse checkouts are used).
     * <p>
     * This parameter does not do anything, a checkout will never be performed.
     *
     * @return a {@link CloneCommand} object.
     */
    @Deprecated
    CloneCommand noCheckout();

    /**
     * Request that tags and their references are not fetched.
     * <p>
     * Default is to fetch tags.
     *
     * @param tags whether tags are fetched or not
     * @return a {@link CloneCommand} object.
     */
    CloneCommand tags(boolean tags);

    /**
     * List of refspecs to be fetched.
     *
     * @param refspecs refspecs defining the references to be fetched
     * @return a {@link CloneCommand} object.
     */
    CloneCommand refspecs(List<RefSpec> refspecs);

    /**
     * When shallow cloning, allow for a depth to be set in cases where you need more than the immediate last commit.
     * <p>
     * Has no effect if {@link #shallow} is set to {@code false} (default).
     *
     * @param depth number of revisions to be included in shallow clone
     * @return a {@link CloneCommand} object.
     */
    CloneCommand depth(Integer depth);
}

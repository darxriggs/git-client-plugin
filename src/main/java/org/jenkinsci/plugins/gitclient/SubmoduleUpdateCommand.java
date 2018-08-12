package org.jenkinsci.plugins.gitclient;

/**
 * Command to update submodules.
 */
public interface SubmoduleUpdateCommand extends GitCommand {

    /**
     * Sets whether to update submodules recursively.
     * <p>
     * Default is non-recursive.
     *
     * @param recursive whether to update submodules recursively (requires git&gt;=1.6.5)
     * @return a {@link SubmoduleUpdateCommand} object.
     */
    SubmoduleUpdateCommand recursive(boolean recursive);

    /**
     * Sets whether to update submodules to the tip of the branch rather than to a specific SHA1.
     * <p>
     * Refer to git documentation for details.  First available
     * in command line git 1.8.2.  Default is to update to a specific
     * SHA1 (compatible with previous versions of git).
     *
     * @param remoteTracking if true, will update the submodule to the tip of the branch requested (requires git&gt;=1.8.2)
     * @return a {@link SubmoduleUpdateCommand} object.
     */
    SubmoduleUpdateCommand remoteTracking(boolean remoteTracking);

    /**
     * Sets whether to use the parent repository credentials.
     * <p>
     * Either the credentials of the parent project or
     * the credentials associated with the submodule URL can be used.
     *
     * @param parentCredentials whether to use the parent repository credentials
     * @return a {@link SubmoduleUpdateCommand} object.
     */
    SubmoduleUpdateCommand parentCredentials(boolean parentCredentials);

    /**
     * Use a reference repository.
     *
     * @param ref path of the reference repository
     * @return a {@link SubmoduleUpdateCommand} object.
     */
    SubmoduleUpdateCommand ref(String ref);

    /**
     * Use a specific branch for a submodule.
     * <p>
     * This method can be invoked multiple times.
     *
     * @param submodule submodule name
     * @param branchname branch name
     * @return a {@link SubmoduleUpdateCommand} object.
     */
    SubmoduleUpdateCommand useBranch(String submodule, String branchname);

    /**
     * Timeout after which the command should be terminated.
     *
     * @param timeout timeout in minutes
     * @return a {@link SubmoduleUpdateCommand} object.
     */
    SubmoduleUpdateCommand timeout(Integer timeout);

    /**
     * Only clone the most recent history, not preceding history.
     * <p>
     * Depth of the shallow clone is controlled by the {@link #depth} method.
     *
     * @param shallow whether the clone is shallow (requires git&gt;=1.8.4)
     * @return a {@link SubmoduleUpdateCommand} object.
     */
    SubmoduleUpdateCommand shallow(boolean shallow);

    /**
     * When shallow cloning, allow for a depth to be set in cases where you need more than the immediate last commit.
     * <p>
     * Has no effect if {@link #shallow} is set to {@code false} (default).
     *
     * @param depth number of revisions to be included in shallow clone (requires git&gt;=1.8.4)
     * @return a {@link SubmoduleUpdateCommand} object.
     */
    SubmoduleUpdateCommand depth(Integer depth);

    /**
     * Update submodules in parallel with the given number of threads. Note that this parallelism only applies to the
     * top-level submodules of a repository. Any submodules of those submodules will be updated serially.
     *
     * @param threads number of threads to use for updating submodules in parallel
     * @return a {@link SubmoduleUpdateCommand} object.
     */
    SubmoduleUpdateCommand threads(int threads);
}

package hudson.plugins.git;

import hudson.model.TaskListener;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.transport.RemoteConfig;
import org.jenkinsci.plugins.gitclient.GitClient;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * IGitAPI interface.
 *
 * @deprecated methods here are deprecated until proven useful by a plugin
 */
public interface IGitAPI extends GitClient {

    /**
     * Returns whether this repository has submodules.
     *
     * @param treeIsh an ignored argument, kept for compatibility
     * @return true if this repository has submodules (git modules file)
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     * @see GitClient#hasGitModules
     */
    boolean hasGitModules( String treeIsh ) throws GitException, InterruptedException;

    /**
     * Returns URL of remote name in repository GIT_DIR.
     *
     * @param name name for the remote repository, for example, "origin"
     * @param GIT_DIR directory containing git repository
     * @return URL of remote "name" in repository GIT_DIR.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    String getRemoteUrl(String name, String GIT_DIR) throws GitException, InterruptedException;

    /**
     * Sets remote repository name and URL.
     *
     * @param name name for the remote repository, for example, "origin"
     * @param url URL for the remote repository, for example git://github.com/jenkinsci/git-client-plugin.git
     * @param GIT_DIR directory containing git repository
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void setRemoteUrl(String name, String url, String GIT_DIR) throws GitException, InterruptedException;

    /**
     * Returns name of default remote.
     *
     * @param _default_ value to return if no remote is defined in this repository
     * @return name of default remote
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    String getDefaultRemote( String _default_ ) throws GitException, InterruptedException;

    /**
     * Returns whether this repository is bare.
     *
     * @return true if this repository is bare
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    boolean isBareRepository() throws GitException, InterruptedException;

    /**
     * Returns whether a repository at the given path is bare.
     *
     * @param GIT_DIR path to the repository (must be to .git dir).
     * @return true if this repository is bare
     * @throws GitException on failure
     * @throws InterruptedException if interrupted
     */
    boolean isBareRepository(String GIT_DIR) throws GitException, InterruptedException;

    /**
     * Synchronizes submodules' remote URL configuration setting to
     * the value specified in .gitmodules. Refer to git submodule sync
     * documentation for more details.
     *
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void submoduleSync() throws GitException, InterruptedException;

    /**
     * Returns URL of the named submodule.
     *
     * @param name submodule name whose URL will be returned
     * @return URL of the named submodule
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    String getSubmoduleUrl(String name) throws GitException, InterruptedException;

    /**
     * Sets URL of the named submodule.
     *
     * @param name submodule name whose URL will be set
     * @param url URL for the named submodule
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void setSubmoduleUrl(String name, String url) throws GitException, InterruptedException;

    /**
     * Fixes submodule URLs.
     *
     * @param remote a {@link String} object.
     * @param listener a {@link TaskListener} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void fixSubmoduleUrls( String remote, TaskListener listener ) throws GitException, InterruptedException;

    void setupSubmoduleUrls( String remote, TaskListener listener ) throws GitException, InterruptedException;

    /**
     * Retrieves commits based on refspec from repository.
     *
     * @param repository URL of the repository to be retrieved
     * @param refspec definition of mapping from remote refs to local refs
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    public void fetch(String repository, String refspec) throws GitException, InterruptedException;

    /**
     * Retrieves commits from remote repository.
     *
     * @param remoteRepository remote repository from which refs will be retrieved
     * @throws InterruptedException if interrupted.
     */
    void fetch(RemoteConfig remoteRepository) throws InterruptedException;

    /**
     * Retrieves commits from default remote.
     *
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void fetch() throws GitException, InterruptedException;

    /**
     * Resets the contents of the working directory of this
     * repository. Refer to git reset documentation.
     *
     * @param hard reset as though "--hard" were passed to "git reset"
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void reset(boolean hard) throws GitException, InterruptedException;

    /**
     * Resets the contents of the working directory of this
     * repository. Refer to git reset documentation.
     *
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void reset() throws GitException, InterruptedException;

    /**
     * Pushes commits to repository.
     *
     * @param repository git repository to receive commits
     * @param revspec commits to be pushed
     * @throws GitException if underlying git operating fails
     * @throws InterruptedException if interrupted
     */
    void push(RemoteConfig repository, String revspec) throws GitException, InterruptedException;

    /**
     * Merges commits from refspec into the current branch.
     *
     * @param revSpec the revision specification to be merged (for example, origin/master)
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void merge(String revSpec) throws GitException, InterruptedException;

    /**
     * Clones repository from source to this repository.
     *
     * @param source remote repository to be cloned
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void clone(RemoteConfig source) throws GitException, InterruptedException;

    /**
     * Clones repository from {@link RemoteConfig} rc to this repository.
     *
     * @param rc the remote config for the remote repository
     * @param useShallowClone if true, use a shallow clone
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void clone(RemoteConfig rc, boolean useShallowClone) throws GitException, InterruptedException;

    /**
     * Finds all the branches that include the given commit.
     *
     * @param revspec substring to be searched for branch name
     * @return list of branches containing refspec
     * @throws GitException on failure
     * @throws InterruptedException if interrupted
     * @deprecated Use {@link GitClient#getBranchesContaining(String, boolean)}
     *             instead. This method does work only with local branches on
     *             one implementation and with all the branches in the other.
     */
    List<Branch> getBranchesContaining(String revspec) throws GitException, InterruptedException;

    /**
     * This method has been implemented as non-recursive historically, but
     * often that is not what the caller wants.
     *
     * @param treeIsh string representation of a treeIsh item
     * @return list of IndexEntry items starting at treeIsh
     * @throws GitException on failure
     * @throws InterruptedException if interrupted
     * @deprecated Use {@link #lsTree(String, boolean)} to be explicit about the recursion behaviour.
     */
    List<IndexEntry> lsTree(String treeIsh) throws GitException, InterruptedException;

    /**
     * lsTree.
     *
     * @param treeIsh a {@link String} object.
     * @param recursive a boolean.
     * @return a {@link List} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    List<IndexEntry> lsTree(String treeIsh, boolean recursive) throws GitException, InterruptedException;

    /**
     * revListBranch.
     *
     * @param branchId a {@link String} object.
     * @return a {@link List} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    List<ObjectId> revListBranch(String branchId) throws GitException, InterruptedException;

    /**
     * getTagsOnCommit.
     *
     * @param revName a {@link String} object.
     * @return a {@link List} object.
     * @throws GitException if underlying git operation fails.
     * @throws IOException if any IO failure
     * @throws InterruptedException if interrupted.
     */
    List<Tag> getTagsOnCommit(String revName) throws GitException, IOException, InterruptedException;

    /** {@inheritDoc} */
    void changelog(String revFrom, String revTo, OutputStream fos) throws GitException, InterruptedException;

    /** {@inheritDoc} */
    void checkoutBranch(String branch, String commitish) throws GitException, InterruptedException;

    /**
     * mergeBase.
     *
     * @param sha1 a {@link ObjectId} object.
     * @param sha2 a {@link ObjectId} object.
     * @return a {@link ObjectId} object.
     * @throws InterruptedException if interrupted.
     */
    ObjectId mergeBase(ObjectId sha1, ObjectId sha2) throws InterruptedException;

    /**
     * showRevision.
     *
     * @param r a {@link Revision} object.
     * @return a {@link List} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    List<String> showRevision(Revision r) throws GitException, InterruptedException;

    /**
     * This method makes no sense, in that it lists all log entries across all refs and yet it
     * takes a meaningless 'branch' parameter. Please do not use this.
     *
     * @param branch a {@link String} object.
     * @return a {@link String} object.
     * @throws InterruptedException if interrupted.
     * @deprecated
     */
    @Restricted(NoExternalUse.class)
    String getAllLogEntries(String branch) throws InterruptedException;
}

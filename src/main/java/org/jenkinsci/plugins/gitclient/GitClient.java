package org.jenkinsci.plugins.gitclient;

import com.cloudbees.jenkins.plugins.sshcredentials.SSHUserPrivateKey;
import com.cloudbees.plugins.credentials.CredentialsMatcher;
import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.common.StandardCredentials;
import com.cloudbees.plugins.credentials.common.StandardUsernameCredentials;
import com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials;
import hudson.FilePath;
import hudson.ProxyConfiguration;
import hudson.model.TaskListener;
import hudson.plugins.git.*;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.URIish;

import javax.annotation.CheckForNull;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface to Git functionality.
 * <p>
 * Since 1.1, this interface is remotable, meaning it can be referenced from a remote closure call.
 *
 * @author <a href="mailto:nicolas.deloof@gmail.com">Nicolas De Loof</a>
 */
public interface GitClient {

    /** Constant {@code verbose=Boolean.getBoolean(IGitAPI.class.getName() + ".verbose")} */
    boolean verbose = Boolean.getBoolean(IGitAPI.class.getName() + ".verbose");

    // If true, do not print the list of remote branches.
    /** Constant {@code quietRemoteBranches=Boolean.getBoolean(GitClient.class.getName() + ".quietRemoteBranches")} */
    boolean quietRemoteBranches = Boolean.getBoolean(GitClient.class.getName() + ".quietRemoteBranches");

    /**
     * The supported credential types.
     *
     * @since 1.2.0
     */
    CredentialsMatcher CREDENTIALS_MATCHER = CredentialsMatchers.anyOf(
            CredentialsMatchers.instanceOf(StandardUsernamePasswordCredentials.class),
            CredentialsMatchers.instanceOf(SSHUserPrivateKey.class)
    );

    /**
     * Removes all credentials from the client.
     *
     * @since 1.2.0
     */
    void clearCredentials();

    /**
     * Adds credentials to be used against a specific url.
     *
     * @param url the url for the credentials to be used against
     * @param credentials the credentials to use
     * @since 1.2.0
     */
    void addCredentials(String url, StandardCredentials credentials);

    /**
     * Adds credentials to be used when there are no url specific credentials defined.
     *
     * @param credentials the credentials to use
     * @see #addCredentials(String, StandardCredentials)
     * @since 1.2.0
     */
    void addDefaultCredentials(StandardCredentials credentials);

    /**
     * Sets the identity of the author for future commits and merge operations.
     *
     * @param name the author's name
     * @param email the author's email
     * @throws GitException if underlying git operation fails.
     */
    void setAuthor(String name, String email) throws GitException;

    /**
     * Sets the identity of the author for future commits and merge operations.
     *
     * @param p the author's identity
     * @throws GitException if underlying git operation fails.
     */
    void setAuthor(PersonIdent p) throws GitException;

    /**
     * Sets the identity of the committer for future commits and merge operations.
     *
     * @param name the committer's name
     * @param email the committer's email
     * @throws GitException if underlying git operation fails.
     */
    void setCommitter(String name, String email) throws GitException;

    /**
     * Sets the identity of the committer for future commits and merge operations.
     *
     * @param p the committer's identity
     * @throws GitException if underlying git operation fails.
     */
    void setCommitter(PersonIdent p) throws GitException;

    /**
     * Exposes the JGit repository this GitClient is using.
     * <p>
     * Don't forget to call {@link Repository#close()}, to avoid JENKINS-12188.
     *
     * @return a {@link Repository} object.
     * @throws GitException if underlying git operation fails.
     * @deprecated as of 1.1
     *      This method was deprecated to make {@link GitClient} remotable. When called on
     *      a proxy object, this method throws {@link java.io.NotSerializableException}.
     *      Use {@link #withRepository(RepositoryCallback)} to pass in the closure instead.
     *      This prevents the repository leak (JENKINS-12188), too.
     */
    Repository getRepository() throws GitException;

    /**
     * Runs the computation that requires local access to {@link Repository}.
     *
     * @param callable the repository callback used as closure to instance
     * @param <T> type for the repository callback
     * @return a T object.
     * @throws IOException in case of IO error
     * @throws InterruptedException if interrupted
     */
    <T> T withRepository(RepositoryCallback<T> callable) throws IOException, InterruptedException;

    /**
     * Returns the working tree of this repository.
     *
     * @return a {@link FilePath} object.
     */
    FilePath getWorkTree();

    /**
     * Initializes an empty repository for further git operations.
     *
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    public void init() throws GitException, InterruptedException;

    /**
     * Adds the files to the index that match the given pattern.
     *
     * @param filePattern a {@link String} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void add(String filePattern) throws GitException, InterruptedException;

    /**
     * Performs a commit.
     *
     * @param message the commit message
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void commit(String message) throws GitException, InterruptedException;

    /**
     * Performs a commit.
     *
     * @param message the commit message
     * @param author the author identity
     * @param committer the committer identity
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     * @deprecated as of 1.1
     *      Use {@link #setAuthor(String, String)} and {@link #setCommitter(String, String)}
     *      then call {@link #commit(String)}
     */
    void commit(String message, PersonIdent author, PersonIdent committer) throws GitException, InterruptedException;

    /**
     * Returns if this workspace has a git repository.
     *
     * @return true if this workspace has a git repository
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    boolean hasGitRepo() throws GitException, InterruptedException;

    /**
     * Returns if a commit is contained in the repository.
     *
     * @param commit a {@link ObjectId} object.
     * @return true if commit is in repository
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    boolean isCommitInRepo(ObjectId commit) throws GitException, InterruptedException;

    /**
     * Gets a remote's URL.
     *
     * @param name the name of the remote (e.g. "origin")
     * @return the remote's URL
     * @throws GitException if executing the git command fails
     * @throws InterruptedException if interrupted.
     */
    String getRemoteUrl(String name) throws GitException, InterruptedException;

    /**
     * Sets a remote's URL.
     *
     * @param name the name of the remote (e.g. "origin")
     * @param url the new value of the remote's URL
     * @throws GitException if executing the git command fails
     * @throws InterruptedException if interrupted.
     */
    void setRemoteUrl(String name, String url) throws GitException, InterruptedException;

    /**
     * Adds a remote.
     *
     * @param name the name of the remote (e.g. "origin")
     * @param url the remote's URL
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void addRemoteUrl(String name, String url) throws GitException, InterruptedException;

    /**
     * Checks out the specified commit/tag/branch into the workspace.
     * (equivalent of <code>git checkout <em>branch</em></code>.)
     *
     * @param ref a git object reference expression (either a sha1, tag or branch)
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     * @deprecated use {@link #checkout()} and {@link CheckoutCommand}
     */
    void checkout(String ref) throws GitException, InterruptedException;

    /**
     * Creates a new branch that points to the specified reference.
     * (equivalent to git checkout -b <em>branch</em> <em>commit</em>)
     * <p>
     * This will fail if the branch already exists.
     *
     * @param ref a git object reference expression. For backward compatibility, {@code null} will checkout current HEAD.
     * @param branch the name of the branch to create from reference
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     * @deprecated use {@link #checkout()} and {@link CheckoutCommand}
     */
    void checkout(String ref, String branch) throws GitException, InterruptedException;

    /**
     * Returns a {@link CheckoutCommand} to build up the git-checkout invocation.
     *
     * @return a {@link CheckoutCommand} object.
     */
    CheckoutCommand checkout();

    /**
     * Regardless of the current state of the workspace (whether there is some dirty files, etc)
     * and the state of the repository (whether the branch of the specified name exists or not),
     * when this method exits the following conditions hold:
     * <ul>
     *     <li>The branch of the specified name <em>branch</em> exists and points to the specified <em>ref</em>
     *     <li>{@code HEAD} points to <em>branch</em>. In other words, the workspace is on the specified branch.
     *     <li>Both index and workspace are the same tree with <em>ref</em>.
     *         (no dirty files and no staged changes, although this method will not touch untracked files
     *         in the workspace.)
     * </ul>
     * <p>
     * This method is preferred over the {@link #checkout(String, String)} family of methods, as
     * this method is affected far less by the current state of the repository. The {@code checkout}
     * methods, in their attempt to emulate the "git checkout" command line behaviour, have too many
     * side effects. In Jenkins, where you care a lot less about throwing away local changes and
     * care a lot more about resetting the workspace into a known state, methods like this is more useful.
     * <p>
     * For compatibility reasons, the order of the parameter is different from {@link #checkout(String, String)}.
     *
     * @since 1.0.6
     * @param branch a {@link String} object.
     * @param ref a {@link String} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void checkoutBranch(@CheckForNull String branch, String ref) throws GitException, InterruptedException;

    /**
     * Clones a remote repository.
     *
     * @param url URL for remote repository to clone
     * @param origin upstream track name, defaults to {@code origin} by convention
     * @param useShallowClone option to create a shallow clone, that has some restriction but will make clone operation
     * @param reference (optional) reference to a local clone for faster clone operations (reduce network and local storage costs)
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void clone(String url, String origin, boolean useShallowClone, String reference) throws GitException, InterruptedException;

    /**
     * Returns a {@link org.jenkinsci.plugins.gitclient.CloneCommand} to build up the git-clone invocation.
     *
     * @return a {@link org.jenkinsci.plugins.gitclient.CloneCommand} object.
     */
    CloneCommand clone_(); // can't use 'clone' as it collides with Object.clone()

    /**
     * Fetches commits from url which match any of the passed in refspecs.
     * Assumes {@code remote.remoteName.url} has been set.
     *
     * @param url a {@link org.eclipse.jgit.transport.URIish} object.
     * @param refspecs a {@link List} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     * @deprecated use {@link #fetch_()} and configure a {@link FetchCommand}
     */
    void fetch(URIish url, List<RefSpec> refspecs) throws GitException, InterruptedException;

    /**
     * Fetches commits from url which match any of the passed in refspecs.
     * Assumes {@code remote.remoteName.url} has been set.
     *
     * @param remoteName a {@link String} object.
     * @param refspec a {@link org.eclipse.jgit.transport.RefSpec} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     * @deprecated use {@link #fetch_()} and configure a {@link FetchCommand}
     */
    void fetch(String remoteName, RefSpec... refspec) throws GitException, InterruptedException;

    /**
     * Fetches commits from url which match the passed in refspec.
     * Assumes {@code remote.remoteName.url} has been set.
     *
     * @param remoteName a {@link String} object.
     * @param refspec a {@link org.eclipse.jgit.transport.RefSpec} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     * @deprecated use {@link #fetch_()} and configure a {@link FetchCommand}
     */
    void fetch(String remoteName, RefSpec refspec) throws GitException, InterruptedException;

    /**
     * fetch_.
     *
     * @return a {@link FetchCommand} object.
     */
    FetchCommand fetch_(); // can't use 'fetch' as legacy IGitAPI already define this method

    /**
     * push.
     *
     * @param remoteName a {@link String} object.
     * @param refspec a {@link String} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     * @deprecated use {@link #push()} and configure a {@link org.jenkinsci.plugins.gitclient.PushCommand}
     */
    void push(String remoteName, String refspec) throws GitException, InterruptedException;

    /**
     * push.
     *
     * @param url a {@link org.eclipse.jgit.transport.URIish} object.
     * @param refspec a {@link String} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     * @deprecated use {@link #push()} and configure a {@link org.jenkinsci.plugins.gitclient.PushCommand}
     */
    void push(URIish url, String refspec) throws GitException, InterruptedException;

    /**
     * push.
     *
     * @return a {@link org.jenkinsci.plugins.gitclient.PushCommand} object.
     */
    PushCommand push();


    /**
     * merge.
     *
     * @param rev a {@link ObjectId} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     * @deprecated use {@link #merge()} and configure a {@link org.jenkinsci.plugins.gitclient.MergeCommand}
     */
    void merge(ObjectId rev) throws GitException, InterruptedException;

    /**
     * merge.
     *
     * @return a {@link org.jenkinsci.plugins.gitclient.MergeCommand} object.
     */
    MergeCommand merge();

    /**
     * rebase.
     *
     * @return a {@link org.jenkinsci.plugins.gitclient.RebaseCommand} object.
     */
    RebaseCommand rebase();

    /**
     * init_.
     *
     * @return a {@link org.jenkinsci.plugins.gitclient.InitCommand} object.
     */
    InitCommand init_(); // can't use 'init' as legacy IGitAPI already define this method

    /**
     * Prune stale remote tracking branches with "git remote prune" on the specified remote.
     *
     * @param repository a {@link org.eclipse.jgit.transport.RemoteConfig} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void prune(RemoteConfig repository) throws GitException, InterruptedException;

    /**
     * Remove untracked files and directories, including files listed
     * in the ignore rules.
     *
     * Fully revert working copy to a clean state, i.e. run both
     * <a href="https://www.kernel.org/pub/software/scm/git/docs/git-reset.html">git-reset(1) --hard</a> then
     * <a href="https://www.kernel.org/pub/software/scm/git/docs/git-clean.html">git-clean(1)</a> for working copy to
     * match a fresh clone.
     *
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void clean() throws GitException, InterruptedException;

    /**
     * Remove untracked files and directories, including files listed
     * in the ignore rules.
     *
     * Fully revert working copy to a clean state, i.e. run both
     * <a href="https://www.kernel.org/pub/software/scm/git/docs/git-reset.html">git-reset(1) --hard</a> then
     * <a href="https://www.kernel.org/pub/software/scm/git/docs/git-clean.html">git-clean(1)</a> for working copy to
     * match a fresh clone.
     *
     * @param cleanSubmodule flag to add extra -f
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void clean(boolean cleanSubmodule) throws GitException, InterruptedException;



    // --- manage branches

    /**
     * branch.
     *
     * @param name a {@link String} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void branch(String name) throws GitException, InterruptedException;

    /**
     * (force) delete a branch.
     *
     * @param name a {@link String} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void deleteBranch(String name) throws GitException, InterruptedException;

    /**
     * Returns the set of branches defined in this repository,
     * including local branches and remote branches. Remote branches
     * are prefixed by "remotes/".
     *
     * @return a {@link java.util.Set} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    Set<Branch> getBranches() throws GitException, InterruptedException;

    /**
     * Returns the remote branches defined in this repository.
     *
     * @return {@link java.util.Set} of remote branches in this repository
     * @throws GitException if underlying git operation fails
     * @throws InterruptedException if interrupted
     */
    Set<Branch> getRemoteBranches() throws GitException, InterruptedException;


    // --- manage tags

    /**
     * Create (or update) a tag. If tag already exist it gets updated (equivalent to {@code git tag --force})
     *
     * @param tagName a {@link String} object.
     * @param comment a {@link String} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void tag(String tagName, String comment) throws GitException, InterruptedException;

    /**
     * tagExists.
     *
     * @param tagName a {@link String} object.
     * @return true if tag exists in repository
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    boolean tagExists(String tagName) throws GitException, InterruptedException;

    /**
     * getTagMessage.
     *
     * @param tagName a {@link String} object.
     * @return a {@link String} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    String getTagMessage(String tagName) throws GitException, InterruptedException;

    /**
     * deleteTag.
     *
     * @param tagName a {@link String} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void deleteTag(String tagName) throws GitException, InterruptedException;

    /**
     * getTagNames.
     *
     * @param tagPattern a {@link String} object.
     * @return a {@link java.util.Set} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    Set<String> getTagNames(String tagPattern) throws GitException, InterruptedException;
    /**
     * getRemoteTagNames.
     *
     * @param tagPattern a {@link String} object.
     * @return a {@link java.util.Set} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    Set<String> getRemoteTagNames(String tagPattern) throws GitException, InterruptedException;


    // --- manage refs

    /**
     * Create (or update) a ref. The ref will reference HEAD (equivalent to {@code git update-ref ... HEAD}).
     *
     * @param refName the full name of the ref (e.g. "refs/myref"). Spaces will be replaced with underscores.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void ref(String refName) throws GitException, InterruptedException;

    /**
     * Check if a ref exists. Equivalent to comparing the return code of {@code git show-ref} to zero.
     *
     * @param refName the full name of the ref (e.g. "refs/myref"). Spaces will be replaced with underscores.
     * @return True if the ref exists, false otherwse.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    boolean refExists(String refName) throws GitException, InterruptedException;

    /**
     * Deletes a ref. Has no effect if the ref does not exist, equivalent to {@code git update-ref -d}.
     *
     * @param refName the full name of the ref (e.g. "refs/myref"). Spaces will be replaced with underscores.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void deleteRef(String refName) throws GitException, InterruptedException;

    /**
     * List refs with the given prefix. Equivalent to {@code git for-each-ref --format="%(refname)"}.
     *
     * @param refPrefix the literal prefix any ref returned will have. The empty string implies all.
     * @return a set of refs, each beginning with the given prefix. Empty if none.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    Set<String> getRefNames(String refPrefix) throws GitException, InterruptedException;

    // --- lookup revision

    /**
     * getHeadRev.
     *
     * @param url a {@link String} object.
     * @return a {@link java.util.Map} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    Map<String, ObjectId> getHeadRev(String url) throws GitException, InterruptedException;

    /**
     * getHeadRev.
     *
     * @param remoteRepoUrl a {@link String} object.
     * @param branch a {@link String} object.
     * @return a {@link ObjectId} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    ObjectId getHeadRev(String remoteRepoUrl, String branch) throws GitException, InterruptedException;

    /**
     * List references in a remote repository. Equivalent to {@code git ls-remote [--heads] [--tags] <repository> [<refs>]}.
     *
     * @param remoteRepoUrl
     *      Remote repository URL.
     * @param pattern
     *      Only references matching the given pattern are displayed.
     * @param headsOnly
     *      Limit to only refs/heads.
     * @param tagsOnly
     *      Limit to only refs/tags.
     *      headsOnly and tagsOnly are not mutually exclusive;
     *      when both are true, references stored in refs/heads and refs/tags are displayed.
     * @return a map of reference names and their commit hashes. Empty if none.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    Map<String, ObjectId> getRemoteReferences(String remoteRepoUrl, String pattern, boolean headsOnly, boolean tagsOnly) throws GitException, InterruptedException;

    /**
     * List symbolic references in a remote repository. Equivalent to {@code git ls-remote --symref <repository>
     * [<refs>]}. Note: the response may be empty for multiple reasons
     *
     * @param remoteRepoUrl Remote repository URL.
     * @param pattern       Only references matching the given pattern are displayed.
     * @return a map of reference names and their underlying references. Empty if none or if the remote does not report
     * symbolic references (i.e. Git 1.8.4 or earlier) or if the client does not support reporting symbolic references
     * (e.g. command line Git prior to 2.8.0).
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException  if interrupted.
     */
    Map<String, String> getRemoteSymbolicReferences(String remoteRepoUrl, String pattern) throws GitException, InterruptedException;

    /**
     * Retrieve commit object that is direct child for {@code revName} revision reference.
     *
     * @param revName a commit sha1 or tag/branch refname
     * @return a {@link ObjectId} object.
     * @throws GitException when no such commit / revName is found in repository.
     * @throws InterruptedException if interrupted.
     */
    ObjectId revParse(String revName) throws GitException, InterruptedException;

    /**
     * revList_.
     *
     * @return a {@link RevListCommand} object.
     */
    RevListCommand revList_();

    /**
     * revListAll.
     *
     * @return a {@link List} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    List<ObjectId> revListAll() throws GitException, InterruptedException;

    /**
     * revList.
     *
     * @param ref a {@link String} object.
     * @return a {@link List} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    List<ObjectId> revList(String ref) throws GitException, InterruptedException;


    // --- submodules

    /**
     * subGit.
     *
     * @param subdir a {@link String} object.
     * @return a IGitAPI implementation to manage git submodule repository
     */
    GitClient subGit(String subdir);

    /**
     * Returns true if the repository has Git submodules.
     *
     * @return true if this repository has submodules
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    boolean hasGitModules() throws GitException, InterruptedException;

    /**
     * Finds all the submodule references in this repository at the specified tree.
     *
     * @param treeIsh a {@link String} object.
     * @return never null.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    List<IndexEntry> getSubmodules( String treeIsh ) throws GitException, InterruptedException;

    /**
     * Creates a submodule in subdir child directory for remote repository.
     *
     * @param remoteURL a {@link String} object.
     * @param subdir a {@link String} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void addSubmodule(String remoteURL, String subdir) throws GitException, InterruptedException;

    /**
     * Run submodule update optionally recursively on all submodules
     * (equivalent of <code>git submodule update <em>--recursive</em></code>.)
     *
     * @param recursive a boolean.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     * @deprecated use {@link #submoduleUpdate()} and {@link SubmoduleUpdateCommand}
     */
    void submoduleUpdate(boolean recursive)  throws GitException, InterruptedException;

    /**
     * Run submodule update optionally recursively on all submodules, with a specific
     * reference passed to git clone if needing to --init.
     * (equivalent of <code>git submodule update <em>--recursive</em> <em>--reference 'reference'</em></code>.)
     *
     * @param recursive a boolean.
     * @param reference a {@link String} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     * @deprecated use {@link #submoduleUpdate()} and {@link SubmoduleUpdateCommand}
     */
    void submoduleUpdate(boolean recursive, String reference) throws GitException, InterruptedException;

    /**
     * Run submodule update optionally recursively on all submodules, optionally with remoteTracking submodules
     * (equivalent of <code>git submodule update <em>--recursive</em> <em>--remote</em></code>.)
     *
     * @param recursive a boolean.
     * @param remoteTracking a boolean.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     * @deprecated use {@link #submoduleUpdate()} and {@link SubmoduleUpdateCommand}
     */
    void submoduleUpdate(boolean recursive, boolean remoteTracking)  throws GitException, InterruptedException;
    /**
     * Run submodule update optionally recursively on all submodules, optionally with remoteTracking, with a specific
     * reference passed to git clone if needing to --init.
     * (equivalent of <code>git submodule update <em>--recursive</em> <em>--remote</em> <em>--reference 'reference'</em></code>.)
     *
     * @param recursive a boolean.
     * @param remoteTracking a boolean.
     * @param reference a {@link String} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     * @deprecated use {@link #submoduleUpdate()} and {@link SubmoduleUpdateCommand}
     */
    void submoduleUpdate(boolean recursive, boolean remoteTracking, String reference)  throws GitException, InterruptedException;

    /**
     * Updates submodules.
     *
     * @return a {@link SubmoduleUpdateCommand} object.
     */
    SubmoduleUpdateCommand submoduleUpdate();

    /**
     * Cleans submodules.
     *
     * @param recursive a boolean.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void submoduleClean(boolean recursive)  throws GitException, InterruptedException;

    /**
     * Initializes submodules.
     *
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void submoduleInit()  throws GitException, InterruptedException;

    /**
     * Sets up submodule URLs so that they correspond to the remote pertaining to
     * the revision that has been checked out.
     *
     * @param rev a {@link hudson.plugins.git.Revision} object.
     * @param listener a {@link hudson.model.TaskListener} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void setupSubmoduleUrls( Revision rev, TaskListener listener ) throws GitException, InterruptedException;


    // --- commit log and notes

    /**
     * changelog.
     *
     * @param revFrom a {@link String} object.
     * @param revTo a {@link String} object.
     * @param os a {@link java.io.OutputStream} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     * @deprecated use {@link #changelog(String, String, Writer)}
     */
    void changelog(String revFrom, String revTo, OutputStream os) throws GitException, InterruptedException;

    /**
     * Adds the changelog entries for commits in the range revFrom..revTo.
     *
     * This is just a short cut for calling {@link #changelog()} with appropriate parameters.
     *
     * @param revFrom a {@link String} object.
     * @param revTo a {@link String} object.
     * @param os a {@link java.io.Writer} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void changelog(String revFrom, String revTo, Writer os) throws GitException, InterruptedException;

    /**
     * Returns a {@link org.jenkinsci.plugins.gitclient.ChangelogCommand} to build up the git-log invocation.
     *
     * @return a {@link org.jenkinsci.plugins.gitclient.ChangelogCommand} object.
     */
    ChangelogCommand changelog();

    /**
     * Appends to an existing git-note on the current HEAD commit.
     *
     * If a note doesn't exist, it works just like {@link #addNote(String, String)}
     *
     * @param note
     *      Content of the note.
     * @param namespace
     *      If unqualified, interpreted as "refs/notes/NAMESPACE" just like cgit.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void appendNote(String note, String namespace ) throws GitException, InterruptedException;

    /**
     * Adds a new git-note on the current HEAD commit.
     *
     * @param note
     *      Content of the note.
     * @param namespace
     *      If unqualified, interpreted as "refs/notes/NAMESPACE" just like cgit.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void addNote(String note, String namespace ) throws GitException, InterruptedException;

    /**
     * showRevision.
     *
     * @param r a {@link ObjectId} object.
     * @return a {@link List} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    public List<String> showRevision(ObjectId r) throws GitException, InterruptedException;

    /**
     * Given a Revision, show it as if it were an entry from git whatchanged, so that it
     * can be parsed by GitChangeLogParser.
     *
     * <p>
     * Changes are computed on the [from..to] range. If {@code from} is null, this prints
     * just one commit that {@code to} represents.
     *
     * <p>
     * For merge commit, this method reports one diff per each parent. This makes this method
     * behave differently from {@link #changelog()}.
     *
     * @param from a {@link ObjectId} object.
     * @param to a {@link ObjectId} object.
     * @return The git show output, in {@code raw} format.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    List<String> showRevision(ObjectId from, ObjectId to) throws GitException, InterruptedException;

    /**
     * Given a Revision, show it as if it were an entry from git whatchanged, so that it
     * can be parsed by GitChangeLogParser.
     *
     * <p>
     * If useRawOutput is true, the '--raw' option will include commit file information to be passed to the
     * GitChangeLogParser.
     *
     * <p>
     * Changes are computed on the [from..to] range. If {@code from} is null, this prints
     * just one commit that {@code to} represents.
     *
     * <p>
     * For merge commit, this method reports one diff per each parent. This makes this method
     * behave differently from {@link #changelog()}.
     *
     * @param from a {@link ObjectId} object.
     * @param to a {@link ObjectId} object.
     * @param useRawOutput a {@link Boolean} object.
     * @return The git show output, in {@code raw} format.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    List<String> showRevision(ObjectId from, ObjectId to, Boolean useRawOutput) throws GitException, InterruptedException;


    /**
     * Equivalent of "git-describe --tags".
     *
     * Find a nearby tag (including unannotated ones) and come up with a short identifier to describe the tag.
     *
     * @param commitIsh a {@link String} object.
     * @return a {@link String} object.
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    String describe(String commitIsh) throws GitException, InterruptedException;

    /**
     * Sets credentials.
     *
     * @param cred a {@link StandardUsernameCredentials} object.
     */
    void setCredentials(StandardUsernameCredentials cred);

    /**
     * Sets a proxy.
     *
     * @param proxy a {@link ProxyConfiguration} object.
     */
    void setProxy(ProxyConfiguration proxy);

    /**
     * Finds all the branches that include the given commit.
     *
     * @param revspec commit id to query for
     * @param allBranches whether remote branches should be also queried ({@code true}) or not ({@code false})
     * @return list of branches the specified commit belongs to
     * @throws GitException on Git exceptions
     * @throws InterruptedException on thread interruption
     */
    List<Branch> getBranchesContaining(String revspec, boolean allBranches) throws GitException, InterruptedException;

    /**
     * Returns name and object ID of all tags in current repository.
     *
     * @return set of tags in current repository
     * @throws GitException on Git exceptions
     * @throws InterruptedException on thread interruption
     */
    Set<GitObject> getTags() throws GitException, InterruptedException;
}

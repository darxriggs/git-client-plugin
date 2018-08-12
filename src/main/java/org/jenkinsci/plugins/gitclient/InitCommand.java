package org.jenkinsci.plugins.gitclient;

/**
 * Command to initialize a repository.
 */
public interface InitCommand extends GitCommand {

    /**
     * Sets the workspace directory.
     *
     * @param workspace the workspace directory
     * @return a {@link InitCommand} object.
     */
    InitCommand workspace(String workspace);

    /**
     * Use a bare repository without a workspace.
     *
     * @param bare whether the repository is bare
     * @return a {@link InitCommand} object.
     */
    InitCommand bare(boolean bare);
}

package org.jenkinsci.plugins.gitclient;

import hudson.plugins.git.GitException;

/**
 * Base type for the builder style command object for various git commands.
 *
 * @author Kohsuke Kawaguchi
 */
public interface GitCommand {
    /**
     * Executes the command.
     *
     * @throws GitException if underlying git operation fails.
     * @throws InterruptedException if interrupted.
     */
    void execute() throws GitException, InterruptedException;
}

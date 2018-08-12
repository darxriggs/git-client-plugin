package org.jenkinsci.plugins.gitclient;

/**
 * Command to rebase commits.
 */
public interface RebaseCommand extends GitCommand {

    /**
     * Sets the upstream to rebase onto.
     *
     * @param upstream a {@link String} object.
     * @return a {@link RebaseCommand} object.
     */
    RebaseCommand setUpstream(String upstream);
}

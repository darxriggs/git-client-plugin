package org.jenkinsci.plugins.gitclient;

import org.eclipse.jgit.transport.URIish;

/**
 * Command to push to a repository.
 *
 * @author <a href="mailto:nicolas.deloof@gmail.com">Nicolas De Loof</a>
 */
public interface PushCommand extends GitCommand {

    /**
     * URL of the repository to be pushed to.
     *
     * @param remote a {@link URIish} object.
     * @return a {@link PushCommand} object.
     */
    PushCommand to(URIish remote);

    /**
     * Refspec that specifies the references to be pushed.
     *
     * @param refspec a {@link String} object.
     * @return a {@link PushCommand} object.
     */
    PushCommand ref(String refspec);

    /**
     * Sets force mode.
     *
     * @return a {@link PushCommand} object.
     * @deprecated favour {@link #force(boolean)}
     */
    @Deprecated
    PushCommand force();

    /**
     * Sets force mode.
     *
     * @param force whether the push should be forced
     * @return a {@link PushCommand} object.
     * @since 2.5.0
     */
    PushCommand force(boolean force);

    /**
     * Request that tags and their references are not pushed.
     *
     * @param tags whether tags are pushed or not
     * @return a {@link PushCommand} object.
     */
    PushCommand tags(boolean tags);

    /**
     * Sets a timeout to be used.
     *
     * @param timeout timeout in minutes
     * @return a {@link PushCommand} object.
     */
    PushCommand timeout(Integer timeout);
}

package org.jenkinsci.plugins.gitclient;

import com.cloudbees.plugins.credentials.common.StandardCredentials;

import java.util.List;

/**
 * Command to checkout a reference into the workspace.
 *
 * @author <a href="mailto:nicolas.deloof@gmail.com">Nicolas De Loof</a>
 */
public interface CheckoutCommand extends GitCommand {

    /**
     * Sets the reference to checkout.
     *
     * @param ref a {@link String} object.
     * @return a {@link CheckoutCommand} object.
     */
    CheckoutCommand ref(String ref);

    /**
     * Sets the branch name to use for the checkout.
     *
     * @param branch a {@link String} object.
     * @return a {@link CheckoutCommand} object.
     */
    CheckoutCommand branch(String branch);

    /**
     * Delete an already existing branch on checkout.
     *
     * @param deleteBranch whether to delete an already existing branch
     * @return a {@link CheckoutCommand} object.
     */
    CheckoutCommand deleteBranchIfExist(boolean deleteBranch);

    /**
     * Sets the paths to use for a sparse checkout.
     *
     * @param sparseCheckoutPaths a {@link List} object.
     * @return a {@link CheckoutCommand} object.
     */
    CheckoutCommand sparseCheckoutPaths(List<String> sparseCheckoutPaths);

    /**
     * Sets a timeout to be used.
     *
     * @param timeout timeout in minutes
     * @return a {@link CheckoutCommand} object.
     */
    CheckoutCommand timeout(Integer timeout);

    /**
     * Call "git lfs pull" for the given remote after checkout.
     *
     * @param lfsRemote name of the remote used for git lfs operations (typically "origin").
     * @return a {@link CheckoutCommand} object.
     */
    CheckoutCommand lfsRemote(String lfsRemote);

    /**
     * Use separate credentials for "git lfs pull".
     *
     * @param lfsCredentials a {@link StandardCredentials} object.
     * @return a {@link CheckoutCommand} object.
     */
    CheckoutCommand lfsCredentials(StandardCredentials lfsCredentials);
}

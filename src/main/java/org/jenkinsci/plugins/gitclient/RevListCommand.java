package org.jenkinsci.plugins.gitclient;

import java.util.List;
import org.eclipse.jgit.lib.ObjectId;

/**
 * Command to list revisions.
 *
 * @author <a href="mailto:m.zahnlecker@gmail.com">Marc Zahnlecker</a>
 */
public interface RevListCommand extends GitCommand {
    /**
     * all.
     *
     * @return a {@link org.jenkinsci.plugins.gitclient.RevListCommand} object.
     * @deprecated favour {@link #all(boolean)}
     */
    @Deprecated
    RevListCommand all();

    /**
     * Sets to list all revisions.
     *
     * @param all whether to list all revisions
     * @return a {@link RevListCommand} object.
     * @since 2.5.0
     */
    RevListCommand all(boolean all);

    /**
     * Sets to only list the given revision or walk all.
     *
     * @param nowalk whether to skip revision walk
     * @return a {@link RevListCommand} object.
     */
    RevListCommand nowalk(boolean nowalk);

    /**
     * Sets to only use the first parent on a merge commit.
     *
     * @return a {@link RevListCommand} object.
     * @deprecated favour {@link #firstParent(boolean)}
     */
    @Deprecated
    RevListCommand firstParent();

    /**
     * Sets to only use the first parent on a merge commit.
     *
     * @param firstParent whether to only use the first parent
     * @return a {@link RevListCommand} object.
     * @since 2.5.0
     */
    RevListCommand firstParent(boolean firstParent);

    /**
     * Sets the result list to add the found revisions to.
     *
     * @param revs a {@link List} object.
     * @return a {@link RevListCommand} object.
     */
    RevListCommand to(List<ObjectId> revs);

    /**
     * Sets the refspec that defines the revisions to examine.
     *
     * @param reference a {@link String} object.
     * @return a {@link RevListCommand} object.
     */
    RevListCommand reference(String reference);
}

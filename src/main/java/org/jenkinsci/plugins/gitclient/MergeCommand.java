package org.jenkinsci.plugins.gitclient;

import org.eclipse.jgit.lib.ObjectId;

/**
 * Command to merge references.
 *
 * @author <a href="mailto:nicolas.deloof@gmail.com">Nicolas De Loof</a>
 */
public interface MergeCommand extends GitCommand {

    /**
     * Sets the revision to merge.
     *
     * @param rev a {@link ObjectId} object.
     * @return a {@link MergeCommand} object.
     */
    MergeCommand setRevisionToMerge(ObjectId rev);

    /**
     * Sets the merge commit message.
     *
     * @param message the merge commit message
     * @return a {@link MergeCommand} object.
     */
    MergeCommand setMessage(String message);

    /**
     * Sets the merge strategy.
     *
     * @param strategy a {@link MergeCommand.Strategy} object.
     * @return a {@link MergeCommand} object.
     */
    MergeCommand setStrategy(Strategy strategy);

    /**
     * The merge strategy.
     */
    public enum Strategy {
        DEFAULT, RESOLVE, RECURSIVE, OCTOPUS, OURS, SUBTREE, RECURSIVE_THEIRS;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    /**
     * Sets the fast forward mode.
     *
     * @param fastForwardMode mode used for merging
     * @return a {@link MergeCommand} object.
     */
    MergeCommand setGitPluginFastForwardMode(GitPluginFastForwardMode fastForwardMode);

    /**
     * The fast forward mode.
     *
     * The name {@code FastForwardMode} collides with
     * {@link org.eclipse.jgit.api.MergeCommand.FastForwardMode}
     * so a different name had to be chosen.
     */
    public enum GitPluginFastForwardMode {
        /** fast forward update the branch pointer only (default) */
        FF,
        /** create a merge commit even for a fast forward */
        FF_ONLY,
        /** abort unless the merge is a fast forward */
        NO_FF;

        @Override
        public String toString() {
            return "--"+name().toLowerCase().replace("_","-");
        }
    }

    /**
     * Sets if commits should be squashed together before merge.
     *
     * @param squash whether to squash commits or not
     * @return a {@link MergeCommand} object.
     */
    MergeCommand setSquash(boolean squash);

    /**
     * Sets if the merge result should be committed.
     *
     * @param commit whether or not to commit the result after a successful merge
     * @return a {@link MergeCommand} object.
     */
    MergeCommand setCommit(boolean commit);
}

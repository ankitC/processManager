package Message;

import Migratable.MigratableProcess;

/**
 * Describes the current state of some {@link MigratableProcess}.
 */
public enum Status {
    RUNNING,
    DONE,
    SUSPENDED
}

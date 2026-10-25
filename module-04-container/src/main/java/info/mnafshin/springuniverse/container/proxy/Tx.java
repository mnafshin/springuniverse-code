package info.mnafshin.springuniverse.container.proxy;

import org.springframework.transaction.support.TransactionSynchronizationManager;

final class Tx {

    private Tx() {
    }

    /** true only while a Spring-managed transaction is running on this thread */
    static boolean active() {
        return TransactionSynchronizationManager.isActualTransactionActive();
    }
}

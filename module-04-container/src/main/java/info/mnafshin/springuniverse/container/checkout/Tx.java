package info.mnafshin.springuniverse.container.checkout;

import org.springframework.transaction.support.TransactionSynchronizationManager;

public final class Tx {

    private Tx() {
    }

    /** true only while a Spring-managed transaction is running on this thread */
    public static boolean active() {
        return TransactionSynchronizationManager.isActualTransactionActive();
    }
}

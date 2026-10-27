package info.mnafshin.springuniverse.container.checkout.mail;

import org.springframework.stereotype.Component;

/** A stand-in for a slow mail server: sending one confirmation email takes 3 seconds. */
@Component
public class MailServer {

    public void send(String id) {
        try {
            Thread.sleep(3000); // a slow mail server
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("confirmation " + id + " sent on: " + Thread.currentThread().getName());
    }
}

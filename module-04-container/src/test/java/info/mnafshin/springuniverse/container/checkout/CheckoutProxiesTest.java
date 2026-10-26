package info.mnafshin.springuniverse.container.checkout;

import info.mnafshin.springuniverse.container.checkout.app.CheckoutApp;
import info.mnafshin.springuniverse.container.checkout.async.AsyncApp;
import info.mnafshin.springuniverse.container.checkout.finalmethod.FinalMethodApp;
import info.mnafshin.springuniverse.container.checkout.fix.FixApp;
import info.mnafshin.springuniverse.container.checkout.kinds.KindsApp;
import info.mnafshin.springuniverse.container.checkout.selfcall.SelfCallApp;
import info.mnafshin.springuniverse.container.checkout.whatis.WhatIsApp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.assertj.core.api.Assertions.assertThat;

/** Checks every line the T4.2 video (proxies, one checkout app) shows, in order. */
@ExtendWith(OutputCaptureExtension.class)
class CheckoutProxiesTest {

    private static String[] lines(CapturedOutput output) {
        return output.getOut().lines().filter(l -> !l.contains(" INFO ") && !l.contains(" WARN ")).toArray(String[]::new);
    }

    @Test
    void theOrderServiceIsAProxyTheCatalogIsNot(CapturedOutput output) {
        WhatIsApp.main(new String[0]);
        assertThat(lines(output)).containsExactly(
                "productCatalog class: ProductCatalog",
                "orderService   class: OrderService$$SpringCGLIB$$0",
                "-- is it a proxy?",
                "productCatalog: false",
                "orderService:   true",
                "orderService is an OrderService? true",
                "the real object behind it: OrderService");
    }

    @Test
    void bootUsesCglibAndProxyableGivesAJdkProxy(CapturedOutput output) {
        KindsApp.main(new String[0]);
        String[] lines = lines(output);
        assertThat(lines).hasSize(8);
        assertThat(lines[0]).isEqualTo("shippingService: ShippingService$$SpringCGLIB$$0");
        assertThat(lines[1]).isEqualTo("  is a ShippingService? true");
        assertThat(lines[2]).startsWith("paymentService:  jdk.proxy").contains("$Proxy");
        assertThat(lines).endsWith("  is a PaymentService? false", "-- asking for the class",
                "failed: NoSuchBeanDefinitionException", "-- asking for the interface", "charge(A-1) works: true");
    }

    @Test
    void aFinalTotalRunsOnTheProxyWithNullFields(CapturedOutput output) {
        FinalMethodApp.main(new String[0]);
        assertThat(lines(output)).containsExactly(
                "-- final total()",
                "failed: NullPointerException: Cannot invoke \"info.mnafshin.springuniverse.container.checkout.finalmethod.PriceTable.price(String)\" because \"this.prices\" is null",
                "-- normal total()",
                "total: 7");
    }

    @Test
    void theSelfCallHasNoTransactionAndLeavesAHalfSavedOrder(CapturedOutput output) {
        SelfCallApp.main(new String[0]);
        assertThat(lines(output)).containsExactly(
                "-- save() from outside: coffee, muffin",
                "save(A-1): transaction active = true",
                "failed: no price for muffin",
                "in the database: orders 0, order lines 0",
                "-- checkout() calls save(): coffee, muffin",
                "save(A-2): transaction active = false",
                "failed: no price for muffin",
                "in the database: orders 1, order lines 1");
    }

    @Test
    void anotherBeanGivesATransactionAndARollback(CapturedOutput output) {
        FixApp.main(new String[0]);
        assertThat(lines(output)).containsExactly(
                "-- checkout() calls writer.save(): coffee, muffin",
                "save(A-3): transaction active = true",
                "failed: no price for muffin",
                "in the database: orders 0, order lines 0",
                "-- checkout(): coffee, cake",
                "save(A-4): transaction active = true",
                "in the database: orders 1, order lines 2");
    }

    @Test
    void theAsyncSelfCallRunsOnMainTheMailerOnATaskThread(CapturedOutput output) {
        AsyncApp.main(new String[0]);
        String[] lines = lines(output);
        assertThat(lines).hasSize(5);
        assertThat(lines).startsWith("checkout runs on: main", "-- checkout() calls this.sendConfirmation()",
                "confirmation sent on: main", "-- fix: ConfirmationMailer, another bean");
        assertThat(lines[4]).startsWith("confirmation sent on: task-");
    }

    @Test
    void theWholeCheckoutGoesThroughEveryProxy(CapturedOutput output) {
        CheckoutApp.main(new String[0]);
        String[] lines = lines(output);
        assertThat(lines).hasSize(9);
        assertThat(lines).startsWith("-- checkout A-7: coffee, cake", "save(A-7): transaction active = true",
                "charge(A-7, 7): transaction active = true");
        assertThat(lines[3]).startsWith("confirmation sent on: task-");
        assertThat(lines).endsWith("in the database: orders 1, order lines 2", "-- checkout A-8: coffee, muffin",
                "save(A-8): transaction active = true", "failed: no price for muffin",
                "in the database: orders 1, order lines 2");
    }
}

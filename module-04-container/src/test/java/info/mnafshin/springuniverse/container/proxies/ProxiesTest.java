package info.mnafshin.springuniverse.container.proxies;

import info.mnafshin.springuniverse.container.proxies.async.AsyncApp;
import info.mnafshin.springuniverse.container.proxies.finalmethod.FinalMethodApp;
import info.mnafshin.springuniverse.container.proxies.kinds.KindsApp;
import info.mnafshin.springuniverse.container.proxies.selfcall.SelfCallApp;
import info.mnafshin.springuniverse.container.proxies.whatis.WhatIsApp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.assertj.core.api.Assertions.assertThat;

/** Checks every line the T4.3 video shows, in order. */
@ExtendWith(OutputCaptureExtension.class)
class ProxiesTest {

    private static String[] lines(CapturedOutput output) {
        return output.getOut().lines().filter(l -> !l.contains(" INFO ") && !l.contains(" WARN ")).toArray(String[]::new);
    }

    @Test
    void aBeanWithTransactionalIsAProxy(CapturedOutput output) {
        WhatIsApp.main(new String[0]);
        assertThat(lines(output)).containsExactly(
                "menuService  class: MenuService",
                "orderService class: OrderService$$SpringCGLIB$$0",
                "-- is it a proxy?",
                "menuService:  false",
                "orderService: true",
                "orderService is an OrderService? true",
                "the real object behind it: OrderService");
    }

    @Test
    void bootUsesCglibAndProxyableGivesAJdkProxy(CapturedOutput output) {
        KindsApp.main(new String[0]);
        String[] lines = lines(output);
        assertThat(lines).hasSize(8);
        assertThat(lines[0]).isEqualTo("refundService:  kinds.RefundService$$SpringCGLIB$$0");
        assertThat(lines[1]).isEqualTo("  is a RefundService? true");
        assertThat(lines[2]).startsWith("paymentService: jdk.proxy").contains("$Proxy");
        assertThat(lines).endsWith("  is a PaymentService? false", "-- asking for the class",
                "failed: NoSuchBeanDefinitionException", "-- asking for the interface", "charge() works: true");
    }

    @Test
    void aFinalMethodRunsOnTheProxyWithNullFields(CapturedOutput output) {
        FinalMethodApp.main(new String[0]);
        assertThat(lines(output)).containsExactly(
                "-- final method",
                "failed: NullPointerException: Cannot invoke \"info.mnafshin.springuniverse.container.proxies.finalmethod.PriceTable.lookup(String)\" because \"this.table\" is null",
                "-- normal method",
                "coffee costs 3");
    }

    @Test
    void theSelfCallHasNoTransactionTheOtherBeanHasOne(CapturedOutput output) {
        SelfCallApp.main(new String[0]);
        assertThat(lines(output)).containsExactly(
                "-- save() from outside", "save(A-1): transaction active = true",
                "-- checkout() calls save()", "save(A-2): transaction active = false",
                "-- fix: save() in another bean", "save(A-3): transaction active = true");
    }

    @Test
    void theSelfCallIsNotAsyncTheOtherBeanIs(CapturedOutput output) {
        AsyncApp.main(new String[0]);
        assertThat(lines(output)).containsExactly(
                "main runs on: main",
                "-- finish() calls this.send()", "send() ran on: main",
                "-- fix: send() in another bean", "send() ran on: task-1");
    }
}

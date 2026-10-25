package info.mnafshin.springuniverse.container.proxy;

import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ProxyApp.class)
class ProxyTest {

    @Autowired
    OrderService orders;

    @Autowired
    FixedOrderService fixedOrders;

    @Autowired
    Payments payments;

    @Test
    void springBootUsesClassBasedCglibProxiesByDefault() {
        assertThat(AopUtils.isCglibProxy(orders)).isTrue();
        assertThat(orders.getClass().getName()).contains("OrderService$$SpringCGLIB$$");
    }

    @Test
    void proxyableInterfacesGivesAJdkDynamicProxy() {
        assertThat(AopUtils.isJdkDynamicProxy(payments)).isTrue();
        assertThat(payments).isNotInstanceOf(PaymentService.class);
        assertThat(payments.charge("A-4")).isTrue();
    }

    @Test
    void callThroughTheProxyStartsATransaction() {
        assertThat(orders.save("A-1")).isTrue();
    }

    @Test
    void selfInvocationSkipsTheProxy() {
        assertThat(orders.checkout("A-2")).isFalse();
    }

    @Test
    void movingTheMethodToAnotherBeanFixesIt() {
        assertThat(fixedOrders.checkout("A-3")).isTrue();
    }

    @Test
    void appPrintsProxyClassesAndTransactionState() {
        ProxyApp.main(new String[0]);
    }
}

package info.mnafshin.springuniverse.container.cycle;

import info.mnafshin.springuniverse.container.cyclefix.FixedCycleApp;
import info.mnafshin.springuniverse.container.fieldcycle.FieldCycleApp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatNoException;

class CircularDependencyTest {

    @Test
    void constructorCycleFailsInSpringBoot() {
        assertThatThrownBy(() -> new SpringApplicationBuilder(CycleApp.class).run())
                .hasRootCauseInstanceOf(BeanCurrentlyInCreationException.class);
    }

    @Test
    void constructorCycleFailsEvenWhenCircularReferencesAreAllowed() {
        assertThatThrownBy(() -> new SpringApplicationBuilder(CycleApp.class)
                .properties("spring.main.allow-circular-references=true").run())
                .hasRootCauseInstanceOf(BeanCurrentlyInCreationException.class);
    }

    @Test
    void fieldCycleFailsInSpringBootByDefault() {
        assertThatThrownBy(() -> new SpringApplicationBuilder(FieldCycleApp.class).run())
                .hasRootCauseInstanceOf(BeanCurrentlyInCreationException.class);
    }

    @Test
    void fieldCycleStartsOnlyWithTheEscapeHatch() {
        try (var context = new SpringApplicationBuilder(FieldCycleApp.class)
                .properties("spring.main.allow-circular-references=true").run()) {
            assertThat(context.getBean(info.mnafshin.springuniverse.container.fieldcycle.InvoiceService.class).customers())
                    .isNotNull();
        }
    }

    @Test
    void plainSpringFrameworkResolvesAFieldCycle() {
        try (var context = new AnnotationConfigApplicationContext(
                info.mnafshin.springuniverse.container.fieldcycle.InvoiceService.class,
                info.mnafshin.springuniverse.container.fieldcycle.CustomerService.class)) {
            assertThat(context.getBean(info.mnafshin.springuniverse.container.fieldcycle.CustomerService.class).invoices())
                    .isNotNull();
        }
    }

    @Test
    void breakingTheCycleWithAThirdBeanStarts() {
        assertThatNoException().isThrownBy(() -> {
            try (var context = new SpringApplicationBuilder(FixedCycleApp.class).run()) {
                assertThat(context.getBean(info.mnafshin.springuniverse.container.cyclefix.InvoiceService.class)
                        .invoiceFor("c-1")).isEqualTo("Invoice for Ada");
            }
        });
    }
}

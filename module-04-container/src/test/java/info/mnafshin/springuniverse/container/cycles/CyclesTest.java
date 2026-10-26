package info.mnafshin.springuniverse.container.cycles;

import info.mnafshin.springuniverse.container.cycles.ctor.CtorCycleApp;
import info.mnafshin.springuniverse.container.cycles.events.EventsApp;
import info.mnafshin.springuniverse.container.cycles.extract.ExtractApp;
import info.mnafshin.springuniverse.container.cycles.field.FieldCycleApp;
import info.mnafshin.springuniverse.container.cycles.halfbuilt.HalfBuiltApp;
import info.mnafshin.springuniverse.container.cycles.lazy.LazyApp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Checks every line the T4.4 video shows. */
@ExtendWith(OutputCaptureExtension.class)
class CyclesTest {

    private static final String ALLOW = "--spring.main.allow-circular-references=true";

    private static String[] lines(CapturedOutput output) {
        return output.getOut().lines().filter(l -> !l.contains(" INFO ") && !l.contains(" WARN ") && !l.contains(" ERROR ")
                && !l.isBlank()).toArray(String[]::new);
    }

    @Test
    void aConstructorCycleFailsWithTheCycleReport(CapturedOutput output) {
        assertThatThrownBy(() -> CtorCycleApp.main(new String[0])).rootCause().isInstanceOf(BeanCurrentlyInCreationException.class);
        assertThat(output.getAll())
                .contains("APPLICATION FAILED TO START")
                .contains("The dependencies of some of the beans in the application context form a cycle:")
                .contains("|  customerService defined in file [")
                .contains("|  invoiceService defined in file [")
                .contains("Relying upon circular references is discouraged and they are prohibited by default.")
                .contains("setting spring.main.allow-circular-references to true")
                .doesNotContain("started");
    }

    @Test
    void theFlagCannotBreakAConstructorCycle(CapturedOutput output) {
        assertThatThrownBy(() -> CtorCycleApp.main(new String[] {ALLOW})).rootCause().isInstanceOf(BeanCurrentlyInCreationException.class);
        assertThat(output.getAll()).contains("Despite circular references being allowed, the dependency cycle between beans could not be broken.");
    }

    @Test
    void aFieldCycleIsStillRefusedByDefault(CapturedOutput output) {
        assertThatThrownBy(() -> FieldCycleApp.main(new String[0])).rootCause().isInstanceOf(BeanCurrentlyInCreationException.class);
        assertThat(output.getAll()).contains("form a cycle")
                .contains("|  customerService (field ").contains("CustomerService.invoices)")
                .contains("|  invoiceService (field ").contains("InvoiceService.customers)");
    }

    @Test
    void theFlagLetsAFieldCycleStart(CapturedOutput output) {
        FieldCycleApp.main(new String[] {ALLOW});
        assertThat(lines(output)).containsExactly("started",
                "customers.invoices is the invoice service? true",
                "invoices.customers is the customer service? true");
    }

    @Test
    void theEarlyReferenceIsHalfBuilt(CapturedOutput output) {
        assertThatThrownBy(() -> HalfBuiltApp.main(new String[] {ALLOW})).rootCause()
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("because \"this.invoices\" is null");
        assertThat(lines(output)).startsWith("InvoiceService @PostConstruct: asking the customer service");
        assertThat(output.getOut()).doesNotContain("open invoices: ").doesNotContain("started");
    }

    @Test
    void theFixesStartWithoutTheFlag(CapturedOutput output) {
        ExtractApp.main(new String[0]);
        EventsApp.main(new String[0]);
        assertThat(lines(output)).containsExactly("started", "welcome Ada, invoice for Ada",
                "started", "invoice paid by c-1", "customer c-1 marked as paid");
    }

    @Test
    void lazyInjectsAProxy(CapturedOutput output) {
        LazyApp.main(new String[0]);
        assertThat(lines(output)).containsExactly("started", "injected: CustomerService$$SpringCGLIB$$0", "invoice for Ada");
    }
}

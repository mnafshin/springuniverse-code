package info.mnafshin.springuniverse.customizing;

/** The value every JSON example prints. */
public record Order(String customerName, int itemCount) {

    public static final Order SAMPLE = new Order("Ada", 3);

}

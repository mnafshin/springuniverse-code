package info.mnafshin.springuniverse.foundations.ioc;

import java.util.List;

/**
 * Library style: YOUR code is in control. You call the library ({@link String#join}),
 * it does its work and returns control to you.
 */
public final class LibraryExample {

    private LibraryExample() {
    }

    public static String toCsv(List<String> names) {
        return String.join(",", names); // you call the library
    }

    public static void main(String[] args) {
        System.out.println(toCsv(List.of("Ada", "Linus", "Grace")));
    }
}

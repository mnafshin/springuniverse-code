package info.mnafshin.springuniverse.foundations.jvm;

/**
 * F08 companion: Java source becomes bytecode (class files) that the JVM runs.
 * {@link #answer()} compiles to a method the JVM can invoke; we also show that
 * this class was loaded by a ClassLoader (part of the JVM).
 */
public final class JvmDemo {

    public static int answer() {
        return 40 + 2;
    }

    public static String runtimeName() {
        return System.getProperty("java.vm.name");
    }

    public static void main(String[] args) {
        System.out.println("answer=" + answer());
        System.out.println("vm=" + runtimeName());
        System.out.println("loaded by " + JvmDemo.class.getClassLoader().getClass().getName());
    }
}

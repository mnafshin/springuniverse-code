package info.mnafshin.springuniverse.foundations.maven;

/**
 * F06 companion: Maven is the build tool that compiles, tests and packages this module.
 * Run {@code ./mvnw -pl module-00-foundations verify} from the repo root.
 * See https://maven.apache.org/what-is-maven.html
 */
public final class MavenNote {

    public static String whatMavenDoes() {
        return "Maven downloads dependencies, compiles sources, runs tests, and packages the project.";
    }

    public static void main(String[] args) {
        System.out.println(whatMavenDoes());
    }
}

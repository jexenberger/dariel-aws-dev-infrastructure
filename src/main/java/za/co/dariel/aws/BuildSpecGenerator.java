package za.co.dariel.aws;

import com.github.mustachejava.DefaultMustacheFactory;

import java.io.StringWriter;

public class BuildSpecGenerator {

    public static String generateBuildSpec(EnvironmentSettings settings) {
        var factory = new DefaultMustacheFactory();
        var m = factory.compile("buildspec.yml");
        final var writer = new StringWriter();
        m.execute(writer, settings);
        return writer.toString();
    }

}

package za.co.dariel.aws;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BuildSpecGeneratorTest {

    @Test
    void generate() {
        var settings = new EnvironmentSettings();
        var template = BuildSpecGenerator.generateBuildSpec(settings);
        assertNotNull(template);
        System.out.println(template);
    }
}

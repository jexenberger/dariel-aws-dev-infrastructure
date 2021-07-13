package za.co.dariel.aws;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnvironmentSettingsTest {

    @Test
    void load() {
        System.setProperty("dev.environment.properties", "src/test/resources/environment.properties");
        var settings = new EnvironmentSettings();
        assertEquals("qwerty", settings.getName());
        assertEquals("a-qwerty-repo", settings.getRepository());
        assertEquals("build", settings.getBuildCommand());
        assertEquals("package", settings.getPackageCommand());
        assertEquals("outputFile", settings.getOutputFile());

    }
}

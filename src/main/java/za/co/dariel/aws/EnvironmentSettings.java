package za.co.dariel.aws;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class EnvironmentSettings {

    private Properties properties = new Properties();

    private String name = null;

    public EnvironmentSettings() {
        var file = System.getenv("DEV_ENVIRONMENT_PROS");
        if (file == null) {
            file = System.getProperty("dev.environment.properties");
        }
        if (file == null) {
            file = System.getProperty("user.home") + "/.dev-environment.properties";
        }

        try {
            properties.load(new FileReader(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var name = properties.getProperty("name");
        if (isBlank(name)) {
            throw new IllegalStateException("properties file must have a name propert");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getBuildCommand() {
        var buildCommand = properties.getProperty("buildCommand");
        if (isBlank(buildCommand)) {
            throw new IllegalStateException("build command is required");
        }
        return buildCommand;
    }

    public String getPackageCommand() {
        var packageCommand = properties.getProperty("packageCommand");
        if (isBlank(packageCommand)) {
            throw new IllegalStateException("package command is required");
        }
        return packageCommand;
    }

    public String getRepository() {
        return properties.getProperty("repository", name + "-repo");
    }

    public String getOutputFile() {
        var outputFile =  properties.getProperty("outputFile");
        if (isBlank(outputFile)) {
            throw new IllegalStateException("outputFile is a required property");
        }
        return outputFile;
    }


    private static boolean isBlank(String val) {
        return val == null || val.trim().equals("");
    }
}

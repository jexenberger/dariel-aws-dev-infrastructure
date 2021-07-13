package za.co.dariel.aws;

import software.amazon.awscdk.core.App;

public class EnvironmentApp {

    public static void main(String[] args) throws Exception {

        var environmentSettings = new EnvironmentSettings();
        var app = new App();
        new EnvironmentStack(app, environmentSettings.getName() + "-dev-network", environmentSettings);
        app.synth();
    }

}

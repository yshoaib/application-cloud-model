package ca.appsimulations.models;

import ca.appsimulations.models.model.application.App;
import ca.appsimulations.models.model.application.DefaultAppBuilder;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AppFactoryTest {

    @Test
    public void testAppFactory() {
        String appName = "test";
        int maxReplicas = 10;
        double responseTime = 350.0;
        App app = DefaultAppBuilder.build(appName,
                                          maxReplicas,
                                          responseTime);

        assertThat(app.name()).isEqualTo("test");
        assertThat(app.services()).hasSize(5);
        assertThat(app.hasService("Browser")).isTrue();
        assertThat(app.findService("Browser").get().name()).isEqualTo("Browser");
        assertThat(app.findService("Browser").get().entries()).hasSize(1);
    }
}

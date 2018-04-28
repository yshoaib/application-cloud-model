package ca.appsimulations.models;

import ca.appsimulations.models.model.application.App;
import ca.appsimulations.models.model.application.DefaultAppBuilder;
import ca.appsimulations.models.model.cloud.Cloud;
import ca.appsimulations.models.model.cloud.ContainerType;
import ca.appsimulations.models.model.cloud.DefaultCloudContainerBuilder;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CloudContainerTypeFindTest {

    @Test
    public void containerTypeFound(){
        App app = DefaultAppBuilder.build("app",
                                          2,
                                          3.0D);

        Cloud cloud = DefaultCloudContainerBuilder.build(app);
        Optional<ContainerType> small = cloud.findContainerTypeByName("small");
        assertThat(small.get()).isEqualTo(ContainerType.SM);
    }

    @Test
    public void containerTypeNotFound(){
        App app = DefaultAppBuilder.build("app",
                                          2,
                                          3.0D);

        Cloud cloud = DefaultCloudContainerBuilder.build(app);
        Optional<ContainerType> small = cloud.findContainerTypeByName("dummy");
        assertThat(small).isEqualTo(Optional.empty());
    }
}

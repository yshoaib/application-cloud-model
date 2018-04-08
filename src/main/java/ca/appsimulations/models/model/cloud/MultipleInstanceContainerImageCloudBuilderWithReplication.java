package ca.appsimulations.models.model.cloud;

import ca.appsimulations.models.model.application.App;

import static ca.appsimulations.models.model.cloud.ContainerType.LARGE;
import static ca.appsimulations.models.model.cloud.ContainerType.MEDIUM;
import static ca.appsimulations.models.model.cloud.ContainerType.SMALL;
import static java.util.Arrays.asList;

public class MultipleInstanceContainerImageCloudBuilderWithReplication {

    public static Cloud build(App app) {
        Cloud cloud = CloudBuilder.builder()
                .name("cloud1")
                .containerTypes(asList(SMALL, MEDIUM, LARGE))
                .containerImage("Browser")
                .service("Browser", app)
                .buildContainerImage()
                .containerImage("TaskA")
                .service("TaskA", app)
                .buildContainerImage()
                .containerImage("TaskB")
                .service("TaskB", app)
                .buildContainerImage()
                .containerImage("TaskC")
                .service("TaskC", app)
                .buildContainerImage()
                .containerImage("TaskD")
                .service("TaskD", app)
                .buildContainerImage()
                .build();

        cloud.instantiateContainer("pClient", "Browser", SMALL);
        cloud.instantiateContainer("TaskA", SMALL, 2);
        cloud.instantiateContainer("TaskA", MEDIUM, 2);
        cloud.instantiateContainer("TaskA", LARGE);
        cloud.instantiateContainer("pTaskB", "TaskB", SMALL);
        cloud.instantiateContainer("pTaskB_1", "TaskB", MEDIUM);
        cloud.instantiateContainer("pTaskC", "TaskC", SMALL);
        cloud.instantiateContainer("pTaskD", "TaskD", SMALL);
        return cloud;
    }
}

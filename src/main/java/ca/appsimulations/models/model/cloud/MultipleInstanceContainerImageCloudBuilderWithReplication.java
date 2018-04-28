package ca.appsimulations.models.model.cloud;

import ca.appsimulations.models.model.application.App;

import static ca.appsimulations.models.model.cloud.ContainerType.LA;
import static ca.appsimulations.models.model.cloud.ContainerType.MD;
import static ca.appsimulations.models.model.cloud.ContainerType.SM;
import static java.util.Arrays.asList;

public class MultipleInstanceContainerImageCloudBuilderWithReplication {

    public static Cloud build(App app) {
        Cloud cloud = CloudBuilder.builder()
                .name("cloud1")
                .containerTypes(asList(SM, MD, LA))
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

        cloud.instantiateContainer("pClient", "Browser", SM);
        cloud.instantiateContainer("TaskA", SM, 2);
        cloud.instantiateContainer("TaskA", MD, 2);
        cloud.instantiateContainer("TaskA", LA);
        cloud.instantiateContainer("pTaskB", "TaskB", SM);
        cloud.instantiateContainer("pTaskB_1", "TaskB", MD);
        cloud.instantiateContainer("pTaskC", "TaskC", SM);
        cloud.instantiateContainer("pTaskD", "TaskD", SM);
        return cloud;
    }
}

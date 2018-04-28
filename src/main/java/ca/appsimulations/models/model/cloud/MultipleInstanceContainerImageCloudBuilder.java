package ca.appsimulations.models.model.cloud;

import ca.appsimulations.models.model.application.App;

import static ca.appsimulations.models.model.cloud.ContainerType.LA;
import static ca.appsimulations.models.model.cloud.ContainerType.MD;
import static ca.appsimulations.models.model.cloud.ContainerType.SM;
import static java.util.Arrays.asList;

public class MultipleInstanceContainerImageCloudBuilder {

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
        cloud.instantiateContainer("pTaskA", "TaskA", SM);
        cloud.instantiateContainer("pTaskA_4", "TaskA", SM);
        cloud.instantiateContainer("pTaskA_2", "TaskA", MD);
        cloud.instantiateContainer("pTaskA_5", "TaskA", MD);
        cloud.instantiateContainer("pTaskA_3", "TaskA", LA);
        cloud.instantiateContainer("pTaskB", "TaskB", SM);
        cloud.instantiateContainer("pTaskB_1", "TaskB", MD);
        cloud.instantiateContainer("pTaskC", "TaskC", SM);
        cloud.instantiateContainer("pTaskD", "TaskD", SM);
        return cloud;
    }
}

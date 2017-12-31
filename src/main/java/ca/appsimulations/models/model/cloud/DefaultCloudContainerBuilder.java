package ca.appsimulations.models.model.cloud;

import ca.appsimulations.models.model.application.App;

import static ca.appsimulations.models.model.cloud.ContainerType.*;
import static java.util.Arrays.asList;

public class DefaultCloudContainerBuilder {

    public static Cloud build(App app) {
        Cloud cloud1 = new Cloud("cloud1", asList(SMALL, MEDIUM, LARGE));
        buildSimpleContainer("pClient", "Browser", cloud1, app, "Browser");
        buildSimpleContainer("pTaskA", "TaskA", cloud1, app, "TaskA");
        buildSimpleContainer("pTaskB", "TaskB", cloud1, app, "TaskB");
        buildSimpleContainer("pTaskC", "TaskC", cloud1, app, "TaskC");
        buildSimpleContainer("pTaskD", "TaskD", cloud1, app, "TaskD");
        return cloud1;
    }

    private static void buildSimpleContainer(String containerName, String imageName, Cloud cloud1, App app, String
            serviceName) {
        ContainerFactory.build(containerName,
                               ContainerImageFactory.build(imageName,
                                                           SMALL,
                                                           app.findService(serviceName).get(),
                                                           cloud1).get(),
                               cloud1);
    }
}

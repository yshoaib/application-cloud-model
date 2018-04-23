package ca.appsimulations.models.model.cloud;

import ca.appsimulations.models.model.application.App;

import java.util.Arrays;

import static ca.appsimulations.models.model.cloud.ContainerType.LARGE;
import static ca.appsimulations.models.model.cloud.ContainerType.MEDIUM;
import static ca.appsimulations.models.model.cloud.ContainerType.SMALL;
import static java.util.Arrays.asList;

public class EntcsCloudBuilder {

    public static Cloud build(App app) {

        Cloud cloud = CloudBuilder.builder().name("cloud1").containerTypes(Arrays.asList(ContainerType.SMALL)).
                containerImage("Browser").service("Browser", app).buildContainerImage().
                containerImage("AppServer").service("AppServer", app).buildContainerImage().
                containerImage("DB").service("DB", app).buildContainerImage().
                containerImage("Disk").service("Disk", app).buildContainerImage().build();
        cloud.instantiateContainer("pClient", "Browser", ContainerType.SMALL);
        cloud.instantiateContainer("pAppServer", "AppServer", ContainerType.SMALL);
        cloud.instantiateContainer("pDB", "DB", ContainerType.SMALL);
        cloud.instantiateContainer("pDisk", "Disk", ContainerType.SMALL);


        return cloud;
    }
}

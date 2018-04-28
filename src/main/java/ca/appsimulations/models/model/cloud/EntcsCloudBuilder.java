package ca.appsimulations.models.model.cloud;

import ca.appsimulations.models.model.application.App;

import java.util.Arrays;

public class EntcsCloudBuilder {

    public static Cloud build(App app) {

        Cloud cloud = CloudBuilder.builder().name("cloud1").containerTypes(Arrays.asList(ContainerType.SM)).
                containerImage("Browser").service("Browser", app).buildContainerImage().
                containerImage("AppServer").service("AppServer", app).buildContainerImage().
                containerImage("DB").service("DB", app).buildContainerImage().
                containerImage("Disk").service("Disk", app).buildContainerImage().build();
        cloud.instantiateContainer("pClient", "Browser", ContainerType.SM);
        cloud.instantiateContainer("pAppServer", "AppServer", ContainerType.SM);
        cloud.instantiateContainer("pDB", "DB", ContainerType.SM);
        cloud.instantiateContainer("pDisk", "Disk", ContainerType.SM);


        return cloud;
    }
}

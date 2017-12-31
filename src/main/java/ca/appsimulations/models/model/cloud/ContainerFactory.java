package ca.appsimulations.models.model.cloud;

public class ContainerFactory {
    public static Container build(String name, ContainerImage containerImage, Cloud cloud) {
        Container container = Container.builder().name(name).containerImage(containerImage).build();
        containerImage.service().containers().add(container);
        containerImage.instances().add(container);
        cloud.containers().add(container);
        return container;
    }
}

package ca.appsimulations.models.model.cloud;

import java.util.Optional;

public class ContainerFactory {
    public static Optional<Container> build(String name, ContainerImage containerImage, ContainerType containerType, Cloud cloud) {
        Optional<Container> result = Optional.empty();
        if(cloud.hasContainerType(containerType)) {
            Container container = Container.builder().name(name).containerImage(containerImage).containerType(containerType).build();
            containerImage.service().containers().add(container);
            containerImage.instances().add(container);
            cloud.containers().add(container);
            result = Optional.of(container);
        }
        return result;
    }
}

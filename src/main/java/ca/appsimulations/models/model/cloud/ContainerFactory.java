package ca.appsimulations.models.model.cloud;

import ca.appsimulations.models.model.application.Service;

import java.util.Optional;

public class ContainerFactory {
    public static Optional<Container> build(String name,
                                            ContainerImage containerImage,
                                            ContainerType containerType,
                                            Cloud cloud) {
        Optional<Container> result = Optional.empty();
        if (cloud.hasContainerType(containerType)) {
            Container container =
                    Container.builder().name(name).containerImage(containerImage).containerType(containerType).build();

            Service service = containerImage.service();
            service.addContainer(container, containerType);
            containerImage.instances().add(container);
            cloud.containers().add(container);
            result = Optional.of(container);
        }
        return result;
    }
}

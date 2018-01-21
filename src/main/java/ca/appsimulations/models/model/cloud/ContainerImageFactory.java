package ca.appsimulations.models.model.cloud;

import ca.appsimulations.models.model.application.Service;

import java.util.Optional;

public class ContainerImageFactory {
    public static Optional<ContainerImage> build(String name,
                                                 Service service,
                                                 Cloud cloud) {

        Optional result;
        ContainerImage containerImage = ContainerImage.builder().name(name).service(service).build();
        cloud.containerImages().add(containerImage);
        service.containerImages().add(containerImage);
        result = Optional.of(containerImage);
        return result;
    }
}

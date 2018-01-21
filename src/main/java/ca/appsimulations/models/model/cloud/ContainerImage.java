package ca.appsimulations.models.model.cloud;

import ca.appsimulations.models.model.application.Service;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ca.appsimulations.models.model.cloud.ContainerType.SMALL;

@Builder
@Data
@Accessors(chain = true, fluent = true)
public class ContainerImage {
    private final String name;
    private final Service service;
    private final List<Container> instances = new ArrayList<>();

    public Container instantiate(String containerName, Cloud cloud, ContainerType containerType) {
        Optional<Container> containerOptional = ContainerFactory.build(containerName,
                                                           this,
                                                           containerType,
                                                           cloud);

        if(containerOptional.isPresent() == false){
            throw new IllegalArgumentException(
                    "unable to instantiate container: " + containerName + " from container image: " + this.name);
        }

        return containerOptional.get();
    }
}

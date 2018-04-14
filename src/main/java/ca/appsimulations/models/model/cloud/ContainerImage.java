package ca.appsimulations.models.model.cloud;

import ca.appsimulations.models.model.application.Service;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ca.appsimulations.models.model.cloud.ContainerType.SMALL;

@Builder
@Data
@Accessors(chain = true, fluent = true)
@ToString(of = "name", includeFieldNames = false)
public class ContainerImage {
    private final String name;
    private final Service service;
    private final List<Container> instances = new ArrayList<>();

    public Container instantiate(String containerName, Cloud cloud, ContainerType containerType) {
        Optional<Container> containerOptional = ContainerFactory.build(containerName,
                                                                       this,
                                                                       containerType,
                                                                       cloud);

        if (containerOptional.isPresent() == false) {
            throw new IllegalArgumentException(
                    "unable to instantiate container: " + containerName + " from container image: " + this.name);
        }
        return containerOptional.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        ContainerImage that = (ContainerImage) o;
        return Objects.equals(name, that.name) &&
               Objects.equals(service, that.service);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), name, service);
    }

    public List<Container> instantiate(Cloud cloud,
                                       ContainerType containerType,
                                       int replication) {
        List<Container> newContainers = new ArrayList<>();
        int size = instances.size();
        for (int i = size; i < replication + size; i++) {
            String containerName = "p" + this.name + "_" + containerType.getName() + "_r" + i;
            newContainers.add(instantiate(containerName, cloud, containerType));
        }
        return newContainers;
    }
}

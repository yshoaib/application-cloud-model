package ca.appsimulations.models.model.cloud;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * Created by maverick on 2017-06-10.
 */
@Data
@Accessors(chain = true, fluent = true)
public class Cloud {
    private final String name;
    private final List<ContainerType> containerTypes = new ArrayList<>();
    private final List<Container> containers = new ArrayList<>();
    private final List<ContainerImage> containerImages = new ArrayList<>();

    public boolean hasContainerType(ContainerType containerType) {
        return containerTypes.contains(containerType);
    }

    public Optional<ContainerImage> findContainerImage(String name) {
        Optional<ContainerImage> result = Optional.empty();
        List<ContainerImage> images =
                containerImages.stream().filter(containerImage -> containerImage.name().equals(name)).collect(toList());

        if(images.isEmpty() == false){
            result = Optional.of(images.get(0));
        }

        return result;
    }

    public Container instantiateContainer(String containerName, String containerImageName, ContainerType containerType) {
        Optional<ContainerImage> containerImageOptional = this.findContainerImage(containerImageName);
        if(containerImageOptional.isPresent() == false){
            throw new IllegalArgumentException(
                    "unable to instantiate container image: " + containerName + " because there is no container " +
                    "image: " + containerImageName);
        }
        ContainerImage image = containerImageOptional.get();

        return image.instantiate(containerName, this, containerType);
    }
}

package ca.appsimulations.models.model.cloud;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maverick on 2017-06-10.
 */
@Data
@Accessors(chain = true, fluent = true)
public class Cloud {
    private final String name;
    private final List<ContainerType> containerTypes;
    private final List<Container> containers = new ArrayList<>();
    private final List<ContainerImage> containerImages = new ArrayList<>();

    public boolean hasContainerType(ContainerType aType) {
        return containerTypes.stream().filter(containerType -> containerType.equals(aType)).count() > 0;
    }
}

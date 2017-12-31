package ca.appsimulations.models.model.cloud;

import ca.appsimulations.models.model.application.Service;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Accessors(chain = true, fluent = true)
public class ContainerImage {
    private final String name;
    private final Service service;
    private final ContainerType containerType;
    private final List<Container> instances = new ArrayList<>();
}

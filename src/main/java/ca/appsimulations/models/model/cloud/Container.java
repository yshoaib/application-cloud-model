package ca.appsimulations.models.model.cloud;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by maverick on 2017-06-11.
 */
@Builder
@Data
@Accessors(chain = true, fluent = true)
public class Container {
    private final String name;
    private final ContainerImage containerImage;
}

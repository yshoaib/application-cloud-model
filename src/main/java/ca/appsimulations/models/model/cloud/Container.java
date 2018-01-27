package ca.appsimulations.models.model.cloud;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * Created by maverick on 2017-06-11.
 */
@Builder
@Data
@Accessors(chain = true, fluent = true)
@ToString(of = "name", includeFieldNames = false)
public class Container {
    private final String name;
    private final ContainerImage containerImage;
    private final ContainerType containerType;

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
        Container container = (Container) o;
        return Objects.equals(name, container.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), name);
    }
}

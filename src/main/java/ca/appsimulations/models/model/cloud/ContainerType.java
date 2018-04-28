package ca.appsimulations.models.model.cloud;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Created by maverick on 2017-06-11.
 */
@ToString(of = "name", includeFieldNames = false)
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true, fluent = true)
public class ContainerType {
    public static final ContainerType SM = new ContainerType("small",
                                                             1,
                                                             1,
                                                             0.5);

    public static final ContainerType MD = new ContainerType("medium",
                                                             2,
                                                             1.5,
                                                             8);
    public static final ContainerType LA = new ContainerType("large",
                                                             4,
                                                             2.5,
                                                             15);

    private String name;
    private int cores;
    private double processingRate;
    private double costPerHour;

}

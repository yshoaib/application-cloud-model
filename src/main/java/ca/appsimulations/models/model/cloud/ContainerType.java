package ca.appsimulations.models.model.cloud;

import lombok.ToString;

/**
 * Created by maverick on 2017-06-11.
 */
@ToString(of = "name", includeFieldNames = false)
public enum ContainerType {
    SMALL("small",
          1,
          1,
          0.5),
    MEDIUM("medium",
           2,
           1.5,
           8),
    LARGE("large",
          4,
          2.5,
          15);

    private String name;
    private int cores;
    private double processingRate;
    private double costPerHour;

    ContainerType(String name,
                  int cores,
                  double processingRate,
                  double costPerHour) {
        this.name = name;
        this.cores = cores;
        this.processingRate = processingRate;
        this.costPerHour = costPerHour;
    }

    public static ContainerType fromName(String containerTypeName) {
        for (ContainerType type : ContainerType.values()) {
            if (type.getName().equals(containerTypeName)) {
                return type;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public int getCores() {
        return cores;
    }

    public double getProcessingRate() {
        return processingRate;
    }

    public double getCostPerHour() {
        return costPerHour;
    }
}

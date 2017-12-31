package ca.appsimulations.models.model.application;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true, fluent = true)
public class Call {
    private final String name;
    private final Service source;
    private final Service destination;
    private final ServiceEntry sourceEntry;
    private final ServiceEntry destinationEntry;
    private final double numCalls;

    public Call(String name,
                Service source,
                Service destination,
                ServiceEntry sourceEntry,
                ServiceEntry destinationEntry,
                int numCalls) {
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.sourceEntry = sourceEntry;
        this.destinationEntry = destinationEntry;
        this.numCalls = numCalls;
    }
}

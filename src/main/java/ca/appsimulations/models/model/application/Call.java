package ca.appsimulations.models.model.application;

import lombok.Data;
import lombok.ToString;
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
                double numCalls) {
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.sourceEntry = sourceEntry;
        this.destinationEntry = destinationEntry;
        this.numCalls = numCalls;
    }

    @Override
    public String toString() {
        return "Call{" +
               "name='" + name + '\'' +
               " source=" + source +
               " -->" +
               " destination=" + destination +
               " , " +
               " sourceEntry=" + sourceEntry +
               " -->" +
               " destinationEntry=" + destinationEntry +
               ", numCalls=" + numCalls +
               '}';
    }
}

package ca.appsimulations.models.model.application;

import java.util.Optional;

public class ServiceEntryFactory {

    public static Optional<ServiceEntry> build(String name,
                                               String activityNamePhase1,
                                               Service service,
                                               double serviceDemand) {
        Optional<ServiceEntry> result = Optional.empty();
        ServiceEntry entry =
                ServiceEntry.builder().name(name).activityNamePhase1(activityNamePhase1).service(service).serviceDemand(
                        serviceDemand).build();
        if (service.addEntry(entry)) {
            result = Optional.of(entry);
        }
        return result;
    }
}

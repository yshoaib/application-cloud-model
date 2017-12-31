package ca.appsimulations.models.model.application;


import ca.appsimulations.models.model.cloud.Container;
import ca.appsimulations.models.model.cloud.ContainerImage;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Data
@Builder
@Accessors(chain = true, fluent = true)
public class Service {
    private final String name;
    private final App app;
    private final int threads;
    private final List<Container> containers = new ArrayList<>();
    private final List<ContainerImage> containerImages = new ArrayList<>();
    private final CallResolver callResolver = new CallResolver();
    private final List<ServiceEntry> entries = new ArrayList<>();

    public void calls(String serviceName, Call call) {
        callResolver.calls(serviceName, call);
    }

    public void calledBy(String serviceName, Call call) {
        callResolver.calledBy(serviceName, call);
    }

    public List<Call> calledBy() {
        return callResolver.calledBy();
    }

    public List<Call> callsTo() {
        return callResolver.callsTo();
    }

    public boolean hasEntry(String name) {
        return findServiceEntry(name).isPresent();
    }

    public Optional<ServiceEntry> findServiceEntry(String name) {
        Optional<ServiceEntry> result = Optional.empty();
        List<ServiceEntry> matchingEntries = entries.stream().filter(entry -> entry.name().equals(name)).collect
                (toList());

        if (matchingEntries.size() > 0) {
            result = Optional.of(matchingEntries.get(0));
        }
        return result;
    }

    public boolean addEntry(ServiceEntry entry) {
        boolean entryAdded = false;
        if (this.hasEntry(entry.name()) == false) {
            entryAdded = entries.add(entry);
        }
        return entryAdded;
    }

    public boolean isReference() {
        return calledBy().size() == 0 && callsTo().size() > 0;
    }
}

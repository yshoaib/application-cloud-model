package ca.appsimulations.models.model.application;


import ca.appsimulations.models.model.cloud.Container;
import ca.appsimulations.models.model.cloud.ContainerImage;
import ca.appsimulations.models.model.cloud.ContainerType;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.*;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Data
@Builder
@Accessors(chain = true, fluent = true)
@ToString(of = "name", includeFieldNames = false)
public class Service {
    private final String name;
    private final App app;
    private final int threads;
    @Builder.Default
    private final List<Container> containers = new ArrayList<>();
    @Builder.Default
    private final List<ContainerImage> containerImages = new ArrayList<>();
    @Builder.Default
    private final CallResolver callResolver = new CallResolver();
    @Builder.Default
    private final List<ServiceEntry> entries = new ArrayList<>();
    @Builder.Default
    private final Map<ContainerType, Integer> replicationCountByContainerTypes = new HashMap();

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

    public void addContainer(Container container, ContainerType containerType) {
        this.containers.add(container);
        Integer prevReplicationCount = replicationCountByContainerTypes.get(containerType);
        if (prevReplicationCount == null) {
            this.replicationCountByContainerTypes.put(containerType, 1);
        }
        else {
            this.replicationCountByContainerTypes.put(containerType, prevReplicationCount + 1);
        }
    }

    public boolean isReference() {
        return calledBy().size() == 0 && callsTo().size() > 0;
    }

    public Set<ContainerType> getContainerTypes() {
        return new HashSet<>(replicationCountByContainerTypes.keySet());
    }

    public Integer getReplicationCount(ContainerType containerType) {
        return replicationCountByContainerTypes.get(containerType);
    }

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
        Service service = (Service) o;
        return Objects.equals(name, service.name) &&
               Objects.equals(app, service.app);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), name, app);
    }
}

package ca.appsimulations.models.model.application;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * Created by maverick on 2017-06-10.
 */

@Data
@Accessors(chain = true, fluent = true)
public class App {
    private final String name;
    private double responseTimeObjective;
    private int maxReplicas;
    private List<Service> services = new ArrayList();

    public boolean addService(Service service) {
        boolean serviceAdded = false;
        if (this.hasService(service.name()) == false) {
            serviceAdded = services.add(service);
        }
        return serviceAdded;
    }

    public Optional<Service> findService(String name) {
        Optional<Service> result = Optional.empty();
        List<Service> searchResult = services.stream().filter(service -> service.name().equals(name)).collect(toList());
        if (searchResult.size() > 0) {
            result = Optional.of(searchResult.get(0));
        }
        return result;
    }

    public boolean hasService(String name) {
        return findService(name).isPresent();
    }

    public List<Service> findNonReferenceServices() {
        return services.stream().filter(service -> service.isReference() == false).collect(toList());
    }
}

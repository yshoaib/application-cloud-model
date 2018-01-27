package ca.appsimulations.models.model.application;

import java.util.Optional;

public class AppBuilder {
    private App app;

    public static AppBuilder builder() {
        return new AppBuilder();
    }

    public AppBuilder name(String name) {
        app = new App(name);
        return this;
    }

    public AppBuilder maxReplicas(int maxReplicas) {
        app.maxReplicas(maxReplicas);
        return this;
    }

    public AppBuilder responseTimeObjective(double responseTimeObjective) {
        app.responseTimeObjective(responseTimeObjective);
        return this;
    }

    public App build() {
        return app;
    }

    public ServiceBuilder service(String name, int threads) {
        return ServiceBuilder.builder().appBuilder(this).service(name, app, threads);
    }

    public AppBuilder call(String name, String source, String destination, String sourceEntryName, String
            sourceDestinationName, int numCalls) {
        CallBuilder.build(name, app, source, destination, sourceEntryName, sourceDestinationName, numCalls);
        return this;
    }


    public static class CallBuilder {

        public static Call build(String callName,
                                 App app,
                                 String sourceName,
                                 String destinationName,
                                 String sourceEntryName,
                                 String destinationEntryName,
                                 int numCalls) {
            Optional<Service> sourceOptional = app.findService(sourceName);
            Optional<Service> destinationOptional = app.findService(destinationName);

            if (sourceOptional.isPresent() == false || destinationOptional.isPresent() == false) {
                throw new IllegalArgumentException(
                        "unable to build call because either source and/or destination service(s) don't belong to " +
                        "app");
            }

            Service source = sourceOptional.get();
            Service destination = destinationOptional.get();

            if (source.hasEntry(sourceEntryName) == false || destination.hasEntry(destinationEntryName) == false) {
                throw new IllegalArgumentException(
                        "unable to build call because either source or destination entries don't belong to " +
                        "their respective service(s)");
            }


            Call call = new Call(callName,
                                 source,
                                 destination,
                                 source.findServiceEntry(sourceEntryName).get(),
                                 destination.findServiceEntry(destinationEntryName).get(),
                                 numCalls);
            source.calls(destination.name(), call);
            destination.calledBy(source.name(), call);
            return call;
        }

    }

    public static class ServiceBuilder {
        private Service service;
        private AppBuilder appBuilder;

        public static ServiceBuilder builder() {
            return new ServiceBuilder();
        }

        public ServiceBuilder appBuilder(AppBuilder appBuilder) {
            this.appBuilder = appBuilder;
            return this;
        }

        public ServiceBuilder service(String serviceName, App app, int threads) {
            Service newService = Service.builder().name(serviceName).threads(threads).build();
            if (app.addService(newService)) {
                service = newService;
            }
            else {
                throw new IllegalArgumentException("unable to add service to app. service with same name exists.");
            }
            return this;
        }

        public ServiceBuilder serviceEntry(String name, String activityNamePhase1, double serviceDemand) {
            Optional<ServiceEntry> serviceEntryOptional =
                    ServiceEntryFactory.build(name, activityNamePhase1, service, serviceDemand);
            if (serviceEntryOptional.isPresent() == false) {
                throw new IllegalArgumentException(
                        "unable to add service-entry to service. service-entry with same name " +
                        "exists");
            }
            return this;
        }

        public AppBuilder buildService() {
            return appBuilder;
        }
    }

}



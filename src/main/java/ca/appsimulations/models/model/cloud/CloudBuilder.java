package ca.appsimulations.models.model.cloud;

import ca.appsimulations.models.model.application.App;
import ca.appsimulations.models.model.application.Service;

import java.util.List;
import java.util.Optional;

public class CloudBuilder {

    private Cloud cloud;

    public static CloudBuilder builder(){ return new CloudBuilder(); }

    public Cloud build(){
        return cloud;
    }

    public CloudBuilder name(String name) {
        cloud = new Cloud(name);
        return this;
    }


    public CloudBuilder containerTypes(List<ContainerType> containerTypes) {
        cloud.containerTypes().addAll(containerTypes);
        return this;
    }

    public ContainerImageBuilder containerImage(String name) {
        return ContainerImageBuilder.builder().cloudBuilder(this).imageName(name);

    }

    public static class ContainerImageBuilder {
        private String imageName;
        private Service service;
        private CloudBuilder cloudBuilder;

        public static ContainerImageBuilder builder(){
            return new ContainerImageBuilder();
        }

        public ContainerImageBuilder cloudBuilder(CloudBuilder cloudBuilder){
            this.cloudBuilder = cloudBuilder;
            return this;
        }

        public ContainerImageBuilder imageName(String name){
            this.imageName = name;
            return this;
        }

        public ContainerImageBuilder service(String serviceName, App app) {
            Optional<Service> serviceOptional = app.findService(serviceName);

            if (serviceOptional.isPresent() == false) {
                throw new IllegalArgumentException(
                        "service: " + serviceName + " doesn't exist in application: " + app.name());
            }
            this.service = serviceOptional.get();
            return this;
        }

        public CloudBuilder buildContainerImage() {
            ContainerImageFactory.build(imageName,
                                        service,
                                        cloudBuilder.cloud).get();
            return cloudBuilder;
        }
    }
}

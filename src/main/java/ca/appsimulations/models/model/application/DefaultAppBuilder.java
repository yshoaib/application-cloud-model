package ca.appsimulations.models.model.application;

public class DefaultAppBuilder {

    public static App build(String appName,
                            int maxReplicas,
                            double responseTimeObjective) {
        App app = AppBuilder.builder()
                .name(appName)
                .maxReplicas(maxReplicas)
                .responseTimeObjective(responseTimeObjective)
                .service("Browser", 100)
                .serviceEntry("load", "load_1", 2.0)
                .buildService()
                .service("TaskA", 10)
                .serviceEntry("funcA1", "funcA1_1", 3.0)
                .buildService()
                .service("TaskB", 10)
                .serviceEntry("funcB1", "funcB1_1", 2.0)
                .buildService()
                .service("TaskC", 10)
                .serviceEntry("funcC1", "funcC1_1", 5.0)
                .buildService()
                .service("TaskD", 10)
                .serviceEntry("funcD1", "funcD1_1", 3.0)
                .buildService()
                .call("call1", "Browser", "TaskA", "load", "funcA1", 1)
                .call("call2", "TaskA", "TaskB", "funcA1", "funcB1", 3)
                .call("call3", "TaskB", "TaskC", "funcB1", "funcC1", 1)
                .call("call4", "TaskC", "TaskD", "funcC1", "funcD1", 2)
                .build();

        return app;
    }
}

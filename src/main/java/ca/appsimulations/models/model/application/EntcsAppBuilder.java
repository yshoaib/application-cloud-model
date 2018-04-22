package ca.appsimulations.models.model.application;

import ca.appsimulations.models.model.cloud.Cloud;
import ca.appsimulations.models.model.cloud.CloudBuilder;
import ca.appsimulations.models.model.cloud.ContainerType;

import java.util.Arrays;

public class EntcsAppBuilder {
    public static App build(String appName,
                            int maxReplicas,
                            double responseTimeObjective) {
        App app = AppBuilder.builder().name("app")
                .maxReplicas(10)
                .responseTimeObjective(0.2d)
                .service("Browser", 1)
                .serviceEntry("load", "load_1", 0.0001D, 7.0D)
                .buildService()
                .service("AppServer", 1)
                .serviceEntry("sendHTML", "sendHTML_1", 0.00962D)
                .serviceEntry("sendJS1", "sendJS1_1", 0.00085D)
                .serviceEntry("sendJS2", "sendJS2_1", 0.0048D)
                .serviceEntry("sendJS3", "sendJS3_1", 0.00064D)
                .serviceEntry("viewRoutes", "viewRoutes_1", 0.09555D)
                .serviceEntry("routing1", "routing1_1", 0.21167D)
                .serviceEntry("add1", "add1_1", 0.05342D)
                .serviceEntry("routing2", "routing2_1", 0.18616D)
                .serviceEntry("add2", "add2_1", 0.05343D)
                .buildService()
                .service("DB", 1)
                .serviceEntry("dbViewRoutes", "dbViewRoutes_1", 0.02938D)
                .serviceEntry("dbRouting1", "dbRouting1_1", 0.25272D)
                .serviceEntry("dbAdd1", "dbAdd1_1", 0.00043D)
                .serviceEntry("dbRouting2", "dbRouting2_1", 0.05209D)
                .serviceEntry("dbAdd2", "dbAdd2_1", 0.00041D)
                .buildService()
                .service("Disk", 1)
                .serviceEntry("disk1", "disk1_1", 0.00001D)
                .serviceEntry("disk2", "disk2_1", 0.00001D)
                .serviceEntry("disk3", "disk3_1", 0.00001D)
                .serviceEntry("disk4", "disk4_1", 0.00002D)
                .serviceEntry("disk5", "disk5_1", 0.00006D)
                .serviceEntry("disk6", "disk6_1", 0.00028D)
                .serviceEntry("disk7", "disk7_1", 0.00032D)
                .serviceEntry("disk8", "disk8_1", 0.00023D)
                .serviceEntry("disk9", "disk9_1", 0.00033D)
                .buildService()
                .call("call1", "Browser", "AppServer", "load", "sendHTML", 1)
                .call("call2", "Browser", "AppServer", "load", "sendJS1", 1)
                .call("call3", "Browser", "AppServer", "load", "sendJS2", 1)
                .call("call4", "Browser", "AppServer", "load", "sendJS3", 1)
                .call("call5", "Browser", "AppServer", "load", "viewRoutes", 1)
                .call("call6", "Browser", "AppServer", "load", "routing1", 1)
                .call("call7", "Browser", "AppServer", "load", "add1", 1)
                .call("call8", "Browser", "AppServer", "load", "routing2", 1)
                .call("call9", "Browser", "AppServer", "load", "add2", 1)
                .call("call10", "AppServer", "DB", "viewRoutes", "dbViewRoutes", 1)
                .call("call11", "AppServer", "DB", "routing1", "dbRouting1", 1)
                .call("call12", "AppServer", "DB", "add1", "dbAdd1", 1)
                .call("call13", "AppServer", "DB", "routing2", "dbRouting2", 1)
                .call("call14", "AppServer", "DB", "add2", "dbAdd2", 1)
                .call("call15", "AppServer", "Disk", "sendHTML", "disk1", 1)
                .call("call16", "AppServer", "Disk", "sendJS1", "disk2", 1)
                .call("call17", "AppServer", "Disk", "sendJS2", "disk3", 1)
                .call("call18", "AppServer", "Disk", "sendJS3", "disk4", 1)
                .call("call19", "DB", "Disk", "dbViewRoutes", "disk5", 1)
                .call("call20", "DB", "Disk", "dbRouting1", "disk6", 1)
                .call("call21", "DB", "Disk", "dbAdd1", "disk7", 1)
                .call("call22", "DB", "Disk", "dbRouting2", "disk8", 1)
                .call("call23", "DB", "Disk", "dbAdd2", "disk9", 1).build();
        return app;
    }
}

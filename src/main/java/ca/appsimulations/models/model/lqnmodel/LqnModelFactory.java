package ca.appsimulations.models.model.lqnmodel;

import ca.appsimulations.jlqninterface.lqn.entities.Entry;
import ca.appsimulations.jlqninterface.lqn.entities.Processor;
import ca.appsimulations.jlqninterface.lqn.entities.Task;
import ca.appsimulations.jlqninterface.lqn.model.LqnModel;
import ca.appsimulations.jlqninterface.lqn.model.LqnXmlDetails;
import ca.appsimulations.jlqninterface.lqn.model.SolverParams;
import ca.appsimulations.jlqninterface.lqn.model.factory.EntryFactory;
import ca.appsimulations.jlqninterface.lqn.model.factory.ProcessorFactory;
import ca.appsimulations.jlqninterface.lqn.model.factory.SyncCallFactory;
import ca.appsimulations.jlqninterface.lqn.model.factory.TaskFactory;
import ca.appsimulations.models.model.application.App;
import ca.appsimulations.models.model.application.Call;
import com.rits.cloning.Cloner;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by maverick on 2017-07-09.
 */
public class LqnModelFactory {

    private static final double CONVERGENCE = 0.01;
    private static final int ITERATION_LIMIT = 50_000;
    private static final double UNDER_RELAX_COEFF = 0.9;
    private static final int PRINT_INTERVAL = 1;
    private static final String XML_NS_URL = "http://www.w3.org/2001/XMLSchema-instance";
    private static final String SCHEMA_LOCATION = "lqn.xsd";
    private static final String XML_NAME = "lqn-model";
    private static final String XML_DESCRIPTION = "lqn model";
    private static final String COMMENT = "lqn model";


    private Cloner cloner;

    public LqnModelFactory(Cloner cloner) {
        this.cloner = cloner;
    }

    public static LqnModel build(App app,
                                 LqnXmlDetails xmlDetails,
                                 SolverParams solverParams) {
        LqnModel lqnModel = new LqnModel();
        lqnModel.xmlDetails(xmlDetails);
        lqnModel.solverParams(solverParams);

        // first build all the tasks from the services
        // no entries and no calls between the tasks are built yet.
        app.services()
                .stream()
                .forEach(service -> {
                    service
                            .containers()
                            .stream()
                            .forEach(container ->
                                     {

                                         Processor processor = ProcessorFactory.build(lqnModel,
                                                                                      container.name(),
                                                                                      service.isReference(),
                                                                                      container.containerImage()
                                                                                              .containerType()
                                                                                              .getCores());


                                         Task task = TaskFactory.build(service.name(),
                                                                       lqnModel,
                                                                       processor,
                                                                       service.isReference(),
                                                                       service.threads());

                                         service.entries().stream().forEach(serviceEntry -> {
                                             EntryFactory.build(serviceEntry.name(),
                                                                serviceEntry.activityNamePhase1(),
                                                                lqnModel,
                                                                task,
                                                                serviceEntry.serviceDemand());
                                         });
                                     });

                });


        List<Call> allCalls =
                app.services().stream().flatMap(service -> service.callsTo().stream()).collect(Collectors.toList());
        allCalls.stream().forEach(call -> {
            Entry sourceEntry = lqnModel.entryByName(call.sourceEntry().name());
            Entry destEntry = lqnModel.entryByName(call.destinationEntry().name());


            SyncCallFactory.build(lqnModel, sourceEntry, destEntry, call.numCalls());
        });

        lqnModel.buildRefTasksFromExistingTasks();
        return lqnModel;
    }

    public LqnModel buildNewModelFromModelTemplate(List<Integer> variables,
                                                   Map taskContainerTypeMap,
                                                   LqnModel modelTemplate) {
        // need to do a deep copy of the model template so that the original is
        // not changed.
        LqnModel lqnModel = cloner.deepClone(modelTemplate);

        //need duplication here. may note require cloning.
        // for each task in the lqn model attach a Container-type processor.


        // replicate the tasks based on the variables of the solution.
        return lqnModel;
    }


    public static SolverParams buildDefaultSolverParams() {
        return SolverParams
                .builder()
                .comment(COMMENT)
                .convergence(CONVERGENCE)
                .iterationLimit(ITERATION_LIMIT)
                .underRelaxCoeff(UNDER_RELAX_COEFF)
                .printInterval(PRINT_INTERVAL)
                .build();
    }

    public static LqnXmlDetails buildDefaultLqnXmlDetails() {
        return LqnXmlDetails
                .builder()
                .name(XML_NAME)
                .xmlnsXsi(XML_NS_URL)
                .description(XML_DESCRIPTION)
                .schemaLocation(SCHEMA_LOCATION)
                .build();
    }

}

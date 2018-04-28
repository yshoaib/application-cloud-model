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
import ca.appsimulations.models.model.application.Service;
import ca.appsimulations.models.model.application.ServiceEntry;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import lombok.Builder;
import lombok.Data;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toList;

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

    public static LqnModel build(App app,
                                 LqnXmlDetails xmlDetails,
                                 SolverParams solverParams) {
        LqnModel lqnModel = new LqnModel();
        lqnModel.xmlDetails(xmlDetails);
        lqnModel.solverParams(solverParams);

        CloudAppLqnMap cloudAppLqnMap = new CloudAppLqnMap();

        // first build all the tasks from the services
        // no entries and no calls between the tasks are built yet.
        app.services()
                .stream()
                .forEach(service -> {
                    AtomicInteger taskId = new AtomicInteger(1);
                    service.containers()
                            .stream()
                            .forEach(container ->
                                     {
                                         Processor processor = ProcessorFactory.build(lqnModel,
                                                                                      container.name(),
                                                                                      service.isReference(),
                                                                                      container.containerType().cores());
                                         cloudAppLqnMap.add(service, processor);

                                         Task task = TaskFactory.build(service.name() + "_" + taskId,
                                                                       lqnModel,
                                                                       processor,
                                                                       service.isReference(),
                                                                       service.threads());

                                         taskId.getAndIncrement();
                                         cloudAppLqnMap.add(service, task);
                                     });
                });


        Queue<Service> serviceQueue = new LinkedList<>();
        serviceQueue.addAll(app.findReferenceServices());

        while (serviceQueue.isEmpty() == false) {
            Service sourceService = serviceQueue.remove();
            List<Call> calls = sourceService.callsTo();

            calls.forEach(call -> {
                ServiceEntry sourceEntry = call.sourceEntry();
                ServiceEntry destEntry = call.destinationEntry();

                Service destService = destEntry.service();

                addUniqueElementToQueue(serviceQueue, destService);

                Collection<Task> sourceTasks = cloudAppLqnMap.getTasks(sourceService);
                Collection<Task> destTasks = cloudAppLqnMap.getTasks(destService);

                AtomicInteger sourceEntryId = new AtomicInteger(1);
                List<Entry> sourceLqnEntries = sourceTasks
                        .stream()
                        .map(sourceTask -> buildLqnEntry(lqnModel,
                                                         cloudAppLqnMap,
                                                         sourceService,
                                                         sourceEntryId.getAndIncrement(),
                                                         sourceTask,
                                                         sourceEntry)
                        ).collect(toList());

                AtomicInteger destEntryId = new AtomicInteger(1);
                AtomicInteger sumProcessorMultiplicity = new AtomicInteger();
                List<Entry> destLqnEntries = destTasks
                        .stream()
                        .map(destTask -> {
                                 Entry entry = buildLqnEntry(lqnModel,
                                                             cloudAppLqnMap,
                                                             destService,
                                                             destEntryId.getAndIncrement(),
                                                             destTask,
                                                             destEntry);
                                 cloudAppLqnMap.add(entry, destTask.getProcessor().getMultiplicity());
                                 sumProcessorMultiplicity.addAndGet(destTask.getProcessor().getMultiplicity());
                                 return entry;
                             }
                        ).collect(toList());

                sourceLqnEntries.forEach(lqnSourceEntry -> {
                    destLqnEntries.forEach(lqnDestEntry -> {
                        double numCalls =
                                Math.round(
                                        cloudAppLqnMap.getProcessorMultiplicity(lqnDestEntry) * call.numCalls() * 1.0 /
                                        sumProcessorMultiplicity.get() * 100.00) / 100.00;
                        SyncCallFactory.build(lqnModel, lqnSourceEntry, lqnDestEntry, numCalls);
                    });
                });
            });
        }

        lqnModel.buildRefTasksFromExistingTasks();
        return lqnModel;
    }

    private static void addUniqueElementToQueue(Queue<Service> serviceQueue, Service destService) {
        boolean unique = true;
        for (Service service : serviceQueue) {
            if (service.equals(destService)) {
                unique = false;
            }
        }
        if (unique) {
            serviceQueue.add(destService);
        }
    }


    public static LqnModel buildWithLqnReplication(App app,
                                                   LqnXmlDetails xmlDetails,
                                                   SolverParams solverParams) {
        LqnModel lqnModel = new LqnModel();
        lqnModel.xmlDetails(xmlDetails);
        lqnModel.solverParams(solverParams);

        CloudAppLqnMap cloudAppLqnMap = new CloudAppLqnMap();

        // first build all the tasks from the services
        // no entries and no calls between the tasks are built yet.
        app.services()
                .stream()
                .forEach(service -> {
                    AtomicInteger taskId = new AtomicInteger(1);
                    service.getContainerTypes().stream().forEach(containerType -> {
                        String name = "p" + service.name() + "_" + containerType.name();

                        Integer replicationCount = service.getReplicationCount(containerType);

                        Processor processor = ProcessorFactory.build(lqnModel,
                                                                     name,
                                                                     service.isReference(),
                                                                     containerType.cores(),
                                                                     replicationCount);
                        cloudAppLqnMap.add(service, processor);

                        Task task = TaskFactory.build(service.name() + "_" + taskId,
                                                      lqnModel,
                                                      processor,
                                                      service.isReference(),
                                                      service.threads(),
                                                      replicationCount);
                        taskId.getAndIncrement();
                        cloudAppLqnMap.add(service, task);
                    });
                });


        Queue<Service> serviceQueue = new LinkedList<>();
        serviceQueue.addAll(app.findReferenceServices());

        while (serviceQueue.isEmpty() == false) {
            Service sourceService = serviceQueue.remove();
            List<Call> calls = sourceService.callsTo();

            calls.forEach(call -> {
                ServiceEntry sourceEntry = call.sourceEntry();
                ServiceEntry destEntry = call.destinationEntry();

                Service destService = destEntry.service();

                addUniqueElementToQueue(serviceQueue, destService);

                Collection<Task> sourceTasks = cloudAppLqnMap.getTasks(sourceService);
                Collection<Task> destTasks = cloudAppLqnMap.getTasks(destService);

                addFanIn(sourceTasks, destTasks);

                AtomicInteger sourceEntryId = new AtomicInteger(1);
                List<Entry> sourceLqnEntries = sourceTasks
                        .stream()
                        .map(sourceTask -> buildLqnEntry(lqnModel,
                                                         cloudAppLqnMap,
                                                         sourceService,
                                                         sourceEntryId.getAndIncrement(),
                                                         sourceTask,
                                                         sourceEntry)
                        ).collect(toList());

                AtomicInteger destEntryId = new AtomicInteger(1);
                AtomicInteger sumProcessorMultiplicity = new AtomicInteger();
                List<Entry> destLqnEntries = destTasks
                        .stream()
                        .map(destTask -> {
                                 Entry entry = buildLqnEntry(lqnModel,
                                                             cloudAppLqnMap,
                                                             destService,
                                                             destEntryId.getAndIncrement(),
                                                             destTask,
                                                             destEntry);
                                 cloudAppLqnMap.add(entry, destTask.getProcessor().getMultiplicity());
                                 sumProcessorMultiplicity.addAndGet(destTask.getProcessor().getMultiplicity() *
                                                                    destTask.getProcessor().getReplication());
                                 return entry;
                             }
                        ).collect(toList());

                sourceLqnEntries.forEach(lqnSourceEntry -> {
                    destLqnEntries.forEach(lqnDestEntry -> {

                        double numCalls =
                                Math.round(
                                        cloudAppLqnMap.getProcessorMultiplicity(lqnDestEntry) * call.numCalls() * 1.0 /
                                        sumProcessorMultiplicity.get() * 100.00) / 100.00;
                        SyncCallFactory.build(lqnModel, lqnSourceEntry, lqnDestEntry, numCalls);
                    });
                });
            });
        }

        lqnModel.buildRefTasksFromExistingTasks();
        return lqnModel;
    }

    private static void addFanIn(Collection<Task> sourceTasks, Collection<Task> destTasks) {
        sourceTasks.stream().forEach(src -> {
            destTasks.stream().forEach(dest -> {
                src.adddFanOut(dest, dest.getReplication());
                dest.adddFanIn(src, src.getReplication());
            });
        });
    }

    private static Entry buildLqnEntry(LqnModel lqnModel,
                                       CloudAppLqnMap cloudAppLqnMap,
                                       Service service,
                                       int entryId, Task task, ServiceEntry serviceEntry) {
        String entryName = serviceEntry.name() + "_" + entryId;
        Entry lqnEntry = lqnModel.entryByName(entryName);
        if (lqnEntry == null) {
            lqnEntry = EntryFactory.build(entryName,
                                          serviceEntry.activityNamePhase1(),
                                          lqnModel,
                                          task,
                                          serviceEntry.serviceDemand(),
                                          serviceEntry.thinkTime());

        }
        cloudAppLqnMap.add(service, lqnEntry);
        return lqnEntry;
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


@Builder
@Data
class CloudAppLqnMap {
    private final Multimap<Service, Processor> serviceProcessorMap = ArrayListMultimap.create();
    private final Multimap<Service, Task> serviceTaskMap = ArrayListMultimap.create();
    private final Multimap<Service, Entry> serviceEntryMap = ArrayListMultimap.create();
    private final Map<Entry, Integer> entryProcessorMultiplicityMap = Maps.newHashMap();

    public void add(Service service, Processor processor) {
        serviceProcessorMap.put(service, processor);
    }

    public void add(Service service, Task task) {
        serviceTaskMap.put(service, task);
    }

    public void add(Service service, Entry entry) {
        serviceEntryMap.put(service, entry);
    }

    public Collection<Task> getTasks(Service service) {
        return serviceTaskMap.get(service);
    }

    public Collection<Entry> getEntries(Service service) {
        return serviceEntryMap.get(service);
    }

    public void add(Entry entry, int multiplicity) {
        entryProcessorMultiplicityMap.put(entry, multiplicity);
    }

    public int getProcessorMultiplicity(Entry entry) {
        return entryProcessorMultiplicityMap.get(entry);
    }
}

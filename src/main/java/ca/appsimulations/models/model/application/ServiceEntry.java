package ca.appsimulations.models.model.application;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Accessors(chain = true, fluent = true)
@ToString(of = "name", includeFieldNames = false)
public class ServiceEntry {
    private final String name;
    private final String activityNamePhase1;
    private final Service service;
    private final double serviceDemand;
    private double thinkTime = 0.0;
    private final CallResolver callResolver = new CallResolver();

    public void calls(String entryName, Call call) {
        callResolver.calls(entryName, call);
    }

    public void calledBy(String entryName, Call call) {
        callResolver.calledBy(entryName, call);
    }

    public List<Call> calledBy() {
        return callResolver.calledBy();
    }

    public List<Call> callsTo() {
        return callResolver.callsTo();
    }

}

package ca.appsimulations.models.model.application;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Data
@NoArgsConstructor
@Accessors(chain = true, fluent = true)
public class CallResolver {

    private Map<String, List<Call>> callsToMap = new HashMap<>();
    private Map<String, List<Call>> calledByMap = new HashMap<>();

    public void calls(String name, Call call) {
        addCallToMap(name, call, callsToMap);
    }

    public void calledBy(String name, Call call) {
        addCallToMap(name, call, calledByMap);
    }

    private void addCallToMap(String name,
                              Call call,
                              Map<String, List<Call>> callsMap) {
        List<Call> calls;
        if (callsMap.containsKey(name)) {
            calls = callsMap.get(name);
        }
        else {
            calls = new ArrayList<>();
        }
        calls.add(call);
        callsMap.put(name,
                     calls);
    }

    public List<Call> calledBy() {
        return calledByMap.values().stream().flatMap(calls -> calls.stream())
                .collect(toList());
    }

    public List<Call> callsTo() {
        return callsToMap.values().stream().flatMap(calls -> calls.stream())
                .collect(toList());
    }
}

package ie.tud.msc.disertation.domain;

import lombok.Data;

import java.util.List;

@Data
public class ResultList {
    private int swarmId;
    private String machineIpAddress;
    private List<Result> results;
}

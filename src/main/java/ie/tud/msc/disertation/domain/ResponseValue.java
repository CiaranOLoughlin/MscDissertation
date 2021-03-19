package ie.tud.msc.disertation.domain;

import lombok.Data;

import java.util.List;

@Data
public class ResponseValue {
    private double finalGroupBest;
    private double averageBestX;
    private double averageBestY;
    private int iterations;
    private long timeToSolution;
    private List<ResultList> resultsList;
}

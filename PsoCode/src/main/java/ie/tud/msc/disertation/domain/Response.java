package ie.tud.msc.disertation.domain;

import ie.tud.msc.disertation.domain.ResultList;
import lombok.Data;

import java.util.List;

@Data
public class Response {
    private double finalGroupBest;
    private double averageBestX;
    private double averageBestY;
    private int iterations;
    private long timeToSolution;
    private List<ResultList> resultsList;
}

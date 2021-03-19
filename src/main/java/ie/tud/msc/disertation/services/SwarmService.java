package ie.tud.msc.disertation.services;

import ie.tud.msc.disertation.function.*;
import ie.tud.msc.disertation.domain.ResponseValue;
import ie.tud.msc.disertation.domain.Result;
import ie.tud.msc.disertation.domain.ResultList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SwarmService {
    private static final int TARGET_NUMBER_OF_EXACT_ANSWERS = 20;
    private static final int MAX_NUMBER_OF_STEPS = 100000;

    public ResponseValue runMultiSwarm(int numberOfSwarms, int numberOfParticles, double inertia, double cognitive, double social, FunctionType functionType) {
        Instant start = Instant.now();
        log.info("Running multi swarm method. Number of swarms: {}, Number of particles: {}", numberOfSwarms, numberOfParticles);
        List<Swarm> multiSwarm = new ArrayList<>();
        boolean killSwitch = true;
        int stepCount = 0;
        List<Double> settledList = new ArrayList<>();
        List<ResultList> multiSwarmResults = this.createMultiSwarmResultSet(numberOfSwarms);

        for(int i = 0; i<numberOfSwarms; i++) {
            log.info("Initialising swarm id {} with the following params. " +
                    "Inertia: {}, Cognitive: {}, Social: {}. Function type {}", i, inertia, cognitive, social, functionType);
            Function function;

            if(functionType.equals(FunctionType.BOOTHS_FUNCTION)){
                function = new BoothsFunction();
            } else if (functionType.equals(FunctionType.PARABOLA)){
                function = new ParabolaFunction();
            } else {
                function = new DefaultFunction();
            }
            Swarm swarm = new Swarm(function, numberOfParticles, inertia, cognitive, social);
            multiSwarm.add(swarm);
        }

        while(killSwitch) {
            double[] bestEvaluation = new double[numberOfSwarms];
            for(int swarmId = 0; swarmId<numberOfSwarms; swarmId++) {
                Result result = runSwarmIteration(multiSwarm.get(swarmId), stepCount, inertia, cognitive, social);
                multiSwarmResults.get(swarmId).getResults().add(result);
                bestEvaluation[swarmId] = result.getBest();
            }

            for(int swarmId = 0; swarmId<numberOfSwarms; swarmId++) {
                if (settledList.size() == 0) {
                    settledList.add(bestEvaluation[swarmId]);
                } else {
                    if (settledList.contains(bestEvaluation[swarmId])) {
                        settledList.add(bestEvaluation[swarmId]);
                    } else {
                        settledList = new ArrayList<>();
                    }
                }

                if (settledList.size() == TARGET_NUMBER_OF_EXACT_ANSWERS) {
                    log.info("Found the same answer {} times. Returning from multi swarm method.", TARGET_NUMBER_OF_EXACT_ANSWERS);
                    killSwitch = false;
                }
            }
            if(stepCount == MAX_NUMBER_OF_STEPS){
                killSwitch =false;
            }

            stepCount++;
        }
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        log.info("Time elapsed running swarms was: {}ms", timeElapsed);

        return createResponse(multiSwarmResults, stepCount, timeElapsed, numberOfSwarms);
    }

    private static Result runSwarmIteration(Swarm swarm, int stepCount, double inertia, double cognitive, double social) {
        return swarm.runSwarm(stepCount);
    }

    private ResponseValue createResponse(List<ResultList> resultLists, int stepCount, long timeToSolution, int numberOfSwarms){
        ResponseValue responseValue = new ResponseValue();

        responseValue.setIterations(stepCount);
        responseValue.setFinalGroupBest(resultLists.get(0).getResults().get(stepCount-1).getBest());
        responseValue.setResultsList(resultLists);
        responseValue.setTimeToSolution(timeToSolution);

        double sumOfXValues = 0;
        double sumOfYValues = 0;
        for(int swarmId = 0; swarmId<numberOfSwarms; swarmId++) {
            sumOfXValues += resultLists.get(swarmId).getResults().get(stepCount-1).getBestPositionX();
            sumOfYValues += resultLists.get(swarmId).getResults().get(stepCount-1).getBestPositionY();
        }

        responseValue.setAverageBestX(sumOfXValues/numberOfSwarms);
        responseValue.setAverageBestY(sumOfYValues/numberOfSwarms);

        return responseValue;
    }

    private List<ResultList> createMultiSwarmResultSet(int numberOfSwarms){
        List<ResultList> multiSwarmResults = new ArrayList<>();

        for(int j = 0; j<numberOfSwarms; j++) {
            ResultList resultList = new ResultList();
            resultList.setSwarmId(j);
            resultList.setMachineIpAddress(getIpAddress());
            List<Result> results = new ArrayList<>();
            resultList.setResults(results);
            multiSwarmResults.add(resultList);
        }
        return multiSwarmResults;
    }

    private String getIpAddress() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        } catch (Exception ex) {
            log.error("Unable to get ip address. Exception details are:", ex);
            return null;
        }
    }
}

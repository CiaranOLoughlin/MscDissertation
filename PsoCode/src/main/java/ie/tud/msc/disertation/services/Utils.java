package ie.tud.msc.disertation.services;

import ie.tud.msc.disertation.domain.ConfigVariables;
import ie.tud.msc.disertation.domain.Response;
import ie.tud.msc.disertation.domain.Result;
import ie.tud.msc.disertation.domain.ResultList;
import ie.tud.msc.disertation.function.Function;
import ie.tud.msc.disertation.function.FunctionType;
import ie.tud.msc.disertation.function.implementations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class Utils {

    public Response createResponse(List<ResultList> resultLists, int stepCount, long timeToSolution, int numberOfSwarms){
        Response responseValue = new Response();

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

    public List<ResultList> createMultiSwarmResultSet(int numberOfSwarms, List<String> ipAddress) {
        List<ResultList> multiSwarmResults = new ArrayList<>();

        for(int swarmId = 0; swarmId<numberOfSwarms; swarmId++) {
            ResultList resultList = new ResultList();
            resultList.setSwarmId(swarmId);
            if(ipAddress.size()!=1) {
                resultList.setMachineIpAddress(ipAddress.get(swarmId));
            } else {
                resultList.setMachineIpAddress(ipAddress.get(0));
            }
            List<Result> results = new ArrayList<>();
            resultList.setResults(results);
            multiSwarmResults.add(resultList);
        }
        return multiSwarmResults;
    }

    public Swarm createSwarm(int swarmId, int numberOfParticles, ConfigVariables configVariables) throws Exception {
        log.info("Initialising swarm id {} with the following params. {}", swarmId, configVariables);
        Function function;

        if(configVariables.getFunctionType().equals(FunctionType.BOOTHS_FUNCTION)){
            function = new BoothsFunction();
        } else if (configVariables.getFunctionType().equals(FunctionType.THREE_HUMP_CAMEL)){
            function = new ThreeHumpCamel();
        } else if(configVariables.getFunctionType().equals(FunctionType.BEALE)) {
            function = new BealeFunction();
        } else if(configVariables.getFunctionType().equals(FunctionType.EASOM)) {
            function = new EasomFunction();
        } else {
            throw new Exception("Not a valid function dipshit");
        }
        return new Swarm(numberOfParticles, configVariables, function);
    }

    public static Result runSwarmIteration(Swarm swarm, int stepCount, double globalNeighbourhoodBest) {
        return swarm.runSwarm(stepCount, globalNeighbourhoodBest);
    }
}

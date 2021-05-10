package ie.tud.msc.disertation.services;

import ie.tud.msc.disertation.domain.ConfigVariables;
import ie.tud.msc.disertation.domain.Result;
import ie.tud.msc.disertation.domain.ResultList;
import ie.tud.msc.disertation.domain.distributed.DistributedRequest;
import ie.tud.msc.disertation.domain.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DistibutedService {
    private Swarm initialisedSwarm;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private Utils utils;

    private static final int TARGET_NUMBER_OF_EXACT_ANSWERS = 20;
    private static final int MAX_NUMBER_OF_STEPS = 100000;

    public Response runDistributedCallingSystem(int numberOfParticles, List<String> baseUrls, ConfigVariables configVariables) throws Exception {
        Instant start = Instant.now();
        List<Boolean> successFlags = new ArrayList<>();
        int numberOfSwarms = baseUrls.size();

        for(int swarmId=0; swarmId < numberOfSwarms; swarmId++){
            DistributedRequest distributedRequest = new DistributedRequest();
            distributedRequest.setNumberOfParticles(numberOfParticles);
            distributedRequest.setSwarmId(swarmId);
            distributedRequest.setConfigVariables(configVariables);
            successFlags.add(callInitialiseSwarms(swarmId, baseUrls.get(swarmId), distributedRequest));
        }
        if(successFlags.contains(false)){
            throw new RuntimeException("Exception initialising swarms. Check logs.");
        }

        boolean targetFound = false;
        int stepCount = 0;
        List<Double> settledList = new ArrayList<>();
        List<ResultList> multiSwarmResults = utils.createMultiSwarmResultSet(numberOfSwarms, baseUrls);

        while(!targetFound) {
            double[] bestEvaluation = new double[numberOfSwarms];
            for(int swarmId = 0; swarmId<numberOfSwarms; swarmId++) {
                DistributedRequest distributedRequest = new DistributedRequest();
                distributedRequest.setNumberOfParticles(numberOfParticles);
                distributedRequest.setSwarmId(swarmId);
                distributedRequest.setConfigVariables(configVariables);
                Result result = callRunSwarm(swarmId, baseUrls.get(swarmId) ,distributedRequest);
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
                    targetFound = true;
                }
            }
            if(stepCount == MAX_NUMBER_OF_STEPS){
                targetFound = true;
            }

            stepCount++;
        }

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        log.info("Time elapsed running swarms was: {}ms", timeElapsed);

        return utils.createResponse(multiSwarmResults, stepCount, timeElapsed, numberOfSwarms);
    }


    private Boolean callInitialiseSwarms(int swarmId, String baseUrl, DistributedRequest distributedRequest) {
        String fullUrl = "http://" + baseUrl + ":8080/swarm/initialiseSwarm";
        log.info("Calling endpoint {} to initialise swarm", fullUrl);
        HttpEntity<DistributedRequest> request = new HttpEntity<>(distributedRequest);
        try {
            ResponseEntity<Boolean> responseEntity = restTemplate.exchange(fullUrl, HttpMethod.POST, request, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception ex) {
            log.error("Exception thrown calling swarm with id {}, trying to initialise swarm", swarmId, ex);
            return false;
        }
    }

    private Result callRunSwarm(int swarmId, String baseUrl, DistributedRequest distributedRequest) throws Exception {
        String fullUrl = "http://" + baseUrl + ":8080/swarm/runSwarmIteration";
        log.info("Calling endpoint {} to run swarm with id {}", fullUrl, swarmId);
        HttpEntity<DistributedRequest> request = new HttpEntity<>(distributedRequest);
        try {
            ResponseEntity<Result> responseEntity = restTemplate.exchange(fullUrl, HttpMethod.POST, request, Result.class);
            return responseEntity.getBody();
        } catch (Exception ex) {
            log.error("Exception thrown calling swarm with id {}, trying to initialise swarm", swarmId, ex);
            throw new Exception("Error calling service to iterate swarm", ex);
        }
    }

    public boolean setInitialisedSwarm(int swarmId, int numberOfParticles, ConfigVariables configVariables) throws Exception {
        this.initialisedSwarm = utils.createSwarm(swarmId,numberOfParticles, configVariables);
        return true;
    }


    public Result runSwarmIteration(int stepCount) {
        return utils.runSwarmIteration(initialisedSwarm, stepCount);
    }
}

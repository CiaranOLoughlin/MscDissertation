package ie.tud.msc.disertation.services;

import ie.tud.msc.disertation.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CentralisedService {
    private static final int TARGET_NUMBER_OF_EXACT_ANSWERS = 20;
    private static final int MAX_NUMBER_OF_STEPS = 10000;

    @Autowired
    Utils utils;

    public Response runSwarm(Request request) throws Exception {
        Instant start = Instant.now();
        log.info("Running centralised swarm method. Number of swarms: {}, Number of particles: {}", request.getNumberOfSwarms(), request.getNumberOfParticles());
        List<Swarm> multiSwarm = new ArrayList<>();
        boolean targetFound = false;
        int stepCount = 0;
        List<Double> settledList = new ArrayList<>();
        List<ResultList> multiSwarmResults = utils.createMultiSwarmResultSet(request.getNumberOfSwarms(), request.getBaseUrls());
        /* Initialize all swarms and particle with supplied configuration */
        for(int swarmId = 0; swarmId<request.getNumberOfSwarms(); swarmId++) {
            multiSwarm.add(utils.createSwarm(swarmId,
                    request.getNumberOfParticles(),
                    request.getConfigVariables()));
        }

        while(!targetFound) {
            double[] bestEvaluation = new double[request.getNumberOfSwarms()];
            for(int swarmId = 0; swarmId<request.getNumberOfSwarms(); swarmId++) {
                Result result = utils.runSwarmIteration(multiSwarm.get(swarmId), stepCount);
                multiSwarmResults.get(swarmId).getResults().add(result);
                bestEvaluation[swarmId] = result.getBest();
            }

            for(int swarmId = 0; swarmId<request.getNumberOfSwarms(); swarmId++) {
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
                targetFound =true;
            }

            stepCount++;
        }
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        log.info("Time elapsed running swarms was: {}ms", timeElapsed);

        return utils.createResponse(multiSwarmResults, stepCount, timeElapsed, request.getNumberOfSwarms());
    }

}

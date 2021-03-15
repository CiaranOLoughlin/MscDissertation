package ie.tud.msc.disertation.services;

import ie.tud.msc.disertation.function.ParabolaFunction;
import ie.tud.msc.disertation.swarm.SwarmImplementation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SwarmService {
    private List<Double> settledList = new ArrayList<>();
    public SwarmService() {}

    public void runSwarm(int numberOfParticles, double targetValue) {
        SwarmImplementation swarmImplementation = new SwarmImplementation(new ParabolaFunction(), 0.3,0.3);
        swarmImplementation.createSwarm(numberOfParticles);

        boolean killSwitch = true;
        int stepCount = 1;

        while(killSwitch) {
            swarmImplementation.updateSwarm(stepCount);
            stepCount++;
            double best = swarmImplementation.getResultsList().getResults().get(swarmImplementation.getResultsList().getResults().size()-1).getBest();
            System.out.println(swarmImplementation.getResultsList().getResults().get(swarmImplementation.getResultsList().getResults().size()-1));
            if(settledList.size()==0){
                settledList.add(best);
            } else {
                if(settledList.contains(best)){
                    settledList.add(best);
                } else {
                    settledList = new ArrayList<>();
                }
            }

            if(settledList.size()==20){
                killSwitch=false;
            }
        }
        System.out.println(swarmImplementation.getResultsList().getResults().get(swarmImplementation.getResultsList().getResults().size()-1));
    }
}

package ie.tud.msc.disertation.controllers;

import ie.tud.msc.disertation.domain.Result;
import ie.tud.msc.disertation.domain.distributed.DistributedRequest;
import ie.tud.msc.disertation.domain.Response;
import ie.tud.msc.disertation.services.DistibutedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DistributedController {
    @Autowired
    private DistibutedService distibutedService;

    @PostMapping("/initialiseSwarm")
    public Boolean initialiseSwarm(@RequestBody DistributedRequest request) throws Exception {
        return distibutedService.setInitialisedSwarm(request.getSwarmId(), request.getNumberOfParticles(), request.getConfigVariables());
    }

    @PostMapping("/runSwarmIteration")
    public Result runSwarmIteration(@RequestBody DistributedRequest request) {
        return distibutedService.runSwarmIteration(request.getEpoch());
    }

    @PostMapping("/distributedImplementation")
    public Response distributedImplementation(@RequestBody DistributedRequest request) {
        return distibutedService.runDistributedCallingSystem(request.getNumberOfParticles(), request.getBaseUrls(), request.getConfigVariables());
    }
}


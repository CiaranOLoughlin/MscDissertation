package ie.tud.msc.disertation.controllers;

import ie.tud.msc.disertation.domain.Request;
import ie.tud.msc.disertation.domain.ResponseValue;
import ie.tud.msc.disertation.services.SwarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SwarmController {
    @Autowired
    private SwarmService swarmService;

    @PostMapping("/runSwarm")
    public ResponseValue runNonDistributedSwarm(@RequestBody Request request) {
        return swarmService.runMultiSwarm(request.getNumberOfSwarms(), request.getNumberOfParticles(),
                request.getInertia(), request.getCognitive(), request.getSocial(), request.getFunctionType());
    }
}

package ie.tud.msc.disertation.controllers;

import ie.tud.msc.disertation.domain.Request;
import ie.tud.msc.disertation.domain.Response;
import ie.tud.msc.disertation.services.CentralisedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SwarmController {
    @Autowired
    private CentralisedService centralisedService;

    @PostMapping("/runSwarm")
    public Response runCentralisedSwarm(@RequestBody Request request) throws Exception {
        return centralisedService.runSwarm(request);
    }
}

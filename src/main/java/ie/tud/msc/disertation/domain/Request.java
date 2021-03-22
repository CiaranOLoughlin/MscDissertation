package ie.tud.msc.disertation.domain;

import ie.tud.msc.disertation.domain.ConfigVariables;
import lombok.Data;

@Data
public class Request {
    private int numberOfParticles;
    private int numberOfSwarms;
    private ConfigVariables configVariables;
}

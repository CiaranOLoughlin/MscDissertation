package ie.tud.msc.disertation.domain;

import ie.tud.msc.disertation.function.FunctionType;
import lombok.Data;

@Data
public class Request {
    private int numberOfSwarms;
    private int numberOfParticles;
    private double inertia;
    private double cognitive;
    private double social;
    private FunctionType functionType;
}

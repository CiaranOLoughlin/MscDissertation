package ie.tud.msc.disertation.domain;

import lombok.Data;

@Data
public class ApplicationProperties {
    private double defaultInertia =  0.2;
    private double defaultCognitive = 1.6;
    private double defaultSocial = 1.6;
    private int TARGET_NUMBER_OF_EXACT_ANSWERS = 20;
    private int MAX_NUMBER_OF_STEPS = 100000;
}

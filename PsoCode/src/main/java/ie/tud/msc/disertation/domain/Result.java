package ie.tud.msc.disertation.domain;

import lombok.Data;

@Data
public class Result {
    private int step;
    private double best;
    private double bestPositionX;
    private double bestPositionY;
}

package ie.tud.msc.disertation.function.implementations;

import ie.tud.msc.disertation.function.Function;

public class DefaultFunction implements Function {
    @Override
    public double doCalc(double x, double y) {
        return (Math.sin(x)-3) + Math.cos(y);
    }
}
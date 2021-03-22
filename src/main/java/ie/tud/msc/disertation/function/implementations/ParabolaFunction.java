package ie.tud.msc.disertation.function.implementations;

import ie.tud.msc.disertation.function.Function;

public class ParabolaFunction implements Function {
    @Override
    public double doCalc(double x, double y) {
        return -4 * (x * x + y * y);
    }
}
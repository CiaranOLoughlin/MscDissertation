package ie.tud.msc.disertation.function.implementations;

import ie.tud.msc.disertation.function.Function;

public class BealeFunction implements Function {
    @Override
    public double doCalc(double x, double y) {
        double term1 = Math.pow(1.5 - x + x*y, 2);
        double term2 = Math.pow(2.25 - x + x*Math.pow(y,2), 2);
        double term3 = Math.pow(2.625 - x + x*Math.pow(y,3), 2);
        return term1 + term2 + term3;
    }
}
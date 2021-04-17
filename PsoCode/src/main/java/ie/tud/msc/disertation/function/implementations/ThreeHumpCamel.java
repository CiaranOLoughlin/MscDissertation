package ie.tud.msc.disertation.function.implementations;

import ie.tud.msc.disertation.function.Function;

public class ThreeHumpCamel implements Function {
    @Override
    public double doCalc(double x, double y) {
        double term1 = Math.pow(2*x, 2);
        double term2 = -1.05*Math.pow(x,4);
        double term3 = Math.pow(x,6) / 6;
        double term4 = x*y;
        double term5 = Math.pow(y,2);
        return term1 + term2 + term3 + term4 + term5;
    }
}
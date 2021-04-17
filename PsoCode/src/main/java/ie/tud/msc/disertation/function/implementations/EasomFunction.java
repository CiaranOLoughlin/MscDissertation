package ie.tud.msc.disertation.function.implementations;

import ie.tud.msc.disertation.function.Function;

public class EasomFunction implements Function {
    @Override
    public double doCalc(double x, double y) {
        /*
x1 = xx(1);
x2 = xx(2);

fact1 = -cos(x1)*cos(x2);
fact2 = exp(-(x1-pi)^2-(x2-pi)^2);

y = fact1*fact2;

         */

        double term1 = -(Math.cos(x)) * Math.cos(y);
        double term2 = Math.exp(-Math.pow((x-Math.PI), 2) - Math.pow(y-Math.PI, 2));

        return term1 * term2;
    }
}
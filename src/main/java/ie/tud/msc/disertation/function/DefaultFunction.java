package ie.tud.msc.disertation.function;

public class DefaultFunction implements Function{
    @Override
    public double doCalc(double x, double y) {
        return (Math.pow(x, 2) -3*x) + (Math.pow(y, 2) -6*y);
    }
}
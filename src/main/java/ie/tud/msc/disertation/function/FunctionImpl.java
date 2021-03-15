package ie.tud.msc.disertation.function;

public class FunctionImpl implements Function{
    @Override
    public double doCalc(double x, double y) {
        return Math.cos(48 * Math.sqrt(x * x + y * y)) - 4 * (x * x + y * y);
    }
}
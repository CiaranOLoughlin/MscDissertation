package ie.tud.msc.disertation.services;

import ie.tud.msc.disertation.domain.Vector;
import ie.tud.msc.disertation.function.Function;

import java.util.Random;

public class Particle {
    private Vector position;
    private Vector velocity;
    private Vector bestPosition;
    private double bestEval;
    private Function function;

    public Particle (Function function) {
        this.function = function;
        position = new Vector();
        velocity = new Vector();
        setRandomPosition();
        bestPosition = velocity.clone();
        bestEval = calculateValue();
    }

    private double calculateValue () {
        return function.doCalc(position.getX(), position.getY());
    }

    private void setRandomPosition () {
        Random r = new Random();
        double x = r.nextDouble();
        double y = r.nextDouble();
        double z = r.nextDouble();
        position.set(x, y, z);
    }

    public void updatePersonalBest () {
        double eval = this.calculateValue();
        if (eval < bestEval) {
            bestPosition = position.clone();
            bestEval = eval;
        }
    }

    public Vector getPosition () {
        return position.clone();
    }

    public Vector getVelocity () {
        return velocity.clone();
    }

    public Vector getBestPosition() {
        return bestPosition.clone();
    }

    public double getBestEval () {
        return bestEval;
    }

    public void updatePosition () {
        this.position.add(velocity);
    }

    public void setVelocity (Vector velocity) {
        this.velocity = velocity.clone();
    }
}

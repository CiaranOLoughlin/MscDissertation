package ie.tud.msc.disertation.services;

import ie.tud.msc.disertation.domain.Result;
import ie.tud.msc.disertation.domain.ResultList;
import ie.tud.msc.disertation.function.Function;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import ie.tud.msc.disertation.domain.Vector;

public class Swarm {
    private Vector bestPosition;
    private double bestEval;
    private Particle[] particles;
    private double inertia;
    private double cognitive;
    private double social;

    public Swarm (Function function, int numberOfParticles, double inertia, double cognitive, double social) {
        double infinity = Double.POSITIVE_INFINITY;
        bestPosition = new Vector(infinity, infinity, infinity);
        bestEval = Double.POSITIVE_INFINITY;
        this.particles = this.initialiseParticles(numberOfParticles, function);
        this.inertia = inertia;
        this.cognitive = cognitive;
        this.social = social;
    }

    public Result runSwarm(int epoch) {
        double oldEval = bestEval;

        if (bestEval < oldEval) {
            oldEval = bestEval;
        }

        for (Particle particle : particles) {
            particle.updatePersonalBest();
            updateGlobalBest(particle);
        }

        for (Particle particle : particles) {
            updateVelocity(particle, this.inertia, this.cognitive, this.social);
            particle.updatePosition();
        }

        return createResultObject(epoch);
    }

    private Result createResultObject(int epoch){
        Result result = new Result();
        result.setStep(epoch);
        result.setBestPositionX(bestPosition.getX());
        result.setBestPositionY(bestPosition.getY());
        result.setBest(bestEval);
        return result;
    }

    private void updateGlobalBest (Particle particle) {
        if (particle.getBestEval() < bestEval) {
            bestPosition = particle.getBestPosition();
            bestEval = particle.getBestEval();
        }
    }

    private void updateVelocity (Particle particle, double inertia, double cognitiveComponent, double socialComponent) {
        Vector oldVelocity = particle.getVelocity();
        Vector personalBest = particle.getBestPosition();
        Vector globalBest = bestPosition.clone();
        Vector currentPosition = particle.getPosition();

        Random random = new Random();
        double r1 = random.nextDouble();
        double r2 = random.nextDouble();

        // The first product of the formula.
        Vector newVelocity = oldVelocity.clone();
        newVelocity.mul(inertia);

        // The second product of the formula.
        personalBest.sub(currentPosition);
        personalBest.mul(cognitiveComponent);
        personalBest.mul(r1);
        newVelocity.add(personalBest);

        // The third product of the formula.
        globalBest.sub(currentPosition);
        globalBest.mul(socialComponent);
        globalBest.mul(r2);
        newVelocity.add(globalBest);

        particle.setVelocity(newVelocity);
    }

    public static Particle[] initialiseParticles(int numberOfParticles, Function function) {
        Particle[] particles = new Particle[numberOfParticles];
        for (int i = 0; i < numberOfParticles; i++) {
            Particle particle = new Particle(function);
            particles[i] = particle;
        }
        return particles;
    }
}

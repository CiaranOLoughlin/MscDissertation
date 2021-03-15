package ie.tud.msc.disertation.services;

import ie.tud.msc.disertation.Swarm;
import ie.tud.msc.disertation.function.Function;
import ie.tud.msc.disertation.function.FunctionImpl;

import java.util.Scanner;

public class Main {

    public static void main (String[] args) {
        System.out.println("Inertia:             " + Swarm.DEFAULT_INERTIA);
        System.out.println("Cognitive Component: " + Swarm.DEFAULT_COGNITIVE);
        System.out.println("Social Component:    " + Swarm.DEFAULT_SOCIAL);
        runSwarm(200, 8000, Swarm.DEFAULT_INERTIA, Swarm.DEFAULT_COGNITIVE, Swarm.DEFAULT_SOCIAL);
        double x = 0.000000000001;
        double y = 0.000000000001;
        System.out.println(Math.cos(48 * Math.sqrt(x * x + y * y)) - 4 * (x * x + y * y));
    }

    private static void runSwarm (int particles, int epochs, double inertia, double cognitive, double social) {
        Swarm swarm = new Swarm(new FunctionImpl(), particles, epochs);
        swarm.run();
    }
}

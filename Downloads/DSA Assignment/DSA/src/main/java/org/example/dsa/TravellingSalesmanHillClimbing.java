package org.example.dsa;

/*
Question 5 a)

Implement travelling a salesman problem using hill climbing algorithm.
*/


import java.util.*;

public class TravellingSalesmanHillClimbing {

    // Function to compute the total distance of a given tour
    private static int calculateTourCost(int[][] distanceMatrix, List<Integer> tour) {
        int cost = 0;
        int n = tour.size();
        for (int i = 0; i < n; i++) {
            int from = tour.get(i);
            int to = tour.get((i + 1) % n);
            cost += distanceMatrix[from][to];
        }
        return cost;
    }

    // Function to generate a neighbor by swapping two cities
    private static List<Integer> getNeighbor(List<Integer> tour) {
        Random rand = new Random();
        List<Integer> newTour = new ArrayList<>(tour);
        int i = rand.nextInt(tour.size());
        int j = rand.nextInt(tour.size());
        Collections.swap(newTour, i, j);
        return newTour;
    }

    // Hill Climbing algorithm for TSP
    public static List<Integer> hillClimbing(int[][] distanceMatrix) {
        int n = distanceMatrix.length;
        List<Integer> currentTour = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            currentTour.add(i);
        }
        Collections.shuffle(currentTour);
        int currentCost = calculateTourCost(distanceMatrix, currentTour);

        boolean improving = true;
        while (improving) {
            improving = false;
            List<Integer> bestNeighbor = new ArrayList<>(currentTour);
            int bestCost = currentCost;

            for (int i = 0; i < 100; i++) { // Check up to 100 neighbors
                List<Integer> neighbor = getNeighbor(currentTour);
                int neighborCost = calculateTourCost(distanceMatrix, neighbor);
                if (neighborCost < bestCost) {
                    bestCost = neighborCost;
                    bestNeighbor = neighbor;
                    improving = true;
                }
            }

            if (improving) {
                currentTour = bestNeighbor;
                currentCost = bestCost;
            }
        }

        return currentTour;
    }

    public static void main(String[] args) {
        // Example distance matrix (symmetric and square)
        int[][] distanceMatrix = {
                {0, 10, 15, 20},
                {10, 0, 35, 25},
                {15, 35, 0, 30},
                {20, 25, 30, 0}
        };

        List<Integer> bestTour = hillClimbing(distanceMatrix);
        int bestCost = calculateTourCost(distanceMatrix, bestTour);

        System.out.println("Best Tour: " + bestTour);
        System.out.println("Tour Cost: " + bestCost);
    }
}


package org.example.dsa;

/*
Question 4 a)

Imagine you're a city planner tasked with improving the road network between key locations (nodes) represented
by numbers 0 to n-1. Some roads (edges) have known travel times (positive weights), while others are under
construction (weight -1). Your goal is to modify the construction times on these unbuilt roads to achieve a specific
travel time (target) between two important locations (from source to destination).
*/


import java.util.*;

public class CityPlanner {

    static class Edge {
        int u, v, weight;
        Edge(int u, int v, int weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }
    }

    public static List<Edge> findOptimalRoads(int n, int[][] roads, int source, int destination, int target) {
        List<Edge> graph = new ArrayList<>();
        Map<Integer, Map<Integer, Integer>> adjList = new HashMap<>();
        Map<Edge, Integer> edgeMap = new HashMap<>();

        for (int[] road : roads) {
            int u = road[0];
            int v = road[1];
            int weight = road[2];
            if (weight == -1) {
                weight = Integer.MAX_VALUE; // Temporarily set to a large value
            }
            graph.add(new Edge(u, v, weight));
            graph.add(new Edge(v, u, weight));
            adjList.putIfAbsent(u, new HashMap<>());
            adjList.putIfAbsent(v, new HashMap<>());
            adjList.get(u).put(v, weight);
            adjList.get(v).put(u, weight);
            edgeMap.put(new Edge(u, v, weight), weight);
            edgeMap.put(new Edge(v, u, weight), weight);
        }

        // Dijkstra's Algorithm to find shortest path from source
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.add(new int[]{source, 0});

        while (!pq.isEmpty()) {
            int[] node = pq.poll();
            int u = node[0];
            int currentDist = node[1];

            if (currentDist > dist[u]) continue;

            if (!adjList.containsKey(u)) continue;

            for (Map.Entry<Integer, Integer> entry : adjList.get(u).entrySet()) {
                int v = entry.getKey();
                int weight = entry.getValue();

                if (weight == Integer.MAX_VALUE) continue; // Skip unmodified edges

                if (dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    pq.add(new int[]{v, dist[v]});
                }
            }
        }

        int distanceWithModifiedRoads = dist[destination];

        // Adjust weights to meet the target
        for (Edge edge : graph) {
            if (edge.weight == Integer.MAX_VALUE) {
                // Determine the required weight
                int u = edge.u;
                int v = edge.v;

                int currentDistance = dist[u] + dist[v] - 2 * dist[destination];
                int requiredWeight = target - currentDistance;

                if (requiredWeight > 0) {
                    edgeMap.put(new Edge(u, v, requiredWeight), requiredWeight);
                    edgeMap.put(new Edge(v, u, requiredWeight), requiredWeight);
                }
            }
        }

        List<Edge> result = new ArrayList<>();
        for (Edge edge : graph) {
            int weight = edgeMap.getOrDefault(edge, edge.weight);
            if (weight != Integer.MAX_VALUE) {
                result.add(new Edge(edge.u, edge.v, weight));
            }
        }

        return result;
    }

    public static void main(String[] args) {
        int n = 5;
        int[][] roads = {{4, 1, -1}, {2, 0, -1}, {0, 3, -1}, {4, 3, -1}};
        int source = 0;
        int destination = 1;
        int target = 5;

        List<Edge> result = findOptimalRoads(n, roads, source, destination, target);
        for (Edge edge : result) {
            System.out.println("[" + edge.u + ", " + edge.v + ", " + edge.weight + "]");
        }
    }
}


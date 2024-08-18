package com.example.demo;

/*
Question 3 a)

Imagine a small community with n houses, numbered 0 to n-1. Some houses have restrictions against becoming
friends, represented by pairs in the restrictions list. For example, if [0, 1] is in the list, houses 0 and 1 cannot be
directly or indirectly friends (through common friends).
*/


import java.util.*;

public class CommunityFriendship {

    // Union-Find Data Structure
    static class UnionFind {
        private int[] parent;
        private int[] rank;

        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX != rootY) {
                if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else {
                    parent[rootY] = rootX;
                    rank[rootX]++;
                }
            }
        }
    }

    public static List<String> canRequestsBeAccepted(int n, int[][] restrictions, int[][] requests) {
        UnionFind uf = new UnionFind(n);

        // Initialize restricted pairs
        Set<Integer> restrictedPairs = new HashSet<>();
        for (int[] restriction : restrictions) {
            int a = restriction[0];
            int b = restriction[1];
            restrictedPairs.add(a * n + b);
            restrictedPairs.add(b * n + a);
        }

        List<String> result = new ArrayList<>();

        for (int[] request : requests) {
            int x = request[0];
            int y = request[1];

            boolean canBeFriends = true;

            // Check if adding this request violates any restrictions
            for (int[] restriction : restrictions) {
                int a = restriction[0];
                int b = restriction[1];

                if (uf.find(a) == uf.find(x) && uf.find(b) == uf.find(y) ||
                        uf.find(a) == uf.find(y) && uf.find(b) == uf.find(x)) {
                    canBeFriends = false;
                    break;
                }
            }

            if (canBeFriends) {
                result.add("approved");
                uf.union(x, y);
            } else {
                result.add("denied");
            }
        }

        return result;
    }

    public static void main(String[] args) {
        int n1 = 3;
        int[][] restrictions1 = {{0, 1}};
        int[][] requests1 = {{0, 2}, {2, 1}};

        int n2 = 5;
        int[][] restrictions2 = {{0, 1}, {1, 2}, {2, 3}};
        int[][] requests2 = {{0, 4}, {1, 2}, {3, 1}, {3, 4}};

        System.out.println(canRequestsBeAccepted(n1, restrictions1, requests1)); // Output: [approved, denied]
        System.out.println(canRequestsBeAccepted(n2, restrictions2, requests2)); // Output: [approved, denied, approved, denied]
    }
}

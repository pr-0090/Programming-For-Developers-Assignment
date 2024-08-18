package com.example.demo;

/*
Question 5 b)

Imagine you're on a challenging hiking trail represented by an array nums, where each element represents the
altitude at a specific point on the trail. You want to find the longest consecutive stretch of the trail you can hike
while staying within a certain elevation gain limit (at most k).
*/

public class LongestHike {
    public static int longestHike(int[] trail, int k) {
        int n = trail.length;
        int maxLength = 0;
        int left = 0;

        for (int right = 0; right < n; right++) {
            // Ensure that the trail from left to right is within the elevation gain limit
            while (right > left && trail[right] - trail[right - 1] > k) {
                left = right;
            }
            maxLength = Math.max(maxLength, right - left + 1);
        }

        return maxLength;
    }

    public static void main(String[] args) {
        // Example input
        int[] trail = {4, 2, 1, 4, 3, 4, 5, 8, 15};
        int k = 3;

        // Find the longest hike
        int result = longestHike(trail, k);
        System.out.println("Longest hike length: " + result);
    }
}

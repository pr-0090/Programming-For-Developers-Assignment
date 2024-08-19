package org.example.dsa;

/*
Question2 b)

Imagine you're at a movie theater with assigned seating. You have a seating chart represented by an array nums
where nums[i] represents the seat number at row i. You're looking for two friends to sit together, considering their
seat preferences and your limitations:
*/


import java.util.*;

public class MovieSeating {

    public static boolean canSitTogether(int[] nums, int indexDiff, int valueDiff) {
        // TreeSet to store seat numbers in a sorted order
        TreeSet<Integer> seatSet = new TreeSet<>();

        for (int i = 0; i < nums.length; i++) {
            int currentSeat = nums[i];

            // Remove seats that are out of the allowed index range
            if (i > indexDiff) {
                seatSet.remove(nums[i - indexDiff - 1]);
            }

            // Check for a seat number within the valueDiff range
            Integer lowerBound = seatSet.floor(currentSeat + valueDiff);
            Integer upperBound = seatSet.ceiling(currentSeat - valueDiff);

            if (lowerBound != null && lowerBound >= currentSeat - valueDiff ||
                    upperBound != null && upperBound <= currentSeat + valueDiff) {
                return true;
            }

            // Add the current seat number to the set
            seatSet.add(currentSeat);
        }

        return false;
    }

    public static void main(String[] args) {
        int[] nums1 = {2, 3, 5, 4, 9};
        int indexDiff1 = 2;
        int valueDiff1 = 1;

        int[] nums2 = {1, 6, 7, 8, 10};
        int indexDiff2 = 2;
        int valueDiff2 = 2;

        System.out.println(canSitTogether(nums1, indexDiff1, valueDiff1)); // Output: true
        System.out.println(canSitTogether(nums2, indexDiff2, valueDiff2)); // Output: false
    }
}

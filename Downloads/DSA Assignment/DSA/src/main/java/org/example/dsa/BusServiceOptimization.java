package org.example.dsa;

/*
Question 3 b)

Imagine you are managing a bus service with passengers boarding at various stops along the route. Your task is to
optimize the boarding process by rearranging the order of passengers at intervals of k stops, where k is a positive
integer. If the total number of passengers is not a multiple of k, then the remaining passengers at the end of the route
should maintain their original order.
*/


import java.util.*;

public class BusServiceOptimization {

    public static List<Integer> optimizeBoarding(List<Integer> head, int k) {
        List<Integer> result = new ArrayList<>();
        int n = head.size();

        for (int i = 0; i < n; i += k) {
            // Determine the end index for the current segment
            int end = Math.min(i + k, n);
            List<Integer> segment = head.subList(i, end);

            // Reverse the segment if its size is exactly k
            if (segment.size() == k) {
                Collections.reverse(segment);
            }

            // Add the processed segment to the result
            result.addAll(segment);
        }

        return result;
    }

    public static void main(String[] args) {
        List<Integer> head1 = Arrays.asList(1, 2, 3, 4, 5);
        int k1 = 2;
        System.out.println(optimizeBoarding(head1, k1)); // Output: [2, 1, 4, 3, 5]

        List<Integer> head2 = Arrays.asList(1, 2, 3, 4, 5);
        int k2 = 3;
        System.out.println(optimizeBoarding(head2, k2)); // Output: [3, 2, 1, 4, 5]
    }
}


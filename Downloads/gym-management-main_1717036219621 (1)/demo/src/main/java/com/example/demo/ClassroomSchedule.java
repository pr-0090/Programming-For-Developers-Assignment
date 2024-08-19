package com.example.demo;


/*
Question1 a)

Imagine you're a scheduling officer at a university with n classrooms numbered 0 to n-1. Several different courses
require classrooms throughout the day, represented by an array of classes classes[i] = [starti, endi], where starti is
the start time of the class and endi is the end time (both in whole hours). Your goal is to assign each course to a
classroom while minimizing disruption and maximizing classroom utilization.
*/


import java.util.*;

public class gitClassroomSchedule {

    static class ClassEntry {
        int start, end;
        public ClassEntry(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    public static int findMostUtilizedRoom(int n, int[][] classes) {
        // Convert the 2D array to a list of ClassEntry objects
        List<ClassEntry> classList = new ArrayList<>();
        for (int[] cls : classes) {
            classList.add(new ClassEntry(cls[0], cls[1]));
        }

        // Sort classes first by start time, then by end time
        classList.sort((a, b) -> {
            if (a.start != b.start) {
                return Integer.compare(a.start, b.start);
            } else {
                return Integer.compare(b.end, a.end); // Larger end time first for prioritization
            }
        });

        // PriorityQueue to track when each room becomes available (end time, room index)
        PriorityQueue<int[]> roomEndTimes = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        // PriorityQueue to track classes waiting for an available room (start time, end time)
        PriorityQueue<ClassEntry> waitingClasses = new PriorityQueue<>(Comparator.comparingInt(a -> a.start));

        // Initialize room utilization counts
        int[] roomClassCount = new int[n];
        // Initialize available rooms (empty)
        for (int i = 0; i < n; i++) {
            roomEndTimes.add(new int[]{0, i});
        }

        for (ClassEntry cls : classList) {
            int start = cls.start;
            int end = cls.end;

            // Free up rooms that are available by the start time of the current class
            while (!roomEndTimes.isEmpty() && roomEndTimes.peek()[0] <= start) {
                int[] room = roomEndTimes.poll();
                if (!waitingClasses.isEmpty()) {
                    ClassEntry waitingClass = waitingClasses.poll();
                    roomEndTimes.add(new int[]{waitingClass.end, room[1]});
                    roomClassCount[room[1]]++;
                } else {
                    roomEndTimes.add(room);
                }
            }

            // Assign current class
            if (roomEndTimes.size() < n) {
                int[] room = roomEndTimes.poll();
                roomEndTimes.add(new int[]{end, room[1]});
                roomClassCount[room[1]]++;
            } else {
                waitingClasses.add(cls);
            }
        }

        // Find the room with the maximum number of classes
        int maxClasses = 0;
        int bestRoom = 0;
        for (int i = 0; i < n; i++) {
            if (roomClassCount[i] > maxClasses) {
                maxClasses = roomClassCount[i];
                bestRoom = i;
            }
        }

        return bestRoom;
    }

    public static void main(String[] args) {
        int[][] classes1 = {{0, 10}, {1, 5}, {2, 7}, {3, 4}};
        int[][] classes2 = {{1, 20}, {2, 10}, {3, 5}, {4, 9}, {6, 8}};

        System.out.println(findMostUtilizedRoom(2, classes1)); // Output: 0
        System.out.println(findMostUtilizedRoom(3, classes2)); // Output: 1
    }
}
package org.example.dsa;

/*
Question 4 b)

Imagine you're on a treasure hunt in an enchanted forest represented by a binary tree root. Each node in the tree has
a value representing a magical coin. Your goal is to find the largest collection of coins that forms a magical grove.
*/

public class MagicalGrove {

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int val) { this.val = val; }
    }

    static class Result {
        boolean isBST;
        int sum;
        int min;
        int max;

        Result(boolean isBST, int sum, int min, int max) {
            this.isBST = isBST;
            this.sum = sum;
            this.min = min;
            this.max = max;
        }
    }

    public static int largestMagicalGrove(TreeNode root) {
        return largestMagicalGroveHelper(root).sum;
    }

    private static Result largestMagicalGroveHelper(TreeNode node) {
        if (node == null) {
            return new Result(true, 0, Integer.MAX_VALUE, Integer.MIN_VALUE);
        }

        Result leftResult = largestMagicalGroveHelper(node.left);
        Result rightResult = largestMagicalGroveHelper(node.right);

        // Check if the current subtree is a valid BST
        boolean isBST = leftResult.isBST && rightResult.isBST
                && node.val > leftResult.max && node.val < rightResult.min;

        int sum = 0;
        if (isBST) {
            sum = node.val + leftResult.sum + rightResult.sum;
        }

        // Update global maximum sum of valid BSTs
        int maxSum = Math.max(sum, Math.max(leftResult.sum, rightResult.sum));

        // Determine new min and max for the current subtree
        int min = Math.min(node.val, Math.min(leftResult.min, rightResult.min));
        int max = Math.max(node.val, Math.max(leftResult.max, rightResult.max));

        return new Result(isBST, maxSum, min, max);
    }

    public static void main(String[] args) {
        // Example Tree: [1,4,3,2,4,2,5,null,null,null,null,null,null,4,6]
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(4);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(2);
        root.left.right = new TreeNode(4);
        root.right.left = new TreeNode(2);
        root.right.right = new TreeNode(5);
        root.right.right.left = new TreeNode(4);
        root.right.right.right = new TreeNode(6);

        System.out.println(largestMagicalGrove(root));  // Output: 20
    }
}


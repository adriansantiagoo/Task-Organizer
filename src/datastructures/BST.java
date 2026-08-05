package datastructures;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class BST <T extends Comparable<T>> {
    private class Node{
        private T data;
        private Node left;
        private Node right;

        public Node(T data){
            this.data = data;
            this.left = null;
            this.right = null;
        }
        public T getData() {
            return data;
        }
        public Node getLeft() {
            return left;
        }
        public void setLeft(Node left) {
            this.left = left;
        }
        public Node getRight() {
            return right;
        }
        public void setRight(Node right) {
            this.right = right;
        }
    }

    private Node root;

    private boolean contains(Node root, T data){
        if (root == null) return false;
        if (data.compareTo(root.getData()) == 0) return true;
        if (data.compareTo(root.getData()) < 0){
            return contains(root.getLeft(), data);
        } else {
            return contains(root.getRight(), data);
        }
    }
    public boolean contains(T data){
        return contains(root, data);
    }

    private T find(Node root, T key){
        if (root == null) return null;
        int cmp = key.compareTo(root.getData());
        if (cmp == 0) return root.getData();                     // if the key is the root.getData
        if (cmp < 0)  return find(root.getLeft(), key);     // if the key is smaller than the root.getData
        return find(root.getRight(), key);                  // if the key is greater than the root.getData
    }
    public T find(T key){
        return find(root, key);
    }

    public void throwIfDuplicate(T data){
        if (contains(root, data)){
            throw new DuplicateValueException("The data already exists!");
        }
    }

    private Node insert(Node root, T data){
        if (root == null) return new Node(data);
        if (data.compareTo(root.getData()) < 0){
            root.setLeft(insert(root.getLeft(), data));
        } else {
            root.setRight(insert(root.getRight(), data));
        }
        return root;
    }
    public void insert(T data){
        throwIfDuplicate(data);
        root = insert(root, data);
    }

    public List<T> inorderTraversal(){
        return inorderHelper(root, new ArrayList<>());
    }
    private List<T> inorderHelper(Node root, List<T> result){
        if (root == null) return result;

        inorderHelper(root.left, result);
        result.add(root.getData());
        inorderHelper(root.right, result);

        return result;
    }

    public List<T> preorderTraversal(){
        return preorderHelper(root, new ArrayList<>());
    }
    private List<T> preorderHelper(Node root, List<T> result){
        if (root == null) return result;

        result.add(root.getData());
        preorderHelper(root.left, result);
        preorderHelper(root.right, result);

        return result;
    }

    public List<T> postorderTraversal(){
        return postorderHelper(root, new ArrayList<>());
    }
    private List<T> postorderHelper(Node root, List<T> result){
        if (root == null) return result;

        postorderHelper(root.left, result);
        postorderHelper(root.right, result);
        result.add(root.getData());

        return result;
    }

    public List<T> inorderIterative(){
        List<T> result = new ArrayList<>();
        Deque<Node> stack = new java.util.ArrayDeque<>();
        Node pointer = root;

        while (pointer != null || !stack.isEmpty()){
            while (pointer != null){
                stack.push(pointer);
                pointer = pointer.getLeft();
            }
            if (stack.peek() != null){
                result.add(stack.peek().getData());
            }
            if (stack.peek() != null){
                pointer = stack.pop().getRight();
            }
        }

        return result;
    }
}

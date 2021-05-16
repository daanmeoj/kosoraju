package com.codewithmosh;

import com.sun.source.tree.IfTree;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Graph {
    private class Node{
        private String label;

        public Node(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    private Map<String,String> nodes=new HashMap<>();
    private Map<String,List<String>> adjacencyList=new HashMap<>();
    private Integer count=0;

    public void addNode(String label){
        //var node=new Node(label);
        var node=label;
        nodes.putIfAbsent(label,label);
//        boolean keyLabelExists=adjacencyList.keySet().stream()
//                .anyMatch((key)->key.toString().equals(node));
//        if(!keyLabelExists)
        adjacencyList.putIfAbsent(node,new ArrayList<>());

    }

    public void addEdge(String from,String to){
        var fromNode=nodes.get(from);
        if(fromNode==null)
            throw new IllegalArgumentException();
        var toNode=nodes.get(to);
        if(toNode==null)
            throw new IllegalArgumentException();
        adjacencyList.get(fromNode).add(toNode);
    }

    public void print(){
        for(var source:adjacencyList.keySet()){
            var targets=adjacencyList.get(source);
            if(!targets.isEmpty()){
                System.out.println(source+" is connected to:"+targets);
            }
        }
    }

    public void removeNode(String label){
        var node=nodes.get(label);
        if(node==null)
            return;
        for(var n:adjacencyList.keySet())
            adjacencyList.get(n).remove(node);
        adjacencyList.remove(node);
        nodes.remove(node);
    }

    public void removeEdge(String from,String to){
        var fromNode=nodes.get(from);
        var toNode=nodes.get(to);
        if(fromNode==null || toNode==null)
            return;
        adjacencyList.get(fromNode).remove(toNode);
    }

    public void traverseDepthFirstRec(String root){
        var node=nodes.get(root);
        if(node==null)
            return;;
        traverseDepthFirstRec(nodes.get(root),new HashSet<>());
    }

    private void traverseDepthFirstRec(String root, Set<String> visited){
        System.out.println(root);
        visited.add(root);

        for(var node:adjacencyList.get(root)){
            if(!visited.contains(node))
                traverseDepthFirstRec(node,visited);
        }
    }

    public void traverseDepthFirst(String root){
        var node=nodes.get(root);
        if(node==null)
            return;;
        Set<String> visited=new HashSet<>();

        Stack<String> stack=new Stack<>();
        stack.push(node);

        while (!stack.isEmpty()){
            var current=stack.pop();
            if(visited.contains(current))
                continue;
            System.out.println(current);
            visited.add(current);

            for (var neighbour:adjacencyList.get(current))
                if(!visited.contains(neighbour))
                    stack.push(neighbour);
        }
    }

    public void traverseBreadthFirst(String root) {
        var node = nodes.get(root);
        if (node == null)
            return;

        Set<String> visited = new HashSet<>();
        Queue<String> queue=new ArrayDeque<>();
        queue.add(node);

        while (!queue.isEmpty()){
            var current=queue.remove();
            if(visited.contains(current))
                continue;
            System.out.println(current);
            visited.add(current);

            for (var neighbour:adjacencyList.get(current))
                if(!visited.contains(neighbour))
                    queue.add(neighbour);
        }
    }

    public List<String> topologicalSort(){

        Stack<String> stack=new Stack<>();
        Set<String> visited = new HashSet<>();

        for (var node:nodes.values()){
            topologicalSort(node, visited,stack);
        }

        List<String> sorted=new ArrayList<>();
        while (!stack.isEmpty())
            sorted.add(stack.pop());
        return sorted;
    }



    private void topologicalSort(String node, Set<String> visited, Stack<String> stack) {
        if (visited.contains(node)){
            return;
        }

        visited.add(node);

        for(var neighbour:adjacencyList.get(node)){
            if(!visited.contains(node))
                topologicalSort(neighbour,visited,stack);
        }

        stack.push(node);
    }

    public boolean hasCycle(){
        Set<String> all=new HashSet<>();
        all.addAll(nodes.values());
        Set<String> visiting=new HashSet<>();
        Set<String> visited=new HashSet<>();

        while (!all.isEmpty()){
            var current=all.iterator().next();
            if (hasCycle(current,all,visiting,visited))
                return true;
        }
        return false;
    }

    private boolean hasCycle(String node, Set<String> all,
                             Set<String> visiting,Set<String> visited){
        all.remove(node);
        visiting.add(node);

        for(var neighbour:adjacencyList.get(node)){
            if(visited.contains(neighbour))
                continue;
            if(visiting.contains(neighbour))
                return true;
            if(hasCycle(neighbour,all,visiting,visited))
                return true;
        }

        visiting.remove(node);
        visited.add(node);
        return false;
    }

    public Stack<String> obtainMagicalOrdering(){

        Stack<String> stack=new Stack<>();
        Set<String> visited = new HashSet<>();

        for (var node:nodes.values()){
            obtainMagicalOrdering(node, visited,stack);
        }

        return stack;
    }



    private void obtainMagicalOrdering(String node, Set<String> visited, Stack<String> stack) {

        var filterNode=node;

        if (visited.contains(filterNode)){
            return;
        }

        visited.add(filterNode);

        for(var neighbour:adjacencyList.get(filterNode)){
            if(!visited.contains(neighbour))
                obtainMagicalOrdering(neighbour,visited,stack);
        }
        stack.push(filterNode);
    }

    public Stack<String> obtainMagicalOrderingIterative(){

        Stack<String> stack=new Stack<>();
        Stack<String> outStack=new Stack<>();
        Set<String> visited = new HashSet<>();

        var sizeOfValues=nodes.values().size();
        var arrayOfValues=nodes.values().toArray();
        for (int i=sizeOfValues-1;i>0;i--){
            var node=(String)arrayOfValues[i];
            obtainMagicalOrderingIterative(node, visited,stack,outStack);
        }

        return outStack;
    }

    private void obtainMagicalOrderingIterative(String node, Set<String> visited,
                                                Stack<String> stack,Stack<String> outStack) {

        if(visited.contains(node))
            return;
        stack.push(node);

        while (!stack.isEmpty()){
            var current=stack.pop();
            if(!visited.contains(current)){
                outStack.push(current);
                visited.add(current);
                for (var neighbour:adjacencyList.get(current)){
                    if(!visited.contains(neighbour)){
                        stack.push(neighbour);
                    }
                }
            }

        }
    }

    public Map<String,Integer> traverseDepthFirstForKosaraju(Stack<String> magicalOrdering){

        Stack<String> stack=new Stack<>();
        Set<String> visited = new HashSet<>();
        Map<String,Integer> leaders=new HashMap<>();
        while(!magicalOrdering.isEmpty()){
            var node=magicalOrdering.pop();
            traverseDepthFirstForKosaraju(node, visited,stack);
            if(count!=0)
                leaders.put(node,count);
            count=0;
        }
        return leaders;

    }



    private void traverseDepthFirstForKosaraju(String node, Set<String> visited, Stack<String> stack) {
        var filterNode=node;

        if (visited.contains(filterNode)){
            return;
        }
        visited.add(filterNode);
        for(var neighbour:adjacencyList.get(filterNode)){
            if(!visited.contains(neighbour)){
                traverseDepthFirstForKosaraju(neighbour,visited,stack);
            }

        }
        stack.push(filterNode);
        count++;
    }

    public Map<String,Integer> traverseDepthFirstForKosarajuIterative(Stack<String> magicalOrdering){

        Stack<String> stack=new Stack<>();
        Set<String> visited = new HashSet<>();
        Map<String,Integer> leaders=new HashMap<>();
        while(!magicalOrdering.isEmpty()){
            var node=magicalOrdering.pop();
            traverseDepthFirstForKosarajuIterative(node, visited,stack);
            if(count>0)
                leaders.putIfAbsent(node,count);
            count=0;
        }
        return leaders;

    }



    private void traverseDepthFirstForKosarajuIterative(String node, Set<String> visited, Stack<String> stack) {


        if(visited.contains(node))
            return;
        stack.push(node);

        while (!stack.isEmpty()){
            var current=stack.pop();
            if(!visited.contains(current)){
                visited.add(current);
                count+=1;

                for (var neighbour:adjacencyList.get(current)){
                    if(!visited.contains(neighbour)){
                        stack.push(neighbour);
                    }
                }
            }

        }
    }

}
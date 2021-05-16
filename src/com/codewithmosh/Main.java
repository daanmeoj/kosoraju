package com.codewithmosh;

import java.io.*;
import java.util.Scanner;
import java.util.Stack;
import java.util.stream.Collectors;


public class Main {

    public static void main(String[] args) {

        var graphRev=new Graph();

        try{
            BufferedReader br=new BufferedReader(
                    new FileReader( "/home/davidmercado/IdeaProjects/Graphs/input/SCC.txt"));



            String s,tail,head;
            while ((s=br.readLine())!=null){
                tail=s.split(" ")[0];
                head=s.split(" ")[1];
                graphRev.addNode(head);
                graphRev.addNode(tail);
                graphRev.addEdge(head,tail);
            }

            var magicalOrderingIter=graphRev.obtainMagicalOrderingIterative();
            System.out.println(magicalOrderingIter.size());
            br=new BufferedReader(
                    new FileReader( "/home/davidmercado/IdeaProjects/Graphs/input/SCC.txt"));


            //graphRev=new Graph();
            var graph=new Graph();
            while ((s=br.readLine())!=null){
                tail=s.split(" ")[0];
                head=s.split(" ")[1];
                graph.addNode(tail);
                graph.addNode(head);
                graph.addEdge(tail,head);
            }
            var leaders=graph.traverseDepthFirstForKosarajuIterative(magicalOrderingIter);
            var total=leaders.values().stream().reduce((a,b)->a+b);
            System.out.println(total);
            var result=leaders
                    .values().stream().sorted((a,b)->b-a).toArray();
            System.out.println("final result:");

            System.out.println(result[0]);
            System.out.println(result[1]);
            System.out.println(result[2]);
            System.out.println(result[3]);
            System.out.println(result[4]);


            br.close();
        }catch(Exception ex){
            System.out.println(ex.getStackTrace()[0].getLineNumber());
            return;
        }


    }
}
package com.company;


import edu.princeton.cs.algs4.Digraph;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by kodoo on 28.02.2016.
 */
public class ClassInfoProcessor {

    private static String FIELDS = "FIELDS:";
    private static String METHODS = "METHODS:";
    private static String DEPENDENCIES = "DEPENDENCIES:";
    private static String TOKEN = "-";

    private Digraph usageGraph;
    private Graph linkGraph = null;
    private List<String> methodsAndFields;
    private int methodsCount;

    public ClassInfoProcessor(String fileName) {
        try (Scanner scan = new Scanner(new FileInputStream(fileName))) {

            if (!scan.next().equals(METHODS))
                throw new InvalidParameterException(METHODS + " must be on the first line!");

            methodsAndFields = new ArrayList<>();
            String line;
            while (!(line = scan.next()).equals(FIELDS)) {
                methodsAndFields.add(line);
            }
            methodsCount = methodsAndFields.size();

            while (!(line = scan.next()).equals(DEPENDENCIES)) {
                methodsAndFields.add(line);
            }

            usageGraph = new Digraph(methodsAndFields.size());
            while (scan.hasNext()) {
                String methodName = scan.next();
                String usedByMethod;
                while (!(usedByMethod = scan.next()).equals(TOKEN)) {
                    usageGraph.addEdge(
                            methodsAndFields.indexOf(usedByMethod),
                            methodsAndFields.indexOf(methodName)
                    );
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Graph buildLinkGraph() {
        if (linkGraph != null)
            return linkGraph;

        linkGraph = new Graph(methodsCount);

        for (int i = 0; i < methodsCount; i++) {
            Iterable<Integer> users = usageGraph.adj(i);

            for (int user : users) {
                linkGraph.addEdge(i, user);
            }
        }

        for (int i = methodsCount; i < methodsAndFields.size(); i++) {
            Iterable<Integer> streightLinkedMethods = usageGraph.adj(i);

            for (int methodAIndex : streightLinkedMethods) {
                for (int methodBIndex : streightLinkedMethods) {
                    if (methodAIndex != methodBIndex) {
                        linkGraph.addEdge(methodAIndex, methodBIndex);
                    }
                }
            }
        }
        return linkGraph;
    }

    public void draw() {
        if (linkGraph == null)
            throw new NullPointerException("Linked graph is not built!");

        GraphDraw frame = new GraphDraw();
        int width = 1000;
        int height = 1000;
        frame.setSize(width, height);
        frame.setVisible(true);

        double fi = 0;
        double deltaFi = 2.0 * Math.PI / linkGraph.V();
        for (int v = 0; v < linkGraph.V(); v++) {
            frame.addNode(
                    methodsAndFields.get(v),
                    (int) (Math.cos(fi) * 400.0) + width / 2,
                    (int) (Math.sin(fi) * 400.0) + height / 2);
            fi += deltaFi;
        }

        for (int v = 0; v < linkGraph.V(); v++) {
            for (int linkTo : linkGraph.adj(v)) {
                frame.addEdge(v, linkTo);
            }
        }
    }
}

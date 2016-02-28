package com.company;

public class Main {

    public static void main(String[] args) {

        ClassInfoProcessor processor = new ClassInfoProcessor("data/class.txt");
        Graph linkGraph = processor.buildLinkGraph();
        processor.draw();
        int N = linkGraph.V();
        int NP = N * (N - 1) / 2;

        CC cc = new CC(linkGraph);
        int NDC = linkGraph.E() / 2;
        int NIC = NP - cc.count() + 1;

        System.out.printf("NP(C) = %d\n", NP);
        System.out.printf("NDC(C) = %d\n", NDC);
        System.out.printf("NIC(C) = %d\n", NIC);
        System.out.printf("TCC(C) = %f\n", (float) NDC / NP);
        System.out.printf("LCC(C) = %f\n", (float) NIC / NP);
    }
}

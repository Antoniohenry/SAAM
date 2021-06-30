package SaamAlgo.Graph;

import SaamAlgo.Model.Aircraft;
import SaamAlgo.Graph.Edge.Edge;
import SaamAlgo.Graph.Edge.FinalEdge;
import SaamAlgo.Graph.Node.Node;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class Graph implements IGraph {

    private final HashMap<String, Node> nodes;
    private final HashMap<String, Edge> edges;

    private Node lEntry;
    private Node rEntry;
    public static Node lRunway;
    public static Node rRunway;

    public Graph(boolean test) {

        nodes = new HashMap<>();
        edges = new HashMap<>();


        if(test){
            createTest();
        }
    }

    private void createTest(){
        lEntry = new Node(-40, 70, "lEntry");
        rEntry = new Node(40, 70, "rEntry");
        lRunway = new Node(-5, 0, "lRunway");
        rRunway = new Node(5, 0, "rRunway");
        setUpTest(lEntry, lRunway, rEntry, rRunway);
    }

    private void setUpTest(Node lEntry, Node lRunway, Node rEntry, Node rRunway){
        Node lSeparation = new Node((int)(lEntry.x * 0.7), (int)(lEntry.y * 0.7), "lSeparation");
        Node rSeparation = new Node((int)(rEntry.x * 0.7), (int)(rEntry.y * 0.7), "rSeparation");
        Node lMerge = new Node((int)(lEntry.x * 0.3), (int)(lEntry.y * 0.3), "lMerge" );
        Node rMerge = new Node((int)(rEntry.x * 0.3), (int)(rEntry.y * 0.3), "rMerge" );

        this.addNode(lMerge);
        this.addNode(rMerge);
        this.addNode(lSeparation);
        this.addNode(rSeparation);
        this.addNode(lRunway);
        this.addNode(rRunway);
        this.addNode(lEntry);
        this.addNode(rEntry);

        Edge lEntryLeg = new Edge(lEntry, lSeparation, "lEntryLeg");
        Edge rEntryLeg = new Edge(rEntry, rSeparation, "rEntryLeg");
        Edge lLeg = new Edge(lSeparation, lMerge, "lLeg");
        Edge rLeg = new Edge(rSeparation, rMerge, "rLeg");
        Edge ltorLeg = new Edge(lSeparation, rMerge, "lToRLeg");
        Edge rtolLeg = new Edge(rSeparation, lMerge, "rToLLeg");
        FinalEdge lFinalLeg = new FinalEdge(lMerge, lRunway, "lFinalLeg");
        FinalEdge rFinalLeg = new FinalEdge(rMerge, rRunway, "rFinalLeg");

        this.addEdge(lEntryLeg);
        this.addEdge(rEntryLeg);
        this.addEdge(lLeg);
        this.addEdge(rLeg);
        this.addEdge(lFinalLeg);
        this.addEdge(rFinalLeg);
        this.addEdge(rtolLeg);
        this.addEdge(ltorLeg);
    }

    public Route getRoute(Node entry, Node runway){
        if (nodes.get("lEntry").equals(entry)) {
            if(nodes.get("lRunway").equals(runway)){
                return new Route(List.of(edges.get("lEntryLeg"), edges.get("lLeg"), edges.get("lFinalLeg")));
            }
            if(nodes.get("rRunway").equals(runway)) {
                return new Route(List.of(edges.get("lEntryLeg"), edges.get("lToRLeg"), edges.get("rFinalLeg")));
            }
        }

        if (nodes.get("rEntry").equals(entry)) {
            if(nodes.get("rRunway").equals(runway)){
                return new Route(List.of(edges.get("rEntryLeg"), edges.get("rLeg"), edges.get("rFinalLeg")));
            }
            if(nodes.get("lRunway").equals(runway)) {
                return new Route(List.of(edges.get("rEntryLeg"), edges.get("rToLLeg"), edges.get("lFinalLeg")));
            }
        }
        return null;
    }

    public void addNode(Node node){
        nodes.put(node.getName(), node);
    }

    public void addEdge(Edge edge){
        edges.put(edge.getName(), edge);
    }

    public void addAircraft(@NotNull Aircraft aircraft){
        double time = aircraft.getTimeIn();
        aircraft.getEntry().addAircraft(aircraft, time); //ajout de l'avion au point d'entré
        for(Edge edge : aircraft.getRoute()){
            edge.addFlyingAircraft(aircraft, time); //l'avion est ajouté à tout les points du parcours sauf l'entré
            time += edge.getLength() / aircraft.getSpeed(); // temps au passage strict du centre du noeud
            edge.getExitNode().addAircraft(aircraft, time);
        }
    }

    public void removeAircraft(Aircraft aircraft){
        double time= aircraft.getTimeIn();
        aircraft.getEntry().removeAircraft(time);
        for(Edge edge : aircraft.getRoute()){
            edge.removeAircraft(time);
            time += edge.getLength() / aircraft.getSpeed();
            edge.getExitNode().removeAircraft(time);
        }
    }


    public Node lEntry() {
        return lEntry;
    }

    public Node rEntry() {
        return rEntry;
    }

    public Node lRunway() {
        return lRunway;
    }

    public Node rRunway() {
        return rRunway;
    }
}

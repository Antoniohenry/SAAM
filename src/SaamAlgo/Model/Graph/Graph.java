package SaamAlgo.Model.Graph;

import SaamAlgo.Model.Aircraft;
import SaamAlgo.Model.Graph.Edge.Arc;
import SaamAlgo.Model.Graph.Edge.Edge;
import SaamAlgo.Model.Graph.Edge.FinalEdge;
import SaamAlgo.Model.Graph.Node.Node;
import SaamAlgo.Model.Constants;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Graph {

    private final HashMap<String, Node> nodes;
    private final HashMap<String, Edge > edges;
    private final List<Route> routes;
    public Set<Node> runways;

    public Graph() {
        nodes = new HashMap<>();
        edges = new HashMap<>();
        runways = new HashSet<>();
        routes = new LinkedList<>();


        getNodesFromFile("DATA1/nodes.txt");
        getEdgesFromFile("DATA1/links.txt");
        getRoutesFromFile("DATA1/routes.txt");

    }

    private void getNodesFromFile(String filename) {
        String line;

        //System.out.println("Loading nodes...");
        try {
            FileReader nodeFile = new FileReader(filename);
            BufferedReader nodeFileInput = new BufferedReader(nodeFile);

            while ((line = nodeFileInput.readLine()) != null){
                StringTokenizer tokenizer = new StringTokenizer(line);
                double x = Double.parseDouble(tokenizer.nextToken());
                double y = Double.parseDouble(tokenizer.nextToken());
                String name = tokenizer.nextToken();

                nodes.put(name, new Node(x, y, name));

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getEdgesFromFile(String filename) {
        String line;

        //System.out.println("Loading edges...");
        try {
            FileReader edgesFile = new FileReader(filename);
            BufferedReader edgesFileInput = new BufferedReader(edgesFile);

            while ((line = edgesFileInput.readLine()) != null){
                StringTokenizer tokenizer = new StringTokenizer(line);
                String entry = tokenizer.nextToken();
                String exit = tokenizer.nextToken();

                String name = entry + '-' + exit;

                if(exit.startsWith("IF")){
                    edges.put(name, new Arc(nodes.get(entry), nodes.get(exit), name));
                }

                if(exit.startsWith("RWY")){
                    edges.put(name, new FinalEdge(nodes.get(entry), nodes.get(exit), name));
                }
                else {
                    edges.put(name, new Edge(nodes.get(entry), nodes.get(exit), name));
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getRoutesFromFile(String filename) {
        String line;

        //System.out.println("Loading routes...");
        try {
            FileReader routesFile = new FileReader(filename);
            BufferedReader routesFileInput = new BufferedReader(routesFile);

            while ((line = routesFileInput.readLine()) != null){
                List<Edge> edgesList = new LinkedList<>();
                StringTokenizer tokenizer = new StringTokenizer(line);
                String currentNode = tokenizer.nextToken();
                while (tokenizer.hasMoreTokens()){
                    String nextNode = tokenizer.nextToken();
                    edgesList.add(edges.get(currentNode + '-' + nextNode));
                    currentNode = nextNode;
                }

                routes.add(new Route(edgesList));
                runways.add(nodes.get(currentNode));

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Node getEntryNode(int entryNodeNumber){
        switch (entryNodeNumber){
            case 1 : return nodes.get("MOPAR");
            case 2 : return nodes.get("LORNI");
            case 3 : return nodes.get("OKIPA");
            case 4 : return nodes.get("BANOX");
            default: throw new Error("The entry node number doesn't match exist");
        }
    }

    public Node getRunway(String runway){
        return nodes.get(runway);
    }


    public Node getADifferentRunway(Node runway){
        Optional<Node> result = runways.stream().filter(node -> node!= runway).findAny();
        if(result.isPresent()){
            return result.get();
        }else {
            throw new Error("Can't find a different runway");
        }
    }

    /**
     Get THE Route between a entry Node and an exit Node
     @param entry The entry point (Node) in TMA
     @param runway The exit point (Node) ie a runway
     @return a Route such as specified in Route
     */
    public Route getRoute(Node entry, Node runway){
        for (Route route : routes) {
            if (route.getFirstNode() == entry && route.getLastNode() == runway) {
                return route;
            }
        }
        throw new Error("try to find a route that doesn't exist");
    }


    /**
     Add an aircraft to the graph, to the Nodes and Edges specified in aircraft.getRoute()
     @param aircraft : the aircraft to add
     */
    public void addAircraft(@NotNull Aircraft aircraft){
        double time = aircraft.getTimeIn();
        aircraft.getEntry().addAircraft(aircraft, time); //ajout de l'avion au point d'entré
        for(Edge edge : aircraft.getRoute().getEdges()){
            edge.addFlyingAircraft(aircraft, time); //l'avion est ajouté à tous les points du parcours sauf l'entré
            time += edge.getLength() / aircraft.getSpeed() * Constants.HOURS_TO_SEC; // temps au passage strict du centre du noeud
            edge.getExitNode().addAircraft(aircraft, time);
        }
    }

    /**
     Remove an aircraft to the graph, to the Nodes and Edges specified in aircraft.getRoute()
     @param aircraft : the aircraft to remove
     */
    public void removeAircraft(Aircraft aircraft){
        double time= aircraft.getTimeIn();
        aircraft.getEntry().removeAircraft(time);
        for(Edge edge : aircraft.getRoute().getEdges()){
            edge.removeAircraft(time);
            time += edge.getLength() / aircraft.getSpeed() * Constants.HOURS_TO_SEC;
            edge.getExitNode().removeAircraft(time);
        }
    }

    public HashMap<String, Node> getNodes() {
        return nodes;
    }
}

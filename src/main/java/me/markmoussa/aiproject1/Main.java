package me.markmoussa.aiproject1;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        // gets input and reads in files
        getInput();
    }

    private static void getInput() {
        // getting input from user
        String locationsFilePath, connectionsFilePath;
        String startCity = null, endCity = null;
        ArrayList<String> excludedCities = new ArrayList<>();
        // default to 0 so compiler will shut up
        int heuristic = 0;
        int solutionType = 0;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter full path of locations.txt");
        // debugging
//        locationsFilePath = "src/main/resources/locations.txt";
        locationsFilePath = scanner.nextLine();
        HashMap<String, ArrayList<String>> locationsFileContent = readFile(locationsFilePath, "locations");
        System.out.println("Enter full path of connections.txt");
        // debugging
//        connectionsFilePath = "src/main/resources/connections.txt";
        connectionsFilePath = scanner.nextLine();

        String temp;
        boolean flag = true;
        // debugging
//        startCity = "D4";

        while(flag) {
            System.out.println("Enter name of starting city");
            startCity = scanner.nextLine();
            if(locationsFileContent.containsKey(startCity)) {
                flag = false;
            } else {
                System.out.println("Sorry, that city is not in this graph. Please try again.");
            }
        }
        // debugging
//        endCity = "G5";

        flag = true;
        while(flag) {
            System.out.println("Enter name of end city");
            endCity = scanner.nextLine();
            if(locationsFileContent.containsKey(endCity)) {
                flag = false;
            } else {
                System.out.println("Sorry, that city is not in this graph. Please try again.");
            }
        }

        flag = true;
        while(flag) {
            System.out.println("Enter names of cities you want to exclude (end by pressing \"ENTER\")");
            String input = scanner.nextLine();
            if(!(input.isEmpty())) {
                excludedCities.add(input);
            } else if(input.equals(startCity) || input.equals(endCity)) {
                System.out.println("Can't exclude the start city or end city. Try again");
            } else {
                flag = false;
            }
        }

        // debugging
//        heuristic = 1;
        flag = true;

        while(flag) {
            System.out.println("Enter name of heuristic to use (\"straight line distance\" or \"fewest links\")");
            temp = scanner.nextLine();
            temp = temp.toLowerCase();
            if(temp.equals("straight line distance")) {
                heuristic = 0;
                flag = false;
            } else if(temp.equals("fewest links")) {
                heuristic = 1;
                flag = false;
            } else {
                System.out.println("Not a valid option. Please try again.");
            }
        }
        // debugging
//        solutionType = 0;
        flag = true;
        while(flag) {
            System.out.println("Enter type of solution you want shown (\"step by step\" or \"optimal path\")");
            temp = scanner.nextLine();
            temp = temp.toLowerCase();
            if (temp.equals("step by step")) {
                solutionType = 0;
                flag = false;
            } else if (temp.equals("optimal path")) {
                solutionType = 1;
                flag = false;
            } else {
                System.out.println("Not a valid option. Please try again.");
            }
        }
        HashMap<String, ArrayList<String>> connectionsFileContent = readFile(connectionsFilePath, "connections");
        for(String city : excludedCities) {
            if(locationsFileContent.containsKey(city)) {
                locationsFileContent.remove(city);
            }
            if(connectionsFileContent.containsKey(city)) {
                connectionsFileContent.remove(city);
            }
            for(Map.Entry<String, ArrayList<String>> entry : connectionsFileContent.entrySet()) {
                entry.getValue().removeIf(value -> value.equals(city));
            }
        }
        DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> connectionsGraph = buildGraph(connectionsFileContent, locationsFileContent, heuristic);
        aStarAlgorithm(connectionsGraph, locationsFileContent, startCity, endCity, heuristic, solutionType);


    }

    // Reads in each line of file in form of HashMap with key = start vertex and
    // value being ArrayList<String> of coordinates (locations.txt) or connections (connections.txt)
    private static HashMap<String, ArrayList<String>> readFile(String filePath, String fileType) {
        // Using Object because I need ArrayList<Integer> for one and ArrayList<String> for the other
        String line = null;
        HashMap<String, ArrayList<String>> fileContents = new HashMap<>();
        if(fileType.equals("locations")) {
            try {
                FileReader fileReader = new FileReader(filePath);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                while(!(line = bufferedReader.readLine()).equals("END")) {
                    ArrayList<String> splitArray = new ArrayList<>(Arrays.asList(line.split("\\s+")));
                    String city = splitArray.get(0);
                    splitArray.remove(0);
                    // debugging
//                    System.out.println(splitArray);

                    /* Commenting this out because it helps to just keep HashMap to have ArrayList<String> to
                     get over some weird stuff with generic types and ArrayLists
                     Just convert to int when we actually need it instead */
//                    ArrayList<Integer> coordinatesArray = new ArrayList<>();
//                    for(String s: splitArray) {
//                        coordinatesArray.add(Integer.parseInt(s));
//                    }
                    fileContents.put(city, splitArray);
                }
            } catch(FileNotFoundException exception) {
                System.out.println("File not found. Exiting");
                System.exit(1);
            } catch(IOException exception) {
                System.out.println("IO Exception. Exiting");
                System.exit(1);
            }
        } else if(fileType.equals("connections")) {
            try {
                FileReader fileReader = new FileReader(filePath);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                while(!(line = bufferedReader.readLine()).equals("END")) {
                    ArrayList<String> splitArray = new ArrayList<>(Arrays.asList(line.split("\\s+")));
                    String city= splitArray.get(0);
                    splitArray.remove(0);
                    splitArray.remove(0);
                    // debugging
//                    System.out.println(splitArray);
                    fileContents.put(city, splitArray);
                }
            } catch(FileNotFoundException exception) {
                System.out.println("File not found. Exiting");
                System.exit(1);
            } catch(IOException exception) {
                System.out.println("IO Exception. Exiting");
                System.exit(1);
            }
        }
        return fileContents;
    }


    // Finds straight line distance. Used to set weight of edges
    private static double euclideanDistance(ArrayList<String> start, ArrayList<String> end) {
        int[] startInt = new int[2];
        int[] endInt = new int[2];
        for(int i = 0; i < start.size(); i++) {
            startInt[i] = Integer.parseInt(start.get(i));
        }
        for(int i = 0; i < end.size(); i++) {
            endInt[i] = Integer.parseInt(end.get(i));
        }
        return Math.sqrt((Math.pow((endInt[0] - startInt[0]), 2)) + (Math.pow((endInt[1] - startInt[1]), 2)));
    }


    // Builds the graph based on connections from connections.txt and weights from locations.txt
    private static DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> buildGraph(HashMap<String, ArrayList<String>> connectionsContent, HashMap<String, ArrayList<String>> locationsContent, int heuristic) {
        DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        double edgeWeight;
        for(HashMap.Entry<String, ArrayList<String>> entry : connectionsContent.entrySet()) {
            String startingVertex = entry.getKey();
            if(!(graph.containsVertex(startingVertex))) {
                graph.addVertex(startingVertex);
            }
            ArrayList<String> values = entry.getValue();
            for(String value : values) {
                if(!(graph.containsVertex(value))) {
                    graph.addVertex(value);
                }
                if(!(graph.containsEdge(startingVertex, value))) {
                    DefaultWeightedEdge edge = graph.addEdge(startingVertex, value);
                    // for the "fewest links" heuristic, the only edge weights are 1, so need to take that into account
                    if(heuristic == 0) {
                        edgeWeight = euclideanDistance(locationsContent.get(startingVertex), locationsContent.get(value));
                        graph.setEdgeWeight(edge, edgeWeight);
                    } else {
                        graph.setEdgeWeight(edge, 1);
                    }
                } else {
                    System.out.println("For some reason this graph already contains that edge? Line 160");
                }
            }
        }
        // debugging : printing newly created graph's edges
//        Set<DefaultWeightedEdge> edges = graph.edgeSet();
//
//        for (DefaultWeightedEdge e : edges) {
//            System.out.println((String.format("\"%s\" -> \"%s\"", graph.getEdgeSource(e), graph.getEdgeTarget(e))));
//        }


        return graph;
    }

    private static void aStarAlgorithm(DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> graph, HashMap<String, ArrayList<String>> locationsContent, String startCity, String endCity, int heuristic, int solutionType) {
        HeuristicComparator comparator = new HeuristicComparator();
        PriorityQueue<QueueNode> open = new PriorityQueue<>(30, comparator);
        ArrayList<QueueNode> closed = new ArrayList<>();
        // f-value = distance from starting node + heuristic value
        // adding starting node
        QueueNode startingNode = new QueueNode(startCity, graph.edgesOf(startCity), 0, 0);
        LinkedList<QueueNode> path = new LinkedList<>();
        path.add(startingNode);
        startingNode.setPath(path);
        open.add(startingNode);

        /* testing this out
        adding path pathway, and at beginning of each while loop once we've got the open node
        I check if the edge between the last node in "path" and this new node exist
        if not, that means we've backtracked and I keep removing from "path" until that path exists */

        // pop value from Priority Queue (me.markmoussa.aiproject1.QueueNode)
        // checking if empty to know when to stop
        while(!(open.isEmpty())) {
            QueueNode node = open.poll();
            // If step by step solution selected, need to do this before proceeding
            if(solutionType == 0) {
                printStepByStep(node);
            }

            // checking to see if we've reached the target node
            if(node.getVertex().equals(endCity)) {
                closed.add(node);
                if(solutionType == 1) {
                    printOptimalPath(node);
                }
                break;
            }
            // This differs from open because it takes the QueueNodes from the edges of the newly popped node and figures out
            // which node has the min f-value so it can go into the open queue
            PriorityQueue<QueueNode> toGoToOpen = new PriorityQueue<>(11, comparator);
            // looping over edges of popped node to see which node we can go to next
            // creating list of the possible nodes current node can go to
            for(DefaultWeightedEdge edge : graph.outgoingEdgesOf(node.getVertex())) {
                String newCityName = graph.getEdgeTarget(edge);

                double newG = node.getG() + graph.getEdgeWeight(edge);
                // checking if possible node has any edges it can go to or if it's a dead end
                if(!(graph.outgoingEdgesOf(newCityName).isEmpty())) {
                    if(heuristic == 0) {
                        QueueNode newNode = new QueueNode(newCityName, graph.outgoingEdgesOf(newCityName), newG , newG + straightLineHeuristic(locationsContent, newCityName, endCity));
                        // supressing warning for now because YOLO
                        @SuppressWarnings("unchecked")
                        LinkedList<QueueNode> newNodePath = (LinkedList<QueueNode>) node.getPath().clone();
                        newNodePath.add(newNode);
                        newNode.setPath(newNodePath);
                        toGoToOpen.add(newNode);
                    } else {
                        // fewest links heuristic is literally just adding 1 to the distance traveled for some reason, so just
                        // adding 1 instead of making a whole other function
                        QueueNode newNode = new QueueNode(newCityName, graph.outgoingEdgesOf(newCityName), newG , newG + 1);
                        @SuppressWarnings("unchecked")
                        LinkedList<QueueNode> newNodePath = (LinkedList<QueueNode>) node.getPath().clone();
                        newNodePath.add(newNode);
                        newNode.setPath(newNodePath);
                        toGoToOpen.add(newNode);
                    }
                }
            }

            // this while loop looks in toGoToOpen for the node with min f-value that also is not in open or closed
            boolean nodePresentInClosed;
            boolean nodePresentInOpen;
            boolean flag = true;
            // while(flag) {
            // debugging this
            while(!(toGoToOpen.isEmpty())) {
                QueueNode newNode;
                // getting node with min f-value from toGoToOpen priority queue
                if(!(toGoToOpen.isEmpty())) {
                    newNode = toGoToOpen.poll();
                } else {
                    flag = false;
                    break;
                }
                // g is set to take the distance from previous node and add it to edge weight to get from previous node to it
                // checking if possible node is in closed or already in open
                nodePresentInClosed = false;
                for(QueueNode j : closed) {
                    if(j.getVertex().equals(newNode.getVertex())) {
                        nodePresentInClosed = true;
                        break;
                    }
                }
                nodePresentInOpen = false;
                for(QueueNode j : open) {
                    if(j.getVertex().equals(newNode.getVertex())) {
                        nodePresentInOpen = true;
                        break;
                    }
                }
                if((!nodePresentInClosed) && (!nodePresentInOpen)) {
                    flag = false;
                    open.add(newNode);
                }
            }

            // checking if current node is in closed (this time so we don't add it to closed a million times and mess up the path)
            nodePresentInClosed = false;
            for(QueueNode i : closed) {
                if(i.getVertex().equals(node.getVertex())) {
                    nodePresentInClosed = true;
                }
            }
            if(!nodePresentInClosed) {
                closed.add(node);
            }
        }
    }

    // honestly just a wrapper around the euclidean distance function
    private static double straightLineHeuristic(HashMap<String, ArrayList<String>> locationsContent, String source, String target) {
        return euclideanDistance(locationsContent.get(source), locationsContent.get(target));
    }

    private static void printOptimalPath(QueueNode endNode) {
        LinkedList<QueueNode> path = endNode.getPath();
        for(int i = 0; i < path.size(); i++) {
            System.out.print(path.get(i).getVertex());
            if(i != (path.size() - 1)) {
                System.out.print(" -> ");
            }
        }
    }


    private static void printStepByStep(QueueNode newNode) {
        System.out.println("Current Optimal Path: ");
        LinkedList<QueueNode> path = newNode.getPath();
        for(int i = 0; i < path.size(); i++) {
            System.out.print(path.get(i).getVertex());
            if(i != (path.size() - 1)) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
        System.out.println("Distance traveled: " + newNode.getG());

        // pressing enter to continue
        boolean flag = true;
        Scanner scanner = new Scanner(System.in);
        String enter;
        while(flag) {
            System.out.println("Press the \"ENTER\" key to continue.");
            enter = scanner.nextLine();
            if(enter.isEmpty()) {
                flag = false;
            } else {
                System.out.println("Sorry, you must ONLY press ENTER to continue. Try again!");
            }
        }

    }

}

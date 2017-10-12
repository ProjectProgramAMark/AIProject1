import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.traverse.BreadthFirstIterator;

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
        String[] excludedCities;
        int heuristic;
        int solutionType;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter full path of locations.txt");
        // debugging
        locationsFilePath = "src/main/resources/locations.txt";
        HashMap<String, ArrayList<String>> locationsFileContent = readFile(locationsFilePath, "locations");
//        locationsFilePath = scanner.nextLine();
        System.out.println("Enter full path of connections.txt");
        // debugging
        connectionsFilePath = "src/main/resources/connections.txt";
//        connectionsFilePath = scanner.nextLine();
        HashMap<String, ArrayList<String>> connectionsFileContent = readFile(connectionsFilePath, "connections");
        DefaultDirectedWeightedGraph connectionsGraph = buildGraph(connectionsFileContent, locationsFileContent);
        String temp;
        boolean flag = true;
        // debugging
        startCity = "A1";
//        while(flag) {
//            System.out.println("Enter name of starting city");
//            startCity = scanner.nextLine();
//            if(locationsFileContent.containsKey(startCity)) {
//                flag = false;
//            } else {
//                System.out.println("Sorry, that city is not in this graph. Please try again.");
//            }
//        }
        // debugging
        endCity = "G2b";
        flag = true;
//        while(flag) {
//            System.out.println("Enter name of end city");
//            endCity = scanner.nextLine();
//            if(locationsFileContent.containsKey(endCity)) {
//                flag = false;
//            } else {
//                System.out.println("Sorry, that city is not in this graph. Please try again.");
//            }
//        }
        // debugging
        heuristic = 0;
        flag = true;

//        while(flag) {
//            System.out.println("Enter name of heuristic to use (\"straight line distance\" or \"fewest links\")");
//            temp = scanner.nextLine();
//            temp = temp.toLowerCase();
//            if(temp.equals("straight line distance")) {
//                heuristic = 0;
//                flag = false;
//            } else if(temp.equals("fewest links")) {
//                heuristic = 1;
//                flag = false;
//            } else {
//                System.out.println("Not a valid option. Please try again.");
//            }
//        }
        // debugging
        solutionType = 0;
        flag = true;
//        while(flag) {
//            System.out.println("Enter type of solution you want shown (\"step by step\" or \"optimal path\")");
//            temp = scanner.nextLine();
//            temp = temp.toLowerCase();
//            if (temp.equals("step by step")) {
//                solutionType = 0;
//                flag = false;
//            } else if (temp.equals("optimal path")) {
//                solutionType = 1;
//                flag = false;
//            } else {
//                System.out.println("Not a valid option. Please try again.");
//            }
//        }

        aStarAlgorithm(connectionsGraph, startCity, endCity);


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
    private static DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> buildGraph(HashMap<String, ArrayList<String>> connectionsContent, HashMap<String, ArrayList<String>> locationsContent) {
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
                    edgeWeight = euclideanDistance(locationsContent.get(startingVertex), locationsContent.get(value));
                    graph.setEdgeWeight(edge, edgeWeight);
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

    private static void aStarAlgorithm(DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> graph, String startCity, String endCity) {
        PriorityQueue<>
        ArrayList<String> closed = new ArrayList<>();
        BreadthFirstIterator<String, DefaultWeightedEdge> iterator = new BreadthFirstIterator<>(graph, startCity);
//        while(iterator.hasNext()) {
//            open.add(iterator.next());
//        }
//        System.out.println("Open array: \n" + open.toString());

    }

    double straightLineHeuristic() {

        return 0;
    }

}

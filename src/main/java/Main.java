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
        String startCity, endCity;
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
        DefaultDirectedWeightedGraph connectionsGraph = buildGraph(connectionsFileContent);
        System.out.println("Enter name of starting city");
        startCity = scanner.nextLine();
        System.out.println("Enter name of end city");
        endCity = scanner.nextLine();
        String temp;
        boolean flag = true;
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
    }

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

    private static DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> buildGraph(HashMap<String, ArrayList<String>> fileContents) {
        DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        //debugging
        int edgeWeight = 0;
        for(HashMap.Entry<String, ArrayList<String>> entry : fileContents.entrySet()) {
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
                    // SET EDGE WEIGHT HERE, edgeWeight for debugging
                    graph.setEdgeWeight(edge, edgeWeight);
                    // debugging
                    edgeWeight++;
                } else {
                    System.out.println("For some reason this graph already contains that edge? Line 144");
                }
            }
        }
        // debugging : printing newly created graph's edges
        Set<DefaultWeightedEdge> edges = graph.edgeSet();

        for (DefaultWeightedEdge e : edges) {
            System.out.println((String.format("\"%s\" -> \"%s\"", graph.getEdgeSource(e), graph.getEdgeTarget(e))));
        }


        return graph;
    }


}

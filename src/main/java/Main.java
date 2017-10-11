package main.java;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
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
        locationsFilePath = scanner.nextLine();
        System.out.println("Enter full path of connections.txt");
        connectionsFilePath = scanner.nextLine();
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



}

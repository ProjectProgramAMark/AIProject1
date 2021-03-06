package me.markmoussa.aiproject1;

import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.LinkedList;
import java.util.Set;

public class QueueNode {
    private String vertex;
    private Set<DefaultWeightedEdge> edges;
    private double f;
    private double g;
    private LinkedList<QueueNode> path;
    public QueueNode(String vertex, Set<DefaultWeightedEdge> edges, double g, double f) {
        this.vertex = vertex;
        this.edges = edges;
        this.g = g;
        this.f = f;
    }

    public String getVertex() {
        return vertex;
    }

    public void setVertex(String vertex) {
        this.vertex = vertex;
    }

    public Set<DefaultWeightedEdge> getEdges() {
        return edges;
    }

    public void setEdges(Set<DefaultWeightedEdge> edges) {
        this.edges = edges;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    public LinkedList<QueueNode> getPath() {
        return path;
    }

    public void setPath(LinkedList<QueueNode> path) {
        this.path = path;
    }
}

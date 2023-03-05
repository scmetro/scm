package scm;

import java.io.*;
import java.util.*;

/*--- raw implementation of Dijkstra algorithm(BOJ) ---*/

class Node implements Comparable<Node>{
    int to, weight;

    public Node(int to, int weight){
        this.to = to;
        this.weight = weight;
    }

    public int compareTo(Node n){
        return weight - n.weight;
    }
}

class Dijkstra{
    private static final BufferedReader BR = new BufferedReader(new InputStreamReader(System.in));
    private static final int INF = Integer.MAX_VALUE;
    private int[] dist;
    private int[][] graph;
    private int vertexSize, edgeSize, st;

    public Dijkstra(int[] dist, int vsize, int esize, int st){
        this.dist = dist;
        this.vertexSize = vsize;
        this.edgeSize = esize;
        this.st = st;
        this.graph = new int[vertexSize + 1][vertexSize + 1];
    }

    public void makeGraph() throws IOException{
        for (int i = 0; i < edgeSize; i++){
            StringTokenizer ST = new StringTokenizer(BR.readLine());
            int from = Integer.parseInt(ST.nextToken());
            int to = Integer.parseInt(ST.nextToken());
            int tmpDist = Integer.parseInt(ST.nextToken());
            graph[from][to] = tmpDist; 
        }
    }
    
    public void search(){
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.offer(new Node(st, 0));
        boolean[] visited = new boolean[vertexSize + 1];
        dist[st] = 0;

        while(!pq.isEmpty()){
            Node curr = pq.poll();
            if (visited[curr.to]) continue;
            visited[curr.to] = true;
            for (int i = 1; i <= vertexSize; i++){
                if (graph[curr.to][i] == 0) continue;
                if (dist[i] > dist[curr.to] + graph[curr.to][i]){
                    dist[i] = dist[curr.to] + graph[curr.to][i];
                    pq.offer(new Node(i, dist[i]));
                }
            }
        }
    }

    public void printDist(){
        for (int i = 1; i <= vertexSize; i++){
            if (dist[i] == INF) System.out.println("INF");
            else System.out.println(dist[i]);
        }
    }
}

public class SearchPath {
    private static final BufferedReader BR = new BufferedReader(new InputStreamReader(System.in));
    private static final int INF = Integer.MAX_VALUE;
    public static int v, e, k;

    public static void main(String[] args) throws IOException{
        StringTokenizer ST = new StringTokenizer(BR.readLine());
        v = Integer.parseInt(ST.nextToken());
        e = Integer.parseInt(ST.nextToken());
        k = Integer.parseInt(BR.readLine());
        int[] tmpDist = new int[v + 1];
        for (int i = 1; i <= v; i++){
            tmpDist[i] = INF;
        }

        Dijkstra d = new Dijkstra(tmpDist, v, e, k);
        d.makeGraph();
        d.search();
        d.printDist();
    }
}

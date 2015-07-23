package ads_001.gamsvr;

import java.io.*;
import java.util.*;

/**
 * Created by Victor Artemjev on 20.06.2015.
 */
public class Gamsvr {

    private static final String FILE_NAME_IN = "gamsvr.in";
    private static final String FILE_NAME_OUT = "gamsvr.out";

    private static int nodeCount;
    private static int clientCount;

    private static Network network;

    public static void readFromFile() {
        String[] data = null;
        try (FileReader fileReader = new FileReader(FILE_NAME_IN);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            // read first line (number of nodes and connections)
            data = bufferedReader.readLine().split(" ");
            nodeCount = Integer.parseInt(data[0]);

            //read second line (numbers of nodes which are clients)
            Node[] nodes = new Node[nodeCount];
            data = bufferedReader.readLine().split(" ");
            clientCount = data.length;
            for (String value : data) {
                int nodeId = Integer.parseInt(value) - 1;
                nodes[nodeId] = new Node(nodeId, true);
            }

            for (int i = 0; i < nodes.length; i++) {
                if (nodes[i] == null) {
                    nodes[i] = new Node(i, false);
                }
            }

            // read the rest of lines (start Node, end Node, latency)
            List<Connection> connections = new ArrayList<>();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                data = line.split(" ");
                int startNodeId = Integer.parseInt(data[0]) - 1;
                int endNodeId = Integer.parseInt(data[1]) - 1;
                long latency = Long.parseLong(data[2]);

                Connection connection = new Connection(nodes[startNodeId], nodes[endNodeId], latency);
                nodes[startNodeId].outboundConnections.add(connection);

                Connection reverseConnection = new Connection(nodes[endNodeId], nodes[startNodeId], latency);
                nodes[endNodeId].outboundConnections.add(reverseConnection);

                connections.add(connection);
                connections.add(reverseConnection);
            }
            network = new Network(nodes, connections);

        } catch (IOException ex) {
            System.err.format("IOException: %s%n", ex);
        }
    }

    private static void writeToFile(long value) {
        try (FileWriter fileWriter = new FileWriter(FILE_NAME_OUT);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(String.valueOf(value));
        } catch (IOException ex) {
            System.err.format("IOException: %s%n", ex);
        }
    }

    private static long[] dijkstra(Network network, Node startNode) {
        final long[] latencies = new long[network.nodes.length];
        long maxLatency = Long.MAX_VALUE;
        for (int i = 0; i < latencies.length; i++) {
            latencies[i] = maxLatency;
        }
        latencies[startNode.id] = 0;

        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(
                new Comparator<Node>() {
            @Override
            public int compare(Node node1, Node node2) {
                return Long.compare(latencies[node1.id], latencies[node2.id]);
            }
        });
        priorityQueue.add(startNode);

        while (!priorityQueue.isEmpty()) {

            Node shortestLatencyNode;
            while (true) {
                shortestLatencyNode = priorityQueue.poll();
                long latency = latencies[shortestLatencyNode.id];
                if (latency != Long.MAX_VALUE) {
                    break;
                }
            }

            for (Connection connection : shortestLatencyNode.outboundConnections) {
                long alternativeLatency = latencies[shortestLatencyNode.id] + connection.latency;
                if (alternativeLatency < latencies[connection.endNode.id]) {
                    latencies[connection.endNode.id] = alternativeLatency;
                    priorityQueue.add(connection.endNode);
                }
            }
        }

//        ArrayList<Node> visitList = new ArrayList<>(Arrays.asList(network.nodes));
//
//        while (!visitList.isEmpty()) {
//            int shortestLatencyIndex = 0;
//            Node shortestLatencyNode = visitList.get(shortestLatencyIndex);
//
//            for (int i = 0; i < visitList.size(); i++) {
//                if (latencies[visitList.get(i).id] < latencies[shortestLatencyIndex]) {
//                    shortestLatencyNode = visitList.get(i);
//                    shortestLatencyIndex = i;
//                }
//            }
//
//            visitList.remove(shortestLatencyIndex);
//
//            for(Connection connection : shortestLatencyNode.outboundConnections) {
//                long alternativeLatency = latencies[shortestLatencyNode.id] + connection.latency;
//                if (alternativeLatency < latencies[connection.endNode.id]) {
//                    latencies[connection.endNode.id] = alternativeLatency;
//                }
//            }
//        }
            return latencies;
    }

    private static long getMaxLatencyToClient(long[] latencies) {
        for (int i = 0; i < latencies.length; i++) {
            Node node = network.nodes[i];
            if (!node.isClient) {
                latencies[i] = 0;
            }
        }
        Arrays.sort(latencies);
        return latencies[latencies.length - 1];
    }

    private static long getMostMinimumLatency() {
        long[] latencyToClients = new long[network.nodes.length - clientCount];
        int latencyCount = 0;
        for (Node node : network.nodes) {
            if (!node.isClient) {
                long[] latencies = dijkstra(network, node);
                latencyToClients[latencyCount++] = getMaxLatencyToClient(latencies);
            }
        }
        Arrays.sort(latencyToClients);
        return latencyToClients[0];
    }

    private static class Node {
        public int id;
        public boolean isClient;
        public List<Connection> outboundConnections = new ArrayList<>();

        public Node(int id, boolean isClient) {
            this.id = id;
            this.isClient = isClient;
        }
    }

    private static class Connection {
        public final Node startNode;
        public final Node endNode;
        public final long latency;

        public Connection(Node startNode, Node endNode, long latency) {
            this.startNode = startNode;
            this.endNode = endNode;
            this.latency = latency;
        }

        @Override
        public String toString() {
            return startNode.id + "======>" + latency + "=======>" + endNode.id;
        }
    }

    private static class Network {
        public Node[] nodes;
        public List<Connection> connections;

        public Network(Node[] nodes, List<Connection> connections) {
            this.nodes = nodes;
            this.connections = connections;
        }
    }

    public static void main(String[] args) {
        readFromFile();
        long mostMinimumLatency = getMostMinimumLatency();
        writeToFile(mostMinimumLatency);
    }
}

package ads_001.gamsrv;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Victor Artemjev on 20.06.2015.
 */
public class Gamsrv {

    private static final String FILE_NAME_IN = "gamsrv.in";
    private static final String FILE_NAME_OUT = "gamsrv.out";

    private static int nodeCount;
    private static int connectionCount;
    private static int clientCount;

    private static Network network;

    public static void readFromFile(String fileName) {
        String[] data = null;
        try (FileReader fileReader = new FileReader(fileName);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            // read first line (number of nodes and connections)
            data = bufferedReader.readLine().split(" ");
            nodeCount = Integer.parseInt(data[0]);
            connectionCount = Integer.parseInt(data[0]);

            //read second line (numbers of nodes which are clients)
            Node[] nodes = new Node[nodeCount];
            data = bufferedReader.readLine().split(" ");
            clientCount = data.length;
            for (String value : data) {
                int nodeId = Integer.parseInt(value) - 1;
                nodes[nodeId] = new Node(nodeId, true);
            }

            for (int i = 0; i < nodes.length; i++) {
                if(nodes[i] == null) {
                    nodes[i] = new Node(i, false);
                }
            }

            System.out.println(Arrays.toString(nodes));

            // read the rest of lines (start Node, end Node, latency)
            List<Connection> connections = new ArrayList<>();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                data = line.split(" ");
                int startNodeId = Integer.parseInt(data[0]) - 1;
                int endNodeId = Integer.parseInt(data[1]) - 1;
                int latency = Integer.parseInt(data[2]);

                Connection connection = new Connection(nodes[startNodeId], nodes[endNodeId], latency);
                nodes[startNodeId].outboundConnections.add(connection);

                Connection reverseConnection = new Connection(nodes[endNodeId], nodes[startNodeId], latency);
                nodes[endNodeId].outboundConnections.add(reverseConnection);

                connections.add(connection);
                connections.add(reverseConnection);
            }
            network = new Network(nodes, connections);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void writeToFile(String fileName, int value) {
        try (FileWriter fileWriter = new FileWriter(fileName);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(String.valueOf(value));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static int[] dijkstra(Network network, Node startNode) {

        int[] distances = new int[network.nodes.length];
        int maxDistance = Integer.MAX_VALUE;
        for (int i = 0; i < distances.length; i++) {
            distances[i] = maxDistance;
        }
        distances[startNode.id] = 0;

        ArrayList<Node> visitList = new ArrayList<>(Arrays.asList(network.nodes));
        System.out.println(visitList);

        while (!visitList.isEmpty()) {
            int shortestDistanceIndex = 0;
            Node shortestDistanceNode = visitList.get(shortestDistanceIndex);

            for (int i = 0; i < visitList.size(); i++) {
                if (distances[visitList.get(i).id] < distances[shortestDistanceIndex]) {
                    shortestDistanceNode = visitList.get(i);
                    shortestDistanceIndex = i;
                }
            }

            visitList.remove(shortestDistanceIndex);

            for(Connection connection : shortestDistanceNode.outboundConnections) {
                int alternativeLatency = distances[shortestDistanceNode.id] + connection.latency;
                if (alternativeLatency < distances[connection.endNode.id]) {
                    distances[connection.endNode.id] = alternativeLatency;
                }
            }
        }
        return distances;
    }

    // TODO: refactor
    private static int getMaxLatencyByRouter(int[] distances) {
        for (int i = 0; i < distances.length; i++) {
            Node node = network.nodes[i];
            if (!node.isClient) {
                distances[i] = 0;
            }
        }
        Arrays.sort(distances);
        return distances[distances.length - 1];
    }

    private static class Node {
        public int id;
        public boolean isClient;
        public List<Connection> outboundConnections = new ArrayList<>();

        public Node(int id, boolean isClient) {
            this.id = id;
            this.isClient = isClient;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "id=" + id +
                    ", isClient=" + isClient +
                    '}';
        }
    }

    private static class Connection {
        public final Node startNode;
        public final Node endNode;
        public final int latency;

        public Connection(Node startNode, Node endNode, int latency) {
            this.startNode = startNode;
            this.endNode = endNode;
            this.latency = latency;
        }

        @Override
        public String toString() {
            return "Connection{" +
                    "startNode=" + startNode +
                    ", endNode=" + endNode +
                    ", latency=" + latency +
                    '}';
        }
    }

    private static class Network {
        public Node[] nodes;
        public List<Connection> connections;

        public Network(Node[] nodes, List<Connection> connections) {
            this.nodes = nodes;
            this.connections = connections;
        }

        @Override
        public String toString() {
            return "\n Network{" +
                    "nodes=" + Arrays.toString(nodes) +
                    ", connections=" + connections +
                    '}';
        }
    }

    //TODO: refactor
    public static void main(String[] args) {
        readFromFile("E:\\Java\\Algorithms\\src\\ads_001\\gamsrv\\gamsrv.in");
        int[] routersLatencies = new int[network.nodes.length - 3];
        int countLatencies = 0;
        for (Node node : network.nodes) {
            if(!node.isClient) {
                int[] latencies = dijkstra(network, node);
                System.out.println(Arrays.toString(latencies));
                routersLatencies[countLatencies++] = getMaxLatencyByRouter(latencies);
            }
        }
        Arrays.sort(routersLatencies);
        int maxLatency = routersLatencies[0];
        System.out.println(maxLatency);
        writeToFile("E:\\Java\\Algorithms\\src\\ads_001\\gamsrv\\gamsrv.out", maxLatency);
    }
}

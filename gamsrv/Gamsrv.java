package ads_001.gamsrv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Victor Artemjev on 20.06.2015.
 */
public class Gamsrv {

    private static int numberOfNodes;
    private static int numberOfConnections;
    private static Network network;

    public static void readFromFile(String fileName) {
        String[] data = null;
        try (FileReader fileReader = new FileReader(fileName);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            // read first line (number of nodes and connections)
            data = bufferedReader.readLine().split(" ");
            numberOfNodes = Integer.parseInt(data[0]);
            numberOfConnections = Integer.parseInt(data[1]);

            // read second line (list of node which are clients)
//            List<Node> nodes = new ArrayList<>(numberOfNodes);
            Node[] nodes = new Node[numberOfNodes];
            data = bufferedReader.readLine().split(" ");
            for (String value : data) {
                int nodeNum = Integer.parseInt(value);
                nodes[nodeNum - 1] = new Node(nodeNum, true);
            }

            for (int i = 0; i < nodes.length; i++) {
                if(nodes[i] == null) {
                    nodes[i] = new Node(i + 1, false);
                }
            }

            // read the rest of lines (start Node, end Node, latency)
            Connection[] connections = new Connection[numberOfConnections];
            String line = null;
            int count = 0;
            while ((line = bufferedReader.readLine()) != null) {
                data = line.split(" ");
                int startNodeId = Integer.parseInt(data[0]) - 1;
                int endNodeId = Integer.parseInt(data[1]) - 1;
                int latency = Integer.parseInt(data[2]);
                Connection connection = new Connection(nodes[startNodeId], nodes[endNodeId], latency);
                connections[count++] = connection;
            }

            network = new Network(Arrays.asList(nodes), Arrays.asList(connections));

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    private static class Node {
        public int id;
        public boolean isClient;

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
        public List<Node> nodes;
        public List<Connection> connections;

        public Network(List<Node> nodes, List<Connection> connections) {
            this.nodes = nodes;
            this.connections = connections;
        }

        @Override
        public String toString() {
            return "\n Network{" +
                    "nodes=" + nodes +
                    ", connections=" + connections +
                    '}';
        }
    }

    public static void main(String[] args) {
        readFromFile("E:\\Java\\Algorithms\\src\\ads_001\\gamsrv\\gamsrv.in");
        System.out.println(network);
    }
}

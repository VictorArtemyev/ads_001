package ads_001.govern;

import java.io.*;
import java.util.*;

/**
 * Created by Victor Artemjev on 25.06.2015.
 */
public class Govern {

    private static final String FILE_NAME_IN = "govern.in";
    private static final String FILE_NAME_OUT = "govern.out";

    private static Government readFromFile() {
        String line = null;
        Government government = null;
        try (FileReader fileReader = new FileReader(FILE_NAME_IN);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            Map<String, Reference> referenceMap = new HashMap<>();
            List<Order> orders = new ArrayList<>();

            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(" ");
                if (data.length > 1) {
                    if (!referenceMap.containsKey(data[0])) {
                        referenceMap.put(data[0], new Reference(data[0]));
                    }
                    if (!referenceMap.containsKey(data[1])) {
                        referenceMap.put(data[1], new Reference(data[1]));
                    }

                    Order order = new Order(referenceMap.get(data[0]), referenceMap.get(data[1]));
                    referenceMap.get(data[0]).outboundOrders.add(order);
                    orders.add(order);
                } else {
                    referenceMap.put(data[0], new Reference(data[0]));
                }
            }

            government = new Government(referenceMap, orders);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return government;
    }

    private static void writeToFile(List<String> result) {
        try (FileWriter writer = new FileWriter(FILE_NAME_OUT)) {
            for (String referenceLabel : result) {
                writer.write(referenceLabel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getTopologicalOrder(Government government) {
        return DFS(government, getReferencesWithoutInboundOrders(government));
    }

    private static List<String> DFS(Government government, List<Reference> references) {
        List<String> result = new ArrayList<>();

        Map<Reference, Boolean> visitedMap = new HashMap<>();
        for (Reference reference : government.references.values()) {
            visitedMap.put(reference, false);
        }

        Stack<Reference> stack = new Stack<>();
        for (Reference reference : references) {
            stack.push(reference);
        }

        while (!stack.isEmpty()) {
            Reference currentReference = stack.get(stack.size() - 1);
            visitedMap.put(currentReference, true);
            List<Reference> neighbors = new ArrayList<>();
            for (Order order : currentReference.outboundOrders) {
                if (!visitedMap.get(order.endReference)) {
                    neighbors.add(order.endReference);
                    stack.push(order.endReference);
                }
            }

            if (neighbors.size() == 0) {
                result.add(currentReference.label + "\n");
                stack.pop();
            }
        }

        return result;
    }

    private static List<Reference> getReferencesWithoutInboundOrders(Government government) {
        List<Reference> result = new ArrayList<>();
        Map<Reference, Boolean> haveInboundsMap = new HashMap<>();
        for (Reference reference : government.references.values()) {
            haveInboundsMap.put(reference, false);
        }

        for (Order order : government.orders) {
            if (order.endReference != null) {
                haveInboundsMap.put(order.endReference, true);
            }
        }

        for (Reference reference : haveInboundsMap.keySet()) {
            if (!haveInboundsMap.get(reference)) {
                result.add(reference);
            }
        }
        return result;
    }

    private static class Reference {
        public String label;
        public List<Order> outboundOrders = new ArrayList<>();

        public Reference(String label) {
            this.label = label;
        }
    }

    private static class Order {
        public Reference startReference;
        public Reference endReference;

        public Order(Reference startReference, Reference endReference) {
            this.startReference = startReference;
            this.endReference = endReference;
        }
    }

    private static class Government {
        public Map<String, Reference> references;
        public List<Order> orders;

        public Government(Map<String, Reference> references, List<Order> orders) {
            this.references = references;
            this.orders = orders;
        }
    }

    public static void main(String[] args) {
        Government government = readFromFile();
        List<String> result = getTopologicalOrder(government);
        writeToFile(result);
    }
}

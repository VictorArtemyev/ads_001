package ads_001.huffmn;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Victor Artemyev on 15.07.2015.
 */
public class Huffmn {
    private static final String COMPRESS = "compress";
    private static final String DECOMPRESS = "decompress";
    private int[] frequencies;
    private Map<Byte, HuffmanLeaf> encodingTable;
    private int bitLength;

    public Huffmn(byte[] source) {
        this();
        init(source);
    }

    public Huffmn() {
        this.frequencies = new int[256];
        encodingTable = new HashMap<>();
    }

    public static void main(String[] args) {
        String operation = args[0];
        String inputFileName = args[1];
        String outputFileName = args[2];
        System.out.println(operation + " " + inputFileName + " " + outputFileName);

        Path inputPath = Paths.get(inputFileName);
        Path outputPath = Paths.get(outputFileName);

        switch (operation) {
            case COMPRESS:
                try {
                    byte[] data = Files.readAllBytes(inputPath);
                    Huffmn huffmn = new Huffmn(data);
                    byte[] compressedData = huffmn.compress(data);
                    Files.write(outputPath, compressedData);

                } catch (IOException e) {
                    System.err.format("IOException: %s%n", e);
                }
                break;
            case DECOMPRESS:

                try {
                    byte[] data = Files.readAllBytes(inputPath);
                    Huffmn huffmn = new Huffmn();
                    byte[] compressedData = huffmn.decompress(data);
                    Files.write(outputPath, compressedData);
                } catch (IOException e) {
                    System.err.format("IOException: %s%n", e);
                }
                break;
        }
    }

    private void init(byte[] source) {
        for (byte b : source) {
            frequencies[b]++;
        }
        PriorityQueue<HuffmanTree> trees = new PriorityQueue<>();
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] > 0) {
                trees.offer(new HuffmanLeaf(frequencies[i], (byte) i));
            }
        }
        while(trees.size() > 1) {
            trees.offer(new HuffmanNode(trees.poll(), trees.poll()));
        }
        HuffmanTree tree = trees.poll();
        code(tree, new StringBuilder());
    }

    private void code(HuffmanTree tree, StringBuilder prefix) {
        if (tree instanceof HuffmanLeaf) {
            HuffmanLeaf leaf = (HuffmanLeaf) tree;
            String prefixCode = (prefix.length() > 0) ? prefix.toString() : "0";
            leaf.setCode(prefixCode);
            encodingTable.put(leaf.value, leaf);
        }

        if (tree instanceof HuffmanNode) {
            HuffmanNode node = (HuffmanNode) tree;
            prefix.append('0');
            code(node.getLeft(), prefix);
            prefix.deleteCharAt(prefix.length() - 1);
            prefix.append('1');
            code(node.getRight(), prefix);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    public int getBitLength() {
        return bitLength;
    }

    public byte[] compress(byte[] originalData) {
        StringBuffer buffer = new StringBuffer();

        for (byte b : originalData) {
            buffer.append(encodingTable.get(b).getCode());
        }

        bitLength = buffer.length();
        List<Byte> bytes = new ArrayList<>();

        while (buffer.length() > 0) {
            while (buffer.length() < 8) {
                buffer.append('0');
            }
            String str = buffer.substring(0, 8);
            bytes.add((byte) Integer.parseInt(str, 2));
            buffer.delete(0, 8);
        }

        byte[] result = new byte[bytes.size()];

        for (int i = 0; i < bytes.size(); i++) {
            result[i] = bytes.get(i);
        }

        return result;
    }

    public byte[] decompress(byte[] compressedData) {
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < compressedData.length; i++) {
            String str = String.format(
                    "%8s",
                    Integer.toBinaryString(compressedData[i] & 0xFF))
                    .replace(' ', '0');
            buffer.append(str);
        }

//        String str = buffer.substring(0, bitLength);
        String str = buffer.substring(0, 3299269);
        List<Byte> bytes = new ArrayList<>();
        String code = "";
        while (str.length() > 0) {
            code += str.substring(0, 1);
            str = str.substring(1);
            Iterator<HuffmanLeaf> list = encodingTable.values().iterator();
            while (list.hasNext()) {
                HuffmanLeaf leaf = list.next();
                if (leaf.getCode().equals(code)) {
                    bytes.add(leaf.value);
                    code = "";
                    break;
                }
            }
        }
        byte[] result = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            result[i] = bytes.get(i);
        }
        return result;
    }
}


package ads_001.huffmn;

/**
 * Created by Victor Artemyev on 27.07.2015.
 */
public abstract class HuffmanTree implements Comparable<HuffmanTree>{

    private final int frequency;

    public HuffmanTree(int frequency) {
        this.frequency = frequency;
    }

    public int getFrequency() {
        return frequency;
    }

    @Override
    public int compareTo(HuffmanTree o) {
        return this.frequency - o.frequency;
    }
}

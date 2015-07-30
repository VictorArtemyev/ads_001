package ads_001.huffmn;

/**
 * Created by Victor Artemyev on 27.07.2015.
 */
public class HuffmanNode extends HuffmanTree {

    private final HuffmanTree left;
    private final HuffmanTree right;

    public HuffmanNode(HuffmanTree left,
                       HuffmanTree right) {
        super(left.getFrequency() +
                right.getFrequency());
        this.left = left;
        this.right = right;
    }

    public HuffmanTree getLeft() {
        return left;
    }

    public HuffmanTree getRight() {
        return right;
    }
}

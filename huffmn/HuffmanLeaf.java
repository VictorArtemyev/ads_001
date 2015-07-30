package ads_001.huffmn;

/**
 * Created by Victor Artemyev on 27.07.2015.
 */
public class HuffmanLeaf extends HuffmanTree {

    public final byte value;
    private String code;

    public HuffmanLeaf(int frequency, byte value) {
        super(frequency);
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

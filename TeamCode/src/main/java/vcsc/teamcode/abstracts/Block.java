package vcsc.teamcode.abstracts;

import com.qualcomm.hardware.limelightvision.LLResultTypes;

public class Block {
    private COLOR color;
    private double x, y;
    private LLResultTypes.ColorResult innerResult;

    public Block(COLOR color, double x, double y) {
        new Block(color, x, y, null);
    }

    public Block(COLOR color, double x, double y, LLResultTypes.ColorResult innerResult) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.innerResult = innerResult;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public LLResultTypes.ColorResult getInnerResult() {
        return innerResult;
    }

    public COLOR getColor() {
        return color;
    }

    public enum COLOR {
        RED, BLUE, YELLOW
    }
}

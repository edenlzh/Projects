package kalah;

/**
 * This class is used to implement a return type
 * which contains the final move position and
 * players' arrays*/
public class ReturnType {
    int finalPosition;
    int[][] NumArray;
    int[] numArrayOneD;
    public ReturnType(int finalPositionNum, int[][] NumberArray) {
        finalPosition = finalPositionNum;
        NumArray = NumberArray;
    }
    public ReturnType(int finalPositionNum, int[] numArray1D) {
        finalPosition = finalPositionNum;
        numArrayOneD = numArray1D;
    }
}
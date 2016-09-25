package in.apra.apraclock.tasks;

/**
 * Created by apra on 9/26/2016.
 */

public class RandomMathTask {
    int firstOp;
    int secondOp;
    int operator; //+, -, *

    public RandomMathTask(){
        firstOp=9+(int)(90.0*Math.random());
        secondOp=9+(int)(90.0*Math.random());
        operator=(int)(3.0*Math.random());
    }
    public void init(int op1, int op2, int opType)
    {
        firstOp=op1;
        secondOp=op2;
        operator=opType;
    }
    private int solve()
    {
        switch(operator){
            case 0: return firstOp+secondOp;
            case 1: return firstOp-secondOp;
            case 2: return firstOp*secondOp;
        }
        throw new RuntimeException("Unknown operator");
    }
    public boolean checkAnswer(int userAnswer)
    {
        return userAnswer==solve();
    }
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(firstOp);
        sb.append(" ");
        switch(operator){
            case 0: sb.append("+");
                break;
            case 1: sb.append("-");
                break;
            case 2: sb.append("*");
                break;
            default: throw new RuntimeException("Unknown Operator");
        }
        sb.append(" ");
        sb.append(secondOp);
        return sb.toString();
    }
}

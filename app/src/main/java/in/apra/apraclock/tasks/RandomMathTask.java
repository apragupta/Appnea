package in.apra.apraclock.tasks;

/**
 * RandomMathTask represents a single randomly generated arithmatic problem
 * with 2 operands and one of the 3 operators: +/-/x
 * Created by apra on 9/26/2016.
 */

public class RandomMathTask {
    // following 3 variables represents the state of this class
    int firstOp;
    int secondOp;
    int operator; //+, -, *

    /**
     * Constructor generates the random question by
     * selecting 2 random operands and
     * 1 random operator
     */
    public RandomMathTask(){
        //select 2 random numbers between 5 and 25
        firstOp=5+(int)(20.0*Math.random());
        secondOp=5+(int)(20.0*Math.random());
        //select a random number between 0 and 2. 0:ADD, 1:SUB, 2:MULT
        operator=(int)(2.5*Math.random()); //biased against multiplications
    }

    /**
     * Used only for unit tests to avoid random generation
     * @param op1
     * @param op2
     * @param opType
     */
    public void init(int op1, int op2, int opType)
    {
        firstOp=op1;
        secondOp=op2;
        operator=opType;
    }

    /**
     * Finds the correct answer to the math question
     * @return the solution
     */
    private int solve()
    {
        switch(operator){
            case 0: return firstOp+secondOp;
            case 1: return firstOp-secondOp;
            case 2: return firstOp*secondOp;
        }
        throw new RuntimeException("Unknown operator");
    }

    /**
     * checks users answer against correct answer
     * @param userAnswer
     * @return true: passed, false: failed
     */
    public boolean checkAnswer(int userAnswer)
    {
        return userAnswer==solve();
    }

    /**
     * Renders the stringified version of the puzzle to be shown on screen
     * @return String representation of the math puzzle
     */
    @Override
    public String toString()
    {
        switch(operator){
            case 0:  return firstOp + " + " + secondOp + " = ?";

            case 1: return firstOp + " - "  + secondOp + " = ?";

            case 2: return firstOp + " x "  + secondOp + " = ?";

            default: throw new RuntimeException("Unknown Operator");
        }


    }
}

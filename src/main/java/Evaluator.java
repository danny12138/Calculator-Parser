import java.math.BigInteger;
import java.util.Stack;

/**
 * Created by whizzmirray on 4/03/17.
 */

public class Evaluator {

    private static final double EPSILON = 1e-15;
    private static boolean deg = false;
    /**
     * Calculates the the Post Fix expression (Reverse polish expression) and if the result is less than EPSILON then it returns zero
     * @param expression A mathematical expression
     * @return the result of the expression
     * @throws Exception if and Operand is missing, divide by zero ect...
     */
    public static String evaluate(String expression) throws Exception{
        String[] postFix = Parser.toPostFix(expression);
        Stack<String> stack = new Stack<>();
        for (String s : postFix) {
            if (Parser.isOperand(s)) {//if the current token is a number push it to the stack
                stack.push(s);
            } else {//if token is an operator pop the stack according to the operators arity (Binary or Unary)
                if (Parser.isBinary(s) == Parser.BINARY) {
                    if (stack.size() < 2) throw new IllegalArgumentException("Missing operand");
                    Double b = Double.parseDouble(stack.pop());
                    Double a = Double.parseDouble(stack.pop());
                    stack.push(String.valueOf(BinaryEval(a, b, s)));
                } else {
                    if (stack.size() < 1) throw new IllegalArgumentException("Missing operand");
                    Double a = Double.parseDouble(stack.pop());
                    stack.push(String.valueOf(UnaryEval(a, s)));
                }
            }
        }
        String result = stack.pop();
        if(Math.abs(Double.parseDouble(result)) < EPSILON)//Error degree
            return "0.0";
        return result;
    }

    public static String evaluate(String expression,boolean deg) throws Exception{//TODO turn off scientific notation
        setDeg(deg);
        return evaluate(expression);
    }
    private static String BinaryEval(double a,double b,String op) throws Exception{
        if(op.equals("+"))
            return (a+b)+"";
        if(op.equals("-")) {
            return (a - b)+"";
        }
        if(op.equals("*"))
            return (a*b)+"";
        if(op.equals("/")) {
            if (b == 0) throw new IllegalArgumentException("Divide by 0");
            return (a / b)+"";
        }
        if(op.equals("^"))
            return Math.pow(a,b)+"";

        throw new IllegalArgumentException("Operation not found");//if non of the above where executed throw this
    }
    private static String UnaryEval(double a,String op){

        if(op.equals("$"))
            return -a+"";

        if(op.equals("cos"))
            return String.valueOf(deg ? Math.cos(Math.toRadians(a)) : Math.cos(a));

        if(op.equals("sin"))
            return String.valueOf(deg ? Math.sin(Math.toRadians(a)) : Math.sin(a));

        if(op.equals("tan"))
            return String.valueOf(deg ? Math.tan(Math.toRadians(a)) : Math.tan(a));

        if(op.equals("arccos"))
            return String.valueOf(deg ? Math.acos(Math.toRadians(a)) : Math.acos(a));

        if(op.equals("arcsin"))
            return String.valueOf(deg ? Math.asin(Math.toRadians(a)) : Math.asin(a));

        if(op.equals("arctan"))
            return String.valueOf(deg ? Math.atan(Math.toRadians(a)) : Math.atan(a));
        //ln, log and square root will return NaN if a is less than 0
        if(op.equals("ln"))
            return Math.log(a)+"";

        if(op.equals("log"))
            return  Math.log10(a)+"";

        if(op.equals("√"))
            return Math.sqrt(a)+"";

        if(op.equals("!")) {
            if(a > 120) throw new IllegalArgumentException("Number too big");
            if(isInt(a))
                return factorial((long) a);

            throw new IllegalArgumentException("Can't use factorial on floats");
        }
        if(op.equals("%"))
            return (a/100)+"";
        return "";
    }

    /**
     * Standard recursive factorial function but with BigInteger for more possibilities
     */
    private static BigInteger recfact(long n) {
        if(n == 0)
            return BigInteger.ONE;
        return BigInteger.valueOf(n).multiply(recfact(n-1));
    }
    private static String factorial(long n) {//TODO Tweak the retured factorial format for big numbers
        //NumberFormat formatter = new DecimalFormat("0.######E0", DecimalFormatSymbols.getInstance(Locale.ROOT));
        //String str = formatter.format(recfact(n));
        return recfact(n).toString();
    }

    public static boolean isInt(double a){//Checks if a is a floating number
        return (a == Math.floor(a) && !Double.isInfinite(a));
    }

    public static void setDeg(boolean deg) {
        Evaluator.deg = deg;
    }
}

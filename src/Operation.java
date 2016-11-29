/**
 * Created by yevgnen on 2016-11-28.
 */
public enum Operation {
  PLUS("+") {
    double apply(double x, double y) { return x + y; }
  }, Minus("-") {
    double apply(double x, double y) { return x - y; }
  }, TIMES("*") {
    double apply(double x, double y) { return x * y; }
  }, DIVIDE("/") {
    double apply(double x, double y) { return x / y; }
  };

  private final String symbol;
  Operation(String symbol) { this.symbol = symbol;}
  @Override public String toString(){ return symbol; }

  abstract double apply(double x, double y);

  public static void main(String[] args){
    double x = Double.parseDouble(args[0]);
    double y = Double.parseDouble(args[1]);
    for(Operation op : Operation.values())
      System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
  }
}

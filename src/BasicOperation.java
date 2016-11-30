/**
 * Created by yevgnen on 2016-11-30.
 */
public enum BasicOperation implements Operation {
  PLUS("+") {
    public double apply(double x, double y) { return x + y; }
  }, Minus("-") {
    public double apply(double x, double y) { return x - y; }
  }, TIMES("*") {
    public double apply(double x, double y) { return x * y; }
  }, DIVIDE("/") {
    public double apply(double x, double y) { return x / y; }
  };

  private final String symbol;
  BasicOperation(String symbol) { this.symbol = symbol;}
  @Override public String toString(){ return symbol; }
}

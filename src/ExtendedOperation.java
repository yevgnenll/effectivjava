/**
 * Created by yevgnen on 2016-11-30.
 */
public enum ExtendedOperation implements Operation{

  EXP("^"){
    public double apply(double x, double y){
      return Math.pow(x, y);
    }
  },
  REMAINER("%"){
    public double apply(double x, double y){
      return x % y;
    }
  };

  private final String symbol;

  ExtendedOperation(String symbol) {
   this.symbol = symbol;
  }
  @Override
  public String toString(){
    return symbol;
  }
}

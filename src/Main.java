import java.util.DoubleSummaryStatistics;

public class Main {

  public static void main(String[] args) {
    double x = Double.parseDouble(String.valueOf(100));
    double y = Double.parseDouble(String.valueOf(7));
    test(ExtendedOperation.class, x, y);
    System.out.println(ExtendedOperation.class);
  }

  private static <T extends Enum<T> & Operation> void test(
      Class<T> opSet, double x, double y ){
    for(Operation op : opSet.getEnumConstants())
      System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
  }

}

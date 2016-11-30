import java.util.Arrays;
import java.util.Collection;

public class Main {

  public static void main(String[] args) {
    double x = Double.parseDouble(String.valueOf(100));
    double y = Double.parseDouble(String.valueOf(7));
    test(Arrays.asList(ExtendedOperation.values()), x, y);
    System.out.println(ExtendedOperation.class);
  }

  private static void test(Collection<? extends Operation> opSet,
      double x, double y){
    for(Operation op : opSet)
      System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
  }

}

package effective.inner;

public class TestInner {

  int a = 10;
  public static void method(){
    System.out.println("inner method");
    class LocalClass{
      public void localPrint(){
        System.out.println("local class print");
      }
    }
  }

  public class Inner{
    public void prnt(){
      System.out.println("inner print");
    }
  }
}

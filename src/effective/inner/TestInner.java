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
  public static void main(String args[]){
    System.out.println(b);
  }

  private static int b = 255;

  public class Inner{
    public void prnt(){
      System.out.println("inner print");
    }
  }
}

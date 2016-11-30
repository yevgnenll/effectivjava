import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by yevgnen on 2016-11-30.
 */
public class RunTests {
  public static void main(String[] args) throws Exception{
    int tests = 0;
    int passed = 0;
    Class testClass = Class.forName(args[0]);
    for(Method m : testClass.getDeclaredMethods()){
      if(m.isAnnotationPresent(Test.class)){
        tests++;
        try {
          m.invoke(null);
          passed++;
        } catch (InvocationTargetException wrappedExc){
          Throwable exc = wrappedExc.getCause();
          Class<? extends Exception> excType =
              m.getAnnotation(ExceptionTest.class).value();
          System.out.println(m + " failed: " + exc);
        } catch (Exception exc){
          System.out.println("INVALID @Test: "+ m);
        }
      }
    }
    System.out.printf("passed: %d, Failed: %d%n", passed, tests- passed);
  }
}

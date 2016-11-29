import java.util.HashMap;
import java.util.Map;

/**
 * Created by yevgnen on 2016-11-23.
 */
public class HashCode {

  public static void main (String[] args){
    Map<Person, String> m = new HashMap<Person, String>();
    m.put(new Person("seungkwon", 29), "developer");
    System.out.println(m.get(new Person("seungkwon", 29)) );
  }

  short areaCode = 100;
  short prefix = 600;
  short lineNumber = 300;

  @Override
  public int hashCode(){
    int result = 17;
    result = result * 31 + areaCode;
    result = result * 31 + prefix;
    result = result * 31 + lineNumber;
    return result;
  }
}


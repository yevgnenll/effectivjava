import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by yevgnen on 2016-12-01.
 */
public class CollectionClassifier {

  public static String classify(Collection<?> c){
    return c instanceof Set ? "set" :
          c instanceof List ? "List" : "Unknow Collection";
  }

  public static void main(String[] args){
    Collection<?>[] collections = {
        new HashSet<String>(),
        new ArrayList<BigInteger>(),
        new HashMap<String, String>().values()
    };
    for(Collection<?> c : collections)
      System.out.println(classify(c));
  }
}

import java.util.AbstractList;
import java.util.List;

/**
 * Created by yevgnen on 2016-11-25.
 */
public class AbstractListEx {

  static List<Integer> intArrayAsList(final int[] a){
    if(a == null){
      throw new NullPointerException();
    }
    return new AbstractList<Integer>() {
      @Override
      public Integer get(int index) {
        return a[index];
      }

      @Override
      public int size() {
        return a.length;
      }

      @Override
      public Integer set(int i, Integer val){
        int oldVal = a[i];
        a[i] = val;
        return oldVal;
      }
    };
  }
}

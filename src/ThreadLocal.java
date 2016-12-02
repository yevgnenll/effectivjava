/**
 * Created by yevgnen on 2016-12-02.
 */
public class ThreadLocal {
  private ThreadLocal(){}

  public static class Key{
    Key(){}
  }
  public static Key getKey(){
    return new Key();
  }
  public static void set(Key key, Object value){}
  public static Object get(Key key){ return null; }
}

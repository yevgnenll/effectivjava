/**
 * Created by yevgnen on 2016-11-29.
 */

import effective.inner.TestInner;

public class InnerClass {

  public static void main(String args[]){
    TestInner t = new TestInner();
    TestInner.Inner i = t.new Inner();
    i.prnt();
  }
}

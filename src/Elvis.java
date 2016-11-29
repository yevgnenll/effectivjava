/**
 * Created by yevgnen on 2016-11-22.
 * singleton pattern example code
 */
public class Elvis {
  private static final Elvis INSTANCE = new Elvis();
  private Elvis() {}
  public static Elvis getInstance(){ return INSTANCE; }

  public void leaveTheBuilding(){

  }
}

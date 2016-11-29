/**
 * Created by yevgnen on 2016-11-23.
 */
public class Developer extends Person {

  public Developer(String name, int age) {
    super(name, age);
  }

  private int n = super.number;

  public static void main(String[] args){
    System.out.println();
  }

  @Override
  public Developer clone(){
    return (Developer) super.clone();
  }

}

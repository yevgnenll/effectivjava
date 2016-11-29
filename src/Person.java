/**
 * Created by yevgnen on 2016-11-22.
 */

public class Person  implements  Comparable{

  private String name;
  private int age;
  public int number = 10;

  public Person(String name, int age){
    this.name = name;
    this.age = age;
  }
  @Override
  public Person clone(){
   try{
     return (Person) super.clone();
   } catch( CloneNotSupportedException e ){
     throw new AssertionError();
   }
  }

  @Override
  public int compareTo(Object o) {
    return 0;
  }

  /*
  Person (Date birthDate) {
   // this.birthDate = birthDate;
  }
  /*
  public boolean isOlderThan2000(){
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    cal.set(2000, Calendar.JANUARY, 1, 0, 0, 0);
    return birthDate.compareTo(cal.getTime()) < 0;
  }
  */
}

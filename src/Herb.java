/**
 * Created by yevgnen on 2016-11-29.
 */
public class Herb {

  enum Type {ANNUAL, PERENNINAL, BIENNIAL}

  final String name;
  final Type type;

  Herb(String name, Type type){
    this.name = name;
    this.type = type;
  }

  @Override
  public String toString(){
    return name;
  }
}

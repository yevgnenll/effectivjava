/**
 * Created by yevgnen on 2016-11-23.
 * https://gist.github.com/yevgnenll/cf396b9b76770f5121ddcb7b60c52a6e
 */
public final class PhoneNumber {
  private final short areaCode;
  private final short prefix;
  private final short lineNumber;
  private volatile int hashCode;

  public PhoneNumber(int areaCode, int prefix, int lineNumber){
    rangeCheck(areaCode, 999, "area code");
    rangeCheck(prefix, 999, "prefix");
    rangeCheck(lineNumber, 9999, "line number");

    this.areaCode = (short)areaCode;
    this.prefix = (short)prefix;
    this.lineNumber = (short)lineNumber;
  }

  private static void rangeCheck(int arg, int max, String name){
    if(arg < 0 || arg > max)
      throw new IllegalArgumentException(name + ": " + arg);
  }

  @Override
  public boolean equals(Object o){
    if(o == this)
      return true;
    if(!(o instanceof PhoneNumber))
      return false;
    PhoneNumber pn = (PhoneNumber) o;
    return pn.lineNumber == lineNumber
        && pn.prefix == prefix
        && pn.areaCode == areaCode;
  }

  @Override
  public int hashCode(){
    int result = hashCode;
    if(result == 0) {
      result = 17;
      result = 31 * result + areaCode;
      result = 31 * result + prefix;
      result = 31 * result + lineNumber;
      hashCode = result;
    }
    return result;
  }

  public int compareTo(PhoneNumber pn){

    // local number
    int areaCodeDiff = areaCode - pn.areaCode;
    if(areaCodeDiff != 0)
      return areaCodeDiff;

    // dialing number
    int prefixDiff = prefix - pn.prefix;
    if(prefixDiff != 0)
      return prefixDiff;

    // line number
    return lineNumber - pn.lineNumber;
  }
}

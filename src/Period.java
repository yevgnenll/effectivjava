import java.util.Date;

/**
 * Created by yevgnen on 2016-12-01.
 */
public class Period {
  private final Date start;
  private final Date end;

  /*
  * @param start: start of period
  * @param end: end of period
  * @throws IllegalArgumentException before end than after
  * @throws NullPointException throw null when start or end is null
  * */
  public Period(Date start, Date end){
    if(start.compareTo(end) > 0)
      throw new IllegalArgumentException(start + " after " + end);
    this.start = start;
    this.end = end;
  }
  public Date start(){
    return start;
  }
  public Date end(){
    return end;
  }
}

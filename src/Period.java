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
    this.start = new Date(start.getTime());
    this.end = new Date(end.getTime());
    if(this.start.compareTo(this.end) > 0)
      throw new IllegalArgumentException(
          this.start + " after " + this.end
      );
  }
  public Date start(){
    return new Date(start.getTime());
  }
  public Date end(){
    return new Date(end.getTime());
  }
}

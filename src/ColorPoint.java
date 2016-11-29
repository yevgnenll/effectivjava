import java.awt.*;

/**
 * Created by yevgnen on 2016-11-24.
 */
public class ColorPoint extends Point{

  private final Point point;
  private final Color color;

  public ColorPoint(int x, int y, Color color){
    super(x, y);
    if(color == null)
      throw new NullPointerException();
    point = new Point(x, y);
    this.color = color;
  }

  public Point asPoint(){
    return point;
  }

  @Override
  public boolean equals(Object o){
    if(!(o instanceof ColorPoint))
      return false;
    ColorPoint cp = (ColorPoint) o;
    return cp.point.equals(point) && cp.color.equals(color);

  }
}

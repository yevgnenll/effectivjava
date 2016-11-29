/**
 * Created by yevgnen on 2016-11-25.
 */

class Figure {
  private enum Shape { RECTANGLE, CIRCLE };

  // 어떤 모양인지 나타내는 태그 필드
  private final Shape shape;

  // 태그가 RECTANGLE일때만 사용되는 필드들
  private double length, width;

  // 태그가 CIRCLE일때만 사용되는 필드들
  private double radius;

  // 원을 만드는 생성자
  public Figure(double radius) {
    shape = Shape.CIRCLE;
    this.radius = radius;
  }

  // 사각형을 만드는 생성자
  public Figure(double length, double width) {
    shape = Shape.RECTANGLE;
    this.length = length;
    this.width = width;
  }

  public double area() {
    switch(shape) {
      case RECTANGLE:
        return length * width;
      case CIRCLE:
        return Math.PI * (radius * radius);
      default:
        throw new AssertionError();
    }
  }
}

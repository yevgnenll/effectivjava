/*
*  complex number -> 복소수를 표현하는 클래스.
* */

public class Complex {
  private final double re;
  private final double im;

  public static final Complex ZERO = new Complex(0, 0);

  private Complex(double re, double im){
    this.re = re;
    this.im = im;
  }

  public static Complex valueOf(double re, double im){
    return new Complex(re, im);
  }

  public double realPart(){ return re; }
  public double imaginaryPart(){ return im; }

  public Complex add(Complex c){
    return new Complex(re + c.re, im + c.im);
  }

  public Complex subtract(Complex c){
    return new Complex(re - c.re, im - c.im);
  }

  public Complex multiply(Complex c){
    return new Complex(re * c.re - im * c.im,
                        re * c.im + im * c.re);
  }

  public Complex divide(Complex c){
    double tmp = c.re * c.re + c.im * c.im;
    return new Complex((re * c.re + im * c.im) / tmp,
        (im * c.re - re * c.im) / tmp);
  }

  @Override
  public boolean equals(Object o){
    if(o == this)
      return true;
    if(!(o instanceof Complex))
      return false;
    Complex c = (Complex) o;

    // float point is not good for using ==
    return Double.compare(re, c.re) == 0 &&
           Double.compare(im, c.im) == 0;
  }

  @Override
  public int hashCode(){
    int result = 17 + hashDouble(re);
    result = 31 * result + hashDouble(im);
    return result;
  }

  private static int hashDouble(double val){
    long longBits = Double.doubleToLongBits(val);
    return (int) (longBits ^ (longBits >>> 32));
  }

  @Override
  public String toString(){
    return "(" + re + " + " + im  + "i)";
  }
}

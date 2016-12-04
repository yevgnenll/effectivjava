# chapter 6. Enum과 annotation
----------------------------

java 1.5에 새로운 참조 자료형이 추가되었다. 열거 자료형(enum type)이라 불리는
새로운 종류의 클래스와, annotation 자료형이라 불리는 새로운 종류의
interface이다.

-----------------

### Rule No.30 int상수 대신 enum을 사용하라

열거 자료형(enumeratred type)은 고정 개수의 상수들로 값이 구성되어있는
자료형이다.

```
public static final int APPLE_FUJI = 0;
public static final int APPLE_PIPPIN = 1;
public static final int APPLE_GRANNY_SMITH = 2;

public static final int ORANGE_NAVEL = 0;
public static final int ORANGE_TEMPLE = 1;
public static final int ORANGE_BLOOD = 2
```

enum이 도입 되기 전 모습이다. APPLE과 ORANGE 별로 차이가 없다. 연산도 가능하다.
만약 상수 int값이 변경되면 모두 다시 compile 해야한다.

```
public enum Apple {FUJI, PIPPIN, GRANNY_SMITH}
public enum Orange {NAVEL, TEMPLE, BLOOD}
```

- 하나의 열거된 상수별로 하나의 객체를 `public static final` 필드 형태로 제공한다.
- 클라이언트 사용자가 접근할 constructor가 없다.
- singleton pattern을 일반화 했다.
- 컴파일 시점 형 안정성 제공(compile-time type safety)
- [namespace](https://en.wikipedia.org/wiki/Namespace) 공간이 분리된다
	- 상수 추가
	- 순서 변경 등으로부터 자유롭다
- toString 호출하면 인쇄 가능 문자열로 변경된다.
- Comparable interface, Serailizable interface가 구현됨
- method, field 추가 가능

```

public enum Planet {
  MERCURY (3.302e+23, 2.439e6),
  VENUS   (4.869e+24, 6.052e6),
  EARTH   (5.975e+24, 6.378e6),
  MARS    (6.419e+23, 3.393e6),
  JUPITER (1.899e+27, 7.149e7),
  SATURE  (5.683e+25, 6.027e7),
  URANUS  (8.683e+25, 2.556e7),
  NEPTUNE (1.024e+26, 2.477e7);

  private final double mass;
  private final double radius;
  private final double surfaceGravity;

  private static final double G = 6.67300E-11;

  Planet(double mass, double radius){
    this.mass = mass;
    this.radius = radius;
    surfaceGravity = G * mass / (radius * radius);
  }

  public double mass(){ return mass; }
  public double radius() { return radius; }
  public double surfaceGravity() { return surfaceGravity; }

  public double surfaceWeight(double mass){
    return mass * surfaceGravity; // f= ma
  }
}
```

enum 상수에 data를 넣으려면 객체 필드를 선언하고 생성자를 통해 데이터를 그 필드에 저장하면 된다.
필드는 public으로 선언할 수도 있지만, private으로 선언하고 public 접근자를 두는 편이 더 낫다.

```
public class WeightTable{
	public static void main(String[] args){
    	double earthWeight = Double.parseDouble(args[0]);
        double mass = earthWeight / Planet.EARTH.surfaceGravity();
        for(Planet p : Plannet.values())
        	System.out.printf("Weight on %s is %f%n", p, p.surfaceWeight(mass));
    }
}
```

출력 결과가 맘에 들지 않으면 `toString`을 override 하면 된다.
클래스 내부의 연산은 private이나 package-private(default) 로 선언하는것이 최선이다.


##### enum의 또 다른 기능

Planet 상수 각각에는 서로 다른 데이터가 저장디어 있다.
상수들이 제각기 다른 방식으로 동작하도록 만들어야 할 때도 있다.

```
public enum Operation {
  PLUS, MINUS, TIMES, DIVIDE;

  double apply(double x, double y){
    switch (this){
      case PLUS: return x + y;
      case MINUS: return x - y;
      case TIMES: return x * y;
      case DIVIDE: return x / y;
    }
    throw new AssertionError("Unknown op: " + this);
  }
}
```

그런데 이 방법은 유지보수에 적합하지 않다
게다가 `throw new AssertionError("Unknown op: " + this);`가 없으면 compile되지 않는다


```
/**
 * Created by yevgnen on 2016-11-28.
 */
public enum Operation {
  PLUS {
    double apply(double x, double y) {
      return x + y;
    }
  }, Minus {
    double apply(double x, double y) {
      return x - y;
    }
  }, TIMES {
    double apply(double x, double y) {
      return x * y;
    }
  }, DIVIDE {
    double apply(double x, double y) {
      return x / y;
    }
  };

  abstract double apply(double x, double y);
}
```

더 깔끔한 방법이다.
상수 선언 다음에 바로 method가 나온다. 잊더라도 compiler가 에러를 찾아준다
상수별 method 구현은 데이터와 혼용이 될수도 있다.

```
/**
 * Created by yevgnen on 2016-11-28.
 */
public enum Operation {
  PLUS("+") {
    double apply(double x, double y) { return x + y; }
  }, Minus("-") {
    double apply(double x, double y) { return x - y; }
  }, TIMES("*") {
    double apply(double x, double y) { return x * y; }
  }, DIVIDE("/") {
    double apply(double x, double y) { return x / y; }
  };

  private final String symbol;
  Operation(String symbol) { this.symbol = symbol;})
  @Override public String toString(){ return symbol; }

  abstract double apply(double x, double y);
}
```

이렇게 toString을 overriding해서 연산의 기호가 return되도록 할 수 있다.

```
  public static void main(String[] args){
    double x = Double.parseDouble(args[0]);
    double y = Double.parseDouble(args[1]);
    for(Operation op : Operation.values())
      System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
  }
```

toString을 이렇게 재정의한 후 CLI 인자로 2, 4를 주면

```
2.000000 + 4.000000 = 6.000000
2.000000 - 4.000000 = -2.000000
2.000000 * 4.000000 = 8.000000
2.000000 I 4.000000 = 0.500000
```

enum 자료형에는 자동 생성된 `valueOf(String)` method가 있는데,
이 method는 상수의 이름을 상수 그 자체로 변환하는 역할을 한다.
toString을 overriding할땐 fromString method를 작성해 toString이 뱉어내는 문자열을
다시 enum 상수로 변환할 수단을 제공해야 한다.

```
private static final Map<String, Operation> stringToEnum
	= new HashMap<String, Operation>();
static {
	for(Operation op : values())
    	stringToEnum.put(op.toString(), op);
}

public static Operation fromString(String symbol){
	return stringToEnum.get(symbol);
}
```

Operation 상수를 stringToEnum 맵에 넣는 것은 상수가 만들어진 다음에 static block에서 처리한다.
생성자에서 자기 자신에게 넣는 방법을 선택하면 NullPointerException이 발생한다.
**enum의 constructor에선 static 필드를 접근할 수 없다.**


**enum**은 고정된 상수 집합에서 필요하다.(태양계행성, 요일, 장기판 말 etc.)
switch 문 대신 상수별 method를 구현해야 한다.


------------

### Rule No.31 ordinal 대신 객체 필드를 사용하라


enum 상수는 int값 하나에 대응한다.

모든 enum은 ordinal라는 method를 가지고 있는데 enum 상수의 위치를 나타내는 정수값이다.
그래서 ordinal() method를 통해 enum 상수에 대응하는 정수값을 구하면 편리하지 싶지만..


```
public enum Ensemble{
	SOLD, DUET, TRIO, QUARTET, QUINTET,
    SEXTET, SEPTET, OCTET, NONET, DECTET;

    public int numberOfMusicians(){ return ordinal() + 1; }
}
```

유지보수 관점에선 피해야할 코드다.
상수의 순서를 변경하면 `numberOfMusicias()`는 깨지고 만다.

대응하는 값이 필요하다면 객체 필드에 저장하는게 맞다

```
public enum Ensemble{
	SOLD(1), DUET(2), TRIO(3), QUARTET(4), QUINTET(5),
    SEXTET(6), SEPTET(7), OCTET(8), DOUBLE_QUARTET(8),
    NONET(9), DECTET(10), THRIPLE_QUARTET(12);

    private final int numberOfMusicians;
    Ensemble(int size){this.numberOfMusicians = size;}
    public int numberOfMusicians(){ return numberOfMusicians; }
}
```

> 대부분의 프로그래머는 이 method르 사용할 일이 없을 것이다. EnumSet이나 EnumMap처럼 일반적인 용도의 enum 기반 자료 구조에서 사용할 목적으로 설계한 method다

그런 자료구조를 만들 생각이 없다면 ordinal은 사용하지 말아야 한다.


------------

### Rule No.32 비트 필드(bit field) 대신 EnumSet


```
public class Text{
    public static final int STYLE_BOLD            = 1 << 0;       // 1
    public static final int STYLE_ITALIC          = 1 << 1;       // 2
    public static final int STYLE_UNDERLINE       = 1 << 2;       // 4
    public static final int STYLE_STRIKETHROUGH   = 1 << 3;       // 8

    // 인자로 STYLE_상수를 비트별로 OR한 값이거나 0
    public void applyStyles(int styles){ ... }
}
```

or 연산이 가능하다.
```
text.applyStyles(STYLE_BOLD | STYLE_ITALIC);
```

하지만 이 bit field 출력은 이해하기가 어렵다. 모든 field를 순차적으로 살펴봐야한다.
이것보다 더 좋은 방법이 있다.

##### EnumSet

이 클래스는 Set을 implements 하여, Set이 제공하는 기능을 그대로 제공한다.
enum 값 개수가 64 이하인경우(대부분) Enumset은 **long 값 하나만** 사용한다.

`removeAll`이나 `retainAll` 같은 일괄 연산도 bit 연산을 해서 빠르지만
bit를 직접 조작할 때 생길 수 있는 오류나 어수선한 로직을 피한다.


```
public class Text{

	public enum Style{ BOLD, ITALIC, UNDERLINE, STRIKETHROUGH }

    public void applyStyles(Set<Style> styles){ ... }

}
```

EnumSet에는 static factory meethod가 다양하게 있어서 편하게 객체를 생성할 수 있다.


```
text.applyStyles(EnumSet.of(Style.BOLD, Style.ITALIC);
```

여기서 주의할것은 `applyStyles`의 매개변수가 Set< Style> 이다.
EnumSet을 인자로 이용할 것 같지만, interface 자료형으로 사용해야 EnumSet 이 아닌
Set을 인자로 전달하여도 처리할 수 있다.


열거 자료형을 집합으로 사용한다고 해서 bit field로 표현할 필요는 없다.


------------

### Rule No.33 ordinal을 배열 첨자로 사용하는 대신 EnumMap

`ordinal` method를 배열의 index로 이용하는 코드를 작성하면 안된다.

1. 형 안정성을 보장하지 못함
2. 출력 결과에 레이블을 수동으로 만들어야함
3. 정확한 int값을 넣어야함, ArrayOutOfBoundsException

```

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
```

요리용 허브를 나타내는 class

이 허브를 배열로 나타낸다고 하면 품종별로 나열할때(일년생, 다년생, 격년생 등)
품종별로 분류하여 집합을 만든디 넣을텐데 배열에 넣으면 위의 단점이 드러난다

```
Herb[] garden = ...;
Set<Herb>[] herbsByType =
	(Set<Herb>[]) new Set[Herb.Type.values().length]; // 품종 갯수만큼
for(int i=0; i<herbsByType.length; i++)
	herbsByType[i] = new HashSet<Herb>();
for(Herb h : garden)
	herbsByType[h.type.ordinal()].add(h);
```

동작은 하지만 배열은 generic과 호환되지 않고, 무점검 형변환이 필요하다
배열은 첨자가 무엇을 나타내는지 모르고 label을 수동으로 붙여줘야 한다.

enum은 **상수**에 대응시킬 목적으로 나타났다

```
Map<Herb.Type, Set<Herb>> herbsByType =
	new EnumMap<Herb.Type, Set<Herb>>(Herb.Type.class);
for(Herb.type t : Herb.Type.values())
	herbsByType.put(t, new HashSet<Herb>());
for(Herb b : garden)
	herbsByType.get(h.type).add(h);
```

[enumMap 이란?](https://docs.oracle.com/javase/7/docs/api/java/util/EnumMap.html)
enumMap 자체가 각 데이터에 데응하는 숫자를 key로 만들어주는 역할을 하기 때문에
배열을 사용하는것 보다 enumMap을 사용하면 ordinal을 사용한 것 보다 성능 면에서 비등하며,
무점검 형변환도 필요없고 label을 만들어야 할 이유도 없다.

위의 코드를 보면 key의 자료형을 나타내는 class 객체를 인자로 받는것을 주의해야한다.
`Map<Herb.Type, Set<Herb>> herbsByType = new EnumMap<Herb.Type, Set<Herb>>(Herb.Type.class);`


```
public enum Phase {
  SOLID, LIQUID, GAS;

  public enum Transition{
    MELT, FREEZE, BOIL, CONDENSE, SUBLIME, DEPOSIT;

    private static final Transition[][] TRANSITIONS = {
        { null, MELT, SUBLIME },
        { FREEZE, null, BOIL },
        { DEPOSIT, CONDENSE, null }
    };

    public static Transition from(Phase src, Phase dst){
      return TRANSITIONS[src.ordinal()][dst.ordinal()];
    }
  }
}
```

2중 배열을 사용한 케이스이다. ordinal을 사용해 첨자(하나의 변수에 두 개 이상의 데이터)를 만들었다.
이 코드는 간결해보이지만 정확한 예측이 불가능하고
`ArrayIndexOutOfBoundsException`이나 `NullPointerException`이 나올 수 있다.(최악)
만약에 값이라도 추가되면 일이 산더미로 불어나게 된다.

위 코드에서 Phase의 상태를 나타내는것은 두가지 인데 이것을 EnumMap으로 대응해야한다.


```
import java.util.EnumMap;
import java.util.Map;

public enum Phase {
  SOLID, LIQUID, GAS;

  public enum Transition {
    MELT(SOLID, LIQUID), FREEZE(LIQUID, SOLID),
    BOIL(LIQUID, GAS), CONDENSE(GAS, LIQUID),
    SUBLIME(SOLID, GAS), DEPOSIT(GAS, SOLID);

    private final Phase src;
    private final Phase dst;

    Transition(Phase src, Phase dst) {
      this.src = src;
      this.dst = dst;
    }

    private static final Map<Phase, Map<Phase, Transition>>
    	m = new EnumMap<Phase, Map<Phase, Transition>>(Phase.class);

    static {
      for(Phase p : Phase.values())
        m.put(p, new EnumMap<Phase, Transition>(Phase.class));
      for(Transition trans : Transition.values())
        m.get(trans.src).put(trans.dst, trans);
    }

    public static Transition from(Phase src, Phase dst) {
      return m.get(src).get(dst);
    }
  }
}
```

이 `Phase`는 상전이 이전 상태, 상전이 이후 상태를 대응시키는 map이다
`static` 블록에서 첫 for문은 바깥 Map을 초기화 하고
그 다음 for문에서는 내부 Map의 상태를 초기화 한다

`EnumMap`은 내부적으로 배열의 배열이기 때문에 memory 요구량이나 성능 측면에서
크게 손해를 일으키지도 않는다. 하지만, 프로그램은 명료해지고, 안전하며, 유지보수하기 좋다

결론 `ordinal` 대신 `EnumMap`을 사용하라


-------------------------

### Rule No.34 확장이 필요한 enum을 만들어야 한다면 interface로

enum 자료형은 계승을 통한 확장이 불가능하다
확장된 자료형의 상수들이 기본 자료형의 상수가 될 수 잇다. 그러나 그 반대는 될 수 없다.
enum은 상속이 가능하다면 설계와 구현에 대해 많은 부분이 수정되어야 한다.


```
public enum BasicOperation implements Operation {
  PLUS("+") {
    public double apply(double x, double y) { return x + y; }
  }, Minus("-") {
    public double apply(double x, double y) { return x - y; }
  }, TIMES("*") {
    public double apply(double x, double y) { return x * y; }
  }, DIVIDE("/") {
    public double apply(double x, double y) { return x / y; }
  };

  private final String symbol;
  BasicOperation(String symbol) { this.symbol = symbol;}
  @Override public String toString(){ return symbol; }
}
```

BasicOperation은 enum 자료형이라 확장이 불가능하지만 Operation은 interface라 확장이 가능하다.

```
public enum ExtendedOperation implements Operation{

  EXP("^"){
    public double apply(double x, double y){
      return Math.pow(x, y);
    }
  },
  REMAINER("%"){
    public double apply(double x, double y){
      return x % y;
    }
  };

  private final String symbol;

  ExtendedOperation(String symbol) {
   this.symbol = symbol;
  }
  @Override
  public String toString(){
    return symbol;
  }
}
```

`ExtendedOperation.class`가 main에서 test로 전달되고 있다.
확장된 연산 집합을 알리기 위한 것이다.
class 객체가 나타내는 모든 자료형이 `enum`자료형이고 `Operation`의 하위 자료형이 되도록 한다.
모든 enum 내부의 상수를 순차적으로 살펴보면서 해당 상수가 나타내는 연산을 실제로 수행한다.


```
import java.util.Arrays;
import java.util.Collection;

public class Main {

  public static void main(String[] args) {
    double x = Double.parseDouble(String.valueOf(100));
    double y = Double.parseDouble(String.valueOf(7));
    test(Arrays.asList(ExtendedOperation.values()), x, y);
    System.out.println(ExtendedOperation.class);
  }

  private static void test(Collection< ? extends Operation> opSet,
      double x, double y){
    for(Operation op : opSet)
      System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
  }

}
```

method를 호출할 때, 여러 enum 자료형에 정의한 연산들을 함께 전달할 수 있도록 하기 위함이다.
(list 형식으로 한꺼번에 보낸다) 그런데 `EnumSet`이나 `EnumMap`을 사용할 수 없다.


interface를 사용해 확장 가능한 enum 자료형을 만들때는 `enum` 구현 자체는 상속이 불가능하다.

**summary**: 확장 가능한 enum 자료형을 만들수는 없다. 하지만, interface를 만들고
그 interface를 implements 하면 enum 자료형을 손수 만들 수 있다.


-------------------------

### Rule No.35 작명 대신 annotation을 사용하라

java 1.5 이전에는 framework을 사용할때 특별한 취급을 위해 method에 prefix name을 붙이곤 하였다.
ex. Junit에서 test로 시작하는 method

하지만 이 방법은 문제가 있다. 이러한 규칙을 모른다면 사용하기가 어렵다는것이다.
그 외에도 특정한 프로그램 요소에만 적용 되도록 할 수 없다는것,
인자를 전달하기가 매우 어려워진다 등 다양한 약점이 존재한다.

annotation은 이 모든 문제를 해결할 수 있다.
annotation을 붙이는 것 만으로도 test 시에 자동으로 실행되야 하는 method를 지정할 수 있고,
예외가 발생하면 test가 실패한것으로 가정하겠다는 사실을 명시할 수 있다.


```
import java.lang.annotation.*;
/**
* 어노테이션이 붙은 method가 test method 임을 표시
* parameterless static method에만 사용이 가능하다
*/

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.MEHTOD)
public @interface Test{

}
```

`@Retention(RetentionPolicy.RUNTIME)` 은 Test가 실행시간(runtime)에도 유지되어야 한다는 뜻이고
`@Target(ElementType.MEHTOD)`는 Test가 method 선언부에만 적용하겠다는 것이다.

annotation은 인자를 받지 않고, "표시"를 하는 역할만 한다.

```
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface Test{
}
```

```
public class Sample {
  @Test
  public static void m1(){}
  public static void m2(){}
  @Test
  public static void m3(){
    throw new RuntimeException("Boom");
  }
  public static void m4(){}
  @Test
  public static void m5(){}
  public static void m6(){}
  @Test
  public static void m7(){
    throw new RuntimeException("Crash");
  }
  public static void m8(){}
}
```

m3과 m7은 예외를 발생시키고, m1과 m5는 예외 없이 수행한다.
m5는 객체 method이다. 따라서 annoation을 잘못 사용한 예시이다.
Test 도구는 annotation이 없는 나머지 method는 무시한다.

`Test` annotation은 Sample 클래스가 동작하는데 직접적인 영향을 주지 않는다.

```
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RuntTests {
  public static void main(String[] args) throws Exception{
    int tests = 0;
    int passed = 0;
    Class testClass = Class.forName(args[0]);
    for(Method m : testClass.getDeclaredMethods()){
      if(m.isAnnotationPresent(Test.class)){
        tests++;
        try {
          m.invoke(null);
          passed++;
        } catch (InvocationTargetException wrappedExc){
          Throwable exc = wrappedExc.getCause();
          System.out.println(m + " failed: " + exc);
        } catch (Exception exc){
          System.out.println("INVALID @Test: "+ m);
        }
      }
    }
    System.out.printf("passed: %d, Failed: %d%n", passed, tests- passed);
  }
}
```

이 테스트를 실행하는 code는 class method 안에서도 `@Test` annotation이 붙은 모든 emthod를 차자내어
reflection 기능을 활용해 실행한다.

`isAnnotationPresent(Test.class)` **method는 실행해야 하는 test method를 찾는데 사용된다.**


```
/**
 * Created by yevgnen on 2016-11-30.
 */
public @interface ExceptionTest {
  Class< ? extends Exception> value();
}
```

이 annotation은 특정한 예외가 발생했을 때만 성공하는 테스트 코드이다.
wildcard 자료형을 사용해서 `Exception`을 상속받은 객체를 걸러낸다

```
// 인자를 받는 annotation example
  public class Sample2 {
  @ExceptionTest(ArithmeticException.class)
  public static void m1(){
    int i = 0;
    i = i / i;
  }
  @ExceptionTest(ArithmeticException.class)
  public static void m2(){
    int[] a = new int[0];
    int i = a[1];
  }

  @ExceptionTest(ArithmeticException.class)
  public static void m3(){}
}
```

이 코드에 적합한 RunTest를 만들면

```
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by yevgnen on 2016-11-30.
 */
public class RunTests {
  public static void main(String[] args) throws Exception{
    int tests = 0;
    int passed = 0;
    Class testClass = Class.forName(args[0]);
    for(Method m : testClass.getDeclaredMethods()){
      if(m.isAnnotationPresent(Test.class)){
        tests++;
        try {
          m.invoke(null);
          passed++;
        } catch (InvocationTargetException wrappedExc){
          Throwable exc = wrappedExc.getCause();
          Class< ? extends Exception> excType =
              m.getAnnotation(ExceptionTest.class).value();
          System.out.println(m + " failed: " + exc);
        } catch (Exception exc){
          System.out.println("INVALID @Test: "+ m);
        }
      }
    }
    System.out.printf("passed: %d, Failed: %d%n", passed, tests- passed);
  }
}

```

모두 똑같지만

`Class< ? extends Exception> excType = m.getAnnotation(ExceptionTest.class).value();`
코드가 추가되었다. m 에서 annotation을 걸러네어 ExceptionTest의 class인지 확인한다


annotation이 있다면, 더 이상 작명 패턴에 기댈 필요가 없다.
annotation은 이미 제공되어있기 때문에(대부분) 굳이 새로 정의하면서 사용할 필요는 없다.


-------------------------

### Rule No.36 Override annotation은 일관되게 사용하라

annotation이 추가 된 이후 가장 많이 사용된것은 @Override 이다.
상위 class의 method를 overring 할때 가장 많이 사용하는데 일관되게 사용한다면
버그가 최소화된 올바른 코드를 작성할 수 있다.


```
import java.util.HashSet;
import java.util.Set;

/**
 * Created by yevgnen on 2016-11-30.
 */
public class Bigram {
  private final char first;
  private final char second;

  public Bigram(char first, char second){
    this.first = first;
    this.second = second;
  }

  public boolean equals(Bigram b){
    return b.first == first && b.second == second;
  }

  public int hashCode(){
    return 31 * first + second;
  }

  public static void main(String[] args){
    Set<Bigram> s = new HashSet<>();
    for(int i = 0; i< 10; i++)
      for(char ch = 'a'; ch <= 'z'; ch++)
        s.add(new Bigram(ch, ch));
  }
}

```

이 코드에서 equals, hashCode를 overridng 했지만, equals는 overloading이 되었다.
다시 정의된 함수인게 아니라 그냥 똑같은 이름의 다른 일을 하는 equals 인것이다.
상위 class에서 equals를 overring 할때 parameter는 object type을 받아야 한다.


```
  @Override
  public boolean equals(Bigram b){
    return b.first == first && b.second == second;
  }
```

여기서 에러가 나는데 doesn't override or implemnet a method 라는 결과가 나온다

```
  @Override
  public boolean equals(Object o){
    if(!(o instanceof Bigram))
      return false;
    Bigram b = (Bigram) o;
    return b.first == first && b.second == second;
  }
```

그리고 상위 클래스의 method를 overring 할때는 반드시 `@Override` annotation을 붙여야 한다.
하지만 **예외**가 있다

absract class를 상속받을때는 `@Override` annotation을 안붙여도 된다
class 뿐 아니라 interface 에 선언된 method를 구현할 때도 Override를 붙여야 한다.
물론 필수는 아니지만, 똑같은 method 선언을 피하기 위해서라도 해두는게 좋다


-------------------------

### Rule No.37 자료형을 정의할 때 표식 interface를 사용하라

maker interface: 표시 인터페이스 는 아무 method도 선언하지 않은 interface이다
maker interface를 사용한다는 뜻은 어떤 속성을 만족한다는 사실을 표시하는것과 같다.
이 interface를 구현한 class를 만들겠다는 것은 해당 클래스로 만든 객체들은
ObjectOutputStream으로 출력할 수 있다는 뜻이다.

제기할 수 있는 문제로는
maker annotation을 사용하면 maker interface는 필요없어지는게 아닐까? 인데
그렇지 않다.

1. maker interface는 표식이 붙은 클래스가 만드는 객체가 구현한 자료형이다
	- annotation은 자료형이 아니다.
	- maker interface는 자료형이므로, 실행 중이나 발견하게 도리 오류를 compile 시점에 발견한다.
2. 적용 범위를 더 세밀하게 지정할 수 있다.
	- `ElementType.TYPE`으로 지정하면 annotation은 class, interface에도 적용이 가능하다

표식이 필요한 class에는 단순히 extends 만 하면 된다.

maker annotation의 주된 장점은 프로그램 안에서 annotation 자료형을 쓰기 시작한 뒤에도
더 많은 정보를 추가할 수 있다.
기본값을 갖는 annotation type element들을 더해 나가면 된다.

하지만 maker interface는 이런 부분이 불가능하다


annotation
#### class, interface 이외에 프로그램 요소에 적용되어야 하는 표식
이외에 이 표식이 붙은 객체만 parameter로 받을 수 있는 method를 만들것인가?
그렇다면 maker interface를 사용해야 한다. compile 시점에 형 검사를 할 수 있기 때문이다

새로운 method가 없는 자료형을 정의하고자 한다면 maker interface를
표현식에 더 많은 정보를 추가할 가능성이 있다면 annotation을 사용해야 한다.



--------------------------

[뒤로가기](../README.md)

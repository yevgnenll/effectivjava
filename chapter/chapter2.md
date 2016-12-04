## chapter 2. 객체의 생성과 삭제

- 언제 만들어야 하는가?
- 그 방법은 무엇인가?
- 객체 생성을 피해야 하는경우
- 적절한 순간에 객체가 삭제되도록 보장하는 방법
- 삭제 전에 반드시 이루어져야 하는 청소작업

이 관점에서 이 챕터를 살펴야겠다.


----------------

### Rule No.1 생성자 대신 정적 메서드 사용이 가능할까?

```
    public static Boolean valueOf(boolean b){
      return b ? Boolean.TRUE : Boolean.FALSE ;
    }
```

#### 첫번째 장점
static method는 이름이 있다. 생성자에 전달되는 인자들은 객체가 언제 생성되는지 설명하지 못한다.

`BigInteger(int, int, Random)`은 `BigInteger.probablePrime`과 같은 이름으로 사용한다면 훨씬더 사용이 편리하다 -&gt; 생성자는 이름이 없다. 그래서 무엇을 하는지 처음에 봐서 알기 어렵지만 메소드는 **이름**이 있어서 알기가 명확하다

- 생성자는 parameter 순서가 바뀌면 다른 행동을 한다. 이 방법은 문서를 매번 참고해야한다.
- 하지만 static method를 만들어두면 이름때문에 문서를 참고하지 않아도 된다.

#### 두번째 장점

static method는 생성자와 달리 호출할때마다 새로운 객체 생성이 필요 없다.
객체 생성의 비용이 큰 경우엔 이 방법이 큰 도움이 된다.

#### 세번째 장점

생성자와 달리 return type 하위 객체를 반환할수 있다.(?)
public으로 선언되지 않은 클래스의 객체를 반환하는데, 세부사항을 감출 수 있으므로 아주 간결한 API가 만들어진다.

사용자는 실제 구현 세부사항을 아는게 아니라 인터페이스만 보고 작성하게된다 -&gt; good
따라서 다른 클래스 사용법에 대한 문서를 읽지 않아도 구현할 수 있다.

### 네번째 장점

parameterize type(형인자 자료형) 객체를 만들 때 편하다.

```
Map<String, List<String>> m = new HashMap<String, List<String>>();
```

이 코드에서 &lt;String, List&gt; 라는 부분이 두번이나 중복된다.

하지만 static method를 사용해서 아래와 같이 한다면 코드를 줄일 수 있다.

```

public static <K, V> HashMap<K, V> newInstance(){
}

Map<String, List<String>> m = HashMap.newInstace();

```

java.util 클래스 안에 이 부분을 넣어두면 실제로 이렇게도 사용이 가능하다.
-> java 1.7 부터는 `Map<String, List<String>> m = new HashMap<>();` 자료형 유추가 가능하다


### 단점

1. `protected`, `public`으로 생성된 생성자가 없어 하위 클래스 생성 불가능
2. static method가 다른 static method와 명시적으로 구분이 안된다.

[factory pattern](https://www.tutorialspoint.com/design_pattern/factory_pattern.htm)


----------------

### Rule No.2 생성자 param이 많다면 builder pattern

[builder pattern](https://www.tutorialspoint.com/design_pattern/builder_pattern.htm)

>> 필요한 객체를 직접 생성하는 대신, 클라이언트는 먼저 필수 인자들을 생성자에 전부 전달하여 객체를 만든다.

위 링크에서 step 6, 7, 8

생성자는 여러개 만들 수 있다. 필수값은 반드시 생성자에 넣지만 선택값은 경우의 수의 따라 만들 수 있는데, 이런 경우 사용자가 순서를 바꿔도 컴파일러의 인식이 불가능하다.

이에 대한 대응중 하나로 **Java Bean** 패턴이 있다. 내가 가장 많이 사용했던 방법인데 effective java에서는 1회 함수 호출로 개체 생성을 끝내는게 아니기 때문에 객체의 일관성이 일시적으로 깨질 수 없음을 지적했고, **immutable class(변경 불가능)**을 만들 수 없다는것이다.


이럴때 사용하는것이 **Builder**패턴이다.

예를들어 영양 성분표를 나타내는 클래스를 예로 들면

1. 총 제공량(필수)
2. 1회 제공량(필수)

그 외에 지방, 트랜스지방량, 나트륨 등등 약 20여가지의 선택적 요소들이 있다.
코드적으로 이야기 한다면

```

NutritionFacts noddle = 
  new NutritionFacts.Builder(240, 8)
  .calories(100).sodium(35)
  .carbohydrate(27).build();


```

국수의 영양 성분표를 위와같이 만들 수 있다.
이렇게 만든다면 parameter의 순서가 혼동되는 경우도 막을 수 있고, 각 성분에 들어가는 수치에 대한 valid check도 가능하다.

그 외의 장점으론

1. 여려개의 varagrs를 갖는다.
2. 하나의 빌더 객체로 여러 객체를 만들 수 있다.

### 단점

1. builder 객체를 생성해야 한다.
    - parameter가 충분히 많은 상황에서 이용해야한다.(4개 이상)
    - 지금은 인자가 적더라도 추가될 수 있음을 고려

**Summary** 많은 생성자 and 대부분의 param이 선택적 인자인경우


### Rule No.3 private 생성자나 enum 자료형은 singleton으로

[singleton pattern](https://www.tutorialspoint.com/design_pattern/singleton_pattern.htm): 객체를 하나만 생성할 수 있다. -> test가 어려워질 수 있다.

**final**: 상수, 선언과 동시에 초기화, 값 수정 불가능, 오직 get만 가능

singleton example

```

public class Elvis {
  private static final Elvis INSTANCE = new Elvis();
  private Elvis() {}
  public static Elvis getInstance(){ return INSTANCE; }

  public void leaveTheBuilding(){

  }
}

```

`private static final Elvis INSTANCE = new Elvis();` 상수로 객체를 선언
`private Elvis() {}` 객체 생성을 막음
`public static Elvis getInstance(){ return INSTANCE; }` 외부에서 접근하는 변수 오로지 final로 선언된 INSTANCE를 가져오는데 유일하게 생성된 Elvis의 객체이다

최근 JVM은 static factory method 호출을 [inline](http://stackoverflow.com/questions/1546694/what-is-inlining) 처리한다. 

**inline** 은 `final`이 붙은경우 그것을 static 변수에 저장된 값으로 보는게 아니라 상수(숫자)로 보기 때문에 이 방법을 쓴다고해서 가장 좋은 성능이 되는것이 아니라 대부분이 이미 좋은 성능을 낸다.


----------------

### Rule No.4 객체 생성을 막을땐 private 생성자를 사용하라

지금까지 객체 생성을 막을경우 private 생성자를 두어 객체 생성을 차단한 케이스가 있었다.

위의 singleton pattern에서도 동일하다. private 생성자를 생략하면 자동으로 default constructor를 만들어버린다. 사용자는 이 생성자를 일반 생성자와 구분할 수 없고, 객체 생성이 가능한 클래스가 되어버린다.

**abstract class**로 생성한다면 자연스럽게 다른 class가 extends 할 것이고. 해당 클래스는 객체 생성이 가능하기 때문에 의미가 없다.

따라서 private constructor로 명시해두는게 좋다.


```

public class Elvis {
  private static final Elvis INSTANCE = new Elvis();
  // 기본 생성자가 자동 생성되지 못하도록 하여 객체 생성 방지
  private Elvis() {
    throw new AssertionError();
  }
  // 생략
}

```

위와같이 AssertionError를 해두면 이 클래스는 어떠한 상황에서도 객체를 생성할 수 없다.


----------------


### Rule No.5 불필요한 객체는 만들지 말라.


기능적으로 동일한 객체는 재사용하는게 낫다. 

```

String s = new String("zum internet"); // bad!

String s = "zum internet"; // good

```


위의 코드는 String 객체를 새로 생성하는것이고, 아래 코드는 기존에 생성한 String 객체를 사용하는것이다. VM에서 실행되는 모든 코드가 해당 객체를 사용한다.

`Boolean(String)` 보다는 `Boolean.valueOf(String)`이 바람직하다
생성자는 호출할 때 마다 객체를 생성하고 static factory method는 그렇지 않다.

```

public class Person {

  private final Date birthDate;

  // 생략

  public boolean isOlderThan2000(){
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    cal.set(2000, Calendar.JANUARY, 1, 0, 0, 0);
    isMillennium = cal.getTime();
    return birthDate.compareTo(isMillennium) < 0;
  }
}

```

위 코드는 2000년도 이후에 태어난 사람인지 아닌지 체크하는 isOlderThan2000() 라는 함수가 있다.
위와같이 작성하면 체크할때마다 `Calendar`, `TimeZone`, `Date` 객체를 만들어낸다


```

public class Person {

  private static final Date isMillennium;
  private final Date birthDate;

  // 생략

  static {
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(("GMT")));
    cal.set(2000, Calendar.JANUARY, 1, 0, 0, 0);
    isMillennium = cal.getTime();
  }

  public boolean isOlderThan2000(){
    return isMillennium.compareTo(birthDate) > 0;
  }
}
```


하지만 이러한 방법으로 리팩토링을 한다면 맨처음에 객체를 생성했을때 한번만 
`Calendar`, `TimeZone`, `Date`의 객체를 생성하고 이후에 재사용하게 된다.

effective java 저자는 250배의 성능 차이가 발생했다고 한다 (ㅎㄷㄷ)
`isMillennium` 이 상수로 처리되면서 훨씬 더 빠른 연산이 가능하고, 가독성에도 더 뛰어나다.

```

public static void main(String[] args){
  Long sum = 0L;
  for(long i=0; i<Integer.MAX_VALUE; i++){
    sum += i;
  }
  System.out.println(sum);
}

```

여기서 `Long sum` 을 보면 sum에 i만큼 더해질 때 계속해서 새로운 Long 객체가 생성된다. -> 위의 경우 2^31개의 필요하지 않은 객체가 생성된 것이다.

**객체 표현형 대신 기본 자료형을 사용하자**

하지만, 객체를 생성하여 코드의 명확성, 단순성이 향상된다면 만드는것이 좋다.


### Rule No.6 유효기간이 지난 객체 참조는 폐기하라


c, c++ 은 malloc이후 free를 통해 메모리 누수를 개발자가 직접관리하지만 java는 GC가 해당 기능을 자동으로 수행한다. 그렇다고 해서 메모리 누수에 대한 고민을 놓아서는 안된다.

```

public class Stack {
  private Object[] elements;
  private int size = 0;
  private static final int DEFAULT_INITIAL_CAPACITY = 16;

  public Stack(){
    elements = new Object[DEFAULT_INITIAL_CAPACITY];
  }

  public void push(Object e){
   ensureCapacity();
    elements[size++] = e;
  }

  public Object pop(){
    if(size == 0)
      throw new EmptyStackException();
    return elements[--size];
  }

  public void ensureCapacity(){
    if(elements.length == size)
      elements = Arrays.copyOf(elements, 2 * size + 1);
  }
}

```


배열의 사이즈를 늘리고, 줄이는과정에서 메모리 누수가 발생한다.
배열을 한칸 빼는것이 아니라 size 라는 변수의 숫자가 변하면서 오고가기 때문에
더 이상 참조하지 않는 (pop()이 실행된) index는 null로 처리해야한다.
왜냐하면 객체 참조를 유지하는경우 해당 객체만 쓰레기 수집에서 제외되는것이 아니라 그 객체를 통해 참조되는 다른 객체들도 GC 수집에서 제외된다.

그래서 pop에 아래 코드를 추가하면 된다.

```

public Object pop(){
  if(size == 0)
    throw new EmptyStackException();
  Object result = element[--size];
  elements[size] = null;
  return result;
}

```


만료된 참조를 사용하더라도 NullPointException 이 발생하지만, 이런 오류는 사전에 찾아내어 수정하는것이 좋다.


----------------

### Rule No.7 종료자 사용을 피하라


종료자(finalizer)는 예측불가능하고 위험하며, 일반적으로 불필요하다.

#### 문제점

1. 신속히 실행된다는 보장이 없다.
    - 어떤 개체에 대한 참조가 사라지고 종료자가 실행되기까지 긴 시간이 걸릴 수 잇다.
    - 긴급한 작업을 종료자 안에서 처리하면 안된다.
2. 메모리 반환이 지연될 수 있다.
    - 종료자 queue에 쌓여 반환되기를 기다리는 상태여서 out of memory error가 발생한 적이 있다.
    - 자바 명세서에 어떤 thread가 종료자를 실행해야 하는지 아무 언급이 없다.

3. 종료자를 사용하면 프로그램 성능이 저하된다.
    - 객체를 만들고 삭제하는데 5.6ns -> 종료자를 붙이면 2,400ns로 늘어난다.

#### 해결방안

1. 명시적인 종료 메서드를 하나 정의하여 사용한다.
    - ex. java.sql.Connection에 정의된 close() method가 있다.
    - ex. Image.flush: flush를 호출한 되에도 Image 객체는 사용이 가능한경우가 있다.

종료 method는 try-finally 문과 함께 스인다. -> 객체 종료를 보장한다.

>> java 1.7부터는 try-with-resources 문을 지원하는데 이 문법을 이용하면 finally 블롱은 사용하지 않아도 된다.


#### 정말 종료자는 필요없는것일까?

1. 명시적 종료 메서드 호출을 잊을 경우 안전망으로서 역할
    - 종료자가 언제 호출될지 알 수 없기 때문에 늦더라도 자원은 반환된다.
    - 단, 이런경우 경고를 log로 남겨야 한다.

2. native peer와 연결된 객체를 다룰때
    - native peer: java 객체가  native method를 통해 기능 수행을 위임하는 네이티브 객체
    - [참조](http://roughexistence.tistory.com/81) 후에 추가


----------------


## chapter 3. 모든 객체의 공통 메서드

java는 객체 생성이 가능한 클래스이지만, 기본적으로는 상속받아 사용하도록 설계되어있다. 재정의 하도록 설계된 method들이 많다.


### Rule No.8 equals를 재정의할 때는 일반 규약을 따르라

equals를 재정의했다 완전 망했던적이...

최고의 방법은 재정의를 **하지 않는**것이다. 그 경우 객체는 오직 자기 자신과만 같다. 아래 조건 가운데 하나라도 만족되면 그래도 된다.

1. 각각의 객체가 고유하다
    - 값 대신 활성 개체를 나타내는 Thread 같은 클래스

2. 클래스에 "논리적 동일성" 검사 방법이 있건 없건 상관없다.
    - `java.util.Random` 클래스는 두 Random 객체가 같은 난수열을 만드는지 검사하는 equal method를 재정이 할 수도 있지만, 이 클래스를 설계한 사람들은 그 기능이 필요하지 않다 생각하였다.

3. 상위 클래스에서 재정의한 equals가 하위 클래스에서 사용하기에 적당하다.
4. 클래스가 private or package-private으로 선언되었고, equals를 호출할일이 없다.
    - effective java 저자는 이런 경우 재정의할것을 추천한다.


각각의 객체가 교유하지 않고, 논리적 동일성 개념을 지원하는 클래스, 하위 클래스의 필요를 충족하지 못할때 **재정의**해야한다.

값 클래스(Integer, Date)는 단순히 어떤 값을 표현하는 것이다.
이런 경우는 **두 값이 같은지 확인하기위해** equals를 사용하지 같은 type인지 확인하기 위해 equals를 사용하지 않는다.

equals를 올바르게 재정의 하지 않은 객체를 `Map` 이나 `Set`과 함게 사용할 경우 그 결과는 예측하기 어렵다.


equals를 재정의할 필요가 없는 **값 클래스**도 있다. 값마다 최대 하나의 객체만 존재하도록 제한하는 클래스는 equals를 정의하지 않아도 된다. (ex. enum)

이런 클래스에서는 객체 동일성이 곧 논리적 동일성이다.


#### equals를 재정의할때 준수해야하는 규약


1. 반사성(reflexive): null이 아닌 참조 x가 있을때, x.equals(x) -> true
  - 모든 객체는 자기 자신과 같아야 한다.

2. 대칭성(symmetric): null이 아닌 참조 x, y가 있을때 x.equals(y), y.equals(x)가 true일 때만 true를 반환한다.
  - 두 객체에게 서로 같은지 물어보면 같은 답이 나와야 한다.\

3. 추이성(trasitive): null이 아닌 참조 x, y, z가 있을때 x.equals(y) -> true && y.equals(z) -> true 이면, x.equals(z)도 true 이다.
  -  삼단논법과 같다.
  - A, B, C 클래스가 있는데 A, C 클래스는 B를 상속받고 속성 1개가 더 추가되었다. 이럴경우 A.equals(B) -> true, B.equals(c) -> true 이지만 A.equals(C)까지 true 라는 보장은 없다. 추가된 속성이 다를 수 있기 대문이다.

4. 일관성(consistent): null이 아닌 참조 x, y가 있을 때, equals를 통해 비교되는 정보에 아무 변화가 없다면 x.equals(y) 호출 결과는 호출 횟수에 상관없이 항상 같아야 한다.
  - 일단 같다고 판정된 객체는 추후 변경되지 않는 한 계속 같아야 한다.

5. null이 아닌 참조 x에 대해 x.equals(null)은 항상 false 이다.
  - 모든 객체는 null과 동치 관계에 있지 아니한다.
  - 이 경우를 위해 equlas 안에서 null 조건을 명시적으로 검사해야한다.


추이성에 관련된 예시

```

class Point {
  private double x;
  private double y;

  public Point(double x, double y){
    this.x = x;
    this.y = y;
  }
}

```

Point 클래스를 정의하고

```

import java.awt.*;

public class ColorPoint extends Point{

  private  final Color color;

  public ColorPoint(int x, int y, Color color){
    super(x, y);
    this.color = color;
  }
}

```


ColorPoint 객체는 Point를 상속받아서 만들었다.
그럼 다음 경우에서는 추이성이 성립하려면 어떻게 해야할까?

```

ColorPoint p1 = new ColorPoint(3, 3, Color.RED);
Point p2 = new Point(3, 3);
ColorPoint p3 = new ColorPoint(3, 3, Color.BLUE);

```


```
p1.equals(p2); -> true
```

```
p2.equals(p3); -> true
```

```
p1.equals(p3); -> false
```

추이성이 성립하지 않는다.

객체지향 언어에서 동치 관계를 구현할때 발생하는 **본질적인** 문제이다

객체생성 가능 클래스를 상속받으면서 새로운 컴포턴트를 추가하면 equals 규약을 어기지 않을 방법이 없다.


이러한 방법을 깔끔하게 해결할 방법은 `abastract` 추상 클래스 사용이다

예를들어 

1. 아무 값도 없는 `Shape` 클래스를 만들고
2. 1을 상속받고 radius를 추가한 `Circle` 클래스
3. length와 width 필드를 추가한 `Rectangle` 클래스

1의 `Shape` 클래스는 객체를 직접 만들 수 없으므로 이런 문제가 발생하지 않는다.


#### summary


1. == 연산자를 사용해 equals의 인자가 자기 자신인지 검사하라
2. instanceof 연산자를 사용하여 인자의 자료형이 정확한지 검사하라(type check)
3. equals의 인자를 정확한 자료형으로 반환하라(형변환)
4. 필드 각각이 인자로 주어진 객체의 해당 필드와 일치하는지 검사한다.
  - `float`, `double` 이외의 자료형은 == 연산자로 비교
  - `float`은 `Float.compare`, `double`은 `Double.compare`로 비교 (`Float`, `NaN`, `-0.0f`와 같은 상수들 때문임)

5. equals 구현완료 후 대칭성, 추이성, 일관성 3 속성이 만족되는지 검토
  - unit test로 검사


----------------


### Rule No.9 equals를 재정의할때 hashCode도 재정의하라


equals를 재정의할때 반드시 **hashCode**도 재정의 해야한다.
[hashCode in java](https://en.wikipedia.org/wiki/Java_hashCode()): 데이터가 저장되어있는 해시값 다른곳에서 참조할때 사용한다.

1. equals가 사용하는 정보들이 변경되지 않으면 언제나 동일한 정수가 반환되어야 한다.
2. equals method가 같으면 hashCode 값도 같아야 한다.
3. equals method가 다를때 hashCode값이 반드시 다를 필요는 없다. 그러나 서로 다른 hashCode가 나오면 성능이 향상된다.


```

import java.util.HashMap;
import java.util.Map;

public class HashCode {

  public static void main (String[] args){
    Map<Person, String> m = new HashMap<Person, String>();
    m.put(new Person("seungkwon", 29), "developer");
    System.out.println(m.get(new Person("seungkwon", 29)) );
  }
}

```

위 코드에서 보면 console에 나오는 로그는 `null`이 된다.
`"developer"`가 나오리라 예상했던것과 정 반대이다.
왜냐하면 하나의 Person 객체는 HashMap에 저장했을때 객체이고 또 하나의 Person객체는 HashMap에서 꺼내올때의 객체이다.

Person에서 hashCode 규약을 위반했기 때문이다.

1. 0이 아닌 상수를 `int result` 변수에 저장한다
2. 객체 안의 equals 메소드가 사용하는 필드 f에 아래의 절차를 시행한다.
  1. 해당 필드에 대한 int hashCode c를 계산한다.
      - boolean이면 `f ? 1 : 0` 을 실행한다
      - byte, char, short, int 중 하나이면 `(int) f`를 계산한다
      - long이면 `(int)(f^(f>>>32))`를 계산한다
      - float이면 `Float.floatToIntBits(f)`를 계산한다.
      - double이면 `Double.doubleToLongBits(f)`를 계산하여 그 결과를 3번과 같이 다시 연산한다.
      - 필드가 객체를 저장했고, equals가 해당 필드의 equals 를 재귀적으로 호출한다면 hashCode 또한 재귀적으로 호출하여 계산한다.
      - 필드가 배열이면 각 element가 별도 필드인것 처럼 계산한다.(java 1.5부터는 Arrays.hashCode method중 하나를 사용할 수 있다.)

  2. 1번 절차에서 계산된 해시코드 c를 아래와 같이 계산한다
     `result = 31 * result + c;`
  3. return result
  4. 구현이 완료되면 hashCode 값이 똑같은지 unit test를 진행한다.

2 - 2의 31은 소수이면서 홀수이기 때문에 선택되었다. 만약 짝수이고 결과가 overflow된다면 정보는 사라졌을 것이다. (2를 곱하는 것은 비트를 왼쪽으로 shift 하는것과 같다)

위의 절차를 따라보면 short형 필드 3개가 있는 경우 아래와 같이 hashCode를 재정의할 수 있다.


```

  @Override
  public int hashCode(){
    int result = 17;
    result = result * 31 + areaCode;
    result = result * 31 + prefix;
    result = result * 31 + lineNumber;
    return result;
  }

```


--------------


### Rule No.15 변경 가능성을 최소화하라


immutable 클래스는 **그 객체를 수정할 수 없는 클래스다.** 객체가 살아있는 동안 그대로 보존된다. (ex. String, primitive, BigInteger, BigDecimal)


왜 변경 불가능 클래스를 만들까?

1. 설계가 쉽고
2. 구현이 용이하고
3. 사용하기 쉽고
4. 오류가 적으며
5. 안전하다
6. 단순하다

구현하는데 5가지 규칙이 있다.


1. 객체 상태를 변경하는 method를 제공하지 않는다.
2. 상속받을 수 없도록 한다.(class를 final로 선언한다)
    - 잘못되거나 악의적인 하위 클래스가 객체상태가 변경된것 처럼 동작해서 변경 불가능성을 깨트리는 일을 막을 수 있다.
3. 모든 필드를 final로 선언한다.
    - 강제되어 프로그래머의 의도가 분명히 드러난다.
    - 새로 생성된 객체의 참조가 동기화 없이 전달된다.
4. 모든 필드를 private으로 선언한다.
    - public 으로 선언하면 후에 내부 표현을 변경할 수 없다.
5. 변경 가능 컴포넌트에 대한 독점적 접근권을 보장한다.
    - 


```
/*
*  complex number -> 복소수를 표현하는 클래스.
* */

public final class Complex {
  private final double re;
  private final double im;

  public Complex(double re, double im){
    this.re = re;
    this.im = im;
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

```


1. 복소수(실수부와 허수부를 갖는 수)를 표현
2. 사칙연산은 this 객체를 변경하는게 아니라 새로운 객체를 return
    - 변경 불가능 클래스가 따르는 패턴


변경 불가능 객체는 생성될때 부여된 **한 가지 상태만** 갖는다.
반면 변경가능 객체의 상태는 까다롭게 바뀔 수 있다.
수정자 호출 시 객체 상태가 어떻게 바뀌는지 정확하게 공유되지 않으면 클래스를 안정적으로 사용하기 어렵거나 불가능하다.


변경 불가능 객체는 thread에 안전하다. 
1. 어떤 동기화도 필요 없다.
2. thread가 동시에 사용해도 훼손되지 않는다.(thread 안정성을 보장하는 가장 쉬운방법)

이런 이유로 변경 불가능한 객체는 **자유롭게 공유** 할 수 있다.


따라서 변경 불가능 클래스는 기존 객체를 사용하도록 적극 장려해서 이러한 장점을 충분히 살려야 한다. 그러기 위한 방법으론 **자주 사용하는 값을** `public static final` 상수로 만들어 제공하는것이다.

예시

```
public static final Complex ZERO = new Complex(0, 0);
public static final Complex ONE  = new Complex(1, 0);
public static final Complex I    = new Complex(0, 1);
```


이 방법은 자주 사용하는것을 cache 하여 이미 있는 객체가 더 생성되지 않도록 하는것 방법이 있다.(기존 객체를 공유하게 되므로 메모리 요구량과 쓰레기 수집 비용 감소)


변경 불가능한 객체를 자유롭게 공유하는것은 복사본을 만들 필요가 없다.
(clone, 복사 생성자를 만들 이유도 없다)


1. 변경 불가능한 객체는 그 내부도 공유할 수 있다.
2. 변경 불가능한 객체는 다른 객체의 구성요소로도 훌륭하다.
    - 구성요소가 변하지 않는다면 복잡하다 하더라도 쉽게 불변식을 준수할 수 있다.
    - map, set: map의 키, 집합의 원소로 사용하기 좋다


##### 단점

값마다 별도의 객체를 만들어야 한다.

객체 생성 비용이 높을 수 있다.


```
BigInteger moby = ... ;
moby = moby.flipBit(0);
```


위에서 보면 flipBit는 백만개의 비트중 하나만 바꿔도 객체를 새로 생성해야한다.
단 한자리수만 다른 경우라도 같다.
단계별로 새로운 객체를 만들고 마지막 객체를 제외한 모든 객체를 버리는 연산을 수행한다면 성능 문제는 심각해진다


##### 해결방안

1. 다단계 연산중 자주 연산되는 것을 기본 연산으로 제공한다.
2. 변경 가능한 public 동료 클래스를 제공한다.
**???**

##### 설계법

- 상속불가능하게 만들기(final이 아닌 private 생성자를 만든다)

static factory method


```

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

// .. 나머지는 동일함

```


여러 개의 package-private 구현 클래스를 활용할 수 있게 되어 유연성도 가장좋다
static factory method를 사용한다면 무엇을 만들 것인이 명시적으로 알 수 있다.


이 Rule 서두에서는 immutable class는 어떤 method도 객체를 수정해서는 안되며, 모든 필드는 final로 선언해야 한다. 
하지만, 이 원칙은 과한면이 있고, 성능상 문제를 야기할 수 있다.


실제로는 어떤 method도 외부에서 관측 가능한 상태 변화를 야기하지 않아야한다.


>>변경 가능한 클래스로 만들 타당한 이유가 없다면, 반드시 변경 불가능한 클래스로 만들어야 한다.

만약 immutable 클래스로 설계가 어렵다면

1. 변경 가능성을 최대한 제한하라.
2. 특별한 이유가 없다면 필드를 final로 선언하라.



---------------------------


### Rule No.16 계승(상속)하는 대신 구성하라


상속은 재사용을 돕지만 항상 최선은 아니다. 상속을 적절히 사용하지 못하면 불안정하다.

1. 상위, 하위 클래스를 같은 개발자가 통제
2. 단일 패키지 안에서 사용
3. 상속을 고려해 설계하고
4. 그에 맞는 문서를 갖춘 클래스에서 사용


일반적인 객체 생성 가능 클래스라면, 해당 클래스가 속한 패키지 밖에서 상속을 시도하는것은 위험하다.

method 호출과 달리, 상속은 캡슐화 원칙을 위반한다.(당연)

하위 클래스가 정상 동작하기 위해서는 상위 클래스의 구현에 의존할 수밖에 없다.


상위클래스 구현은 리팩토링 되면서 바뀔 수 있는데, 결과로 하위클래스가 수정이 없어도 망가질 수 있다. 즉, 상위클래스가 수정되면하위클래스도 함께 수정을 고려해야한다.


```

import java.util.Collection;
import java.util.HashSet;

public class InstrumentedHashSet<E> extends HashSet<E>{
  private int addCount = 0;
  public InstrumentedHashSet(){

  }

  public InstrumentedHashSet(int initCap, float loadFactor){
    super(initCap, loadFactor);
  }

  @Override
  public boolean add(E e){
    addCount ++;
    return super.add(e);
  }

  @Override
  public boolean addAll(Collection<? extends E> c){
    addCount += c.size();
    return super.addAll(c);
  }

  public int getAddCount(){
    return addCount;
  }
}

```


위는 상속을 잘못 사용한 예이다.

```
InstrumentedHashSet<String> s =
    new InstrumentedHashSet<String>();
s.addAll(Arrays.asList("cup", "dish", "spoon");
```

이 코드에서 getAddCount의 결과가 3일거라 예상하지만 실제론 6이 나온다.

1. HashSet의 addAll은 add method를 통해 구현했다.
2. HashSet 문서에는 그런 사실이 없다.
3. InstrumentedHashSet에 정의된 addAll은 addCount += 3
4. HashSet의 addAll은 super.addAll과 함께 호출
5. 4번의 addAll은 InstrumentedHashSet에서 정의한 add를 삽입할 원소마다 호출한다.

이러한 이유로 getAddCount는 6이 나온다.
해결방안으로 addAll을 삭제하면 이 문제를 당장 대응할수는 있지만, 정상동작은 아니다. HashSet의 세부사항이라 모든 자바에서 똑같이 구현되었다고 볼 수 없으며, update가 지속되면서 수정되었을 수 있다.


게다가 다음 버젼에서 상위 클래스에 새로운 method가 추가된다면..?

overring해서 생긴 문제라면 새로운 method를 정의할수도 있지만, 그것이 상위 클래스에 있는 method일수도 있으며, 클래스 내부의 규약을 깰수도 있다.


이것의 해결방안은 기존 클래스를 상속하고 새로운 클래스에 상위 클래스 객체를 참조하는 private field를 하나 두는것이다. 이 방법이 **구성(composition)** 이다.

기존 클래스가 새 클래스의 **일부(component)**가 되기 때문이다.


```

import java.util.Collection;
import java.util.Set;

public class InstrumentedSet<E> extends ForwardingSet<E>{
  private int addCount = 0;

  public InstrumentedSet(Set<E> s){
    super(s);
  }

  @Override
  public boolean add(E e){
    addCount ++;
    return super.add(e);
  }

  @Override
  public boolean addAll(Collection<? extends E> c){
    addCount += c.size();
    return super.addAll(c);
  }
  public int getAddCount(){
    return addCount;
  }
}

```


```

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class ForwardingSet<E> implements Set<E> {

  // Set 객체
  private final Set<E> s;
  public ForwardingSet(Set<E> s) {
    this.s = s;
  }

  public void clear(){ s.clear(); }
  public boolean contains(Object o){return s.contains(o); }
  public boolean isEmpty(){ return s.isEmpty(); }
  public int size(){ return s.size(); }
  public Iterator<E> iterator(){ return s.iterator(); }
  public boolean add(E e){ return s.add(e); }
  public boolean remove (Object o){ return s.remove(o); }
  public boolean containsAll(Collection<?> c){ return s.containsAll(c); }
  public boolean addAll(Collection<? extends  E> c){ return s.addAll(c); }
  public boolean removeAll(Collection<?> c){ return s.removeAll(c); }
  public boolean retainAll(Collection<?> c){ return s.retainAll(c); }

  public Object[] toArray() {
    return new Object[0];
  }

  public <T> T[] toArray(T[] a){ return s.toArray(a); }
  @Override
  public boolean equals(Object o){ return s.equals(o); }
  @Override
  public int hashCode(){ return s.hashCode(); }
  @Override
  public String toString(){ return s.toString(); }

}

```


`InstrumentedSet`과 같은 클래스를 **wrapper class**라고 부르는데, 다른 Set 객체를 감싸고 있기 때문이다.

또한 [decorator(장식자)패턴]
(https://ko.wikipedia.org/wiki/%EB%8D%B0%EC%BD%94%EB%A0%88%EC%9D%B4%ED%84%B0_%ED%8C%A8%ED%84%B4) 이라 하는데 기존 `Set` 객체에 기능을 덧붙여 장식하는 구실을 하기 때문이다.


`InstrumentedSet` 클래스는 `Set` 인터페이스를 구현하며, `Set` 객체를 인자로 받아 필요한 기능을 갖춘 다른 `Set` 객체로 변환한느 구실을 한다.

상속을 이용한 접근법은 한 클래스엠나 적용이 가능하고, 상위 클래스 생성자마다 별도의 생성자를 구현해야 하지만, `wrapper class` 기법을 쓰면 `Set` 구현도 가능하고, 이미 있는 생성자도 재사용이 가능하다.



wrapper class는 단점이 별로 없지만 [callback framework(역호출 프레임워크)](http://softwareengineering.stackexchange.com/questions/117434/what-are-callback-frameworks) 에는 적합하지 않다. 이 링크로 들어가면 effective java의 원문을 볼 수 있다. - GUI에서 버튼 클릭을 해서 특정 코드를 실행시키는 부분에서 구현


상속을 사용할때 이러한 조건을 살펴봐야한다.

1. class B는 class A와 "IS-A" 관계가 성립할때만 사용한다.
    - B가 정말 A인가? 라는 질문을 해야한다. **"그렇다"** 라는 답변이 나오지 않는다면 상속을 사용하면 안된다.
2. 상속할 클래스 API에 문제가 있는가?
    - 그렇다면, 그 문제들이 새 API의 일부가 되어도 괜찮은가?

상속은 강력한 도구이지만 캡슐화 원칙을 침해하므로 위 조건이 성립하여도 상위클래스와 하위클래스위 관계를 면밀히 따지고 최대한 composition 원칙을 사용해서 구현해야한다.


**아직 구성이 명백히 이해되지 않지만, 상속을 주의하면서 써야겠다**


-------------------


### Rule No.17 상속을 위한 문서를 만들거나, 불가능하면 상속을 못하게하라


overring이 가능한 method는 내부적으로 **어떻게 사용하는지** 반드시 문서에 남겨야한다.

1. overring method를 어떤 순서로 호출하는지
2. 호출 결과가 어떤 영향을 미치는지
    - background thread를 호출하는지
    - static 초기화 구문안에서 호출되는지
    - 등등

예를들어 `java.util.AbstracCollection` 의 명세중 아래 함수의 명시를 보면

```
public boolean remove(Object o)
```

remove를 위해서 Iterator를 사용한다는 내용이 있는데 이 문서로 보아 개발자가 Iterator를 overring 하면 remove에 영향을 미친다는 사실을 알 수 있다.

문서만 썻다고 끝이 아니다. 클래스 내부 동작에 개입할 수 있는 hooks을 신중하게 고른 `protected` method 형태로 제공해야 한다.
그리고 다양한 하위클래스를 만들어봐도 사용하지 않는 필드는 모두 `private`으로 수정한다. 


상속을 허용하려면 반드시 따라야하는 **제약사항(restriction)**이 몇가지 더 있다.

###### 1. 생성자는 직,간접적으로 overrding method를 호출해선 안된다.
    - 상위 클래스 constructor는 하위 클래스 constructor보다 먼저 실행


```

public class Super {
  public Super(){
    overrideMe();
  }
  public void overrideMe(){

  }
}

```


```

import java.util.Date;

public final class Sub extends Super {

  private final Date date;

  Sub(){
    date = new Date();
  }

  @Override
  public void overrideMe(){
    System.out.println(date);
  }

  public static void main(String[] args){
    Sub sub = new Sub();
    sub.overrideMe();
  }
}

```

위 결과 콘솔에 처음엔 null, 그 다음에 날짜가 찍힌다

상위 constructor가 실행되어 Sub의 생성자가 실행되기도 전에 overrideMe가 실행되여  `System.out.println()`이 실행되고 그 다음 overrideMe를 실행시킨다

따라서 생성자에는 overriding 해야만 하는 함수를 실행시켜서는 안된다.

`Cloneable`, `Serializable` 인터페이스를 구현한 클래스를 **상속** 하려면 더 어렵다


이러한 문제를 해결하는 가장 좋은 방법은

**상속에 맞도록 설계하고, 문서화하지 않은 클래스에 대한 하위 클래스를 만들지 않는 것이다.**


-------------------


### Rule No.18 use interface instead of abstract class


abstract class는 구현된 method를 포함할 수 있지만, interface는 아니다

java는 다중상속을 지원하지 않는다. 추상 클래스가 규정하는 자료형을 만들려면 반드시 상속받아야 하고, interface는 포함된 method를 정의하고 interface가 규정하는 일반 규약을 지키기만 하면 된다.


###### 1. interface는 mixin을 정의하는데 이상적이다.

어떤 선택적 기능을 제공한다는 사실을 선언하기위해 쓰인다.
주된 자료형 이외에 선택적인 기능을 혼합(mixin) 할 수 있다.


###### 2. 비계층적인 자료형 framework를 만든다

```
public interface Singer {
    AudioClip sing(Song s);
}
```

```
public interface Songwriter {
    Song compose(boolean hit);
}
```

각각 가수와 작곡가 interface이다. 그런데 가수 and 작곡가인 경우도 있다.
interface는 이 부분을 쉽게 표현할 수 있다.

```
public interface SingerSongwriger extends Singer, Songwriter{
    AudioClip strum();
    void actSensitive();
}
```

[참고](https://github.com/yevgnenll/but/blob/master/but/trades/views/order_page.py)


python에서 java로 넘어올때 가장 두려운 부분은 다중상속이 불가능하다는 점이었는데, 이부분을 사용한다면 충분히 해결이 가능하겠다!!


###### 3. 포장 클래스 숙어를 통해 강력한 개선이 가능하다


추상 클래스를 사용해 자료형을 정의하면 상속 이외의 수단은 불가능하다.
이 방법이 그다지 안정적인 방법도 아니다.
추상 골격 구현(abstract skeletal implementation) 클래스를 중요 interface 마다 두면, interface의 장점과 추상 클래스의 장점을 결합할 수 있다.


골격구현 클래스의 naming은 [Abstract][Interface] 와 같이 정의한다
> AbstractCollection, AbstractSet, AbstractList


```
import java.util.AbstractList;
import java.util.List;

public class AbstractListEx {

  static List<Integer> intArrayAsList(final int[] a){
    if(a == null){
      throw new NullPointerException();
    }
    return new AbstractList<Integer>() {
      @Override
      public Integer get(int index) {
        return a[index];
      }

      @Override
      public int size() {
        return a.length;
      }

      @Override
      public Integer set(int i, Integer val){
        int oldVal = a[i];
        a[i] = val;
        return oldVal;
      }
    };
  }
}

```

(골격 구현의 강력함을 보여주는 예시)

```

import java.util.AbstractList;
import java.util.List;

/**
 * Created by yevgnen on 2016-11-25.
 */
public class AbstractListEx {

  static List<Integer> intArrayAsList(final int[] a){
    if(a == null){
      throw new NullPointerException();
    }
    return new AbstractList<Integer>() {
      @Override
      public Integer get(int index) {
        return a[index];
      }

      @Override
      public int size() {
        return a.length;
      }

      @Override
      public Integer set(int i, Integer val){
        int oldVal = a[i];
        a[i] = val;
        return oldVal;
      }
    };
  }
}

```


골격 구현 클래스는 상속을 위해 설계하므로 주석을 사용해 문서화를 철저하게 해야하지만 생략


```
import java.util.Map;

public abstract class AbstractMapEntry<K, V> implements Map.Entry<K, V> {

  public abstract K getKey();
  public abstract V getValue();

  public V setValue(V value){
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean equals(Object o){
    if(o == this)
      return true;
    if(!(o instanceof Map.Entry))
      return false;
    Map.Entry<?, ?> arg = (Map.Entry) o;
    return equals(getKey(), arg.getKey()) &&
          equals(getValue(), arg.getValue());
  }

  private static boolean equals(Object o1, Object o2){
    return o1 == null ? o2 == null : o1.equals(o2);
  }

  @Override
  public int hashCode(){
    return hashCode(getKey()) ^ hashCode(getValue());
  }

  private static int hashCode(Object obj){
    return ob == null ? 0 : obj.hashCode();
  }

}

```


음...


public interface는 신중하게 설계해야한다.
공개되고 널리 구현된 다음에는, interface 수정이 거의 불가능하다




-------------------


### Rule No.19 인터페이스는 자료형을 정의할 때만 사용하라


```

// 상수 인터페이스 안티패턴 - 사용 ㄴㄴ
public interface PhysicalConstants {
    // 아보가드로수(1/mol)
    static final double AVOGADROS_NUMBER = 6.02214199e23;

    // 볼쯔만 상수(j/k)
    static final double BOLTZMANN_CONSTANT = 1.3806503E-23;
}

```

상수 interface 패턴은 interface를 잘못 사용한것이다.
어떤 상수를 구현하냐에만 사용해야하지 상세내용은 구현 세부사항이다.

java 내부에도 이런 경우가 있지만, 실수로 포함된 것이라 생각해야한다.

```

public class PhysicalConstants {
    private PhysicalConstants() {} 

    static final double AVOGADROS_NUMBER = 6.02214199e23;
}

```

`static import`를 사용하면 더 편하게 사용할 수 있다.

```

import static com.effectivejava.science.PhysicalConstants.*;

public class Test{
    double atoms(double mols){
        return AVOGADROS_NUMBER * mols;
    }
}

```


interface는 자료형을 정의할 때만 사용한다.


-------------------

### Rule No.20 태그 달린 클래스 대신 계층을 활용하라


태그 달린 클래스란? 두 가지 이상의 기능을 가진 클래스


```

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

```


이 `Shape` 클래스는 RECTANGLE, CIRCLE 두 가지를 구현한다.

1. 서로 다른기능을 위한 코드가 모여 가독성이 떨어지고
2. enum, switch 등 상투적 코드가 반복되며
3. 한 객체에 필요없는 기능이 함께있어 메모리 사용량이 늘어나며
4. 새로운 기능을 추가해야할때 switch에 코드 추가


즉 코딩의 생산성이 떨어지는 이슈가 발생한다.

> 태그 기반 클래스는 클래스 계층을 얼기설기 흉내 낸 것일 뿐이다

라고 표현한다.


올바른 계층 구조로 코딩하려면

1. 태그값이 따라 달라지는 method를 선언하는 추상 클래스를 정의한다.
2. 그 추상 클래스를 abstract 제일 위에 놓는다.
3. 모든 기능에 공통이 되는 필드도 전부 상위 클래스에 넣는다.


```

abstract class Figure {
  abstract double area();
}

class Circle extends Figure {
    final double radius;

    Circle(double radius) { this. radius = radius; }

    public double area() { return Math.PI * (radius * radius); }
}

class Rectangle extends Figure {
    final double length;
    final double width;

    Rectangle(double length, double width) {
        this. length = length;
        this. width = width;
    }

    public double area() { return length * width; }
}

```

코드가 이전보다 간결해지고 swich case에 추가하지 않아 발생하는 문제가 없다.
협업시 직관적인 naming만 있다면 효율적으로 일할 수 있다.


이것을 이용하면 더 자연러운 계층 관계를 만들 수 있다.

```
class Square extends Rectangle{
  Square(double side){
     super(side, side);
  }
}
```

Rectangle을 상속받아 받는 Sqaure(정사각형)이다.


-------------------


[뒤로가기](../README.md)

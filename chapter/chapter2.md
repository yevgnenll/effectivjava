오늘은 회식하는날~~ 양꼬치 먹으러간다아아아아아


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


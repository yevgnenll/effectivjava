### Rule No.22 멤버 클래스는 static으로 선언하라

중첩 클래스는 다른 클래스 안에 정의된 class 이다.

1.  static member class

2.  nonstatic member class

3.  anonymous class

4.  local class

##### 1. static class

바깥 클래스의 모든 `private` 멤버에 접근할 수 있다. 다른 정적 멤버와 동일한 접근
규칙을 따름 바깥 클래스와 함께 사용할때 유용한 helper class(public)을 정의한다.

```
Calculater.Operation.PLUS
Calculator.Operation.MINUS
```

##### 2. nonstatic member class

문법적으로 봤을때 단지 `static`이 빠져있는것 뿐이지만, static member class와
nonstatic member class는 다르다

1.  nonstatic member class는 바깥 클래스 **객체**와 연결된다.

2.  바깥 클래스의 method를 호출할 수 있고, this를 사용해 바깥 객체에 대한 참조를
    가질 수 있다.

3.  nonstatic member class 객체와 바깥 객체와의 연결은 객체 생성시 확립되며
    변경이 불가능하다.

4.  객체 생성시 연결을 위한 공간이 필요해 객체 생성 시간이 늘어난다.

바깥 클래스 객체에 접근할 필요가 없는 member class를 정의할때는 static class로
선언하자

static을 생략하면 모든 객체는 내부적으로 바깥 객체에 대한 참조를 유지하게 되어
더 많은 리소스가 필요하다.

##### 3.private static member class

바깥 클래스 객체의 속성을 표현하는데 사용된다. Map을 구현하는 클래스는
내부적으로 key-value를 보관하는 `Entry` 객체를 사용한다. Entry객체의
method(`getKey`, `getValue`, `setValue`)는 맴 객체에 접근할 필요가 없다.
이런경우 static 키워드를 붙여 연결하는데 필요한 공간을 낭비하지 않는다.

member class가 public이나 protected 인 경우, static 키워드를 붙일지 말지의
선택은 더욱 중요하다. API에 포함되어 공개되면 호환성을 깨는것 말고는 static을
nonstatic으로, nonstatic을 static으로 수정할 방법이 없기 때문이다.

##### 4. anonymous class

익명 클래슨느 바깥 클래스의 **멤버**도 아니다. 멤버로 선언하지 않으며, 사용하는
순간에 객체를 만든다. 문법만 준수하면 코드 어디에서도 사용가능하다.

-   nonstatic context 안에서 사용할때만 바깥 객체를 갖는다.

-   static context 안에서 사용되어도 static 멤버를 가질 수 없다.

-   instanceof 와 같이 클래스 이름이 필요한곳은 사용 불가

-   다중 interface를 implement 하는곳은 선언 불가

-   특정 클래스를 상속받는 anonymous 클래스 불가

-   가동성이 낮다

이러한 특징에도 불구하고 사용되는곳은

1.  배열 길이에 따라 **정렬**하기 위해 익명 `Comparator` 객체

2.  Runnable, Thread, TimeTask같은 프로세스 객체

와 같은 용도로 사용된다.

##### 5. local class

사용빈도가 가장 낮다 local variable이 선언되는 곳이면 어디든지 사용가능하다.
nonstatic context에서 정의했을때만 바깥 객체를 갖는다. static member를 가질 수
없다.

객체 각각에 대한 참조를 가져야 하는 경우에는 nonstatic member class로 만들고,
그렇지 않은 경우에는 static memeber class로 만들어야 한다.

chapter 5. Generic
------------------

java 1.5부터 generic 지원됨 collection 객체를 읽어낼 때 일일히 casting을 해야했고,
엉뚱한 자료형의 object를 넣으면 casting error가 난다.
하지만 generic은 객체의 자료형이 무엇인지 compiler에게 알려줄 수 있다.
제너릭의 **혜택**은 누리면서 **복잡함을 피하는** 방법을 살펴본다

### Rule No.23 새 코드에 무인자 generic type를 사용하지 마라

**용어정리**

1.  **generic class, generic interface**: type parameter가 포함된 class,
    interface

2.  **generic 자료형**:  기호로 감싼 실 형인자

3.  **무인자(raw type)**: `List<E>`의 무인자는 `List`이다.

예시

```
private final Collection stamps = ...;
```

기존엔 이 객체에 엉뚱한 자료형을 넣어도 **compile**시엔 아무런 문제가 없었다.

```
stpams.add(new Coin(...));
```

stamps collectioon에 실수로 coin 객체를 넣었다.

```
for(Iterator i = stamps.iterator(); i.hasNext();){
    Stamp s = (Stapm) i.next();
    // classCastException 예외 발생
}
```

effective java에서 error는 가능한 빨리 발견해야 하며, compile할때 발견하는게
이상적이다. 그러나 위 예제 코드는 프로그램 **실행중**에 발생된다. 그것도 관련없는
곳에서 에러가 발생하여 찾는데 시간이 걸린다.

```

private final Collection<Stamps> statpms = ...;
```

이 선언에서 stamps 변수안엔 **오로지 Stapm 객체**만 삽입되도록 표현한다. 게다가
collection 자료형을 사용하면 원소를 꺼낼때 **형변환**을 하지 않아도 된다.

generic에 무인자 자료형을 사용하면 type 안전성이 사라지고, generic의 장점인
표현력 측면에서 손해를 본다.

무인자 자료형을 지원하는 이유는 **호환성** 때문이다.


##### List(무인자)와 List< Object> 차이


`List`는 type을 **체크**하는 철자를 철저히 **생략**한 것이고, `List<Object>`는 아무
type이나 가능함을 compiler에게 알려주는것이다.

**따라서 `List`와 같이 무인자 자료형을 사용하면 형 안정성을 잃지만,`List<Object>`와 같은 형인자 자료형을 사용하면 그렇지 않다.**

그런데 type을 모르거나 정해지지 않았을겨우 **비한정적 와일드카드
자료형(unbounded wildcard type)**이라는 좀 더 안전한 방법이 있다. `< ?>` 를
입력하면 된다.

```
static int numElementsInCommon(Set< ?> s1, Set< ?> s2){
    int result = 0;
    for(Object o1 : s1)
        if(s2.contains(o1))
            result++;
    return result;
}
```

Collection< ?> 에는 `null` 이외의 어떤 원소도 넣을 수 없다. 어떤 객체를 넣는지 알 방법이 없다

컬렉션의 자료형 **불변식**이 **위반**되지 않도록 compiler가 실행된다


##### 그럼 언제 무인자 자료형을 사용할 수 있을까?(예외)

1.  class literal 에는 무인자 자료형을 사용한다.

    -   List.class, String[].class, int.class는 가능하지만 List.class나
        List< ?>.class 는 불가

2.  instanceof 연산자 사용규칙

```
if(o instanceof Set){
    Set< ?> m = (Set< ?>) o;
}
```

단 o가 Set의 객체라는것이 확인되었을 경우, Set< ?>으로 casting 해야하는것에
주의!

| 용어                                                | 예시                     | 규칙   |
|-----------------------------------------------------|--------------------------|--------|
| 형인자 자료형(parameterized type)                   | List                     | 23     |
| 실 형인자(actual type parameter)                    | String                   | 23     |
| 제너릭 자료형(generic type)                         | List                     | 23, 26 |
| 형식 형인자(formal type parameter)                  | E                        | 23     |
| 비한정적 와일드카드 자료형(unbounded wildcard type)   | List< ?>                | 23     |
| 무인자 자료형(raw type)                             | List                     | 12     |
| 한정적 형인자(bounded type parameter)               | < T extends Comparable< T>>| 26     |
| 한정적 와일드카드 자료형(bounded wildcard type)     | List< ? extends Number> | 28     |
| 제너릭 메서드(generic method)                       | static  List asList      | 27     |
| 자료형 토큰(type token)                             | String.class             | 29     |

무인자 자료형을 최대한 줄이고 generic< ?> 를 명시해 사용하자


----------------


### Rule No.24 무점검 경고(unchecked warning)를 제거


generic으로 코드를 만들다보면 **많은** compiler 경고 메세지를 보게된다.

1.  무점검 형변환 경고(unchecked cast warning)
2.  무점검 메서드 호출 경고(unchecked method invocation warning)
3.  무점검 제너릭 배열 생성 경고(unchecked conversion warning)


그 중 unchecked warning은 대부분 쉽게 없앨 수 있다.

```
Set<Lark> exaltation = new Hashset();
```

결과는 아래와 같은 warning이 나타난다.

```
Venery.java: 4: warning: [unchecked] unchecked conversion
found: HashSet, required: Set<Lark>
Set<Lark> exaltation = new Hashset();
```

경고를 반영하여 코드를 고치면

```
Set<Lark> exaltation = new HashSet<Lark>();
```

이것보다 수정하기 더 어려운 코드도 많다. 모든 무점검 경고는, 가능하다면
없애야한다. 없애고 나면 typesafe(코드의 형 안정성)이 **보장**된다.


제거할 수 없는 경고 메세지는 형 안정성이 확실할 때만
@SupressWarnings("unchecked") annotation을 사용해 억제해야 한다.
`SuppressWarnings` annotation은 개별 지역 변수부터 클래스까지 어떤 단위에도 적용이 가능하다.

- 작은 범위
- 짧은 method
- constructo
- 선언문에서만 사용이 가능(return x)


```
public <T> T[] toArray(T[] a){
    if(a.length < size){
        @SuppressWarnings("unchecked")
        T[] result = (T[]) Arrays.copyOf(elements, size, a.getClass());
        return result;
    }
    System.arraycopy(elements, 0, a, 0, size);
    if(a.length > size)
        s[size] = null;
    return a;
}
```

`@SuppressWarnings("unchecked")`를 사용할 때 왜 안전성이 확실한지 주석으로 남겨둬야 한다.


-------------------


### Rule No.25 배열 대신 리스트를 써라

- 배열: 공변자료형
- generic: 불변자료형


공변자료형: Sub(extends Super), Super 인 경우 Sub[]도 Super[]의 의 하위자료형이다
불변자료형: Type1, Type2가 있을때 List< Type1>은 List< Type2> 상위, 하위 자료형이 될 수 없다.



```
// 실행 중에 문제를 일으킴
Object[] objectArray = new Long[1]; // ArrayStoreException
objectArray[0] = "I love cakes";
```

```
// 컴파일 되지 않음
List<Object> ol = new ArrayList<Long>();
ol.add("I love cakes");
```

실행 후 Exception 에러가 발생되느냐 **or** 처음부터 compile이 안되게 만들것이냐 이다

##### 배열은 실체화(reification) 되는 자료형

String 객체를 Long 배열에 넣으려고 하면 ArrayStoreException이 발생한다.
generic은 삭제(erasure)과정을 통해 구현된다.
> 자료형에 관계된 조건들은 컴파일 시점에만 적용되고, 각 원소의 자로형 정보는 프로그램이 실행될 때는 삭제된다. 덕에 제너릭 자료형은 제너릭을 사용하지 않고 작성된 오래된 코드와도 문제없이 연동한다.


이러한 특징으로 배열과 generic은 함께 사용이 어렵다.

`new List<E>[]`, `new List<String>[]`, new E[]`는 전부 compile 되지 않는다.

##### generic 배열이 생성되지 않는 이유

typesafe(형 안정성)이 보장되지 않는다.


```
1. List<String>[] stringLists = new List<String>[1];
2. List<Integer> intList = Arrays.asList(42);
3. Object[] objects = stringLists;
4. Objects[0] = intList;
5. String s = stringLists[0].get(0);
```

1번이 compile 된다고 가정하자 2는 하나의 원소를 갖는 배열로 초기화한다. 3은 List
배열을 Object 배열에 대입한다. (공변 자료형이기 때문에 가능하다) 4은 2번의 0번
index에 대입한다. List 객체의 실행시점 자료형은 List이며, List[]의 실행시점 또한
List[] 이다. 따라서 ArrayStoreException이 나오지 않는다. 5에서 꺼낸 배열안의
유일한 원소는 자동적으로 String으로 변환된다. 하지만 원래의 값은 Integer이다.

따라서 `E[]`대신 `List<E>`를 사용하도록 하자

배열을 실체화 가능 자료형이고, generic은 불변 자료형이며, 실행 시간에 형인자
정보는 삭제된다. 따라서 **배열**은 **compile 시간에 형 안정성을 보장하지
못하며**, 제너릭은 보장할 수 있다.


---------------------------------


### Rule No.26 가능하면 generic 자료형으로 만들것

generic 자료형과 method를 사용하는건 어렵지 않다.

```
import java.util.Arrays;
import java.util.EmptyStackException;

import sun.misc.Cleaner;


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
    Object result = elements[--size];
    elements[size] = null; // 만기참조 제거
    return result;
  }

  public boolean isEmpty(){
    return size == 0;
  }

  public void ensureCapacity(){
    if(elements.length == size)
      elements = Arrays.copyOf(elements, 2 * size + 1);
  }

}
```

이 클래스가 generic화 하기 좋은 예시다. 호환성을 유지하면서도 generic 자료형을
사용할 수 있도록 개선할 수 있다.
generic을 사용하면 꺼낼때 casting을 생략할 수 있다.

```
import java.util.Arrays;
import java.util.EmptyStackException;

import sun.misc.Cleaner;

public class Stack<E> {
  private E[] elements; // ?
  private int size = 0;
  private static final int DEFAULT_INITIAL_CAPACITY = 16;

  public Stack(){
    elements = new E[DEFAULT_INITIAL_CAPACITY];
  }

  public void push(E e){
   ensureCapacity();
    elements[size++] = e;
  }

  public E pop(){
    if(size == 0)
      throw new EmptyStackException();
    E result = elements[--size];
    elements[size] = null; // 만기참조 제거
    return result;
  }

  public boolean isEmpty(){
    return size == 0;
  }

  public void ensureCapacity(){
    if(elements.length == size)
      elements = Arrays.copyOf(elements, 2 * size + 1);
  }

  @Override
  public Stack clone(){
    try{
      Stack result = (Stack) super.clone();
      result.elements = elements.clone();
      return result;

    } catch (CloneNotSupportedException e){
      throw new AssertionError();
    }
  }
}
```

그러면 이러한 에러가 나타난다

```
Stack.java:8: warning: [unchecked] unchecked cast
found : Object [] , required: E[]
elements= new Object[DEFAULT_INITIAL_CAPACITY];
```

>   바로 위 rule에서 E[]대신 List를 사용하라고 했는데

```
Stack.java:8: warning: [unchecked] unchecked cast
found : Object [] , required: E[]
elements=(E[]) new Object[DEFAULT_INITIAL_CAPACITY];
```

이런 방법으로 우회가 가능하다

1.  elements는 private 필드이므로 client에게 반환되지 않는다.

2.  다른 method에도 전달되지 않는다.

3.  push method에 전달되는 원소만이 배열에 저장된다.

4.  저장된 타입은 모두 E다.

5.  이러한 이유로 형변환을 해도 문제가 없다.

```
  @SuppressWarnings("unchecked")
  public Stack(){
    elements = new E[DEFAULT_INITIAL_CAPACITY];
  }
```

generic 배열 생성을 피하는 방법으로는 elements의 자료형을 E[]에서 Objects[]로
바꾸는것이다.

```
Stack.java:19: incompatible types
found : Object, required: E
    E result = (E) elements [ --size];
```

규칙 24에 따라 경고를 억제하자

```
  public E pop(){
    if(size == 0)
      throw new EmptyStackException();
    @SuppressWarnings("unchecked")
    E result = (E) elements[--size];
    elements[size] = null; // 만기참조 제거
    return result;
  }
```

generic stack class의 사용 예시

```
public static void main(String[] args){
    Stack<String> stack = new Stack<String>();
    for(String arg: args)
        stack.push(arg);
    while(!stack.isEmpty())
        System.out.println(stack.pop().toUpperCase());

}
```

그런데 이 예제는 규칙 25에서 배열 대신 리스트를 사용하라는 지침과 모순되어
보인다.

>   그러나 제너릭 자료형 안에서 리스트를 항상 사용할 수 있는것도 아니고, 항상
>   바람직한 것도 아니다. 리스트는 자바의 기본(native) 자료형이 아니다. 그래서
>   어떤 제너릭 자료형, 가령 ArrayList 같은 것은 반드시 배열 위에 구현되어야
>   한다. HashMap과 같은 제너릭 자료형도 서능 문제 때문에 배열 위에 구현된다. -p
>   191

위의 stack은 `Stack<Object>`, `Stack<int[]>`, `Stack<List<String>>` 등 객체
참조에 사용할 수 있는 자료형이면 다 된다.

단 `Stack<int>`나 `Stack<double>`과 같이 사용하면 compile 도중 오류가 난다.
이것의 대응 방안으로 boxed primitive type을 사용해아 한다.


-----------------


형인자를 제한하는 generic 자료형도 있다.

```
class DelayQueue<E extends Delayed> implements BlockingQueue<E>;
```

인자목록(`<E extends Delayed>`)을 보면 E는 반드시
`java.util.concurrent.Delayed`의 하위 자료형이어야 한다.

따라서 DelayQueue를 구현할때나 사용할때, DelayQueue에 저장되는 원소들을
명시적으로 Delayed로 형변환하거나 ClassCastException이 발생할것을 고려하지
않아도 된다.

가능하면 **기존**자료형을 **generic** 자료형으로 변환하라


-----------------


### Rule No.27 가능하면 generic method로 만들것

generification으로 혜택을 보는것은 class뿐 아니라 method도 포함 된다.
**static** untility method는 특히나 generic하기 편리하다

```
public static <E> Set<E> union(Set<E> s1, Set<E> s2){
    Set result = new HashSet(s1);
    result.addAll(s2);
    return result;
}
```

이 정도 코드는 에러없이 compile이 가능하다. 형 안정성도 보장한다.

```
public stativc void main(String[] args){
    Set<String> guys = new HashSet<String>{
        Arrays.asList("Tom", "Dick", "Harry")
    };
    Set<String> stooges = new Hashset<String>(
        Arrays.asList("Larray", "Moe", "Curly")
    );
    Set<String> aflCio = union(guys, stooges);
    System.out.println(aflCio);
}
```

이 method를 실행하면 [Moe, Harray, Tom, Curly, Larry, Dick] 이라 나오고 순서는
JDK에 따라 달라진다.

입력값, 출력값 모두 `String`이기 때문에 정적 와일카드 자료형을 사용하면 더
유연한 method 구현이 가능하다.

**생성자** 호출시엔 명시적으로 주어야 했던 형인자를 전달할 필요가 없다.
paramter가 `Set<String>` 이므로 compiler는 E가 String임을 알 수 있다. 이 과정을
**자료형 유추(type inference)**라 한다.

```
// 똑같이 만들어야 함.
Map<String, List<String>> anagrams = new HashMap<String, List<String>>();
```

이런 번거로움을 피하기 위해서 static factory method를 만드는 방법을 사용하면
된다.

```
public static <K, V> HashMap<K, V> newHashMap(){
    return new HashMap<K, V>();
}
```

이 결과로 중복되는 형인자를 제거하여 간결한 코드 작성이 가능하다

```
Map<String, List<String>> anagrams = newHashMap();
```

>   java 1.7 부터는 추론이 가능하다 고로 위 코드는 지나가도 좋다



이것에 관련된 **싱클톤 패턴**이 있다.

때로는 변경이 불가능하지만 많은 자료형에 적용 가능한 객체를 만들어야 할때
사용한다. generic은 자료형 삭제 과정을 통해 구현되므로 모든 필요한 형인자화
과정에서 동일 객체를 활용할 수 있다. 그러려면 필요한 형인자화 과정마다 같은
객체를 나눠주는 static factory method를 작성해야한다.

```
public static interface UnaryFunction<T>{
    T apply(T arg);
}
```

[항등함수](https://en.wikipedia.org/wiki/Identity_function)를 구현한다고 할때,
항등함수는 상태가 없는 함수이므로, 필요할때마다 새로운 method를 만드는것은
효율적이지 않다.

```
private static UnaryFunction<Object> IDENTITY_FUNCTION
	= new UnaryFunction<Object>(){
    	public Object apply(Object arg){ return arg;
    }
}

@SuppressWarning("unchecked")
public static <T> UnaryFunction<T> identityFunction(){
    return (UnaryFunction<T>) IDENTITY_FUNCTION;
}
```

IDENTITY_FUNCTION을 (UnaryFunction)로 casting하면 unchecked cast warning이
발생한다. 하지만, 항등함수는 특별히 인자를 수정없이 반환하기 때문에 T가 무엇이든
UnaryFunction 인 것 처럼 써도 형 안정성이 **보장** 된다.

싱글톤 사용 예시

```
public static void main(String[] args){
    String[] strings = {"jute", "hemp", "nylon"};
    UnaryFunction<String> sameString = identityFunction();
    for(String s : strings)
        System.out.println(sameString.apply(s));

	Number[] numbers = {1, 2.0, 3L};
    UnaryFunction<Number> sameNumber = identityFunction();
    for(Number n : numbers)
        System.out.println(sameNumber.apply(n));
}
```


형인자가 포함된 표현식으로 형인자를 한정할 수 있다.
이런 용벅을 재귀적 자료 한정recursive type bound) 이라한다.

```
public interface Comparable<T>{
	int compareTo(T o);
}
```

`compareTo` 는 모두 같은 자료형 객체만 비교할 수 있다.(당연)
Comparable을 구현하는 원소들의 리스트를 인자로 받는 method가 많은데
이러한 method들은 정렬, 탐색 등을 하며 max, min을 계산한다.

이러한 작업을 위해서 원소들이 서로 비교 가능해야하고, 이 조건을 을 위해
아래와 같이 표현할 수 있다.


```
	public static <T extends Comparable<T>> T max(List<T> list)){...}
```

이것을 문장으로 표현하면 **자기 자신과 비교 가능한 모든 자료형 T** 이다

**재귀적 한정**을 구현한 예시 method
```
public static <T extends Comparable<T>> T max(List<T> list){
    Iterator<T> i = list.iterator();
    T result = i.next();
    while(i.hasNext()){
        T t = i.next();
        if(t.compareTo(result) > 0)
            result = t;
    }
    return result;
}
```

generic method는 클라이언트가 직접 입력 값, 반환값의 자료형을 **casting** 하는것
보다 사용하기 쉽고 typesafe도 유지하기 쉽다. 시간날때 기존의 method를
generic으로 수정해두는 작업을 해두면 편리하다


---------------------------


### Rule No.28 한정적 와일드카드를 서서 API 유연성을 높여라

generic은 **불변(invariant)** 자료형이다. `List< String>`, 이 `List< Object>`의 하위 자료형이 아니다.

```
public class Stack<E>{
    public Stack();
    public void push(E e);
    public E pop();
    public boolean isEmpty();
}
```

이러한 스택에 원소들을 인자로 받아 차례로 스택에 넣는 `pushAll`을 구현하고 싶다면

```
public void pushAll(Iterable<E> src){
    for(E e : src)
        push(e);
}
```

compile(실체화 불가능 자료형)시엔 문제가 없지만, Stack 인경우를 고려하면
Integer형의 intVal로 `push(intVal)`을 호출하면 제대로 동작한다. Integer는
Number의 하위 자료형이기 때문이다.

```
Stack<Number> numberStack = new Stack<Number>();
Iterable<Integer> integers = ...;
numberStack.pushAll(integers);
```

하지만 이 코드에서는 에러가 발생한다.

```
StackTest.java:7: pushAll(Iterable<Number>) in Stack<Number>
cannot be applied to (Iterable<Integer>)
numberStack. pushAll (integers);
```

왜냐하면 불변(invariant) 자료형이기 때문이다. 정확히는 "Number의 하위자료형
Integer가 사용되도록 해야한다." 가 명시 되야한다.

```
List<Object> list1;
List<String> list2;
```

list1이 Object 타입이라고 해서 list2와 같은것은 아니다

이것을 와일드카드 자료형을 써서 구현해야한다.

(생산자)

```
public void pushAll(Iterable< ? extends E> src){
    for(E e : src)
        push(e);
}
```

이렇게 바꾸면 pushAll method로 컴파일 되지 않았던 코드들 까지 컴파일된다.
맞찬가지로 popAll도 동일하게 구현이 가능하다 그런데 Stack 이고, Object 형의
변수가 하나 있다고 하면

```
Stack<Number> numberStack = new Stack<Number>();
Collection<Object> objects = ...;
numberStack.popAll(objects);
```

위에서 제시했던것과 비슷한 에러가 발생한다. 이 상황은 "Number의 상위자료
Object가 사용되도록 해야한다." 가 명시되야 한다.

(소비자)

```
public void popAll(Collection< ? super E> dst){
    while(!isEmpty())
        dst.add(pop());
}
```

Number는 Integer보다 상위 -> `extends` - 생산자 Object는 Number보다 상위 ->
`super` - 소비자

생산자 === 소비자 인경우엔 wildcard를 제외하면 된다. 어떤 wild card를 사용할지
암기가 어렵다면 PECS - (Produce - extends, Consumer - super) 로 암기!

이 관점에서 보면

```
public static <E> Set<E> union(Set<E> s1, Set<E> s2)
```

s1과 s2는 모두 생산자 이다.

```
public static <E> Set<E> union(Set< ? extends E> s1,
                                Set< ? extends E> s2)
```

와 같이 리팩토링이 가능하다.

return type은 Set 이다. 반환값에는 와일드카드 자료형을 쓰면 안된다. 클라이언트
코드에서도 **wildcard 자료형**을 사용해야하기 때문이다 클래스 사용자가 wildcard
자료형에 대해 고민한다면, 클래스 설계가 잘못된 것이다.

```
Set<Integer> integers = ...;
Set<Double> doubles = ...;
Set<Number> numbers = union(integers, doubles);
```

이런 코드를 만들 수 있다고 생각하지만 아래와 같은 에러가 나타난다

```
Union.java:14: incompatible types
found : Set<Number & Comparable<? extends Number & Comparable< ?>>>
required: Set< Number>
Set< Number> numbers = union(integers , doubles);
```

컴파일러가 정확한 자료형을 유추하지 못할경우 명시적 형인자를 통해 어떤 자료형을
쓸지 알려준다.

`Set<Number> numbers = Union.<Number>union(integers, doubles);`

max

```
public static <T extends Comparable<T>> T max(List<T> list)
```

와일드 카드 자로형을 써서 고쳐보면 다음과 같다.

```
public static <T extends Comparable<? super T>> T max(List< ? extends T> list)
```

Comparable은 언제나 **소비자**이다. 따라서 Comparable< ? super T>를
사용해야한다. 비교자도 마찬가지로 Comparator< ? super T>를 사용해야한다.

```
public static < T extends Comparable< ? super T>> T max(
    List< ? extends T> list){

    Iterator<T> i = list.iterator();
    T result = i.next();
    while(i.hasNext()){
        T t = i.next();
        if(t.compareTo(result) > 0)
            result = t;
    }
    return result;
}
```

compile하면 아래와 같은 에러가 나온다

```
Max.java:7: incompatible types
found   : Iterator<capture#591 of? extends T>
required: Iterator<T>
    Iterator<T> i = list. iterator();
```

list가 List가 아니므로 iteraotr method가 Iterator를 반환하지 않는다는 뜻이다.

`Iteraotr< ? extends T> i = list.iteraotr();` 로 수정해야 한다.

\***다시봐야함**




----------------------------

### Rule No.29형 안정 다형성 컨테이너를 쓰면 어떨지 따져보라

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

# chapter 7. method


--------------------------

### Rule No.38 parameter 유효성 검사


parma이 `null`이나 음수가 될 수 없는 경우에는 제한을 해야한다.

되도록 method 시작 부분에 적용하여 error를 빠르게 찾아내야 한다.

이런 규칙을 생략하면 **debugging**이 어려워 진다.


유효성 검사가 없다면

1. 이상한 예외를 내면서 죽거나
2. 동작은 하는데 엉뚱한 결과를 낸다 -> 후에 엉뚱한 곳에서 에러를 낸다

public method에 대해서는 Exception class 들을 이용해서 유효성 검사를 하고

public이 아닌 method는 개발자가 제어할 수 있는 상황이므로 `assertion`을 이용한다.


```
private static void sort(long a[], int offset, int length){
	assert a != null;
    assert offset >= 0 && offset <  a.length;
    assert length >= 0 && length <= a.length;

    // > 계산 수행
}
```

통상적인 유효성 검사와 달리 assertion을 이용하여 조건이 만족되지 않으면 `AssertionError`를 낸다

생성자는 유효성 검사를 반드시 해야한다는 원칙의 특별한 경우에 해당한다.

유효성 검사도 예외를 두어 하지 말아야 하는 경우가 있다.


`Collection.sort(List)` 처럼 객체 리스트를 정렬 하는 경우.

리스트 내부의 모든 객체가 비교할 수 있어야 하기 때문에 비교가 불가능한경우

`ClassCastException`이 발생한다.


> 즉, 유효성 검사를 실행하는 오버헤드가 너무 크거나, 비현실적이고, 계산과정에서 유효성 검사가 자연스럽게 이루어지는 경우다.

모든 parameter에 대해서 적절한 동작을 수행한다면, 제약이 적을 수록 좋다.

따라서 method, contructor에 제한을 두고 그 사실을 문서에 남겨야 한다.

--------------------------

### Rule No.39 필요하다면 방어적 복사본을 만들어라

자바가 편리한 이유는 safe language이기 때문이다.

native method를 사용하지 않으면 buffer overrun이나 array overrun, wild pointer 같은

메모리 훼손 오류가 생기지 않는다.


하지만, 우리가 코드를 작성할 때에는 client 사용자가 작성된 class의 invariant를 망가뜨리기위해

최선을 다한다는 최악의 경우를 고려하며 만들어야 한다.


왜냐하면 실수로 API를 잘못 사용할 수 있음을 고려해야 하기 때문이다.

```
import java.util.Date;

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
```

얼핏 봐서는 변경이 불가능해 보이지만, Date는 변경이 가능하다

```
Date start = new Date();
Date end = new Date();
Period p = new Period(start, end);
end.setYear(78); // p의 내부를 변경했다.
```

따라서 constructor에 전달되는 변경가능 객체를 **방어적**으로 복사해야 안전하다


```
  public Period(Date start, Date end){
    this.start = new Date(start.getTime());
    this.end = new Date(end.getTime());
    if(this.start.compareTo(this.end) > 0)
      throw new IllegalArgumentException(
          this.start + " after " + this.end
      );
  }
```
**Q)**실제로 이렇게 사용하는지


여기서 주목할만한 부분은 유효성 검사를 하기 전에 방어적 **복사본**을 **만들었다**는 사실에 유의해야한다.

유효성 검사는 복사본에 대해서 시행해야한다.


인자를 검사한 **직후** 복사본이 만들어지기 직전까지의 시간에 다른 thread가

인자를 변경해버리는 일을 막기 위한 것이다.


게다가 clone을 사용하지 않았다.

그런데 paramter를 통한 공격은 방어했지만, 접근자를 통한 공격은 막을 수 없다.


```
Date start = new Date();
Date end = new Date();
Period p = new Period(start, end);
p.end().setYear(78);
```

이러한 공격을 막기 위해 변경 가능 내부 필드도 방어적 복사본을 사용해야 한다.

```
  public Date start(){
    return new Date(start.getTime());
  }
  public Date end(){
    return new Date(end.getTime());
  }
```

코드가 이렇게 변경되면 변경 불가능 클래스가 된다. constructor와 다르게 `clone`을 사용해도 된다

length가 0이 아닌 모든 배열은 **변경이 가능하다** 라는 사실을 염두해야한다.


방어적 복사본을 사용할땐 성능상 손해가 있다. 클라이언트가 패키지 내부에 있어 변경하지 않는다는게

보장되면, 방어적 복사본을 만들지 않아도 된다. 단, 이러한 상황에서는 method 호출자가

parameter나 return을 변경하면 안된다는 사실을 명시해야 한다.


**summary**

클라이언트로부터 구했거나 반환되는 변경 가능 component의 경우 방어적으로 복사해야한다.


--------------------------

### Rule No.40 메서드 시그너처는 신중하게 설계하라

1. method naming은 신중하게
	- 이해하기 쉽게
	- 같은 패키지의 다른 이름과 일관성이 유지
2. 더 널리 부합하는 이름을 사용하라
3. 편의 method를 제공하는데 너무 리소스 낭비를 하지 마라
	- 맡은 일이 명확하고 충실해야한다
	- method가 너무 길면 test, 유지보수가 어렵다
	- 자주 쓰이는 것만 모듈화하고 아니면 빼라
4. parameter list를 길게 만들지 마라
	- 4개 이하가 적당하다

긴 parameter list를 줄이는 방법
1. method를 나눠라
2. helper 클래스를 만들어라
	- static member class 이다
3. builder pattern을 고쳐서 객체 생성 대신 method 호출에 적용하라
	- 앞 부분에서 논의된적이 있으므로 생략

#### 인자의 자료형으로는 class보다 interface가 좋다

`HashMap`을 인자의 자료형으로 사용할 필요는 없다.`Map`을 사용하는게 좋다

그렇게 하면 `Hashtable`을 인자로 받을 수 있고, `TreeMap`, `TreeMap`의 하위 자료까지

모두 param으로 받을 수 있기 때문이다. class를 사용하면 종속적이다


#### 인자의 자료형으로 boolean 보다, 원소 2개인 enum이 낫다

IDE를 사용한다면 더욱 잘맞게 되고, 다른 옵션을 추가하기도 편리하다


--------------------------

### Rule No.41 오버로딩 할 때는 주의하라

```
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CollectionClassifier {
  public static String classify(Set<?> s){
   return "Set";
  }

  public static String classify(List<?> lst){
    return "List";
  }
  public static String classify(Collection< ?> c){
    return "unknown collection";
  }

  public static void main(String[] args){
    Collection< ?>[] collections = {
        new HashSet<String>(),
        new ArrayList<BigInteger>(),
        new HashMap<String, String>().values()
    };
    for(Collection< ?> c : collections)
      System.out.println(classify(c));
      // just overloading
  }
}
```

이 코드를 실행시키면 Set, List, Unkonwn Collection을 순서대로 출력하리라 예상했지만

Unknown Collection을 3번 출력한다.

overroading 된 method 가운데 어떤것이 호출될지 compile 시점에서 결정된다.

>overloading된 method는 static으로 선택되지만, overriding 된 method는 동적으로 선택된다.

**무슨소린지 모르겠으니 아래 예제를 확인하면**



```
class Wine{
	String name(){ return "wine"; }
}

class SparklingWine extends Wine{
	@Override String name(){
    	return "sparkling wine";
    }
}

class Champagne extends SparklingWine{
	@Override
    String name(){ return "champagne"; }
}

public class Overriding{
	public static void main(String[] args){
    	Wine[] wines = {
        	new Wine, new SparklingWine(), new Champagne()
        };
        for(Wine wine : wines)
        	System.out.println(wine.name());
    }
}
```


compile 시점은 항상 Wine 이었지만, 순서대로 wine, sparkling wine, champagne 이 출력된다.

overring 가운데 하나를 선택해도 객체의 컴파일 시점은 아무 영향을 주지 못한다.


CollectionClassifier 의 의도는 실행시점의 자료형을 근거로 Overloading된 method 가운데

적절한 것을 **자동**으로 실행해서 인자의 자료형을 출력하는 것이다.

`classify` 라는 method는 `CollectionClassifier`라는 class 안에 여러개 정의되어 있고

구분할 수 있는것이 parameter의 type밖에 없으므로 알맞은 param type을 체크하였기 때문에

collection param이 들어왔을때 결과인 "Unknown colection"이 나오는것이다.



overloading에선 이러한 역할을 할 수 없으므로, 3개의 method를 합치는게 가장 좋다


```
  public static String classify(Collection< ?> c){
    return c instanceof Set ? "set" :
          c instanceof List ? "List" : "Unknow Collection";
  }
```

overriding이 일반적이라면 overloading은 예외에 해당하고, overring method 호출이 어떻게

처리되는가는 예측에 부합한다. 오버로딩은 이런 예측에 혼란을 준다.



혼란을 피하기 위해서는 같은수의 parameter를 갖는 두개의 overloading method를 API에 포함하지 않는것이다.

`varargs`를 사용하는 경우에는 오버로딩을 아예 하지 않는 전략을 취한다
업무량이 늘어날 것으로 보일 수 있지만 method의 이름을 다르게 하는게 좋다


**생성자**의 경우는 method 이름을 바꾸는것이 불가능하므로 static factory method를 사용하는게 방법이 될 수 있다.
그리고 **생성자**는 이러한 매커니즘을 신경쓰지 않아도 되는것이, param type이 다르다면 충분히 논리적으로 사용이 가능하다.
또한 형변환 할 수 없게 만들어도 된다.


```
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class SetList {
  public static void main(String[] args){
    Set<Integer> set = new TreeSet<>();
    List<Integer> list = new ArrayList<>();

    for(int i = -3; i< 3; i++){
      set.add(i);
      list.add(i);
    }
    for(int i=0; i<3; i++){
      set.remove(i);
      list.remove(i);
    }
    System.out.println(set + " " + list);
  }
}

```

이 코드는 -3, -2, -1, 0, 1, 2 를 `Set`, `List`에 넣는 작업을 한다

그리고 remove를 똑같이 3번 호출하여 [-3, -2, 1] [-3, -2, 1] 이 나오는걸 생각하지만

실제로는 [-3, -2, -1] [-2, 0, 2]가 출력된다.




`set.remove(i)`는 재정의 된 `remove(E)`가 호출된다. 의도한대로 동작한다

그런데 `list.removce(i)` 호출 결과가 `remove(int i)`이다.

저장된 **위치**에 있는 원소를 제거한다. 따라서 [-2, 0, 2]가 출력된다



```
list.remove((Integer) i);
```
로 수정해주면 원래 원하는 값으로 나오는데,


이렇게 되는 이유는 List의 interface에 `remove(E)`와 `remoce(Object)`가 **둘 다 있기 때문이다.**

generic과 자동 객체화(auto boxing)이 도입되면서 이러한 문제가 발생한 것이기 때문에

overloading을 할때 주의해서 사용해야 한다.




결론적으로 param의 갯수가 같은 함수에 대해서는 overloading을 최대한 피해야한다.

생성자라면 이 규칙을 따를 수 없겠지만, 형변환만 추가하면서 여러개를 만드는것을 피해야한다.




--------------------------

### Rule No.42 varargs는 신중히 사용하라

```
static int sum(int... args){
	int sum = 0;
    for(int arg : args)
    	sum += arg;
    return sum;
}
```

이렇게 함수를 만들면 args에는 0개 이상의 인자가 들어올 수 있다.

하지만, 반드시 1개 이상의 인자가 필요할 때가 있다.


```
static int mint(int... args){
	if(args.length == 0)
    	throw new IllegalArgumentException("Too few arguments");
    int min = args[0];
    for(int i = 1; i < args.length ; i++)
    	if(args[i] < min)
        	min = args[i];
    return min;
}
```

이렇게 하여 0개도 안들어왔을 경우 exception을 처리하는것인데 올바른 코드가 아니다

이런 경우는 인자를 2개 받도록 만드는것이 최선이다.

왜냐하면 **인자없이** method를 **호출**하는것이 가능할 뿐 아니라 실행 도중에 exception이 나오기 때문이다.(최악)


```
static int min(int firstArg, int... remainingArgs){
	int min = firstArg;
    for(int arg : remainingArgs)
    	if(arg < min)
        	min = arg;
    return min;
    // -> 무조건 하나는 들어오게 된다.
}
```

현재 이렇게 임의의 갯수의 parameter를 사용할때 varagrs를 사용한다.

`printf`가 그 예시이다.



물론 이 방법이 항상 좋은것은 아니다.

```
List<String> numbers = Arrays.asList("to", "too", "two");
```

이렇게 한 것은 큰 실수였다. java 1.5 이전에 배열 내용을 **출력** 하기위해

```
System.out.println(Arrays.asList(myArray));
```

코드를 이렇게 만들었을 경우 [Ljava.lang.Integer;@343534] 와 같이 출력된다.(출력도 안되었다고 함)

java 1.5부터는 Array 클래스에는 varargs를 받는 method로 바꾼다는 결정을 내렸기 때문에

오류없이 compile이 가능하고, 출력되는 결과값도 예측한대로 나타난다.


`Arrays.asList` 함수는 객체 참조를 모아 배열로 만든다(실제 데이터가 아닌 참조값)

int 배열 digits에 대한 참조가 담긴 길이 1 짜리 배열, 배열의 배열이 만들어진다

`List<int[]>` 이 리스트에 `toString`을 호출하면 다시 int 배열의 toString이 호출되어

원하는 문자열이 만들어진다.



> 마지막 인자가 배열이라고 해서 무조건 수정할 필요는 없다., varargs는 임의 개수의 인자를 처리할 수 있는 method를 만들어야 할 때만 사용한다.



그러니깐 varagrs의를 사용할때 정말 주의해야 하는것은

```
RerturnType1 suspect1(Object... args){}
<T> ReturnType2 suspect2(T... args){}
```

아무 인자 리스트나 받을 수 있는 method들은 compile 시점에서 자료형 검사가 불가능하다.

따라서 성능이 중요한 상황이라면 `varargs`를 사용하는데 있어서 더욱 신중해야한다.

오버헤드를 감당할 수 없을 상황이면 다른 패턴을 사용하는것도 좋다


예를들면 overloading이다

```
public void foo(){}
public void foo(int a1){}
public void foo(int a1, int a2){}
public void foo(int a1, int a2, int a3){}
public void foo(int a1, int a2, int a3, int... rest){}
```

varargs method는 인자 개수가 **가변적인** method를 정의할때 편리하지만 사용을 신중히 해야한다.




--------------------------

### Rule No.43 null 대신 빈 배열이나 collection을 반환하라

**와 이건 진짜 대박**

```
public Cheese[] getCheeses(){
	if(cheeseInStock.size()==0)
    	return null;
}
```

치즈가 없을때 null을 주는 함수이다.

```
Cheese[] cheese = shop.getcheese();
if(chsses != null && //null 체크
	Arrays.asList(cheeses).contains(Cheese.STILTON))
    System.out.println("Jolly good, just the thing.");
```

그런데 `null`이 아닌 빈 배열이나, collection이 반환된다면

```
if(Arrays.asList(cheeses).contains(Cheese.STILTON))
    System.out.println("Jolly good, just the thing.");
```

이렇게 간단하게 처리할 수 있다. `null`은 오류를 수반하기가 쉽다.

`null`에 관한 처리를 잊을 수도 있기 때문에 권장하지 않는다.



null을 반환하는것이 배열을 반환하는거 보다 가볍고 메모리에 대한 리소스를 덜 차지 하지 않는가? 라는 질문을 할 수 있다.

1. 해당 method가 성능 저하의 주범이라는것이 밝혀지지 않는 한, 이 수준까지의 질문은 바람직하지 않다.
2. 길이가 0인 배열은 immutable 이므로 아무 제약없이 사용할 수 있다.
3.

```
private final List<Cheese> cheesesInStock = ...;
private static final Cheese[] EMPTY_CHEESE_ARRAY = new Cheese[0];

public Cheese[] getCheeses(){
	return cheesesInStock.toArray(EMPTY_CHEESE_ARRAY);
}
```

`toArray`에 전달되는 빈상수는 반환하는 값의 자료형을 명시하는 역할을 한다.

`toArray`는 반환되는 원소가 담긴 배열을 스스로 할당하는데, collection이 비어있는 경우에는

인자로 주어진 배열을 사용한다.


```
public List<Cheese> getCheeseList(){
	if(cheesesInStock.isEmpty())
    	return Collection.emptyList();
    else
    	return new ArrayList<Cheese>(cheeseInStock);
}
```

null 대신 빈 배열이나 빈 collection을 반환하라


--------------------------

### Rule No.44 모든 API 요소에 문서화 주석을 달아라

사용할 수 있는 API의 조건은 문서가 있어야한다. java는 javadoc 이라는 유틸이 포함되어 있어서

API 문서작업을 쉽게 할 수 있다.


문법같은것이 정해져있는 것은 아니지만, 배워야하고 어느정도 표준화되어 있다.

오라클에는 [How to Write Doc Comments](http://www.oracle.com/technetwork/articles/java/index-137868.html) 라는 문서가 설명되어 있다.


좋은 API 문서는 class, interface, constructor, method, field에 문서화 주석을 달아야 한다.


상속을 고려한 method가 아니라면 어떻게 그 일을 하는지 설명해서는 안된다.

그리고 **선행조건**과 **후행조건**을 나열해야 한다.


선행조건: 클라이언트가 method를 호출하려면 `true` 가 되어야하는 조건들

보통 `@throws` 태그를 통해 암묵적으로 기술함

관계된 인자는 `@param` 태그를 통해 명시함


그리고 **side effect**에 대해서도 문서화 해야한다.

background thread를 실행한다면 그 사실이 명시화 되어야하고, thread safety에 대해서도 남겨야한다.

void가 아니라면 `@return`도 필수이다.

예외처리에 대해서는 모두 `@throws` 태그도 붙여야 한다.



```
/**
* Returns the element at the specified position in this list.
*
* <p>This method is <i>not</i> gua 「anteed to run in constant
* time. In some implementations it may run in time proportional
* to the element position.
*
* @param index index of element to return; must be
* 		  non-negative and less than the size of this list
* @return the element at the specified position in this list
* @throws IndexOutOfBoundsException if the index is out of range
* 		  ({@code index < e || index >= this. size()})
*/
```

javadoc 유틸리는 문서화 주석을 HTML 문서로 변환해준다.

`{@code}` 태그가 사용한은 코드 폰트로 나타내기 위한것이다.

코드가 여러줄일 경우엔 `<pre></pre>` 태그안에 넣으면 된다.



{@literal} 태그는 <, >,& 와 같은 문자들을 주석의 일부로 사용할때 편리하다


1. 최대한 javadoc으로 변환한 결과물의 가독성을 우선시해야한다.
2. 마침표가 여러번 오는경우도 주의해야 한다. 마침표 이후 첫 줄까지는 문장의 끝으로 간주한다
3. 주석은 완벽한 문장일 필요가 없다. 최대한 동사구로 작성하여야한다.
4. generic 자료형이나 method에 주석을 달때는 모든 자료형 인자를 설명해야 한다.
5. enum 자료형에 주석을 달 때는, public 뿐 아니라 상수에도 달아야 한다.
6. annotation 자료형에 주석을 달 때는 자료형 뿐 아니라 모든 멤버에도 주석을 달아야 한다.


-------------------

## chapter 8. 일반적인 프로그래밍 원칙들

자바의 기본적인 부분을 살펴본다

1. 지역변수
2. 제어구조
3. 라이브러리 사용
4. 다양한 자료형 사용
5. reflaction
6. native method


----------------

### Rule No.45 지역 변수의 유효범위를 최소화하라


1. 지역변수는 유효범위를 최소화하기 위해, 처음 사용하는 곳에 선언한다.
2. 거의 모든 지역 변수 선언에는 **초기값**이 포함되어야 한다.
	- 적정한 초기값을 설정할 수 없다면 선언을 미루는것도 좋은 방법이다.
	- `try~catch`는 예외
3. loop을 잘 쓰면 유효범위를 취소화한다.
	- for 뒤 ()안에 선언된 값을 잘 사용한다.
	- 따라서 `while`보다는 `for`가 좋다
	```
    	Iterator<Element> i = c.interator();
        while(i.hasNext()){
        	doSomething(i.next());
        }

        for(Iterator<Element> i = c.iterator(); i.hasNext(); i){
        	doSomething(i.next());
        }
    ```
    - 같은 변수명을 연속해서 사용할 수 있다.
4. method의 크기를 줄이고 특정 기능에 집중한다.

두가지 서로 다른 기능을 하나의 method에 넣어두면 한 가지 기능을 수행하는데 필요한
지역 변수의 유효범위가 다른 기능까지 확장된다.


----------------

### Rule No.46 for문 보다는 for-each

```
for(Iteraotr i = c.iterator(); i.hasNext(); ){
	doSomething((Element) i.next()); // generic이 없었을때
}

for(int i=0; i<a.length; i++){
	doSomething(a[i]); // -> 많이 사용
}
```

`while`을 사용하는것 보다는 낫지만 완벽하지 않다.
반복자나 첨자, 변수가 여러모로 생산성을 낮추기 때문이다.

```
for(Element e : elements){
	doSomething(e);
}
```

어떤 상황에서는 일반 `for`보다 더 나은 성능을 보이기도 한다.
특히 2중 for문을 사용할때는 더욱 효율적인 코딩이 가능하다

```
for(Iteraotr<Suit> i = suits.iteraotr(); i.hasNext(); ){
	Suit suit = i.next();
    for(Iterator<Rank> j = ranks.iterator(); j.hasNext(); )
    	deck.add(new Card(suid, j.next()));
}
```

이러한 코드를

```
for(Suid suit : suits)
	for(Rank rank : ranks)
    	deck.add(new Card(suid, rank));
```

`Iterable` interface를 구현하는 어떤 객체도 순회할 수 있다. method가 하나뿐이다.

```
public interface Iterable<E>{
	Iterator<E> iterator();
}
```

Iterable interface는 되도록 꼭 구현하여 for-each를 사용하는게 좋다

하지만 이러한 경우에는 이 좋은 for-each를 사용할 수 없다.


1. filtering
	- collection을 순회하다가 특정 원소를 삭제할 수 없다.
	- 반복자를 명시적으로 사용해야한다.
2. transforming(변환)
	- 리스트, 배열을 순회하다 일부 혹은 전부의 값을 변경할땐 반복자, 첨자가 필요하다.
3. parallel iteration(병렬 순회)
	- 2개 이상의 collection이 있다면 하나의 for문에서 처리가 불가능하다

이런 경우에만 일반 for문을 사용하고, 아니라면 for-each를 사용하자


----------------

### Rule No.47 어떤 라이브러리가 있는지 파악하고, 적절히 활용하라

랜덤 난수를 발생하는 함수를 만들어야 할 경우 난수 발생에 대한 수학적 지식과 알고리즘없이

만들게 된다면, 모든 숫자가 동일한 확률로 나오는게 아닌 특정 수, 특정 패턴의 숫자만

반복해서 나오게 된다.


```
private static final Random rnd = new Random();

static int random(int n){
	return Math.abs(rnd.nextInt()) % n;
}
```


이런 경우 n이 크지 않은 2의 제곱수인 경우 중복 난수 발생이 높다

2의 제곱수가 아닌경우는 특정 수에서 많은 중복이 발생된다.



하지만 `Random.nextInt(int)` 를 사용하면 수 이론, 2의 보수연산 등 전문적인 지식과

알고리즘이 들어가 설계자가 전문가로부터 검증받아 사용되었고

근 반세기 동안 수많은 개발자들이 사용하면서 검증되어 왔다.


이러한 표준 라이브러리(standard library)를 사용하면 그 라이브러리를 개발한 전문가의 지식과

먼저 해당 라이브러리를 사용한 개발자들의 경험을 사용할 수 있다.



게다가 이런 라이브러리는 별다른 노력을 하지 않아도 성능이 점점 개선된다.


- [d3](https://github.com/d3/d3) - 데이터 시각화
- [pandas](http://pandas.pydata.org/) - 데이터 분석
- [jquery](https://jquery.com/) - jquery


java 개발자라면 `java.lang`, `java.util` 안에 있는 내용은 어느정도 숙지를 해야하며,

`java.io`의 내용도 어느정도 알고 있으면 좋다.



**java.util.concurrent** 패키지를 보면 병행성(task) 관리에 대한 패키지들이 추가되었다.


다중 thread를 만들 수 있도록 저수준의 utility가 포함되어 있다.

`java.util.concurrent` 패키지에 있는 고수준 병행성 유틸리티들은 기본적으로 알고 있을 필요가 있다


바퀴를 다시 발명하지말라(don't reinvent the wheel) 이라는 말이 있다.

최대한 라이브러리를 뒤져보면서 필요한 기능을 찾아 구현해라



Q) 그런데 라이브러리를 구성하는데 필요한 지식 없이 그냥 사용해도 괜찮을까??

celery, redis etc..

----------------

### Rule No.48 정확한 답이 필요하다면 float와 double을 피하라


float, double은 과학, engineering 연산을 위해 설계도니 자료형이다.

부동 소주점 연산을 수행하는데 넓은 범위의 값에 대해 정확도 높은 **근사치**를 제공하기 위해 설계되었다.

따라서 정확한 결과를 제공하지 않기 때문에 **돈**과 관련도니 계산에는 적합하지 않다.


돈 계산을 할 때에는 **BigDecimal**, **int**, **long** 을 사용해야한다.


하지만 BigDecimal을 사용하면 느리고 불편하다. 그래서 int, long을 사용하는것이다.

십지수 19개 이상일땐 BigDecimal을 그 아래는 long, int를 사용한다.


----------------

### Rule No.49 객체화된 기본 자료형 대신 기본 자료형을 사용하라

- 기본자료형: int, double, boolean
- 객채화기본자료형: Integer, Double, Boolean
- 참조 자료형: String, List


기본자료형과 **객채화 기본자료형**은 3가지의 큰 차이점이 있다.

1. 기본자료형은 **값** 만 가지지만, 객체화 기본자료형은 identity를 갖는다.
	- 두개가 있을경우 값은 같지만 신원이 다를 수 있다.
2. 값은 기능적으로 완전하지만, 객체화된 기본자료형에 저장된 값 에는 아무 기능도 없는값(null)이 하나 있다.
3. 기본자료형은 시간, 공간 요구량 측면에서 효율적


`compare`: 첫 인자의 값이 두 번째 인자의 값보다 작을때, 같을때, 클때 -> 음, 0, 양


```
Comparation<Integer> naturalOrder = new Comparator<Integer>(){
	public int compare(Integer first, Integer second){
    	return first < second ? -1 : (first == second ? 0 : 1);
    }
    // > 문제될게 없어보이지만... Integer는 identity를 갖고있다.
}
```

얼핏 봐서는 문제가 없어보이지만 이러한 문제가 있다


```
naturalOrder.compare(new Integer(42), new Integer(42))
```

둘다 값이 42 이기 때문에 0이 나와야 하지만 1이 나온다.


만약 첫번째 값이 41이거나 0이라면 제대로 음수가 나올것이다.

두번째가 43이거나 이보다 큰 양수라면 제대로 1이 나온다

하지만 값이 같은 경우에는 == 연산자로 비교하는데 이때 identity를 비교하는 값은 다르다

그래서 숫자가 같은 경우는 무조건 오류라고 봐야한다.


```
Comparator<Integer> naturalOrder = new Comparator<Integer>(){
	public int compare(Integer first, Intger second){
    	int f = first;
        int s = second;
        return f < s ? -1 : (f == s ? 0 : 1);
        // -> 이렇게 숫자만 꺼내서 비교해야 한다.
    }
}
```

이렇게 int에 숫자만 저장해서 오로지 숫자만 비교하는 작업을 해야한다.
비슷한 예시로 아래 코드가 있다.


```
public class Unbelievable{
	static Integer i;

    public static void main(String[] args){
    	if(i == 42)
        	System.out.println("Unbelievable");
    }
}
```

이 코드에선 Unbelievable을 출력하지 않고 `NullPointException`을 발생시킨다


i가 int가 아니라 `Integer`이기 때문이다


초기 객체 i의값은 null이다.

(i == 42)를 비교할때 자동으로 기본 자료형으로 변경되고 null과 비교하게 되므로 이러한 결과가 발생된다.



그렇다면 객체화된 자료형은 언제 사용해야할까?

1. collection의 요소, 키, 값으로 사용할 때다.
	- collection은 기본 자료형을 넣을 수 없어서 객체화 자료형을 사용해야한다.
2. 형인자 로는 기본자료형을 사용할 수 없다.


객체화된 기본 자료형과 기본본 자료형을 한 표현식에서 사용하면 비객체화가 자동으로 일어나며

그 과정에서 `NullPointerException`이 발생한다

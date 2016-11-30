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

chapter 6. Enum과 annotation
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

















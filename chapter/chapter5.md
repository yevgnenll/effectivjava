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





[뒤로가기](../README.md)

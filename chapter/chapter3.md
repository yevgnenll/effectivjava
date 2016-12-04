아직 chapter 3을 진행중이다.

어제까지는 equals, hashCode 를 살펴보았고 오늘부터는 toString을 살펴본다.
지금까지 내가 알았던 java는 진짜 아무것도 아니었구나 싶다... ㅠㅠ
오늘은 chapter 4까지 끝내고 총 복습을 해야할듯 싶다. 얼굴이 진짜 파래질때까지 달려야할듯 :)


-------------



### Rule No.10 toString은 항상 재정의하라


`java.lang.Object` 클래스가 `toString`을 제공하지만, 결과는 클래스이름@16진수 이다.

specification doc에서 toStrin은

>> 사람이 읽기 쉽도록 간략하지만 유용한 정보를 제공해야 한다

라고 되어있다.

python에서도 `def __str_()`를 재정의 하는것과 같다.


이전 posting에서 나온 Person 클래스에 toString 을 overriding 하지 않았다면 `System.out.println(person)`은 절대 사람이름이 나오지 않는다.


그 다음 이슈는 어떻게 보여줄것인지 문서에 명시해야 하느냐인데, 그 유무를 떠나 어떤 의도인지는 문서에 남겨야 한다.



--------------



### Rule No.11 clone을 재정의할 때는 신중하라


용어설명:

[clone이란?](https://en.wikipedia.org/wiki/Clone_(Java_method)): 객체를 복사할경우 **shallow copy**를 한다. 주소값만 복사한다는 것인데, clone을 하면 주소값 마저도 다르게 된다. (이것 때문에 알고리즘 문제 풀다 고생한적이...ㅜㅜ)


**접근제어자**

|종류|클래스|하위클래스|동일패키지|모든클래스|
|---|---|---|---|---|
|private| o | x | x | x |
|default| o | x | o | x |
|protected| o | o | o | x |
|public| o | o | o | o |


**super**

Developer 클래스가 Person 클래스를 상속받는다면 Developer 클래스에서 Person의 attribute를 부르고 싶을때 super.setName()과 같이 부르면 된다.

참고 - **super()**는 Developer 에서 Person의 생성자를 부를때 사용한다.


-----------

clone의 최초 설계목적은 어떤 객체가 복제를 허용한다는 사실을 알리려고 하는것이다. 하지만 그 목적에 부합하지 않는다고 한다.
Cloneable 인터페이스에는 clone()이 없으며, Object의 clone()은 protected로 선언되어 있다.


Cloneable을 implemnts 하면 Object의 clone은 해당 객체를 필드 단위로 복사한 객체를 return한다. implements를 하지 않는다면 `CloneNotSupportedException`을 던진다.(interface 사용의 안좋은 예)


```
x.clone() != x
``` 

위 조건은 true여야 한다.

```
x.clone().getClass() == x.getClass()
```

위 조건은 참이 되겠지만, 반드시 성립하는것은 아니다.


```
x.clone().equals(x)
```


위 조건도 참이 되겠지만, 반드시 성립하는것은 아니다.

객체를 복사하면 같은 클래스의 새로운 객체가 생성되는데 내부 자료 구조까지 복사해야할 수도 있으며, 어떤 생성자도 호출되지 않는다.


```

public class Developer extends Person {

  public Developer(String name, int age) {
    super(name, age);
  }

  @Override
  public Person clone(){
   try{
     return (Person) super.clone();
   } catch( CloneNotSupportedException e ){
     throw new AssertionError();
   }
  }
}
```


final 클래스가 아닌 곳에서 clone을 재정의 할대는 위의 코드처럼 `super.clone`을 사용해야한다. 모든 상위 클래스가 이 규칙을 따른다면 최종적으로 Object.clone을 불러오기 때문이다.


위 clone은 Object가 아닌 Person을 return한다. (java 1.5 이상) [convariant return type](https://blogs.oracle.com/sundararajan/entry/covariant_return_types_in_java)이 도입되었기 때문이다.


1.5 이전 버젼에서는 superclass method를 return할때 상위클래스 타입으로 return해야 했지만, 1.5부터는 변경되어 overring method의 반환값 자료형은 하위클래스가 되고 더 많은 정보를 제공할 수 있게 되었다.


실질적으로 returne 되었을때 마다 형변환을 하는것 보다 return에서 형변환을 해주면 코드가 줄어든다.


이 방법에 대해 저자는 아래와 같이 강조하였다.

>>라이브러리가 할 수 있는 일을 클라이언트에게 미루지 말라


Stack에서 clone을 살펴보면

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


위에 작성한 clone 방법 그대로 사용하면 클래스 내부의 arrays에서 문제가 발생한다.

size는 올바르게 복사가 되어도 elements가 원본의 elements를 참조하기 때문에 원본이 변경되면 clone된 객체도 변경이 되고, **복제본이 변경되면 원본도 변경** 되어 데이터에 문제가 발생된다.


```
x.clone() != x

x.clone().getClass() == x.getClass()

x.clone().equals(x)
```

이 조건들이 성립하지 않게 된다.


Stack 클래스의 생성자를 호출하면 이런 상황이 벌어지지 않는다.

clone은 또다른 형태의 **생성자(constructor)**이다. 따라서 원래 객체를 손상시키는 일이 없도록 해야하고, 복사본의 invariant(불변식)도 만족해야한다.

따라서 가장 정확한 방법은 elements의 배열에도 clone을 재귀호출해야한다.


```

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

```


위 코드 `result.elements = elements.clone();` 부분에 `(Objects[])`로 형변환이 필요하지 않다. 배열에 clone을 호출하면 반환되는 배열의 컴파일되면 원본과 동일하게 된다.


**주의** 위의 elements가 final로 선언이 되어있으면 동작하지 않는다. clone 안에서  **새로운 값을** 할당할 수 없기 때문이다.


하지만, clone을 재귀적호출만으론 부족할 수 있다. 아래 코드를 보면

```
/**
 * Created by yevgnen on 2016-11-23.
 */
public class HashTable  implements Cloneable{
  private Entry[] buckets = ....;
  
  private static class Entry{
    final Object key;
    Object value;
    Entry next;
    
    Entry(Object key, Object value, Entry next){
      this.key = key;
      this.value = value;
      this.next = next;
    }
  }
}

```


위의 코드를 Stack에서 햇던대로 재귀적으로 만든다면


```
  @Override
  public HashTable clone(){
   try{
     HashTable result = (HashTable) super.clone();
     result.buckets = buckets.clone();
     return result;
   } catch (CloneNotSupportedException e) {
     throw new AssertionError();
   }
 ```


복사본은 동일한 연결리스트를 참조하게 될 것이다.



```
public class HashTable  implements Cloneable{
  private Entry[] buckets = null;

  private static class Entry{
    final Object key;
    Object value;
    Entry next;

    Entry(Object key, Object value, Entry next){
      this.key = key;
      this.value = value;
      this.next = next;
    }

    Entry deepCopy(){
      return new Entry(key, value,
          next == null ? null : next.deepCopy());
    }
  }

  @Override
  public HashTable clone(){
   try{
     HashTable result = (HashTable) super.clone();
     result.buckets = new Entry[buckets.length];
     for(int i=0; i<buckets.length; i++)
       result.buckets[i] = buckets[i].deepCopy();
     return result;
   } catch (CloneNotSupportedException e) {
     throw new AssertionError();
   }
  }
}

```


Entry 클래스 내에 deepCopy를 구현하여 deep copy가 되도록 만들었고, 원래 bucket 배열을 돌면서 새로운 배열을 생성하도록 수정하였다.
그런데 이 방법은 배열의 길이가 길다면 재귀호출로 인해 overflow가 발생하기 때문에 순환문을 사용하는게 올바르다


```

    Entry deepCopy(){
      Entry result = new Entry(key, value, next);
      for(Entry p = result; p.next != null; p=p.next)
        p.next = new Entry(p.next.key, p.next.value, p.next.next);
      return result;
    }

```


1. Cloneable을 구현하는 모든 클래스는 return type이 자기 자신이어야 한다.
2. clone은 처음에 super.clone을 호출해야한다.
3. 복사본 내부를 call by reference로 참조할경우 재귀호출로 clone을 사용해야한다. 하지만 이 방법이 최선은 아닐 수 있다.
4. 클래스 내부 필드가 전부 기본자료형 이거나 변경 불가능 객체라면 필드를 수정할 필요는 없다.



>> 만일 Cloneable 인터페이스를 implements 하지 않는다면, 객체를 복사할 대안을 제공하거나, 아예 복제 기능을 제공하지 않는것이 낫다.

Cloneable보다 copy factory, copy constrcutor를 제공하는게 더 좋은 방법일 수 있다.





--------------



### Rule No.12 Comparable 구현을 고려하라


Objects의 equals 와 특성은 비슷하지만, 동치 검사외에 순서 비교가 가능하고 더 일반적이다.

Comparable interface를 구현하면 search, max/min 계산이 간단하고
정렬된 상태로 유지하기도 쉽다


**따라서, 알파펫 순서, 값의 크기, 시간순서 등 자연적 순서를 따르는 클래스를 구현할대는 Comparable interface 구현을 반드시 고려해야한다.**


```

public interface Comparable<T>{
    int compareTo(T t);
}

```

이 객체의 값이 주어진 객체보다 작으면 음수, 같으면 0, 크면 양수를 반환한다.


sgn(expression) -> expression이 음수이면 -1, 0이면 0, 양수이면 1

3가지 값을 return한다


##### compareTo 구현 조건

- 모든 x, y에 대해 `sgn(x.compareTo(y) == -sgn(y.compareTo(x))` -> true
- transitivity(추이성) 만족
    `(x.compareTo(y) > 0 && y.compareTo(z) > 0)` 이면 `x.compareTo(z)>0` 이다
- `x.compareTo(y) == 0` 이면 `sng(x.compareTo(z) == sng(y.compareTo(z))` 성립
- `(x.compareTo(y)==0) == (x.equals(y))` 강력한 권고사항


equals의 반사성, 대칭성, 추이성을 만족하도록 설계해야 한다.


compareTo 규약 준수하지 않는 클래스는 비교 연산에 기반한 클래스들을 오동작 시킬 수 있다.(TreeSet, TreeMap 와 같은 sorted collection), Array, Collections 같은 유틸리티 클래스, 탐생, 정렬 알고리즘을 포함하는 클래스


CompareTo method를 구현하는것은 equals 와 비슷하지만 차이가 있다.

Comparable interface가 자료형을 진자료 받는 generic interface이므로 compareTo method의 param은 컴파일 시간에 static으로 결정된다. 따라서 자료형을 검사하거나 casting을 할 필요가 없다.


잘못된 param을 보내면 compile 자체가 안되기 때문이다. 만약 null이 들어오면 NullPointException을 발생시킨다.


비교할 필드가 Comparable을 구현하지 않고 있거나 특이한 순서 관계를 사용해야 하는 경우에는 Comparator를 명시적으로 사용할 수 있다. 직접 작성도 가능하다.


정수형의 기본자료형 필드는 [relational operator](https://en.wikipedia.org/wiki/Relational_operator)를 사용해 비교한다

부동소수점 필드는 `Double.compare`나 `Float.compare`를 사용해 비교한다.


이유는 부동소수점 필드에 `<`, `>`와 같은 relational operator를 사용하면 일반 규약을 만족할 수 없다. 배열의 경우엔 이 지침을 원소마다 적용해야한다.


클래스에 선언된 필드 갯수가 많은경우 비교 순서가 중요한데, 가장 중요한 필드부터 우선순위를 정해 차례로 비교해야한다. 비교 결과가 `0`(same)이 아니면 비교를 중단하고 결과를 반환해야 한다.


[전화번호를 관리하는 class](https://gist.github.com/cf396b9b76770f5121ddcb7b60c52a6e)


위의 설명대로 compareTo를 만들 수 있다.

```

  public int compareTo(PhoneNumber pn){

    // local number
    if(areaCode < pn.areaCode)
      return -1;
    if(areaCode > pn.areaCode)
      return 1;
    // dialing
    if(prefix < pn.prefix)
      return -1;
    if(prefix > pn.prefix)
      return 1;
    // line number
    if(lineNumber < pn.lineNumber)
      return -1;
    if(lineNumber > pn.lineNumber)
      return 1;

    return 0;
  }

```


위에서 설명한 sgn(expression)과 같이 구현되었다. 하지만 `compareTo`는 양수, 음수 등 더 다양한 결과를 보여준다.


그런데 이 코드는 숫자가 음수일 경우 올바른 코드가 아니다.

정확히는 최대값, 최소값 차가 Integer.MAX_VALUE (2^31-1) 이하인 경우에만 사용할 수 있다.

overflow가 발생할 수 있기 때문이다.
이러한 에러가 발생하면 debugging이 어렵다 대부분의 코드에선 정상동작한다.


--------------


[뒤로가기](../README.md)

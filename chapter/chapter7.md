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

compile 시점에는 Collection 이기 때문에 Collection을 param으로 받는 가장 아래 method가 실행된다.

>overloading된 method는 static으로 선택되지만, overriding 된 method는 동적으로 선택된다.

**무슨소린지 모르겠으니 아래 예제 overriding과 비교하면**



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

(즉 overring은 runtime 시점에 선택을 한다)


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

혼란을 주는 코드를 작성하는것은 좋지 않다.(API 라면 더욱!!)



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

`remove(E)`와 `removce(int)` 라는 오버로딩 method 두 개가 존재한다.

generic이 도입되면서 List 인터페이스에 `remove(E)` 대신 `removce(Object)`가 있었다.

Object와 int는 다른 자료형이기 때문에 문제될 것이 없었지만 E와 int가 **완전히 다르다**라고 할 순 없다.


즉, 자동 객체화, generic이 자바로 포함되었으니 overloading 할 때 주의해야 한다.

서로 형변환이 될 수 없는 자료형이 많다, 하지만 어떤 overloading method가 주어진 인자들을

처리하도록 선택될지 알 수 없다.





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

처음에 이렇게 한 것은 큰 실수였다. java 1.5 이전에 배열 내용을 **출력** 하기위해

```
System.out.println(Arrays.asList(myArray));
```

코드를 이렇게 만들었을 경우 [Ljava.lang.Integer;@343534] 와 같이 출력된다.

그런데 이 숙어는 object reference type에 대해서만 동작했고, primitive type 배열에 적용하면

컴파일 조차도 되지 않았다.


```
public static void main(String[] args){
	int[] digits = { 3, 1, 4, 1, 5, 9, 2, 6, 5, 4};
    System.out.println(Arrays.asList(digits));
}
```

이 코드를 컴파일 하려고하면 java 1.4에서 asList 부분에 에러가 났었다.


그러나 java 1.5부터 Array.asList를 varargs를 받는 method로 바꾼다는 결정을 내렸기 때문에

오류없이 compile이 가능하고, 출력되는 결과값도 예측한대로 나타난다.


개선(?) 된 `Arrays.asList` 함수는 **객체 참조**를 모아 **배열**로 만드는데,

int 배열 digits에 대한 참조가 담긴 길이 1짜리 배열, 즉 배열의 배열이 만들어진다.

`List<int[]>` 이 리스트에 `toString`을 호출하면 다시 int 배열의 toString이 호출되어

원하는 문자열이 만들어진다.

```
System.out.println(Arrays.toString(myArray));
```




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


[뒤로가기](../README.md)

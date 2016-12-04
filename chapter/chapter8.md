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



----------------

### Rule No.50 다른 자료형이 적절하다면 문자열은 피하라

목적: 문자열로 하면 **안되는** 일들

1. 문자열은 value type을 대신하기엔 부족하다
	- 숫자라면 int, float, BigInteger 같은 수 자료형으로 변환해야한다.
	- yes on no로 답변이 가능하면 boolean을 사용한다.
	- 적절한 자료형이 있다면 해당 자료형을 사용하자
	- 적당한 자료형이 없다면 새로 만들어야 한다.
2. 문자열은 enum을 대신하기엔 부족하다
	- `enum`은 문자열 보다 훨씬 좋은 열거 할 수 있는 자료형이다.
3. 문자열은 혼합 자료형을 대신하기엔 부족하다
	- 다양한 컴포턴트를 문자열로 표시하는건 좋은 생각이 아니다
	- `String compondKey = className + "#" + i.next();` nonono
		- parsing이 필요하다
		- 느리다
		- 오류 발생 가능성이 굉장히 높다
4. 문자열은 capability(권한)을 표현하기에 부족하다

4번의 설명

(java 1.2를 기준으로) thread의 지역변수를 식별하는데 string을 사용했다.

```
// 문자열을 권한으로 사용하는 잘못되 예제
public class ThreadLocal{
    private ThreadLocal(){}

    // 주어진 이름이 가리키는 thread 지연 변수 값 설정
    public static void set(String key, Object value);

    // 주어진 이름이 가리키는 thread 지연 변수의 값
    public static Object get(String key);
}
```

문자열이 thread 지역변수의 전역적인 이름공간 이라는 것이다(?)

위 코드가 성립하려면

1. 문자열 키의 유일성이 보장되어야 한다.
2. 두 클라이언트가 같은 지역변수명을 사용한다면 동일한 변수를 공유하여 오류를 낸다
3. 악의적인 클라이언트가 의도적으로 같은 문자열을 사용하면 데이터 접근이 가능하다

```
public class ThreadLocal {
  private ThreadLocal(){}

  public static class Key{
    Key(){}
  }
  public static Key getKey(){
    return new Key();
  }
  public static void set(Key key, Object value){}
  public static Object get(Key key){ return null; }
}
```

키는 더 이상 thread 지역 변수의 키가 아니라, 자체 thread의 지역 변수가 된다.



[뒤로가기](../README.md)

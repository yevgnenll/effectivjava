## chapter 4. 클래스와 인터페이스


더 안정적이고 유연한 class, interface를 설계해보자


### Rule No.13 클래스와 멤버 접근권한은 최소화


잘 설계된 모듈과 그렇지 않은 모듈의 차이는 모듈 내부의 구현 세부내용을 얼마나 잘 **감추느냐**의 여부이다. (information hiding - 은닉화, encapsulation - 캡슐화)


##### 왜 은닉화, 캡슐화를 해야할까?


1. 은닉화, 캡슐화가 중요한 이유는 모듈 사이의 의존성을 낮춰서(decouple) 개별적으로 **개발하고**, **test하며**, **optimize**하고, **이해하며**, **수정**할 수 있도록 한다는데 기초한다.
    (이렇게 생각해야하는구나)
2. 유지보수의 부담도 낮아지고, 다른 모듈에 끼칠 영향도 줄일 수 있다.
3. 어떤 모듈이 문제를 일으키는지 확인하기 용이하다.
4. 소프트웨어 재사용성을 높인다.
5. 다른 소프트웨어 개발에도 유용하게 스인다.
6. 대규모 시스템 개발 과정의 위험성도 낮춘다.


**정보 은닉이 높은 성능을 보장하는것은 아니지만, 성능 튜닝을 가능하게 하는것은 사실이다**


이러한 정보 은닉을 위해 접근제어자를 제공한다.


**각 클래스와 멤버는 가능한 한 접근 불가능하도록 만들것**


최상의 레벨(중첩되지 않은) class, interface에 public을 붙이면 전역적 개체
그렇지 않으면 해당 패키지 않에서만 유효한 개체가 된다.

그래서 가능한 package-private으로 선언해야 하고, 그 결과로 다음번 릴리즈에 다른 코드에 영향을 줄 걱정없이 자유롭게 리팩토링이 가능하다


public으로 선언한다면 호환성 보장을 위해 지속적으로 지원해야 한다.


package-private의 접근권한을 낮추는것 보다 public class의 접근 권한을 낮추는것이 중요하다. public class는 해당 패키지 API 일부이기 때문이다.


**접근제어자**

|종류|클래스|하위클래스|동일패키지|모든클래스|
|---|---|---|---|---|
|private| o | x | x | x |
|package-private(default)| o | x | o | x |
|protected| o | o | o | x |
|public| o | o | o | o |


클래스를 주의깊게 설계한 뒤에는 반사적으로 private으로 설계할것이다. 

그리고 변경을 자주할 경우 지속적으로 package간 의존성을 끊을 방법을 고민해야한다.

protected는 하위 클래스에서 접근이 가능하기 때문에 실질적으로 공개 API이며 영원히 유지해야하는 속성이다. 또한, 공개된 클래스의 protected 멤버는 해당 클래스의 공개적 약속과도 같다.


하지만 **method의 접근권한을 줄일 수 없는 경우가 하나 있다** 상위 클래스를 overring 할대는 원래 method 접근 권한보다 낮은 권한을 설정할 수 없다.


상위 클래스 객체를 사용할 수 있는 곳에는 하위 클래스 객체도 사용 할 수 있어야 하기 때문이다. -> 이 규칙이 지켜지지 않으면 오류가 발생한다.


**객체 field는 절대로 public으로 선언하면 안된다**


비-final 필드나 변경 가능한 객체에 대한 final 참조 필드를 public으로 선언하면 피드에 저장될 값을 제한할 수 없게 된다. 값이 **변경되었을때 특정한 동작이 실행되도록 할수도 없으므로**, 변경 가능 public field를 가진 클래스는 **다중 thread에 안전하지 않다.** 


예외는 어떤 상수들이 클래스로 추상화된 결과물의 **핵심**적 부분을 구성한다고 판단되는 경우, 해당 상수를 public static final로 선언할 수 있다. 이런경우 관습적으로 대문자 + _ + 대문자 로 표현한다.


public static final 배열 필드를 두거나, 배열 필드를 반환하는 접근자를 정의하면 안된다.


이미 public static final 배열이 있거나, 배열 필드를 반환하는 접근자를 정의했다면

1. public -> private으로 바꾸고 변경이 불가능한 public list를 하나 만드는것이다
    ```

private static final Thing[] PRIVATE_VALUES = {....};
public static final List<Thing} VALUE =
 Collections.unmodifiableList(Arrays.asList(PRIVATE_VALUES ));

    ```
2. 배열은 private으로 선언하고 해당 배열을 복사해 반환하는 public method를 추가한다
    ```

private static final Thing[] PRIVATE_VALUES = {....};
public static final Thing[] values(){
    return PRIVATE_VALUES.clone();
}
    ```


두가지 중 하나를 선택해야할땐 client가 어떤 작업을 하길 원하는지를 고려한다.



### Rule No.14 public 클래스안에는 public 필드를 두지 말것

>> public class 안에는 public field를 두지 말고 접근자 method를 사용하라


```

class Point {
  public double x;
  public double y;
}

```


이런 클래스는 직접 x, y값을 변경할 수 있다.
이런 클래스를 private 필드와 public 접근자 method로 바꿔야한다.


```
class Point {
  private double x;
  private double y;

  public Point(double x, double y){
    this.x = x;
    this.y = y;
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }
}

```


선언된 패키지 밖에서도 사용 가능한 클래스에는 접근자 메서드를 제공하라.

public class의 내부 표현을 공개하면 그 내부 표현을 변경할 수 없게 된다.

변경하면 클라이언트 코드가 깨지게 된다.





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



[뒤로가기](../README.md)

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

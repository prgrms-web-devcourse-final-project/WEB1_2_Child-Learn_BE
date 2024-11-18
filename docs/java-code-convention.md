## JAVA 코드 규칙
### 목차
[파일 공통 요건](#파일-공통-요건)  
[공통 네이밍 규칙](#공통-네이밍-규칙)  
[선언](#선언)  
[들여쓰기](#들여쓰기)  
[중괄호](#중괄호)  
[줄바꿈](#줄바꿈)  
[공백](#공백)

<br>

### 파일 공통 요건
* 파일 인코딩은 UTF-8
  * 인텔리제이 Encoding 설정  
    Help -> Edit Custom VM Options을 클릭한 후, -Dfile.encoding=UTF-8 를 추가해준 후 저장
    ![스크린샷](https://github.com/user-attachments/assets/50085acc-3cea-479f-9d9b-5e3560271e00)
    ![스크린샷](https://github.com/user-attachments/assets/54c869c4-6bc9-4b0a-ad9a-8ebfcbe9a429)
  * 인텔리제이 파일 인코딩 설정  
    File -> Settings -> Editor -> File Encodings -> Global, Project Encoding, Properties Files 설정을 UTF-8로 변경
    ![스크린샷](https://github.com/user-attachments/assets/f1698254-97f8-4b96-a9fc-c1cc1ae20417)

<br>

### 공통 네이밍 규칙
* 대소문자가 구분되며 길이에 제한이 없다.
* [예약어](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/_keywords.html)를 사용해서는 안 된다.
* 숫자로 시작해서는 안 된다.
* 파스칼 표기법 (PascalCase)과 카멜 표기법(camelCase)를 사용한다.
  * PascalCase : 모든 단어에서 첫 번째 문자는 대문자이며 나머지는 소문자이다.
  * camelCase : 최초에 사용된 단어를 제외한 첫 번째 문자가 대문자이며 나머지는 소문자이다.
* 반의어는 반드시 대응하는 개념으로 사용해야 한다.
* 식별자에는 영문/숫자/언더스코어만 허용한다.
  * 변수명, 클래스명, 메서드명 등에는 영어와 숫자만을 사용한다. 
  * 상수에는 단어 사이의 구분을 위하여 언더스코어(_)를 사용한다. 
* 식별자의 이름을 한국어 발음대로 영어로 옮겨 표기하지 않는다.
  * 나쁜 예 : moohyungJasan (무형자산)
  * 좋은 예 : intangibleAssets (무형자산)
* 패키지 이름은 소문자로 구성한다.
  * 단어별 구문을 위해 언더스코어(_)나 대문자를 섞지 않는다.
  ```java
  // Bad
  package com.navercorp.apiGateway
  package com.navercorp.api_gateway

  // Good
  package com.navercorp.apigateway
  ```
* 클래스/인터페이스 이름에 대문자 카멜표기법 적용한다.
  * 클래스 이름은 단어의 첫 글자를 대문자로 시작하는 대문자 카멜표기법(Upper camel case)을 사용한다.
  ```java
  // Bad
  public class reservation
  public class Accesstoken
  
  // Good
  public class Reservation
  public class AccessToken
  ```
* 클래스 이름과 에 인터페이스 이름에 명사를 사용한다.
  * 클래스 이름은 동사가 아닌 명사나 명사절로 짖는다.
  * 인터페이스(interface)의 이름은 클래스 이름은 명사/명사절로 혹은 형용사/형용사절로 짓는다.
* 테스트 클래스는 "TEST"로 끝난다.
  * JUnit 등으로 작성한 테스트 코드를 담은 클래스의 이름 마지막에 "Test"을 붙인다
* 메서드 이름과 변수에는 소문자 카멜표기법을 적용한다.
  * 첫 번째 단어를 소문자로 작성하고, 이어지는 단어의 첫 글자를 대문자로 작성하는 소문자 카멜표기법(Lower camel case)를 사용한다. 
  * 테스트 클래스의 메서드 이름에서는 언더스코어를 허용한다.
  ```
  // 메서드
  getSize();
  printBridgeResult();
   
  // 변수
  private boolean authorized;
  private int accessToken;
  ```
* 메서드의 이름은 동사/전치사로 시작한다.
  * 메서드명은 기본적으로는 동사로 시작한다.
  * 다른 타입으로 전환하는 메서드나 빌더 패턴을 구현한 클래스의 메서드에는 전치사를 쓸 수 있다.
  ```java
  - 동사 사용 : renderHtml()
  - 전환 메서드의 전치사 : toString()
  - Builder 패턴 적용한 클래스의 메서드의 전치사 : withUserId(String id)
  ```
* 상수는 대문자와 언더스코어로 구성한다.
  * "static final"로 선언되어 있는 필드일 때를 상수로 간주한다.
  * 상수 이름은 대문자로 작성하며, 복합어는 언더스코어 ' _ ' 를 사용하여 단어를 구분한다.
  ```
  public final int LOTTO_MIN_RANGE = 1;
  public final int LOTTO_MAX_RANGE = 45;
  public final String BUY_LOTTO_INPUT = “구입할 로또 개수를 입력해주세요”;
  ```

<br>

### 선언
* 한 줄에 여러 문장을 쓰지 않고, 하나의 선언문에는 하나의 변수만을 다룬다.
  * 문장이 끝나는 ; 뒤에는 새줄을 삽입한다.
  * 변수 선언문은 한 문장에서 하나의 변수만을 다룬다.
  ```
  // Bad
  int base, weight;
   
  // Good
  int base;
  int weight;
   
  // Bad
  int base = 0; int weight = 2;
   
  // Good
  int base = 0;
  int weight = 2;
  ```

<br>

### 들여쓰기
* 4개의 빈 칸(space)를 들여쓰기 단위로 사용한다.
  * 1개의 탭의 크기는 스페이스 4개와 같도록 에디터에서 설정한다.
  * File - Settings - Editor - Code Style - HTML
  ![스크린샷](https://github.com/user-attachments/assets/b421c92a-c4fd-4ef1-a431-4fe2075b6155)

<br>

### 중괄호
* 중괄호 선언은 K&R 스타일(Kernighan and Ritchie style)을 따른다.
  * 줄의 마지막에서 시작 중괄호`{`를 쓰고 열고 새줄을 삽입한다. 블럭을 마친후에는 새줄 삽입 후 중괄호를 닫는다.
  ```java
  public class SearchConditionParser {
      public boolean isValidExpression(String exp) {
   
          if (exp == null) {
              return false;
          }
   
          return true;
      }
  }
  ```
* 닫는 중괄호와 같은 줄에 else, catch, finally, while을 선언한다.
  ```java
  if (line.startWith(WARNING_PREFIX)) {
      return LogPattern.WARN;
  } else if (line.startWith(DANGER_PREFIX)) {
      return LogPattern.NORMAL;
  } else {
      return LogPattern.NORMAL;
  }
  ```

<br>

### 줄바꿈
* 줄바꿈 후 추가 들여쓰기
  * 줄바꿈 이후 이어지는 줄에서는 최초 시작한 줄에서보다 적어도 1단계의 들여쓰기를 더 추가한다.
  * IDE의 자동정렬 기능 ( 인텔리제이의 경우 ctrl + alt + l )을 활용한다.
  ```
  AbstractAggregateRootTest.AggregateRoot proxyAggregateRoot =
          em.getReference(AbstractAggregateRootTest.AggregateRoot.class, aggregateRoot.getId());
  ```
* package 선언 후 빈 줄을 삽입한다.
  ```java
  package baseball;
   
  import java.util.List;
  ```
* 메서드의 선언이 끝난 후 다음 메서드 선언이 시작되기 전에 빈줄을 삽입한다.
  ```
  public void setId(int id) {
      this.id = id;
  }
   
  public void setName(String name) {
      this.name = name;
  }
  ```

<br>

### 공백
* 대괄호 뒤에 공백 삽입
  * 닫는 대괄호(]) 뒤에 `;`으로 문장이 끝나지 않고 다른 선언이 올 경우 공백을 삽입한다.
  ```java
  // Bad
  int[]masks = new int[]{0, 1, 1};
  
  // Good
  int[] masks = new int[] {0, 1, 1};
  ```
* 중괄호 시작 전과 종료 후에 공백 삽입
  * 여는 중괄호({) 앞에는 공백을 삽입한다. 
  * 닫는 중괄호(}) 뒤에 else ,catch 등의 키워드가 있을 경우 중괄호와 키워드 사이에 공백을 삽입한다.
  ```java
  public void printWarnMessage(String line) {
    if (line.startsWith(WARN_PREFIX)) {
      ...
    } else {
      ...
    }
  }
  ```
* 식별자와 여는 소괄호 사이에는 공백을 삽입하지 않는다.
  * 식별자와 여는 소괄호(() 사이에는 공백을 삽입하지 않는다. 
  * 생성자와 메서드의 선언, 호출, 애너테이션 선언 뒤에 쓰이는 소괄호가 그에 해당한다.
  ```java
  public StringProcessor() {} // 생성자
  
  @Cached("local")
  public String removeEndingDot(String original) {    
    assertNotNull(original);
  }
  ```
* 콤마/구분자 세미콜론의 뒤에 공백을 삽입한다.
  * 콤마(,)와 반복문(while, for)의 구분자로 쓰이는 세미콜론(;)에는 뒤에만 공백을 삽입한다.
  ```
  // Bad
  for (int i = 0;i < length;i++) {
    display(level,message,i)
  }
  // Good
  for (int i = 0; i < length; i++) {
    display(level, message, i)
  }
  ```
* 주석문 기호 전후의 공백 삽입
  * 주석의 전후에는 아래와 같이 공백을 삽입한다
  ```
  /* 
  * 공백 후 주석내용 시작
  */
  
  System.out.print(true); // 주석 기호 앞 뒤로 공백
  
  /* 주석내용 앞에 공백, 뒤에도 공백 */
  ```

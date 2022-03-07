# 예약 관리 시스템

### 사용한 기술
* html, jquery, thymeleaf, bootstrap, npm 패키지관리
* java 8, springboot 2.6.4, mysql, jpa, maven
* spring security, modelmapper, querydsl, junit, lombok, spring scheduler

### 구조 설계
* infra package
  * third party extension과 configuration을 관리
* modules : 각 모듈별로 관리
* module
  * dto, validator, entity, service, controller
  * 모두 하나의 모듈에서 관리

### 테이블 설계
* Account : 유저 정보, 이메일 인증 정보 
* Facility : 시설 정보, accounts, reservations 연관관계 정보 
* Reservation : 예약 정보, account, facility 연관관계 정보
* Account <- Facility (단방향)[nTom]
* Account <- Reservation (단방향)[1tom]
* Facility <-> Reservation (양방향)[1tom]

### 로그인, 로그아웃 구현
* spring security를 활용
  * SecurityConfig 설정
* 토큰활용
* 비밀번호 인코딩
* 이메일 인증 
  * smtp
  * local, test : 콘솔 메일
  * dev : 실제 이메일 보내기
* @CurrentAccount : user principal 표현
* 유저 생성, 수정, 삭제

### 시설 관리 
* 관리자만 시설 생성, 수정, 삭제할 수 있음
* 유저는 확인만 가능.
* 시설 검색, 정렬은 둘 다 가능

### 예약 관리
* 관리자는 예약할 수 없음
* 유저는 예약 및 취소 가능
* 유저는 날짜를 선택하여 예약할 수 있음
* 예약 업데이트 동기화
* 예약관리창에서 시설 검색, 정렬 가능

### 스케줄러
* 매일 자정에 "오늘 예약 수" 업데이트
 
### 공통
* 날짜 포맷 처리는 globalService
* service에서 exception과 비즈니스로직을 최대한 녹임
* n+1 문제발생하지 않고, 가독성 좋고, 성능 향상을 위해 querydsl 사용
* test 코드 작성
* url keyword를 상수로 한 클래스에서 사용
* json 응답에 CustomResponse를 만들어 응답
* local, test, dev 환경 구성.


### references
* https://www.w3schools.com/TAgs/default.asp
* https://api.jquery.com/
* https://www.thymeleaf.org/
* https://getbootstrap.com/
* https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#reference
* https://docs.spring.io/spring-security/reference/
* http://modelmapper.org/user-manual/property-mapping/
* http://querydsl.com/static/querydsl/5.0.0/reference/html_single/
* https://junit.org/junit5/docs/current/user-guide/
* https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/scheduling.html
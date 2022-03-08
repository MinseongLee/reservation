# 예약 관리 시스템

### 사용한 기술
* html, jquery, thymeleaf, bootstrap, npm 패키지관리
* java 8, springboot 2.6.4, mysql 8.x, spring data jpa, maven
* spring security, modelmapper, querydsl, junit, lombok, spring scheduler

### 실행 방법
1. maven compile, 새로고침 등
2. Edit configurations 클릭(오른쪽 위에 실행버튼 옆에 클릭)
3. \+ 버튼 클릭 후 springboot 클릭
4. name : application.yml
- configuration / main class : com.youwent.ReservationApplication
- 나머지는 디폴트 설정 사용
5. name : application-dev.yml
- configuration / main class : com.youwent.ReservationApplication
- configuration / active profile : dev
이렇게 총 두 개 파일 만들어 로컬일 때와 dev일 때의 환경을 분리
6. create database
7. CREATE DATABASE `reservation` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
8. CREATE DATABASE `reservationtest` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
- test 디비와 local 디비를 분리
9. admin 유저 생성은 runner/UserRunner 에서 주석 풀고 생성가능.
- 단 현재 ddl이 update이므로, 한 번 생성 한 후 주석 처리. (아니면 create-drop으로 변경해서 사용)
10. mysql user : root, pwd : 1111 

### git 전략
* master, develop, feature, hotfix, release
* master : 실서버
* develop : 개발
* feature : 기능
* 현재 개발 단계므로, feature에서 개발 후 develop에 merge 후 관리.

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
- 예약관리(검색 등)는 reservation 모듈을 통해서 관리하고,
- 시설관리(검색 등)는 facility 모듈을 통해서 관리하도록 설계

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
* 일반 유저 생성, 수정, 삭제
* 어드민 타입은 UI에서 선택불가.
* UI에서 유저 생성 후, 어드민 타입(UserType.ADMIN) 변경 혹은 runner로 어드민 유저 생성 후 사용.

### 시설 관리 
* 관리자만 시설 생성, 수정, 삭제할 수 있음
* 유저는 확인만 가능.
* 시설 검색, 정렬은 둘 다 가능
* paging 처리

### 예약 관리
* 관리자는 예약할 수 없음
* 유저는 예약 및 취소 가능
* 유저는 날짜를 선택하여 예약할 수 있음
* 예약 업데이트 동기화
* 예약관리창에서 시설 검색, 정렬 가능
* paging 처리

### 스케줄러
* 매일 자정에 "오늘 예약 수" 업데이트
* AsyncConfig를 생성해 비동기 설정
* 시설과 예약을 한 번에 가져와 쿼리 한 번만 발생하게 처리
 
### 공통
* 날짜 포맷 처리는 globalService
* service에서 exception과 비즈니스로직을 최대한 녹임
* n+1 문제발생하지 않고, 가독성 좋고, 성능 향상을 위해 querydsl 사용
* test 코드 작성
* url keyword를 상수로 한 클래스에서 사용
* json 응답에 CustomResponse를 만들어 응답
* local, test, dev 환경 구성.

### 에러처리
* modules/common/CustomErrorController 를 생성하여 에러메세지 리턴
* 비동기 처리에서 에러 발생 시 알림 메세지(data.responseJSON.message)를 리턴


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
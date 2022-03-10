# 예약 관리 시스템

### 사용한 기술
* front-end : html, jquery, bootstrap
* front package manager : npm
* template engine : thymeleaf
* language : java 8
* Database : mysql 8.x
* project manager : maven
* framework : springboot 2.6.4
* dependencies : spring data jpa, spring security, modelmapper, querydsl, junit, lombok, spring scheduler

### API end point
1. 회원가입 양식
- url: GET /signup
- request: 
- response: account/signup.html 

2. 회원가입 요청
- url: POST /signup
- request:
  

    {
      "accountDto": {
        "name": "dexter",
        "email": "dexter@dexter.com",
        "password": "dexter1234",
        "phone": "01044445555"
      }
    }
- response: index.html

3. 회원가입완료(이메일 토큰 검증)
- url: GET /emailtoken
- request:

      {
        "token": "0e20ba50-7548-4b90-a359-0b7c965e48d0",
        "email": "dexlee@dexter.com"
      }
- response: account/checkedemail.html

4. 이메일 검증 페이지
- url: GET /checkemail
- request: @CurrentAccount
- response: account/checkemail.html

5. 재전송 이메일
- url: GET /resendemail
- request: @CurrentAccount
- response: account/checkemail.html

6. 유저 상세
- url: GET /profile/id
- request: @CurrentAccount
  
      { "id": 1 }
- response: account/profile.html 

7. 유저 수정 폼 
- url: GET /settings/profile
- request: @CurrentAccount
- response: settings/profile.html

8. 유저 수정 처리
- url: POST /settings/profile
- request: @CurrentAccount
  
      {  
        "profile": {
            "name" : "dexter",
            "phone": "01033334444"
        }
      }
- response: settings/profile.html

9. 패스워드 수정 폼
- url: GET /settings/password
- request: @CurrentAccount
- response: settings/password.html

10. 패스워드 수정 처리
- url: POST /settings/password
- request: @CurrentAccount
  
      { 
        "passwordForm": {
          "newPassword":"dexter1256",
          "confirmedNewPassword":"dexter1256"
        }
      }
- response: settings/password.html

11. 유저 삭제 폼
- url: GET /settings/account
- request: @CurrentAccount
- response: settings/account.html

12. 유저 삭제 처리
- url: DELETE /settings/account/id
- request: @CurrentAccount

      { 
        "id": 1 
      }
- response: 

      { 
        "statusCode": "200", 
        "resultMsg": "success"
      }

13. 시설 리스트
- url: GET /facility
- request: @CurrentAccount
  
      { 
        "keyword":"dex", 
        "orderByBuilding":"desc"
      }
- response: facility/index.html

14. 시설 생성 폼
- url: GET /facility/form
- request: @CurrentAccount
- response: facility/form.html

15. 시설 생성 처리
- url: POST /facility/form
- request: @CurrentAccount
  
      {
        "facilityForm": {
          "building":"dexbuilding",
          "address":"서울시동작구",
          "openTime":"9:30",
          "closeTime":"21:30",
          "maxReserveCnt":15
        }
      }
- response: facility/index.html

16. 시설 상세
- url: GET /facility/id
- request: @CurrentAccount
  
      { 
        "id": 2
      }
- response: facility/view.html

17. 시설 수정 폼
- url: GET /facility/id/settings/facility
- request: @CurrentAccount
  
      {
        "id": 3
      }
- response: facility/settings/facility.html

17. 시설 수정 처리
- url: POST /facility/id/settings/facility
- request: @CurrentAccount
  
      {
        "id": 3, 
        "facilityForm": {
          "building": "dexJin",
          "address": "서울시관악구",
          "openTime": "10:30",
          "closeTime": "22:20",
          "maxReserveCnt": 15
        }
      }
- response: facility/settings/facility.html

18. 시설 삭제 폼
- url: GET /facility/id/settings/delete
- request: @CurrentAccount

      {
        "id": 3
      }
- response: facility/settings/delete

19. 시설 삭제 처리
- url: DELETE /facility/id/settings/delete
- request:

      {
        "id": 3
      }
- response:

      {
        "statusCode": "200",
        "resultMsg": "success"
      }
20. 시설 예약 처리
- url: GET /facility/id/settings/reservation
- request: @CurrentAccount

      {
        "id": 3,
        "reservationDate": "2022-03-09"
      }
- response:

      {
        "statusCode": "200",
        "resultMsg": "success",
        "dto": { 
          "nowReserveCnt": 5,
          "possibleReservation": true
        }
      }
21. 예약 리스트
- url: /reservation
- request: @CurrentAccount
  
      {
        "keyword": "dex",
        "orderByBuilding": "asc"
      }
- response: reservation/index.html

22. 예약 취소
- url: /reservation/id
- request:
  
      { 
        "id": 2
      }
- response: 


      {
        "statusCode": "200",
        "resultMsg": "success"
      }

### 실행 방법(intelliJ 기준)
0. maven compile
1. Edit configurations 클릭(오른쪽 위에 실행버튼 옆에 클릭)
2. \+ 버튼 클릭 후 springboot 클릭
3. name : application.yml
- configuration / main class : com.youwent.ReservationApplication
- 나머지는 디폴트 설정 사용
4. name : application-dev.yml
- configuration / main class : com.youwent.ReservationApplication
- configuration / active profile : dev
이렇게 총 두 개 파일 만들어 로컬일 때와 dev일 때의 환경을 분리
5. create database
6. CREATE DATABASE `reservation` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
7. CREATE DATABASE `reservationtest` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
- test 디비와 local 디비를 분리
8. mysql user : root, pwd : 1111 (변경해서 사용가능)
9. application.yml : - ddl: create-drop
10. runner/UserRunner
- 테스트 유저, 시설, 예약 자동생성
11. test admin
- id : dexlee@dexter.com
- pwd : dexter1234
12. test user
- id : dexter@dexter.com
- pwd : dexter1234

### git flow
* master, develop, feature, hotfix, release
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
* UI에서 유저 생성 후, 어드민 타입(UserType.ADMIN) 변경 혹은 sql로 어드민 유저 생성 후 사용.

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
# OAuth 2.0 마스터
[book](http://www.yes24.com/24/Goods/38100766?Acode=101)

## Chapter 01
인증/인가
인증(authentication): 사용자 판단
인가(authorization): 권한 판단

연합된 신원(frederated identity)
권한 위임(delegated authority)

## OAuth 2.0 개요
### 그랜트 유형
정보 교환 흐름
- 인가 코드 그랜트(authorization code grant)
    - 서버 사이드 워크플로우
- 암사직 그랜트(implicit grant)
    - 클라이언트 사이드 워크플로우
- 리소스 소유자 비밀번호 자격증면 그랜트(resource owner password credentials grant)
- 클라이언트 자격증명 그랜트(client credentials grant)
  
스펙에서는 4가지를 지원하지만(별도의 그랜트 정의 가능) 주로 인가와 암시적이 가장 많이 사용

### OAuth 2.0 Client
client 신뢰 판단 기준은 안전하게 정보 저장, 전달이다.
- 신뢰 클라이언트(trusted client)
- 비신뢰 클라이언트(untrusted client)

Oauth 스팩에서 trusted와 untrusted에 해당하는 것은 confidential과 public이다

### 액세스 토큰과 bearer 토큰
- 액세스 토큰: 보호된 리소스에 접근할 수 있는 권한을 나타내는 문자열 토큰
- bearer 토큰: 액세스 토큰의 한 유형

## 네 개의 단계
OAuth 2.0 Client가 되기 위한 단계

### 클라이언트 등록
- 클라이언트 ID(client id)
    - 다른 클라이언트와 구분을 하기 위한 ID
- 클라이언트 시크릿(client secret)
    - 애플리케이션의 신원을 알려주기 위한 값
- 리다이렉션 앤드포인트(redirection endpoint)
    - 서비스 제공자가 응답을 전달하기 위해 사용하는 앤드포인트
- 인가 앤드포인트(authorization endpoint)
    - 클라이언트 애플리케이션이 인가 플로루를 시작할 때 사용하는 앤드포인트
- 토큰 앤드포인트(token endpoint)
    - 클라이언트 애플리케이션이 토큰 플로우를 시작할 때 사용하는 엔드포인트

클라이언트 자격증명(client credentials)은 클라이언트 ID와 클라이언트 시크릿을 사용

### 액세스 토큰 얻기
액세스 토큰은 범위와 접근 기간 이라는 속성을 가진다.
- 범위(scope)
- 접근 기간(duration of access)
    - 영구 토큰(perpetual token): 만료 시간을 가지지 않는 토큰

### 액세스 토큰을 이용하여 보호된 리소스에 접근

### 액세스 토큰 갱신


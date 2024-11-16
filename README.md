# Easy-JWT 라이브러리

**Easy-JWT**는 Spring Boot 애플리케이션에서 JWT(Json Web Token)를 쉽게 통합하고 관리할 수 있도록 도와주는 라이브러리입니다.

[easy-jwt](https://github.com/geunoo/easy-jwt) 입맛대로 변경 버전입니다.

## 특징

- **간편한 통합**: 최소한의 설정으로 JWT 인증 기능을 추가할 수 있습니다.
- **확장성**: 사용자 정의 서비스와 구성으로 확장 가능합니다.
- **Spring Security 통합**: Spring Security와 원활하게 연동됩니다.

## 설치

다음 의존성을 추가하세요:

```gradle
dependencies {
    implementation 'com.github.Alfex4936:easy-jwt:1.0.2'
}
```

or

```pom
<dependency>
    <groupId>com.github.Alfex4936</groupId>
    <artifactId>easy-jwt</artifactId>
    <version>1.0.2</version>
</dependency>
```

## 구성

### 1. 애플리케이션 프로퍼티 설정

`application.yml` 또는 `application.properties`에 다음 설정을 추가하세요:

```yaml
easy-jwt:
  enabled: true
  secret: your-secret-key-here
  access-token-expiration: 600 # 액세스 토큰 만료 시간(초)
  refresh-token-expiration: 2592000 # 리프레시 토큰 만료 시간(초)
  token-prefix: "Bearer "
  header-string: "Authorization"
```

**주의:** `secret`은 최소 32자 이상의 안전한 값으로 설정해야 합니다.

### 2. `QueryJwtUserService` 구현

사용자 정보를 조회하기 위한 `QueryJwtUserService` 인터페이스를 구현해야 합니다.

```java
@Service
public class CustomQueryJwtUserService implements QueryJwtUserService {

    @Override
    public Optional<JwtUser> execute(String username) {
        // 데이터베이스 또는 다른 저장소에서 사용자 정보를 조회하여 반환합니다.
        return userRepository.findByUsername(username)
                .map(user -> new CustomJwtUser(user));
    }
}
```

### 3. `JwtUser` 구현

사용자 정보를 나타내는 `JwtUser` 인터페이스를 구현해야 합니다.

```java
public class CustomJwtUser implements JwtUser {

    private final User user;

    public CustomJwtUser(User user) {
        this.user = user;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles();
    }
}
```

### 4. Spring Security 설정

`WebSecurityConfigurerAdapter`를 확장하여 JWT 필터를 보안 필터 체인에 추가합니다.

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // 기타 보안 설정
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
```

### 5. 예외 처리

JWT 인증 중 발생하는 예외를 처리하기 위해 `@ControllerAdvice`를 사용하여 글로벌 예외 처리를 구성합니다.

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<String> handleInvalidTokenException(InvalidTokenException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
    }

    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<String> handleExpiredTokenException(ExpiredTokenException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 만료되었습니다.");
    }

    // 기타 예외 처리
}
```

## 사용 방법

### 토큰 생성

```java
@Autowired
private JwtTokenProvider jwtTokenProvider;

public String createAccessToken(String username) {
    Map<String, Object> claims = new HashMap<>();
    // 추가 클레임 설정
    return jwtTokenProvider.generateAccessToken(username, claims);
}
```

### 현재 사용자 정보 가져오기

```java
@Autowired
private CurrentUserService<CustomJwtUser> currentUserService;

public void someMethod() {
    CustomJwtUser currentUser = currentUserService.getCurrentUser();
    // 현재 사용자 정보 사용
}
```

package com.sparta.sparta_board.jwt;

import com.sparta.sparta_board.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    // Header Key 값
    // Authorization 과 Bearer 부분의 Key 값
    public static final String AUTHORIZATION_HEADER = "Authorization";

    // 사용자 권한 값의 KEY
    // 사용자 권한도 Token 안에 값을 넣어 줄 건데 그거를 가지고 올 때 사용되는 Key 값이다.
    public static final String AUTHORIZATION_KEY = "auth";

    // Token 식별자
    // Token 을 만들때 같이 옆에 붙어서 들어가는 부분
    private static final String BEARER_PREFIX = "Bearer ";

    // 토큰 만료시간
    // 밀리세컨드 기준이라 1시간 60 * (60 * 1000L = 1분) 즉 60 * 1분이니까 60분, 1시간
    private static final long TOKEN_TIME = 60 * 60 * 1000L;

    // Value 어노테이션을 사용하면 우리가 application.properties 에 있는 키 값을 가지고 온다.
    @Value("${jwt.secret.key}")

    // secretKey는 사용할 때 application.properties 에 있는 키 값을 가지고 온다.
    private String secretKey;

    // 우리가 Token을 만들 때 넣어 줄 Key 값이다.
    private Key key;

    // Enum 타입, 여러가지 암호화 알고리즘들이 있다. 그 중에서 HS256을 사용한다.
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // @PostConstruct 어노테이션은 처음에 이 객체가 생성될 때 초기화하는 함수이다.
    @PostConstruct
    private void init() {
        // base64로 인코딩이 되어 있기 때문에 secretKey 값을 가지고와서 한번 decode를 하는 과정이다.
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        // 반환 값이 byte 라서 byte 배열로 받은 다음에 Key 객체에 넣을건데 Key 객체에 넣을 때는
        // hmacShakeyFor(bytes) 메서드를 사용해서 메서드 인자로 만들어진 byte 값을 넣어주면 만들어진다.
        key = Keys.hmacShaKeyFor(bytes);
    }
    // header 토큰을 가져오기
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 생성 (JWT를 만드는 메소드)
    public String createToken(String username, UserRoleEnum role) {
        Date date =new Date();

        return BEARER_PREFIX + // ( Token에 BEARER이 붙었으므로 BEARER을 앞에 붙여놓음)
                // 토큰을 만드는 메소드
                Jwts.builder()
                        // 공간 , 공간 부분에 username을 넣어준다.
                        .setSubject(username)

                        // 사용자의 권한을 넣어준다. 그 권한을 가져 올 때는 지정해놓은 OAuth Key를 사용해서 넣어준다.
                        // "auth" : USER , "AUTH" : ADMIN
                        .claim(AUTHORIZATION_KEY, role)

                        // 이 토큰을 언제까지 유효하게 가져갈 건지 지정해주는 부분
                        // date는 위에 date를 가져온거고 .getTime()을 이용해서 현재 시간을 가지고오고
                        // 거기에 지정해놓은 26번줄에 시간을 같이 더해서 지금 기준으로 언제까지 가능한지를 만들어주는 부분
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))

                        // 이 토큰이 언제 만들어졌는지 넣어 주는 부분
                        .setIssuedAt(date)

                        // 실제로 Secret Key 를 사용해서 만든
                        // Key 객체와 그 Key 객체를 어떤 알고리즘을 사용해서 암호화 할 것인지 지정해주는 부분
                        .signWith(key,signatureAlgorithm)

                        // compact(); 를 통해 string 형식의 JWT 토큰으로 반환이 된다.
                        .compact();
    }
        // 토큰 검증
        public boolean vaildateToken(String token) {
        try {
            // 아까 사용했던 그 Jwts에 들어있는 parserBuilder를 통해서 검증을 한다.
            // serSigningKey() 에 Token 을 만들 때 사용한 키를 넣어주고
            // 어떠한 토큰에 검증을 할 건지를 parseClaimsJws() 부분에 Token 을 넣어주면 내부적으로 토큰을 검증해준다.
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
            // 서명이 유효한지, 만료가 됐는지 등에 대해서 자동으로 try/catch 문으로 잡아줄 수 있다.
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰입니다.");
        }
            return false;
    }

        // 토큰에서 사용자 정보 가져오기
        // 위에 검증과 같으나 getBody()를 사용해서 그 안에 들어있는 정보들을 기지고 올 수 있다.
        // 사용자 정보를 가져오는 것은 이미 vaildateToken() 으로 검증을 했기 때문에 이거는 유효한 토큰
        // 이라고 가정을 해서 여기는 try/catch가 없다.
        public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        }


}

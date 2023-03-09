package com.b4t.app.security.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

import com.b4t.app.security.CustomGrantedAuthority;
import com.google.common.base.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.github.jhipster.config.JHipsterProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenProvider {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    private Key key;

    private long tokenValidityInMilliseconds;

    private long tokenValidityInMillisecondsForRememberMe;

    private final JHipsterProperties jHipsterProperties;
    private long tokenValidityInMillisecondsForLGSP = 86400 * 365 * 3;

    public TokenProvider(JHipsterProperties jHipsterProperties) {
        this.jHipsterProperties = jHipsterProperties;
    }

    @PostConstruct
    public void init() {
        byte[] keyBytes;
        String secret = jHipsterProperties.getSecurity().getAuthentication().getJwt().getSecret();
        if (!StringUtils.isEmpty(secret)) {
            log.warn("Warning: the JWT key used is not Base64-encoded. " +
                "We recommend using the `jhipster.security.authentication.jwt.base64-secret` key for optimum security.");
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        } else {
            log.debug("Using a Base64-encoded JWT secret key");
            keyBytes = Decoders.BASE64.decode(jHipsterProperties.getSecurity().getAuthentication().getJwt().getBase64Secret());
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.tokenValidityInMilliseconds =
            1000 * jHipsterProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds();
        this.tokenValidityInMillisecondsForRememberMe =
            1000 * jHipsterProperties.getSecurity().getAuthentication().getJwt()
                .getTokenValidityInSecondsForRememberMe();
    }

    public String createToken(Authentication authentication, boolean rememberMe) {
//        String authorities = authentication.getAuthorities().stream()
//            .map(GrantedAuthority::getAuthority)
//            .collect(Collectors.joining(","));
        String authorities = ((List<CustomGrantedAuthority>) authentication.getAuthorities()).get(0).getPermissions().stream().collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }

        return Jwts.builder()
            .setSubject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(key)
            .parseClaimsJws(token)
            .getBody();

        List<String> lstPermission =
            Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .collect(Collectors.toList());
        CustomGrantedAuthority grantedAuthority = new CustomGrantedAuthority();
        grantedAuthority.setPermissions(lstPermission);
        grantedAuthority.setRole("ROLE_USER");

        User principal = new User(claims.getSubject(), "", Arrays.asList(grantedAuthority));

        return new UsernamePasswordAuthenticationToken(principal, token, Arrays.asList(grantedAuthority));
    }

    public String getAuthenticationByToken(String token) {
        return getClaimFromToken( token, Claims::getSubject );
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken( token );
        return claimsResolver.apply( claims );
    }
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
            .setSigningKey( key )
            .parseClaimsJws( token )
            .getBody();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace.", e);
        }
        return false;
    }


    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        String auths = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect( Collectors.joining( "," ) );
        return doGenerateToken( claims, userDetails.getUsername(), auths );
    }
    private String doGenerateToken(Map<String, Object> claims, String subject, String auths) {

        String token = Jwts.builder().setClaims( claims ).setSubject( subject )
            .claim( AUTHORITIES_KEY, auths )
            .setIssuedAt( new Date( System.currentTimeMillis() ) )
            .setExpiration( new Date( System.currentTimeMillis() + tokenValidityInMillisecondsForRememberMe * 1000 ) )
            .signWith( getSigningKey() ).compact();

        return token;
    }
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor( key.getEncoded() );
    }

    public String createTokenLGSP(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
            .map( GrantedAuthority::getAuthority )
            .collect( Collectors.joining( "," ) );

        long now = System.currentTimeMillis();
        Date validity = new Date( now + tokenValidityInMillisecondsForLGSP);

        return Jwts.builder()
            .setSubject( authentication.getName() )
            .claim( AUTHORITIES_KEY, authorities )
            .signWith( key, SignatureAlgorithm.HS512 )
            .setExpiration( validity )
            .compact();
    }

}

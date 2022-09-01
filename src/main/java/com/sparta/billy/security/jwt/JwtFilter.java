package com.sparta.billy.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.billy.dto.response.ResponseDto;
import com.sparta.billy.service.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  public static String AUTHORIZATION_HEADER = "Authorization";
  public static String BEARER_PREFIX = "Bearer ";

  public static String AUTHORITIES_KEY = "auth";

  private final String SECRET_KEY;

  private final TokenProvider tokenProvider;
  private final UserDetailsServiceImpl userDetailsService;

  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {

    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    Key key = Keys.hmacShaKeyFor(keyBytes);

    String jwt = resolveToken(request);

    if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
      Claims claims;
      try {
        claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
      } catch (ExpiredJwtException e) {
        claims = e.getClaims();
      }

      if (claims.getExpiration().toInstant().toEpochMilli() < Instant.now().toEpochMilli()) {
          response.setContentType("application/json;charset=UTF-8");
          response.sendError(HttpServletResponse.SC_BAD_REQUEST, "토큰이 유효하지 않습니다.");
          response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
          response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me, Origin,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization,Access-Token-Expire-Time");
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      }

      String subject = claims.getSubject();
      Collection<? extends GrantedAuthority> authorities =
          Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
              .map(SimpleGrantedAuthority::new)
              .collect(Collectors.toList());

      UserDetails principal = userDetailsService.loadUserByUsername(subject);

      Authentication authentication = new UsernamePasswordAuthenticationToken(principal, jwt, authorities);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }
  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(7);
    }
    return null;
  }

}

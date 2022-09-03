package com.sparta.billy.dto.MemberDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletResponse;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
  private String grantType;
  private String accessToken;
  private String refreshToken;
  private Long accessTokenExpiresIn;
  public void tokenToHeaders(HttpServletResponse response) {
    response.addHeader("Authorization", "Bearer " + getAccessToken());
    response.addHeader("Refresh-Token", getRefreshToken());
    response.addHeader("Access-Token-Expire-Time", getAccessTokenExpiresIn().toString());
  }
}

package com.example.woddy.user.utils;

import com.example.woddy.user.dto.CustomOAuth2User;
import com.example.woddy.user.dto.UserDTO;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtUtil;

    public JwtAuthFilter(JwtTokenUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, java.io.IOException {

        String requestUri = request.getRequestURI();

        // /login 또는 /oauth2 경로에 대한 JWT 필터 무시
        if (requestUri.matches("^/login(?:/.*)?$") || requestUri.matches("^/oauth2(?:/.*)?$")) {
            filterChain.doFilter(request, response);
            return;
        }

        // header에서 Authorization 추출
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // "Bearer " 이후의 토큰 값만 추출
        }


        // 토큰이 없는 경우 필터 처리 후 종료
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // JWT 토큰 검증
        try {
            jwtUtil.verifyToken(token);
        } catch (Exception e) {
            // 토큰 검증 실패 처리
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 토큰에서 username 추출
        String username = jwtUtil.getUid(token);

        // UserDTO에 사용자 정보 저장
        UserDTO userDTO = new UserDTO();
        userDTO.setOauthId(username);

        // CustomOAuth2User로 사용자 정보 설정
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);

        // 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

//         다음 필터로 이동
        filterChain.doFilter(request, response);
    }
}
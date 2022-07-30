package com.unibe.titulation.security.jwt;

import com.unibe.titulation.security.service.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {

    @Value("${jwt.accesTokenCookieName}")
    private String accessTokenCookieName;
    @Autowired
    JwtProvider jwtTokenUtil;
    @Autowired
    UserDetailsServiceImpl jwtUserDetailsService;
 
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getJwtFromCookie(req);
            if(token != null && jwtTokenUtil.validateToken(token)){
                String nombreUsuario = jwtTokenUtil.getUserNameFromToken(token);
                UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(nombreUsuario);
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e){
            logger.error("fail en el método doFilter " + e.getMessage());
        }
        filterChain.doFilter(req, res);
    }


      public String getJwtFromCookie(HttpServletRequest request) {
            Cookie cookie = WebUtils.getCookie(request, accessTokenCookieName);
            return cookie != null ? cookie.getValue() : null;
        }
      
}

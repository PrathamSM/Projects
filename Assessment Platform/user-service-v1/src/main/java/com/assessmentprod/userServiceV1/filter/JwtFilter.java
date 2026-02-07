package com.assessmentprod.userServiceV1.filter;

//import com.assessmentprod.userServiceV1.service.CustUserDetailsService;
//import com.assessmentprod.userServiceV1.utils.JwtUtils;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//@Configuration
//public class JwtFilter extends OncePerRequestFilter {
    public class JwtFilter  {
//    @Autowired
//    private JwtUtils jwtUtils;
//
//    @Autowired
//    private CustUserDetailsService custUserDetailsService;
//
//
//    /**
//     * Same contract as for {@code doFilter}, but guaranteed to be
//     * just invoked once per request within a single request thread.
//     * See {@link #shouldNotFilterAsyncDispatch()} for details.
//     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
//     * default ServletRequest and ServletResponse ones.
//     *
//     * @param request
//     * @param response
//     * @param filterChain
//     */
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String authHeader = request.getHeader("Authorization");
//        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//        String jwt = authHeader.substring(7);
//        String username = jwtUtils.extractUsername(jwt);
//
//        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = custUserDetailsService.loadUserByUsername(username);
//
//            if(userDetails != null && jwtUtils.isTokenValid(jwt)) {
//                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
//
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//        }
//
//        filterChain.doFilter(request, response);
//
//    }
}

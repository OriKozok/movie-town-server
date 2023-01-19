package com.MovieTown.filters;

import com.MovieTown.beans.MySession;
import com.MovieTown.exceptions.UnauthorizedException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Component
@Order(2)
public class TokenFilter extends OncePerRequestFilter {

    //HashMap<String, MySession> sessions;

    //public TokenFilter(HashMap<String, MySession> sessions) {
    //    this.sessions = sessions;
    //}

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            String token = request.getHeader("Authorization").replace("Bearer ", "");
            DecodedJWT decoded = JWT.decode(token);
            String iss = decoded.getClaim("iss").toString();
            if(!iss.equals("\"Movie Town\"")){
                throw new UnauthorizedException();
            }
            String type = decoded.getClaim("type").asString();
            request.setAttribute("type", type);
            filterChain.doFilter(request, response);
        }catch (JWTDecodeException | NullPointerException | UnauthorizedException e){
            response.setStatus(401);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/client") && !path.startsWith("/client/out");
    }
}

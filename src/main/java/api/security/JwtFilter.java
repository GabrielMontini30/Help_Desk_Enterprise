package api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private  UserDetailsServices userDetailsServices;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = recoveryToken(request);

        if(token!=null && jwtService.verifyToken(token)){
            String username = jwtService.extractUsername(token);
            if(username!=null || SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails = userDetailsServices.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }
        }

        filterChain.doFilter(request,response);

    }

    public String recoveryToken(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if(header==null || !header.startsWith("Bearer ")) return null;
        return header.replace("Bearer ","");
    }
}

package org.academy.OnlineStoreDemo.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        System.out.println(exception.getMessage());
        String login=request.getParameter("login");
        if (exception.getMessage().equals("User is disabled")){
            request.setAttribute("error","User is disabled");
            request.setAttribute("login",login);
        }
        if (exception.getMessage().equals("Bad credentials")){
            request.setAttribute("error","Bad credentials");
        }
        request.getServletContext().getRequestDispatcher("/login").forward(request,response);
    }
}
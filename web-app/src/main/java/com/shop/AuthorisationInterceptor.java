//package com.shop;
//
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import static com.shop.SessionAttributes.ROLE_SESSION_PARAMETER;
//
//public class AuthorisationInterceptor implements HandlerInterceptor {
//    @Override
//    public boolean preHandle(
//        HttpServletRequest request,
//        HttpServletResponse response,
//        Object handler
//    ) throws Exception {
//        String path = request.getRequestURI()
//            .substring(request.getContextPath().length())
//            .replaceAll("[/]+$", "");
//        HttpSession session = request.getSession();
//
//        boolean loggedIn = (session != null)
//            && (session.getAttribute(ROLE_SESSION_PARAMETER) != null);
//        boolean allowedPath = path.equals("/authorisation");
//
//        if (!(loggedIn || allowedPath)) {
//            response.sendRedirect(request.getContextPath() + "/authorisation");
//        }
//
//        return true;
//    }
//}

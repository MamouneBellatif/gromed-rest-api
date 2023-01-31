//package fr.miage.gromed.service.auth;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
////@Component
//public class FirebaseTokenInterceptor  implements HandlerInterceptor {
//        Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
////        boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//////                String token = request.getHeader("Authorization");
//////                try {
//////                        logger.info("Token: " + token);
//////                        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
//////                        request.setAttribute("user", decodedToken.getUid());
//////
//////                        return true;
//////                } catch (FirebaseAuthException e) {
//////                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//////                        response.getWriter().write("Unauthorized: Invalid Firebase Token");
//////                        return true;
//////                }
////                System.out.println("preHanSdle");
////                logger.info("preHanSdle");
////                return true;
////        }
//
//        @Override
//        public void afterCompletion(HttpServletRequest request,
//                                    HttpServletResponse response, Object object, Exception arg3)
//                throws Exception {
//                log.info("Request is complete");
//        }
//
//        @Override
//        public void postHandle(HttpServletRequest request,
//                               HttpServletResponse response, Object object, ModelAndView model)
//                throws Exception {
//                log.info("Handler execution is complete");
//        }
//
//        @Override
//        public boolean preHandle(HttpServletRequest request,
//                                 HttpServletResponse response, Object object) throws Exception {
//                log.info("Before Handler execution");
//                return true;
//        }
//
//
//}

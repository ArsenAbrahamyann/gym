package org.example.gym.logger;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor for generating and managing a unique transaction ID for each request.
 * This ID can be used for logging and tracing purposes throughout the request lifecycle.
 */
public class TransactionInterceptor implements HandlerInterceptor {

    /**
     * Pre-handle method that is executed before the actual handler is executed.
     * It generates a new transaction ID, stores it in the Mapped Diagnostic Context (MDC),
     * and allows the request to proceed.
     *
     * @param request the current HTTP request
     * @param response the current HTTP response
     * @param handler the chosen handler to execute
     * @return {@code true} to continue the execution chain; {@code false} to abort
     * @throws Exception if an error occurs during request handling
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String transactionId = UUID.randomUUID().toString();
        MDC.put("transactionId", transactionId);
        System.out.println("Generated Transaction ID: " + transactionId);
        return true;
    }

    /**
     * Callback after the handler has been executed.
     * This method is invoked after the request is completed,
     * allowing for cleanup tasks such as removing the transaction ID from the MDC.
     *
     * @param request the current HTTP request
     * @param response the current HTTP response
     * @param handler the executed handler
     * @param ex any exception thrown on handler execution, if any
     * @throws Exception if an error occurs during the cleanup process
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        MDC.remove("transactionId");
    }
}

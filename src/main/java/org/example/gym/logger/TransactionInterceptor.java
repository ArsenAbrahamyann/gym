package org.example.gym.logger;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor for generating and managing a unique transaction ID for each request.
 * This ID can be used for logging and tracing purposes throughout the request lifecycle.
 */
@Component("customTransactionInterceptor")
public class TransactionInterceptor implements HandlerInterceptor {

    /**
     * Pre-handle method executed before the request is processed.
     * A unique transaction ID is generated using {@link UUID#randomUUID()}, which is then placed
     * into the {@link MDC} for logging and tracing.
     *
     * @param request  the incoming {@link HttpServletRequest}
     * @param response the outgoing {@link HttpServletResponse}
     * @param handler  the handler responsible for processing the request
     * @return {@code true} to continue processing the request
     * @throws Exception if any error occurs
     */
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {
        String transactionId = UUID.randomUUID().toString();
        MDC.put("transactionId", transactionId);
        return true;
    }

    /**
     * After-completion method executed after the request has been processed.
     * This method removes the transaction ID from the {@link MDC} once the request is completed
     * to prevent it from leaking into other requests.
     *
     * @param request  the processed {@link HttpServletRequest}
     * @param response the processed {@link HttpServletResponse}
     * @param handler  the handler that processed the request
     * @param ex       any exception that occurred during processing (may be {@code null})
     * @throws Exception if any error occurs
     */
    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                @NonNull Object handler, Exception ex)
            throws Exception {
        MDC.remove("transactionId");
    }

}

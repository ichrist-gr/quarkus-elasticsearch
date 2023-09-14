package org.acme.logging;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.Dependent;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import java.util.Arrays;
import java.util.Objects;

@Dependent
@Interceptor
@ElasticQueryLogging
public class ElasticQueryLoggingInterceptor {
    @AroundInvoke
    public Object intercept(InvocationContext invocationContext) throws Exception {
        Object parameter = getQueryParameter(invocationContext);
        if (parameter == null) {
            return invocationContext.proceed();
        }

        Log.info(parameter.toString());
        return invocationContext.proceed();
    }

    private Object getQueryParameter(InvocationContext invocationContext) {
        return Arrays.stream(invocationContext.getParameters())
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }
}

package com.google.ratel.service.error;

import com.google.ratel.RatelConfig;
import com.google.ratel.service.log.*;
import com.google.ratel.core.Mode;
import javax.servlet.*;
import javax.servlet.http.*;

public interface ErrorHandlerService {
    
    public void onInit(ServletContext servletContext);
    
    public void onDestroy(ServletContext servletContext);

    public void handleRuntimeException(Throwable throwable, Mode mode, HttpServletRequest request, HttpServletResponse response, RatelConfig config);
    
    public void handleInitializationException(Throwable throwable, Mode mode, ServletContext servletContext, LogService logService);
}

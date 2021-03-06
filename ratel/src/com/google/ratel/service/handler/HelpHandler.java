package com.google.ratel.service.handler;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.*;
import java.util.*;
import javax.servlet.ServletContext;
import com.google.ratel.deps.io.*;
import com.google.ratel.Context;
import com.google.ratel.RatelConfig;
import com.google.ratel.core.JsonParam;
import com.google.ratel.core.Param;
import com.google.ratel.deps.lang3.*;
import com.google.ratel.service.classdata.*;
import com.google.ratel.service.json.JsonService;
import com.google.ratel.service.resolver.*;
import com.google.ratel.util.*;

/**
 *
 */
public class HelpHandler {

    private ServletContext servletContext;

    private String helpTemplate;

    private String pingTemplate;

    public void onInit(ServletContext servletContext) {
        this.servletContext = servletContext;

        helpTemplate = loadTemplate("help.htm");
        if (helpTemplate == null) {
            helpTemplate = "<html><body><h1>Help</h1>@TOKEN</body></html>";
        }

        pingTemplate = loadTemplate("ping.htm");
        if (pingTemplate == null) {
            pingTemplate = "<html><body><h1>Success!</h1>@TOKEN</body></html>";
        }
    }

    public void onDestroy(ServletContext servletContext) {
    }

    public void handleHelp(Context context) {
        ServiceResolver serviceResolver = context.getRatelConfig().getServiceResolver();

        Map<String, ClassData> serviceData = serviceResolver.resolveServices();

        HtmlStringBuffer html = new HtmlStringBuffer();

        renderServiceList(html, serviceData, context);

        String result = helpTemplate.replace("@TOKEN", html.toString());
        JsonService jsonService = context.getRatelConfig().getJsonService();
        RatelUtils.writeContent(jsonService, context.getResponse(), result, Constants.HTML);

        //templateService.renderTemplate("/help_1.htm", model, writer);
    }

    public Object invokeAsPing(Object target, MethodData methodData, Context context) {

        StringBuilder html = new StringBuilder();

        Method method = methodData.getMethod();
        List<ParameterData> parameters = methodData.getParameters();
        String location = buildPingLocation(target, methodData.getMethod(), parameters);

        html.append(location);
        html.append(" is UP!");

        JsonService jsonService = context.getRatelConfig().getJsonService();

        html.append("<p><b>Input:</b></p>");
        html.append("<ol>");
        for (ParameterData parameter : parameters) {
            Class parameterType = parameter.getType();

            String json = createJson(jsonService, parameterType);

            String displayName = getDisplayName(parameterType);
            if (RatelUtils.isPojo(parameterType)) {
                html.append("<li><pre>").append(displayName).append(" : <pre>").append(json).append("</pre></pre></li>");
            } else {
                html.append("<li><pre style='display:inline'>").append(displayName).append(" : ").append(json).append("</pre></li>");
            }
        }
        html.append("</ol>");

        Class returnType = method.getReturnType();
        String displayName = getDisplayName(returnType);
        String json = createJson(jsonService, returnType);
        html.append("<p><b>Output:</b></p>");
        html.append("<ul>");
        if (RatelUtils.isPojo(returnType)) {
                html.append("<li><pre>").append(displayName).append(" : <pre>").append(json).append("</pre></pre></li>");
            } else {
                html.append("<li><pre style='display:inline'>").append(displayName).append(" : ").append(json).append("</pre></li>");
            }
        html.append("</ul>");

        String result = pingTemplate.replace("@TOKEN", html.toString());

        return result;
    }

    private String getDisplayName(Class type) {
        if (type.isArray()) {
            return type.getComponentType().getName() + "[]";
        } else {
            return type.getName();
        }
    }

    private String buildPingLocation(Object target, Method targetMethod, List<ParameterData> parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("Service: ");
        sb.append(target.getClass().getName());
        sb.append(".");
        sb.append(targetMethod.getName());

        if (parameters.isEmpty()) {
            sb.append("()");
        } else {
            sb.append("(");

            Iterator<ParameterData> it = parameters.iterator();

            while (it.hasNext()) {
                ParameterData parameter = it.next();
                Class type = parameter.getType();
                boolean isRequired = parameter.isRequired();


                sb.append(type.getSimpleName());
                if (isRequired) {
                    sb.append("<span class='required-ind'>*</span>");
                }

                if (it.hasNext()) {
                    sb.append(", ");
                }
            }
            sb.append(")");
        }

        return sb.toString();
    }

    private void renderServiceList(HtmlStringBuffer html, Map<String, ClassData> serviceData, Context context) {

        String mappingPrefix = getMappingPrefix(context);
        String urlPrefix = getUrlPrefix(context);

        if (StringUtils.isNotBlank(mappingPrefix)) {

            if (!urlPrefix.equals(mappingPrefix)) {
                String packagePart = mappingPrefix.replace('/', '.');
            html.append("<div>");
            html.append("<span class='required-text'>**</span> Ratel is mapped in <span class='str'>web.xml</span> with <span class='str'>&lt;url-mapping&gt;</span> : ");
            html.append("<span style='color:red'>'").append(mappingPrefix).append("</span>");
            html.append("'. Either specify the &lt;context-param&gt; <span style='color:red'>ratel.urlPrefix</span>");
            html.append("");
                html.append(" (easiest) or ensure your Services are packaged under the package <span style='color:red'> '");
            html.append(packagePart);
            html.append("'</span> eg: 'com.mycorp<span style='color:red'>").append(packagePart);
            html.append("</span>.services.MyService. Also ensure your @Path annotations includes the path <span style='color:red'>").append(mappingPrefix).append("</span>.</td></tr>");
            html.append("</div>");
            }
        }

        Iterator<Map.Entry<String, ClassData>> it = serviceData.entrySet().iterator();
        html.elementStart("table");
        html.appendAttribute("class", "table");
        html.closeTag();

        html.elementStart("thead");
        html.closeTag();

        html.elementStart("tr");
        html.closeTag();

        html.elementStart("th");
        html.closeTag();
        html.append("Service Name");
        html.elementEnd("th");

        html.elementStart("th");
        html.closeTag();
        html.append("Method Name");
        html.elementEnd("th");

        html.elementStart("th");
        html.closeTag();
        html.append("Argument Details");
        html.elementEnd("th");

        html.elementStart("th");
        html.closeTag();
        html.append("URL");
        html.elementEnd("th");

        html.elementStart("th");
        html.closeTag();
        html.append("Comment");
        html.elementEnd("th");

        html.elementEnd("tr");
        html.elementEnd("thead");

        html.elementStart("tbody");
        html.elementStart("tr");

        while (it.hasNext()) {
            Map.Entry<String, ClassData> entry = it.next();
            ClassData serviceClassData = entry.getValue();


            if (serviceClassData.getMethods().isEmpty()) {
                html.elementStart("tr");
                html.closeTag();
                html.elementStart("td");
                html.closeTag();

                Class serviceClass = serviceClassData.getServiceClass();
                html.elementStart("pre");
                html.closeTag();
                html.append(serviceClass.getName());
                html.elementEnd("pre");
                html.elementEnd("td");

                html.elementStart("td");
                html.appendAttribute("colspan", "4");
                html.closeTag();
                html.append("<b>Service has no methods!</b>");
                html.elementEnd("td");
                html.elementEnd("tr");
            } else {
                renderServiceMethodsList(html, serviceClassData, context);
            }
        }

        html.elementEnd("tbody");
        html.elementEnd("table");
    }

    private void renderServiceMethodsList(HtmlStringBuffer html, ClassData serviceClassData, Context context) {
        Iterator<Map.Entry<String, MethodData>> it = serviceClassData.getMethods().entrySet().iterator();
        boolean renderedServiceNameInd = false;

        while (it.hasNext()) {
            Map.Entry<String, MethodData> entry = it.next();
            MethodData methodData = entry.getValue();

            html.elementStart("tr");
            html.closeTag();

            html.elementStart("td");
            html.closeTag();
            if (!renderedServiceNameInd) {
                Class serviceClass = serviceClassData.getServiceClass();

                html.elementStart("pre");
                html.closeTag();
                html.append(serviceClass.getName());
                html.elementEnd("pre");

                renderedServiceNameInd = true;
            }

            html.elementEnd("td");

            html.elementStart("td");
            html.closeTag();
            html.elementStart("pre");
            html.closeTag();
            String methodName = methodData.getMethod().getName();
            html.append(methodName);

            html.append(" (");
            renderArguments(html, methodData);
            html.append(")");

            html.elementEnd("pre");
            html.elementEnd("td");

            html.elementStart("td");
            html.closeTag();
            html.elementStart("pre");
            html.closeTag();
            renderArgumentDetails(html, methodData);
            html.elementEnd("pre");
            html.elementEnd("td");

            html.elementStart("td");
            html.closeTag();
            html.elementStart("a");

            String mappingPrefix = getMappingPrefix(context);
            String urlPrefix = getUrlPrefix(context);
            boolean urlPrefixCorrect = true;

            if (StringUtils.isNotBlank(mappingPrefix)) {
                urlPrefixCorrect = urlPrefix.equals(mappingPrefix);
            }

            StringBuilder val = new StringBuilder();
            val.append(servletContext.getContextPath());
            val.append(urlPrefix);
            val.append(serviceClassData.getServicePath());
            val.append(methodData.getMethodPath());
            val.append("?ping=true");
            html.appendAttribute("href", val);
            html.closeTag();
            html.append(urlPrefix);
            html.append(serviceClassData.getServicePath());
            html.append(methodData.getMethodPath());
            html.elementEnd("a");

            html.elementEnd("td");

            html.elementStart("td");
            html.closeTag();

            String servicePath = serviceClassData.getServicePath();

            boolean goodPathMapping = true;
            if (!urlPrefixCorrect) {
                if (!servicePath.startsWith(mappingPrefix)) {
                    goodPathMapping = false;
                }
            }

            // TODO add checks if the URL for the help is contained in the ServiceClass package name. If packageName: 'com.mycorp' and help url is: /rest/help
            // then class should be com.mycorp.rest.MyService

            if (goodPathMapping) {
                html.append("<span style='color:green'>OK</span>");

            } else {
                html.append("<span style='color:red'>ERROR: </span> Service path should be mapped as: <span style='color:red'>" + mappingPrefix
                    + "</span>" + servicePath + ". <span class='required-ind'>**</span>");
            }
            html.elementEnd("td");

            html.elementEnd("tr");
        }
    }

    private void renderArguments(HtmlStringBuffer html, MethodData methodData) {

        List<ParameterData> parameters = methodData.getParameters();
        Iterator<ParameterData> it = parameters.iterator();

        while (it.hasNext()) {
            ParameterData parameter = it.next();

            Class type = parameter.getType();
            String typeName = type.getSimpleName();
            html.append(typeName);
            if (parameter.isRequired()) {
                html.append("<span class='required-ind'>*</span>");
            }

            if (it.hasNext()) {
                html.append(", ");
            }
        }
    }

    private void renderArgumentDetails(HtmlStringBuffer html, MethodData methodData) {

        List<ParameterData> parameters = methodData.getParameters();
        Iterator<ParameterData> it = parameters.iterator();

        while (it.hasNext()) {
            ParameterData parameter = it.next();

            Param param = parameter.getParam();
            renderAnnotation(html, param);

            JsonParam jsonParam = parameter.getJsonParam();
            renderAnnotation(html, jsonParam);

            html.append(" ");
            Class type = parameter.getType();
            html.append("<span class='arg'>");
            String typeName = type.getSimpleName();
            html.append(typeName);
            html.append("</span>");
            if (parameter.isRequired()) {
                html.append("<span class='required-ind'>*</span>");
            }

            if (it.hasNext()) {
                html.append(",<br/>");
            }
        }
    }

    private void renderAnnotation(HtmlStringBuffer html, Param param) {
        if (param == null) {
            return;
        }

        html.append("<span class='annot'>@");
        html.append(param.annotationType().getSimpleName());
        html.append("</span>");
        html.append("(");
        html.append("name=<span class='str'>\"");
        html.append(param.name());
        html.append("\"</span>, ");
        html.append("required=<span class='kwd'>");
        html.append(param.required());
        html.append("</span>");
        html.append(")");
    }

    private void renderAnnotation(HtmlStringBuffer html, JsonParam param) {
        if (param == null) {
            return;
        }

        html.append("<span class='annot'>@");
        html.append(param.annotationType().getSimpleName());
        html.append("</span>");
        html.append("(");
        html.append("name=<span class='str'>\"");
        html.append(param.name());
        html.append("\"</span>, ");
        html.append("required=<span class='kwd'>");
        html.append(param.required());
        html.append("</span>, ");
        html.append(")");
    }

    private String loadTemplate(String name) {
        InputStream is = servletContext.getResourceAsStream("/" + name);
        if (is == null) {
            String resourcePath = "/META-INF/resources/" + name;
            is = RatelUtils.getClasspathResourceAsStream(resourcePath);
        }

        String template = null;

        try {
            if (is != null) {
                template = IOUtils.toString(is, (Charset) null);
            }
            return template;

        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    protected String getMappingPrefix(Context context) {
        RatelConfig ratelConfig = context.getRatelConfig();
        ServiceResolver resolver = ratelConfig.getServiceResolver();
        String path = resolver.resolvePath(context.getRequest());
        String prefix = StringUtils.remove(path, Constants.HELP);
        return prefix;
    }

    protected String createJson(JsonService jsonService, Class type) {

        try {
            Object input = RatelUtils.createInstance(type);
            String json = jsonService.toJson(input);
            return json;

        } catch (Exception e) {
            return "Could not instantiate instance!";
        }
    }

    protected String getUrlPrefix(Context context) {
        String urlPrefix = context.getRatelConfig().getUrlPrefix();
        if (urlPrefix == null) {
            urlPrefix = "";
        }
        return urlPrefix;
    }
}

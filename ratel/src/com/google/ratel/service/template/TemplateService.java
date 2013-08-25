package com.google.ratel.service.template;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import javax.servlet.ServletContext;


/**
 * Provides a templating service interface.
 *
 * <h3>Configuration</h3>
 * The default TemplateService is {@link VelocityTemplateService}.
 * <p/>
 * However you can instruct Click to use a different implementation by adding
 * the following element to your <tt>click.xml</tt> configuration file.
 *
 * <pre class="codeConfig">
 * &lt;?xml version="1.0" encoding="UTF-8" standalone="yes"?&gt;
 * &lt;click-app charset="UTF-8"&gt;
 *
 *     &lt;pages package="org.apache.click.examples.page"/&gt;
 *
 *     &lt;<span class="red">template-service</span> classname="<span class="blue">org.apache.click.extras.service.FreemarkerTemplateService</span>"&gt;
 *
 * &lt;/click-app&gt; </pre>
 */
public interface TemplateService {

    /**
     * Initialize the TemplateService with the given application configuration
     * service instance.
     * <p/>
     * This method is invoked after the TemplateService has been constructed.
     * <p/>
     * Note you can access {@link ConfigService} by invoking
     * {@link org.apache.click.util.ClickUtils#getConfigService(javax.servlet.ServletContext)}
     *
     * @param servletContext the application servlet context
     * @throws Exception if an error occurs initializing the Template Service
     */
    public void onInit(ServletContext servletContext) throws Exception;

    /**
     * Destroy the TemplateService.
     */
    public void onDestroy(ServletContext servletContext);

    /**
     * Render the given template and model to the writer.
     *
     * @param templatePath the path of the template to render
     * @param model the model to merge with the template and render
     * @param writer the writer to send the merged template and model data to
     * @throws IOException if an IO error occurs
     * @throws TemplateException if template error occurs
     */
    public void renderTemplate(String templatePath, Map<String, ?> model, Writer writer)
        throws IOException, TemplateException;

}

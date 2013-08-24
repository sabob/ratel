package com.google.ratel.service.classdata;

import java.lang.annotation.Annotation;
import com.google.ratel.core.JsonParam;
import com.google.ratel.core.Param;

/**
 *
 */
public class ParameterData {

    private Class<?> type;

    private Annotation[] annotations;

    private Param param;

    private JsonParam jsonParam;

    private boolean required;

    /**
     * @return the type
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(
        Class<?> type) {
        this.type = type;
    }

    /**
     * @return the annotations
     */
    public Annotation[] getAnnotations() {
        return annotations;
    }

    /**
     * @param annotations the annotations to set
     */
    public void setAnnotations(Annotation[] annotations) {
        this.annotations = annotations;
    }

    /**
     * @return the param
     */
    public Param getParam() {
        return param;
    }

    /**
     * @param param the param to set
     */
    public void setParam(Param param) {
        this.param = param;
        if (param.required()) {
            setRequired(true);
        }
    }

    /**
     * @return the jsonParam
     */
    public JsonParam getJsonParam() {
        return jsonParam;
    }

    /**
     * @param jsonParam the jsonParam to set
     */
    public void setJsonParam(JsonParam jsonParam) {
        this.jsonParam = jsonParam;
        if (jsonParam.required()) {
            setRequired(true);
        }
    }

    /**
     * @return the required
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * @param required the required to set
     */
    public void setRequired(boolean required) {
        this.required = required;
    }
}

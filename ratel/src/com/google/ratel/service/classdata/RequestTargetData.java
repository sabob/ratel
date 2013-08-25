package com.google.ratel.service.classdata;

/**
 *
 */
public class RequestTargetData {

    private ClassData classData;

    private MethodData methodData;

    public RequestTargetData(ClassData classData, MethodData methodData) {
        this.classData = classData;
        this.methodData = methodData;
    }

    /**
     * @return the classData
     */
    public ClassData getClassData() {
        return classData;
    }

    /**
     * @param classData the classData to set
     */
    public void setClassData(ClassData classData) {
        this.classData = classData;
    }

    /**
     * @return the methodData
     */
    public MethodData getMethodData() {
        return methodData;
    }

    /**
     * @param methodData the methodData to set
     */
    public void setMethodData(MethodData methodData) {
        this.methodData = methodData;
    }
}

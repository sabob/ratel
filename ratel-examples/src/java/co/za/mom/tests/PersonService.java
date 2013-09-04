package co.za.mom.tests;

import com.google.ratel.Context;
import com.google.ratel.RatelConfig;
import com.google.ratel.core.JsonParam;
import com.google.ratel.core.Param;
import com.google.ratel.core.RatelService;
import com.google.ratel.deps.fileupload.FileItem;
import com.google.ratel.deps.gson.Gson;
import com.google.ratel.deps.gson.GsonBuilder;
import com.google.ratel.util.Constants;
import java.util.HashMap;
import java.util.Map;

@RatelService
public class PersonService {

    public String getNoArgs() {
        return "OK";
    }

    public Integer getInteger(Integer i) {
        System.out.println("getInteger() called: " + i);
        return i;
    }
    
    public Integer[] getIntegerArray(@Param(name="id", required = true) Integer[] i) {
        System.out.println("getIntegerArray() called: " + i);
        return i;
    }

    public long getPrimitiveLong(long l) {
        System.out.println("getPrimitiveLong() called: " + l);
        return l;
    }
    
    public long[] getPrimitiveLongArray(@Param(name="id", required = true) long[] l) {
        System.out.println("getPrimitiveLongArray() called: " + l);
        return l;
    }

    public boolean getPrimitiveBoolean(boolean bool) {
        System.out.println("getPrimitiveBoolean() called: " + bool);
        return bool;
    }

    public String getJson(String args) {       
        System.out.println("json() called: " + args);
        return args;
    }

    public Person getPojo(Person person) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(person);
        System.out.println("getPerson() called with: " + json);
        Person result = new Person();
        result.setFirstname(person.getFirstname());
        result.setLastname(person.getLastname());
        return result;
    }

    public Person getNestedPojo(Person person) {
        System.out.println("getNestedPojo() called with: person: " + person);

        Person pojo = new Person();
        pojo.setFirstname(person.getFirstname());
        pojo.setLastname(person.getLastname());

        if (person.getOrg() != null) {
            Organisation pojo2 = new Organisation();
            pojo2.setName(person.getOrg().getName());
        }

        return person;
    }

    public Object[] getArray(int i, long l, boolean b, Person person) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(person);
        System.out.println("getArray() called with: " + json);
        Person result = new Person();
        result.setFirstname(person.getFirstname());
        result.setLastname(person.getLastname());
        Object[] ar = new Object[4];
        ar[0] = i;
        ar[1] = l;
        ar[2] = b;
        ar[3] = person;
        return ar;
    }

    public Integer getParam(@Param(name = "id", required = true) Integer i) {
        System.out.println("getParam() called - id:" + i);
        return i;
    }

    public Object[] getParams(@Param(name = "id", required = true) Long id, @Param(name = "employed") boolean employed, @Param(name =
        "description", required = true) String description) {
        System.out.println("getParams() called - id: " + id + ", employed: " + employed + ", description: " + description);
        Object[] array = new Object[3];
        array[0] = id;
        array[1] = employed;
        array[2] = description;
        return array;
    }

    public Person getJsonParam(@JsonParam(name = "person", required = true) Person person) {
        System.out.println("getJsonParam() called - person: " + person);
        return person;
    }

    public Object[] getMixedParams(@JsonParam(name = "person", required = true) Person person,
        @Param(name = "description") String description) {
        System.out.println("getMixedParams() called -  person: " + person + ", description: " + description);
        Object[] array = new Object[2];
        array[0] = person;
        array[1] = description;
        return array;
    }

    public String formParam(@Param(name = "name", required = true) String name) {
        System.out.println("formParam() called - name: " + name);
        Context.getContext().getResponse().setContentType(Constants.HTML);
        
        return "<b>" + name + "</b>";
    }

    public Object[] formParams(@Param(name = "description") String description, @JsonParam(name = "person", required = true) Person person) {
        System.out.println("formParams() called -  person: " + person + ", description: " + description);
        Object[] array = new Object[2];
        array[0] = description;
        array[1] = person;
        return array;
    }

    public String upload() {
        Context c = Context.getContext();
        FileItem[] fileItems = c.getFileItems("file");

        StringBuilder sb = new StringBuilder();
        for (FileItem fileItem : fileItems) {
            System.out.println("content:" + fileItem.getString());
            sb.append("Filename:").append(fileItem.getName());
            sb.append("<br/>");
        }
        return "Uploaded file(s): " + sb.toString();
    }

    public String uploadFileItem(@Param(name = "file", required = true) FileItem fileItem) {
        Context.getContext().getResponse().setContentType(Constants.HTML);

        StringBuilder sb = new StringBuilder();

        System.out.println("content:" + fileItem.getString());
        sb.append("Filename:").append(fileItem.getName());
        sb.append("<br/>");

        return "Uploaded file: " + sb.toString();
    }

    public String uploadFileItems(@Param(name = "file", required = true) FileItem[] fileItems) {
        Context.getContext().getResponse().setContentType(Constants.HTML);

        StringBuilder sb = new StringBuilder();
        for (FileItem fileItem : fileItems) {
            System.out.println("content:" + fileItem.getString());
            sb.append("Filename:").append(fileItem.getName());
            sb.append("<br/>");
        }
        return "Uploaded file(s): " + sb.toString();
    }
    
    public String getExceptionWithArgs(@Param(name = "id", required = true) String args) {
        return args;
    }
    
    public String getExceptionWithRuntime(String args) {
        throw new RuntimeException("Runtime Exception");
    }
    
    public String getExceptionWithJson() {
        throw new RuntimeException("JSON Exception");
    }

    public String getTemplate() {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("name", "Steve");
        model.put("break", this);
        String result = Context.getContext().renderTemplate("/template/test.htm", model);
        System.out.println(result);
        return result;
    }
    
    public String getTemplate2() {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("name", "Steve");
        model.put("BrokenRenderer", new BrokenRenderer());
        String result = Context.getContext().renderTemplate("/template/test2.htm", model);
        System.out.println(result);
        return result;
    }
    
    public void exception() {
        throw new RuntimeException("Stop!");
    }

    public static class BrokenRenderer {

        /**
         * Guaranteed to fail, or you money back.
         *
         * @see Object#toString()
         */
        @SuppressWarnings("null")
        @Override
        public String toString() {
            Object object = null;
            return object.toString();
        }
    }
}

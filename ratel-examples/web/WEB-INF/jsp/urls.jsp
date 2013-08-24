<%@ page %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page isELIgnored="false" %>

<portlet:defineObjects/>

<portlet:renderURL var="renderURL ">
    <portlet:param name="param-name" value="param-value" />
    <portlet:param name="param-name" value="param-value" />
</portlet:renderURL>

<script>
    var resourceUrl = '<portlet:resourceURL var="resourceURL" id="co.za.mom.PersonService/getPerson"><portlet:param name="param1" value="1" /></portlet:resourceURL>';

console.log("resource url", resourceUrl);
console.log("render url", "<%=renderURL%>");
</script>

<a href="<%=resourceURL%>"><%=resourceURL%></a>

<table border="0" cellspacing="2" cellpadding="2">
    <tr>
        <td class="portlet-section-alternate">
            <font class="portlet-font">This is an installation of <b>GateIn Portlet Container</font>
        </td>
    </tr>
</table>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag body-content="empty"  %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ attribute name="price" required="true" %>

${price}&nbsp;zł


<%--<fmt:formatNumber type="currency" maxFractionDigits="2" value="${price}"/>&nbsp;zł--%>
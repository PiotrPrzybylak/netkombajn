<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/jsp/sklep3.tld" prefix="s3" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="sklep" %>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag body-content="empty"  %>

<%@ attribute name="picture" required="true" type="pl.netolution.sklep3.domain.Picture" %>
<%@ attribute name="format" type="java.lang.String" %>
<%@ attribute name="id" type="java.lang.String" %>
<%@ attribute name="onmouseover" type="java.lang.String" %>
<%@ attribute name="alt" type="java.lang.String" %>
<%@ attribute name="lightbox" type="java.lang.Boolean" %>




<c:choose>
	<c:when test="${not empty format}">
		<c:set var="pictureUrl">${s3:generatePictureFormatUrl(picture,format)}</c:set>
	</c:when>
	<c:otherwise>
		<c:set var="pictureUrl">${s3:generatePictureUrl(picture)}</c:set>
	</c:otherwise>
</c:choose>

		<c:set var="originalPictureUrl">${s3:generatePictureUrl(picture)}</c:set>
			
<c:choose>
<c:when test="${lightbox}">
	<a href="${originalPictureUrl}" rel="lightbox[1]" title="${alt}">
		<img class="obrazek_produktu_tmp" src="${pictureUrl}" id="${id}" alt="${product.name}" onmouseover="${onmouseover}" alt="${alt}" rel="lightbox"/>				
	</a>
</c:when>
<c:otherwise>
	<img class="obrazek_produktu_tmp" src="${pictureUrl}" id="${id}" alt="${product.name}" onmouseover="${onmouseover}" alt="${alt}"/>
</c:otherwise>
</c:choose>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/jsp/sklep3.tld" prefix="s3" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="sklep" %>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag body-content="empty"  %>

<%@ attribute name="product" required="true" type="pl.netolution.sklep3.domain.Product" %>
<%@ attribute name="format" type="java.lang.String" %>
<%@ attribute name="id" type="java.lang.String" %>
<%@ attribute name="lightbox" type="java.lang.Boolean" %>

<c:choose>
	<c:when test="${product.useExternalPicture}">	
		<c:choose>
			<c:when test="${lightbox}">
				<a href="${product.externalPictureUrl}" rel="lightbox" title="${product.name}">
					<img src="${product.externalPictureUrl}" alt="${product.name}"/>		
				</a>
			</c:when>
			<c:otherwise>
				<img src="${product.externalPictureUrl}" alt="${product.name}"/>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<c:choose>
			<c:when test="${lightbox}">
				<a href="${product.mainPicture}" rel="lightbox" title="${product.name}">
					<sklep:picture picture="${product.mainPicture}" format="${format}" id="${id}" alt="${product.name}"/>
				</a>
			</c:when>
			<c:otherwise>
					<sklep:picture picture="${product.mainPicture}" format="${format}" id="${id}" alt="${product.name}"/>
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>


<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="string" uri="http://jakarta.apache.org/taglibs/string-1.1" %>

<%@ taglib tagdir="/WEB-INF/tags" prefix="sklep" %>

<c:set var="blockId"><string:replace replace=" " with="">${group.name}${param.counter}</string:replace></c:set>
<c:set var="nextBlockId"><string:replace replace=" " with="">${group.name}${param.counter+1}</string:replace></c:set>
<c:set var="prevBlockId"><string:replace replace=" " with="">${group.name}${param.counter-1}</string:replace></c:set>
<c:set var="groupName"><string:replace replace=" " with="">${group.name}</string:replace></c:set>

<div class="${param.visibilityClass}" id="${blockId}">
	
	<span class="hidden" id="productId">${product.id}</span>
	
	<div>
		<c:if test="${param.counter!=0}">
			<div class="compositeNavigation">
				<div class="backwardButton" onclick="CompositeProductDisplay.changeVisibility('${blockId}','${prevBlockId}','${groupName}')"></div>
			</div>
		</c:if>
		
		<div class="product-small-picture">
			<sklep:productPicture product="${product}" lightbox="true"/>
		</div>
		<div class="compositeProductPartInfo">
			<h4>${product.name}</h4>
			<div>${product.shortDescription}</div>
		</div>
		
		<c:if test="${!param.isLast}">
			<div class="compositeNavigation">
				<div class="forwardButton" onclick="CompositeProductDisplay.changeVisibility('${blockId}','${nextBlockId}','${groupName}')"></div>
			</div>
		</c:if>
	</div>
</div>
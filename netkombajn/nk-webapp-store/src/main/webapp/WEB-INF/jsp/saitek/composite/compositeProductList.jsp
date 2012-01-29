<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="sklep" %>

<div class="box">
<h2>Zestawy komputerowe</h2>
<div class="box-content">
	

	<c:forEach items="${compositeProducs}" var="compositeProduct">
		<div class="compositeProduct">
			<div>
				<div class="compositePicture"> </div>
				<div>
						<div class="compositeProductName">
							<a href="compositeProductDetails.html?compositeId=${compositeProduct.id}">${compositeProduct.name}</a>
						</div>
						<div class="compositeProductDescription">${compositeProduct.description}</div>
						<div class="compositeLinkToDetails">
							<a href="compositeProductDetails.html?compositeId=${compositeProduct.id}">więcej >></a>
						</div>
				</div>
			</div>
		</div>
	</c:forEach>

</div>

</div>
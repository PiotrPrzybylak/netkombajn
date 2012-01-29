<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="search box">
<h2>Wyszukiwanie</h2>
<div class="box-content">
<form method="get" action="search.html">
<p><label for="searchPhrase">Wyraz(y)</label> <input
	id="searchPhrase" name="searchPhrase" value="${param.searchPhrase}" class="textInput" /></p>
<p><label for="minPrice">Cena</label> <span id="priceRange"><input
	id="minPrice" name="minPrice" value="${param.minPrice}" class="textInput" /><span>do</span><input
	id="maxPrice" name="maxPrice" value="${param.maxPrice}" class="textInput" /></span></p>
<p><label for="category">Kategoria</label> 
	<select name="categoryId" id="categoryId">
			<option value="0">Wszystkie</option>
		<c:forEach items="${categories}" var="category">
			<option value="${category.id}" ${ category.id == param.categoryId ? 'selected="selected"' : '' }>${category.name}</option>
		</c:forEach>
	</select>
</p>
<button type="submit" class="searchButton"><span>Szukaj</span></button>
</form>
</div>
</div>
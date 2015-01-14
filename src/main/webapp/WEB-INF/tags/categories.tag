<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag description="Writes the HTML code for output categories menu." %>
<%@ attribute name="categories" required="true" type="java.util.Collection" %>

<div class="title_box">Categories</div>
<ul class="left_menu">
    <c:set var="count" value="0" scope="page"/>
    <c:forEach items="${categories}" var="category" varStatus="loopStatus">
        <c:choose>
            <c:when test="${loopStatus.count % 2 == 0}">
                <li class="odd"><a href="catalog?category=${category.name}">${category.name}</a></li>
            </c:when>
            <c:otherwise>
                <li class="even"><a href="catalog?category=${category.name}">${category.name}</a></li>
            </c:otherwise>
        </c:choose>
    </c:forEach>
</ul>
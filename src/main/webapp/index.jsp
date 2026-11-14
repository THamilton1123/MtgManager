<%@include file="taglib.jsp" %>

<html>
<c:import url="/head.jsp" />
<body>

<c:choose>
    <c:when test="${empty userName}">
        <h2><a href="logIn">Log In</a></h2>
    </c:when>
    <c:otherwise>
        <div style="display: flex; justify-content: space-between; align-items: center; padding-right: 250px; margin-top: 15px; margin-bottom: 15px;">
            <h2>Welcome, ${userName}, to your Personal MTG Manager</h2>
            <a href="${pageContext.request.contextPath}/logOut" style="font-size: 1.1em; font-weight: bold;">Log Out</a>
        </div>
        <form action="searchCard" class="form-inline">
            <div class="form-group">
                <label for="searchTerm">Search Your Collection</label>
                <input type="text" class="form-control" id="searchTerm" name="searchTerm" aria-describedby="searchTermHelp" placeholder="Enter card name">
            </div>
            <button type="submit" name="submit" value="search" class="btn btn-primary">Search</button>
            <button type="submit" name="submit" value="viewAll" class="btn btn-primary">View all cards</button>
        </form><br>
        <form action="searchScryfall" class="form-inline">
            <div class="form-group">
                <label for="searchTermCardName">Search Scryfall</label>
                <input type="text" class="form-control" id="searchTermCardName" name="searchTermCardName" aria-describedby="searchTermHelp" placeholder="Enter exact card name">
            </div>
            <button type="submit" name="submit" value="searchByNameExact" class="btn btn-primary">Search</button>
        </form>
    </c:otherwise>
</c:choose>

</body>
</html>
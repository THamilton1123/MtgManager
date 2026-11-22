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
        <h3>Search Your Collection</h3>
        <form action="searchCard" class="form-inline">
            <div class="form-group">
                <label for="searchTerm">By Name:</label>
                <input type="text" class="form-control" id="searchTerm" name="searchTerm" aria-describedby="searchTermHelp" placeholder="Enter card name">
            </div>
            <button type="submit" name="submit" value="searchCollectionByNameLike" class="btn btn-primary">Search</button>
            <button type="submit" name="submit" value="viewAllCollection" class="btn btn-primary">View all cards</button>
        </form><br>
        <h3>Search ScryFall</h3>
        <form action="searchScryfall" class="form-inline">
            <div class="form-group">
                <label for="exactCardName">By Name:</label>
                <input type="text" class="form-control" id="exactCardName" name="exactCardName" aria-describedby="searchTermHelp" placeholder="Enter exact card name">
            </div>
            <button type="submit" name="submit" value="searchScryfallByNameExact" class="btn btn-primary">Search</button>
        </form>
        <form action="searchScryfall" class="form-inline">
            <div class="form-group">
                <label for="scryfallUuid">By UUID (Universally Unique Identifier):</label>
                <input type="text" class="form-control" id="scryfallUuid" name="scryfallUuid" aria-describedby="searchTermHelp" placeholder="Enter card uuid">
            </div>
            <button type="submit" name="submit" value="searchScryfallByUuid" class="btn btn-primary">Search</button>
        </form>
    </c:otherwise>
</c:choose>

</body>
</html>
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
        <h3>Search Cards</h3>
        <form id="searchForm" action="searchCard">

            <div class="form-group" style="display: inline-block; background-color: #f4f6f9; border: 2px solid #337ab7; border-radius: 6px; padding: 10px 18px; margin-bottom: 16px;">
                <span style="font-weight: bold; font-size: 1.15em; margin-right: 15px;">Search In:</span>
                <label class="radio-inline" style="font-size: 1.15em; font-weight: bold; cursor: pointer; margin-right: 18px;">
                    <input type="radio" name="searchTarget" value="collection" checked
                           style="transform: scale(1.35); margin-right: 8px; cursor: pointer;"
                           onchange="document.getElementById('searchForm').action='searchCard';">
                    My Collection
                </label>
                <label class="radio-inline" style="font-size: 1.15em; font-weight: bold; cursor: pointer;">
                    <input type="radio" name="searchTarget" value="scryfall"
                           style="transform: scale(1.35); margin-right: 8px; cursor: pointer;"
                           onchange="document.getElementById('searchForm').action='searchScryfall';">
                    Scryfall
                </label>
            </div>

            <div class="form-inline">
                <div class="form-group">
                    <label for="searchName">Name:</label>
                    <input type="text" class="form-control" id="searchName" name="searchName" placeholder="Enter card name">
                </div>
            </div>

            <div class="checkbox" style="margin: 0 10px;">
                <label>
                    <input type="checkbox" name="exactName" value="true"> Exact name?
                </label>
            </div>

            <button type="submit" name="submit" value="search" class="btn btn-primary" style="margin-top: 24px;">Search</button>
            <button type="submit" name="submit" value="viewAllCollection" class="btn btn-default" style="margin-top: 24px;"
                    onclick="document.getElementById('searchForm').action='searchCard';">
                View All Collection
            </button>
        </form><br>

        <h4>Quick Lookup by Scryfall UUID</h4>
        <form action="searchScryfall" class="form-inline">
            <div class="form-group">
                <label for="scryfallUuid">By UUID:</label>
                <input type="text" class="form-control" id="scryfallUuid" name="scryfallUuid" placeholder="Enter card uuid">
            </div>
            <button type="submit" name="submit" value="searchByUuid" class="btn btn-primary">Search</button>
        </form>
    </c:otherwise>
</c:choose>

</body>
</html>
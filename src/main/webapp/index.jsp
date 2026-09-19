<%@include file="taglib.jsp" %>

<html>
<c:import url="/head.jsp" />
<body>

<h2>Personal MTG Manager</h2>
<form action="searchCard" class="form-inline">
    <div class="form-group">
        <label for="searchTerm">Search</label>
        <input type="text" class="form-control" id="searchTerm" name="searchTerm" aria-describedby="searchTermHelp" placeholder="Enter card name">
    </div>
    <button type="submit" name="submit" value="search" class="btn btn-primary">Search</button>
    <button type="submit" name="submit" value="viewAll" class="btn btn-primary">View all cards</button>
</form>

</body>
</html>
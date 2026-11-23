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

                <%-- Top Control Bar: Search Target (Left) & Unique/Inclusion Options (Right) --%>
            <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 15px; margin-bottom: 16px;">

                    <%-- Left Box: Search In --%>
                <div class="form-group" style="background-color: #f4f6f9; border: 2px solid #337ab7; border-radius: 6px; padding: 10px 18px; margin-bottom: 0;">
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

                    <%-- Right Box: Unique 3-Way Toggle & Inclusion Checkboxes --%>
                <div class="form-group" style="background-color: #f4f6f9; border: 2px solid #337ab7; border-radius: 6px; padding: 10px 18px; margin-bottom: 0;">
                    <span style="font-weight: bold; font-size: 1.15em; margin-right: 12px;">Unique:</span>
                    <label class="radio-inline" style="font-size: 1.1em; font-weight: bold; cursor: pointer; margin-right: 12px;">
                        <input type="radio" name="uniqueMode" value="cards" checked
                               style="transform: scale(1.3); margin-right: 6px; cursor: pointer;">
                        Cards
                    </label>
                    <label class="radio-inline" style="font-size: 1.1em; font-weight: bold; cursor: pointer; margin-right: 12px;">
                        <input type="radio" name="uniqueMode" value="art"
                               style="transform: scale(1.3); margin-right: 6px; cursor: pointer;">
                        Art
                    </label>
                    <label class="radio-inline" style="font-size: 1.1em; font-weight: bold; cursor: pointer; margin-right: 20px;">
                        <input type="radio" name="uniqueMode" value="prints"
                               style="transform: scale(1.3); margin-right: 6px; cursor: pointer;">
                        Prints
                    </label>

                    <span style="border-left: 2px solid #ccc; padding-left: 16px; font-weight: bold; font-size: 1.15em; margin-right: 12px;">Include:</span>
                    <label class="checkbox-inline" style="font-size: 1.1em; font-weight: bold; cursor: pointer; margin-right: 14px;">
                        <input type="checkbox" name="includeExtras" value="true"
                               style="transform: scale(1.3); margin-right: 6px; cursor: pointer;">
                        Extras
                    </label>
                    <label class="checkbox-inline" style="font-size: 1.1em; font-weight: bold; cursor: pointer; margin-right: 14px;">
                        <input type="checkbox" name="includeVariations" value="true"
                               style="transform: scale(1.3); margin-right: 6px; cursor: pointer;">
                        Variations
                    </label>
                    <label class="checkbox-inline" style="font-size: 1.1em; font-weight: bold; cursor: pointer;">
                        <input type="checkbox" name="includeMultilingual" value="true"
                               style="transform: scale(1.3); margin-right: 6px; cursor: pointer;">
                        Multilingual
                    </label>
                </div>

            </div>


            <div class="form-inline">
                <div class="form-group">
                    <label for="searchName">Name:</label>
                    <input type="text" class="form-control" id="searchName" name="searchName"
                           placeholder="Enter card name" style="width: 450px;">
                </div>
            </div>

            <div class="checkbox" style="margin: 0 10px;">
                <label>
                    <input type="checkbox" name="exactName" value="true"> Exact name?
                </label>
            </div>

            <div class="form-inline" style="margin-top: 15px; margin-bottom: 15px;">
                <div class="form-group">
                    <label for="setCode">Set:</label>
                    <select class="form-control" id="setCode" name="setCode" onchange="updateSetIcon(this)">
                        <option value="" data-icon="">-- Any Set --</option>
                        <c:forEach var="set" items="${scryfallSets}">
                            <option value="${set.setCode}" data-icon="${set.setIconUri}">
                                    ${set.setName} (${set.setCode.toUpperCase()})
                            </option>
                        </c:forEach>
                    </select>
                    <img id="selectedSetIcon" src="" alt="" style="display: none; width: 22px; height: 22px; margin-left: 8px; vertical-align: middle;">
                </div>
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

<script>
    function updateSetIcon(selectEl) {
        const iconUrl = selectEl.options[selectEl.selectedIndex].getAttribute('data-icon');
        const img = document.getElementById('selectedSetIcon');
        if (iconUrl) {
            img.src = iconUrl;
            img.style.display = 'inline-block';
        } else {
            img.style.display = 'none';
        }
    }
</script>

</body>
</html>
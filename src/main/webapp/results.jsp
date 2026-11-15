<%@include file="taglib.jsp" %>
<c:set var="title" value="Search Results" />
<c:import url="/head.jsp" />

<style>
    /* Keeps the header row stuck to the top of the screen when scrolling */
    #cardTable thead th {
        position: sticky;
        top: 0;
        background-color: #ffffff; /* Gives headers a solid background color so content doesn't bleed underneath */
        z-index: 100;              /* Forces headers to float over images while scrolling */
        box-shadow: 0 2px 2px -1px rgba(0,0,0,0.15); /* Adds a subtle shadow separating it from scrolling rows */
    }

    .results-header {
        display: flex;
        align-items: center;
        gap: 30px;              /* Controls the space between Home and Log Out */
        padding-right: 250px;   /* Keeps your reserved space on the right */
        margin: 15px 0;
    }

    .results-header h2 {
        margin: 0 auto 0 0;     /* Pushes the links all the way to the right */
    }

    .nav-link {
        font-size: 1.1em;
        font-weight: bold;
    }

</style>

<script type="text/javascript" class="init">
    $(document).ready( function () {
        $('#cardTable').DataTable({
            "dom": '<"top"lp>rt<"bottom"lp><"clear">'
        });
    } );
</script>

<html>
<body>

<div class="container-fluid">
    <header class="results-header">
        <h2>${searchLocation} Search Results (${cards.size()}): </h2>
        <a class="nav-link" href="${pageContext.request.contextPath}/">Home</a>
        <a class="nav-link" href="${pageContext.request.contextPath}/logOut">Log Out</a>
    </header>
    <table id="cardTable" class="display" cellspacing="0" width="100%">
        <thead>
        <tr>
            <c:if test="${searchLocation == 'Your Collection'}">
                <th>DB ID</th>
            </c:if>
            <th>Card Image</th>
            <th>ScryFall UUID</th>
            <th>Card Name</th>
            <th>Card CMC</th>
            <th>Card Type</th>
            <c:if test="${searchLocation == 'Your Collection'}">
                <th>Card Quantity</th>
            </c:if>
            <th>Card Rulings</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${cards}" var="card">
            <tr>
                <c:if test="${searchLocation == 'Your Collection'}">
                    <td>${card.id}</td>
                </c:if>
                <td>
                    <div style="display: flex; align-items: center; gap: 15px;">
                        <img src="${card.imageUrl}" alt="${card.cardName}" width="225">

                        <div style="display: flex; flex-direction: column; gap: 25px;">
                            <form action="UpdateCollection" method="post"
                                  onsubmit="return confirm('Add ' + this.quantity.value + ' copy/copies of ' + this.cardName.value + ' to your collection?');"
                                  style="display: flex; flex-direction: column; gap: 5px; margin: 0;">
                                <input type="hidden" name="cardName" value="${card.cardName}">
                                <input type="hidden" name="cardCmc" value="${card.cardCmc}">
                                <input type="hidden" name="cardType" value="${card.cardType}">
                                <input type="hidden" name="imageUrl" value="${card.imageUrl}">
                                <input type="hidden" name="scryfallUuid" value="${card.scryfallUuid}">

                                <label for="add-qty-${card.cardName}" style="margin: 0; font-size: 0.9em;">Add Qty:</label>
                                <input type="number" id="add-qty-${card.cardName}" name="quantity" value="1" min="1" style="width: 65px;" required>

                                <button type="submit" name="action" value="add">Add</button>
                            </form>

                            <c:if test="${searchLocation == 'Your Collection'}">
                                <form action="UpdateCollection" method="post"
                                      onsubmit="return confirm('Are you sure you want to remove ' + this.quantity.value + ' copy/copies of ' + this.cardName.value + '?');"
                                      style="display: flex; flex-direction: column; gap: 5px; margin: 0; padding-top: 15px; border-top: 1px solid #ccc;">
                                    <input type="hidden" name="cardId" value="${card.id}">
                                    <input type="hidden" name="cardName" value="${card.cardName}">
                                    <input type="hidden" name="scryfallUuid" value="${card.scryfallUuid}">

                                    <label for="remove-qty-${card.id}" style="margin: 0; font-size: 0.9em;">Remove Qty:</label>
                                    <input type="number" id="remove-qty-${card.id}" name="quantity" value="1" min="1" max="${card.cardQuantity}" style="width: 65px;" required>

                                    <button type="submit" name="action" value="remove">Remove</button>
                                </form>
                            </c:if>
                        </div>
                    </div>
                </td>
                <td>${card.scryfallUuid}</td>
                <td>${card.cardName}</td>
                <td>${card.cardCmc}</td>
                <td>${card.cardType}</td>
                <c:if test="${searchLocation == 'Your Collection'}">
                    <td>${card.cardQuantity}</td>
                </c:if>
                <td>
                    <ul>
                        <c:forEach items="${card.rulings}" var="ruling">
                            <li>${ruling.rulingDate}:<br/>${ruling.rulingText}</li>
                        </c:forEach>
                    </ul>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

</body>
</html>

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
    <h2>${searchLocation} Search Results: </h2>
    <table id="cardTable" class="display" cellspacing="0" width="100%">
        <thead>
        <tr>
            <c:if test="${searchLocation == 'Your Collection'}">
                <th>DB ID</th>
            </c:if>
            <th>Card Image</th>
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
                <td><img src="${card.imageUrl}" alt="${card.cardName}" width="250"></td>
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

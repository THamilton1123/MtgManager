<%@include file="taglib.jsp" %>
<c:set var="title" value="Search Results" />
<c:import url="/head.jsp" />

<script type="text/javascript" class="init">
    $(document).ready( function () {
        $('#cardTable').DataTable();
    } );
</script>

<html>
<body>

<div class="container-fluid">
    <h2>Search Results: </h2>
    <table id="cardTable" class="display" cellspacing="0" width="100%">
        <thead>
        <tr>
            <th>DB ID</th>
            <th>Card Name</th>
            <th>Card CMC</th>
            <th>Card Type</th>
            <th>Card Quantity</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${cards}" var="card">
            <tr>
                <td>${card.cardId}</td>
                <td>${card.cardName}</td>
                <td>${card.cardCmc}</td>
                <td>${card.cardType}</td>
                <td>${card.cardQuantity}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

</body>
</html>

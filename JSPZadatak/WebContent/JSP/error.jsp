<%@page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Greska</title>
</head>
<body>
	<p style=color:red> Doslo je do greske pri unosu podataka.</p>
	<c:if test="${not empty error}"> 
	<p style="color: red">${error}</p> 
	</c:if>
</body>
</html>
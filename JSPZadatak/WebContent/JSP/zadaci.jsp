<%@page import="beans.Zadatak"%>
<%@ page import="beans.TezinaZadatka" %>
<%@page contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="zadaci" class="dao.ZadaciDAO" scope="application"/>
<!-- Koristi objekat registrovan pod ključem "user" iz sesije -->
<html>
<head>
</head>
<body>
<h1 style="color:pink">JSP Pregled zadataka</h1>
	<table border="1">
		<thead>
			<th>Broj zadatka</th>
			<th>Naziv</th>
			<th>Zaduzeni</th>
			<th>Broj telefona</th>
			<th>Tezina zadatka</th>
			<th>Bodovi</th>
			<th></th>
		</thead>
		<tbody>
		<c:forEach var="z" items="${zadaci.getZadaci()}">
			<tr>
				
					<td>${z.brojZadatka}</td>
					<td>${z.naziv}</td>
					<td>${z.zaduzeni}</td>
					<td>${z.brojTelefona}</td>
					<td>${z.tezinaZadatka}</td>
					<td>${z.bodovi}</td>
					<c:choose>
					  <c:when test="${zadaci.isZadatakUToku(z)}">
					    <td></td>
					  </c:when>
					  <c:otherwise>
					    <td> <a href="/JSPZadatak/UTokuServlet?id=${z.brojZadatka}"> U toku </a> </td>
					  </c:otherwise>
					</c:choose>
					
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<h3 style="color:grey"> Pretraga zadatka po nazivu </h3>
	<form action="PretraziServlet" method="POST">
	<table>
		<tr>	
			<td> Unesite naziv zadatka: </td>
			<td> <input type="text" id="nazivSearch" name="nazivSearch" ></td>
		</tr>
		<tr>
			<td></td>
			<td> <input type="submit" value="Pretrazi"></td>
		</tr>
	</table>
	</form>
</body>
</html>
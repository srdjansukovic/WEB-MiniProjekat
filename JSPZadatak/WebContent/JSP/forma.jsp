<%@page contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title> Kreiranje zadatka </title>
</head>
<body>
	<h1 style="color:pink"> JSP. Kreiranje zadatka </h1>
	<form action="FormServlet" method="POST">
		<table>
                <tr>
                    <td>
                        Broj zadatka(id):
                    </td>
                    <td>
                        <input type="text" id="brojZadatka" name="brojZadatka">
                    </td>
                </tr>
                <tr>
                    <td>
                        Naziv:
                    </td>
                    <td>
                        <input type="text" id="naziv" name="naziv">
                    </td>
                </tr>
                <tr>
                    <td>
                        Zaduzeni:
                    </td>
                    <td>
                        <input type="text" id="zaduzeni" name="zaduzeni">
                    </td>
                </tr>
                <tr>
                    <td>
                        Broj telefona:
                    </td>
                    <td>
                        <input type="text" id="brojTelefona" name="brojTelefona">
                    </td>
                </tr>
                <tr>
                    <td>
                        <label> Tezina zadatka: </label>
                    </td>
                    <td>
                        <select id="tezinaZadatka" name="tezinaZadatka">
                            <option value="LAK"> Lak </option>
                            <option value="TEZAK"> Tezak </option>
                        </select> <br>
                    </td>
                </tr>
                <tr>
                    <td>
                        Bodovi:
                    </td>
                    <td>
                        <input type="text" id="bodovi" name="bodovi">
                    </td>
                </tr>
                
                <tr>
                    <td align="right">JSP:</td>
                    <td>
                        <input type="submit" value="Posalji">
                    </td>
                </tr>
            </table>

          </form>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>File Upload for Data Migration</title>
</head>
<body>
    <div align="center">
        <h2>File Upload for Data Migration</h2>
        <form method="post" action="UploadServlet" enctype="multipart/form-data">
            <table border="0">
                <tr>
                    <td align="right">Please provide the data file to import:</td>
                    <td><input type="file" name="filename" size="80"/></td>
                </tr>
                <tr>
                    <td colspan="2" align=center>
                        <input type="submit" value="Import">
                    </td>
                </tr>
                <tr>
                    <td colspan="2" align=center>
                        <%  String message = (String)request.getAttribute("message");
                            if(message != null && !message.equals("null")) {%>
                            <%= message %>
                        <% }%>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</body>
</html>

<%@page import="java.util.Iterator"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<%@page import="com.lue.pintrests.FinalResult"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.List"%>
<%@page import="com.lue.pintrests.PinterestAnalyzer"%>
<%@page import="static com.lue.pintrests.Analyzeservlet.*"%>
<%@page import="com.lue.pins.PinSearchResponse"%>
<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@page import="com.fasterxml.jackson.databind.DeserializationFeature"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>Pinterest_Result</title>
        <style>
            table, td, th {
                border: 1px solid green;
            }

            th {
                background-color: green;
                color: white;
            }
        </style>
    </head>
    <body style="alignment-adjust: central;">
        <h1 style="color: royalblue; text-align: center;">Results </h1>

        <table>
            <thead>
	            <th>#</th>
	            <th>PIN Img</th>
	            <th># Repins</th>
	            <th># Likes</th>
	            <th>Description</th>
	        </thead>
        <tbody>
            <%
                int r = 0;
                Map m = (Map) request.getSession().getAttribute("value");
                Set<String> key = m.keySet();
                ArrayList<FinalResult> fls = new ArrayList<FinalResult>();
                for (String lstp : key) {
                    FinalResult fr = (FinalResult) m.get(lstp);
                    fls.add(fr);
                }
                Collections.sort(fls, Collections.reverseOrder());
                for (FinalResult fr : fls) {
                    r++;
            %>

            <tr>
                <td style="text-align: center;"><%=r%></td>
                <td width="220px">
                	<a href="<%=fr.getUrl()%>" target="_blank">
                		<img src="<%=fr.getImageUrl()%>" width="200" style="padding: 10px 10px;"/>
                	</a>
                </td>

                <td style="text-align: center;"><%=fr.getRepins().intValue()%></td>
                <td style="text-align: center;"><%=fr.getLikes().intValue()%></td>
                <td><%=fr.getNote()%></td>
            </tr>

            <%} %>
        </tbody>
    </table>
    <%if (r == 0) {%>
    <h1 style="color: red; text-align: center;">No Results</h1>
    <a href="analyze-board.jsp">Back</a>
    <%}%>

</body>
</html>

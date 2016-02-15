<%
    System.out.println( request.getAttribute("url"));
    response.sendRedirect( (String)request.getAttribute("url"));
%>
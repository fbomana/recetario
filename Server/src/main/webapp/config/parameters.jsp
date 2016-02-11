<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Recetario- configuration</title>
    <link rel="stylesheet" href="../css/recetario.css">
    <link rel="stylesheet" href="../css/config.css">
</head>
<body>
<jsp:include page="../menu"/>
<div id="content">
<h1>Configuration</h1>
Version: ${preferences.version}.<br>
<form method="post" name="fconf" id="fconf" action="parameters">
    <label for="name">Name</label><br>
    <input type="text" id="name" name="name" value="${preferences.name}" required><br>
    <label for="autosaveInterval">Autosave Interval</label><br>
    <input type="number" id="autosaveInterval" name="autosaveInterval" value="${preferences.autoSaveInterval}" min="10000"><br><input type="submit" value="Save Changes"/><br>
</form>
</div>
</body>
</html>

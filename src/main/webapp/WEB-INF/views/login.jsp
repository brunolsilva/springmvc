<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login</title>
</head>
<body>
	<form:form servletRelativeAction="/login">
		<div>
			<label><fmt:message key="login.user" /> </label>
			<input type="text" name="username" value="" />
		</div>
		<div>
			<label><fmt:message key="login.password" /></label>
			<input type="password" name="password" value="" />
		</div>
		<div>
			<input name="submit" type="submit" value="Login" />
		</div>
	</form:form>
</body>
</html>
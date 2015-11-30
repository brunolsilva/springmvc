<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Cadastro de Produtos</title>
</head>
<body>
	<form:form method="post" action="/casadocodigo/produtos" enctype="multipart/form-data" modelAttribute="product" >
		<div>
			<label for="title">Título</label>
			<form:input path="title"/>
			<form:errors path="title" />
		</div>
		<div>
			<label for="description">Descrição</label>
			<form:textarea path="description" rows="10" cols="20" />
			<form:errors path="description" />
		</div>
		<div>
			<label for="pages">Número de paginas</label>
			<form:input path="pages"/>
			<form:errors path="pages" />
		</div>
		<c:forEach items="${types }" var="bookType" varStatus="status">
			<div>
				<label for="price_${bookType }">${bookType}</label>
				<form:input path="prices[${status.index}].value" id="price_${bookType }" />
				<form:hidden path="prices[${status.index}].bookType" value="${bookType }" />
				<%-- <input type="text" name="prices[${status.index}].value" id="price_${bookType }" />
				<input type="hidden" name="prices[${status.index}].bookType" value="${bookType }" /> --%>
			</div>
		</c:forEach>
		
		<div>
			<label for="releaseDate">Data de lançamento</label>
			<form:input path="releaseDate" type="date" />
			<form:errors path="releaseDate" />
		</div>
		
		<div>
			<label for="summary">Sumario do livro</label>
			<input type="file" name="summary" />
			<form:errors path="summaryPath" />
			
		</div>
		
		<div>
			<input type="submit" value="Enviar" />
		</div>
	</form:form>
</body>
</html>
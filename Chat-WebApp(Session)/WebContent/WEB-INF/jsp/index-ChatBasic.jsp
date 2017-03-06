<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Conversazione</title>
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

<!-- jQuery library -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>

<!-- Latest compiled JavaScript -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<!--  Form ajax plugin
 <script src="http://malsup.github.com/jquery.form.js"></script>
 -->
 <link href="${pageContext.request.contextPath}/resources/chatTheme/css/style.css" rel="stylesheet" />
<style>
#conversation {
	width: 500px;
	height: 500px;
	overflow-y: auto;
}
</style>
</head>

<body>
	<div class="container">
	<p>	${pageContext.request.contextPath}</p>
		<div style='text-align: left;' id='conversation'></div>
		<form id="formInput">
			<input type="text" name="input" id="input"> <input
				type="submit" value="Invia">
		</form>
	</div>
	<script type="text/javascript">
		$(document).ready(function() {
			var url_welcome = "welcome.html";
			$.ajax({
				type : "GET",
				url : url_welcome,
				success : function(data) {
					$('#conversation').append(data);
				}
			});

			$("#formInput").submit(function(e) {
				var url = "response.html";

				var input_val = $("#input").val();
				$("#input").val('');
				//alert(input_val);
				$('#conversation').append("<p> Tu >> " + input_val + "</p>");

				$.ajax({
					type : "GET",
					url : url,
					data : "input=" + input_val,
					success : function(data) {
						//alert(data);
						$('#conversation').append(data);
					}
				});

				e.preventDefault();
			});
		});
	</script>
</body>
</html>
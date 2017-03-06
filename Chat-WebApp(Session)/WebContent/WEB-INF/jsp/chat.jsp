<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>Fully responsive chat layout</title>
<!-- let's add tag srping:url -->
<spring:url value="/resources/chatTheme/css/style.css" var="chatCSS" />
<spring:url value="/resources/chatTheme/js/index.js" var="chatJS" />
<link href="${chatCSS}" rel="stylesheet" />
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
</head>
<body>
	<div class="container">
		<div id="device" text-allign="center">
			<div class="chat" id="conversation"></div>
			<span id="drag">Drag me!</span> <input id="size" type="range"
				min="350" max="1000" value="350" />
		</div>
		<div align="center">
			<form id="formInput">
				<input type="text" name="input" id="input"> <input
					type="submit" value="Invia">
			</form>
		</div>
	</div>
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
	<script src="${chatJS}"></script>
	<!-- Latest compiled JavaScript -->
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			var message = "<div class=\"message\"><img src=\"http://api.randomuser.me/portraits/med/men/66.jpg\" /><div>";
			var message_me = "<div class=\"message me\"><img src=\"http://api.randomuser.me/portraits/med/women/36.jpg\" /><div>";
			var close = "</div></div>";
			var url_welcome = "welcome.html";
			$.ajax({
				type : "GET",
				url : url_welcome,
				success : function(data) {
					$('#conversation').append(message + data + close);
					}
				});

			$("#formInput").submit(function(e) {
				var url = "response.html";

				var input_val = $("#input").val();
				$("#input").val('');
				//alert(input_val);
				$('#conversation').append(message_me + "<p>"+ input_val+ "</p>"+ close);

				$.ajax({
					type : "GET",
					url : url,
					data : "input="+ input_val,
					success : function(data) {
						//alert(data);
						$('#conversation').append(message+ data+ close);
						var objDiv = document.getElementById("conversation");
						objDiv.scrollTop = objDiv.scrollHeight;
						}
				});

		      e.preventDefault();
			});
	});
	</script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>Chat bot</title>
<!-- let's add tag srping:url 
<spring:url value="/resources/chatTheme/css/style.css" var="chatCSS" />
<spring:url value="/resources/chatTheme/js/index.js" var="chatJS" />-->
<link href="${pageContext.request.contextPath}/resources/chatTheme/css/style.css" rel="stylesheet" />
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
</head>
<body>
	<div class="container pippo">
		<div id="device" text-allign="center">
			<div class="chat" id="conversation"></div>
			<form id="formInput" class="form-group">
				<input type="text" name="input" id="input" class="form-control" autocomplete="off"> 
				<!-- <input type="submit" value="Invia" class="btn btn-default"> -->
				<span id="drag">Drag me!</span> <input id="size" type="range"
				min="350" max="1000" value="350" />
			</form>
		</div>
	</div>
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/chatTheme/js/index.js"></script>
	<!-- Latest compiled JavaScript -->
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#input").focus();
			var message = "<div class=\"message\"><img src=\"resources/image/bot.png\" /><div>";
			var message_me = "<div class=\"message me\"><img src=\"resources/image/client.jpg\" /><div>";
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
				scrollBottom('conversation');
				$.ajax({
					type : "GET",
					url : url,
					data : "input="+ input_val,
					success : function(data) {
						if(data!=""){
						$('#conversation').append(message+ data+ close);
						scrollBottom('conversation');
						}
					}
				});

		      e.preventDefault();
			});
	});
		function scrollBottom(id){
			var objDiv = document.getElementById(id);
			objDiv.scrollTop = objDiv.scrollHeight;
		}
	</script>
</body>
</html>
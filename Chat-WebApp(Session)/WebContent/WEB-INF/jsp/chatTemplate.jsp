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

  </head>
  <body>
    <div id="device">
  <div class="chat">
    <div class="message">
      <img src="http://api.randomuser.me/portraits/med/men/66.jpg" />
      <div><p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam eget pretium sapien, et gravida metus.</p></div>
    </div>
    <div class="message">
      <img src="http://api.randomuser.me/portraits/med/men/66.jpg" />
      <div><p>Aliquam gravida semper pharetra.</p></div>
    </div>
    <div class="message me">
      <img src="http://api.randomuser.me/portraits/med/women/36.jpg" />
      <div><p>
        Curabitur congue lorem quis dolor blandit hendrerit. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae;
        Vivamus bibendum efficitur tortor, non porttitor magna imperdiet in.
        </p></div>
    </div>
    <div class="message me">
      <img src="http://api.randomuser.me/portraits/med/women/36.jpg" />
      <div><p>Curabitur feugiat libero sed lacinia sollicitudin.</p></div>
    </div>
    <div class="message me">
      <img src="http://api.randomuser.me/portraits/med/women/36.jpg" />
      <div><p>Cras mollis nisl ac velit euismod ultrices.</p></div>
    </div>
    <div class="message">
      <img src="http://api.randomuser.me/portraits/med/men/66.jpg" />
      <div><p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam eget pretium sapien, et gravida metus.</p></div>
    </div>
    <div class="message">
      <img src="http://api.randomuser.me/portraits/med/men/66.jpg" />
      <div><p>Aliquam gravida semper pharetra.</p></div>
    </div>
  </div>

  
  <span id="drag">Drag me!</span>
  <input id="size" type="range" min="350" max="1000" value="350" />
</div>
<!-- This is for the preview at codepen.io :) -->
<div id="preview">     
  <div class="chat">
    <div class="message">
      <img src="http://api.randomuser.me/portraits/med/men/66.jpg" />
      <div><p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam eget pretium sapien, et gravida metus.</p></div>
    </div>
    <div class="message me">
      <img src="http://api.randomuser.me/portraits/med/women/36.jpg" />
      <div><p>
        Curabitur congue lorem quis dolor blandit hendrerit. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae;
        Vivamus bibendum efficitur tortor, non porttitor magna imperdiet in.
        </p></div>
    </div>
    
    <p id="text">Make you view height larger to see the full thing ;)</p>
  </div>
</div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script src="${chatJS}"></script>
  </body>
</html>

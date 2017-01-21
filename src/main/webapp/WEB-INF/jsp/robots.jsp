<%--
  Created by IntelliJ IDEA.
  User: fox
  Date: 15.01.17
  Time: 14:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="tasks" value="${['task.code','task.calc','task.discover','task.build','task.sing','task.suicide']}" scope="application" />

<html>
<jsp:include page="fragments/header.jsp"/>
<body>
<jsp:include page="fragments/bodyPart.jsp"/>

<!-- Page Content -->
<div class="container">

    <!-- Portfolio Item Heading -->
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">
                <small><spring:message code="robot.activity"/></small>
            </h1>
        </div>
    </div>
    <!-- /.row -->

    <!-- Portfolio Item Row -->
    <div class="row">

        <div class="col-md-8">
            <textarea class="form-control noresize" rows="25" id="logs" readonly style="resize: none" onclick="getNewLogs()"></textarea>
        </div>

        <div class="col-md-4">
            <h3><spring:message code="app.description"/></h3>
            <p><spring:message code="app.fulldesc"/> </p>
            <h3><spring:message code="app.details"/> </h3>
            <ul>
                <li>Java Multithreading</li>
                <li>Spring MVC</li>
                <li>REST / AJAX</li>
                <li>jQuery</li>
            </ul>
        </div>

    </div>
    <!-- /.row -->

    <!-- Related Projects Row -->
    <div class="row">

        <div class="col-lg-12">
            <h3 class="page-header"><spring:message code="robot.control"/></h3>
        </div>

        <div class="col-sm-2 col-xs-2">
            <a class="btn btn-md btn-info btn-block" onclick="openWindow('sendPersonalTask', 'name')"><spring:message code="robot.perstask"/></a>
        </div>

        <div class="col-sm-2 col-xs-2">
            <a class="btn btn-md btn-info btn-block" onclick="openNonSelectorWindow('sendBroadcastTask')"><spring:message code="robot.broadtask"/></a>
        </div>

        <div class="col-sm-2 col-xs-2">
            <a class="btn btn-md btn-success btn-block" onclick="openNonSelectorWindow('addRobot')"><spring:message code="robot.add"/></a>
        </div>

        <div class="col-sm-2 col-xs-2">
            <a class="btn btn-md btn-danger btn-block" onclick="openWindow('killRobot', 'suicide')"><spring:message code="robot.kill"/></a>
        </div>

    </div>

    <jsp:include page="popups/sendPersonalTask.jsp"/>

    <jsp:include page="popups/sendBroadcastTask.jsp"/>

    <jsp:include page="popups/addRobot.jsp"/>

    <jsp:include page="popups/killRobot.jsp"/>

    <jsp:include page="fragments/footer.jsp"/>

</div>
<!-- /.container -->
<script type="text/javascript" src="resources/js/ajaxScripts.js"></script>
</body>
</html>

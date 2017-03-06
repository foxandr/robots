<%--
  Created by IntelliJ IDEA.
  User: fox
  Date: 18.01.17
  Time: 13:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<hr>

<!-- Footer -->
<footer>
    <div class="row">
        <div class="col-lg-8">
            <p><spring:message code="app.footer"/></p>
        </div>
        <div class="col-lg-4">
            <p class="text-center">
                <select class="btn-xs" id="chLang" onchange="show()">
                    <option disabled selected value> <spring:message code="common.lang"/> </option>
                    <option value="en">English</option>
                    <option value="ru">Русский</option>
                </select>
            </p>
        </div>
    </div>
    <!-- /.row -->
</footer>

<script type="text/javascript" src="webjars/jquery/3.1.1/jquery.min.js"></script>
<script type="text/javascript" src="webjars/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script type="text/javascript" src="webjars/noty/2.3.8/js/noty/packaged/jquery.noty.packaged.min.js"></script>
<script type="text/javascript" src="resources/js/ajaxScripts.js"></script>
<script type="text/javascript" src="resources/js/commonScripts.js"></script>
<script type="text/javascript" src="resources/js/pageNotes.js"></script>
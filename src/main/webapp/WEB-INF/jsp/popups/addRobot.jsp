<%--
  Created by IntelliJ IDEA.
  User: fox
  Date: 19.01.17
  Time: 2:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="robots" value="${['type.code','type.calc','type.discover','type.build','type.sing']}" scope="application" />

<div class="modal fade" id="addRobot">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h2 class="modal-title"><spring:message code="robot.add"/></h2>
            </div>
            <div class="modal-body">
                <form:form class="form-horizontal" method="post" id="add" role="form">
                    <div class="form-group">
                        <label for="id" class="control-label col-xs-4"><spring:message code="robot.type"/></label>
                        <div class="col-xs-5">
                            <select class="form-control" id="id" name="id" placeholder="id">
                                <c:forEach begin="0" end="4" varStatus="loop">
                                    <option value="${loop.index}"><spring:message code="${robots[loop.index]}"/></option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-xs-offset-4 col-xs-5">
                            <button type="button" class="btn btn-success" onclick="addRobot()"><spring:message code="common.apply"/></button>
                            <button type="button" class="btn btn-warning" data-dismiss="modal" aria-hidden="true"><spring:message code="common.cancel"/></button>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>

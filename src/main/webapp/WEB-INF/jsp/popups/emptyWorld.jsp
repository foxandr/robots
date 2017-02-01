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

<div class="modal fade" id="emptyWorld">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h2 class="modal-title"><spring:message code="robot.empty"/></h2>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <h4 class="col-xs-6"><spring:message code="robot.emptytext"/></h4>
                    <button type="button" class="btn btn-warning" data-dismiss="modal" aria-hidden="true" id="cancel"><spring:message code="common.cancel"/></button>
                </div>
            </div>
        </div>
    </div>
</div>

<%--

    Copyright (C) 2011  JTalks.org Team
    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.
    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.
    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA

--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="jtalks" uri="http://www.jtalks.org/tags" %>
<html>
<head>
    <title><spring:message code="label.signup"/></title>
    <script src="${pageContext.request.contextPath}/resources/javascript/custom/captcha.js"
            type="text/javascript"></script>
</head>

<body>

<div class="container form-login-related">
    <form:form id="form" name="form" action='${pageContext.request.contextPath}/user/new'
            modelAttribute="newUser" method="POST" class='form-vertical'>
        <fieldset>
            <legend><spring:message code="label.fillmessage"/></legend>
          
            <div class="control-group">
                    <form:label path="username" class='control-label'> <spring:message code="label.username"/></form:label>
                    <div class="controls">
                            <form:input path="username" class="reg_input" type="text"/><br/>
                        <form:errors path="username" cssClass="help-inline"/>
                    </div>
                    <span class="reg_info"><spring:message code="label.tip.username"/></span>
            </div>
            <div class="control-group">
                    <form:label path="email" class='control-label'><spring:message code="label.email"/></form:label>
                    <div class="controls">
                            <form:input path="email" class="reg_input" type="text"/><br/>
                        <form:errors path="email" cssClass="help-inline"/>
                    </div>
                    <span class="reg_info"><spring:message code="label.tip.email"/></span>
            </div>
            <div class="control-group">
                    <form:label path="password" class='control-label'><spring:message code="label.password"/></form:label>

                    <div class="controls">
                            <form:input path="password" class="reg_input" type="password"/><br/>
                        <form:errors path="password" cssClass="help-inline"/>
                    </div>
                    <span class="reg_info"><spring:message code="label.tip.password"/></span>
            </div>
            <div class="control-group">
                    <form:label path="passwordConfirm" class='control-label'><spring:message code="label.confirmation"/></form:label>

                    <div class="controls">
                            <form:input path="passwordConfirm" class="reg_input" type="password"/><br/>
                        <form:errors path="passwordConfirm" cssClass="help-inline"/>
                    </div>
                    <span class="reg_info"><spring:message code="label.tip.confirmation"/></span>
            </div>
            <div class="control-group">
                    <form:label path="captcha" class='control-label'><spring:message code="label.captcha"/></form:label>

                    <div class="controls">
                        <img id="captcha_img" src='${pageContext.request.contextPath}/captcha/image' class="captcha"/>
                        <img id="captcha_refresh"
                             src='${pageContext.request.contextPath}/resources/images/captcha-refresh.gif'/>
                            <form:input path="captcha" class="captcha_input" type="text" id="captcha"/><br/>
                        <form:errors path="captcha" cssClass="help-inline"/>
                    </div>
                    <span class="reg_info"><spring:message code="label.tip.captcha"/></span>
            </div>
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">
                    <spring:message code="label.signup"/>
                </button>
            </div>
        </fieldset>
    </form:form>
</div>
</body>
</html>

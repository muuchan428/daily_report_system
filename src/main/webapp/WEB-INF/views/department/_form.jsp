<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="action" value="${ForwardConst.ACT_DEP.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />

<c:if test="${errors != null}">
    <div id="flush_error">
        入力内容にエラーがあります。<br />
        <c:forEach var="error" items="${errors}">
            ・<c:out value="${error}" /><br />
        </c:forEach>

    </div>
</c:if>
<label for="${AttributeConst.DEP_CODE.getValue()}">部署番号</label><br />
<input type="text" name="${AttributeConst.DEP_CODE.getValue()}" value="${department.code}" />
<br /><br />

<label for="${AttributeConst.DEP_NAME.getValue()}">部署名</label><br />
<input type="text" name="${AttributeConst.DEP_NAME.getValue()}" value="${department.name}" />
<br /><br />
<input type="hidden" name="${AttributeConst.DEP_ID.getValue()}" value="${department.id}" />
<input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
<button type="submit">投稿</button>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="action" value="${ForwardConst.ACT_EMP.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />

<c:if test="${errors != null}">
    <div id="flush_error">
        入力内容にエラーがあります。<br />
        <c:forEach var="error" items="${errors}">
            ・<c:out value="${error}" /><br />
        </c:forEach>

    </div>
</c:if>
<label for="${AttributeConst.EMP_CODE.getValue()}">社員番号</label><br />
<input type="text" name="${AttributeConst.EMP_CODE.getValue()}" value="${employee.code}" />
<br /><br />

<label for="${AttributeConst.EMP_L_NAME.getValue()}">姓</label><br />
<input type="text" name="${AttributeConst.EMP_L_NAME.getValue()}" value="${employee.lastName}" />
<br />
<label for="${AttributeConst.EMP_F_NAME.getValue()}">名</label><br />
<input type="text" name="${AttributeConst.EMP_F_NAME.getValue()}" value="${employee.firstName}" />

<br /><br />

<label for="${AttributeConst.EMP_PASS.getValue()}">パスワード</label><br />
<input type="password" name="${AttributeConst.EMP_PASS.getValue()}" />
<br /><br />

<label for="${AttributeConst.EMP_ADMIN_FLG.getValue()}">権限</label><br />
<select name="${AttributeConst.EMP_ADMIN_FLG.getValue()}">
    <option value="${AttributeConst.ROLE_GENERAL.getIntegerValue()}"<c:if test="${employee.adminFlag == AttributeConst.ROLE_GENERAL.getIntegerValue()}"> selected</c:if>>一般</option>
    <option value="${AttributeConst.ROLE_ADMIN.getIntegerValue()}"<c:if test="${employee.adminFlag == AttributeConst.ROLE_ADMIN.getIntegerValue()}"> selected</c:if>>管理者</option>
</select>
<br /><br />

<label for="${AttributeConst.EMP_DEP.getValue()}">部署</label><br />
<select name="${AttributeConst.EMP_DEP.getValue()}">
<option value="${department.id}" selected > --部署を選択--</option>
<c:forEach var="department"  items="${departments}">
    <option value="${department.id}"<c:if test="${employee.department.id == department.id}">selected </c:if>>${department.name}</option>
</c:forEach>

</select>
<br /><br />

<label for="${AttributeConst.EMP_STO.getValue()}">店舗</label><br />
<select name="${AttributeConst.EMP_STO.getValue()}">
<option value="${department.id}" selected > --店舗を選択--</option>
<c:forEach var="store"  items="${stores}">
    <option value="${store.id}"<c:if test="${employee.store.id == store.id}">selected</c:if>>${store.name}</option>
</c:forEach>
</select>
<br /><br />
<input type="hidden" name="${AttributeConst.EMP_ID.getValue()}" value="${employee.id}" />
<input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
<button type="submit">投稿</button>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="action" value="${ForwardConst.ACT_STO.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />

<c:if test="${errors != null}">
    <div id="flush_error">
        入力内容にエラーがあります。<br />
        <c:forEach var="error" items="${errors}">
            ・<c:out value="${error}" /><br />
        </c:forEach>

    </div>
</c:if>
<label for="${AttributeConst.STO_CODE.getValue()}">店舗番号</label><br />
<input type="text" name="${AttributeConst.STO_CODE.getValue()}" value="${store.code}" />
<br /><br />

<label for="${AttributeConst.STO_NAME.getValue()}">店舗名</label><br />
<input type="text" name="${AttributeConst.STO_NAME.getValue()}" value="${store.name}" />
<br /><br />
<input type="hidden" name="${AttributeConst.STO_ID.getValue()}" value="${store.id}" />
<input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
<button type="submit">投稿</button>
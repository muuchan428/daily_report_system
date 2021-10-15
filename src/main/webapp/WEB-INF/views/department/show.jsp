<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="constants.ForwardConst" %>
<%@ page import="constants.AttributeConst" %>

<c:set var="actDep" value="${ForwardConst.ACT_DEP.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commCreate" value="${ForwardConst.CMD_CREATE.getValue()}" />
<c:set var="commDest" value="${ForwardConst.CMD_DESTROY.getValue() }"/>
<c:set var="commEdit" value="${ForwardConst.CMD_EDIT.getValue()}" />
<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />


<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2> ${department.name} の 詳細ページ</h2>


         <table>
            <tbody>
                <tr>
                    <th>部署番号</th>
                    <td><c:out value="${department.code}" /></td>
                </tr>
                <tr>
                    <th>部署名</th>
                    <td><c:out value="${department.name}" /></td>
                </tr>
            </tbody>
        </table>
         <p>
                     <a href="<c:url value='?action=${actDep}&command=${commEdit}&id=${department.id}' />">この部署情報を編集する</a>
                </p>
<a href="javascript:history.back()">戻る</a>
    </c:param>
</c:import>
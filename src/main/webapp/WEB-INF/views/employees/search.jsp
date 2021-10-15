<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actEmp" value="${ForwardConst.ACT_EMP.getValue()}" />
<c:set var="actDep" value="${ForwardConst.ACT_DEP.getValue()}" />
<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />
<c:set var="commNew" value="${ForwardConst.CMD_NEW.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:choose>
<c:when test="${errors != null}">
    <div id="flush_error">
            ・<c:out value="${errors}" /><br />
     </div>
 </c:when>

        <c:when test= "${emplyoees.length != 0}">
                <h2>${word} の検索結果</h2>
        <table id="employee_list">
            <tbody>
                <tr>
                    <th>社員番号</th>
                    <th>氏名</th>
                    <th>部署</th>
                    <th>店舗</th>
                    <th>操作</th>
                </tr>
                <c:forEach var="employee" items="${employees}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td><c:out value="${employee.code}" /></td>
                        <td><c:out value="${employee.lastName}" />&nbsp;<c:out value="${employee.firstName}" /></td>
                        <td><c:out value="${employee.department.name}"/></td>
                        <td><c:out value="${employee.store.name}"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${employee.deleteFlag == AttributeConst.DEL_FLAG_TRUE.getIntegerValue()}">
                                    （削除済み）
                                </c:when>
                                <c:otherwise>
                                    <a href="<c:url value='?action=${actEmp}&command=${commShow}&id=${employee.id}' />">詳細を見る</a>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        </c:when>
        <c:otherwise>
    <h2>${word}　に該当する従業員はいませんでした。</h2>


        </c:otherwise>
    </c:choose>


    </c:param>
</c:import>

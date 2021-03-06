<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actDep" value="${ForwardConst.ACT_DEP.getValue()}" />
<c:set var="actSto" value="${ForwardConst.ACT_STO.getValue()}" />
<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />
<c:set var="commNew" value="${ForwardConst.CMD_NEW.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2>部署・店舗　一覧</h2>
        <table id="department_list">
            <tbody>
                <tr>
                    <th>部署番号</th>
                    <th>部署名</th>
                    <th>操作</th>
                </tr>
                <c:forEach var="department" items="${departments}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td><c:out value="${department.code}" /></td>
                        <td><c:out value="${department.name}" /></td>
                        <td>
                        <a href="<c:url value='?action=${actDep}&command=${commShow}&id=${department.id}' />">詳細を見る</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            （全 ${departments_count} 件）<br />
            <c:forEach var="i" begin="1" end="${((departments_count - 1) / maxRow) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='?action=${actDep}&command=${commIdx}&page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p><a href="<c:url value='?action=${actDep}&command=${commNew}' />">新規部署の登録</a></p>

        <table id="store_list">
            <tbody>
                <tr>
                    <th>店舗番号</th>
                    <th>店舗名</th>
                    <th>操作</th>
                </tr>
                <c:forEach var="store" items="${stores}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td><c:out value="${store.code}" /></td>
                        <td><c:out value="${store.name}" /></td>
                        <td>
                        <a href="<c:url value='?action=${actSto}&command=${commShow}&id=${store.id}' />">詳細を見る</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            （全 ${stores_count} 件）<br />
            <c:forEach var="i" begin="1" end="${((stores_count - 1) / maxRow) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == storePage}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='?action=${actDep}&command=${commIdx}&storePage=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p><a href="<c:url value='?action=${actSto}&command=${commNew}' />">新規店舗の登録</a></p>

    </c:param>
</c:import>

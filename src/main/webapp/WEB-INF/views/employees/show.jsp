<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="constants.ForwardConst" %>
<%@ page import="constants.AttributeConst" %>

<c:set var="actEmp" value="${ForwardConst.ACT_EMP.getValue()}" />
<c:set var="actFol" value="${ForwardConst.ACT_FOL.getValue()}" />
<c:set var="actRep" value="${ForwardConst.ACT_REP.getValue()}" />
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
        <h2>${employee.lastName}&nbsp; ${employee.firstName}さん の 詳細ページ</h2>

        <c:if test="${login_employee.id.equals(employee.id) == false}">
        <c:choose>
            <c:when test= "${follow_check == false}">
             <div class ="follow_button">
       <a href="#" onclick="confirmCreate();">フォロー</a>
       </div>
       <form method="POST"
            action="<c:url value='?action=${actFol}&command=${commCreate}' />">
            <input type="hidden" name="${AttributeConst.EMP_ID.getValue()}" value="${employee.id}" />
            <input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />

        </form>
        <script>
            function confirmCreate() {
                if (confirm("フォローしますか？")) {
                    document.forms[0].submit();
                }
            }
        </script>
        </c:when>
        <c:otherwise>
            <div class ="follow_button">
               <a href="#"  onclick="confirmDestroy();">フォロー解除</a>
               </div>
       <form method="POST"
            action="<c:url value='?action=${actFol}&command=${commDest}' />">
            <input type="hidden" name="${AttributeConst.EMP_ID.getValue()}" value="${employee.id}" />
            <input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
        </form>
        <script>
            function confirmDestroy() {
                if (confirm("フォロー解除しますか？")) {
                    document.forms[0].submit();
                }
            }
        </script>
        </c:otherwise>
        </c:choose>
        </c:if>
        <h3>従業員詳細</h3>

        <table>
            <tbody>
                <tr>
                    <th>社員番号</th>
                    <td><c:out value="${employee.code}" /></td>
                </tr>
                <tr>
                    <th>氏名</th>
                    <td><c:out value="${employee.lastName}" />&nbsp;<c:out value="${employee.firstName}" /></td>
                </tr>

                <tr>
                                <tr>
                    <th>部署</th>
                    <td><c:out value="${employee.department.name}" /></td>
                </tr>
                 <tr>
                    <th>店舗</th>
                    <td><c:out value="${employee.store.name}" /></td>
                </tr>
                    <th>権限</th>
                    <td><c:choose>
                            <c:when test="${employee.adminFlag == AttributeConst.ROLE_ADMIN.getIntegerValue()}">管理者</c:when>
                            <c:otherwise>一般</c:otherwise>
                        </c:choose></td>
                </tr>

                <tr>
                    <th>登録日時</th>
                    <fmt:parseDate value="${employee.createdAt}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="createDay" type="date" />
                    <td><fmt:formatDate value="${createDay}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                </tr>
                <tr>
                    <th>更新日時</th>
                    <fmt:parseDate value="${employee.updatedAt}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="updateDay" type="date" />
                    <td><fmt:formatDate value="${updateDay}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                </tr>
            </tbody>
        </table>

        <h3>日報一覧</h3>
          <table id="report_list">
            <tbody>
                <tr>
                    <th class="report_date">日付</th>
                    <th class="report_title">タイトル</th>
                    <th class="report_action">操作</th>
                </tr>
                <c:forEach var="report" items="${reports}" varStatus="status">
                    <fmt:parseDate value="${report.reportDate}" pattern="yyyy-MM-dd" var="reportDay" type="date" />
                    <tr class="row${status.count % 2}">

                        <td class="report_date"><fmt:formatDate value='${reportDay}' pattern='yyyy-MM-dd' /></td>
                        <td class="report_title">${report.title}</td>
                        <td class="report_action"><a href="<c:url value='?action=${actRep}&command=${commShow}&id=${report.id}' />">詳細を見る</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            （全 ${reports_count} 件）<br />
            <c:forEach var="i" begin="1" end="${((reports_count - 1) / maxRow) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='?action=${actTop}&command=${commIdx}&id=${employee.id}&page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>

        <c:if test="${login_employee.adminFlag == AttributeConst.ROLE_ADMIN.getIntegerValue()}">
                <p>
                     <a href="<c:url value='?action=${actEmp}&command=${commEdit}&id=${employee.id}' />">この従業員情報を編集する</a>
                </p>
          </c:if>
        <p>
<a href="javascript:history.back()">戻る</a>

        </p>
    </c:param>
</c:import>
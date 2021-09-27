package services;

import java.util.List;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import actions.views.FollowConverter;
import actions.views.FollowView;
import actions.views.ReportConverter;
import actions.views.ReportView;
import constants.JpaConst;
import models.Employee;
import models.Follow;
import models.Report;

public class FollowService extends ServiceBase {
    /**
     * 指定した従業員のフォローする従業員データを、指定されたページ数の一覧画面に表示する分取得し、FollowViewのリストで返却する
     * @param employee
     * @param page
     * @return 一覧画面に表示するデータのリスト
     */
    public List<EmployeeView> getFollowEmployeesPerPage(EmployeeView employee, int page) {

        List<Employee>    employees = em.createNamedQuery(JpaConst.Q_EMP_GET_ALL_FOL, Employee.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        return EmployeeConverter.toViewList(employees);
    }
    /**
     * 指定した従業員のフォロー件数を取得し、返却する
     * @param employee
     * @return フォローの件数
     */
    public long countAllFollowEmployees(EmployeeView employee) {

        long count = (long) em.createNamedQuery(JpaConst.Q_FOL_COUNT_ALL_MINE, Long.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .getSingleResult();

        return count;
    }
    /**
     * 指定されたページ数の一覧画面に表示する、指定された従業員がフォローしている従業員の日報データを取得し、ReportViewのりすとで返却する
     *@param employee
     * @param ページ数
     * @return  一覧画面に表示するデータのリスト
     */
    public List<ReportView> getFollowReportsPerPage(EmployeeView employee, int page) {

        List<Report> reports = em.createNamedQuery(JpaConst.Q_REP_GET_ALL_FOL, Report.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        return ReportConverter.toViewList(reports);
    }
    /**
     * 指定した従業員が作成した日報データの件数を取得し、返却する
     * @param employee
     * @return 日報データの件数
     */
    public long countAllFollowReports(EmployeeView employee) {

        long count = (long) em.createNamedQuery(JpaConst.Q_REP_COUNT_ALL_FOL, Long.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .getSingleResult();

        return count;
    }

    /**
     * 指定した従業員がフォローしているかを取得し、データがあればTRUEでかえす
     *
     */
    public Boolean checkFollow(EmployeeView employee, EmployeeView follow_employee) {

        Follow follows = (Follow)em.createNamedQuery(JpaConst.Q_FOL_REMOVE_FOL_EMP,Follow.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE,EmployeeConverter.toModel(employee))
                .setParameter(JpaConst.JPQL_PARM_FOLLOW, EmployeeConverter.toModel(follow_employee));

        if(follows == null) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * 登録内容を元にデータを1件作成し、フォローテーブルに登録する
     * @param fv follow登録内容
     *
     */
    public void create(FollowView fv) {

            createInternal(fv);
        }


    /**
     * 指定した従業員がフォロー解除したフォローデータを削除する
     * @param employee
     * @param follow_employee
     */
    public void removeFollowEmployee(EmployeeView employee, EmployeeView follow_employee) {

        Follow follows = (Follow)em.createNamedQuery(JpaConst.Q_FOL_REMOVE_FOL_EMP,Follow.class)
                                            .setParameter(JpaConst.JPQL_PARM_EMPLOYEE,EmployeeConverter.toModel(employee))
                                            .setParameter(JpaConst.JPQL_PARM_FOLLOW, EmployeeConverter.toModel(follow_employee));
        em.getTransaction().begin();
        em.remove(follows);
        em.getTransaction().commit();

    }
  /**
     * フォローデータを１件登録する
     * @param fv フォローデータ
     */
    private void createInternal(FollowView fv) {

        em.getTransaction().begin();
        em.persist(FollowConverter.toModel(fv));
        em.getTransaction().commit();
    }
}

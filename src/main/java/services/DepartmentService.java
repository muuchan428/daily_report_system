package services;


import java.util.List;

    import javax.persistence.NoResultException;

    import actions.views.DepartmentConverter;
    import actions.views.DepartmentView;
import constants.JpaConst;
    import models.Department;
import models.validators.DepartmentValidator;

public class DepartmentService extends ServiceBase {
    /**
     * すべての部署データの件数を取得
     * @return
     */
    public long countAll() {
        long depCount = (long)em.createNamedQuery(JpaConst.Q_DEP_COUNT_ALL, Long.class)
                                    .getSingleResult();
        return depCount;
    }
   /**
    * ページに表示する部署データを取得
    * @param page
    * @return
    */
    public List<DepartmentView> getPerPage(int page){
        List<Department> departments = em.createNamedQuery(JpaConst.Q_DEP_GET_ALL, Department.class)
                                                                      .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                                                                      .setMaxResults(JpaConst.ROW_PER_PAGE)
                                                                      .getResultList();
        return DepartmentConverter.toViewList(departments);
    }
    /**
     * idを条件にデータを一件取得しDepartmentViewのインスタンスで返却する
     * @param id
     * @return
     */
    public DepartmentView findOne(int id) {
        Department d = findOneInternal(id);
        return DepartmentConverter.toView(d);
    }
    /**
     *  idを条件にデータを1件取得し、Departmentのインスタンスで返却する
     * @param id
     * @return
     */
    public Department  findOneInternal(int id) {
        Department d = em.find(Department.class, id);
        return d;
    }
    /**
     * データを一件登録する
     * @param dv
     * @return
     */
    public List<String> create(DepartmentView dv) {
        List<String> errors = DepartmentValidator.validate(this,dv,true);
        if (errors.size() == 0) {
            createInternal(dv);
        }
        return errors;
    }
    private void createInternal(DepartmentView dv) {

        em.getTransaction().begin();
        em.persist(DepartmentConverter.toModel(dv));
        em.getTransaction().commit();
    }

    /**
     * 社員番号を条件に該当するデータの件数を取得し、返却する
     * @param code 社員番号
     * @return 該当するデータの件数
     */
    public long countByCode(String  code) {

        //指定した社員番号を保持する従業員の件数を取得する
        long departments_count = (long) em.createNamedQuery(JpaConst.Q_DEP_COUNT_RESISTERED_BY_CODE, Long.class)
                .setParameter(JpaConst.JPQL_PARM_CODE, code)
                .getSingleResult();
        return departments_count;
    }


    /**
     * 画面から入力された部署の更新内容を元にデータを1件作成し、部署テーブルを更新する
     * @param ev 画面から入力された部署の登録内容
     * @param pepper pepper文字列
     * @return バリデーションや更新処理中に発生したエラーのリスト
     */
    public List<String> update(DepartmentView dv) {

        //idを条件に登録済みの部署情報を取得する
        DepartmentView savedDep = findOne(dv.getId());

        boolean validateCode = false;
        if (!savedDep.getCode().equals(dv.getCode())) {
            //部署番号を更新する場合

            //社員番号についてのバリデーションを行う
            validateCode = true;
            //変更後の社員番号を設定する
            savedDep.setCode(dv.getCode());
        }


        savedDep.setName(dv.getName()); //変更後の部署名を設定する

        //更新内容についてバリデーションを行う
        List<String> errors = DepartmentValidator.validate(this, savedDep, validateCode);

        //バリデーションエラーがなければデータを更新する
        if (errors.size() == 0) {
            update(savedDep);
        }

        //エラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }
    /**
     * 部署データを一件削除する
     * @param department
     */
    public void removeDepartment(DepartmentView department) {

        Department dep = (Department)em.createNamedQuery(JpaConst.Q_DEP_GET_BY_ID,Department.class)
                                            .setParameter(JpaConst.JPQL_PARM_EMPLOYEE,DepartmentConverter.toModel(department))
                                            .getSingleResult();


        em.getTransaction().begin();
        em.remove(dep);
        em.getTransaction().commit();

    }

}

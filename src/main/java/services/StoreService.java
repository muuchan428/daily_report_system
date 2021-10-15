package services;


import java.util.List;

import actions.views.DepartmentConverter;
import actions.views.DepartmentView;
import actions.views.StoreConverter;
    import actions.views.StoreView;
import constants.JpaConst;
import models.Department;
import models.Store;
import models.validators.StoreValidator;

public class StoreService extends ServiceBase {
    /**
     * すべての店舗データの件数を取得
     * @return
     */
    public long countAll() {
        long storeCount = (long)em.createNamedQuery(JpaConst.Q_STO_COUNT_ALL, Long.class)
                                    .getSingleResult();
        return storeCount;
    }

    public List<StoreView> getAllStore(){
        List<Store> stores = em.createNamedQuery(JpaConst.Q_STO_GET_ALL, Store.class)
                                                                 .getResultList();
        return StoreConverter.toViewList(stores);

    }
   /**
    * ページに表示する店舗データを取得
    * @param page
    * @return
    */
    public List<StoreView> getPerPage(int page){
        List<Store> stores = em.createNamedQuery(JpaConst.Q_STO_GET_ALL,Store.class)
                                                                      .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                                                                      .setMaxResults(JpaConst.ROW_PER_PAGE)
                                                                      .getResultList();
        return StoreConverter.toViewList(stores);
    }
    /**
     * idを条件にデータを一件取得しStoreViewのインスタンスで返却する
     * @param id
     * @return
     */
    public StoreView findOne(int id) {
        Store s = findOneInternal(id);
        return StoreConverter.toView(s);
    }
    /**
     *  idを条件にデータを1件取得し、Storeのインスタンスで返却する
     * @param id
     * @return
     */
    public Store  findOneInternal(int id) {
        Store s = em.find(Store.class, id);
        return s;
    }
    /**
     * データを一件登録する
     * @param sv
     * @return
     */
    public List<String> create(StoreView sv) {
        List<String> errors = StoreValidator.validate(this, sv, true);
        if (errors.size() == 0) {
            createInternal(sv);
        }
        return errors;
    }
    private void createInternal(StoreView sv) {

        em.getTransaction().begin();
        em.persist(StoreConverter.toModel(sv));
        em.getTransaction().commit();
    }

    /**
     * 店舗番号を条件に該当するデータの件数を取得し、返却する
     * @param code 店舗番号
     * @return 該当するデータの件数
     */
    public long countByCode(String  code) {

        //指定した店舗番号を保持する従業員の件数を取得する
        long storesCount = (long) em.createNamedQuery(JpaConst.Q_STO_COUNT_RESISTERED_BY_CODE, Long.class)
                .setParameter(JpaConst.JPQL_PARM_CODE, code)
                .getSingleResult();
        return storesCount;
    }


    /**
     * 画面から入力された部署の更新内容を元にデータを1件作成し、部署テーブルを更新する
     * @param ev 画面から入力された部署の登録内容
     * @param pepper pepper文字列
     * @return バリデーションや更新処理中に発生したエラーのリスト
     */
    public List<String> update(StoreView sv) {

        //idを条件に登録済みの部署情報を取得する
        StoreView savedStore = findOne(sv.getId());

        boolean validateCode = false;
        if (!savedStore.getCode().equals(sv.getCode())) {
            //店舗番号を更新する場合

            //店舗番号についてのバリデーションを行う
            validateCode = true;
            //変更後の店舗番号を設定する
            savedStore.setCode(sv.getCode());
        }


        savedStore.setName(sv.getName()); //変更後の店舗名を設定する

        //更新内容についてバリデーションを行う
        List<String> errors = StoreValidator.validate(this, savedStore, validateCode);

        //バリデーションエラーがなければデータを更新する
        if (errors.size() == 0) {
            updateInternal(savedStore);
        }

        //エラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }
    /**
     * 店舗データを一件削除する
     * @param store
     */
    public void removeDepartment(StoreView store) {

        Store sto =(Store)em.createNamedQuery(JpaConst.Q_STO_GET_BY_ID,Store.class)
                                            .setParameter(JpaConst.JPQL_PARM_EMPLOYEE,store.getId())
                                            .getSingleResult();


        em.getTransaction().begin();
        em.remove(sto);
        em.getTransaction().commit();

    }
    /**
     * データを更新する
     * @param sv 画面から入力された店舗の登録内容
     */
    private void updateInternal(StoreView sv) {

        em.getTransaction().begin();
       Store s = findOneInternal(sv.getId());
        StoreConverter.copyViewToModel(s, sv);
        em.getTransaction().commit();

    }


}

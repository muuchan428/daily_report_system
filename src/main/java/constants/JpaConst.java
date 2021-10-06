package constants;
    /**
     * DB関連の項目値を定義するインターフェース
     * ※インターフェイスに定義した変数は public static final 修飾子がついているとみなされる
     */

public interface JpaConst {


        //persistence-unit名
        String PERSISTENCE_UNIT_NAME = "daily_report_system";

        //データ取得件数の最大値
        int ROW_PER_PAGE = 15; //1ページに表示するレコードの数

        //従業員テーブル
        String TABLE_EMP = "employees"; //テーブル名
        //従業員テーブルカラム
        String EMP_COL_ID = "id"; //id
        String EMP_COL_CODE = "code"; //社員番号
        String EMP_COL_NAME = "name"; //氏名
        String EMP_COL_PASS = "password"; //パスワード
        String EMP_COL_ADMIN_FLAG = "admin_flag"; //管理者権限
        String EMP_COL_CREATED_AT = "created_at"; //登録日時
        String EMP_COL_UPDATED_AT = "updated_at"; //更新日時
        String EMP_COL_DELETE_FLAG = "delete_flag"; //削除フラグ

        int ROLE_ADMIN = 1; //管理者権限ON(管理者)
        int ROLE_GENERAL = 0; //管理者権限OFF(一般)
        int EMP_DEL_TRUE = 1; //削除フラグON(削除済み)
        int EMP_DEL_FALSE = 0; //削除フラグOFF(現役)

        //日報テーブル
        String TABLE_REP = "reports"; //テーブル名
        //日報テーブルカラム
        String REP_COL_ID = "id"; //id
        String REP_COL_EMP = "employee_id"; //日報を作成した従業員のid
        String REP_COL_REP_DATE = "report_date"; //いつの日報かを示す日付
        String REP_COL_TITLE = "title"; //日報のタイトル
        String REP_COL_CONTENT = "content"; //日報の内容
        String REP_COL_CREATED_AT = "created_at"; //登録日時
        String REP_COL_UPDATED_AT = "updated_at"; //更新日時

        //フォローテーブル
        String TABLE_FOL = "follows";//テーブル名
        //フォローテーブルカラム
        String FOL_COL_ID = "id";//id
        String FOL_COL_EMP = "employee_id";//フォローする従業員のid
        String FOL_COL_FOL_EMP = "follow_employee_id";//フォローされる従業員のid

       //部署テーブル
        String TABLE_DEP ="departments";//テーブル名
        //部署テーブルカラム
        String DEP_COL_ID ="id";//id
        String DEP_COL_CODE = "code";//部署番号
        String DEP_COL_NAME = "name";//部署名
        //店舗テーブル
        String TABLE_STO = "stores";//テーブル名
        //店舗テーブルカラム
        String STO_COL_ID = "id";//id
        String STO_COL_CODE = "code";//店舗番号
        String STO_COL_NAME = "name";//店舗名


        //Entity名
        String ENTITY_EMP = "employee"; //従業員
        String ENTITY_REP = "report"; //日報
        String ENTITY_FOL = "follow";//フォロー
        String ENTITY_DEP = "department";//部署
        String ENTITY_STO = "store";//店舗



        //JPQL内パラメータ
        String JPQL_PARM_CODE = "code"; //社員番号
        String JPQL_PARM_PASSWORD = "password"; //パスワード
        String JPQL_PARM_EMPLOYEE = "employee"; //従業員
        String JPQL_PARM_FOLLOW = "follow_employee";//フォローされた従業員
        String JPQL_PARM_DEPARTMENT = "department";//部署

        //NamedQueryの nameとquery
        //全ての従業員をidの降順に取得する
        String Q_EMP_GET_ALL = ENTITY_EMP + ".getAll"; //name
        String Q_EMP_GET_ALL_DEF = "SELECT e FROM Employee AS e ORDER BY e.id DESC"; //query
        //全ての従業員の件数を取得する
        String Q_EMP_COUNT = ENTITY_EMP + ".count";
        String Q_EMP_COUNT_DEF = "SELECT COUNT(e) FROM Employee AS e";
        //社員番号とハッシュ化済パスワードを条件に未削除の従業員を取得する
        String Q_EMP_GET_BY_CODE_AND_PASS = ENTITY_EMP + ".getByCodeAndPass";
        String Q_EMP_GET_BY_CODE_AND_PASS_DEF = "SELECT e FROM Employee AS e WHERE e.deleteFlag = 0 AND e.code = :" + JPQL_PARM_CODE + " AND e.password = :" + JPQL_PARM_PASSWORD;
        //指定した社員番号を保持する従業員の件数を取得する
        String Q_EMP_COUNT_RESISTERED_BY_CODE = ENTITY_EMP + ".countRegisteredByCode";
        String Q_EMP_COUNT_RESISTERED_BY_CODE_DEF = "SELECT COUNT(e) FROM Employee AS e WHERE e.code = :" + JPQL_PARM_CODE;
        //全ての日報をidの降順に取得する
        String Q_REP_GET_ALL = ENTITY_REP + ".getAll";
        String Q_REP_GET_ALL_DEF = "SELECT r FROM Report AS r ORDER BY r.id DESC";
        //全ての日報の件数を取得する
        String Q_REP_COUNT = ENTITY_REP + ".count";
        String Q_REP_COUNT_DEF = "SELECT COUNT(r) FROM Report AS r";
        //指定した従業員が作成した日報を全件idの降順で取得する
        String Q_REP_GET_ALL_MINE = ENTITY_REP + ".getAllMine";
        String Q_REP_GET_ALL_MINE_DEF = "SELECT r FROM Report AS r WHERE r.employee = :" + JPQL_PARM_EMPLOYEE + " ORDER BY r.id DESC";
        //指定した従業員が作成した日報の件数を取得する
        String Q_REP_COUNT_ALL_MINE = ENTITY_REP + ".countAllMine";
        String Q_REP_COUNT_ALL_MINE_DEF = "SELECT COUNT(r) FROM Report AS r WHERE r.employee = :" + JPQL_PARM_EMPLOYEE;
        //指定した従業員のフォロー一覧を全件idの降順で取得する
        String Q_FOL_GET_ALL_MINE = ENTITY_FOL  + ".getAllMine";
        String Q_FOL_GET_ALL_MINE_DEF = "SELECT f.follow_employee FROM Follow AS f WHERE f.employee = :" + JPQL_PARM_EMPLOYEE ;
      //指定した従業員のフォロー件数を取得する
        String Q_FOL_COUNT_ALL_MINE = ENTITY_FOL + ".countAllMine";
        String Q_FOL_COUNT_ALL_MINE_DEF = "SELECT COUNT(f) FROM Follow AS f WHERE f.employee = : " + JPQL_PARM_EMPLOYEE;
      //指定した従業員のフォローしている従業員の日報を全件idの降順で取得する
        String Q_REP_GET_ALL_FOL = ENTITY_REP + ".getAllFollow";
        String Q_REP_GET_ALL_FOL_DEF = "SELECT r FROM Report AS r WHERE r.employee IN ("+ Q_FOL_GET_ALL_MINE_DEF + ")";
        //指定した従業員のフォローしている従業員の日報の件数を取得する
        String Q_REP_COUNT_ALL_FOL = ENTITY_REP + ".countAllFollow";
        String Q_REP_COUNT_ALL_FOL_DEF = "SELECT COUNT(r) FROM Report AS r WHERE r.employee  IN ("+ Q_FOL_GET_ALL_MINE_DEF + ")";
       //指定した従業員のフォローしている従業員を全件idの降順で取得する
        String Q_EMP_GET_ALL_FOL = ENTITY_EMP + ".getAllFollow";
        String Q_EMP_GET_ALL_FOL_DEF = "SELECT e FROM Employee AS e WHERE e.id IN (" + Q_FOL_GET_ALL_MINE_DEF + ") ORDER BY e.id DESC";
        //指定した従業員のフォローを解除するデータを取得する
        String Q_FOL_REMOVE_FOL_EMP = ENTITY_FOL + ".deleteFollowEmployee";
        String Q_FOL_REMOVE_FOL_EMP_DEF = "SELECT f  FROM Follow AS f WHERE f.employee = :" + JPQL_PARM_EMPLOYEE + " AND f.follow_employee = :" + JPQL_PARM_FOLLOW;
        //指定した従業員とフォローされる従業員のデータがあるか取得する
        String Q_FOL_CHECK_FOL_EMP = ENTITY_FOL + ".checkFollowEmployee";
        String Q_FOL_CHECK_FOL_EMP_DEF = "SELECT COUNT(f) FROM Follow AS f WHERE f.employee = :" + JPQL_PARM_EMPLOYEE + " AND f.follow_employee = :" + JPQL_PARM_FOLLOW;
        //すべての部署データをid順に降順に取得
        String Q_DEP_GET_ALL = ENTITY_DEP + ".getAll";
        String Q_DEP_GET_ALL_DEF = "SELECT d FROM Department AS d ORDER BY d.id DESC";
        //部署データの件数を取得する
        String Q_DEP_COUNT_ALL = ENTITY_DEP + ".countAll";
        String Q_DEP_COUNT_ALL_DEF ="SELECT COUNT(d) FROM Department AS d";
        //指定した部署を取得する
        String Q_DEP_GET_BY_ID = ENTITY_DEP + ".getEmpDep";
        String Q_DEP_GET_BY_ID_DEF = "SELECT d FROM Department AS d WHERE d.id = :" + JPQL_PARM_EMPLOYEE;
        //指定した部署番号を保持する部署の件数を取得
        String Q_DEP_COUNT_RESISTERED_BY_CODE = ENTITY_DEP + ".countRegisteredByCode";
        String Q_DEP_COUNT_RESISTERED_BY_CODE_DEF = "SELECT COUNT(d) FROM Department AS d WHERE d.code = :" + JPQL_PARM_CODE;
        //指定した部署に所属している従業員の件数を取得する
        String Q_EMP_COUNT_IN_DEP = ENTITY_EMP +".countDep";
        String Q_EMP_COUNT_IN_DEP_DEF = "SELECT COUNT(e) FROM Employee AS e WHERE e.department_id = :" + JPQL_PARM_DEPARTMENT;
}

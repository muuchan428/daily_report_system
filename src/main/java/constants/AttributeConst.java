package constants;
/**
 * 画面の項目値等を定義するEnumクラス
 *
 *
 *
 */
public enum AttributeConst {

    //フラッシュメッセージ
    FLUSH("flush"),

    //一覧画面共通
    MAX_ROW("maxRow"),
    PAGE("page"),
    STO_PAGE("storePage"),

    //入力フォーム共通
    TOKEN("_token"),
    ERR("errors"),

    //ログイン中の従業員
    LOGIN_EMP("login_employee"),

    //ログイン画面
    LOGIN_ERR("loginError"),

    //従業員管理
    EMPLOYEE("employee"),
    EMPLOYEES("employees"),
    EMP_COUNT("employees_count"),
    EMP_ID("id"),
    EMP_CODE("code"),
    EMP_PASS("password"),
    EMP_F_NAME("firstName"),
    EMP_L_NAME("lastName"),
    EMP_ADMIN_FLG("admin_flag"),
    EMP_DEP("department"),
    EMP_STO("store"),

    //管理者フラグ
    ROLE_ADMIN(1),
    ROLE_GENERAL(0),

    //削除フラグ
    DEL_FLAG_TRUE(1),
    DEL_FLAG_FALSE(0),

    //日報管理
    REPORT("report"),
    REPORTS("reports"),
    REP_COUNT("reports_count"),
    REP_ID("id"),
    REP_DATE("report_date"),
    REP_TITLE("title"),
    REP_CONTENT("content"),

    //フォロー管理
    FOLLOW("follow"),
    FOLLOWS("follows"),
    FOL_COUNT("follow_count"),
    FOL_ID("id"),
    FOL_EMP("follow_employee"),
    FOL_CHECK("follow_check"),

    //部署、店舗管理
    DEPARTMENT("department"),
    DEPARTMENTS("departments"),
    DEP_COUNT("departments_count"),
    DEP_ID("id"),
    DEP_CODE("department_code"),
    DEP_NAME("department_name"),

    STORE("store"),
    STORES("stores"),
    STO_COUNT("stores_count"),
    STO_ID("id"),
    STO_CODE("store_code"),
    STO_NAME("store_name"),

    //検索関連
    SEARCH_WORD("word"),
    SEARCH_RESULT("result");


    private final String text;
    private final Integer i;

    private AttributeConst(final String text) {
        this.text = text;
        this.i = null;

    }

    private AttributeConst(final Integer i) {
        this.text = null;
        this.i = i;
    }

    public String getValue() {
        return this.text;
    }

    public Integer getIntegerValue() {
        return this.i;
    }

}

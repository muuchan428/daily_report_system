package constants;

public enum ForwardConst {

    //action
    ACT("action"),
    ACT_TOP("Top"),
    ACT_EMP("Employee"),
    ACT_REP("Report"),
    ACT_FOL("Follow"),
    ACT_DEP("Department"),
    ACT_STO("Store"),
    ACT_AUTH("Auth"),

    //command

    CMD("command"),
    CMD_NONE(""),
    CMD_INDEX("index"),
    CMD_SHOW("show"),
    CMD_SHOW_LOGIN("showLogin"),
    CMD_LOGIN("login"),
    CMD_LOGOUT("logout"),
    CMD_NEW("entryNew"),
    CMD_CREATE("create"),
    CMD_EDIT("edit"),
    CMD_UPDATE("update"),
    CMD_DESTROY("destroy"),
    CMD_SEARCH("search"),

    //id
    ID("id"),


    //jsp
    FW_ERR_UNKNOWN("error/unknown"),
    FW_TOP_INDEX("topPage/index"),
    FW_LOGIN("login/login"),
    FW_EMP_INDEX("employees/index"),
    FW_EMP_SHOW("employees/show"),
    FW_EMP_NEW("employees/new"),
    FW_EMP_EDIT("employees/edit"),
    FW_EMP_SEARCH("employees/search"),
    FW_REP_INDEX("reports/index"),
    FW_REP_SHOW("reports/show"),
    FW_REP_NEW("reports/new"),
    FW_REP_EDIT("reports/edit"),
    FW_FOL_INDEX("follows/index"),
    FW_FOL_SHOW("follows/show"),
    FW_DEP_INDEX("department/index"),
    FW_DEP_EDIT("department/edit"),
    FW_DEP_NEW("department/new"),
    FW_DEP_SHOW("department/show"),
    FW_STO_EDIT("store/edit"),
    FW_STO_SHOW("store/show"),
    FW_STO_NEW("store/new");


    /**
     * 文字列
     */
    private final String text;

    /**
     * コンストラクタ
     */
    private ForwardConst(final String text) {
        this.text = text;
    }

    /**
     * 値(文字列)取得
     */
    public String getValue() {
        return this.text;
    }

    /**
     * 値(文字列)から、該当する定数を返却する
     * (例: "Report"→ForwardConst.ACT_REP)
     * @param 値(文字列)
     * @return ForwardConst型定数
     */
    public static ForwardConst get(String key) {
        for(ForwardConst c : values()) {
            if(c.getValue().equals(key)) {
                return c;
            }
        }
        return CMD_NONE;
    }

}

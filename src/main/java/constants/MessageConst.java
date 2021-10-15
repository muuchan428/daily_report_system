package constants;

/**
 * 各出力メッセージを定義するEnumクラス
 *
 */
public enum MessageConst {

    //認証
    I_LOGINED("ログインしました"),
    E_LOGINED("ログインに失敗しました。"),
    I_LOGOUT("ログアウトしました。"),

    //DB更新
    I_REGISTERED("登録が完了しました。"),
    I_FOLLOWED("フォローしました"),
    I_REMOVED("フォロー解除しました"),
    I_UPDATED("更新が完了しました。"),
    I_DELETED("削除が完了しました。"),

    //バリデーション
    E_NONAME("氏名を入力してください。"),
    E_NOPASSWORD("パスワードを入力してください。"),
    E_NOEMP_CODE("社員番号を入力してください。"),
    E_EMP_CODE_EXIST("入力された社員番号の情報は既に存在しています。"),
    E_NOTITLE("タイトルを入力してください。"),
    E_NOCONTENT("内容を入力してください。"),
    E_NODEP_CODE("部署番号を入力してください。"),
    E_DEP_CODE_EXIST("入力された部署番号の情報は既に存在しています。"),
    E_NOSTO_CODE("店舗番号を入力してください。"),
    E_STO_CODE_EXIST("入力された店舗番号の情報は既に存在しています。"),
    E_NOSTO_NAME("店舗名を入力してください。"),
    E_NODEP_NAME("部署名を入力してください。"),
    E_NOT_DELETE_DEP_STO("従業員が所属しているため削除できません。");


    /**
     * 文字列
     */
    private final String text;

    /**
     * コンストラクタ
     */
    private MessageConst(final String text) {
        this.text = text;
    }

    /**
     * 値(文字列)取得
     */
    public String getMessage() {
        return this.text;
    }
}

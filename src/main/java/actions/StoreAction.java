package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import actions.views.EmployeeView;
import actions.views.StoreView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.MessageConst;
import services.EmployeeService;
import services.StoreService;

public class StoreAction extends ActionBase {
        EmployeeService employeeService;
        StoreService storeService;
    @Override
    public void process() throws ServletException, IOException {
        employeeService = new EmployeeService();
        storeService = new StoreService();

        //メソッドを実行
        invoke();

        employeeService.close();
        storeService.close();

    }

    /**
     * 新規登録画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void entryNew() throws ServletException, IOException {

        //
        if(checkAdmin()) {


        putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
        putRequestScope(AttributeConst.STORE, new StoreView()); //空の店舗インスタンス

        //新規登録画面を表示
        forward(ForwardConst.FW_STO_NEW);
        }
    }
    /**
     * 詳細画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void show() throws ServletException, IOException {


            //管理者かどうかのチェック
            if(checkAdmin()) {
                //idを条件に店舗データを取得する
                StoreView sv = storeService.findOne(toNumber(getRequestParam(AttributeConst.STO_ID)));

                putRequestScope(AttributeConst.STORE, sv);//取得した店舗情報
                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン

                //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
                String flush = getSessionScope(AttributeConst.FLUSH);
                if (flush != null) {
                    putRequestScope(AttributeConst.FLUSH, flush);
                    removeSessionScope(AttributeConst.FLUSH);
                }

                //詳細画面を表示
                forward(ForwardConst.FW_STO_SHOW);
            }
    }

    /**
     * 編集画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void edit() throws ServletException, IOException {

        if(checkAdmin()) {

        //idを条件に店舗データを取得する
        StoreView sv = storeService.findOne(toNumber(getRequestParam(AttributeConst.STO_ID)));


        putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
        putRequestScope(AttributeConst.STORE, sv); //取得した店舗情報

        //編集画面を表示する
        forward(ForwardConst.FW_STO_EDIT);
        }

    }
    /**
     * 新規登録を行う
     * @throws ServletException
     * @throws IOException
     */
    public void create() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkAdmin() && checkToken()) {

            //パラメータの値を元に店舗情報のインスタンスを作成する


                StoreView sv = new StoreView(
                        null,
                       getRequestParam(AttributeConst.STO_CODE),
                       getRequestParam(AttributeConst.STO_NAME));
                //店舗情報登録
                List<String> errors = storeService.create(sv);

                if (errors.size() > 0) {
                    //登録中にエラーがあった場合

                    putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                    putRequestScope(AttributeConst.STORE, sv); //入力された店舗情報
                    putRequestScope(AttributeConst.ERR, errors); //エラーのリスト

                    //新規登録画面を再表示
                    forward(ForwardConst.FW_STO_NEW);

                } else {
                    //登録中にエラーがなかった場合

                    //セッションに登録完了のフラッシュメッセージを設定
                    putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());

                    //一覧画面にリダイレクト
                    redirect(ForwardConst.ACT_DEP, ForwardConst.CMD_INDEX);
                }


        }
    }

    public void update() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkAdmin() && checkToken()) {
            //パラメータの値を元に店舗情報のインスタンスを作成する
            StoreView sv = new StoreView(
                    toNumber(getRequestParam(AttributeConst.STO_ID)),
                    getRequestParam(AttributeConst.STO_CODE),
                    getRequestParam(AttributeConst.STO_NAME));

            //店舗情報更新
            List<String> errors = storeService.update(sv);

            if (errors.size() > 0) {
                //更新中にエラーが発生した場合

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.STORE, sv); //入力された店舗情報
                putRequestScope(AttributeConst.ERR, errors); //エラーのリスト

                //編集画面を再表示
                forward(ForwardConst.FW_STO_EDIT);
            } else {
                //更新中にエラーがなかった場合

                //セッションに更新完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_UPDATED.getMessage());
                System.out.println("update");
                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_DEP, ForwardConst.CMD_INDEX);
            }
        }
    }
    /**
     * 指定された店舗に所属している従業員がいないかを確認し、いなければデータを削除する
     * 1人以上いる場合はエラーメッセージを表示し、Show画面にリダイレクト
     * @throws ServletException
     * @throws IOException
     */
    public void destroy() throws ServletException, IOException {


        //CSRF対策 tokenのチェック
        if (checkAdmin() && checkToken()) {

            StoreView store = storeService.findOne(toNumber(getRequestParam(AttributeConst.STO_ID)));
            long countEmp = employeeService.countInStore(store);//所属している従業員の数

            if(countEmp == 0) {
                //所属している従業員の数が０の場合

                //指定した店舗を削除する
            storeService.removeDepartment(store);

            //セッションに削除完了のフラッシュメッセージを設定
            putSessionScope(AttributeConst.FLUSH, MessageConst.I_DELETED.getMessage());

            //一覧画面にリダイレクト
            redirect(ForwardConst.ACT_DEP, ForwardConst.CMD_INDEX);
            } else {
                //所属している従業員の数が１以上の場合

                //エラーメッセージを設定
                putRequestScope(AttributeConst.ERR, MessageConst.E_NOT_DELETE_DEP_STO.getMessage() );
                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.STORE, store); //入力された店舗情報
                //編集画面を再表示
                forward(ForwardConst.FW_STO_EDIT);
            }
        }
    }

        /**
         * ログイン中の従業員が管理者かどうかチェックし、管理者でなければエラー画面を表示
         * true: 管理者 false: 管理者ではない
         * @throws ServletException
         * @throws IOException
         */
        private boolean checkAdmin() throws ServletException, IOException {

            //セッションからログイン中の従業員情報を取得
            EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

            //管理者でなければエラー画面を表示
            if (ev.getAdminFlag() != AttributeConst.ROLE_ADMIN.getIntegerValue()) {

                forward(ForwardConst.FW_ERR_UNKNOWN);
                return false;

            } else {

                return true;
            }

        }
}

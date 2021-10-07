package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.DepartmentView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import constants.PropertyConst;
import services.EmployeeService;
import services.DepartmentService;

public class DepartmentAction extends ActionBase {
        EmployeeService empService;
        DepartmentService depService;
    @Override
    public void process() throws ServletException, IOException {
        empService = new EmployeeService();
        depService = new DepartmentService();

        //メソッドを実行
        invoke();

        empService.close();
        depService.close();

    }
    public void index() throws ServletException, IOException {

        //管理者かどうかのチェック
        if(checkAdmin()) {
        //指定されたページ数の一覧画面に表示するデータを取得
        int page = getPage();
        List<DepartmentView> departments = depService.getPerPage(page);

        //全ての部署データの件数を取得
        long departmentCount = depService.countAll();

        putRequestScope(AttributeConst.DEPARTMENTS, departments); //取得した従業員データ
        putRequestScope(AttributeConst.DEP_COUNT, departmentCount); //全ての従業員データの件数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //一覧画面を表示
        forward(ForwardConst.FW_DEP_INDEX);

        }
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
        putRequestScope(AttributeConst.DEPARTMENT, new DepartmentView()); //空の部署インスタンス

        //新規登録画面を表示
        forward(ForwardConst.FW_DEP_NEW);
        }
    }

    /**
     * 編集画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void edit() throws ServletException, IOException {

        if(checkAdmin()) {

        //idを条件に従業員データを取得する
        DepartmentView dv = depService.findOne(toNumber(getRequestParam(AttributeConst.DEP_ID)));

            //データが取得できなかった、場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);

        putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
        putRequestScope(AttributeConst.EMPLOYEE, dv); //取得した従業員情報

        //編集画面を表示する
        forward(ForwardConst.FW_DEP_EDIT);
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

            //パラメータの値を元に部署情報のインスタンスを作成する


                DepartmentView dv = new DepartmentView(
                        null,
                       getRequestParam(AttributeConst.DEP_CODE),
                       getRequestParam(AttributeConst.DEP_NAME));
                //部署情報登録
                List<String> errors = depService.create(dv);

                if (errors.size() > 0) {
                    //登録中にエラーがあった場合

                    putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                    putRequestScope(AttributeConst.DEPARTMENT, dv); //入力された部署情報
                    putRequestScope(AttributeConst.ERR, errors); //エラーのリスト

                    //新規登録画面を再表示
                    forward(ForwardConst.FW_DEP_NEW);

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
            //パラメータの値を元に部署情報のインスタンスを作成する
            DepartmentView dv = new DepartmentView(
                    toNumber(getRequestParam(AttributeConst.DEP_ID)),
                    getRequestParam(AttributeConst.DEP_CODE),
                    getRequestParam(AttributeConst.DEP_NAME));

            //部署情報更新
            List<String> errors = depService.update(dv);

            if (errors.size() > 0) {
                //更新中にエラーが発生した場合

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.DEPARTMENT, dv); //入力された従業員情報
                putRequestScope(AttributeConst.ERR, errors); //エラーのリスト

                //編集画面を再表示
                forward(ForwardConst.FW_DEP_EDIT);
            } else {
                //更新中にエラーがなかった場合

                //セッションに更新完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_UPDATED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_DEP, ForwardConst.CMD_INDEX);
            }
        }
    }
    /**
     * 指定された部署に所属している従業員がいないかを確認し、いなければ部署を削除する
     * 1人以上いる場合はエラーメッセージを表示し、Show画面にリダイレクト
     * @throws ServletException
     * @throws IOException
     */
    public void destroy() throws ServletException, IOException {


        //CSRF対策 tokenのチェック
        if (checkAdmin() && checkToken()) {

            DepartmentView department = depService.findOne(toNumber(getRequestParam(AttributeConst.DEP_ID)));
            long countEmp = empService.countInDepartment(department);//所属している従業員の数

            if(countEmp == 0) {
                //所属している従業員の数が０の場合

                //指定した部署を削除する
            depService.removeDepartment(department);

            //セッションに削除完了のフラッシュメッセージを設定
            putSessionScope(AttributeConst.FLUSH, MessageConst.I_DELETED.getMessage());

            //一覧画面にリダイレクト
            redirect(ForwardConst.ACT_DEP, ForwardConst.CMD_INDEX);
            } else {
                //所属している従業員の数が１以上の場合

                //エラーメッセージを設定
                putRequestScope(AttributeConst.ERR, MessageConst.E_NOT_DELETE_DEP.getMessage() );

                //詳細画面にリダイレクト
                redirect(ForwardConst.ACT_DEP, ForwardConst.CMD_SHOW);
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

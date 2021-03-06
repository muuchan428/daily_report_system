package actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.DepartmentView;
import actions.views.EmployeeView;
import actions.views.ReportView;
import actions.views.StoreView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import constants.PropertyConst;
import services.DepartmentService;
import services.EmployeeService;
import services.FollowService;
import services.ReportService;
import services.StoreService;

/**
 * 従業員に関わる処理を行うActionクラス
 *
 */
public class EmployeeAction extends ActionBase {

    private EmployeeService employeeService;
    private FollowService followService;
    private ReportService reportService;
    private DepartmentService departmentService;
    private StoreService storeService;


    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        employeeService = new EmployeeService();
        followService = new FollowService();
        reportService = new ReportService();
        departmentService = new DepartmentService();
        storeService = new StoreService();


        //メソッドを実行
        invoke();

        employeeService.close();
        followService.close();
        reportService.close();
        departmentService.close();
        storeService.close();
    }

    /**
     * 一覧画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void index() throws ServletException, IOException {

        //管理者かどうかのチェック
        if(checkAdmin()) {
        //指定されたページ数の一覧画面に表示するデータを取得
        int page = getPage();
        List<EmployeeView> employees = employeeService.getPerPage(page);

        //全ての従業員データの件数を取得
        long employeeCount = employeeService.countAll();

        putRequestScope(AttributeConst.EMPLOYEES, employees); //取得した従業員データ
        putRequestScope(AttributeConst.EMP_COUNT, employeeCount); //全ての従業員データの件数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //一覧画面を表示
        forward(ForwardConst.FW_EMP_INDEX);
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

            List<DepartmentView> departments = departmentService.getAllDep();
            List<StoreView> stores = storeService.getAllStore();
            putRequestScope(AttributeConst.STORES, stores);//取得した店舗リスト
            putRequestScope(AttributeConst.DEPARTMENTS,departments);//取得した部署リスト
         putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
        putRequestScope(AttributeConst.EMPLOYEE, new EmployeeView()); //空の従業員インスタンス

        //新規登録画面を表示
        forward(ForwardConst.FW_EMP_NEW);
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
          //idを条件に部署データを取得する
            DepartmentView dv = departmentService.findOne(toNumber(getRequestParam(AttributeConst.EMP_DEP)));
          //idを条件に店舗データを取得する
            StoreView sv = storeService.findOne(toNumber(getRequestParam(AttributeConst.EMP_STO)));

            //パラメータの値を元に従業員情報のインスタンスを作成する
            EmployeeView ev = new EmployeeView(
                    null,
                    getRequestParam(AttributeConst.EMP_CODE),
                    getRequestParam(AttributeConst.EMP_F_NAME),
                    getRequestParam(AttributeConst.EMP_L_NAME),
                    getRequestParam(AttributeConst.EMP_PASS),
                    toNumber(getRequestParam(AttributeConst.EMP_ADMIN_FLG)),
                    null,
                    null,
                    AttributeConst.DEL_FLAG_FALSE.getIntegerValue(),
                    dv,
                    sv);

                    ;

            //アプリケーションスコープからpepper文字列を取得
            String pepper = getContextScope(PropertyConst.PEPPER);

            //従業員情報登録
            List<String> errors = employeeService.create(ev, pepper);

            if (errors.size() > 0) {
                //登録中にエラーがあった場合

                List<DepartmentView> departments = departmentService.getAllDep();
                List<StoreView> stores = storeService.getAllStore();
                putRequestScope(AttributeConst.STORES, stores);//取得した店舗リスト
                putRequestScope(AttributeConst.DEPARTMENTS,departments);//取得した部署リスト
                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.EMPLOYEE, ev); //入力された従業員情報
                putRequestScope(AttributeConst.ERR, errors); //エラーのリスト

                //新規登録画面を再表示
                forward(ForwardConst.FW_EMP_NEW);

            } else {
                //登録中にエラーがなかった場合

                //セッションに登録完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_EMP, ForwardConst.CMD_INDEX);
            }



        }
    }

    /**
     * 詳細画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void show() throws ServletException, IOException {


            //セッションからログイン中の従業員情報を取得
            EmployeeView loginEmployee = (EmployeeView)getSessionScope(AttributeConst.LOGIN_EMP);

        //idを条件に従業員データを取得する
        EmployeeView ev = employeeService.findOne(toNumber(getRequestParam(AttributeConst.EMP_ID)));

        if (ev == null || ev.getDeleteFlag() == AttributeConst.DEL_FLAG_TRUE.getIntegerValue()) {

            //データが取得できなかった、または論理削除されている場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);
            return;
        }
        //ログインしている従業員からフォローされているかのチェック
        Boolean checkFollow = followService.checkFollow(loginEmployee, ev);

        //従業員が作成した日報データを、指定されたページ数の一覧画面に表示する分取得する
        int page = getPage();
        List<ReportView> reports = reportService.getMinePerPage(ev, page);

        //従業員が作成した日報データの件数を取得
        long ReportsCount = reportService.countAllMine(ev);

        putRequestScope(AttributeConst.REPORTS, reports); //取得した日報データ
        putRequestScope(AttributeConst.REP_COUNT, ReportsCount); //ログイン中の従業員が作成した日報の数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数

        putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
        putRequestScope(AttributeConst.LOGIN_EMP,loginEmployee);//ログインしている従業員情報
        putRequestScope(AttributeConst.EMPLOYEE, ev); //取得した従業員情報
        putRequestScope(AttributeConst.FOL_CHECK, checkFollow);//フォローされているかの情報

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //詳細画面を表示
        forward(ForwardConst.FW_EMP_SHOW);
        }


    /**
     * 編集画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void edit() throws ServletException, IOException {

        if(checkAdmin()) {

        //idを条件に従業員データを取得する
        EmployeeView ev = employeeService.findOne(toNumber(getRequestParam(AttributeConst.EMP_ID)));

        if (ev == null || ev.getDeleteFlag() == AttributeConst.DEL_FLAG_TRUE.getIntegerValue()) {

            //データが取得できなかった、または論理削除されている場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);
            return;
        }
        List<DepartmentView> departments = departmentService.getAllDep();
        List<StoreView> stores = storeService.getAllStore();

        putRequestScope(AttributeConst.STORES, stores);//取得した店舗リスト
        putRequestScope(AttributeConst.DEPARTMENTS,departments);//取得した部署リスト
        putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
        putRequestScope(AttributeConst.EMPLOYEE, ev); //取得した従業員情報

        //編集画面を表示する
        forward(ForwardConst.FW_EMP_EDIT);
        }

    }
/**
 * 検索キーワードから従業員検索を行う
 * @throws ServletException
 * @throws IOException
 */
    public void search() throws ServletException, IOException {
        String word =getRequestParam(AttributeConst.SEARCH_WORD);
        List<EmployeeView> ev = new ArrayList<EmployeeView>();
        if(word != "") {
            if(employeeService.searchEmployee(word) != null ) {//該当する従業員がいた場合
         ev.addAll(employeeService.searchEmployee(word));
         putRequestScope(AttributeConst.EMPLOYEES, ev);
            } else {//該当する従業員がいない場合
                putRequestScope(AttributeConst.EMPLOYEES, null);
            }
         putRequestScope(AttributeConst.ERR,null );
        } else {//入力されていない場合
            putRequestScope(AttributeConst.ERR, "検索キーワードを入力してください");
        }

        putRequestScope(AttributeConst.SEARCH_WORD, word);


        //検索結果画面を表示する
        forward(ForwardConst.FW_EMP_SEARCH);
    }

    /**
     * 更新を行う
     * @throws ServletException
     * @throws IOException
     */
    public void update() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkAdmin() && checkToken()) {
            //idを条件に部署データを取得する
            DepartmentView dv = departmentService.findOne(toNumber(getRequestParam(AttributeConst.EMP_DEP)));
          //idを条件に店舗データを取得する
            StoreView sv = storeService.findOne(toNumber(getRequestParam(AttributeConst.EMP_STO)));
            //パラメータの値を元に従業員情報のインスタンスを作成する
            EmployeeView ev = new EmployeeView(
                    toNumber(getRequestParam(AttributeConst.EMP_ID)),
                    getRequestParam(AttributeConst.EMP_CODE),
                    getRequestParam(AttributeConst.EMP_F_NAME),
                    getRequestParam(AttributeConst.EMP_L_NAME),
                    getRequestParam(AttributeConst.EMP_PASS),
                    toNumber(getRequestParam(AttributeConst.EMP_ADMIN_FLG)),
                    null,
                    null,
                    AttributeConst.DEL_FLAG_FALSE.getIntegerValue(),
                    dv,
                    sv);

            //アプリケーションスコープからpepper文字列を取得
            String pepper = getContextScope(PropertyConst.PEPPER);

            //従業員情報更新
            List<String> errors = employeeService.update(ev, pepper);

            if (errors.size() > 0) {
                //更新中にエラーが発生した場合

                List<DepartmentView> departments = departmentService.getAllDep();
                List<StoreView> stores = storeService.getAllStore();

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.EMPLOYEE, ev); //入力された従業員情報
                putRequestScope(AttributeConst.ERR, errors); //エラーのリスト
                putRequestScope(AttributeConst.STORES, stores);//取得した店舗リスト
                putRequestScope(AttributeConst.DEPARTMENTS,departments);//取得した部署リスト

                //編集画面を再表示
                forward(ForwardConst.FW_EMP_EDIT);
            } else {
                //更新中にエラーがなかった場合

                //セッションに更新完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_UPDATED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_EMP, ForwardConst.CMD_INDEX);
            }
        }
    }

    /**
     * 論理削除を行う
     * @throws ServletException
     * @throws IOException
     */
    public void destroy() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkAdmin() && checkToken()) {

            //idを条件に従業員データを論理削除する
            employeeService.destroy(toNumber(getRequestParam(AttributeConst.EMP_ID)));

            //セッションに削除完了のフラッシュメッセージを設定
            putSessionScope(AttributeConst.FLUSH, MessageConst.I_DELETED.getMessage());

            //一覧画面にリダイレクト
            redirect(ForwardConst.ACT_EMP, ForwardConst.CMD_INDEX);
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


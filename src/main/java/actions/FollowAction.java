package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.FollowView;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import services.EmployeeService;
import services.FollowService;
import services.ReportService;

    /**
     * フォローに関する処理を行うActionクラス
     *
     */
    public class FollowAction extends ActionBase {

        private FollowService followService;
        private EmployeeService  emplyoeeService;
        private ReportService reportService;
        /**
         * メソッドを実行する
         */
        @Override
        public void process() throws ServletException, IOException {

            followService = new FollowService();
            emplyoeeService = new EmployeeService();
            reportService = new ReportService();

            //メソッドを実行
            invoke();
            followService.close();
            emplyoeeService.close();
            reportService.close();

        }

        /**
         * フォローしている日報の一覧を表示する
         * @throws ServletException
         * @throws IOException
         */
        public void index() throws ServletException, IOException {

            //セッションからログイン中の従業員情報を取得
            EmployeeView loginEmp = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

            //指定されたページ数の一覧画面に表示する日報データを取得
            int page = getPage();
            List<ReportView> reports = followService.getFollowReportsPerPage(loginEmp, page);

            //全日報データの件数を取得
            long reportsCount = followService.countAllFollowReports(loginEmp);

            putRequestScope(AttributeConst.REPORTS, reports); //取得した日報データ
            putRequestScope(AttributeConst.REP_COUNT, reportsCount); //全ての日報データの件数
            putRequestScope(AttributeConst.PAGE, page); //ページ数
            putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数

            //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
            String flush = getSessionScope(AttributeConst.FLUSH);
            if (flush != null) {
                putRequestScope(AttributeConst.FLUSH, flush);
                removeSessionScope(AttributeConst.FLUSH);
            }

            //一覧画面を表示
            forward(ForwardConst.FW_FOL_INDEX);
        }

        /**
         * フォロー一覧画面を表示する
         * @throws ServletException
         * @throws IOException
         */
        public void show() throws ServletException, IOException {

          //セッションからログイン中の従業員情報を取得
            EmployeeView loginEmp = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

            //指定されたページ数の一覧画面に表示する従業員データを取得
            int page = getPage();
            List<EmployeeView> employees = followService.getFollowEmployeesPerPage(loginEmp,page);

            //全従業員データの件数を取得
            long followEmpCount = followService.countAllFollowEmployees(loginEmp);

            putRequestScope(AttributeConst.EMPLOYEES, employees ); //取得した従業員のデータ
            putRequestScope(AttributeConst.FOL_COUNT, followEmpCount); //従業員データの件数
            putRequestScope(AttributeConst.PAGE, page); //ページ数
            putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数

            //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
            String flush = getSessionScope(AttributeConst.FLUSH);
            if (flush != null) {
                putRequestScope(AttributeConst.FLUSH, flush);
                removeSessionScope(AttributeConst.FLUSH);
            }



            //一覧画面を表示
            forward(ForwardConst.FW_FOL_SHOW);
        }

        /**
         * 指定した従業員がフォロー解除するフォローデータを削除する
         * @throws ServletException
         * @throws IOException
         */

        public void destroy() throws ServletException, IOException {


            //CSRF対策 tokenのチェック
            if (checkToken()) {

        //セッションからログイン中の従業員情報を取得
           EmployeeView loginEmp = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

           //idを条件に従業員データを取得する
           EmployeeView followEmp = emplyoeeService.findOne(toNumber(getRequestParam(AttributeConst.EMP_ID)));

                //社員番号を条件にフォローデータ削除する
                followService.removeFollowEmployee(loginEmp, followEmp);

                //セッションに削除完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_REMOVED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_EMP, ForwardConst.CMD_SHOW, followEmp.getId());
            }
        }


        /**
             * フォローを行う
             * @throws ServletException
             * @throws IOException
             */
            public void create() throws ServletException, IOException {

                //CSRF対策 tokenのチェック
                if (checkToken()) {

                    //セッションからログイン中の従業員情報を取得
                    EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

                    //idを条件に従業員データを取得する
                    EmployeeView f_ev = emplyoeeService.findOne(toNumber(getRequestParam(AttributeConst.EMP_ID)));

                    //パラメータの値をもとにフォローデータのインスタンスを作成する
                    FollowView fv = new FollowView(
                            null,
                            ev,
                            f_ev);

                    followService.create(fv);

                    //セッションに登録完了のフラッシュメッセージを設定
                    putSessionScope(AttributeConst.FLUSH, MessageConst.I_FOLLOWED.getMessage());

                    //従業員詳細にリダイレクト
                    redirect(ForwardConst.ACT_EMP, ForwardConst.CMD_SHOW, f_ev.getId());


                }
            }


    }
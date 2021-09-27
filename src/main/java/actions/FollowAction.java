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
     * 日報に関する処理を行うActionクラス
     *
     */
    public class FollowAction extends ActionBase {

        private FollowService f_service;
        private EmployeeService  e_service;
        private ReportService r_service;
        /**
         * メソッドを実行する
         */
        @Override
        public void process() throws ServletException, IOException {

            f_service = new FollowService();
            e_service = new EmployeeService();
            r_service = new ReportService();

            //メソッドを実行
            invoke();
            f_service.close();
            e_service.close();
            r_service.close();

        }

        /**
         * フォローしている日報の一覧を表示する
         * @throws ServletException
         * @throws IOException
         */
        public void index() throws ServletException, IOException {

            //セッションからログイン中の従業員情報を取得
            EmployeeView loginEmployee = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

            //指定されたページ数の一覧画面に表示する日報データを取得
            int page = getPage();
            List<ReportView> reports = f_service.getFollowReportsPerPage(loginEmployee, page);

            //全日報データの件数を取得
            long reportsCount = f_service.countAllFollowReports(loginEmployee);

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
        public void edit() throws ServletException, IOException {

          //セッションからログイン中の従業員情報を取得
            EmployeeView loginEmployee = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

            //指定されたページ数の一覧画面に表示する従業員データを取得
            int page = getPage();
            List<EmployeeView> employees = f_service.getFollowEmployeesPerPage(loginEmployee,page);

            //全従業員データの件数を取得
            long followEmployeeCount = f_service.countAllFollowEmployees(loginEmployee);

            putRequestScope(AttributeConst.EMPLOYEES, employees ); //取得した従業員のデータ
            putRequestScope(AttributeConst.FOL_COUNT, followEmployeeCount); //従業員データの件数
            putRequestScope(AttributeConst.PAGE, page); //ページ数
            putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数

            //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
            String flush = getSessionScope(AttributeConst.FLUSH);
            if (flush != null) {
                putRequestScope(AttributeConst.FLUSH, flush);
                removeSessionScope(AttributeConst.FLUSH);
            }



            //一覧画面を表示
            forward(ForwardConst.FW_FOL_EDIT);
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
           EmployeeView loginEmployee = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

           //idを条件に従業員データを取得する
           EmployeeView f_ev = e_service.findOne(toNumber(getRequestParam(AttributeConst.EMP_ID)));

                //社員番号を条件にフォローデータ削除する
                f_service.removeFollowEmployee(loginEmployee, f_ev);

                //セッションに削除完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_DELETED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_FOL, ForwardConst.CMD_EDIT);
            }
        }

        /**
         * フォローされているかのチェックを行う
         * @return
         * @throws ServletException
         * @throws IOException
         */
        public Boolean checkFollow() throws ServletException, IOException{

                //セッションからログイン中の従業員情報を取得
                EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

                //idを条件に従業員データを取得する
                EmployeeView f_ev = e_service.findOne(toNumber(getRequestParam(AttributeConst.EMP_ID)));

                //フォローされているかの確認を行う
                Boolean checkFollow = f_service.checkFollow(ev, f_ev);

                return checkFollow;

        }
        /**
             * フォローを行う
             * @throws ServletException
             * @throws IOException
             */
            public void entrynew() throws ServletException, IOException {

                if (checkToken()) {

                    //セッションからログイン中の従業員情報を取得
                    EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

                    //idを条件に従業員データを取得する
                    EmployeeView f_ev = e_service.findOne(toNumber(getRequestParam(AttributeConst.EMP_ID)));

                    //パラメータの値をもとにフォローデータのインスタンスを作成する
                    FollowView fv = new FollowView(
                            null,
                            ev,
                            f_ev);

                    f_service.create(fv);

                    //セッションに登録完了のフラッシュメッセージを設定
                    putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());

                    //従業員詳細にリダイレクト
                    redirect(ForwardConst.ACT_EMP, ForwardConst.CMD_SHOW);
                }


            }


    }
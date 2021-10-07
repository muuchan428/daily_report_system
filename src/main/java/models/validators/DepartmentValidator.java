package models.validators;

import java.util.ArrayList;
import java.util.List;

import actions.views.DepartmentView;
import constants.MessageConst;
import services.DepartmentService;

/**
 * 部署インスタンスに設定されている値のバリデーションを行うクラス
 *
 */
public class DepartmentValidator {

    /**
     * 部署インスタンスの各項目についてバリデーションを行う
     * @param service 呼び出し元Serviceクラスのインスタンス
     * @param ev EmployeeServiceのインスタンス
     * @param codeDuplicateCheckFlag 社員番号の重複チェックを実施するかどうか(実施する:true 実施しない:false)
     *
     * @return エラーのリスト
     */
    public static List<String> validate(
            DepartmentService service, DepartmentView dv, Boolean codeDuplicateCheckFlag) {
        List<String> errors = new ArrayList<String>();

        //部署番号のチェック
        String codeError = validateCode(service, dv.getCode(), codeDuplicateCheckFlag);
        if (!codeError.equals("")) {
            errors.add(codeError);
        }

        //部署名のチェック
        String nameError = validateName(dv.getName());
        if (!nameError.equals("")) {
            errors.add(nameError);
        }

        return errors;
    }

    /**
     *部署番号の入力チェックを行い、エラーメッセージを返却
     * @param service DepartmentServiceのインスタンス
     * @param code 部署番号
     * @param codeDuplicateCheckFlag 部署番号の重複チェックを実施するかどうか(実施する:true 実施しない:false)
     * @return エラーメッセージ
     */
    private static String validateCode(DepartmentService service,String  code, Boolean codeDuplicateCheckFlag) {

        //入力値がなければエラーメッセージを返却
        if (code == null || code.equals("")) {
            return MessageConst.E_NODEP_CODE.getMessage();
        }

        if (codeDuplicateCheckFlag) {
            //部署番号の重複チェックを実施

            long departmentsCount = isDuplicateDepartment(service, code);

            //同一部署番号が既に登録されている場合はエラーメッセージを返却
            if (departmentsCount > 0) {
                return MessageConst.E_DEP_CODE_EXIST.getMessage();
            }
        }

        //エラーがない場合は空文字を返却
        return "";
    }
    /**
     * @param service　DepartmentServiceのインスタンス
     * @param code 部署番号
     * @return 部署テーブルに登録されている同一部署番号のデータの件数
     */
    private static long isDuplicateDepartment(DepartmentService service,String  code) {

        long departmensCount = service.countByCode(code);
        return departmensCount;
    }

    /**
     * 氏名に入力値があるかをチェックし、入力値がなければエラーメッセージを返却
     * @param name 氏名
     * @return エラーメッセージ
     */
    private static String validateName(String name) {

        if (name == null || name.equals("")) {
            return MessageConst.E_NODEP_NAME.getMessage();
        }

        //入力値がある場合は空文字を返却
        return "";
    }



    }




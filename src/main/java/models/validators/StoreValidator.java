package models.validators;

import java.util.ArrayList;
import java.util.List;

import actions.views.StoreView;
import constants.MessageConst;
import services.StoreService;

/**
 * 店舗インスタンスに設定されている値のバリデーションを行うクラス
 *
 */
public class StoreValidator {

    /**
     * 部署インスタンスの各項目についてバリデーションを行う
     * @param service 呼び出し元Serviceクラスのインスタンス
     * @param ev EmployeeServiceのインスタンス
     * @param codeDuplicateCheckFlag 店舗番号の重複チェックを実施するかどうか(実施する:true 実施しない:false)
     *
     * @return エラーのリスト
     */
    public static List<String> validate(
            StoreService service, StoreView sv, Boolean codeDuplicateCheckFlag) {
        List<String> errors = new ArrayList<String>();

        //店舗番号のチェック
        String codeError = validateCode(service, sv.getCode(), codeDuplicateCheckFlag);
        if (!codeError.equals("")) {
            errors.add(codeError);
        }

        //店舗名のチェック
        String nameError = validateName(sv.getName());
        if (!nameError.equals("")) {
            errors.add(nameError);
        }

        return errors;
    }

    /**
     *店舗番号の入力チェックを行い、エラーメッセージを返却
     * @param service StoreServiceのインスタンス
     * @param code 店舗番号
     * @param codeDuplicateCheckFlag 店舗番号の重複チェックを実施するかどうか(実施する:true 実施しない:false)
     * @return エラーメッセージ
     */
    private static String validateCode(StoreService service,String  code, Boolean codeDuplicateCheckFlag) {

        //入力値がなければエラーメッセージを返却
        if (code == null || code.equals("")) {
            return MessageConst.E_NOSTO_CODE.getMessage();
        }

        if (codeDuplicateCheckFlag) {
            //店舗番号の重複チェックを実施

            long storeCount = isDuplicateDepartment(service, code);

            //同一店舗番号が既に登録されている場合はエラーメッセージを返却
            if (storeCount > 0) {
                return MessageConst.E_STO_CODE_EXIST.getMessage();
            }
        }

        //エラーがない場合は空文字を返却
        return "";
    }
    /**
     * @param service　StoreServiceのインスタンス
     * @param code 部署番号
     * @return 店舗テーブルに登録されている同一部署番号のデータの件数
     */
    private static long isDuplicateDepartment(StoreService service,String  code) {

        long storeCount = service.countByCode(code);
        return storeCount;
    }

    /**
     * 店舗名に入力値があるかをチェックし、入力値がなければエラーメッセージを返却
     * @param name 氏名
     * @return エラーメッセージ
     */
    private static String validateName(String name) {

        if (name == null || name.equals("")) {
            return MessageConst.E_NOSTO_NAME.getMessage();
        }

        //入力値がある場合は空文字を返却
        return "";
    }



    }




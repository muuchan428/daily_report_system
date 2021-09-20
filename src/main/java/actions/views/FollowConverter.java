package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.Follow;


public class FollowConverter {

    /**
     * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
     * @param fv FollowViewのインスタンス
     * @return Employeeのインスタンス
     */
    public static Follow toModel(FollowView fv) {
        return new Follow(
                fv.getId(),
                EmployeeConverter.toModel(fv.getEmployee()),
                EmployeeConverter.toModel(fv.getFollow_employee())
                );
    }

    /**
     * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
     * @param f Followのインスタンス
     * @return FollowViewのインスタンス
     */
        public static FollowView toView(Follow f) {

            if(f == null) {
                return null;
            }
            return new FollowView(
                    f.getId(),
                    EmployeeConverter.toView(f.getEmployee()),
                    EmployeeConverter.toView(f.getFollow_employee())
                    );
        }

            /**
             * DTOモデルのリストからViewモデルのリストを作成する
             * @param List DTOモデルのリスト
             * @return Viewモデルのリスト
             */
        public static List<FollowView> toViewList(List<Follow> list){
            List<FollowView> evs = new ArrayList<>();

            for(Follow r : list) {
                evs.add(toView(r));
            }

            return evs;

        }
        /**
         * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
         * @param f DTOモデル(コピー先)
         * @pram fv Viewモデル（コピー元）
         */

        public static void copyViewToModel(Follow f, FollowView fv) {
            f.setId(fv.getId());
            f.setEmployee(EmployeeConverter.toModel(fv.getEmployee()));
            f.setFollow_employee(EmployeeConverter.toModel(fv.getFollow_employee()));
            }

        /**
         * DTOモデルの全フィールドの内容をViewもでるのフィールドにコピーする
         * @param f DTOモデル（コピー元）
         * @param fv Viewモデル（コピー先）
         */
        public static void copyModelTovView(Follow f, FollowView fv) {
            fv.setId(f.getId());
            fv.setEmployee(EmployeeConverter.toView(f.getEmployee()));
            fv.setFollow_employee(EmployeeConverter.toView(f.getFollow_employee()));

        }

}



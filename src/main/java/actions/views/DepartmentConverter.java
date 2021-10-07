package actions.views;

import java.util.ArrayList;
import java.util.List;
import models.Department;


public class DepartmentConverter {

    /**
     * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
     * @param dv FollowViewのインスタンス
     * @return Departmentのインスタンス
     */
    public static Department toModel(DepartmentView dv) {
        return new Department(
                dv.getId(),
                dv.getCode(),
                dv.getName());

    }

    /**
     * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
     * @param d Departmentのインスタンス
     * @return DepartmentViewのインスタンス
     */
        public static DepartmentView toView(Department d) {

            if(d == null) {
                return null;
            }
            return new DepartmentView(
                    d.getId(),
                    d.getCode(),
                    d.getName()
                    );
        }

            /**
             * DTOモデルのリストからViewモデルのリストを作成する
             * @param List DTOモデルのリスト
             * @return Viewモデルのリスト
             */
        public static List<DepartmentView> toViewList(List<Department> list){
            List<DepartmentView> dvs = new ArrayList<>();

            for(Department r : list) {
                dvs.add(toView(r));
            }

            return dvs;

        }
        /**
         * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
         * @param d DTOモデル(コピー先)
         * @pram dv Viewモデル（コピー元）
         */

        public static void copyViewToModel(Department d, DepartmentView dv) {
            d.setId(dv.getId());
            d.setCode(dv.getCode());
            d.setName(dv.getName());
            }

        /**
         * DTOモデルの全フィールドの内容をViewもでるのフィールドにコピーする
         * @param d DTOモデル（コピー元）
         * @param dv Viewモデル（コピー先）
         */
        public static void copyModelToView(Department d, DepartmentView dv) {
            dv.setId(d.getId());
            dv.setCode(d.getCode());
            dv.setName(d.getName());

        }

}



package actions.views;

import java.util.ArrayList;
import java.util.List;
import models.Store;


public class StoreConverter {

    /**
     * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
     * @param dv FollowViewのインスタンス
     * @return Departmentのインスタンス
     */
    public static Store toModel(StoreView sv) {
        return new Store(
                sv.getId(),
                sv.getCode(),
                sv.getName());

    }

    /**
     * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
     * @param d Departmentのインスタンス
     * @return DepartmentViewのインスタンス
     */
        public static StoreView toView(Store s) {

            if(s == null) {
                return null;
            }
            return new StoreView(
                    s.getId(),
                    s.getCode(),
                    s.getName()
                    );
        }

            /**
             * DTOモデルのリストからViewモデルのリストを作成する
             * @param List DTOモデルのリスト
             * @return Viewモデルのリスト
             */
        public static List<StoreView> toViewList(List<Store> list){
            List<StoreView> svs = new ArrayList<>();

            for(Store s : list) {
                svs.add(toView(s));
            }

            return svs;

        }
        /**
         * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
         * @param d DTOモデル(コピー先)
         * @pram dv Viewモデル（コピー元）
         */

        public static void copyViewToModel(Store s, StoreView sv) {
            s.setId(sv.getId());
            s.setCode(sv.getCode());
            s.setName(sv.getName());
            }

        /**
         * DTOモデルの全フィールドの内容をViewもでるのフィールドにコピーする
         * @param d DTOモデル（コピー元）
         * @param dv Viewモデル（コピー先）
         */
        public static void copyModelToView(Store s, StoreView sv) {
            sv.setId(s.getId());
            sv.setCode(s.getCode());
            sv.setName(s.getName());

        }

}



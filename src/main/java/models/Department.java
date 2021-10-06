package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import constants.JpaConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Table(name = JpaConst.TABLE_DEP)
@NamedQueries({
    @NamedQuery(
            name = JpaConst.Q_DEP_GET_ALL,
            query = JpaConst.Q_DEP_GET_ALL_DEF ),
    @NamedQuery(
            name = JpaConst.Q_DEP_COUNT_ALL,
            query = JpaConst.Q_DEP_COUNT_ALL_DEF),
    @NamedQuery(
            name = JpaConst.Q_DEP_GET_BY_ID,
            query = JpaConst.Q_DEP_GET_BY_ID_DEF),
    @NamedQuery(
            name = JpaConst.Q_DEP_COUNT_RESISTERED_BY_CODE,
            query = JpaConst.Q_DEP_COUNT_RESISTERED_BY_CODE_DEF)


})

@Getter //全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter //全てのクラスフィールドについてsetterを自動生成する(Lombok)
@NoArgsConstructor //引数なしコンストラクタを自動生成する(Lombok)
@AllArgsConstructor //全てのクラスフィールドを引数にもつ引数ありコンストラクタを自動生成する(Lombok)
@Entity

public class Department {
    //id
    @Id
    @Column(name = JpaConst.DEP_COL_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //部署番号
    @JoinColumn(name = JpaConst.DEP_COL_CODE, nullable = false, unique = true)
    private String  code;

    //部署名
    @JoinColumn(name = JpaConst.DEP_COL_NAME,nullable = false)
    private String name;

    /**
     * 削除された従業員かどうか（現役：0、削除済み：1）
     */
    @Column(name = JpaConst.EMP_COL_DELETE_FLAG, nullable = false)
    private Integer deleteFlag;

}

package exunion.standardize;

/**
 * 各类值标准化接口
 *
 * @param <S> 标准化值类型
 * @param <L> 本地化值类型
 * @author robothy
 */
public interface Standardizable<S, L> {

    /**
     * 将本地化的值转化为标准化值
     *
     * @param l 本地化值
     * @return 标准化值
     */
    S standardize(L l);

    /**
     * 将标准化值转化为本地化值
     *
     * @param s 标准化值
     * @return 本地化值
     */
    L localize(S s);

}

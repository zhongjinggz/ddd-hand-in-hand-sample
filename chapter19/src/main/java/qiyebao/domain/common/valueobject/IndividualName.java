package qiyebao.domain.common.valueobject;

import java.util.Set;

import static qiyebao.common.utils.StringUtils.*;

public class IndividualName {
    // 支持的复姓列表（按拼音排列，可根据需要扩展）
    private static final Set<String> COMPOUND_SURNAMES = Set.of(
        "独孤",
        "公孙",
        "皇甫", "呼延",
        "令狐",
        "慕容",
        "南宫",
        "欧阳",
        "司空", "司马", "司徒", "上官",
        "拓跋",
        "万俟",
        "鲜于",
        "宇文", "尉迟",
        "长孙", "诸葛"
    );

    private final String fullName;

    private IndividualName(String fullName) {
        shouldNotBlank(fullName, "姓名不能为空");
        shouldNotLessThen(fullName, 2, "姓名长度不能小于2个字");

        this.fullName = fullName.trim();
    }

    public static IndividualName of(String fullName) {
        return new IndividualName(fullName);
    }

    // 获取姓氏
    public String getSurname() {

        String firstTwoChars = fullName.substring(0, 2);
        if (COMPOUND_SURNAMES.contains(firstTwoChars)) {
            return firstTwoChars; // 复姓
        } else {
            return fullName.substring(0, 1); // 单姓
        }
    }

    // 获取名
    public String getGivenName() {
        int surnameLength = getSurname().length();
        return fullName.substring(surnameLength); // 姓名剩余部分为名
    }

    @Override
    public String toString() {
        return fullName;
    }
}

package qiyebao.domain.orgmng.posttype;

import qiyebao.common.framework.domain.CodeEnum;
import qiyebao.domain.orgmng.orgtype.OrgTypeStatus;

public class PostType {
    public static enum Status implements CodeEnum{

        EFFECTIVE("EF", "有效"),
        TERMINATED("TE", "终止");

        private final String code;
        private final String desc;

        private Status(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static Status ofCode(String code) {
            return CodeEnum.ofCode(values(), code);
        }

        @Override
        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }
}

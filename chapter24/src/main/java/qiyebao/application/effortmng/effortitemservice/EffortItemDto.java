package qiyebao.application.effortmng.effortitemservice;

import java.util.ArrayList;
import java.util.List;

public class EffortItemDto {
    private Long effortItemId;
    private String name;
    private Type type;
    private List<EffortItemDto> subEffortItems = new ArrayList<>();

    public EffortItemDto(Long effortItemId, String name, Type type) {
        this.effortItemId = effortItemId;
        this.name = name;
        this.type = type;
    }

    public Long getEffortItemId() {
        return effortItemId;
    }


    public String getName() {
        return name;
    }

    public String getTypeCode() {
        return type.getCode();
    }


    public List<EffortItemDto> getSubEffortItems() {
        return subEffortItems;
    }

    public void addSubItem(EffortItemDto subEffortItem) {
        this.subEffortItems.add(subEffortItem);
    }

    public static enum Type {
        PROJECT("PRJ"),
        SUB_PROJECT("SUB"),
        COMMON("COM");

        private String code;

        Type(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public static Type of(String code) {
            for (Type type : Type.values()) {
                if (type.getCode().equals(code)) {
                    return type;
                }
            }
            return null;
        }
    }
}

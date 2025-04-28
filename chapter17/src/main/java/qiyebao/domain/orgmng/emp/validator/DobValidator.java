package qiyebao.domain.orgmng.emp.validator;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DobValidator {
    public void ageShouldValid(LocalDate dob) {
        // TODO 验证年龄, 可能需要从数据库获取年龄范围
    }
}

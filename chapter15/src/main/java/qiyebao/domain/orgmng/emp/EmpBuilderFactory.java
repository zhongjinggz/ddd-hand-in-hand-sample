package qiyebao.domain.orgmng.emp;

import org.springframework.stereotype.Component;
import qiyebao.domain.orgmng.emp.validator.EmpValidators;
import qiyebao.domain.orgmng.empnumcounter.EmpNumCounterRepository;
import qiyebao.domain.orgmng.org.OrgBuilder;

@Component
public class EmpBuilderFactory {
    private final EmpValidators empValidators;
    private final EmpNumCounterRepository empNumCounterRepository;

    public EmpBuilderFactory(EmpValidators empValidators
    , EmpNumCounterRepository empNumCounterRepository
    ) {
        this.empValidators = empValidators;
        this.empNumCounterRepository = empNumCounterRepository;
    }

        public EmpBuilder newBuilder () {
        return new EmpBuilder(empValidators, empNumCounterRepository);
    }
    }

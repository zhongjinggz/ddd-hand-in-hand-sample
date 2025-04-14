package qiyebao.application.orgmng.empservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qiyebao.domain.orgmng.emp.Emp;
import qiyebao.domain.orgmng.emp.EmpRepository;

@Service
public class EmpService {
    private final EmpRepository empRepository;
    private final EmpAssembler assembler;

    @Autowired
    public EmpService(EmpRepository empRepository
        , EmpAssembler assembler
    ) {
        this.empRepository = empRepository;
        this.assembler = assembler;
    }

    @Transactional
    public EmpResponse addEmp(addEmpRequest request, Long userId) {
        Emp emp = assembler.buildEmp(request, userId);

        empRepository.add(emp);
        return new EmpResponse(emp);
    }

}

package ControlCenter.entity;

import ControlCenter.annotations.ImmutableField;
import ControlCenter.dto.EmploymentDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "employment")
public class Employment {

    @Id
    @ColumnDefault("gen_random_uuid()")
    @GeneratedValue
    @Column(name = "internal_id", nullable = false)
    @ImmutableField
    private UUID internalId;

    @Column(name = "tenant", nullable = false)
    @ImmutableField
    private UUID tenant;

    @Column(name = "person", nullable = false)
    @ImmutableField
    private UUID person;

    @Column(name = "employee_id", nullable = false, length = 16)
    private String employeeId;

    @Column(name = "username", nullable = false, length = 16)
    private String username;

    @Column(name = "email", length = 126)
    private String email;

    @Column(name = "password", length = 256)
    private String password;

    @Column(name = "login_method")
    private Integer loginMethod = 0;

    @Column(name = "primary_employment", nullable = false)
    private Boolean primaryEmployment;

    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @Column(name = "termination_date")
    private LocalDate terminationDate;

    @Column(name = "freeze_access_from")
    private LocalDate freezeAccessFrom;

    @OneToMany(mappedBy = "employment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EmploymentHistory> employmentHistories;

    public void addEmploymentHistory(EmploymentHistory history) {
        if (employmentHistories == null) {
            employmentHistories = new ArrayList<>();
        }
        employmentHistories.add(history);
        history.setEmployment(this);
    }

    public void removeEmploymentHistory(EmploymentHistory history) {
        if (employmentHistories != null) {
            employmentHistories.remove(history);
        }
    }

    public static Employment fromDTO(EmploymentDTO dto) {
        Employment emp = new Employment();
        emp.setTenant(dto.getTenant());
        emp.setPerson(dto.getPerson());
        emp.setEmployeeId(dto.getEmployeeId());
        emp.setUsername(dto.getUsername());
        emp.setEmail(dto.getEmail());
        emp.setPassword(dto.getPassword());
        emp.setLoginMethod(dto.getLoginMethod());
        emp.setPrimaryEmployment(dto.getPrimaryEmployment());
        emp.setHireDate(dto.getHireDate());
        emp.setTerminationDate(dto.getTerminationDate());
        emp.setFreezeAccessFrom(dto.getFreezeAccessFrom());
        return emp;
    }
}

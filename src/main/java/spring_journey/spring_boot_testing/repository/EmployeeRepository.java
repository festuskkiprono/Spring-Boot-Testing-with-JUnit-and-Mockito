package spring_journey.spring_boot_testing.repository;

import org.hibernate.boot.jaxb.mapping.spi.JaxbPersistentAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import spring_journey.spring_boot_testing.entity.Employee;
@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {

}

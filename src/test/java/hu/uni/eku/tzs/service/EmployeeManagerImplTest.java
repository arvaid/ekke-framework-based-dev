package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.dao.EmployeeRepository;
import hu.uni.eku.tzs.dao.entity.EmployeeEntity;
import hu.uni.eku.tzs.model.Employee;
import hu.uni.eku.tzs.service.exceptions.EmployeeAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.EmployeeNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeManagerImplTest {

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    EmployeeManagerImpl service;

    @Test
    void recordEmployeeHappyPath() throws EmployeeAlreadyExistsException {
        // given
        Employee employee = TestDataProvider.getJohnDoe();
        EmployeeEntity employeeEntity = TestDataProvider.getJohnDoeEntity();
        when(employeeRepository.findById(any())).thenReturn(Optional.empty());
        when(employeeRepository.save(any())).thenReturn(employeeEntity);
        // when
        Employee actual = service.record(employee);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(employee);
    }

    @Test
    void recordEmployeeAlreadyExistsException() throws EmployeeAlreadyExistsException {
        // given
        Employee employee = TestDataProvider.getJohnDoe();
        EmployeeEntity employeeEntity = TestDataProvider.getJohnDoeEntity();
        when(employeeRepository.findById(TestDataProvider.johnDoeId))
                .thenReturn(Optional.ofNullable(employeeEntity));
        // when then
        assertThatThrownBy(() -> service.record(employee))
                .isInstanceOf(EmployeeAlreadyExistsException.class);
    }

    @Test
    void readByIdHappyPath() throws EmployeeNotFoundException {
        // given
        Employee expected = TestDataProvider.getJohnDoe();
        when(employeeRepository.findById(TestDataProvider.johnDoeId))
                .thenReturn(Optional.of(TestDataProvider.getJohnDoeEntity()));
        // when
        Employee actual = service.readById(TestDataProvider.johnDoeId);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readyByIdEmployeeNotFoundException() throws EmployeeNotFoundException {
        // given
        when(employeeRepository.findById(TestDataProvider.unknownId))
                .thenReturn(Optional.empty());
        // when then
        assertThatThrownBy(() -> service.readById(TestDataProvider.unknownId))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessageContaining(String.valueOf(TestDataProvider.unknownId));
    }

    @Test
    void readAllHappyPath() {
        // given
        List<EmployeeEntity> employeeEntities = List.of(
                TestDataProvider.getJohnDoeEntity(),
                TestDataProvider.getJaneDoeEntity()
        );
        Page<EmployeeEntity> page = new PageImpl<>(employeeEntities);
        Collection<Employee> expectedEmployees = List.of(
                TestDataProvider.getJohnDoe(),
                TestDataProvider.getJaneDoe()
        );
        when(employeeRepository.findAll(isA(Pageable.class))).thenReturn(page);
        // when
        Collection<Employee> actualEmployees = service.readAll();
        // then
        assertThat(actualEmployees)
                .usingRecursiveComparison()
                .isEqualTo(expectedEmployees);
    }

    @Test
    void modifyEmployeeHappyPath() {
        // given
        Employee employee = TestDataProvider.getJohnDoe();
        EmployeeEntity employeeEntity = TestDataProvider.getJohnDoeEntity();
        when(employeeRepository.save(employeeEntity)).thenReturn(employeeEntity);
        // when
        Employee actual = service.modify(employee);
        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(employee);
    }

    private static class TestDataProvider {
        public static final int johnDoeId = 1;
        public static final int janeDoeId = 2;

        public static final int unknownId = -1;

        public static Employee getJohnDoe() {
            return new Employee(johnDoeId, "John", "x", "Doe");
        }

        public static EmployeeEntity getJohnDoeEntity() {
            return EmployeeEntity.builder()
                    .id(johnDoeId)
                    .firstName("John")
                    .middleInitial("x")
                    .lastName("Doe")
                    .build();
        }

        public static Employee getJaneDoe() {
            return new Employee(janeDoeId, "Jane", "x", "Doe");
        }

        public static EmployeeEntity getJaneDoeEntity() {
            return EmployeeEntity.builder()
                    .id(janeDoeId)
                    .firstName("Jane")
                    .middleInitial("x")
                    .lastName("Doe")
                    .build();
        }

    }
}
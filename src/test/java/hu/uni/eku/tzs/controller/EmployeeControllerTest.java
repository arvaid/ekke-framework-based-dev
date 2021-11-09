package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.EmployeeDto;
import hu.uni.eku.tzs.controller.dto.EmployeeMapper;
import hu.uni.eku.tzs.model.Employee;
import hu.uni.eku.tzs.service.EmployeeManager;
import hu.uni.eku.tzs.service.exceptions.EmployeeAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.EmployeeNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeManager employeeManager;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeController controller;

    @Test
    void readAllHappyPath() {
        // given
        when(employeeManager.readAll()).thenReturn(List.of(TestDataProvider.getJohnDoe()));
        when(employeeMapper.employee2employeeDto(any())).thenReturn(TestDataProvider.getJohnDoeDto());
        Collection<EmployeeDto> expected = List.of(TestDataProvider.getJohnDoeDto());
        // when
        Collection<EmployeeDto> actual = controller.readAllEmployees();
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void createEmployeeHappyPath() throws EmployeeAlreadyExistsException {
        // given
        Employee johnDoe = TestDataProvider.getJohnDoe();
        EmployeeDto johnDoeDto = TestDataProvider.getJohnDoeDto();
        when(employeeMapper.employeeDto2employee(johnDoeDto)).thenReturn(johnDoe);
        when(employeeManager.record(johnDoe)).thenReturn(johnDoe);
        when(employeeMapper.employee2employeeDto(johnDoe)).thenReturn(johnDoeDto);
        // when
        EmployeeDto actual = controller.create(johnDoeDto);
        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(johnDoeDto);
    }

    @Test
    void createEmployeeThrowsEmployeeAlreadyExistsException() throws EmployeeAlreadyExistsException {
        // given
        Employee johnDoe = TestDataProvider.getJohnDoe();
        EmployeeDto johnDoeDto = TestDataProvider.getJohnDoeDto();
        when(employeeMapper.employeeDto2employee(johnDoeDto)).thenReturn(johnDoe);
        when(employeeManager.record(johnDoe)).thenThrow(new EmployeeAlreadyExistsException());
        // when then
        assertThatThrownBy(() -> controller.create(johnDoeDto)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void deleteFromQueryParamHappyPath() throws EmployeeNotFoundException {
        // given
        Employee johnDoe = TestDataProvider.getJohnDoe();
        when(employeeManager.readById(TestDataProvider.johnDoeId)).thenReturn(johnDoe);
        doNothing().when(employeeManager).delete(johnDoe);
        // when
        controller.delete(TestDataProvider.johnDoeId);
        // then is not necessary, mock are checked by default
    }

    @Test
    void deleteFromQueryParamWhenEmployeeNotFound() throws EmployeeNotFoundException {
        // given
        final int notFoundEmployeeId = TestDataProvider.unknownId;
        doThrow(new EmployeeNotFoundException(String.format("Cannot find employee with ID %d", notFoundEmployeeId)))
                .when(employeeManager).readById(notFoundEmployeeId);
        // when then
        assertThatThrownBy(() -> controller.delete(notFoundEmployeeId))
                .isInstanceOf(ResponseStatusException.class);
    }

    private static class TestDataProvider {

        public static final int johnDoeId = 1;
        public static final int janeDoeId = 2;

        public static final int unknownId = -1;

        public static Employee getJohnDoe() {
            return new Employee(johnDoeId, "John", "x", "Doe");
        }

        public static EmployeeDto getJohnDoeDto() {
            return EmployeeDto.builder()
                    .id(johnDoeId)
                    .firstName("John")
                    .middleInitial("x")
                    .lastName("Doe")
                    .build();
        }

        public static Employee getJaneDoe() {
            return new Employee(janeDoeId, "Jane", "x", "Doe");
        }

        public static EmployeeDto getJaneDoeDto() {
            return EmployeeDto.builder()
                    .id(johnDoeId)
                    .firstName("Jane")
                    .middleInitial("x")
                    .lastName("Doe")
                    .build();
        }
    }
}
package com.example.empsched.employee.service.impl;

import com.example.empsched.employee.entity.Employee;
import com.example.empsched.employee.entity.Organisation;
import com.example.empsched.employee.exception.EmployeeLimitReachedException;
import com.example.empsched.employee.exception.EmployeeNotFoundException;
import com.example.empsched.employee.exception.OrganisationNotFoundException;
import com.example.empsched.employee.repository.EmployeeRepository;
import com.example.empsched.employee.repository.OrganisationRepository;
import com.example.empsched.employee.service.EmployeeService;
import com.example.empsched.employee.service.StorageService;
import com.example.empsched.shared.util.BaseThrowChecks;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final OrganisationRepository organisationRepository;
    private final StorageService storageService;

    @Override
    public Page<Employee> getAllEmployees(final UUID organisationId, final Pageable pageable) {
        return employeeRepository.findAllByOrganisationId(organisationId, pageable);
    }

    @Override
    public Employee createEmployee(final Employee employee, final UUID organisationId) {
        final Organisation organisation = organisationRepository.findById(organisationId)
                .orElseThrow(() -> new OrganisationNotFoundException(organisationId));

        final int maxEmployees = organisation.getPlan().getMaxEmployees();
        final int currentEmployeeCount = employeeRepository.countByOrganisationId(organisationId);

        if (currentEmployeeCount >= maxEmployees) {
            throw new EmployeeLimitReachedException(organisation.getId(), maxEmployees);
        }

        employee.setOrganisation(organisation);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployeeById(final UUID employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
    }

    @Override
    public void deleteEmployee(UUID employeeId, UUID organisationId) {
        Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);
        if (employeeOpt.isEmpty()) {
            return;
        }
        BaseThrowChecks.throwIfNotRelated(organisationId, employeeOpt.get().getOrganisation().getId());
        employeeRepository.deleteById(employeeId);
    }

    @Override
    public byte[] getProfilePictureOfEmployee(UUID employeeId) {
        final Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        if (employee.getProfilePicturePath() == null || employee.getProfilePicturePath().isBlank()) {
            return new byte[0];
        }
        return storageService.downloadFile(employee.getProfilePicturePath());
    }

    @Override
    public Employee updateProfilePictureToEmployee(final UUID employeeId, final MultipartFile pictureData) {
        final Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        final String fileType = pictureData.getOriginalFilename()
                .substring(pictureData.getOriginalFilename().lastIndexOf('.') + 1);

        if (employee.getProfilePicturePath() != null && !employee.getProfilePicturePath().isBlank()) {
            storageService.deleteFile(employee.getProfilePicturePath());
        }

        final String profilePicturePath = storageService.uploadProfilePicture(pictureData, employeeId, fileType);
        employee.setProfilePicturePath(profilePicturePath);
        return employeeRepository.save(employee);
    }
}

package com.jwcomptech.commons.javafx.wizard.services;

import com.jwcomptech.commons.javafx.wizard.data.Company;
import com.jwcomptech.commons.utils.SingletonManager;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
public class CompanyService {
    private final List<Company> companies = new ArrayList<>();

    public CompanyService() {
        companies.add(new Company("Alpha Corp", "/styles/alphaCorp.css"));
        companies.add(new Company("Beta LLC", "/styles/betaLLC.css"));
    }

    public List<Company> getCompanies() {
        return Collections.unmodifiableList(companies);
    }

    public Optional<Company> findById(String id) {
        return companies.stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    public static CompanyService getInstance() {
        return SingletonManager.getInstance(CompanyService.class, CompanyService::new);
    }
}

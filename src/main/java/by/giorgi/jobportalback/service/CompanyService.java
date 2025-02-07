package by.giorgi.jobportalback.service;

import by.giorgi.jobportalback.model.entity.Company;
import by.giorgi.jobportalback.repository.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("companyService")
@AllArgsConstructor
public class CompanyService implements UserDetailsService {

    private final CompanyRepository companyRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Company company = companyRepository.findByCompanyEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return company;
    }
}

package org.sid.service;

import org.sid.dao.AppRoleRepository;
import org.sid.dao.AppUserRepository;
import org.sid.entities.AppRole;
import org.sid.entities.AppUser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {


    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AccountServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.appUserRepository = appUserRepository;
        this.appRoleRepository = appRoleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    public AppUser saveUser(String userName, String password, String confirmedPassword) {

        //Voir si l'utilisateur existe ou pas

        AppUser user = appUserRepository.findByUsername(userName);
        if(user != null )
            throw new RuntimeException("User already exists");
        //Voir si le password est le même
        if(!password.equals(confirmedPassword))
            throw new RuntimeException("Please confirm your password");

        AppUser appUser = new AppUser();
        appUser.setUsername(userName);
        appUser.setPassword(bCryptPasswordEncoder.encode(password));
        appUser.setActivated(true);
        appUserRepository.save(appUser);
        AppRole appRole = appRoleRepository.findByRoleName("USER");
        appUser.getRoles().add(appRole);
        return appUser;
    }

    @Override
    public AppRole saveRole(AppRole role) {
        return appRoleRepository.save(role);
    }

    @Override
    public AppUser loadUserByUserName(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        AppUser appUser = appUserRepository.findByUsername(username);
        AppRole appRole = appRoleRepository.findByRoleName(roleName);
        System.out.println("****"+appRole);
        System.out.println("****"+appUser);

        appUser.getRoles().add(appRole);
    }
}

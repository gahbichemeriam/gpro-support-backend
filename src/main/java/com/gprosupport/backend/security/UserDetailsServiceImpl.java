package com.gprosupport.backend.security;

import com.gprosupport.backend.utilisateur.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implémentation de UserDetailsService pour Spring Security.
 *
 * Spring Security appelle loadUserByUsername() lors de l'authentification
 * pour récupérer l'utilisateur depuis la base de données.
 *
 * Ici, le "username" = l'email de l'utilisateur.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return utilisateurRepository.findByEmail(email)
                .orElseThrow(() ->
                    new UsernameNotFoundException("Utilisateur introuvable avec l'email : " + email)
                );
    }
}

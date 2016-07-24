/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.userDetail;

import com.sistem.proyecto.entity.Rol;
import com.sistem.proyecto.entity.RolPermiso;
import com.sistem.proyecto.entity.Usuario;
import com.sistem.proyecto.manager.RolPermisoManager;
import com.sistem.proyecto.manager.UsuarioManager;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author Miguel
 */
public class UserSession implements AuthenticationProvider {

    //@EJB(mappedName = "java:global/sistem/sistem-ejb/UsuarioManagerImpl")
    private UsuarioManager usuarioManager;
    private RolPermisoManager rolPermisoManager;

    private Context context;

    private IfaceLogin serviceLogin;

    /**
     * @return the serviceLogin
     */
    public IfaceLogin getServiceLogin() {
        return serviceLogin;
    }

    /**
     * @param serviceLogin the serviceLogin to set
     */
    public void setServiceLogin(IfaceLogin serviceLogin) {
        this.serviceLogin = serviceLogin;
    }

    public Usuario userExist(Usuario user) {
        Usuario ejUsuario = new Usuario();
        try {
            inicializarUsuarioManager();
            ejUsuario.setAlias(user.getAlias());
            ejUsuario.setClaveAcceso(user.getClaveAcceso());

            ejUsuario = usuarioManager.get(ejUsuario);
            if (ejUsuario != null && ejUsuario.getId() != null) {
                return ejUsuario;
            } else {
                return ejUsuario;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error" + ex);
            return ejUsuario;
        }
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        inicializarUsuarioManager();
        inicializarRolPermisoManager();
        String userLogin = authentication.getPrincipal().toString();
        String passwordLogin = authentication.getCredentials().toString();
        System.out.println("User: " + userLogin);
        System.out.println("Password: " + passwordLogin);
        Usuario user = new Usuario();
        user = usuarioManager.loginSistema(userLogin, passwordLogin);

        if (user != null && user.getId() != null) {
            List<GrantedAuthority> autoridades = new ArrayList<GrantedAuthority>();
            boolean superUsuario = false;
            UserDetail userDetails = new UserDetail();
            if (user.isSuperUsuario()) {
                
                userDetails.setSuperUsuario(true);
                userDetails.setNombreRol("Super Usuario");
                autoridades.add(new SimpleGrantedAuthority("SuperUsuario"));
                autoridades.add(new SimpleGrantedAuthority("Usuario.Crear"));
                autoridades.add(new SimpleGrantedAuthority("Usuario.Listar"));
                autoridades.add(new SimpleGrantedAuthority("Usuario.Visualizar"));
                autoridades.add(new SimpleGrantedAuthority("Usuario.Editar"));
                autoridades.add(new SimpleGrantedAuthority("Usuario.Desactivar"));
                autoridades.add(new SimpleGrantedAuthority("Usuario.Activar"));
                autoridades.add(new SimpleGrantedAuthority("Usuario.AsignarRol"));        
            } else {
                
                RolPermiso ejemplo = new RolPermiso();
                ejemplo.setRol(new Rol(user.getRol().getId()));

                List<Map<String, Object>> listMapPermisos = rolPermisoManager.listAtributos(ejemplo, "id,rol.id,permiso.nombre".split(","), true);

                for (Map<String, Object> rpm : listMapPermisos) {
                    
                    autoridades.add(new SimpleGrantedAuthority(rpm.get("permiso.nombre").toString()));
                    
                }
                
                userDetails.setNombreRol(user.getRol().getNombre());
                userDetails.setIdEmpresa(user.getEmpresa().getId());
            }

            userDetails.setUsername(user.getAlias());
            userDetails.setPassword(passwordLogin);
            userDetails.setNombre(user.getNombre() + " " + user.getApellido());
            userDetails.setId(user.getId());

            Authentication customAuthentication = new UsernamePasswordAuthenticationToken(userDetails,
                    passwordLogin, autoridades);
            return customAuthentication;
        } else {
            System.out.println("Usuario o Contraseña Inválidos.");
            throw new BadCredentialsException("Usuario o Contraseña Inválidos.");
        }
    }

    @Override
    public boolean supports(Class<?> type) {
        return true;
    }

    private void inicializarUsuarioManager() {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (NamingException e1) {
                throw new RuntimeException("No se puede inicializar el contexto", e1);
            }
        }
        if (usuarioManager == null) {
            try {

                usuarioManager = (UsuarioManager) context.lookup("java:app/proyecto-ejb/UsuarioManagerImpl");
            } catch (NamingException ne) {
                throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
            }
        }
    }

    private void inicializarRolPermisoManager() {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (NamingException e1) {
                throw new RuntimeException("No se puede inicializar el contexto", e1);
            }
        }
        if (rolPermisoManager == null) {
            try {

                rolPermisoManager = (RolPermisoManager) context.lookup("java:app/proyecto-ejb/RolPermisoManagerImpl");
            } catch (NamingException ne) {
                throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
            }
        }
    }

}

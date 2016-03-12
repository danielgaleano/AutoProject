 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.userDetail;



import com.sistem.proyecto.entity.Usuario;
import java.util.List;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 *
 * @author miguel ojeda
 */
@Service
public class ServiceLogin  implements IfaceLogin {
    EntityManager em;
    
    @Override
    public Usuario validarLogin(Usuario obj) throws Exception {
        
        Query query = em.createNamedQuery("UsuarioExterno.findByUsuarioAndPassword");
//        query.setParameter("usuario", obj.getUsuario());
//        query.setParameter("password", obj.getClaveAcceso());
//        
        List lista = query.getResultList();
        
        Usuario ejEmisor = new Usuario();
        //ejEmisor.setUsuario("Jose");
        return ejEmisor;
    }
}

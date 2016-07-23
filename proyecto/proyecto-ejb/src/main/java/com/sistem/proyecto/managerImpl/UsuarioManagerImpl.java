/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Permiso;
import com.sistem.proyecto.entity.Rol;
import com.sistem.proyecto.entity.Usuario;
import com.sistem.proyecto.manager.EmpresaManager;
import com.sistem.proyecto.manager.PermisoManager;
import com.sistem.proyecto.manager.RolManager;
import com.sistem.proyecto.manager.UsuarioManager;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Miguel
 */
@Stateless
public class UsuarioManagerImpl extends GenericDaoImpl<Usuario, Long>
		implements UsuarioManager{
    
    @EJB(mappedName = "java:app/proyecto-ejb/EmpresaManagerImpl")
    private EmpresaManager empresaManager;
    
    @EJB(mappedName = "java:app/proyecto-ejb/RolManagerImpl")
    private RolManager rolManager;
    
    @EJB(mappedName = "java:app/proyecto-ejb/PermisoManagerImpl")
    private PermisoManager permisoManager;
    
    @Override
    protected Class<Usuario> getEntityBeanType() {
            return Usuario.class;
    }

    @Override
    public Usuario loginSistema(String alias, String paswword){
        Usuario ejUsuario = new Usuario();
        
        try{
            List<Map<String, Object>> usuarios = this.listAtributos(ejUsuario,"id".split(","));
            
            String[] entidades = "Cliente,Usuario,Proveedor,Vehiculo".split(",");
            String[] permisos = "Crear,Editar,Visualizar,Activar,Desactivar,Listar".split(",");
            
            if(usuarios != null && !usuarios.isEmpty()){
                ejUsuario = new Usuario();
                ejUsuario.setAlias(alias);
                ejUsuario.setClaveAcceso(paswword);
                
                ejUsuario =  this.get(ejUsuario);
                if(ejUsuario != null && ejUsuario.getId() != null) {
                    return ejUsuario;
                } else {
                    return ejUsuario;
                }
            }else{
                
                ejUsuario = new Usuario();
                ejUsuario.setNombre("Ramon Daniel");
                ejUsuario.setApellido("Galeano Bate");
                ejUsuario.setDocumento("4576708");
                ejUsuario.setAlias("dbate");
                ejUsuario.setClaveAcceso("12345678");
                ejUsuario.setActivo("S");
                ejUsuario.setEmail("daniel@gamil.com");
                ejUsuario.setTelefono("0981777201");
                ejUsuario.setSuperUsuario(true);
                ejUsuario.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                ejUsuario.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                
                this.save(ejUsuario);
                
                ejUsuario = new Usuario();

                ejUsuario.setDocumento("45376535");
                ejUsuario.setNombre("Victor Poro");
                ejUsuario.setApellido("Santos");
                ejUsuario.setAlias("vsantos");
                ejUsuario.setClaveAcceso("12345678");
                ejUsuario.setActivo("S");
                ejUsuario.setEmail("santos@gamil.com");
                ejUsuario.setTelefono("0981999999");
                ejUsuario.setSuperUsuario(true);
                ejUsuario.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                ejUsuario.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                
                this.save(ejUsuario);
                
                ejUsuario = new Usuario();
                
                ejUsuario.setNombre("Miguel Angel");
                ejUsuario.setDocumento("45356575");
                ejUsuario.setApellido("Ojeda Avalos");
                ejUsuario.setAlias("mojeda");
                ejUsuario.setClaveAcceso("12345678");
                ejUsuario.setActivo("S");
                ejUsuario.setEmail("miguel@gamil.com");
                ejUsuario.setTelefono("0981999999");
                ejUsuario.setSuperUsuario(true);
                ejUsuario.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                ejUsuario.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                
                this.save(ejUsuario); 
                
                for (String entidade : entidades) {
                    for (String permiso : permisos) {
                        Permiso ejPermiso = new Permiso();
                        ejPermiso.setNombre(entidade + "." + permiso);
                        permisoManager.save(ejPermiso);
                    }
                }
                
                ejUsuario = new Usuario();
                ejUsuario.setAlias(alias);
                ejUsuario.setClaveAcceso(paswword);
                
                ejUsuario =  this.get(ejUsuario);
                if(ejUsuario != null && ejUsuario.getId() != null) {
                    return ejUsuario;
                } else {
                    return ejUsuario;
                }
            
            }
            
        }catch (Exception ex){
            System.err.println("Error al crear Usuarios Nuevos. " + ex);
        }
        return ejUsuario;
    }
        
    

}

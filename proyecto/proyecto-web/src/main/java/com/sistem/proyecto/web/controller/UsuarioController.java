/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.web.controller;

import com.google.gson.Gson;
import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Imagen;
import com.sistem.proyecto.entity.Usuario;
import com.sistem.proyecto.entity.Rol;
import com.sistem.proyecto.userDetail.UserDetail;
import com.sistem.proyecto.utils.Base64Bytes;
import com.sistem.proyecto.utils.FilterDTO;
import com.sistem.proyecto.utils.MensajeDTO;
import com.sistem.proyecto.utils.ReglaDTO;
import com.sistem.proyecto.utils.SendMail;
import static com.sistem.proyecto.web.controller.BaseController.logger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import py.com.pronet.utils.DTORetorno;

/**
 *
 * @author Miguel
 */
@Controller
@RequestMapping(value = "/usuarios")
public class UsuarioController extends BaseController {

    String atributos = "id,nombre,apellido,documento,email,claveAcceso,telefono,telefonoMovil,alias,rol.id,"
            + "rol.nombre,empresa.id,empresa.nombre,direccion,activo,superUsuario";

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listaUsuarios(Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("usuariosListar");
        
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        List<Map<String, Object>> listMapUsuarios;
        Empresa ejemplo = new Empresa();
        Usuario ejUsuario = new Usuario();

        try {
//            inicializarUsuarioManager();
//            System.out.println(userDetail.getNombre());
//             
//            if(userDetail.isSuperUsuario()){
//                ejUsuario.setSuperUsuario(Boolean.FALSE);
//                listMapUsuarios = usuarioManager.listAtributos(ejUsuario, atributos.split(","), true);
//
//            }else{
//                ejUsuario.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
//                listMapUsuarios = usuarioManager.listAtributos(ejUsuario, atributos.split(","), true);
//
//            }
//            for (Map<String, Object> rpm : listMapUsuarios) {
//                rpm.put("rolNombre", rpm.get("rol.nombre"));
//                rpm.put("empresaNombre", rpm.get("empresa.nombre"));
//            }
//
//            model.addAttribute("usuarios", listMapUsuarios);

        } catch (Exception ex) {

        }

        return retorno;

    }
    
    @RequestMapping(value = "/listar", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno listar(@ModelAttribute("_search") boolean filtrar,
            @ModelAttribute("filters") String filtros,
            @ModelAttribute("page") Integer pagina,
            @ModelAttribute("rows") Integer cantidad,
            @ModelAttribute("sidx") String ordenarPor,
            @ModelAttribute("sord") String sentidoOrdenamiento,
            @ModelAttribute("todos") boolean todos) {

        DTORetorno retorno = new DTORetorno();
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        ordenarPor = "nombre";
        Usuario ejemplo = new Usuario();
        List<Map<String, Object>> listMapGrupos = null;
        try {

            inicializarUsuarioManager();

            Gson gson = new Gson();
            String camposFiltros = null;
            String valorFiltro = null;

            if (filtrar) {
                FilterDTO filtro = gson.fromJson(filtros, FilterDTO.class);
                if (filtro.getGroupOp().compareToIgnoreCase("OR") == 0) {
                    for (ReglaDTO regla : filtro.getRules()) {
                        if (camposFiltros == null) {
                            camposFiltros = regla.getField();
                            valorFiltro = regla.getData();
                        } else {
                            camposFiltros += "," + regla.getField();
                        }
                    }
                } else {
                    //ejemplo = generarEjemplo(filtro, ejemplo);
                }

            }
            // ejemplo.setActivo("S");

            pagina = pagina != null ? pagina : 1;
            Integer total = 0;

            if (!todos) {
                total = usuarioManager.list(ejemplo, true).size();
            }

            Integer inicio = ((pagina - 1) < 0 ? 0 : pagina - 1) * cantidad;

            if (total < inicio) {
                inicio = total - total % cantidad;
                pagina = total / cantidad;
            }
            
            if(userDetail.isSuperUsuario()){
                ejemplo.setSuperUsuario(Boolean.FALSE);

                listMapGrupos = usuarioManager.listAtributos(ejemplo, atributos.split(","), todos, inicio, cantidad, 
                    ordenarPor.split(","), sentidoOrdenamiento.split(","), true, true, camposFiltros, valorFiltro, 
                    null, null, null, null, null, null, null, null, true);

            }else{
                ejemplo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
                listMapGrupos = usuarioManager.listAtributos(ejemplo, atributos.split(","), todos, inicio, cantidad, 
                    ordenarPor.split(","), sentidoOrdenamiento.split(","), true, true, camposFiltros, valorFiltro, 
                    null, null, null, null, null, null, null, null, true);

            }
                                       
                    
            if (todos) {
                total = listMapGrupos.size();
            }
            Integer totalPaginas =  total / cantidad;
            
            retorno.setTotal(totalPaginas+1);
            retorno.setRetorno(listMapGrupos);
            retorno.setPage(pagina);

        } catch (Exception e) {

            logger.error("Error al listar", e);
        }

        return retorno;
    }

    @RequestMapping(value = "/crear", method = RequestMethod.GET)
    public ModelAndView crear(Model model) {
        try {
            inicializarEmpresaManager();
            model.addAttribute("tipo", "Crear");
            model.addAttribute("editar", false);
            List<Map<String, Object>> listMapEmpresas = empresaManager.listAtributos(new Empresa(), "id,nombre".split(","), true);
            model.addAttribute("empresas", listMapEmpresas);

        } catch (Exception ex) {

        }
        return new ModelAndView("usuario");

    }

    @RequestMapping(value = "/guardar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO guardar(@ModelAttribute("Usuario") Usuario usuarioRecibido) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO mensaje = new MensajeDTO();
        Usuario ejUsuario = new Usuario();
        try {
            inicializarUsuarioManager();
            inicializarImagenManager();

            if (usuarioRecibido.getDocumento() == null || usuarioRecibido.getDocumento() != null
                    && usuarioRecibido.getDocumento().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El documento del usuario no puede estar vacio.");
                return mensaje;
            }

            if (usuarioRecibido.getNombre() == null || usuarioRecibido.getNombre() != null
                    && usuarioRecibido.getNombre().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El nombre del usuario no puede estar vacio.");
                return mensaje;
            }

            if (usuarioRecibido.getApellido() == null || usuarioRecibido.getApellido() != null
                    && usuarioRecibido.getApellido().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El apellido del usuario no puede estar vacio.");
                return mensaje;
            }

            if (usuarioRecibido.getAlias() == null || usuarioRecibido.getAlias() != null
                    && usuarioRecibido.getAlias().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El alias del usuario no puede estar vacio.");
                return mensaje;
            }

            if (usuarioRecibido.getClaveAcceso() == null || usuarioRecibido.getClaveAcceso() != null
                    && usuarioRecibido.getClaveAcceso().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("La clave del usuario no puede estar vacia.");
                return mensaje;
            }

            if (usuarioRecibido.getTelefono() == null || usuarioRecibido.getTelefono() != null
                    && usuarioRecibido.getTelefono().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El telefono del usuario no puede estar vacia.");
                return mensaje;
            }

            Imagen imagenP = null;

            String imagenPortada = usuarioRecibido.getImagenPort();

            ejUsuario.setDocumento(usuarioRecibido.getDocumento());

            Map<String, Object> usuarioDocumento = usuarioManager.getLike(ejUsuario,"id".split(","));

            if (usuarioDocumento != null && !usuarioDocumento.isEmpty()) {
                mensaje.setError(true);
                mensaje.setMensaje("El numero de documento ya se encuentra registrado.");
                return mensaje;

            }

            ejUsuario = new Usuario();
            ejUsuario.setAlias(usuarioRecibido.getAlias());

            Map<String, Object> usuarioAlias = usuarioManager.getLike(ejUsuario,"id".split(","));

            if (usuarioAlias != null && !usuarioAlias.isEmpty()) {
                mensaje.setError(true);
                mensaje.setMensaje("El alias ya se encuentra registrado.");
                return mensaje;

            }
            ejUsuario = new Usuario();

            if (userDetail.isSuperUsuario()) {

                ejUsuario.setEmpresa(new Empresa(usuarioRecibido.getEmpresa().getId()));

            } else {

                ejUsuario.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

            }

            ejUsuario.setActivo("S");
            ejUsuario.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
            ejUsuario.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            ejUsuario.setAlias(usuarioRecibido.getAlias());
            ejUsuario.setClaveAcceso(usuarioRecibido.getClaveAcceso());
            ejUsuario.setDireccion(usuarioRecibido.getDireccion());
            ejUsuario.setDocumento(usuarioRecibido.getDocumento());
            ejUsuario.setEmail(usuarioRecibido.getEmail());
            ejUsuario.setNombre(usuarioRecibido.getNombre());
            ejUsuario.setApellido(usuarioRecibido.getApellido());
            ejUsuario.setTelefono(usuarioRecibido.getTelefono());
            ejUsuario.setTelefonoMovil(usuarioRecibido.getTelefonoMovil());

            ejUsuario.setSuperUsuario(false);

            usuarioManager.save(ejUsuario);

            if (imagenPortada != null && !imagenPortada.equals("")
                    && imagenPortada.length() > 0) {
                imagenP = new Imagen();
                imagenP.setImagen(Base64Bytes.decode(imagenPortada.split(",")[1]));
                String extension = imagenPortada.split(";")[0];
                extension = extension.substring(extension.indexOf("/") + 1);
                imagenP.setNombreTabla("usuario");
                imagenP.setNombreImagen(usuarioRecibido.getNombre() + "." + extension);
                imagenP.setActivo("S");
                imagenP.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                imagenP.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                imagenP.setEmpresa(new Empresa(usuarioRecibido.getEmpresa().getId()));
                imagenP.setEntidadId(ejUsuario.getId());

                imagenManager.save(imagenP);

            }

            mensaje.setError(false);
            mensaje.setMensaje("El usuario " + usuarioRecibido.getAlias() + " se guardo exitosamente.");

        } catch (Exception e) {
            mensaje.setError(true);
            mensaje.setMensaje("Error a guardar el usuario");
            System.out.println("Error" + e);
        }

        return mensaje;
    }

    @RequestMapping(value = "/editar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO editar(@ModelAttribute("Usuario") Usuario usuarioRecibido) {
        MensajeDTO mensaje = new MensajeDTO();
        Usuario ejUsuario = new Usuario();
        try {
            inicializarUsuarioManager();
            inicializarImagenManager();
            if (usuarioRecibido.getId() == null) {
                mensaje.setError(true);
                mensaje.setMensaje("Error al editar el usuario.");
                return mensaje;
            }

            if (usuarioRecibido.getNombre() == null || usuarioRecibido.getNombre() != null
                    && usuarioRecibido.getNombre().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El nombre del usuario no puede estar vacio.");
                return mensaje;
            }

            if (usuarioRecibido.getApellido() == null || usuarioRecibido.getApellido() != null
                    && usuarioRecibido.getApellido().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El apellido del usuario no puede estar vacio.");
                return mensaje;
            }

            if (usuarioRecibido.getClaveAcceso() == null || usuarioRecibido.getClaveAcceso() != null
                    && usuarioRecibido.getClaveAcceso().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("La clave del usuario no puede estar vacia.");
                return mensaje;
            }

            if (usuarioRecibido.getTelefono() == null || usuarioRecibido.getTelefono() != null
                    && usuarioRecibido.getTelefono().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El telefono del usuario no puede estar vacia.");
                return mensaje;
            }

            Usuario ejUsuarioUp = new Usuario();
            ejUsuarioUp = usuarioManager.get(usuarioRecibido.getId());

            Imagen imagenP = null;

            String imagenPortada = usuarioRecibido.getImagenPort();

            if (imagenPortada != null && !imagenPortada.equals("")
                    && imagenPortada.length() > 0) {
                imagenP = new Imagen();
                imagenP.setEntidadId(usuarioRecibido.getId());
                imagenP.setNombreTabla("usuario");
                imagenP = imagenManager.get(imagenP);

                if (imagenP != null) {

                    imagenP.setImagen(Base64Bytes.decode(imagenPortada.split(",")[1]));
                    imagenP.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                    String extension = imagenPortada.split(";")[0];
                    extension = extension.substring(extension.indexOf("/") + 1);
                    imagenP.setNombreImagen(usuarioRecibido.getNombre() + "." + extension);

                    imagenManager.update(imagenP);

                } else {
                    imagenP = new Imagen();
                    imagenP.setImagen(Base64Bytes.decode(imagenPortada.split(",")[1]));
                    String extension = imagenPortada.split(";")[0];
                    extension = extension.substring(extension.indexOf("/") + 1);
                    imagenP.setNombreTabla("usuario");
                    imagenP.setNombreImagen(usuarioRecibido.getNombre() + "." + extension);
                    imagenP.setActivo("S");
                    imagenP.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                    imagenP.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                    imagenP.setEmpresa(ejUsuarioUp.getEmpresa());
                    imagenP.setEntidadId(usuarioRecibido.getId());

                    imagenManager.save(imagenP);
                }
            }

            ejUsuarioUp.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            ejUsuarioUp.setClaveAcceso(usuarioRecibido.getClaveAcceso());
            ejUsuarioUp.setDireccion(usuarioRecibido.getDireccion());
            ejUsuarioUp.setEmail(usuarioRecibido.getEmail());
            ejUsuarioUp.setNombre(usuarioRecibido.getNombre());
            ejUsuarioUp.setApellido(usuarioRecibido.getApellido());
            ejUsuarioUp.setTelefono(usuarioRecibido.getTelefono());
            ejUsuarioUp.setTelefonoMovil(usuarioRecibido.getTelefonoMovil());

            usuarioManager.update(ejUsuarioUp);

            mensaje.setError(false);
            mensaje.setMensaje("El usuario " + usuarioRecibido.getNombre() + " se modifico exitosamente.");
            return mensaje;

        } catch (Exception e) {
            mensaje.setError(true);
            mensaje.setMensaje("Error a guardar el usuario");
            System.out.println("Error" + e);
        }

        return mensaje;
    }

    @RequestMapping(value = "/asignar/{id}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView listarRoles(@PathVariable("id") Long id, Model model) {
        ModelAndView retorno = new ModelAndView();

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        try {
            inicializarRolManager();

            inicializarUsuarioManager();

            Usuario ejUsuario = new Usuario();
            ejUsuario.setId(id);

            Map<String, Object> usuarios = usuarioManager.getAtributos(ejUsuario, atributos.split(","));
            usuarios.put("rolNombre", usuarios.get("rol.nombre"));
            Rol ejemplo = new Rol();
            ejemplo.setEmpresa(new Empresa(Long.parseLong(usuarios.get("empresa.id").toString())));
            ejemplo.setActivo("S");

            List<Map<String, Object>> listMapRoles = rolManager.listAtributos(ejemplo, "id,nombre".split(","), true);

            model.addAttribute("roles", listMapRoles);
            model.addAttribute("usuario", usuarios);
            retorno.setViewName("asignarRol");

        } catch (Exception ex) {
            System.out.println("Error " + ex);
        }

        return retorno;
    }

    @RequestMapping(value = "/asignar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO asignar(@ModelAttribute("Usuario") Usuario usuarioRecibido) {
        MensajeDTO mensaje = new MensajeDTO();
        Usuario ejUsuario = new Usuario();
        try {
            inicializarUsuarioManager();

            if (usuarioRecibido.getRol() == null || usuarioRecibido.getRol() != null
                    && usuarioRecibido.getRol().getId() == null) {
                mensaje.setError(true);
                mensaje.setMensaje("El rol del usuario no puede estar vacio.");
                return mensaje;
            }

            if (usuarioRecibido.getId() != null) {

                Usuario ejUsuarioUp = new Usuario();
                ejUsuarioUp = usuarioManager.get(usuarioRecibido.getId());
                ejUsuarioUp.setRol(usuarioRecibido.getRol());

                usuarioManager.update(ejUsuarioUp);

                mensaje.setError(false);
                mensaje.setMensaje("El rol se asigno exitosamente.");
                return mensaje;
            }

        } catch (Exception e) {
            mensaje.setError(true);
            mensaje.setMensaje("Error al asignar el rol.");
            System.out.println("Error" + e);
        }

        return mensaje;
    }

    @RequestMapping(value = "/desactivar/{id}", method = RequestMethod.GET)
    public @ResponseBody
    MensajeDTO desactivar(@PathVariable("id") Long id) {
        MensajeDTO retorno = new MensajeDTO();
        String nombre = "";

        try {

            inicializarUsuarioManager();

            Usuario usuario = usuarioManager.get(id);

            if (usuario != null) {
                nombre = usuario.getNombre().toString();
            }

            if (usuario != null && usuario.getActivo().toString()
                    .compareToIgnoreCase("N") == 0) {
                retorno.setError(true);
                retorno.setMensaje("El usuario " + nombre + " ya se encuentra desactivada.");
            }
            usuario.setActivo("N");
            usuario.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            usuario.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));

            usuarioManager.update(usuario);

            retorno.setError(false);
            retorno.setMensaje("El Usuario " + nombre + " se desactivo exitosamente.");

        } catch (Exception e) {
            retorno.setError(true);
            retorno.setMensaje("Error al tratar de desactivar el usuario.");
        }

        return retorno;

    }

    @RequestMapping(value = "/activar/{id}", method = RequestMethod.GET)
    public @ResponseBody
    MensajeDTO activar(@PathVariable("id") Long id) {
        MensajeDTO retorno = new MensajeDTO();
        String nombre = "";

        try {

            inicializarUsuarioManager();

            Usuario usuario = usuarioManager.get(id);

            if (usuario != null) {
                nombre = usuario.getNombre().toString();
            }

            if (usuario != null && usuario.getActivo().toString()
                    .compareToIgnoreCase("N") == 0) {
                retorno.setError(true);
                retorno.setMensaje("El usuario " + nombre + " ya se encuentra activo.");
            }
            usuario.setActivo("S");
            usuario.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            usuario.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));

            usuarioManager.update(usuario);

            retorno.setError(false);
            retorno.setMensaje("El usuario " + nombre + " se activo exitosamente.");

        } catch (Exception e) {
            retorno.setError(true);
            retorno.setMensaje("Error al tratar de activar el usuario.");
        }

        return retorno;

    }

    @RequestMapping(value = "/editar/{id}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView editar(@PathVariable("id") Long id, Model model) {
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        ModelAndView retorno = new ModelAndView();
        String nombre = "";

        try {

            inicializarUsuarioManager();
            inicializarEmpresaManager();

            Map<String, Object> usuario = usuarioManager.getAtributos(
                    new Usuario(id), atributos.split(","), false, true);
            
            usuario.put("idEmpresa", Long.parseLong(usuario.get("empresa.id").toString()));

            List<Map<String, Object>> listMapEmpresas = empresaManager.listAtributos(new Empresa(), "id,nombre".split(","), true);
            model.addAttribute("empresas", listMapEmpresas);

            model.addAttribute("usuario", usuario);

            model.addAttribute("editar", true);

            retorno.setViewName("usuario");
        } catch (Exception e) {

            System.out.println("Error" + e);
        }

        return retorno;

    }

    @RequestMapping(value = "/visualizar/{id}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView visualizar(@PathVariable("id") Long id, Model model) {
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        ModelAndView retorno = new ModelAndView();
        String nombre = "";

        try {

            inicializarUsuarioManager();
            inicializarEmpresaManager();

            Map<String, Object> usuario = usuarioManager.getAtributos(
                    new Usuario(id), atributos.split(","), false, true);
                        
            usuario.put("idEmpresa", Long.parseLong(usuario.get("empresa.id").toString()));

            List<Map<String, Object>> listMapEmpresas = empresaManager.listAtributos(new Empresa(), "id,nombre".split(","), true);
            model.addAttribute("empresas", listMapEmpresas);

            model.addAttribute("usuario", usuario);

            model.addAttribute("editar", false);

            retorno.setViewName("usuario");
        } catch (Exception e) {

            System.out.println("Error" + e);
        }

        return retorno;

    }
    
    @RequestMapping(value = "/reset/{id}", method = RequestMethod.GET)
    public @ResponseBody
    MensajeDTO resetearPass(@PathVariable("id") Long id) {
        MensajeDTO retorno = new MensajeDTO();
        String nombre = "";
        List<String> to = new ArrayList<String>();
        List<String> toUser = new ArrayList<String>();        
        try {

            inicializarUsuarioManager();

            Usuario ejUsuario = new Usuario();            
            ejUsuario.setSuperUsuario(Boolean.TRUE);
            
            List<Map<String, Object>> listMapUsuarios = usuarioManager.listAtributos(ejUsuario, "email,superUsuario".split(","), true);
            
            
            
            for(Map<String, Object> rmp: listMapUsuarios){
                to.add(rmp.get("email").toString()) ;
            }
            
            Usuario usuario = usuarioManager.get(id);
            toUser.add(usuario.getEmail());
            
            String nuevaContraseña = randomString(10);
            String antiguaContraseña = usuario.getClaveAcceso();
            
            String mensajeUsuario = mensajeCambioPass(usuario.getNombre() +" " +usuario.getApellido(),nuevaContraseña);
            
            String mensajeAdmin = mensajeCambioPassUsuario(usuario.getNombre() +" " +usuario.getApellido());
            
            SendMail.enviarMensaje("sistemaproyecto2016@gmail.com", "proyecto2016*auto",
                    toUser, new ArrayList<String>(), new ArrayList<String>(), mensajeUsuario, "Reseteo de contraseña");
            
            SendMail.enviarMensaje("sistemaproyecto2016@gmail.com", "proyecto2016*auto",
                    to, new ArrayList<String>(), new ArrayList<String>(), mensajeAdmin, "Reseteo de contraseña");

            usuario.setClaveAcceso(nuevaContraseña);
            usuario.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            usuario.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));

            usuarioManager.update(usuario);

            retorno.setError(false);
            retorno.setMensaje("Contraseña reseteada exitosamente.");

        } catch (Exception e) {
            retorno.setError(true);
            retorno.setMensaje("Error al resetear contraseña.");
            logger.error("Error al resetear contraseña.", e);
        }

        return retorno;

    }
    
    @RequestMapping(value = "/cambiar/pass/{id}", method = RequestMethod.GET)
    public ModelAndView cambiarPass(Model model,@PathVariable("id") Long id) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("cambiarContrasenha");
        
        try {
            inicializarUsuarioManager();
            
            Map<String, Object> usuario = usuarioManager.getAtributos(
                    new Usuario(id), "id,nombre,apellido,claveAcceso,alias,documento".split(","), false, true);
            
            model.addAttribute("usuario", usuario);

        } catch (Exception ex) {
            logger.error("Error al optener vista reset pass.", ex);
        }
        return retorno;

    }
    

}

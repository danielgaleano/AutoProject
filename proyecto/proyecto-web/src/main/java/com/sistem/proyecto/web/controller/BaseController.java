/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.web.controller;

import com.sistem.proyecto.entity.DocumentoPagar;
import com.sistem.proyecto.manager.ClienteManager;
import com.sistem.proyecto.manager.CompraManager;
import com.sistem.proyecto.manager.ContactoManager;
import com.sistem.proyecto.manager.DetalleCompraManager;
import com.sistem.proyecto.manager.DetallePedidoManager;
import com.sistem.proyecto.manager.DetalleVentaManager;
import com.sistem.proyecto.manager.DocumentoCobrarManager;
import com.sistem.proyecto.manager.DocumentoPagarManager;
import com.sistem.proyecto.manager.EmpleoManager;
import com.sistem.proyecto.manager.ProveedorManager;
import com.sistem.proyecto.manager.EmpresaManager;
import com.sistem.proyecto.manager.ImagenManager;
import com.sistem.proyecto.manager.PermisoManager;
import com.sistem.proyecto.manager.RolManager;
import com.sistem.proyecto.manager.RolPermisoManager;
import com.sistem.proyecto.manager.TipoManager;
import com.sistem.proyecto.manager.MarcaManager;
import com.sistem.proyecto.manager.ModeloManager;
import com.sistem.proyecto.manager.VehiculoManager;
import com.sistem.proyecto.manager.MonedaManager;
import com.sistem.proyecto.manager.MovimientoManager;
import com.sistem.proyecto.manager.NumeracionFacturaManager;
import com.sistem.proyecto.manager.PedidoManager;
import com.sistem.proyecto.manager.UsuarioManager;
import com.sistem.proyecto.manager.VentaManager;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Miguel
 */
@Controller
public class BaseController {

    private Context context;
    
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();
    
    SimpleDateFormat fecha_español = new SimpleDateFormat("EEE d 'de' MMMM 'de' yyyy", new Locale("es","ES"));
    SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd");

    protected UsuarioManager usuarioManager;

    protected EmpresaManager empresaManager;

    protected RolManager rolManager;

    protected PermisoManager permisoManager;

    protected RolPermisoManager rolPermisoManager;

    protected ImagenManager imagenManager;

    protected ClienteManager clienteManager;
    
    protected ProveedorManager proveedorManager;
    
    protected TipoManager tipoManager;
    
    protected MarcaManager marcaManager;
    
    protected ModeloManager modeloManager;
    
    protected VehiculoManager vehiculoManager;
    
    protected ContactoManager contactoManager;
    
    protected PedidoManager pedidoManager;
    
    protected DetallePedidoManager detallePedidoManager;
    
    protected MonedaManager monedaManager;
    
    protected NumeracionFacturaManager numeracionFacturaManager;
    
    protected EmpleoManager empleoManager;
    
    protected CompraManager compraManager;
    
    protected DetalleCompraManager detalleCompraManager;
    
    protected DocumentoPagarManager documentoPagarManager;
    


    protected VentaManager ventaManager;
    
    protected DetalleVentaManager detalleVentaManager;
    
    protected DocumentoCobrarManager documentoCobrarManager;

    protected MovimientoManager movimientoManager;



    public static final Logger logger = LoggerFactory
            .getLogger("proyecto");
    
    protected void inicializarMovimientoManager() throws Exception {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (NamingException e1) {
                throw new RuntimeException("No se puede inicializar el contexto", e1);
            }
        }
        if (movimientoManager == null) {
            try {

                movimientoManager = (MovimientoManager) context.lookup("java:app/proyecto-ejb/MovimientoManagerImpl");
            } catch (NamingException ne) {
                throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
            }
        }
    }
    
    protected void inicializarImagenManager() throws Exception {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (NamingException e1) {
                throw new RuntimeException("No se puede inicializar el contexto", e1);
            }
        }
        if (imagenManager == null) {
            try {

                imagenManager = (ImagenManager) context.lookup("java:app/proyecto-ejb/ImagenManagerImpl");
            } catch (NamingException ne) {
                throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
            }
        }
    }
    protected void inicializarCompraManager() throws Exception {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (NamingException e1) {
                throw new RuntimeException("No se puede inicializar el contexto", e1);
            }
        }
        if (compraManager == null) {
            try {

                compraManager = (CompraManager) context.lookup("java:app/proyecto-ejb/CompraManagerImpl");
            } catch (NamingException ne) {
                throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
            }
        }
    }
    
    protected void inicializarDocumentoPagarManager() throws Exception {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (NamingException e1) {
                throw new RuntimeException("No se puede inicializar el contexto", e1);
            }
        }
        if (documentoPagarManager == null) {
            try {

                documentoPagarManager = (DocumentoPagarManager) context.lookup("java:app/proyecto-ejb/DocumentoPagarManagerImpl");
            } catch (NamingException ne) {
                throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
            }
        }
    }
    
    protected void inicializarDetalleCompraManager() throws Exception {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (NamingException e1) {
                throw new RuntimeException("No se puede inicializar el contexto", e1);
            }
        }
        if (detalleCompraManager == null) {
            try {

                detalleCompraManager = (DetalleCompraManager) context.lookup("java:app/proyecto-ejb/DetalleCompraManagerImpl");
            } catch (NamingException ne) {
                throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
            }
        }
    }
    
    protected void inicializarVentaManager() throws Exception {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (NamingException e1) {
                throw new RuntimeException("No se puede inicializar el contexto", e1);
            }
        }
        if (ventaManager == null) {
            try {

                ventaManager = (VentaManager) context.lookup("java:app/proyecto-ejb/VentaManagerImpl");
            } catch (NamingException ne) {
                throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
            }
        }
    }
    
    protected void inicializarDocumentoCobrarManager() throws Exception {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (NamingException e1) {
                throw new RuntimeException("No se puede inicializar el contexto", e1);
            }
        }
        if (documentoCobrarManager == null) {
            try {

                documentoCobrarManager = (DocumentoCobrarManager) context.lookup("java:app/proyecto-ejb/DocumentoCobrarManagerImpl");
            } catch (NamingException ne) {
                throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
            }
        }
    }
    
    protected void inicializarDetalleVentaManager() throws Exception {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (NamingException e1) {
                throw new RuntimeException("No se puede inicializar el contexto", e1);
            }
        }
        if (detalleVentaManager == null) {
            try {

                detalleVentaManager = (DetalleVentaManager) context.lookup("java:app/proyecto-ejb/DetalleVentaManagerImpl");
            } catch (NamingException ne) {
                throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
            }
        }
    }
    
    protected void inicializarEmpleoManager() throws Exception {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (NamingException e1) {
                throw new RuntimeException("No se puede inicializar el contexto", e1);
            }
        }
        if (empleoManager == null) {
            try {

                empleoManager = (EmpleoManager) context.lookup("java:app/proyecto-ejb/EmpleoManagerImpl");
            } catch (NamingException ne) {
                throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
            }
        }
    }

    protected void inicializarRolPermisoManager() throws Exception {
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

    protected void inicializarPermisoManager() throws Exception {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (NamingException e1) {
                throw new RuntimeException("No se puede inicializar el contexto", e1);
            }
        }
        if (permisoManager == null) {
            try {

                permisoManager = (PermisoManager) context.lookup("java:app/proyecto-ejb/PermisoManagerImpl");
            } catch (NamingException ne) {
                throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
            }
        }
    }

    protected void inicializarUsuarioManager() throws Exception {
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

    protected void inicializarEmpresaManager() throws Exception {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (NamingException e1) {
                throw new RuntimeException("No se puede inicializar el contexto", e1);
            }
        }
        if (empresaManager == null) {
            try {

                empresaManager = (EmpresaManager) context.lookup("java:app/proyecto-ejb/EmpresaManagerImpl");
            } catch (NamingException ne) {
                throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
            }
        }
    }

    protected void inicializarRolManager() throws Exception {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (NamingException e1) {
                throw new RuntimeException("No se puede inicializar el contexto", e1);
            }
        }
        if (rolManager == null) {
            try {

                rolManager = (RolManager) context.lookup("java:app/proyecto-ejb/RolManagerImpl");
            } catch (NamingException ne) {
                throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
            }
        }
    }

    protected void inicializarClienteManager() throws Exception {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (NamingException e1) {
                throw new RuntimeException("No se puede inicializar el contexto", e1);
            }
        }
        if (clienteManager == null) {
            try {

                clienteManager = (ClienteManager) context.lookup("java:app/proyecto-ejb/ClienteManagerImpl");
            } catch (NamingException ne) {
                throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
            }
        }
    }
    
    protected void inicializarProveedorManager() throws Exception {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (NamingException e1) {
                throw new RuntimeException("No se puede inicializar el contexto", e1);
            }
        }
        if (proveedorManager == null) {
            try {

                proveedorManager = (ProveedorManager) context.lookup("java:app/proyecto-ejb/ProveedorManagerImpl");
            } catch (NamingException ne) {
                throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
            }
        }
    }
    
    protected void inicializarTipoManager() throws Exception {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (NamingException e1) {
                throw new RuntimeException("No se puede inicializar el contexto", e1);
            }
        }
        if (tipoManager == null) {
            try {

                tipoManager = (TipoManager) context.lookup("java:app/proyecto-ejb/TipoManagerImpl");
            } catch (NamingException ne) {
                throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
            }
        }
    }
    
    protected void inicializarMarcaManager() throws Exception {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (NamingException e1) {
                throw new RuntimeException("No se puede inicializar el contexto", e1);
            }
        }
        if (marcaManager == null) {
            try {

                marcaManager = (MarcaManager) context.lookup("java:app/proyecto-ejb/MarcaManagerImpl");
            } catch (NamingException ne) {
                throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
            }
        }
    }
    
    protected void inicializarModeloManager() throws Exception {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (NamingException e1) {
                throw new RuntimeException("No se puede inicializar el contexto", e1);
            }
        }
        if (modeloManager == null) {
            try {

                modeloManager = (ModeloManager) context.lookup("java:app/proyecto-ejb/ModeloManagerImpl");
            } catch (NamingException ne) {
                throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
            }
        }
    }
    
    protected void inicializarVehiculoManager() throws Exception {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (NamingException e1) {
                throw new RuntimeException("No se puede inicializar el contexto", e1);
            }
        }
        if (vehiculoManager == null) {
            try {

                vehiculoManager = (VehiculoManager) context.lookup("java:app/proyecto-ejb/VehiculoManagerImpl");
            } catch (NamingException ne) {
                throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
            }
        }
    }
    
    protected void inicializarContactoManager() throws Exception {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (NamingException e1) {
                throw new RuntimeException("No se puede inicializar el contexto", e1);
            }
        }
        if (contactoManager == null) {
            try {

                contactoManager = (ContactoManager) context.lookup("java:app/proyecto-ejb/ContactoManagerImpl");
            } catch (NamingException ne) {
                throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
            }
        }
    }
    
    protected void inicializarPedidoManager() throws Exception {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (NamingException e1) {
                throw new RuntimeException("No se puede inicializar el contexto", e1);
            }
        }
        if (pedidoManager == null) {
            try {

                pedidoManager = (PedidoManager) context.lookup("java:app/proyecto-ejb/PedidoManagerImpl");
            } catch (NamingException ne) {
                throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
            }
        }
    }
    
    protected void inicializarDetallePedidoManager() throws Exception {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (NamingException e1) {
                throw new RuntimeException("No se puede inicializar el contexto", e1);
            }
        }
        if (detallePedidoManager == null) {
            try {

                detallePedidoManager = (DetallePedidoManager) context.lookup("java:app/proyecto-ejb/DetallePedidoManagerImpl");
            } catch (NamingException ne) {
                throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
            }
        }
    }
    
    protected void inicializarMonedaManager() throws Exception {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (NamingException e1) {
                throw new RuntimeException("No se puede inicializar el contexto", e1);
            }
        }
        if (monedaManager == null) {
            try {

                monedaManager = (MonedaManager) context.lookup("java:app/proyecto-ejb/MonedaManagerImpl");
            } catch (NamingException ne) {
                throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
            }
        }
    }

    protected void inicializarNumeracionFacturaManager() throws Exception {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (NamingException e1) {
                throw new RuntimeException("No se puede inicializar el contexto", e1);
            }
        }
        if (numeracionFacturaManager == null) {
            try {

                numeracionFacturaManager = (NumeracionFacturaManager) context.lookup("java:app/proyecto-ejb/NumeracionFacturaManagerImpl");
            } catch (NamingException ne) {
                throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
            }
        }
    }
    
    

    String randomString(int len, String variable) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            String rand = AB + variable;
            sb.append(rand.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }
    
    String mensajeCambioPass(String nombreUsuario, String pass) {
        String mensaje = "Buenas " + nombreUsuario+",\n"
                +"        Se ha realizado el reseteo de su contraseña, su nueva contraseña \n"
                +" es la siguiente  "+ pass +"  , favor ingresar al sistema y modificarla.";
        return mensaje;
    }
    
    String mensajeCambioPassUsuario(String nombreUsuario) {
        String mensaje = "Buenas,\n"
                +"        Se ha realizado el reseteo de la contraseña del usuario "+nombreUsuario+" \n";
        return mensaje;
    }
}

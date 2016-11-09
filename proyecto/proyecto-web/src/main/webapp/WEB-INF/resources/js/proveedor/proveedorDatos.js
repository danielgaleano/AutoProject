
$(document).ready(function(data) {
    
     
    $("#editButton").click(function () {
        $('#buttonOption').show();
        $('#buttonOptionContacto').show();
        
        $('#validation-form').find('.tableusuario-input').attr("disabled", false);
        $('#validation-formContacto').find('.tableusuario-input').attr("disabled", false);
    });

    
    if (action === 'VISUALIZAR') {
        $('#buttonOption').hide();
        $('#buttonOptionContacto').hide();
        $('#buttonOptionEmpleo').hide();
        
        $('#validation-form').find('.tableusuario-input').attr("disabled", true);
        $('#validation-formContacto').find('.tableusuario-input').attr("disabled", true);

    }else if (action === 'EDITAR'){
        $('#documento').attr("disabled", true);
    }
    
    if (id !== null && id !== "") {
        var jqXHR = $.get(CONTEXT_ROOT + "/proveedores/" + id, function(response, textStatus, jqXHR) {

            if (response.error) {
                $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                        + '<button class="close" data-dismiss="alert" type="button"'
                        + '><i class="fa  fa-remove"></i></button>'
                        + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                        + response.mensaje
                        + '</div>');
            } else {
                var proveedor = response.data;
                
                $('#idProveedor').val(proveedor.id);
                $('#ruc').val(proveedor.ruc);
                $('#nombre').val(proveedor.nombre);
                $('#email').val(proveedor.email);
                $('#telefono').val(proveedor.telefono);
                $('#direccion').val(proveedor.direccion);
                $('#comentario').val(proveedor.comentario);
                $('#fax').val(proveedor.fax);
                $('#pais').val(proveedor.pais);
                $('#ciudad').val(proveedor.ciudad);
                $('#codigoPostal').val(proveedor.codigoPostal);              
                $('#comentario').val(proveedor.comentario);
                $('#idContacto').val(proveedor['contacto.id']);
                $('#movilContacto').val(proveedor['contacto.movil']);
                $('#documentoContacto').val(proveedor['contacto.documento']);
                $('#nombreContacto').val(proveedor['contacto.nombre']);
                $('#cargoContacto').val(proveedor['contacto.cargo']);
                $('#telefonoContacto').val(proveedor['contacto.telefono']);
                $('#contactoEmail').val(proveedor['contacto.email']);
                $('#comentarioContacto').val(proveedor['contacto.comentario']);

                $('#mensaje').append('<div class="alert alert-success alert-dismissible fade in">'
                        + '<button type="button" class="close" data-dismiss="alert"'
                        + 'aria-label="Close"><i class="fa  fa-remove"></i></button>'
                        + '<h4><strong><i class="icon fa fa-check"></i> Exito! </strong></h4>'
                        + response.mensaje
                        + '</div>');

            }

        });

        jqXHR.fail(function(jqXHR, textStatus, errorThrown) {
            $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                        + '<button class="close" data-dismiss="alert" type="button"'
                        + '><i class="fa  fa-remove"></i></button>'
                        + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                        + 'Error! Favor comunicarse con el Administrador'
                        + '</div>');
        });
    }
});

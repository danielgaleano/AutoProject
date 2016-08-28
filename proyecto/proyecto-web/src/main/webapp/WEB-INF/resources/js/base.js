var baseConstantes = {
    botonCerrar: '<button data-dismiss="alert" class="close" type="button"><i class="fa fa-times"></i></button>'
};
function estado(cellvalue, options, rowObject) {
    if(cellvalue === 'S'){
        return '<span class="table-estado label label-success" value="S">Activo</span>';
    }else{
        return '<span class="table-estado label label-danger"  value="N" >Inactivo</span>';
    }
}
function visualizarButton(id, permisoVisualizar) {
    var content = window.location.href;
    var button = '';
    if (permisoVisualizar) {
        button = '<a onmouseout="jQuery(this).removeClass(' + "'ui-state-hover'" + ')"'
                + ' onmouseover="jQuery(this).addClass(' + "'i-state-hover'" + ');" href="' + content + '/visualizar/' + id + '"'
                + '  class=" btn btn-xs btn-info" style="float:left;cursor:pointer;" title="Visualizar">'
                + ' <span class="ace-icon fa fa-external-link"></span></a>';
    }

    return button;
}
function asigButton(id, permisoAsignar) {
    var content = window.location.href;
    var button = '';
    if (permisoAsignar) {
        button = '<a onmouseout="jQuery(this).removeClass(' + "'ui-state-hover'" + ')"'
                + ' onmouseover="jQuery(this).addClass(' + "'i-state-hover'" + ');" href="' + content + '/asignar/' + id + '"'
                + '  class=" btn btn-xs btn-info" style="float:left;cursor:pointer;" title="Asignar">'
                + ' <span class="fa fa-group"></span></a>';
    }

    return button;
}

function editFormButton(id, permisoEditar) {
    var content = window.location.href;
    var button = '';
    if (permisoEditar) {
        button = '<a onmouseout="jQuery(this).removeClass(' + "'ui-state-hover'" + ')"'
                + ' onmouseover="jQuery(this).addClass(' + "'i-state-hover'" + ');" href="' + content + '/editar/' + id + '"'
                + '  class=" btn btn-xs btn-info" style="float:left;cursor:pointer;" title="Asignar">'
                + ' <span class="glyphicon glyphicon-pencil"></span></a>';
    }

    return button;
}

function desactivarButton(id, permisoDesactivar) {
    var content = window.location.href;
    var button = '';
    if (permisoDesactivar) {
        button = '<a onmouseout="jQuery(this).removeClass(' + "'ui-state-hover'" + ')"'
                + ' onmouseover="jQuery(this).addClass(' + "'i-state-hover'" + ');"'
                + '  class=" btn btn-xs btn-danger" style="float:left;cursor:pointer;" type="button" title="Desactivar" onclick="desactivar(this,' + id + ');">'
                + ' <span class="glyphicon glyphicon-trash"></span></a>';
    }

    return button;
}
function activarButton(id, permisoActivar) {
    var button = '';
    if (permisoActivar) {
        button = '<a onmouseout="jQuery(this).removeClass(' + "'ui-state-hover'" + ')"'
                + ' onmouseover="jQuery(this).addClass(' + "'i-state-hover'" + ');"'
                + '  class="btn btn-xs btn-success" style="float:left;cursor:pointer;" type="button" title="Activar" onclick="activar(this,' + id + ');">'
                + ' <span class="ace-icon fa fa-check bigger-120"></span></a>';
    }

    return button;
}

function editInlineButton(cl, permisoEditar) {
    var be = '';
    var se = '';
    var ce = '';
    if (permisoEditar) {
        be = '<div onmouseout="jQuery(this).removeClass(' + "'ui-state-hover'" + ')"'
                + ' onmouseover="jQuery(this).addClass(' + "'i-state-hover'" + ');" onclick="jQuery.fn.fmatter.rowactions.call(this,' + "'edit'" + ');"'
                + ' id="jEditButton_' + cl + '" class=" ui-inline-edit btn btn-xs btn-info" style="float:left;cursor:pointer;" title="Editar">'
                + ' <span class="glyphicon glyphicon-pencil"></span></div>';
        se = '<div onmouseout="jQuery(this).removeClass(' + "'ui-state-hover'" + ');"'
                + ' onmouseover="jQuery(this).addClass(' + "'ui-state-hover'" + ');" onclick="jQuery.fn.fmatter.rowactions.call(this,' + "'save'" + ');"'
                + ' id="jSaveButton_' + cl + '" class="ui-pg-div ui-inline-save" style="float: left; display: none;" title="Guardar">'
                + ' <span class="ui-icon ui-icon-disk"></span></div>';
        ce = '<div onmouseout="jQuery(this).removeClass(' + "'ui-state-hover'" + ');" onmouseover="jQuery(this).addClass(' + "'ui-state-hover'" + ');"'
                + ' onclick="jQuery.fn.fmatter.rowactions.call(this,' + "'cancel'" + ');" id="jCancelButton_' + cl + '" class="ui-pg-div ui-inline-cancel"'
                + ' style="float: left; margin-left: 5px; display: none;" title="Cancelar">'
                + '<span class="ui-icon ui-icon-cancel"></span></div>';
    }

    return be + se + ce;
}

function desactivar(content, id) {
    var content = window.location.href;   
    var jqXHR = $.get(content+"/desactivar/" + id, function(data, textStatus, jqXHR) {
        if (data.error === true) {
            $('#mensaje').append('<div class="alert alert-error">'
                    + '<button class="close" data-dismiss="alert" type="button"'
                    + '><i class="fa  fa-remove"></i></button>'
                    + '<strong>Error! </strong>'
                    + data.mensaje
                    + '</div>');

        } else {
            $('#mensaje').append('<div class="alert alert-info alert-dismissible fade in">'
                    + '<button type="button" class="close" data-dismiss="alert"'
                    + 'aria-label="Close"><i class="fa  fa-remove"></i></button>'
                    + '<strong>Exito! </strong>'
                    + data.mensaje
                    + '</div>');
            $('#grid').trigger('reloadGrid');
        }
    });
}

function activar(content, id) {
    var content = window.location.href;   
    var jqXHR = $.get(content+"/activar/" + id, function(data, textStatus, jqXHR) {
        if (data.error === true) {
            $('#mensaje').append('<div class="alert alert-error">'
                    + '<button class="close" data-dismiss="alert" type="button"'
                    + '><i class="fa  fa-remove"></i></button>'
                    + '<strong>Error! </strong>'
                    + data.mensaje
                    + '</div>');

        } else {
            $('#mensaje').append('<div class="alert alert-info alert-dismissible fade in">'
                    + '<button type="button" class="close" data-dismiss="alert"'
                    + 'aria-label="Close"><i class="fa  fa-remove"></i></button>'
                    + '<strong>Exito! </strong>'
                    + data.mensaje
                    + '</div>');
            $('#grid').trigger('reloadGrid');
        }
    });
}

function sendData(url) {

    var serialize = $("#formUsuario").serialize();

    var jqXHR = $.post(CONTEXT_ROOT + url, serialize, function(data, textStatus, jqXHR) {
        console.log(data);
        if (data.error == true) {
            $('#mensaje').before('<div class="alert alert-danger alert-dismissible fade in">'
                    + '<button type="button" class="close" data-dismiss="alert"'
                    + 'aria-label="Close"><span aria-hidden="true">&times;</span></button>'
                    + '<strong>Error! </strong>'
                    + data.mensaje
                    + '</div>');
            $('html, body').animate({scrollTop: 0}, 0);

        } else {
            $('#mensaje').before('<div class="alert alert-info alert-dismissible fade in">'
                    + '<button type="button" class="close" data-dismiss="alert"'
                    + 'aria-label="Close"><span aria-hidden="true">&times;</span></button>'
                    + '<strong>Exito! </strong>'
                    + data.mensaje
                    + '</div>');
            $('html, body').animate({scrollTop: 0}, 0);
        }
    }, 'json');

    jqXHR.fail(function(jqXHR, textStatus, errorThrown) {

        $('#mensaje').before('<div class="alert alert-danger alert-dismissible fade in">'
                + '<button type="button" class="close" data-dismiss="alert"'
                + 'aria-label="Close"><span aria-hidden="true">&times;</span></button>'
                + '<strong>Error! </strong>'
                + 'Error al crear el registro. Vuelva a intentarlo.'
                + '</div>');
        $('html, body').animate({scrollTop: 0}, 0);
    });

    jqXHR.always(function() {

    });

    return jqXHR;

}

// Botón para subir imágenes
$('.btn-subir-imagen').each(function(b, btn) {

    if (!$(btn).data('activar')) {
        $(btn).removeClass('btn-subir-imagen');
        return;
    }

    var div = $('<div>').addClass('img-boton');
    $(btn).after(div);
    $(btn).appendTo(div);

    $(btn).click(function() {
        var form = $(btn).data('form');
        var campo = $(btn).data('campo');
        var id = 'imagen-' + form + '-' + campo;

        if (!$('#' + id).length) {
            $('<input>').attr('id', id)
                    .attr('type', 'file')
                    .addClass('hidden')
                    .appendTo('body')
                    .change(function() {
                        var input = this;
                        if (input.files && input.files[0]) {
                            var FR = new FileReader();
                            FR.onload = function(e) {
                                $(btn).attr("src", e.target.result);
                                $('#' + form).find('input[name="' + campo + '"]')
                                        .val(e.target.result);
                            };
                            FR.readAsDataURL(input.files[0]);

                            $(btn).parent().mensaje({
                                tipoMensaje: 'alert-warning',
                                mensaje: 'Para confirmar el cambio de imagen haz click en <strong>Guardar</strong>.',
                                after: true
                            });
                        }
                    });
        }

        $('#' + id).click();
    });

});

// Botón de cancelar
$('.boton-cancelar').click(function() {
    window.history.back();
});

//Aplicar el spin de 'cargando' solo para los botones que van a ir a otra vista
//$('#page-content-wrapper i.icon').click(function(e){
//	$(this).addClass('fa fa-spinner fa fa-spin');
//});

// Funcion para atrapar el evento de la tecla ENTER
//onEnter Plugin
$.fn.onEnter = function(callback) {
    this.keyup(function(e) {
        if (e.keyCode == baseKeys.ENTER) {
            e.preventDefault();
            if (typeof callback == 'function') {
                callback.apply(this);
            }
        }
    });
    return this;
};

// Función para mensajes
$.fn.mensaje = function(data) {
    if (data.tipoMensaje == undefined || data.mensaje == undefined ||
            data.tipoMensaje == '' || data.mensaje == '') {
        return;
    }

    var div = data.after ? this.next() : this.prev();
    var contenido = '<div class="alert alert-dismissable ' + data.tipoMensaje + '">'
            + baseConstantes.botonCerrar + data.mensaje + '</div>';

    if (div.hasClass('alert')) {
        div.remove();
    }

    if (data.after) {
        this.after(contenido);
    } else {
        this.before(contenido);
    }

    return this;
};
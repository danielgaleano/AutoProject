
$(document).ready(function(data) { 
  var permisoActivar = parseBolean($(this).find('.tablactivate-permiso').text());
  var permisoDesactivar = parseBolean($(this).find('.tabldelete-permiso').text());
  var permisoEditar = parseBolean($(this).find('.tabledit-permiso').text());
  var permisoAsignar = parseBolean($(this).find('.tablasignar-permiso').text());

  $('#example1').Tabledit({
        url: CONTEXT_ROOT+'/marcas/editar',
        urlDelete: CONTEXT_ROOT+'/marcas/desactivar/',
        urlActivate: CONTEXT_ROOT+'/marcas/activar/',
        tableId:'example1',
        isStatus:true,
        editarFormButton: false,
        editButton: permisoEditar,
        deleteButton: permisoDesactivar,
        activarButton:permisoActivar,
        asignarButton:false,
        visualizarButton:false,
        columns: {
            identifier: [0, 'id'],
            editable: [
                [1, 'nombre']
            ]
        },
         onDraw: function() {
            console.log('onDraw()');
        },
        onSuccess: function(data, textStatus, jqXHR) {

        },
        onFail: function(jqXHR, textStatus, errorThrown) {

        },
        onAlways: function() {

        },
        onAjax: function(action, serialize) {

        }

    }); 
    $('#example1').DataTable( {
        "order": [[ 0, "asc" ]],
        "pageLength": 10,
        "autoWidth": false,

        "language": {
            "lengthMenu": "Mostrar _MENU_ resultados por pagina",
            "zeroRecords": "No se encontraron resultados",
            "info": "Pagina _PAGE_ de _PAGES_",
            "infoEmpty": "No hay resultados disponibles",
            "infoFiltered": "(filtrado de un total de _MAX_ )",
            "search": "Buscar:"
        }
    } );  
    
    var $table = $('#example1 > tbody');
    
    $('#crear').on('click',function (){
        console.log($('#example1 > tbodyt'));
        $('#crear').attr('disabled','disabled');
        var input = '<input class="tabledit-input form-control input-sm" type="text" name="nombre" value="" style=""/>';

        $('#example1 > tbody').prepend($('<tr class="odd" role="row">\n')
                .append('<input class="tabledit-input tabledit-identifier" type="hidden" value="" name="id">\n')
                .append('<th class="sorting_1 tabledit-edit-mode">'+ input+'</th>\n')
                .append('<th class="tabledit-estado"></th>\n')
                .append('<th class="tabledit-buttons" style="white-space: nowrap; width: 1%;">'
                +'<div class="btn-group btn-group-sm" style="float: none;">\n'
                +'<button id="guardar" style="float: none;" class="tabledit-guardar-registro-button btn btn-sm btn-success">Guardar</button>\n'
                +'<button id="cancelar" title="Cancelar" style="float: none;" class="remover_celda btn btn-xs btn-danger">'
                +'<span class="glyphicon glyphicon-remove"></span></button>'
                +'</div>'
                +'</th>\n')
                .append('</tr>'));
        
        
    });
        
   $table.on('click','button.tabledit-guardar-registro-button',function (){
       $('#crear').removeAttr('disabled');
        var serialize = $table.find('.tabledit-input').serialize();
        
        var jqXHR = $.post(CONTEXT_ROOT+'/marcas/guardar', serialize, function(data, textStatus, jqXHR) {
            if(data.error){
                $('#mensaje').append('<div class="alert alert-error">'
                                    + '<button class="close" data-dismiss="alert" type="button"'
                                    +'><i class="fa  fa-remove"></i></button>'
                                    +'<strong>Error! </strong>'
                                    + data.mensaje
                                    + '</div>');
            }else{
                $('#mensaje').append('<div class="alert alert-info alert-dismissible fade in">'
                                    + '<button type="button" class="close" data-dismiss="alert"'
                                    +'aria-label="Close"><i class="fa  fa-remove"></i></button>'
                                    +'<strong>Exito! </strong>'
                                    + data.mensaje
                                    + '</div>');
                setTimeout(function() {
                    //$lastEditedRow.removeClass(settings.warningClass);
                    location.reload();
                }, 1500);
            }
            
        });
        
        jqXHR.fail(function(jqXHR, textStatus, errorThrown) {

        });
            
            
   }); 
   
   $table.on('click','button.remover_celda',function (){
       $('#crear').removeAttr('disabled');
       $(this).closest('tr').remove();
   });
    
    
});

function guardarRegistro(){
    if(val.toLowerCase() === 'true'){
        return true;
    }else if (val.toLowerCase() === 'false' || val.toLowerCase() === ''){
        return false;
    }

}

function parseBolean(val){
    if(val.toLowerCase() === 'true'){
        return true;
    }else if (val.toLowerCase() === 'false' || val.toLowerCase() === ''){
        return false;
    }

}
            
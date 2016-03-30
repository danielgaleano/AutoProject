
$(document).ready(function(data) { 
  var permisoActivar = parseBolean($(this).find('.tablactivate-permiso').text());
  var permisoDesactivar = parseBolean($(this).find('.tabldelete-permiso').text());
  var permisoEditar = parseBolean($(this).find('.tabledit-permiso').text());

  $('#example1').Tabledit({
        url: CONTEXT_ROOT+'/roles/guardar',
        urlDelete: CONTEXT_ROOT+'/roles/desactivar/',
        urlActivate: CONTEXT_ROOT+'/roles/activar/',
        urlAsignar: CONTEXT_ROOT+'/roles/asignar',
        tableId:'example1',
        isStatus:true,
        editButton: permisoEditar,
        deleteButton: permisoDesactivar,
        activarButton:permisoActivar,
        asignarButton:true,
        visualizarButton:false,
        columns: {
            identifier: [0, 'id'],
            editable: [
                [1, 'nombre'],
                [2, 'empresa',datosEmpresa]
            ]
        },
         onDraw: function() {
            console.log('onDraw()');
        },
        onSuccess: function(data, textStatus, jqXHR) {
            window.location = window.location +"#success";

            console.log('onSuccess(data, textStatus, jqXHR)');
            console.log(data);
            console.log(textStatus);
            console.log(jqXHR);
        },
        onFail: function(jqXHR, textStatus, errorThrown) {
            console.log('onFail(jqXHR, textStatus, errorThrown)');
            console.log(jqXHR);
            console.log(textStatus);
            console.log(errorThrown);
        },
        onAlways: function() {
            console.log('onAlways()');
        },
        onAjax: function(action, serialize) {
            console.log('onAjax(action, serialize)');
            console.log(action);
            console.log(serialize);
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
    
    var $table = $('#example1');
    
    $('#crear').on('click',function (){
        $('#crear').attr('disabled','disabled');
        var input = '<input class="tabledit-input form-control input-sm" type="text" name="nombre" value="" style="">';
        
        var select = '<select class="tabledit-input form-control input-sm" name="empresa" style="">';
        select += '<option value="" selected>Seleccione Empresa</option>';    
        
        $.each(datosEmpresa, function(index, value) {
                    select += '<option value="' + value.id + '">' + value.nombre + '</option>';
            });
        input += '</select>';

        $table.append($('<tr class="odd" role="row">\n')
                .append('<input class="tabledit-input tabledit-identifier" type="hidden" value="" name="id">\n')
                .append('<th class="sorting_1 tabledit-edit-mode">'+ input+'</th>\n')
                .append('<th class="tabledit-edit-mode">'+ select+'</th>\n')
                .append('<th class="tabledit-estado"></th>\n')
                .append('<th class="tabledit-buttons" style="white-space: nowrap; width: 1%;">'
                +'<div class="btn-group btn-group-sm" style="float: none;">\n'
                +'<button id="guardar" style="float: none;" class="tabledit-guardar-registro-button btn btn-sm btn-success">Guardar</button>\n'
                +'<button id="cancelar" title="Cancelar" style="float: none;" class="btn btn-xs btn-danger">'
                +'<span class="glyphicon glyphicon-remove"></span></button>'
                +'</div>'
                +'</th>\n')
                .append('</tr>'));
        
        
    });
        
   $table.on('click','button.tabledit-guardar-registro-button',function (){
       $('#crear').removeAttr('disabled');
        var serialize = $table.find('.tabledit-input').serialize();
        
        var jqXHR = $.post(CONTEXT_ROOT+'/roles/guardar', serialize, function(data, textStatus, jqXHR) {
            location.reload();
        });
        
        jqXHR.fail(function(jqXHR, textStatus, errorThrown) {

        });
            
            
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
            
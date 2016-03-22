
$(document).ready(function(data) { 
  var permisoActivar = parseBolean($(this).find('.tablactivate-permiso').text());
  var permisoDesactivar = parseBolean($(this).find('.tabldelete-permiso').text());
  var permisoEditar = parseBolean($(this).find('.tabledit-permiso').text());

  $('#example1').Tabledit({
        url: CONTEXT_ROOT+'/roles/guardar',
        urlDelete: CONTEXT_ROOT+'/roles/desactivar/',
        urlActivate: CONTEXT_ROOT+'/roles/activar/',
        tableId:'example1',
        isStatus:true,
        editButton: permisoEditar,
        deleteButton: permisoDesactivar,
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
            "lengthMenu": "Mostrar _MENU_ resultados por página",
            "zeroRecords": "No se encontraron resultados",
            "info": "Página _PAGE_ de _PAGES_",
            "infoEmpty": "No hay resultados disponibles",
            "infoFiltered": "(filtrado de un total de _MAX_ )",
            "search": "Buscar:"
        }
    } );  
});



function parseBolean(val){
    if(val.toLowerCase() === 'true'){
        return true;
    }else if (val.toLowerCase() === 'false' || val.toLowerCase() === ''){
        return false;
    }

}
            
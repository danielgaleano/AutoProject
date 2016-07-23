
$(document).ready(function(data) { 
  var permisoActivar = parseBolean($(this).find('.tablactivate-permiso').text());
  var permisoDesactivar = parseBolean($(this).find('.tabldelete-permiso').text());
  var permisoEditar = parseBolean($(this).find('.tabledit-permiso').text());
  var permisoVisualizar = parseBolean($(this).find('.tablvisualizar-permiso').text());
  var permisoAsignar = parseBolean($(this).find('.tablasignar-permiso').text());

  $('#example1').Tabledit({
        url: CONTEXT_ROOT+'',
        urlDelete: CONTEXT_ROOT+'/clientes/desactivar/',
        urlActivate: CONTEXT_ROOT+'/clientes/activar/',
        urlVisualizar: CONTEXT_ROOT+'/clientes/visualizar',
        urlEditar: CONTEXT_ROOT+'/clientes/editar',
        urlAsignar: CONTEXT_ROOT+'',
        tableId:'example1',
        titleAsignar:'Asignar Rol',
        isStatus:true,
        deleteButton: permisoDesactivar,
        activarButton:permisoActivar,
        visualizarButton:permisoVisualizar,
        editarFormButton:permisoEditar,
        editButton:false,
        asignarButton:false,
        columns: {
            identifier: [0, 'id'],
            editable: []
        },
         onDraw: function() {
            console.log('onDraw()');
        },
        onSuccess: function(data, textStatus, jqXHR) {

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
});



function parseBolean(val){
    if(val.toLowerCase() === 'true'){
        return true;
    }else if (val.toLowerCase() === 'false' || val.toLowerCase() === ''){
        return false;
    }

}
            
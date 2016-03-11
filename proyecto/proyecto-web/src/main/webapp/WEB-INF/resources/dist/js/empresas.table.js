$(document).ready(function() {
    
    $('#table_bug_report').DataTable( {
        "order": [[ 0, "asc" ]],
        "pageLength": 10,
        "autoWidth": false,
        
        "language": {
            "lengthMenu": "Mostrar _MENU_ resultados por página",
            "zeroRecords": "No se encontraron resultados",
            "info": "Página _PAGE_ de _PAGES_",
            "infoEmpty": "No hay resultados disponibles",
            "infoFiltered": "(filtrado de un total de _MAX_ )",
            "search": "Buscar:",
            "paginate": {
                "first":      "Primera",
                "last":       "Ultima",
                "next":       ">",
                "previous":   "<"
            },
        }
    } );
} );



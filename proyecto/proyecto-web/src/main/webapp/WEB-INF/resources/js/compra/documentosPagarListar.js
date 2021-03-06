$(document).ready(function(data) {
    
    var id_compra = id;
    

    var isEditarInline = false;
    var isStatus = true;

    var permisoActivar = parseBolean($(this).find('.tablactivate-permiso').text());
    var permisoDesactivar = parseBolean($(this).find('.tabldelete-permiso').text());
    var permisoEditar = parseBolean($(this).find('.tabledit-permiso').text());
    var permisoVisualizar = parseBolean($(this).find('.tablvisualizar-permiso').text());
    var permisoDetalle = parseBolean($(this).find('.tabladd-permiso').text());

    var grid_selector = "#grid";
    var pager_selector = "#grid-pager";


    $(window).on('resize.jqGrid', function() {
        setTimeout(function() {
            $(grid_selector).jqGrid('setGridWidth', $(".content").width());

        }, 0);
    });
    $(grid_selector).jqGrid({
        url: CONTEXT_ROOT + '/compras/docsPagar/listar?_search=false&todos=true&rows=60&page=1&sidx=&sord=asc&idCompra=' + id_compra,
        datatype: 'json',
        mtype: 'GET',
        height: 350,
        hidegrid: false,
        rownumbers: true,
        loadonce:true,
        width: 1050,
        colNames: ['NRO. CUOTA', 'MONTO', 'SALDO', 'FECHA VENCIMIENTO', 'ESTADO'],
        colModel: [
            {name: "nroCuota", index: "nroCuota", width: 120, key: true},
            {name: "monto", index: "monto", formatter: 'number', width: 130},
            {name: "saldo", index: "saldo", formatter: 'number', width: 80, align: "right"},
            {name: "fecha", index: "fecha", width: 120, align: "right"},
            {name: "estado", index: "estado", width: 130, align: "right", sortable: false},
//            {name: 'act', index: 'act', width: 160, fixed: true, sortable: false, resize: false,
//                //               formatter: 'actions',
//                formatoptions: {
//                    onError: function(jqXHR, textStatus, errorThrwn) {
//                        if (textStatus.status !== 200) {
//                            $('#mensaje').append('<div class="alert alert-error">'
//                                    + '<button class="close" data-dismiss="alert" type="button"'
//                                    + '><i class="fa  fa-remove"></i></button>'
//                                    + '<strong>Error ' + textStatus.status + ' ! </strong>'
//                                    + 'Error al editar el registro'
//                                    + '</div>');
//                        }
//
//                    },
//                    onSuccess: function(data) {
//                        if (data.responseJSON.error === true) {
//                            $('#mensaje').append('<div class="alert alert-error">'
//                                    + '<button class="close" data-dismiss="alert" type="button"'
//                                    + '><i class="fa  fa-remove"></i></button>'
//                                    + '<strong>Error! </strong>'
//                                    + data.responseJSON.mensaje
//                                    + '</div>');
//
//                        } else {
//                            $('#mensaje').append('<div class="alert alert-info alert-dismissible fade in">'
//                                    + '<button type="button" class="close" data-dismiss="alert"'
//                                    + 'aria-label="Close"><i class="fa  fa-remove"></i></button>'
//                                    + '<strong>Exito! </strong>'
//                                    + data.responseJSON.mensaje
//                                    + '</div>');
//                            $(grid_selector).trigger('reloadGrid');
//
//                        }
//                    }
//
//                }
//            }
        ],
        viewrecords: true,
        rowNum: 10,
        rowList: [10, 20, 30],
        pager: pager_selector,
        altRows: true,
        loadtext: "Cargando...",
        emptyrecords: "No se encontaron datos.",
        pgtext: "Pagina {0} de {1}",
        postData: {
            atributos: "id,nombre",
            filters: null,
            estado: action,
            todos: false
        },
        jsonReader: {
            root: 'retorno',
            page: 'page',
            total: 'total',
            records: function(obj) {
                if (obj.retorno !== null) {
                    return obj.retorno.length;
                } else {
                    return 0;
                }

            }
        },
        //toppager: true,
        loadComplete: function() {
            var table = this;
            setTimeout(function() {
                //styleCheckbox(table);

                //updateActionIcons(table);
                updatePagerIcons(table);
                //enableTooltips(table);
            }, 0);
        },
        gridComplete: function() {
            var ids = $(grid_selector).getDataIDs();
            console.log(ids);
            var datos = $(grid_selector).getGridParam();
            //console.log($(grid_selector));
            for (var i = 0; i < ids.length; i++) {
                var cl = ids[i];
                var dato = $(grid_selector).jqGrid('getRowData', cl);
                //var asignar = '';
                //var editForm = '';
                var ce = '';
                //var visuali = '';
                //var desact = '';
                //var activar = '';
                var ini = '<div style="float: none;" class="btn-group btn-group-sm">';
                var fin = '</div>';
                var recibo = '';
                if (isStatus) {
                    
                    
                    
                    
                    
                    var estado = dato.activo;
                    if (estado === 'S') {
                        var labelActivo = '<span class="table-estado label label-success" value="S">Activo</span>';
                        if (isEditarInline) {

                            edit = editInlineButton(cl, permisoEditar);
                            $(grid_selector).setRowData(ids[i], {act: edit});
                            
                            
                            
                            
                        } else {

                            //asignar = "";
                            //visuali = visualizarButton(cl, permisoVisualizar, null);
                            //edit = editInlineButton(cl, permisoEditar);
                            //editForm = detalleButton(cl, permisoDetalle, 'Realizar Compra', 'orden/compras/realizar');
                            //desact = desactivarButton(cl, permisoDesactivar);
                            $(grid_selector).setRowData(ids[i], {act: ini + fin});
                        }
                        $(grid_selector).setRowData(ids[i], {activo: labelActivo});
                    } else {
                        var labelInactivo = '<span class="table-estado label label-danger"  value="N" >Inactivo</span>';
                        //activar = activarButton(cl, permisoActivar);
                        $(grid_selector).setRowData(ids[i], {act: ini + fin});
                        $(grid_selector).setRowData(ids[i], {activo: labelInactivo});
                    }
                } else {
                    if (isEditarInline) {

                        edit = editInlineButton(cl, permisoEditar);
                        $(grid_selector).setRowData(ids[i], {act: edit});
                    } else {

                        //asignar = asigButton(cl, true);
                        //visuali = visualizarButton(cl, permisoVisualizar, null);
                        //editForm = editFormButton(cl, permisoEditar);
                        $(grid_selector).setRowData(ids[i], {act: ini + fin});
                    }
                }


            }
        },
        editurl: "/editar", //nothing is saved
        caption: "Documentos a Pagar",
        subGrid: false


    });
    $(window).triggerHandler('resize.jqGrid');
    $(grid_selector).jqGrid('setGridWidth', $(".content").width());
    //navButtons
    jQuery(grid_selector).jqGrid('navGrid', pager_selector,
            {//navbar options
                edit: false,
                editicon: 'ace-icon fa fa-pencil blue',
                add: false,
                addicon: 'ace-icon fa fa-plus-circle purple',
                del: false,
                delicon: 'ace-icon fa fa-trash-o red',
                search: false,
                searchicon: 'ace-icon fa fa-search orange',
                refresh: true,
                refreshicon: 'ace-icon fa fa-refresh green',
                view: false,
                viewicon: 'ace-icon fa fa-search-plus grey',
            },
            {
                //edit record form
                //closeAfterEdit: true,
                //width: 700,
                recreateForm: true,
                beforeShowForm: function(e) {

                }
            },
    {
        //new record form
        //width: 700,
        closeAfterAdd: true,
        recreateForm: true,
        viewPagerButtons: false,
        beforeShowForm: function(e) {

        }
    },
    {
        //delete record form
        recreateForm: true,
        beforeShowForm: function(e) {

        },
        onClick: function(e) {
            //alert(1);
        }
    },
    {
        //search form
        recreateForm: true,
        afterShowSearch: function(e) {


        },
        afterRedraw: function() {
            style_search_filters($(this));
        }
        ,
        multipleSearch: true,
        /**
         multipleGroup:true,
         showQuery: true
         */
    }
    )
});

function updatePagerIcons(table) {
    var replacement =
            {
                'ui-icon-seek-first': 'ace-icon fa fa-angle-double-left bigger-140',
                'ui-icon-seek-prev': 'ace-icon fa fa-angle-left bigger-140',
                'ui-icon-seek-next': 'ace-icon fa fa-angle-right bigger-140',
                'ui-icon-seek-end': 'ace-icon fa fa-angle-double-right bigger-140'
            };
    $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function() {
        var icon = $(this);
        var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

        if ($class in replacement)
            icon.attr('class', 'ui-icon ' + replacement[$class]);
    });
}



function parseBolean(val) {
    if (val.toLowerCase() === 'true') {
        return true;
    } else if (val.toLowerCase() === 'false' || val.toLowerCase() === '') {
        return false;
    }

}
$("#verPagos").click(function(){
        verPagos(id);
});
    
function verPagos(id_compra) {
    $('#pagos-modal').modal('show');
    
    var grid_selector_modal = "#grid-modal";
    var pager_selector_modal = "#grid-pager-modal";
    
    $(window).on('resize.jqGrid', function() {
        setTimeout(function() {
            $(grid_selector_modal).jqGrid('setGridWidth', $(".grid-container").width());

        }, 0);
    });
    
    $(window).triggerHandler('resize.jqGrid');
    $(grid_selector_modal).jqGrid('setGridWidth', $(".grid-container").width());
    
}
            





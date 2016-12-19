$(document).ready(function(data) {
    
    var id_venta = id;
    

    var isEditarInline = false;
    var isStatus = true;

    var permisoActivar = parseBolean($(this).find('.tablactivate-permiso').text());
    var permisoDesactivar = parseBolean($(this).find('.tabldelete-permiso').text());
    var permisoEditar = parseBolean($(this).find('.tabledit-permiso').text());
    var permisoVisualizar = parseBolean($(this).find('.tablvisualizar-permiso').text());
    var permisoDetalle = parseBolean($(this).find('.tabladd-permiso').text());

    var grid_selector = "#grid-modal";
    var pager_selector = "#grid-pager-modal";


    $(window).on('resize.jqGrid', function() {
        setTimeout(function() {
            $(grid_selector).jqGrid('setGridWidth', $(".grid-container").width());

        }, 0);
    });
    
    $(grid_selector).jqGrid({
        url: CONTEXT_ROOT + '/ventas/cobrosrecibidos/listar?_search=false&todos=true&rows=10&page=1&sidx=&sord=asc&idVenta=' + id_venta,
        datatype: 'json',
        mtype: 'GET',
        autowidth: true, 
        shrinkToFit: true,
        height: 300,
        hidegrid: false,
        rownumbers: true,
        width: 500,
        colNames: ['FECHA', 'NRO. CUOTA', 'IMPORTE', 'SALDO', 'VUELTO', 'INTERES', 'NETO', ''],
        colModel: [
            {name: 'fechaIngreso', index: 'fechaIngreso', width: 70},
            {name: 'cuota', index: 'cuota', sortable: false, width: 70},
            {name: 'importe', index: 'importe', formatter: 'number', sortable: false, width: 100},
            {name: 'saldo', index: 'saldo', formatter: 'number', sortable: false, width: 100},
            {name: 'vuelto', index: 'vuelto', formatter: 'number', sortable: false, width: 100},
            {name: 'interes', index: 'interes', formatter: 'number', sortable: false, width: 100},
            {name: 'neto', index: 'neto', formatter: 'number', sortable: false, width: 100},
            {name: 'act', index: 'act', fixed: true, sortable: false, resize: false, width: 50,
                //               formatter: 'actions',
                formatoptions: {
                    onError: function(jqXHR, textStatus, errorThrwn) {
                        if (textStatus.status !== 200) {
                            $('#mensaje').append('<div class="alert alert-error">'
                                    + '<button class="close" data-dismiss="alert" type="button"'
                                    + '><i class="fa  fa-remove"></i></button>'
                                    + '<strong>Error ' + textStatus.status + ' ! </strong>'
                                    + 'Error al editar el registro'
                                    + '</div>');
                        }

                    },
                    onSuccess: function(data) {
                        if (data.responseJSON.error === true) {
                            $('#mensaje').append('<div class="alert alert-error">'
                                    + '<button class="close" data-dismiss="alert" type="button"'
                                    + '><i class="fa  fa-remove"></i></button>'
                                    + '<strong>Error! </strong>'
                                    + data.responseJSON.mensaje
                                    + '</div>');

                        } else {
                            $('#mensaje').append('<div class="alert alert-info alert-dismissible fade in">'
                                    + '<button type="button" class="close" data-dismiss="alert"'
                                    + 'aria-label="Close"><i class="fa  fa-remove"></i></button>'
                                    + '<strong>Exito! </strong>'
                                    + data.responseJSON.mensaje
                                    + '</div>');
                            $(grid_selector).trigger('reloadGrid');

                        }
                    }

                }
            }
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
                    
                        //esto mover a la subgrilla en los pagos
                        

                            recibo = '<a onmouseout="jQuery(this).removeClass(' + "'ui-state-hover'" + ')"'
                                    + ' onmouseover="jQuery(this).addClass(' + "'i-state-hover'" + ');" onclick=imprimirRecibo(' + cl + ');'
                                    + '  class=" btn btn-xs btn-info" style="float:left;cursor:pointer;" title="Imprimir recibo">'
                                    + ' <span class="fa fa-fw fa-print"></span></a>';

                            $(grid_selector).setRowData(ids[i], {act: ini + recibo + fin});
                        
    
                    
                    
                    
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
                            $(grid_selector).setRowData(ids[i], {act: ini + recibo + fin});
                        }
                        $(grid_selector).setRowData(ids[i], {activo: labelActivo});
                    } else {
                        var labelInactivo = '<span class="table-estado label label-danger"  value="N" >Inactivo</span>';
                        //activar = activarButton(cl, permisoActivar);
                        $(grid_selector).setRowData(ids[i], {act: ini + recibo + fin});
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
                        $(grid_selector).setRowData(ids[i], {act: ini + recibo + fin});
                    }
                }


            }
        },
        editurl: "/editar", //nothing is saved
        caption: "Cobros",
        subGrid: false


    });
    $(window).triggerHandler('resize.jqGrid');
    $(grid_selector).jqGrid('setGridWidth', $(".grid-container").width());
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
function imprimirRecibo(id_pago){
    console.log(id_pago);
    //Se genera el pdf del recibo
}


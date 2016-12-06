
$(document).ready(function(data) {

    var isEditarInline = true;
    var isStatus = false;

    /*if(action === "CREAR" || action === "AGREGAR"){
     */
    if (action === "VISUALIZAR") {
        var permisoVisualizar = false;
        var permisoEditar = false;
        var permisoAgegar = false;
    } else {
        var permisoAprobar = parseBolean($(this).find('.tablaprobar-permiso').text());
        var permisoRechazar = parseBolean($(this).find('.tablrechazar-permiso').text());
        var permisoEditar = parseBolean($(this).find('.tabledit-permiso').text());
        var permisoAgegar = parseBolean($(this).find('.tabladd-permiso').text());
    }


    /*
     }else{
     var permisoAprobar = false;
     var permisoRechazar = false;
     var permisoEditar = false;
     var permisoAgegar = false;
     }*/

    var grid_selector = "#grid";
    var pager_selector = "#grid-pager";

    $(window).on('resize.jqGrid', function() {
        setTimeout(function() {
            $(grid_selector).jqGrid('setGridWidth', $(".content").width());

        }, 0);
    });

    //ventaForm($("#idVenta").val(), action);

    $(grid_selector).jqGrid({
        url: CONTEXT_ROOT + '/ventas/vehiculos/listar',
        datatype: 'json',
        mtype: 'GET',
        height: 150,
        hidegrid: false,
        rownumbers: true,
        //width: $(".content").width(),
        colNames: ['ID', 'ID_VEHICULO', 'CHASIS', 'KILOMETRAJE', 'TIPO VEHICULO', 'MARCA', 'MODELO', 'ANHO', 'COLOR', 'TRASMISION', 'PRECIO COSTO', 'PRECIO VENTA', 'PROVEEDOR', 'CARACTERISTICA',''],
        colModel: [
            {name: 'id', index: 'id', key: true, hidden: true, width: 60, sorttype: "int", editable: false},
            {name: 'codigo', index: 'codigo', key: true, width: 100, editable: false},
            {name: 'chasis', index: 'chasis', key: true, width: 100, editable: false},
            {name: 'kilometraje', index: 'kilometraje', width: 100, editable: false},
            {name: 'tipo.nombre', index: 'tipo.nombre', width: 100, editable: true, edittype: 'select', editrules: {edithidden: true, custom: true, custom_func: customValidationMessage}},
            {name: 'marca.nombre', index: 'marca.nombre', width: 150, editable: true, edittype: 'select', editrules: {edithidden: true, custom: true, custom_func: customValidationMessage}},
            {name: 'modelo.nombre', index: 'modelo.nombre', width: 130, editable: true, edittype: 'select'},
            {name: 'anho', index: 'anho', width: 90, editable: true, sorttype: "date", unformat: pickYear, editrules: {edithidden: true, custom: true, custom_func: customValidationMessage}},
            {name: 'color', index: 'color', width: 90, sortable: false, editable: true, editrules: {edithidden: true, custom: true, custom_func: customValidationMessage}},
            {name: 'transmision', index: 'transmision', width: 110, editable: true, edittype: "select", editoptions: {value: "MECANICO:MECANICO;AUTOMATICO:AUTOMATICO"}},
            {name: 'precioCosto', index: 'precioCosto', width: 120, editable: true, formatter: 'number', edittype: "select"},
            {name: 'precioVenta', index: 'precioVenta', width: 160, sortable: false, formatter: 'number', resize: false, editable: true, disabled: true, editoptions: {disabled: true}, editrules: {edithidden: true, custom: true, custom_func: customValidationMessage}},
            {name: 'proveedor.nombre', index: 'proveedor.nombre', width: 160, sortable: false, editable: true, disabled: true, editoptions: {disabled: true}, resize: false},
            {name: 'caracteristica', index: 'caracteristica', width: 160, sortable: false, resize: false, editoptions: {disabled: true}, editrules: {edithidden: true, custom: true, custom_func: customValidationMessage}},
            {name: 'estado', index: 'estado', hidden: true, width: 100, editable: false}
        ],
        viewrecords: true,
        rowNum: 10,
        rowList: [10, 20, 30],
        pager: pager_selector,
        altRows: true,
        loadtext: "Cargando...",
        emptyrecords: "No se encontaron datos.",
        pgtext: "Pagina {0} de {1}",
//        beforeSelectRow: function(rowid, data) {
//            $('#validation-form').valid();
//        },
        multiselect: true,
        //multikey: "ctrlKey",
        multiboxonly: false,
        postData: {
            atributos: "id,nombre",
            filters: null,
            estado: 'APROBADO',
            todos: false,
            idVenta: function() {
                return $("#idVenta").val();
            }
        },
        onSelectRow: function(row, isSelected) {
            var data = $(grid_selector).jqGrid('getRowData', row);
            setTimeout(function() {
                var neto = parseInt($('#montoTotal').val());
                console.log(neto);
                if ($.isNumeric(neto) !== true) {
                    neto = 0;
                }
                console.log(neto);
                if (isSelected) {
                    neto = neto + parseInt(data.precioVenta);
                } else {
                    neto = neto - parseInt(data.precioVenta);
                }
                $('#montoTotal').val(neto);
            }, 0);
        },
        onSelectAll: function(aRowids, status) {
            console.log(aRowids);
            console.log(status);
            if (status) {
                var neto = parseInt($('#montoTotal').val());
                if ($.isNumeric(neto) !== true) {
                    neto = 0;
                }
                for (var i = 0; i < aRowids.length; i++) {
                    var data = $(grid_selector).jqGrid('getRowData', aRowids[i]);
                    var isSelect = $(grid_selector).jqGrid("getGridParam", "selarrrow");
                    console.log(isSelect);
                    console.log("neto");
                    neto = neto + parseInt(data.precioVenta);
                    console.log(neto+"netooo");


                }
                console.log(neto);
                $('#montoTotal').val(neto);
            } else {
                $('#montoTotal').val("0");
            }
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
        editurl: CONTEXT_ROOT + '/ventas/editar', //nothing is saved
        caption: "Detalle del venta",
        subGrid: false,
        subGridOptions: {
            plusicon: 'fa fa-fw fa-sort-amount-asc',
            minusicon: 'fa fa-fw fa-arrow-up'
        },
        subGridRowExpanded: function(subgrid_id, row_id) {
            // we pass two parameters
            // subgrid_id is a id of the div tag created within a table
            // the row_id is the id of the row
            // If we want to pass additional parameters to the url we can use
            // the method getRowData(row_id) - which returns associative array in type name-value
            // here we can easy construct the following
            var subgrid_table_id;
            subgrid_table_id = subgrid_id + "_t";
            $("#" + subgrid_id).html("<table id='" + subgrid_table_id + "' class='scroll'></table>");
            $("#" + subgrid_table_id).jqGrid({
                url: CONTEXT_ROOT + '/venta/detalles/listar?_search=false&todos=true&rows=10&page=1&sidx=&sord=asc&idDetalle=' + row_id,
                datatype: 'json',
                mtype: 'GET',
                colNames: ['ID', 'MONEDA', 'PRECIO', 'TOTAL', 'DESCUENTO', 'MONTO DESCUENTO', 'NETO', ''],
                colModel: [
                    {name: "id", key: true, hidden: true, index: "id", width: 100, align: "right", sortable: false},
                    {name: "moneda.nombre", index: "moneda.nombre", width: 100, align: "right", sortable: false},
                    {name: "precio", index: "precio", width: 100, align: "right", formatter: 'number', sortable: false, editable: false, disabled: true, editoptions: {disabled: true}},
                    {name: "total", index: "total", width: 100, align: "right", formatter: 'number', sortable: false, editable: true, disabled: true, editoptions: {disabled: true}},
                    {name: "porcentajeDescuento", index: "porcentajeDescuento", editable: true, width: 100, align: "right", sortable: false,
                        editoptions: {
                            dataEvents: [
//                                {type: 'click', fn: function(e) {
//                                        var total = this.value * 1;
//                                        $('input[name="total"]').val(total);
//                                    }},
                                {type: 'keypress', fn: function(e) {
                                        var total;
                                        setTimeout(function() {
                                            if ($.isNumeric(e.key) || e.key === 'Backspace') {
                                                total = $('input[name="total"]').val() * $('input[name="porcentajeDescuento"]').val() / 100;
                                                $('input[name="montoDescuento"]').val(total);
                                                var neto = $('input[name="total"]').val() - total;
                                                $('input[name="neto"]').val(neto);
                                            } else {
                                                $('input[name="total"]').val('');
                                                $.messager.alert('Error!!', 'Debe ingresar un valor numerico!!!');
                                            }
                                        }, 0);


                                    }}

                            ]}},
                    {name: "montoDescuento", index: "montoDescuento", width: 100, align: "right", formatter: 'number', editable: true, sortable: false, disabled: true, editoptions: {disabled: true}},
                    {name: "neto", index: "neto", width: 100, align: "right", formatter: 'number', editable: true, sortable: false, disabled: true, editoptions: {disabled: true}},
                    {name: 'act', index: 'act', fixed: true, sortable: false, resize: false,
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
                                cargarDatos(data.responseJSON.id);
                                $("#idVenta").val(data.responseJSON.id);
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
                height: '100%',
                rowNum: 10,
                sortname: 'num',
                altRows: true,
                sortorder: "asc",
                serializeRowData: function(postData) {
                    postData['nroFactura'] = $('#factura').val();
                    return postData;
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
                    var ids = $("#" + subgrid_table_id).getDataIDs();
                    var datos = $("#" + subgrid_table_id).getGridParam();
                    console.log(ids);
                    for (var i = 0; i < ids.length; i++) {
                        var cl = ids[i];
                        var dato = $("#" + subgrid_table_id).jqGrid('getRowData', cl);
                        var asignar = '';
                        var editForm = '';
                        var ce = '';
                        var visuali = '';
                        var desact = '';
                        var activar = '';
                        var button = '';
                        var ini = '<div style="float: none;" class="btn-group btn-group-sm">';
                        var fin = '</div>';

                        if ($('#detallado').is(':checked')) {

                            edit = editInlineButton(cl, permisoEditar);
                            $("#" + subgrid_table_id).setRowData(ids[i], {act: ini + edit + fin});
                        }

                    }
                },
                editurl: CONTEXT_ROOT + "/ventas/detalles/descuento"
            });
        }

    });
    $(window).triggerHandler('resize.jqGrid');
    $(grid_selector).jqGrid('setGridWidth', $(".content").width());
    $(grid_selector).jqGrid('navGrid', pager_selector, {edit: false, add: false, del: false, search: false});
    $(grid_selector).jqGrid('inlineNav', pager_selector,
            {
                edit: false,
                add: permisoAgegar,
                addtext: 'Agregar',
                addicon: "ui-icon ace-icon fa fa-plus-circle purple",
                save: true,
                savetext: 'Guardar',
                saveicon: "ui-icon-disk",
                cancel: true,
                cancelicon: "ui-icon-cancel",
                canceltext: 'Cancelar',
                refresh: true,
                cloneToTop: true,
                "view": false,
                addParams: {
                    //position: 'last',
                    useDefValues: true,
                    reloadAfterSubmit: true,
                    addRowParams: {
                        url: CONTEXT_ROOT + '/ventas/guardar',
                        mtype: "POST",
                        datatype: 'json',
                        keys: true,
                        successfunc: function(data) {
                            if (data.responseJSON.id !== null && data.responseJSON.id !== "") {
                                cargarDatos(data.responseJSON.id);
                                $("#idVenta").val(data.responseJSON.id);
                            }
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

                                $(grid_selector).setGridParam({postData: {todos: false,
                                        idVenta: data.responseJSON.id}});
//                                setTimeout(function() {
//                                    postData = $(grid_selector).jqGrid("getGridParam", "postData");
//                                
//                                    postData.filters = JSON.stringify({
//                                        todos: false,
//                                        idPedido: data.responseJSON.id
//                                    });
//                                }, 0);


                                $(grid_selector).trigger('reloadGrid');

                            }

                        },
                        errorfunc: function(jqXHR, textStatus, errorThrwn) {
                            if (textStatus.status !== 200) {
                                $('#mensaje').append('<div class="alert alert-error">'
                                        + '<button class="close" data-dismiss="alert" type="button"'
                                        + '><i class="fa  fa-remove"></i></button>'
                                        + '<strong>Error ' + textStatus.status + ' ! </strong>'
                                        + 'Error al editar el registro'
                                        + '</div>');
                            }
                        },
                        aftersavefunc: function(response) {
                            console.log('1231456465465');
                        }
                    },
                    afterSubmit: function(response, postdata) {
                        if (response.responseText === "") {
                            $(this).jqGrid('setGridParam',
                                    {datatype: 'json'}).trigger('reloadGrid'); //Reloads the grid after Add
                            return [true, ''];
                        } else {
                            $(this).jqGrid('setGridParam',
                                    {datatype: 'json'}).trigger('reloadGrid'); //Reloads the grid after Add
                            return [false, response.responseText];
                        }
                    },
                    onclickSubmit: function(rowid) {
                        console.log('cliccccc11');
                    },
                    beforeSubmitCell: function() {
                        alert('holaaa');
                    }
                }
            });

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

function guardarRegistro() {
    if (val.toLowerCase() === 'true') {
        return true;
    } else if (val.toLowerCase() === 'false' || val.toLowerCase() === '') {
        return false;
    }

}

function parseBolean(val) {
    if (val.toLowerCase() === 'true') {
        return true;
    } else if (val.toLowerCase() === 'false' || val.toLowerCase() === '') {
        return false;
    }

}






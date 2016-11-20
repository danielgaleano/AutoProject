
$(document).ready(function(data) {

    var isEditarInline = true;
    var isStatus = false;

    /*if(action === "CREAR" || action === "AGREGAR"){
     */
    var permisoAprobar = parseBolean($(this).find('.tablaprobar-permiso').text());
    var permisoRechazar = parseBolean($(this).find('.tablrechazar-permiso').text());
    var permisoEditar = parseBolean($(this).find('.tabledit-permiso').text());
    
    if(action === 'CREAR'){
        var permisoAgegar = true;
    }else{
        var permisoAgegar = false;
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

    //compraForm($("#idCompra").val(), action);

    $(grid_selector).jqGrid({
        url: CONTEXT_ROOT + '/compra/detalles/listar',
        datatype: 'json',
        mtype: 'GET',
        height: 150,
        hidegrid: false,
        rownumbers: true,
        //width: $(".content").width(),
        colNames: ['ID', 'ID_VEHICULO', 'TIPO VEHICULO', 'MARCA', 'MODELO', 'CARACTERISTICA', 'ANHO', 'COLOR', 'TRASMISION', 'MONEDA', 'COTIZACION', 'PRECIO', 'NETO', '', ''],
        colModel: [
            {name: 'id', index: 'id', key: true, hidden: true, width: 60, sorttype: "int", editable: false},
            {name: 'vehiculo.codigo', index: 'vehiculo.codigo', key: true, width: 100, editable: false},
            {name: 'vehiculo.tipo.nombre', index: 'vehiculo.tipo.nombre', width: 100, editable: true, edittype: 'select', editrules: {edithidden: true, custom: true, custom_func: customValidationMessage},
                editoptions: {
                    dataUrl: CONTEXT_ROOT + '/tipos/listar?_search=false&todos=true&rows=10&page=1&sidx=&sord=asc',
                    buildSelect: function(resp) {

                        var sel = '<select>';
                        sel += '<option value="">Seleccione la opcion</option>';
                        var obj = $.parseJSON(resp);
//                        var sel_id = $(grid_selector).jqGrid('getGridParam', 'selrow');
//                        var value = $(grid_selector).jqGrid('getCell',sel_id ,'tipo.id');

                        $.each(obj.retorno, function() {
                            sel += '<option value="' + this['id'] + '">' + this['nombre'] + '</option>'; // label and value are returned from Java layer
                        });
                        sel += '</select>';
                        return sel;
                    }
                }},
            {name: 'vehiculo.marca.nombre', index: 'vehiculo.marca.nombre', width: 100, editable: true, edittype: 'select', editrules: {edithidden: true, custom: true, custom_func: customValidationMessage},
                editoptions: {
                    dataUrl: CONTEXT_ROOT + '/marcas/listar?_search=false&todos=true&rows=10&page=1&sidx=&sord=asc',
                    buildSelect: function(resp) {

                        var sel = '<select>';
                        sel += '<option value="">Seleccione la opcion</option>';
                        var obj = $.parseJSON(resp);
//                        var sel_id = $(grid_selector).jqGrid('getGridParam', 'selrow');
//                        var value = $(grid_selector).jqGrid('getCell',sel_id ,'tipo.id');

                        $.each(obj.retorno, function() {
                            sel += '<option value="' + this['id'] + '">' + this['nombre'] + '</option>'; // label and value are returned from Java layer
                        });
                        sel += '</select>';
                        return sel;
                    }
                }},
            {name: 'vehiculo.modelo.nombre', index: 'vehiculo.modelo.nombre', width: 100, editable: true, edittype: 'select', editrules: {edithidden: true, custom: true, custom_func: customValidationMessage},
                editoptions: {
                    dataUrl: CONTEXT_ROOT + '/marcas/listar?_search=false&todos=true&rows=10&page=1&sidx=&sord=asc',
                    buildSelect: function(resp) {

                        var sel = '<select>';
                        sel += '<option value="">Seleccione la opcion</option>';
                        var obj = $.parseJSON(resp);
//                        var sel_id = $(grid_selector).jqGrid('getGridParam', 'selrow');
//                        var value = $(grid_selector).jqGrid('getCell',sel_id ,'tipo.id');

                        $.each(obj.retorno, function() {
                            sel += '<option value="' + this['id'] + '">' + this['nombre'] + '</option>'; // label and value are returned from Java layer
                        });
                        sel += '</select>';
                        return sel;
                    }
                }},
            {name: 'vehiculo.caracteristica', index: 'vehiculo.caracteristica', width: 130, sortable: false, editable: true, edittype: "textarea", editoptions: {rows: "2", cols: "10"}},
            {name: 'vehiculo.anho', index: 'vehiculo.anho', width: 90, editable: true, sorttype: "date", unformat: pickYear, editrules: {edithidden: true, custom: true, custom_func: customValidationMessage}},
            {name: 'vehiculo.color', index: 'vehiculo.color', width: 90, sortable: false, editable: true, editrules: {edithidden: true, custom: true, custom_func: customValidationMessage}},
            {name: 'vehiculo.trasmision', index: 'trasmision', width: 110, editable: true, edittype: "select", editoptions: {value: "MECANICO:MECANICO;AUTOMATICO:AUTOMATICO"}},
            {name: 'moneda.nombre', index: 'moneda.nombre', width: 90, editable: false, edittype: "select",
                editoptions: {
                    dataUrl: +'/monedas/listar?_search=false&todos=true&rows=10&page=1&sidx=&sord=asc',
                    buildSelect: function(resp) {

                        var sel = '<select>';
                        sel += '<option value="">Seleccione moneda</option>';
                        var obj = $.parseJSON(resp);
//                        var sel_id = $(grid_selector).jqGrid('getGridParam', 'selrow');
//                        var value = $(grid_selector).jqGrid('getCell',sel_id ,'tipo.id');

                        $.each(obj.retorno, function() {
                            sel += '<option value="' + this['id'] + '">' + this['nombre'] + '</option>'; // label and value are returned from Java layer
                        });
                        sel += '</select>';
                        return sel;
                    }
                }},
            {name: 'moneda.valor', index: 'moneda.valor', width: 160, sortable: false, formatter: 'number', editable: false, editrules: {edithidden: true, custom: true, custom_func: customValidationMessage}},
            {name: 'precio', index: 'precio', width: 90, sortable: false, editable: false, formatter: 'number', editrules: {edithidden: true, custom: true, custom_func: customValidationMessage}, //unformat: spinnerNumber,
                editoptions: {
                    dataEvents: [
                        {type: 'click', fn: function(e) {
                                var total = this.value * 1;
                                $('input[name="total"]').val(total);
                            }},
                        {type: 'keypress', fn: function(e) {
                                var total;
                                setTimeout(function() {
                                    if ($.isNumeric(e.key) || e.key === 'Backspace') {
                                        total = $('input[name="precio"]').val() * 1;
                                        $('input[name="total"]').val(total);
                                    } else {
                                        $('input[name="precio"]').val('');
                                        $.messager.alert('Error!!', 'Debe ingresar un valor numerico!!!');
                                    }
                                }, 0);


                            }}

                    ]}},
            // {name: 'cantidad', index: 'cantidad', width: 90, sorttype: "number", editable: false, //unformat: spinnerNumber,
            //     editoptions: {defaultValue: '1', type: 'number', min: 1, max: 100,
            //         dataEvents: [
            //             {type: 'click', fn: function(e) {
            //                     var precio = $('input[name="precio"]').val();
            //                     console.log(precio);
            //                     var total = this.value * precio;
            //                     $('input[name="total"]').val(total);
            //                 }}
            //         ]}},
            {name: 'neto', index: 'neto', width: 90, sortable: false, formatter: 'number', editable: false},
            {name: 'estadoCompra', index: 'estadoCompra', hidden: true, width: 110, editable: false},
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
        afterInserRow: function(rowid, data) {
            console.log('dsdsdsdsdsdsdsds');
        },
        serializeRowData: function(postData) {
            if ($.isNumeric(postData.id) !== true) {
                postData.id = "";
            }
            if ($("#idCompra").val() !== null && $("#idCompra").val() !== "") {
                postData['compra.id'] = $("#idCompra").val();
            } else {
                postData['compra.factura'] = $('#factura').val();
                postData['compra.ruc'] = $('#ruc').val();
                postData['compra.fechaCompra'] = $('#id-date-picker-compra').val();
                postData['compra.fechaCuota'] = $('#id-date-picker-cuota').val();
                postData['compra.cliente.id'] = $('#cliente').val();
            }
            postData['tipo.id'] = postData['tipo.nombre'];
            postData['marca.id'] = postData['marca.nombre'];
            postData['moneda.id'] = postData['moneda.nombre'];
            delete postData['tipo.nombre'];
            delete postData['marca.nombre'];
            delete postData['moneda.nombre'];
            return postData;
        },
        postData: {
            atributos: "id,nombre",
            filters: null,
            estado: 'APROBADO',
            todos: false,
            idCompra: function() {
                return $("#idCompra").val();
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
        gridComplete: function() {
            var ids = $(grid_selector).getDataIDs();
            var datos = $(grid_selector).getGridParam();
            //console.log($(grid_selector));
            for (var i = 0; i < ids.length; i++) {
                var cl = ids[i];
                var dato = $(grid_selector).jqGrid('getRowData', cl);
                var asignar = '';
                var editForm = '';
                var ce = '';
                var visuali = '';
                var desact = '';
                var activar = '';
                var button = '';
                var ini = '<div style="float: none;" class="btn-group btn-group-sm">';
                var fin = '</div>';


                if (isStatus) {
                    var estado = dato.estadoCompra;
                    if (estado === 'APROBADO') {
                        // var labelActivo = '<span class="table-estado label label-success" value="S">Activo</span>';
                        if (isEditarInline) {

                            activar = aprobarButton(cl, permisoAprobar)
                            desact = rechazarButton(cl, permisoRechazar);
                            visuali = visualizarButton(cl, true);
                            edit = editInlineButton(cl, permisoEditar);
                            $(grid_selector).setRowData(ids[i], {act: ini + edit + activar + desact + fin});

                        }
//                        else {
//
//                            asignar = "";
//                            visuali = visualizarButton(cl, permisoVisualizar);
//                            editForm = editFormButton(cl, permisoEditar);
//                            desact = desactivarButton(cl, permisoDesactivar);
//                            $(grid_selector).setRowData(ids[i], {act: ini + editForm + asignar + visuali + desact + fin});
//                        }
                        // $(grid_selector).setRowData(ids[i], {activo: labelActivo});
                    } else if (estado === 'APROBADO') {

                        //var labelInactivo = '<span class="table-estado label label-danger"  value="N" >Inactivo</span>';
                        desact = rechazarButton(cl, permisoRechazar);

                        $(grid_selector).setRowData(ids[i], {act: ini + desact + fin});
                        //$(grid_selector).setRowData(ids[i], {activo: labelInactivo});
                    }
                }
                else {
                    if (isEditarInline) {

                        edit = editInlineButton(cl, true);
                        $(grid_selector).setRowData(ids[i], {act: ini + edit + button + activar + desact + fin});
                    } else {

                        //asignar = asigButton(cl, true);
                        visuali = visualizarButton(cl, permisoVisualizar);
                        editForm = editFormButton(cl, permisoEditar);
                        $(grid_selector).setRowData(ids[i], {act: ini + editForm + asignar + visuali + fin});
                    }
                }


            }
        },
        editurl: "/compras/detalles/guardar", //nothing is saved
        caption: "Detalle del compra",
        subGrid: true,
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
                url: CONTEXT_ROOT + '/compra/detalles/listar?_search=false&todos=true&rows=10&page=1&sidx=&sord=asc&idDetalle=' + row_id,
                datatype: 'json',
                mtype: 'GET',
                colNames: ['ID', 'MONEDA', 'PRECIO', 'DESCUENTO', 'MONTO DESCUENTO', 'NETO', ''],
                colModel: [
                    {name: "id", key: true, hidden: true, index: "id", width: 100, align: "right", sortable: false},
                    {name: "moneda.nombre", index: "moneda.nombre", width: 100, align: "right", sortable: false},
                    {name: "precio", index: "precio", width: 100, align: "right", formatter: 'number', sortable: false, editable: true, disabled: true, editoptions: {disabled: true}},
                    {name: "porcentajeDescuento", index: "porcentajeDescuento", editable: true, width: 100, align: "right", sortable: false,
                        editoptions: {
                            dataEvents: [
                                {type: 'click', fn: function(e) {
                                        var total = this.value * 1;
                                        $('input[name="total"]').val(total);
                                    }},
                                {type: 'keypress', fn: function(e) {
                                        var total;
                                        setTimeout(function() {
                                            if ($.isNumeric(e.key) || e.key === 'Backspace') {
                                                total = $('input[name="precio"]').val() * $('input[name="porcentajeDescuento"]').val() / 100;
                                                $('input[name="montoDescuento"]').val(total);
                                                var neto = $('input[name="precio"]').val() - total;
                                                $('input[name="neto"]').val(neto);
                                            } else {
                                                $('input[name="precio"]').val('');
                                                $.messager.alert('Error!!', 'Debe ingresar un valor numerico!!!');
                                            }
                                        }, 0);


                                    }}

                            ]}},
                    {name: "montoDescuento", index: "montoDescuento", width: 100, align: "right", editable: true, sortable: false, disabled: true, editoptions: {disabled: true}},
                    {name: "neto", index: "neto", width: 100, align: "right", editable: true, sortable: false, disabled: true, editoptions: {disabled: true}},
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
                                    cargarDatos(id);

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

                            edit = editInlineButton(cl, true);
                            $("#" + subgrid_table_id).setRowData(ids[i], {act: ini + edit + fin});
                        }

                    }
                },
                editurl: CONTEXT_ROOT + "/compras/detalles/descuento"
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
                        url: +'/compra/detalles/guardar',
                        mtype: "POST",
                        datatype: 'json',
                        keys: true,
                        successfunc: function(data) {
                            if (data.responseJSON.id !== null && data.responseJSON.id !== "") {
                                compraForm(data.responseJSON.id, "recargar");
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
                                $(grid_selector).setGridParam({postData: {idCompra: data.responseJSON.id, todos: false, atributos: "id,nombre", filters: null}});

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

function descuentoModal(val) {
    //$('#validation-form').submit();
    $('#idDetPedido').val(val);
    var jqXHR = $.get(CONTEXT_ROOT + "/pedido/detalles/" + val, function(response, textStatus, jqXHR) {

        if (response.error) {
            $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                    + '<button class="close" data-dismiss="alert" type="button"'
                    + '><i class="fa  fa-remove"></i></button>'
                    + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                    + response.mensaje
                    + '</div>');
        } else {
            var pedido = response.data;
            $('#precioCompra').val(pedido.neto);

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
    $('#ex1').appendTo('body').modal();
}
            
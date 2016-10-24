
$(document).ready(function(data) {

    var isEditarInline = true;
    var isStatus = true;

    if (action === "CREAR" || action === "AGREGAR") {
        var permisoAprobar = parseBolean($(this).find('.tablaprobar-permiso').text());
        var permisoRechazar = parseBolean($(this).find('.tablrechazar-permiso').text());
        var permisoEditar = parseBolean($(this).find('.tabledit-permiso').text());
        var permisoAgegar = parseBolean($(this).find('.tabladd-permiso').text());
    } else {
        var permisoAprobar = false;
        var permisoRechazar = false;
        var permisoEditar = false;
        var permisoAgegar = false;
    }

    var grid_selector = "#grid";
    var pager_selector = "#grid-pager";

    $(window).on('resize.jqGrid', function() {
        setTimeout(function() {
            $(grid_selector).jqGrid('setGridWidth', $(".content").width());

        }, 0);
    });

    pedidoForm($("#idPedido").val(), action);

    $(grid_selector).jqGrid({
        url: CONTEXT_ROOT + '/pedido/detalles/listar',
        datatype: 'json',
        mtype: 'GET',
        height: 150,
        hidegrid: false,
        rownumbers: true,
        //width: $(".content").width(),
        colNames: ['ID', 'CODIGO', 'TIPO VEHICULO', 'MARCA', 'MODELO', 'CARACTERISTICA', 'ANHO', 'COLOR', 'TRASMISION', 'MONEDA', 'COTIZACION', 'PRECIO', 'TOTAL', 'NETO', 'CONFIRMADO', ''],
        colModel: [
            {name: 'id', index: 'id', key: true, hidden: true, width: 60, sorttype: "int", editable: false},
            {name: 'vehiculo.codigo', index: 'vehiculo.codigo', key: true, width: 150, editable: false},
            {name: 'vehiculo.tipo.nombre', index: 'vehiculo.tipo.nombre', width: 190, editable: true, edittype: 'select', editrules: {edithidden: true, custom: true, custom_func: customValidationMessage},
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
            {name: 'vehiculo.marca.nombre', index: 'vehiculo.marca.nombre', width: 150, editable: true, edittype: 'select', editrules: {edithidden: true, custom: true, custom_func: customValidationMessage},
                editoptions: {
                    dataEvents: [
                        {type: 'change', fn: function(e) {
                                var jqXHR = $.get(CONTEXT_ROOT + "/modelos/listar?_search=false&todos=true&idMarca=" + this.value + "&rows=10&page=1&sidx=&sord=asc", function(response, textStatus, jqXHR) {
                                    var sel = '<option value="">Seleccione opcion</option>';
                                    $.each(response.retorno, function() {

                                        sel += '<option value="' + this['id'] + '">' + this['nombre'] + '</option>'; // label and value are returned from Java layer

                                    });
                                    console.log($('select[name="vehiculo.modelo.nombre"]'));
                                    $('select[name="vehiculo.modelo.nombre"]').empty().append(sel);
                                });

                            }}

                    ],
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
            {name: 'vehiculo.modelo.nombre', index: 'vehiculo.modelo.nombre', width: 130, editable: true, edittype: 'select',
                editrules: {edithidden: true, custom: true, custom_func: customValidationMessage},
                editoptions: {value: {'': 'Seleccione Opcion'}}
            },
            {name: 'vehiculo.caracteristica', index: 'caracteristica', width: 200, sortable: false, editable: true, edittype: "textarea", editoptions: {rows: "2", cols: "10"}},
            {name: 'vehiculo.anho', index: 'anho', width: 80, editable: true, sorttype: "date", unformat: pickYear, editrules: {edithidden: true, custom: true, custom_func: customValidationMessage}},
            {name: 'vehiculo.color', index: 'color', width: 90, sortable: false, editable: true, editrules: {edithidden: true, custom: true, custom_func: customValidationMessage}},
            {name: 'vehiculo.transmision', index: 'trasmision', width: 180, editable: true, edittype: "select", editoptions: {value: "MECANICO:MECANICO;AUTOMATICO:AUTOMATICO"}},
            {name: 'moneda.nombre', index: 'moneda.nombre', width: 120, editable: true, edittype: "select",
                editoptions: {
                    dataEvents: [
                        {type: 'change', fn: function(e) {
                                var jqXHR = $.get(CONTEXT_ROOT + "/monedas/"+ this.value, function(response, textStatus, jqXHR) {
                                    $('input[name="moneda.valor"]').val(response.data.valor);
                                });

                            }}

                    ],
                    dataUrl: CONTEXT_ROOT + '/monedas/listar?_search=false&todos=true&rows=10&page=1&sidx=&sord=asc',
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
            {name: 'moneda.valor', index: 'moneda.valor', width: 120, sortable: false, formatter:'number', editable: true, editrules: {edithidden: true, custom: true, custom_func: customValidationMessage}},
            {name: 'precio', index: 'precio', width: 90, sortable: false, editable: true, formatter:'number', editrules: {edithidden: true, custom: true, custom_func: customValidationMessage}, //unformat: spinnerNumber,
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
//            {name: 'cantidad', index: 'cantidad', width: 90, sorttype: "number", editable: false, //unformat: spinnerNumber,
//                editoptions: {defaultValue: '1', type: 'number', min: 1, max: 100,
//                    dataEvents: [
//                        {type: 'click', fn: function(e) {
//                                var precio = $('input[name="precio"]').val();
//                                console.log(precio);
//                                var total = this.value * precio;
//                                $('input[name="total"]').val(total);
//                            }}
//                    ]}},
            {name: 'total', index: 'total', width: 110, sortable: false, formatter:'number', editable: true},
            {name: 'neto', index: 'neto', width: 150, sortable: false, formatter:'number', editable: false},
            {name: 'estadoPedido', index: 'estadoPedido', width: 150, editable: false},
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
            if ($("#idPedido").val() !== null && $("#idPedido").val() !== "") {
                postData['pedido.id'] = $("#idPedido").val();
            } else {
                postData['pedido.codigo'] = $('#codigo').val();
                postData['pedido.observacion'] = $('#observacion').val();
                postData['pedido.fecha'] = $('#id-date-picker').val();
                postData['pedido.proveedor.id'] = $('#proveedor').val();
            }
            postData['vehiculo.tipo.id'] = postData['vehiculo.tipo.nombre'];
            postData['vehiculo.marca.id'] = postData['vehiculo.marca.nombre'];
            postData['vehiculo.modelo.id'] = postData['vehiculo.modelo.nombre'];
            postData['moneda.id'] = postData['moneda.nombre'];
            delete postData['vehiculo.tipo.nombre'];
            delete postData['vehiculo.marca.nombre'];
            delete postData['vehiculo.modelo.nombre'];
            delete postData['moneda.nombre'];
            return postData;
        },
        postData: {
            atributos: "id,nombre",
            filters: null,
            todos: false,
            idPedido: function() {

                return $("#idPedido").val();

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
                var ini = '<div style="float: none;" class="btn-group btn-group-sm">';
                var fin = '</div>';
                if (isStatus) {
                    var estado = dato.estadoPedido;
                    if (estado === 'PENDIENTE') {
                        // var labelActivo = '<span class="table-estado label label-success" value="S">Activo</span>';
                        if (isEditarInline) {

                            activar = aprobarButton(cl, permisoAprobar)
                            desact = rechazarButton(cl, permisoRechazar);
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
//                else {
//                    if (isEditarInline) {
//
//                        edit = editInlineButton(cl, permisoEditar);
//                        $(grid_selector).setRowData(ids[i], {act: edit});
//                    } else {
//
//                        //asignar = asigButton(cl, true);
//                        visuali = visualizarButton(cl, permisoVisualizar);
//                        editForm = editFormButton(cl, permisoEditar);
//                        $(grid_selector).setRowData(ids[i], {act: ini + editForm + asignar + visuali + fin});
//                    }
//                }


            }
        },
        editurl: "/editar", //nothing is saved
        caption: "Detalle del Pedido"

    });
    $('select[name="marca.nombre"]').change(function(e) {
        console.log(e);
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
                        url: CONTEXT_ROOT + '/pedido/detalles/guardar',
                        mtype: "POST",
                        datatype: 'json',
                        keys: true,
                        successfunc: function(data) {
                            if (data.responseJSON.id !== null && data.responseJSON.id !== "") {
                                pedidoForm(data.responseJSON.id, "recargar");
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
                                postData = $(grid_selector).jqGrid("getGridParam", "postData");
                                
                                postData.filters = JSON.stringify({
                                    todos: false,
                                    idPedido: data.responseJSON.id
                                });

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
            
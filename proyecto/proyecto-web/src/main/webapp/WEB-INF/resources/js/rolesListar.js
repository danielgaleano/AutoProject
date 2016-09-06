
$(document).ready(function(data) {

    var isEditarInline = true;
    var isStatus = true;

    var permisoActivar = parseBolean($(this).find('.tablactivate-permiso').text());
    var permisoDesactivar = parseBolean($(this).find('.tabldelete-permiso').text());
    var permisoEditar = parseBolean($(this).find('.tabledit-permiso').text());
    var permisoAsignar = parseBolean($(this).find('.tablasignar-permiso').text());

    var grid_selector = "#grid";
    var pager_selector = "#grid-pager";


    $(window).on('resize.jqGrid', function() {
        setTimeout(function() {
            $(grid_selector).jqGrid('setGridWidth', $(".content").width());

        }, 0);
    });
    $(grid_selector).jqGrid({
        url: CONTEXT_ROOT + '/roles/listar',
        datatype: 'json',
        mtype: 'GET',
        height: 360,
        hidegrid: false,
        rownumbers: true,
        //width: $(".content").width(),
        colNames: ['ID', 'NOMBRE ROL', 'EMPRESA', 'EMPRESA', 'STATUS', ''],
        colModel: [
            {name: 'id', index: 'id', key: true, hidden: true, width: 60, sorttype: "int", editable: false},
            {name: 'nombre', index: 'nombre', width: 90, editable: true, editrules: { edithidden: true, custom: true, custom_func: customValidationMessage}},
            {name: 'empresa.nombre', index: 'empresa.nombre', width: 90, editable: false,
                cellattr: function(rowid, tv, rawObject, cm, rdata) {
                    if ($.isNumeric(rowid) !== true) {
                        return 'class="tableedit-combo-disable"';
                    }
                }
            },
            {name: 'empresa.id', index: 'empresa.id', width: 90, editable: true, hidden: true, editrules: {edithidden: false, custom: true, custom_func: customValidationMessage}, clearSearch: true,
                cellattr: function(rowid, tv, rawObject, cm, rdata) {
                    if ($.isNumeric(rowid) !== true) {
                        return 'class="tableedit-combo"';
                    }
                },
                edittype: 'select',
                editoptions: {
                    dataUrl: CONTEXT_ROOT + '/empresas/listar?_search=false&todos=true&rows=10&page=1&sidx=&sord=asc',
                    buildSelect: function(resp) {

                        var sel = '<select>';
                        sel += '<option value="">Seleccione la opcion</option>';
                        var obj = $.parseJSON(resp);
                        $.each(obj.retorno, function() {
                            sel += '<option value="' + this['id'] + '">' + this['nombre'] + '</option>'; // label and value are returned from Java layer
                        });
                        sel += '</select>';
                        return sel;
                    }
                }},
            {name: 'activo', index: 'activo', width: 90, editable: false},
            {name: 'act', index: 'act', align: 'center', fixed: true, sortable: false, resize: false}
        ],
        viewrecords: true,
        rowNum: 10,
        rowList: [10, 20, 30],
        pager: pager_selector,
        altRows: true,
        loadtext: "Cargando...",
        emptyrecords: "No se encontaron datos.",
        pgtext: "Pagina {0} de {1}",
        serializeRowData: function(data) {
            if ($.isNumeric(data.id) !== true) {
                data.id = "";
            }
            return data;
        },
        postData: {
            atributos:"id,nombre",
            filters:null,
            todos:false
        },
        jsonReader: {
            root: 'retorno',
            page: 'page',
            total: 'total',
            records: function(obj) {
                return obj.retorno.length;
            }
        },        
        onSelectRow: edit,
        loadComplete: function(rowid, rowdata, rowelem) {
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
                    var estado = dato.activo;
                    if (estado === 'S') {
                        var labelActivo = '<span class="table-estado label label-success" value="S">Activo</span>';
                        if (isEditarInline) {

                            edit = editInlineButton(cl, permisoEditar);
                            desact = desactivarButton(cl, permisoDesactivar);
                            asignar = asigButton(cl, permisoAsignar);
                            $(grid_selector).setRowData(ids[i], {act: ini + edit + asignar + desact + fin});

                        } else {

                            asignar = asigButton(cl, true);
                            //visuali = visualizarButton(cl, permisoVisualizar);
                            editForm = editFormButton(cl, permisoEditar);
                            desact = desactivarButton(cl, permisoDesactivar);
                            $(grid_selector).setRowData(ids[i], {act: ini + editForm + asignar + visuali + desact + fin});
                        }
                        $(grid_selector).setRowData(ids[i], {activo: labelActivo});
                    } else if (estado === 'N') {
                        var labelInactivo = '<span class="table-estado label label-danger"  value="N" >Inactivo</span>';
                        activar = activarButton(cl, permisoActivar);
                        $(grid_selector).setRowData(ids[i], {act: ini + activar + fin});
                        $(grid_selector).setRowData(ids[i], {activo: labelInactivo});
                    }
                } else {
                    if (isEditarInline) {

                        edit = editInlineButton(cl, permisoEditar);
                        $(grid_selector).setRowData(ids[i], {act: edit});
                    } else {

                        //asignar = asigButton(cl, true);
                        visuali = visualizarButton(cl, permisoVisualizar);
                        editForm = editFormButton(cl, permisoEditar);
                        $(grid_selector).setRowData(ids[i], {act: ini + editForm + asignar + visuali + fin});
                    }
                }

            }
        },
        editurl: CONTEXT_ROOT + "/roles/editar", //nothing is saved
        caption: "Roles"


    });



    $(window).triggerHandler('resize.jqGrid');
    $(grid_selector).jqGrid('setGridWidth', $(".content").width());
    $(grid_selector).jqGrid('navGrid', pager_selector, {edit: false, add: false, del: false, search: false});

    $(grid_selector).jqGrid('inlineNav', pager_selector,
            {
                edit: false,
                add: permisoAsignar,
                addtext: 'Agregar',
                addicon: "ui-icon ace-icon fa fa-plus-circle purple",
                save: permisoAsignar,
                savetext: 'Guardar',
                saveicon: "ui-icon-disk",
                cancel: permisoAsignar,
                cancelicon: "ui-icon-cancel",
                canceltext: 'Cancelar',
                refresh: true,
                cloneToTop: true,
                "view": false,
                addParams: {
                    //position: 'last',
                    useDefValues: true,
                    addRowParams: {
                        url: CONTEXT_ROOT + '/roles/guardar',
                        mtype: "POST",
                        datatype: 'json',
                        keys: true,
                        successfunc: function(data) {
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
                                console.log('reload');


                            }

                        }
                    },
                    addEditParams: {
                        successfunc: function(data) {
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
                                console.log('reload');


                            }

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

function edit(id)
{
    var tr = $('#grid').jqGrid('getInd', id, true);
    if ($.isNumeric(id) !== true) {
        $(tr).find('td.tableedit-combo').show();
        $(tr).find('td.tableedit-combo-disable').hide();
    }
}

function customValidationMessage(val, colname) {
    if (val.trim() == "") {
        return [false, colname + " es requerido "];
    } else {
        return [true, ""];
    }
}
            
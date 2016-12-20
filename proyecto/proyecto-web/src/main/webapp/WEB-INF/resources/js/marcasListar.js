
$(document).ready(function(data) {

    var isEditarInline = true;
    var isStatus = true;

    var permisoActivar = parseBolean($(this).find('.tablactivate-permiso').text());
    var permisoDesactivar = parseBolean($(this).find('.tabldelete-permiso').text());
    var permisoEditar = parseBolean($(this).find('.tabledit-permiso').text());
    var permisoVisualizar = parseBolean($(this).find('.tablvisualizar-permiso').text());
    var permisoAgregar = parseBolean($(this).find('.tabladd-permiso').text());
    var permisoModeloAgregar = parseBolean($(this).find('.tabladdmodelo-permiso').text());

    var grid_selector = "#grid";
    var pager_selector = "#grid-pager";


    $(window).on('resize.jqGrid', function() {
        setTimeout(function() {
            $(grid_selector).jqGrid('setGridWidth', $(".content").width());

        }, 0);
    });
    $(grid_selector).jqGrid({
        url: CONTEXT_ROOT + '/marcas/listar',
        datatype: 'json',
        mtype: 'GET',
        height: 360,
        hidegrid: false,
        rownumbers: true,
        //width: $(".content").width(),
        colNames: ['ID', 'MARCA', 'STATUS', ''],
        colModel: [
            {name: 'id', index: 'id', key: true, hidden: true, width: 150, sorttype: "int", editable: false},
            {name: 'nombre', index: 'nombre', width: 120, editable: true, editrules: {edithidden: true, custom: true, custom_func: customValidationMessage}},
            {name: 'activo', index: 'activo', width: 100, editable: false},
            {name: 'act', index: 'act', fixed: true, sortable: false, resize: false,
//                formatter: 'actions',
                formatoptions: {
//                    keys: true,
//                    delbutton: false, //disable delete button
//                    editbutton: true,
                    onError: function(jqXHR, textStatus, errorThrwn) {
                        if (textStatus.status !== 200){
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
                }}
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
            atributos: "id,nombre",
            filters: null,
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
                            desact = desactivarButton(cl, permisoDesactivar,"");
                            asignar = detalleButton(cl, permisoModeloAgregar,"Agregar Modelos","modelos/agregar");
                            visuali = visualizarButton(cl, permisoVisualizar,"Visualizar Modelos","modelos/visualizar");
                            $(grid_selector).setRowData(ids[i], {act: ini + edit + asignar + desact + fin});

                        } else {

                            asignar = asigButton(cl, true);
                            //visuali = visualizarButton(cl, permisoVisualizar);
                            editForm = editFormButton(cl, permisoEditar);
                            desact = desactivarButton(cl, permisoDesactivar);
                            $(grid_selector).setRowData(ids[i], {act: ini + editForm + asignar  + desact + fin});
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
                        $(grid_selector).setRowData(ids[i], {act: ini + editForm + asignar  + fin});
                    }
                }

            }
        },
        editurl: CONTEXT_ROOT + "/marcas/editar", //nothing is saved
        caption: "Marcas",
        subGrid: true,
        subGridOptions:{
            plusicon : 'fa fa-fw fa-sort-amount-asc',
            minusicon : 'fa fa-fw fa-arrow-up'
        },
        subGridRowExpanded: function(subgrid_id, row_id) {
        // we pass two parameters
        // subgrid_id is a id of the div tag created within a table
        // the row_id is the id of the row
        // If we want to pass additional parameters to the url we can use
        // the method getRowData(row_id) - which returns associative array in type name-value
        // here we can easy construct the following
           var subgrid_table_id;
           subgrid_table_id = subgrid_id+"_t";
           $("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table>");
           $("#"+subgrid_table_id).jqGrid({
                url:CONTEXT_ROOT + '/modelos/listar?_search=false&todos=true&rows=10&page=1&sidx=&sord=asc&idMarca='+row_id,
                datatype: 'json',
                mtype: 'GET',
                colNames: ['MODELO'],
                colModel: [
                    {name:"nombre",index:"nombre",width:200,key:true}
                  ],
                height: '100%',
                rowNum:10,
                sortname: 'num',
                sortorder: "asc",
                caption: "Modelos",
                jsonReader: {
                    root: 'retorno',
                    page: 'page',
                    total: 'total',
                    records: function(obj) {
                        if(obj.retorno !== null){
                            return obj.retorno.length;
                        }else{
                            return 0 ;
                        }
                        
                    }
              }
           });
       }


    });



    $(window).triggerHandler('resize.jqGrid');
    $(grid_selector).jqGrid('setGridWidth', $(".content").width());
    $(grid_selector).jqGrid('navGrid', pager_selector, {edit: false, add: false, del: false, search: false});

    $(grid_selector).jqGrid('inlineNav', pager_selector,
            {
                edit: false,
                add: permisoAgregar,
                addtext: 'Agregar',
                addicon: "ui-icon ace-icon fa fa-plus-circle purple",
                save: permisoAgregar,
                savetext: 'Guardar',
                saveicon: "ui-icon-disk",
                cancel: permisoAgregar,
                cancelicon: "ui-icon-cancel",
                canceltext: 'Cancelar',
                refresh: true,
                cloneToTop: true,
                "view": false,
                addParams: {
                    //position: 'last',
                    useDefValues: true,
                    addRowParams: {
                        url: CONTEXT_ROOT + '/marcas/guardar',
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


            
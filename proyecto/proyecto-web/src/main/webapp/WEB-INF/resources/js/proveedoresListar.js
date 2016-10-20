
$(document).ready(function(data) {

    var isEditarInline = false;
    var isStatus = true;

    var permisoActivar = parseBolean($(this).find('.tablactivate-permiso').text());
    var permisoDesactivar = parseBolean($(this).find('.tabldelete-permiso').text());
    var permisoEditar = parseBolean($(this).find('.tabledit-permiso').text());
    var permisoVisualizar = parseBolean($(this).find('.tablvisualizar-permiso').text());
    var permisoAsignar = parseBolean($(this).find('.tablasignar-permiso').text());

    var grid_selector = "#grid";
    var pager_selector = "#grid-pager";


    $(window).on('resize.jqGrid', function() {
        setTimeout(function() {
            $(grid_selector).jqGrid('setGridWidth', $(".content").width());

        }, 0);
    });
    $(grid_selector).jqGrid({
        url: CONTEXT_ROOT + '/proveedores/listar',
        datatype: 'json',
        mtype: 'GET',
        height: 310,
        hidegrid: false,
        rownumbers: true,
        //width: $(".content").width(),
        colNames: ['ID', 'NOMBRE', 'RUC', 'DIRECCION', 'TELEFONO', 'EMAIL', 'STATUS', ''],
        colModel: [
            {name: 'id', index: 'id', key: true, hidden: true, width: 60, sorttype: "int", editable: false},
            {name: 'nombre', index: 'nombre', width: 90, editable: false},
            {name: 'ruc', index: 'ruc', width: 90, editable: false},
            {name: 'direccion', index: 'direccion', width: 150, editable: false},
            {name: 'telefono', index: 'telefono', width: 90, sortable: false},
            {name: 'email', index: 'email', width: 90, sortable: false},
            {name: 'activo', index: 'activo', width: 90, editable: false},
            {name: 'act', index: 'act', fixed: true, sortable: false, resize: false
                        //formatter:'actions', 
                        //formatoptions:{ 
                        //	keys:true,
                        //delbutton: false,//disable delete button

                        //	delOptions:{recreateForm: true, beforeShowForm:beforeDeleteCallback},
                        //editformbutton:true, editOptions:{recreateForm: true, beforeShowForm:beforeEditCallback}
                        //}
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
            todos: false
        },
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
                    var estado = dato.activo;
                    if (estado === 'S') {
                        var labelActivo = '<span class="table-estado label label-success" value="S">Activo</span>';
                        if (isEditarInline) {

                            edit = editInlineButton(cl, permisoEditar);
                            $(grid_selector).setRowData(ids[i], {act: edit});

                        } else {

                            asignar = "";
                            visuali = visualizarButton(cl, permisoVisualizar,null);
                            editForm = editFormButton(cl, permisoEditar);
                            desact = desactivarButton(cl, permisoDesactivar);
                            $(grid_selector).setRowData(ids[i], {act: ini + editForm + asignar + visuali + desact + fin});
                        }
                        $(grid_selector).setRowData(ids[i], {activo: labelActivo});
                    } else {
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
                        visuali = visualizarButton(cl, permisoVisualizar,null);
                        editForm = editFormButton(cl, permisoEditar);
                        $(grid_selector).setRowData(ids[i], {act: ini + editForm + asignar + visuali + fin});
                    }
                }


            }
        },
        editurl: "/editar", //nothing is saved
        caption: "Proveedores"

    });
    $(window).triggerHandler('resize.jqGrid');
    $(grid_selector).jqGrid('setGridWidth', $(".content").width());
    $(grid_selector).jqGrid('navGrid', pager_selector, {edit: false, add: false, del: false, search: false});
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
            
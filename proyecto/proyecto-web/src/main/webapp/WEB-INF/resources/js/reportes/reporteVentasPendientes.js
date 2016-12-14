
$(document).ready(function(data) {

    $('#proveedor').searchableOptionList({
        data: CONTEXT_ROOT + "/clientes/listar?_search=false&todos=true&rows=10&page=1&sidx=&sord=asc",
        converter: function(sol, rawDataFromUrl) {
            var solData = [];
            var aSingleOptionItem = {
                // required attributes
                "type": "option",
                "label": "Todos",
                "value": "",
                // optional attributes
                "selected": false
            };
            solData.push(aSingleOptionItem);
            $.each(rawDataFromUrl.retorno, function() {

                var aSingleOptionItem = {
                    // required attributes
                    "type": "option",
                    "label": this['nombre'],
                    "value": this['id'],
                    // optional attributes
                    "selected": false
                };
                solData.push(aSingleOptionItem);
            });
            return solData;
        },
        maxHeight: '220px',
        events: {
            onChange: function(a) {
                $('#idProveedor').val(a.getSelection()[0].value);
            }
        }
    });

    var permisoEsalud = parseBolean($(this).find('.tablesalud-permiso').text());

    //Inicializar Fechas
    var hasta = new Date();
    var dia = hasta.getDate();
    var mes = hasta.getMonth() + 1;
    var anho = hasta.getFullYear();

    var fechaDesde = dia + "/" + mes + "/" + anho + " " + "00:00";
    var fechaHasta = dia + "/" + mes + "/" + anho + " " + "23:59";

    $('#date-timeDesde').datetimepicker({
        format: 'DD/MM/YYYY HH:mm',
        language: 'es',
        defaultDate: fechaDesde,
        maxDate: fechaHasta
    });

    $("#timepicker").timepicker({
        minuteStep: 1,
        secondStep: 5,
        maxHours: 1,
        maxMin: 15,
        defaultTime: '00:00:30',
        showDuration: true,
        showInputs: false,
        showSeconds: true,
        showMeridian: false
    });


    $('#date-timeHasta').datetimepicker({
        format: 'DD/MM/YYYY HH:mm',
        language: 'es',
        defaultDate: fechaHasta,
        useCurrent: false,
        maxDate: fechaHasta
    });


    $("#date-timeHasta").on("dp.change", function(e) {
        $('#date-timeDesde').data("DateTimePicker").setMaxDate(e.date);
    });

    filtrarReporte();

    var grid_selector = "#grid";
    var pager_selector = "#grid-pager";


    $(window).on('resize.jqGrid', function() {
        setTimeout(function() {
            $(grid_selector).jqGrid('setGridWidth', $(".widget-body").width());

        }, 0);
    });
    $(grid_selector).jqGrid({
        url: CONTEXT_ROOT + '/reportes/ventas/pendientes/listar',
        datatype: 'json',
        mtype: 'GET',
        height: 150,
        width: 1050,
        hidegrid: false,
        rownumbers: true,
        //width: $(".content").width(),
        colNames: ['NRO. FACTURA', 'FORMA PAGO', 'CANT. CUOTAS', 'PROVEEDOR', 'FECHA COMPRA', 'CUOTA PENDIENTE', 'IMPORTE', 'SALDO', 'NETO','ESTADO'],
        colModel: [
            {name: 'nroFactura', index: 'nroFactura', width: 100},
            {name: 'formaPago', index: 'formaPago', width: 100},
            {name: 'cantidadCuotas', index: 'cantidadCuotas', width: 100, editable: true},
            {name: 'proveedor.nombre', index: 'proveedor.nombre', width: 100},
            {name: 'fechaCompra', index: 'fechaCompra', width: 90},
            {name: 'cuota', index: 'cuota', width: 90, sortable: false},
            {name: 'importe', index: 'importe', formatter: 'number', width: 90, sortable: false},
            {name: 'saldo', index: 'saldo', formatter: 'number', width: 90, sortable: false},
            {name: 'neto', index: 'neto', formatter: 'number', width: 90, sortable: false},
            {name: 'estadoPago', index: 'estadoPago', width: 90, sortable: false}
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
            fechaInicio : $('#date-timeDesde').val().toString(),
            fechaFin : $('#date-timeHasta').val().toString(),
            estado: "PENDIENTE",
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
        editurl: "/editar", //nothing is saved
        caption: "Ventas",
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
                url: CONTEXT_ROOT + '/compra/detalles/listar?_search=false&todos=true&rows=10&page=1&sidx=&sord=asc&idCompra=' + row_id,
                datatype: 'json',
                mtype: 'GET',
                colNames: ['CODIGO', 'TIPO VEHICULO', 'MARCA', 'MODELO', 'ANHO', 'TRASMISION', 'MONEDA', 'PRECIO', 'NETO'],
                colModel: [
                    {name: "vehiculo.codigo", index: "vehiculo.codigo", width: 80, key: true},
                    {name: "vehiculo.tipo.nombre", index: "vehiculo.tipo.nombre", width: 130},
                    {name: "vehiculo.marca.nombre", index: "vehiculo.marca.nombre", width: 80, align: "right"},
                    {name: "vehiculo.modelo.nombre", index: "vehiculo.modelo.nombre", width: 80, align: "right"},
                    {name: "vehiculo.anho", index: "vehiculo.anho", width: 100, align: "right", sortable: false},
                    {name: "vehiculo.transmision", index: "vehiculo.transmision", width: 100, align: "right", sortable: false},
                    {name: "moneda.nombre", index: "moneda.nombre", width: 100, align: "right", sortable: false},
                    {name: "precio", index: "precio", width: 100, align: "right", formatter: 'number', sortable: false},
                    {name: "neto", index: "neto", width: 100, align: "right", formatter: 'number', sortable: false}
                ],
                height: '100%',
                rowNum: 10,
                sortname: 'num',
                sortorder: "asc",
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
                }
            });
        }

    });
    $(window).triggerHandler('resize.jqGrid');
    $(grid_selector).jqGrid('setGridWidth', $(".widget-body").width());
    $(grid_selector).jqGrid('navGrid', pager_selector, {edit: false, add: false, del: false, search: false});




    $('#recargar').click(function() {

        if (timeoutID !== null) {
            clearTimeout(timeoutID);
        }

        var entidad = {};
        entidad.tipo = 'ESALUD';
        entidad.servicio = '';
        entidad.esalud = $('#selectEntidad').val();
        entidad.remesas = '';

        var filters = [
            {nombre: 'fechaInicio', dato: $('#date-timeDesde').val().toString()},
            {nombre: 'fechaFin', dato: $('#date-timeHasta').val().toString()}
        ];

        var datos = {};
        datos.datos = filters;

        var enviar = {};
        enviar._search = true;
        enviar.entidad = JSON.stringify(entidad);
        enviar.filters = JSON.stringify(datos);

        var jqXHR = $.get(CONTEXT_ROOT + '/datos/reportes', enviar, function(data, textStatus, jqXHR) {
            if (data.error) {
                $('#mensaje').append('<div class="alert alert-error">'
                        + '<button class="close" data-dismiss="alert" type="button"'
                        + '><i class="icon-remove"></i></button>'
                        + '<strong>Error al obtener reportes</strong>'
                        + 'Error con el registro.'
                        + '</div>');

            } else {
                verificarDatos(data.eSalud.reporte);
                cargarValorReporte(data.eSalud.total);
                detalleServicios("Rechazado", "ESALUD", "#DA5430", "barChartRechazado", true);
                detalleServicios("Time Out", "ESALUD", "#FEE074", "barChartTime", true);
            }

        });

        jqXHR.fail(function(jqXHR, textStatus, errorThrown) {
            $('#mensaje').append('<div class="alert alert-error">'
                    + '<button class="close" data-dismiss="alert" type="button"'
                    + '><i class="icon-remove"></i></button>'
                    + '<strong>Error! </strong>'
                    + 'Error con el registro.'
                    + '</div>');
        });
    });

});

function filtrar() {
    $("#filtroReporteTiempo").slideUp();
    $("#filtroReporte").slideToggle();
}


function parseBolean(val) {
    if (val.toLowerCase() === 'true') {
        return true;
    } else if (val.toLowerCase() === 'false' || val.toLowerCase() === '') {
        return false;
    }

}


function filtrarReporte() {

    var chart = new CanvasJS.Chart("movimiento",
            {
                theme: "theme2",
                title: {
                    text: "Ventas Pendientes"
                },
                data: [
                    {
                        type: "pie",
                        showInLegend: true,
                        toolTipContent: "{y} - #percent %",
                        yValueFormatString: "#,##0,,.## Million",
                        legendText: "{indexLabel}"
                    }
                ]
            });

    var enviar = {};
    enviar.fechaInicio = $('#date-timeDesde').val().toString();
    enviar.fechaFin = $('#date-timeHasta').val().toString();
    enviar.estado = $('#idProveedor').val().toString();


    setTimeout(function() {
        var rules = [];

        rules.push({
            field: "Proveedor",
            op: "cn",
            data: $('#idProveedor').val().toString()
        });

        var postData = $("#grid").jqGrid("getGridParam", "postData");

        postData.filters = JSON.stringify({
            groupOp: "AND",
            rules: rules,
            data: $('#idProveedor').val().toString()
        });

        postData.fechaInicio = $('#date-timeDesde').val().toString();
        postData.fechaFin = $('#date-timeHasta').val().toString();

        $("#grid").jqGrid("setGridParam", {search: true});
        $("#grid").trigger("reloadGrid", [{page: 1, current: true}]);

    }, 0);

    var jqXHR = $.get(CONTEXT_ROOT + '/reportes/grafico/ventas/pendientes', enviar, function(data, textStatus, jqXHR) {
        if (data.error) {
            $('#mensaje').append('<div class="alert alert-error">'
                    + '<button class="close" data-dismiss="alert" type="button"'
                    + '><i class="icon-remove"></i></button>'
                    + '<strong>Error! </strong>'
                    + 'Error al obtener el reporte.'
                    + '</div>');
        } else {
            console.log(chart);
            chart.options.data[0].dataPoints = data.data;
            chart.render();
        }

    });

    jqXHR.fail(function(jqXHR, textStatus, errorThrown) {
        $('#mensaje').append('<div class="alert alert-error">'
                + '<button class="close" data-dismiss="alert" type="button"'
                + '><i class="icon-remove"></i></button>'
                + '<strong>Error! </strong>'
                + 'Error con el registro.'
                + '</div>');
    });


}

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




















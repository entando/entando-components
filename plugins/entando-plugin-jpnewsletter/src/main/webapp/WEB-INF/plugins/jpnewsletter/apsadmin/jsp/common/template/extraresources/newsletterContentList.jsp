<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<script src="<wp:resourceURL />administration/js/data-tables/jquery.dataTables.min.js"></script>
<script src="<wp:resourceURL />administration/js/data-tables/dataTables.fixedColumns.min.js"></script>
<script src="<wp:resourceURL />administration/js/data-tables/dataTables.colVis.min.js"></script>

<script type="text/javascript">
    $(document).ready(function () {

        /* DataTables con ColVis e FixeColumns */
        var table = $('#contentListTable').DataTable({
            dom: 'Ct',
            "bSort": false,
            scrollY: false,
            scrollX: true,
            scrollCollapse: true,
            paging: false,
            "colVis": {
                "buttonText": '<s:text name="title.searchResultOptions" />&#32;',
                "sAlign": "right"
            },
            columnDefs: [
                {width: 50, targets: 0},
                {width: 200, "targets": [1, 2, 3, 4, 5, 6, 7], },
                {width: 100, "targets": [8, 9], }

            ],
            fixedColumns: {
                leftColumns: 2,
                rightColumns: 1,
            }
        });

        /* Selezione colonne tabella visibili */
        $(".ColVis_Button").addClass("btn btn-primary dropdown-toggle").click(function () {
            $(this).parent(".ColVis").addClass("btn-group open");
            $(".ColVis_collection label").addClass("checkbox");
            $(".ColVis_collectionBackground, .ColVis_catcher").click(function () {
                $(".ColVis").removeClass("open");
            });
        });


        /* Selezione multipla elementi della tabella */

        var itemsNum = $('.DTFC_LeftBodyLiner .content-list tbody input[type="checkbox"]').length;
        $(".js_selectAll").click(function () {
            $(".selectall-box").toggleClass("hidden");
            var isChecked = ($(this).prop("checked") == true);
            if (isChecked) {
                $(".DTFC_LeftBodyLiner .content-list tbody input").prop("checked", true);
            } else {
                $(".DTFC_LeftBodyLiner .content-list tbody input").prop("checked", false);
            }
            updateCounter();
        });
        $('.DTFC_LeftBodyLiner .content-list tbody input[type="checkbox"]').click(function () {
            var selectedItemsNum = updateCounter();
            if (itemsNum == selectedItemsNum) {
                $(".js_selectAll").prop("checked", true);
                $(".selectall-box").removeClass("hidden");
            } else {
                $(".js_selectAll").prop("checked", false);
                $(".selectall-box").addClass("hidden");
            }
        });


        /* Fix per dropdown in FixedColumns */
        $('.dropdown.dropdown-kebab-pf').on('show.bs.dropdown', function () {
            var dropdownMenu = $(this).parent().find(".dropdown-menu").clone();

            $('body').append(dropdownMenu.addClass("clone"));

            var eOffset = $($(this)).offset();

            dropdownMenu.css({
                'display': 'block',
                'top': eOffset.top + $($(this)).outerHeight(),
            });
        });

        $(window).on('hide.bs.dropdown', function (e) {
            $(".clone").remove();
        });
    });
</script>

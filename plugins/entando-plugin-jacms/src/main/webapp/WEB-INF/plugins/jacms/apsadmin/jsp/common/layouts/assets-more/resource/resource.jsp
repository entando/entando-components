<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/css/bootstrap.vertical-tabs.min.css"/>
<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/cropper-js/cropper.min.css"/>
<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/crop-editor/crop-editor.css"/>

<script src="<wp:resourceURL />administration/js/jquery.sticky.js"></script>
<script src="<wp:resourceURL />administration/js/data-tables/jquery.dataTables.min.js"></script>
<script src="<wp:resourceURL />administration/js/data-tables/dataTables.fixedColumns.min.js"></script>
<script src="<wp:resourceURL />administration/js/data-tables/dataTables.colVis.min.js"></script>

<script src="<wp:resourceURL />administration/js/generate-code-from-title.js"></script>

<script type="text/javascript">
    $(document).ready(function () {

        var treeStyle = '<wp:info key="systemParam" paramName="treeStyle_category" />';
        var isTreeOnRequest = (treeStyle === 'request') ? true : false;
        $('.table-treegrid').treegrid(null, isTreeOnRequest);
        $(".treeRow ").on("click", function (event) {
            $(".treeRow").removeClass("active");
            $(this).find('.subTreeToggler').prop("checked", true);
            $(this).addClass("active");
        });

        $("#expandAll").click(function () {
            $('#categoryTree .treeRow').removeClass('hidden');
            $('#categoryTree .treeRow').removeClass('collapsed');
            $('#categoryTree .icon.fa-angle-right').removeClass('fa-angle-right').addClass('fa-angle-down');
        });

        $("#collapseAll").click(function () {
            $('#categoryTree .treeRow:not(:first-child)').addClass('hidden');
            $('#categoryTree .treeRow').addClass('collapsed');
            $('#categoryTree .icon.fa-angle-down').removeClass('fa-angle-down').addClass('fa-angle-right');
        });
    });
</script>

<script src="<wp:resourceURL />administration/file-uploader/file-uploader.js"></script>
<script src="<wp:resourceURL />administration/cropper-js/cropper.min.js"></script>

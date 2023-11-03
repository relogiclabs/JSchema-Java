$(document).ready(function() {
    $(".menu-checkbox").change(function() {
        if (this.checked) {
            $(".sidebar").css("left", "0");
            $(".overlay").css("display", "block");
        } else {
            $(".sidebar").css("left", "");
            $(".overlay").css("display", "none");
        }
    });

    $(".overlay").click(function() {
        $(".menu-checkbox").prop("checked", false).trigger("change");
    });
})
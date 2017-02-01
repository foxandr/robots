/**
 * Created by fox on 18.01.17.
 */

var ajaxUrl = "ajax/";

function getRobotsName() {
    return $.ajax({
        type: "GET",
        url: ajaxUrl + "get",
        async: false,
        error: function (response) {
            failNoty(response);
        }
    });
}

function getNewLogs() {
    $.ajax({
        type: "GET",
        url: ajaxUrl + "logs",
        async: true,
        success: function (result) {
            var str = "";
            $.each(result, function(key, value) {
                 str = value + "\n" + str;
            });
            $("#logs").val(str);
        },
        error: function (response) {
            failNoty(response);
        }
    });
}

function sendAjax(windowName, ajaxEnding, form) {
    $.ajax({
        type: "POST",
        url: ajaxUrl + ajaxEnding,
        data: $("#" + form).serialize(),
        success: function () {
            successNoty("The task was sent.");
        }
    });
    $("#" + windowName).modal("hide");
}

/**
 * Created by fox on 18.01.17.
 */

var ajaxUrl = "ajax/";

function getRobotsName() {
    return $.ajax({
        type: "GET",
        url: ajaxUrl + "robots/all/",
        async: false,
        error: function (response) {
            failNoty(response);
        }
    });
}

function getNewLogs() {
    $.ajax({
        type: "GET",
        url: ajaxUrl + "logs/",
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

function sendPersonal() {
    $.ajax({
        type: "PUT",
        url: ajaxUrl + "robots/" + $("#prsTask select[name=name]").val() + "/tasks/" + $("#prsTask select[name=id]").val() + "/",
        async: false,
        success: function () {
            successNoty("The task was sent.");
        }
    });
    $("#sendPersonalTask").modal("hide");
}

function sendBroadcast() {
    $.ajax({
        type: "PUT",
        url: ajaxUrl + "robots/all/tasks/" + $("#brdTask select[name=id]").val() + "/",
        async: false,
        success: function () {
            successNoty("The task was sent.");
        }
    });
    $("#sendBroadcastTask").modal("hide");
}

function addRobot() {
    $.ajax({
        type: "POST",
        url: ajaxUrl + "robots/",
        data: $("#add").serialize(),
        success: function () {
            successNoty("The task was sent.");
        }
    });
    $("#addRobot").modal("hide");
}

function killRobot() {
    $.ajax({
        type: "DELETE",
        url: ajaxUrl + "robots/" + $("#suicide").val() + "/",
        async: false,
        success: function () {
            successNoty("The task was sent.");
        }
    });
    $("#killRobot").modal("hide");
}

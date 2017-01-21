/**
 * Created by fox on 18.01.17.
 */

$(document).ready(function(){
    getNewLogs();
    setInterval('getNewLogs()',1000);
});

function show(lang) {
    $(location).attr('href', window.location.href.split("?")[0] + "?lang=" + $("#chLang").val());
}

function openWindow(taskName, selector) {
    var result = $.parseJSON(getRobotsName()["responseText"]);
    if (result){
        var sel = $("#" + selector);
        sel.find("option").remove();
        $.each(result, function(key, value) {
            sel.append($("<option></option>")
                    .attr("value", value)
                    .text(value));
        });
        $("#"+ taskName).modal();
    }
}

function openNonSelectorWindow(taskName) {
    $("#"+ taskName).modal();
}

function submitForm(windowName, formName, ajaxEnding) {
    form = $("#" + formName);

    form.submit(function () {
        sendAjax(windowName, ajaxEnding, formName);
        return false;
    });

    $(document).ajaxError(function (event, jqXHR, options, jsExc) {
        failNoty(event, jqXHR, options, jsExc);
    });
}



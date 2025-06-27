$(document).ready(function(){
    $("#buttonCancel").on("click", function(){
        window.location = moduleURL;
    });
    $("#fileImage").change(function (){
        if(!checkFileSize(this)){
            return;
        }
        showImageThumbnail(this);
    });
});

function showImageThumbnail(fileInput){
    var file = fileInput.files[0];
    var reader = new FileReader();
    reader.onload = function (e){
        $("#thumbnail").attr("src", e.target.result);
    };
    reader.readAsDataURL(file);
}

function checkFileSize(fileInput){
    fileSize = fileInput.files[0].size;
    if(fileSize > MAX_FILE_SIZE){
        fileInput.setCustomValidity("You must choose an image less than" + MAX_FILE_SIZE + "bytes");
        fileInput.reportValidity();
        return false;
    }
    else{
        fileInput.setCustomValidity("");
        return true;
    }
}
function checkEmailUnique(form){
    url = "[[@{/users/check_email}]]";
    userEmail = $("#email").val();
    userId = $("#id").val();
    csrfValue = $("input[name = '_csrf']").val();
    params = {id: userId, email: userEmail, _csrf: csrfValue};

    $.post(url, params, function (response){
        if(response == "OK"){
            form.submit();
        }else if(response == "Duplicated"){
            showModalDialog("Warning","There is another user having the email "+ userEmail);
        }else{
            showModalDialog("Error", "Unknown response from server");
        }
    }).fail(function (){
        showModalDialog("Error", "Cound not connect to server");
    });
    return false;
}
function showModalDialog(title, message){
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
    $("#modalDialog").modal();
}
function showErrorModal(message){
    showModalDialog("Error", message);
}
function showWarningModal(message){
    showModalDialog("Warning", message);
}